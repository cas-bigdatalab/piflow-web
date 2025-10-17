package cn.cnic.component.schedule.listener.Kafka;

import cn.cnic.base.utils.AESUtils;
import cn.cnic.base.utils.JsonUtils;
import cn.cnic.common.Eunm.MessageProtocol;
import cn.cnic.component.schedule.entity.MessageTriggerTaskDefinition;
import cn.cnic.component.schedule.listener.IMessageListener;
import cn.cnic.component.schedule.processor.IMessageProcessor;
import cn.cnic.component.schedule.service.Impl.MessageSourceConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.TimeoutException;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.MDC;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
@Component
@Scope("prototype")
public class KafkaMessageListener implements IMessageListener {

    private MessageTriggerTaskDefinition definition;
    private IMessageProcessor messageProcessor;
    private KafkaConfig kafkaConfig;
    private Properties mainConsumerProps; // [MODIFIED] 主消费者的配置

    private volatile KafkaConsumer<String, String> mainConsumer; // [MODIFIED] 主消费者，用 volatile 保证可见性
    private KafkaConsumer<String, String> healthCheckConsumer; // [NEW] 健康检查专用消费者

    private ExecutorService consumerExecutor;
    private ScheduledExecutorService healthCheckExecutor;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final MessageSourceConfigService messageSourceConfigService;


    private final Map<TopicPartition, NavigableSet<Long>> completedOffsets = new ConcurrentHashMap<>();
    private final Map<TopicPartition, Long> lastCommittedOffsets = new ConcurrentHashMap<>();


    public KafkaMessageListener(MessageSourceConfigService messageSourceConfigService) {
        this.messageSourceConfigService = messageSourceConfigService;
    }

    @Override
    public void init(MessageTriggerTaskDefinition definition) throws Exception {
        this.definition = definition;
        if (definition.getProperties() == null) {
            throw new IllegalArgumentException("Kafka properties are not configured.");
        }

        this.kafkaConfig = JsonUtils.toObject(definition.getProperties(), KafkaConfig.class);
        log.info("[KafkaListener-INIT] 成功将 properties 转换为 KafkaConfig. sourceId={}", definition.getId());
        
        // 解析高级配置
        if (definition.getAdvancedConfig() != null) {
            try {
                this.kafkaConfig.setAdvancedConfig(JsonUtils.toObject(definition.getAdvancedConfig(), KafkaAdvancedConfig.class));
                log.info("[KafkaListener-INIT] 成功解析 KafkaAdvancedConfig. sourceId={}", definition.getId());
            } catch (Exception e) {
                log.error("[KafkaListener-INIT-ERROR] 转换 KafkaAdvancedConfig 失败，sourceId={}，错误：{}", definition.getId(), e.getMessage(), e);
                throw new RuntimeException("Kafka 高级配置解析失败", e);
            }
        }

        //基础配置
        Properties baseProps = new Properties();
        baseProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        baseProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        baseProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        baseProps.put(ConsumerConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, 5000); // 用于API调用的默认超时

        // 只有在用户名和密码都配置且不为空时才启用SASL认证
        if (StringUtils.isNotBlank(kafkaConfig.getUsername()) && StringUtils.isNotBlank(kafkaConfig.getPassword())) {
            String jaasConfig = String.format(
                    "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";",
                    kafkaConfig.getUsername(),
                    AESUtils.aesDecrypt(kafkaConfig.getPassword())
            );
            baseProps.put("sasl.jaas.config", jaasConfig);
            baseProps.put("security.protocol", "SASL_PLAINTEXT");
            baseProps.put("sasl.mechanism", "PLAIN");
            log.info("[KafkaListener-INIT] 启用SASL认证, username={}, sourceId={}", 
                    kafkaConfig.getUsername(), definition.getId());
        } else {
            log.info("[KafkaListener-INIT] 未配置认证信息，使用无认证模式, sourceId={}", definition.getId());
        }
        
        // 根据KafkaAdvancedConfig设置高级配置
        KafkaAdvancedConfig advancedConfig = kafkaConfig.getAdvancedConfig();
        if (advancedConfig != null) {
            // 参数校验
            validateKafkaAdvancedConfig(advancedConfig);
            
            // 设置请求超时时间
            if (advancedConfig.getRequestTimeout() != null) {
                baseProps.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, advancedConfig.getRequestTimeout());
                log.info("[KafkaListener-INIT] 设置请求超时时间: {}毫秒, sourceId={}", 
                        advancedConfig.getRequestTimeout(), definition.getId());
            }
            
            // 设置重连退避最大时间
            if (advancedConfig.getReconnectBackoffMax() != null) {
                baseProps.put(ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, advancedConfig.getReconnectBackoffMax());
                log.info("[KafkaListener-INIT] 设置重连退避最大时间: {}毫秒, sourceId={}", 
                        advancedConfig.getReconnectBackoffMax(), definition.getId());
            }
            
            // 设置单次poll最大记录数
            if (advancedConfig.getMaxPollRecords() != null) {
                baseProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, advancedConfig.getMaxPollRecords());
                log.info("[KafkaListener-INIT] 设置单次poll最大记录数: {}, sourceId={}", 
                        advancedConfig.getMaxPollRecords(), definition.getId());
            }
            
