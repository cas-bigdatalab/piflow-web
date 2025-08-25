package cn.cnic.component.schedule.listener.RabbitMQ;

import cn.cnic.base.utils.AESUtils;
import cn.cnic.common.Eunm.MessageProtocol;
import cn.cnic.component.schedule.entity.MessageTriggerTaskDefinition;
import cn.cnic.component.schedule.listener.IMessageListener;
import cn.cnic.component.schedule.processor.IMessageProcessor; // 引入消息处理器接口
import cn.cnic.component.schedule.service.Impl.MessageSourceConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*; // RabbitMQ客户端库
import com.rabbitmq.client.impl.recovery.AutorecoveringConnection;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * RabbitMQ消息监听器实现。
 * 每个实例管理一个 RabbitMQ 连接和消费通道。
 */
@Log4j2
@Component
@Scope("prototype") // 每次从Spring容器获取时都创建新的实例
public class RabbitMQMessageListener implements IMessageListener {

    private MessageTriggerTaskDefinition definition; // 存储当前监听器的配置
    private IMessageProcessor messageProcessor; // 消息处理器

    private RabbitMQConfig rabbitMQConfig;
    private Connection connection;
    private Channel channel;
    private String consumerTag;
    private ExecutorService consumerExecutor; // 用于消费消息的独立线程池

    private ConnectionFactory rabbitConnectionFactory;

    private final MessageSourceConfigService messageSourceConfigService;

    public RabbitMQMessageListener(MessageSourceConfigService messageSourceConfigService) {
        this.messageSourceConfigService = messageSourceConfigService;
    }

    /**
     * 初始化监听器配置。
     * @param definition 消息触发任务的定义
     */
    @Override
    public void init(MessageTriggerTaskDefinition definition) throws Exception {
        this.definition = definition;
        // 将 properties Map 转换为 RabbitMQConfig 对象
        if (definition.getProperties() != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                this.rabbitMQConfig = objectMapper.readValue(definition.getProperties(), RabbitMQConfig.class);
                if (StringUtils.isNoneEmpty(this.rabbitMQConfig.getAdvancedConfig())) {
                    this.rabbitMQConfig.setRabbitMQAdvancedConfig(objectMapper.readValue(this.rabbitMQConfig.getAdvancedConfig(), RabbitMQAdvancedConfig.class));
                }
                log.info("[RabbitMQListener-INIT] 成功将 properties 转换为 RabbitMQConfig. sourceId={}", definition.getId());
            } catch (Exception e) {
                log.error("[RabbitMQListener-INIT-ERROR] 转换 RabbitMQConfig 失败，sourceId={}，错误：{}", definition.getId(), e.getMessage(), e);
                // 根据业务需求，这里可以选择抛出运行时异常或标记为无效配置
                throw new RuntimeException("RabbitMQ 配置解析失败", e);
            }
        } else {
            log.warn("[RabbitMQListener-INIT] properties 为空，无法初始化 RabbitMQ 配置。sourceId={}", definition.getId());
            throw new IllegalArgumentException("RabbitMQ 配置不能为空");
        }
        // 配置 ConnectionFactory
        if (rabbitConnectionFactory == null) {
            rabbitConnectionFactory = new ConnectionFactory();
        }
        rabbitConnectionFactory.setHost(rabbitMQConfig.getHost());
        rabbitConnectionFactory.setPort(rabbitMQConfig.getPort());
        rabbitConnectionFactory.setUsername(rabbitMQConfig.getUsername());
        rabbitConnectionFactory.setPassword(AESUtils.aesDecrypt(rabbitMQConfig.getPassword()));
        rabbitConnectionFactory.setVirtualHost(rabbitMQConfig.getVirtualHost());
    }

    /**
     * 设置消息处理器。
     * @param processor 消息处理器实例
     */
    @Override
    public void setMessageProcessor(IMessageProcessor processor) {
        this.messageProcessor = processor;
        log.info("[RabbitMQListener-INIT] 消息处理器已设置。sourceId={}", definition.getId());
    }

    @Override
    public void startListening() {
        if (rabbitMQConfig == null) {
            log.error("[RabbitMQListener-START-FAIL] 监听器配置未初始化，无法启动。");
            return ;
        }
        if (connection != null && connection.isOpen() && channel != null && channel.isOpen()) {
            log.warn("[RabbitMQListener-START-SKIP] 监听器已在运行。sourceId={}", definition.getId());
            return ;
        }
        try {
            // 设置连接超时时间，从配置中获取，如果未设置则提供默认值（例如 5000ms = 5秒）
            int actualConnectionTimeout = (rabbitMQConfig.getConnectionTimeout() != null && rabbitMQConfig.getConnectionTimeout() >= 0)
                    ? rabbitMQConfig.getConnectionTimeout()
                    : 5000; // 默认5秒
            rabbitConnectionFactory.setConnectionTimeout(actualConnectionTimeout);

            log.info("[RabbitMQListener-START] 尝试连接 RabbitMQ... sourceId={}, host={}:{}, queue={}",
                    definition.getId(), rabbitMQConfig.getHost(), rabbitMQConfig.getPort(), rabbitMQConfig.getQueueName());

            connection = rabbitConnectionFactory.newConnection();
            channel = connection.createChannel();
            // 设置 QoS (Quality of Service)，即预取数量
            // prefetchCount = 1 通常用于公平调度，避免一个慢消费者拖垮整个队列
            // 如果需要更高吞吐量，可以设置为更大的值
            // 如果 rabbitMQConfig.getPrefetchCount() 为 null 或小于1，可以给一个默认值，例如 1
            int actualPrefetchCount = (rabbitMQConfig.getPrefetchCount() != null && rabbitMQConfig.getPrefetchCount() >= 1)
                    ? rabbitMQConfig.getPrefetchCount()
                    : 1; // 默认值设为1，确保可靠性

            channel.basicQos(actualPrefetchCount, false); // false 表示此QoS设置仅应用于当前channel的消费者，而非所有channel
            // 声明队列（如果不存在则创建）
            // 参数：queue, durable, exclusive, autoDelete, arguments
            channel.queueDeclare(rabbitMQConfig.getQueueName(), true, false, false, null);

            RabbitMQAdvancedConfig advancedConfig = rabbitMQConfig.getRabbitMQAdvancedConfig();
            if (advancedConfig != null) {
                channel.exchangeDeclare(advancedConfig.getExchangeName(), "direct");
                channel.queueBind(rabbitMQConfig.getQueueName(), advancedConfig.getExchangeName(), advancedConfig.getRoutingKey());
            }

            connection.addShutdownListener(new ShutdownListener() {
                @Override
                public void shutdownCompleted(ShutdownSignalException cause) {
                    // isHardError() 表示是 Connection 级别的错误，而不是 Channel 的正常关闭
                    // cause.isInitiatedByApplication() 为 false 表示是意外关闭
                    if (cause.isHardError() && !cause.isInitiatedByApplication()) {
                        log.warn("[RabbitMQ-STATE-CHANGE] 连接中断，开始自动重连... sourceId={}", definition.getId());
                        // 在这里更新你的业务状态
                        definition = updateTaskStatus(definition,"RECONNECTING");
                    }
                }
            });

            ((AutorecoveringConnection) connection).addRecoveryListener(new RecoveryListener() {
                @Override
                public void handleRecovery(Recoverable recoverable) {
                    log.info("[RabbitMQ-STATE-CHANGE] 连接已成功恢复！ sourceId={}", definition.getId());
                   definition = updateTaskStatus(definition, "ACTIVE");
                }

                @Override
                public void handleRecoveryStarted(Recoverable recoverable) {
                    log.info("[RabbitMQ-STATE-CHANGE] 开始尝试恢复连接... sourceId={}", definition.getId());
                }
            });

            // 创建独立线程池来处理消息消费回调，避免阻塞MQ客户端线程
            // 使用更大的线程池，确保有足够的线程处理消息，避免因并发限制导致的阻塞
            // 注意：线程池大小应该远大于并发限制，避免线程池耗尽
            int threadPoolSize = Math.max(rabbitMQConfig.getPrefetchCount() * 5, 20); // 至少20个线程，确保有足够的线程处理消息
            consumerExecutor = Executors.newFixedThreadPool(
                    threadPoolSize,
                    r -> new Thread(r, "RabbitMQConsumer-" + definition.getId())
            );
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                    consumerExecutor.submit(() -> {
                        Runnable ackCallback = () -> {
                            try {
                                if (channel != null && channel.isOpen()) {
                                    log.info("[MQ_ACK] 异步任务处理成功，执行 ACK。deliveryTag={}", envelope.getDeliveryTag());
                                    channel.basicAck(envelope.getDeliveryTag(), false);
                                }
                            } catch (IOException e) {
                                log.error("[MQ_ACK_ERROR] 执行 ACK 时发生IO异常。deliveryTag={}", envelope.getDeliveryTag(), e);
                            }
                        };

                    String rawMessage = new String(body, StandardCharsets.UTF_8);
                    String triggerInstanceId = UUID.randomUUID().toString();

                    MDC.put("triggerInstanceId", triggerInstanceId);
                    MDC.put("sourceId", String.valueOf(definition.getId()));
                    MDC.put("workflowId", definition.getTargetWorkflowId());
                    MDC.put("messageId", envelope.getDeliveryTag() + "_" + System.currentTimeMillis()); // 临时MessageId

                    try {
                        log.info("[MQ_RECEIVE] triggerInstanceId={}, sourceId={}, queue={}, deliveryTag={}, msgSize={}",
                                triggerInstanceId, definition.getId(), rabbitMQConfig.getQueueName(), envelope.getDeliveryTag(), body.length);

                        // 将消息提交给消息处理器
                        RabbitMQRawMessageWrapper rawMessageWrapper = new RabbitMQRawMessageWrapper(body, properties, envelope);
                        messageProcessor.process(definition, rawMessageWrapper, rawMessage, triggerInstanceId, ackCallback);

                    } catch (Exception e) {
                        log.error("[MQ_PROCESSING_ERROR] 消息处理失败，triggerInstanceId={}, sourceId={}, deliveryTag={}, error={}",
                                triggerInstanceId, definition.getId(), envelope.getDeliveryTag(), e.getMessage(), e);
                        try {
                            channel.basicNack(envelope.getDeliveryTag(), false, true); // 重新入队
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } finally {
                        MDC.remove("triggerInstanceId");
                        MDC.remove("sourceId");
                        MDC.remove("workflowId");
                        MDC.remove("messageId");
                    }
                    });
                }
            };

            // 开始消费消息
            this.consumerTag = channel.basicConsume(rabbitMQConfig.getQueueName(), false, consumer);
            log.info("[RabbitMQListener-START-SUCCESS] RabbitMQ 监听器已启动。sourceId={}, queueName={}, consumerTag={}",
                    definition.getId(), rabbitMQConfig.getQueueName(), this.consumerTag);
            definition = updateTaskStatus(definition, "ACTIVE");
        } catch (IOException | TimeoutException e) {
            log.error("[RabbitMQListener-START-FAIL] 启动 RabbitMQ 监听器失败，sourceId={}, error={}",
                    definition != null ? definition.getId() : null, e.getMessage(), e);
            // 确保在启动失败时清理资源
            stopListening();
            throw new RuntimeException("RabbitMQ监听器启动失败", e); // 关键：抛出异常
        }
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
        log.error("[MS_SERVICE_UPDATE_STATUS_FAIL] 激活失败，数据库更新失败。ID: {}", id);
        return definition;
    }

    @Override
    public void stopListening() {
        log.info("[RabbitMQListener-STOP] 尝试停止 RabbitMQ 监听器，sourceId={}", definition != null ? definition.getId() : null);
        try {
            if (channel != null && channel.isOpen()) {
                if (consumerTag != null) {
                    channel.basicCancel(consumerTag); // 取消消费者
                    log.info("[RabbitMQListener-STOP] RabbitMQ consumer cancelled, sourceId={}, consumerTag={}", definition.getId(), consumerTag);
                }
                channel.close(); // 关闭通道
                log.info("[RabbitMQListener-STOP] RabbitMQ channel closed.");
            }
            if (connection != null && connection.isOpen()) {
                connection.close(); // 关闭连接
                log.info("[RabbitMQListener-STOP] RabbitMQ connection closed.");
            }
        } catch (IOException | TimeoutException e) {
            log.error("[RabbitMQListener-STOP-ERROR] 停止 RabbitMQ 监听器时发生错误，sourceId={}, error={}",
                    definition != null ? definition.getId() : null, e.getMessage(), e);
        } finally {
            if (consumerExecutor != null && !consumerExecutor.isShutdown()) {
                // 优雅关闭线程池，等待正在处理的消息完成
                consumerExecutor.shutdown(); // 先尝试优雅关闭
                try {
                    if (!consumerExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                        log.warn("[RabbitMQListener-STOP] consumerExecutor 未在规定时间内优雅关闭，强制关闭.");
                        consumerExecutor.shutdownNow(); // 如果优雅关闭失败，再强制关闭
                    } else {
                        log.info("[RabbitMQListener-STOP] consumerExecutor 已优雅关闭.");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("[RabbitMQListener-STOP] 等待线程池关闭时被中断，强制关闭.");
                    consumerExecutor.shutdownNow();
                }
            }
            connection = null;
            channel = null;
            consumerTag = null;
        }
    }

    @Override
    public Long getMessageSourceId() {
        return definition != null ? definition.getId() : null;
    }

    @Override
    public String getProtocolType() {
        return MessageProtocol.RABBITMQ.getName(); // 返回协议的名称字符串
    }

    /**
     * **实现新增：** 获取此监听器当前正在使用的消息触发任务定义。
     * @return 当前的消息触发任务定义
     */
    @Override
    public MessageTriggerTaskDefinition getDefinition() {
        return definition;
    }
}