            // 设置最大poll间隔时间
            if (advancedConfig.getMaxPollInterval() != null) {
                baseProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, advancedConfig.getMaxPollInterval());
                log.info("[KafkaListener-INIT] 设置最大poll间隔时间: {}毫秒, sourceId={}", 
                        advancedConfig.getMaxPollInterval(), definition.getId());
            }
            
            // 设置会话超时时间
            if (advancedConfig.getSessionTimeout() != null) {
                baseProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, advancedConfig.getSessionTimeout());
                log.info("[KafkaListener-INIT] 设置会话超时时间: {}毫秒, sourceId={}", 
                        advancedConfig.getSessionTimeout(), definition.getId());
            }
            
            // 设置心跳间隔时间
            if (advancedConfig.getHeartbeatInterval() != null) {
                baseProps.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, advancedConfig.getHeartbeatInterval());
                log.info("[KafkaListener-INIT] 设置心跳间隔时间: {}毫秒, sourceId={}", 
                        advancedConfig.getHeartbeatInterval(), definition.getId());
            }
            
            log.info("[KafkaListener-INIT] 高级配置已应用, sourceId={}", definition.getId());
        } else {
            log.info("[KafkaListener-INIT] 未配置高级参数，使用默认设置, sourceId={}", definition.getId());
        }

        // --- 1. 为主消费者准备专属配置 ---
        this.mainConsumerProps = new Properties();
        this.mainConsumerProps.putAll(baseProps);
        this.mainConsumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfig.getGroupId()); // 真实的 group.id
        if (StringUtils.isNotBlank(kafkaConfig.getAutoOffsetReset())) {
            this.mainConsumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConfig.getAutoOffsetReset());
        }
        this.mainConsumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");


        // --- 2. 为健康检查消费者创建独立的配置和实例 ---
        Properties healthCheckProps = new Properties();
        healthCheckProps.putAll(baseProps);
        // [CRITICAL] 必须设置一个随机、唯一的 group.id，以避免干扰主消费组
        healthCheckProps.put(ConsumerConfig.GROUP_ID_CONFIG, "health-check-" + definition.getId() + "-" + UUID.randomUUID());
        this.healthCheckConsumer = new KafkaConsumer<>(healthCheckProps);

        log.info("[KafkaListener-INIT] Kafka properties and health-check consumer initialized for sourceId={}", definition.getId());
    }

    /**
     * 校验Kafka高级配置参数的合理性
     * @param advancedConfig Kafka高级配置
     */
    private void validateKafkaAdvancedConfig(KafkaAdvancedConfig advancedConfig) {
        // 1. 心跳间隔必须小于会话超时时间
        if (advancedConfig.getHeartbeatInterval() != null && advancedConfig.getSessionTimeout() != null) {
            if (advancedConfig.getHeartbeatInterval() >= advancedConfig.getSessionTimeout()) {
                String errorMsg = String.format("心跳间隔(%dms)必须小于会话超时时间(%dms)", 
                        advancedConfig.getHeartbeatInterval(), advancedConfig.getSessionTimeout());
                log.error("[KafkaListener-VALIDATE] 参数校验失败: {}", errorMsg);
                throw new IllegalArgumentException("Kafka配置参数校验失败: " + errorMsg);
            }
        }
        
        // 2. 会话超时时间必须小于最大poll间隔时间
        if (advancedConfig.getSessionTimeout() != null && advancedConfig.getMaxPollInterval() != null) {
            if (advancedConfig.getSessionTimeout() >= advancedConfig.getMaxPollInterval()) {
                String errorMsg = String.format("会话超时时间(%dms)必须小于最大poll间隔时间(%dms)", 
                        advancedConfig.getSessionTimeout(), advancedConfig.getMaxPollInterval());
                log.error("[KafkaListener-VALIDATE] 参数校验失败: {}", errorMsg);
                throw new IllegalArgumentException("Kafka配置参数校验失败: " + errorMsg);
            }
        }
        
        // 3. 请求超时时间必须小于会话超时时间
        if (advancedConfig.getRequestTimeout() != null && advancedConfig.getSessionTimeout() != null) {
            if (advancedConfig.getRequestTimeout() >= advancedConfig.getSessionTimeout()) {
                String errorMsg = String.format("请求超时时间(%dms)必须小于会话超时时间(%dms)", 
                        advancedConfig.getRequestTimeout(), advancedConfig.getSessionTimeout());
                log.error("[KafkaListener-VALIDATE] 参数校验失败: {}", errorMsg);
                throw new IllegalArgumentException("Kafka配置参数校验失败: " + errorMsg);
            }
        }
        
        // 4. 数值范围校验
        if (advancedConfig.getHeartbeatInterval() != null && advancedConfig.getHeartbeatInterval() < 100) {
            log.warn("[KafkaListener-VALIDATE] 心跳间隔时间过小({}ms)，建议设置为100ms以上", advancedConfig.getHeartbeatInterval());
        }
        
        if (advancedConfig.getSessionTimeout() != null && advancedConfig.getSessionTimeout() < 1000) {
            log.warn("[KafkaListener-VALIDATE] 会话超时时间过小({}ms)，建议设置为1000ms以上", advancedConfig.getSessionTimeout());
        }
        
        if (advancedConfig.getMaxPollRecords() != null && advancedConfig.getMaxPollRecords() > 5000) {
            log.warn("[KafkaListener-VALIDATE] 单次poll记录数过大({})，可能影响性能，建议设置为5000以下", advancedConfig.getMaxPollRecords());
        }
        
        log.info("[KafkaListener-VALIDATE] Kafka高级配置参数校验通过, sourceId={}", definition.getId());
    }

    @Override
    public void setMessageProcessor(IMessageProcessor processor) {
        this.messageProcessor = processor;
    }

    @Override
    public synchronized void startListening() {
        if (!running.compareAndSet(false, true)) {
            log.warn("[KafkaListener-START-SKIP] Kafka listener already running for sourceId={}", definition.getId());
            return;
        }

        log.info("[KafkaListener-START] Starting Kafka listener for sourceId={}", definition.getId());

        int threadPoolSize = 5;
        consumerExecutor = Executors.newFixedThreadPool(
                threadPoolSize,
                r -> new Thread(r, "KafkaConsumer-" + definition.getId())
        );
        // 主消费线程在独立线程中运行
        consumerExecutor.execute(this::runConsumerLoop);

        startHealthChecker();
    }

    private void runConsumerLoop() {
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(this.mainConsumerProps)) {
            this.mainConsumer = consumer;
            consumer.subscribe(Collections.singletonList(kafkaConfig.getTopicName()));
            log.info("[KafkaListener-RUN] 开始监听 Kafka 主题: {}...", kafkaConfig.getTopicName());

            while (running.get()) {
                try {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
                    if (records.isEmpty()) {
                        // 如果没有拉到新消息，仍然尝试提交一次，处理那些已完成但未提交的位移
                        commitOffsets(consumer);
                        continue;
                    }
                    for (ConsumerRecord<String, String> record : records) {
                        // 为每个分区初始化位移跟踪器
                        TopicPartition tp = new TopicPartition(record.topic(), record.partition());
                        completedOffsets.computeIfAbsent(tp, k -> new ConcurrentSkipListSet<>());

                        final long offsetToComplete = record.offset();

                        Runnable ackAction = () -> {
                            // 任务完成后，只记录该 offset 已完成
                            completedOffsets.get(tp).add(offsetToComplete);
                        };

                        consumerExecutor.submit(() -> processRecord(record, ackAction));
                    }
                    commitOffsets(consumer);
                } catch (WakeupException e) {
                    log.info("[KafkaListener-RUN] Consumer poll loop awakened for shutdown.");
                    break;
                }
            }
        } catch (Exception e) {
            log.error("[KafkaListener-RUN-FATAL] Kafka 监听器主循环发生严重错误，监听器将停止. sourceId={}, error={}", definition.getId(), e.getMessage(), e);
            definition = updateTaskStatus(definition, "INACTIVE"); // 更新状态为失败
            throw new RuntimeException("Kafka监听器启动失败", e); // 关键：抛出异常
        } finally {
            this.mainConsumer = null; // 清理引用
            log.info("[KafkaListener-RUN-FINISH] Kafka 消费线程已结束. sourceId={}", definition.getId());
            // 如果是因为错误而终止，确保 running 状态也为 false
            running.set(false);
        }
    }

    private void processRecord(ConsumerRecord<String, String> record, Runnable ackAction) {
        String triggerInstanceId = UUID.randomUUID().toString();
        try {
            MDC.put("triggerInstanceId", triggerInstanceId);
            MDC.put("sourceId", String.valueOf(definition.getId()));
            MDC.put("workflowId", definition.getTargetWorkflowId());
            MDC.put("messageId", record.key() + "_" + System.currentTimeMillis());

            log.debug("接收到消息: topic={}, partition={}, offset={}, key={}, value={}",
                    record.topic(), record.partition(), record.offset(), record.key(), record.value());

            KafkaRawMessageWrapper rawMessage = new KafkaRawMessageWrapper(record);
            messageProcessor.process(definition, rawMessage, String.valueOf(record), triggerInstanceId, ackAction);
        } catch (Exception e) {
            log.error("处理 Kafka 消息时发生错误: error={}", e.getMessage(), e);
        } finally {
            MDC.clear();
        }
    }

    private void startHealthChecker() {
        healthCheckExecutor = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "KafkaHealthCheck-" + definition.getId()));
        // 初始延迟5秒，之后每10秒检查一次
        healthCheckExecutor.scheduleAtFixedRate(() -> {
            if (!running.get() || this.healthCheckConsumer == null) {
                return;
            }
            try {
                // [MODIFIED] 使用独立的 healthCheckConsumer 进行检查
                healthCheckConsumer.listTopics(Duration.ofSeconds(5));
                // 如果请求成功，并且当前状态不是 ACTIVE，则更新为 ACTIVE
                if (!"ACTIVE".equals(definition.getStatus())) {
                    definition = updateTaskStatus(definition, "ACTIVE");
                }
            } catch (TimeoutException e) {
                // 如果请求超时，说明连接中断，客户端正在内部重连
                log.warn("[KafkaListener-HEALTH-CHECK] Health check failed (Timeout), connection lost. Client is recovering... sourceId={}", definition.getId());
                definition = updateTaskStatus(definition, "RECONNECTING");
            } catch (Exception e) {
                // 其他异常也可能意味着连接问题
                log.error("[KafkaListener-HEALTH-CHECK] Health check encountered an unexpected error. sourceId={}", definition.getId(), e);
                definition = updateTaskStatus(definition, "RECONNECTING");
            }
        }, 5, 10, TimeUnit.SECONDS);
    }

    @Override
    public synchronized void stopListening() {
        if (!running.compareAndSet(true, false)) {
            log.warn("[KafkaListener-STOP-SKIP] Kafka listener already stopped for sourceId={}", definition.getId());
            return;
        }

        log.info("[KafkaListener-STOP] Stopping Kafka listener for sourceId={}", definition.getId());

        // 优雅地关闭健康检查器
        if (healthCheckExecutor != null) {
            healthCheckExecutor.shutdown();
        }
        if (healthCheckConsumer != null) {
            try {
                healthCheckConsumer.close(Duration.ofSeconds(5));
            } catch (Exception e) {
                log.error("Error closing health check consumer.", e);
            }
        }

        // 唤醒 consumer.poll() 使其立即退出循环
        if (mainConsumer != null) {
            mainConsumer.wakeup();
        }

        // 优雅地关闭主线程池
        if (consumerExecutor != null) {
            consumerExecutor.shutdown();
            try {
                if (!consumerExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    consumerExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                consumerExecutor.shutdownNow();
            }finally {
                this.completedOffsets.clear();
                this.lastCommittedOffsets.clear();
                log.info("[KafkaListener-STOP] 已清理内存中的位移跟踪状态。sourceId={}", definition.getId());
            }
        }
    }

    @Override
    public Long getMessageSourceId() {
        return definition != null ? definition.getId() : null;
    }

    @Override
    public String getProtocolType() {
        return MessageProtocol.KAFKA.getName();
    }

    @Override
    public MessageTriggerTaskDefinition getDefinition() {
        return definition;
    }

    private MessageTriggerTaskDefinition updateTaskStatus(MessageTriggerTaskDefinition definition, String status) {
        // 如果当前状态与目标状态相同，则不进行更新，避免不必要的数据库写入
        if (status.equals(definition.getStatus())) {
            return definition;
        }

        Long id = definition.getId();
        Date updateTime = new Date(System.currentTimeMillis());

        int affectedRows = messageSourceConfigService.updateStatusById(id, status, updateTime);
        if (affectedRows > 0) {
            definition.setStatus(status);
            definition.setUpdateTime(updateTime);
            log.info("[MS_SERVICE_UPDATE_STATUS_SUCCESS] 定义 {} 状态更新为 {}。", id, status);
            return definition;
        }
        log.error("[MS_SERVICE_UPDATE_STATUS_FAIL] 定义 {} 状态更新为 {} 失败，数据库更新失败。", id, status);
        return definition;
    }

    private void commitOffsets(KafkaConsumer<String, String> consumer) {
        Map<TopicPartition, OffsetAndMetadata> offsetsToCommit = new HashMap<>();

        for (Map.Entry<TopicPartition, NavigableSet<Long>> entry : completedOffsets.entrySet()) {
            TopicPartition tp = entry.getKey();
            NavigableSet<Long> completed = entry.getValue();

            // 获取该分区上次提交的位移，如果没有则从-1开始
            long lastCommitted = lastCommittedOffsets.getOrDefault(tp, -1L);
            long nextOffsetToCommit = lastCommitted + 1;

            // 关键：从上一个提交的位移开始，连续地查找已完成的位移
            while (completed.contains(nextOffsetToCommit)) {
                nextOffsetToCommit++;
            }

            // 如果找到了新的可以提交的连续位移
            if (nextOffsetToCommit > lastCommitted + 1) {
                long commitUpTo = nextOffsetToCommit;
                offsetsToCommit.put(tp, new OffsetAndMetadata(commitUpTo));

                // 清理掉已经包含在提交范围内的旧位移，防止内存无限增长
                completed.removeIf(offset -> offset < commitUpTo);
            }
        }

        if (!offsetsToCommit.isEmpty()) {
            consumer.commitSync(offsetsToCommit);
            // 更新我们内存中记录的已提交位移
            offsetsToCommit.forEach((tp, metadata) -> lastCommittedOffsets.put(tp, metadata.offset() -1));

            log.info("[KAFKA_COMMIT] 成功提交连续位移. topic={}, offsets={}",
                    kafkaConfig.getTopicName(), offsetsToCommit);
        }
    }
}