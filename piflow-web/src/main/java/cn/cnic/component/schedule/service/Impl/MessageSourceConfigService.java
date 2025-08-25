package cn.cnic.component.schedule.service.Impl;

import cn.cnic.base.utils.AESUtils;
import cn.cnic.component.schedule.domain.MessageSourceConfigDomain;
import cn.cnic.component.schedule.domain.TaskTriggerInstanceDomain;
import cn.cnic.component.schedule.dto.MessageConfigDTO;
import cn.cnic.common.Eunm.MessageProtocol;
import cn.cnic.component.schedule.entity.MessageTriggerTaskDefinition;
// import cn.cnic.component.schedule.manager.MessageDrivenSchedulerManager; // 不再直接注入和调用此管理器
import cn.cnic.component.schedule.listener.Kafka.KafkaConfig;
import cn.cnic.component.schedule.listener.RabbitMQ.RabbitMQConfig;
import cn.cnic.component.schedule.manager.MessageDrivenSchedulerManager;
import cn.cnic.component.system.domain.SysUserDomain;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import cn.cnic.component.schedule.exception.ResourceNotFoundException;
import cn.cnic.component.schedule.vo.MessageSourceConfigVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 消息源（事件定义）业务服务。
 * **此服务现在只负责 MessageTriggerTaskDefinition 在数据库中的持久化和状态更新。**
 * **监听器的实际生命周期管理（启停、热更新）将由一个独立的定时任务 `ListenerLifecycleManager` 负责。**
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class MessageSourceConfigService {

    private final MessageSourceConfigDomain domain;
    private final TaskTriggerInstanceDomain triggerInstanceDomain;
    private final MessageDrivenSchedulerManager schedulerManager;
    private final SysUserDomain sysUserDomain;


    /**
     * 获取所有事件定义
     * @return 所有消息源配置实体列表
     */
    public List<MessageTriggerTaskDefinition> getAll(Integer page, Integer limit, String search) {
        return domain.getAll(page, limit, search);
    }

    /**
     * 新增事件定义
     * @param configVo 消息源配置VO
     * @return 添加后的实体ID
     */
    @Transactional
    public Long create(MessageSourceConfigVo configVo, String username) {
        MessageTriggerTaskDefinition definition = new MessageTriggerTaskDefinition();
        BeanUtils.copyProperties(configVo, definition);
        definition.setProtocol(MessageProtocol.fromName(configVo.getProtocol()));
        definition.setCreateTime(new Date(System.currentTimeMillis())); // 使用java.time.LocalDateTime
        definition.setUpdateTime(new Date(System.currentTimeMillis()));

        String creatorId = sysUserDomain.findUserByName(username).get(0).getId();
        definition.setCreatorId(creatorId);
        // 默认设置为非激活状态，等待定时任务激活
        if (definition.getStatus() == null || definition.getStatus().isEmpty()) {
            definition.setStatus("INACTIVE");
        }

        int affectedRows = domain.insert(definition);
        if (affectedRows > 0) {
            log.info("[MS_SERVICE_ADD] 消息触发任务定义添加成功，ID: {}", definition.getId());
            // **不再直接启动监听器，交由 ListenerLifecycleManager 处理**
            return definition.getId();
        }
        log.error("[MS_SERVICE_ADD_FAIL] 消息触发任务定义添加失败。");
        return null;
    }

    /**
     * 更新事件定义
     * @param configVo 消息源配置VO
     * @return 是否更新成功
     */
    @Transactional
    public boolean update(MessageSourceConfigVo configVo, String username) throws JsonProcessingException {
        if (configVo.getId() == null) {
            log.error("[MS_SERVICE_UPDATE_FAIL] 更新失败，定义ID不能为空。");
            return false;
        }

        MessageTriggerTaskDefinition existingDefinition = domain.getById(configVo.getId());
        if (existingDefinition == null) {
            log.error("[MS_SERVICE_UPDATE_FAIL] 更新失败，未找到ID为 {} 的定义。", configVo.getId());
            return false;
        }

        MessageTriggerTaskDefinition updatedDefinition = new MessageTriggerTaskDefinition();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> propertiesMap = mapper.readValue(configVo.getProperties(), new TypeReference<Map<String, Object>>() {});

        boolean hasPassword = propertiesMap.containsKey("password");
        if (!hasPassword) {
            Map<String, Object> existingPropertiesMap = mapper.readValue(existingDefinition.getProperties(), new TypeReference<Map<String, Object>>() {});
            propertiesMap.put("password", existingPropertiesMap.get("password"));
        }
        configVo.setProperties(mapper.writeValueAsString(propertiesMap));


        BeanUtils.copyProperties(configVo, updatedDefinition);
        updatedDefinition.setProtocol(MessageProtocol.fromName(configVo.getProtocol()));
        updatedDefinition.setUpdateTime(new Date(System.currentTimeMillis()));

        Boolean startListener = true;
        Boolean existInListenerMap = schedulerManager.isExistInListenerMap(updatedDefinition.getId());
        if (existInListenerMap) {
            startListener = schedulerManager.startListener(updatedDefinition);
            if (!startListener) {
                log.warn("[MS_SERVICE_UPDATE] 监听实例热更新启动失败，ID: {}", updatedDefinition.getId());
                updatedDefinition.setStatus("INACTIVE");
            }
        }
        int affectedRows = domain.update(updatedDefinition);
        if (affectedRows > 0) {
            log.info("[MS_SERVICE_UPDATE] 消息触发任务定义更新成功，ID: {}", updatedDefinition.getId());
        }else {
            log.error("[MS_SERVICE_UPDATE_FAIL] 消息触发任务定义更新失败，ID: {}", updatedDefinition.getId());
        }
        return startListener;
    }

    /**
     * 根据ID删除消息触发任务定义（仅更新数据库状态）。
     * **监听器的实际停止和移除将由 ListenerLifecycleManager 处理。**
     * @param id 定义ID
     * @return 是否删除成功
     */
    @Transactional
    public boolean delete(Long id) {
        if (id == null) {
            log.error("[MS_SERVICE_DELETE_FAIL] 删除失败，定义ID不能为空。");
            return false;
        }

        int affectedRows = domain.deleteById(id);
        if (affectedRows > 0) {
            log.info("[MS_SERVICE_DELETE] 消息触发任务定义删除成功，ID: {}", id);
            // 即时通知调度器关闭监听器
            schedulerManager.stopListener(id);
            //删除所有实例
            triggerInstanceDomain.deleteByMessageSourceId(id);
            return true;
        }
        log.error("[MS_SERVICE_DELETE_FAIL] 消息触发任务定义删除失败，ID: {}", id);
        return false;
    }

    /**
     * 根据ID查询消息触发任务定义。
     * @param id 定义ID
     * @return 消息源配置实体
     */
    public MessageTriggerTaskDefinition getById(Long id) {
        MessageTriggerTaskDefinition config = domain.getById(id);
        if (config == null) {
            throw new ResourceNotFoundException("消息源不存在: " + id);
        }
        return config;
    }

    /**
     * 获取所有消息触发任务定义的VO列表。
     * @return 消息源配置VO列表
     */
    public List<MessageSourceConfigVo> getAllVo(Integer page, Integer limit, String search) {
        return getAll(page, limit, search).stream().map(this::toVo).collect(Collectors.toList());
    }

    /**
     * 根据ID查询消息触发任务定义的VO。
     * @param id 定义ID
     * @return 消息源配置VO
     */
    public MessageSourceConfigVo getByIdVo(Long id) {
        return toVo(getById(id));
    }

    /**
     * 将实体转换为VO。
     * @param entity 消息源配置实体
     * @return 消息源配置VO
     */
    public MessageSourceConfigVo toVo(MessageTriggerTaskDefinition entity) {
        if (entity == null) return null;
        MessageSourceConfigVo vo = new MessageSourceConfigVo();
        BeanUtils.copyProperties(entity, vo);
        // 如果 MessageProtocol 是枚举，需要特殊处理，确保VO的type字段能正确映射
        if (entity.getProtocol() != null) {
            vo.setProtocol(entity.getProtocol().name()); // 或者 entity.getProtocol().getDisplayName() 如果有
        }
        return vo;
    }

    /**
     * 激活指定ID的消息触发任务定义（仅更新数据库状态）。
     * **监听器的实际启动将由 ListenerLifecycleManager 处理。**
     * @param id 定义ID
     * @return 是否操作成功
     */
    @Transactional
    public boolean activate(Long id) {
        MessageTriggerTaskDefinition definition = domain.getById(id);
        if (definition == null) {
            log.warn("[MS_SERVICE_ACTIVATE_FAIL] 激活失败，未找到ID为 {} 的定义。", id);
            return false;
        }
        if ("ACTIVE".equalsIgnoreCase(definition.getStatus())) {
            log.info("[MS_SERVICE_ACTIVATE_SKIP] 定义 {} 已是激活状态。", id);
            return true;
        }
        try {
            MessageConfigDTO messageConfigDTO = new MessageConfigDTO();
            messageConfigDTO.setId(definition.getId());
            messageConfigDTO.setProtocol(definition.getProtocol());

            String properties = definition.getProperties();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> propertiesMap = mapper.readValue(properties, new TypeReference<Map<String, Object>>() {});
            propertiesMap.remove("password");
            String propertiesWithoutPassword = mapper.writeValueAsString(propertiesMap);

            messageConfigDTO.setProperties(propertiesWithoutPassword);
            this.testConnection(messageConfigDTO);

            Boolean startListener = schedulerManager.startListener(definition);
            if (!startListener) {
                log.warn("[MS_SERVICE_UPDATE] 监听实例启动失败，ID: {}", definition.getId());
                definition.setStatus("INACTIVE");
            }else {
                definition.setStatus("ACTIVE");
            }
            definition.setUpdateTime(new Date(System.currentTimeMillis()));
            int affectedRows = domain.update(definition);
            if (affectedRows > 0 && startListener) {
                log.info("[MS_SERVICE_ACTIVATE_SUCCESS] 定义 {} 状态更新为ACTIVE。", id);
                // 即时通知调度器启动监听器
                return true;
            }
            log.error("[MS_SERVICE_ACTIVATE_FAIL] 激活失败，数据库更新失败。ID: {}", id);
            return false;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 停用指定ID的消息触发任务定义（仅更新数据库状态）。
     * @param id 定义ID
     * @return 是否操作成功
     */
    @Transactional
    public boolean pause(Long id) {
        MessageTriggerTaskDefinition definition = domain.getById(id);
        if (definition == null) {
            log.warn("[MS_SERVICE_PAUSE_FAIL] 停用失败，未找到ID为 {} 的定义。", id);
            return false;
        }
        if ("INACTIVE".equalsIgnoreCase(definition.getStatus())) {
            log.info("[MS_SERVICE_PAUSE_SKIP] 定义 {} 已是停用状态。", id);
            return true;
        }
        definition.setStatus("INACTIVE");
        definition.setUpdateTime(new Date(System.currentTimeMillis()));
        int affectedRows = domain.update(definition);
        if (affectedRows > 0) {
            log.info("[MS_SERVICE_PAUSE_SUCCESS] 定义 {} 状态更新为INACTIVE。", id);
            // 即时通知调度器关闭监听器
            schedulerManager.stopListener(definition.getId());
            return true;
        }
        log.error("[MS_SERVICE_PAUSE_FAIL] 停用失败，数据库更新失败。ID: {}", id);
        return false;
    }

    /**
     * 测试消息队列连接。
     * @param messageConfig 定义配置
     * @return 是否操作成功
     */
    public void testConnection(MessageConfigDTO messageConfig) throws Exception {
        if (messageConfig == null || messageConfig.getProtocol() == null || messageConfig.getProperties() == null) {
            throw new IllegalArgumentException("消息协议和配置不能为空");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        MessageProtocol protocol = messageConfig.getProtocol();

        switch (protocol) {
            case RABBITMQ:
                // 1. 转换为 RabbitMQConfig
                RabbitMQConfig rabbitMQConfig = objectMapper.readValue(messageConfig.getProperties(), RabbitMQConfig.class);

                // 2. 尝试连接 RabbitMQ
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(rabbitMQConfig.getHost());
                factory.setPort(rabbitMQConfig.getPort());
                factory.setUsername(rabbitMQConfig.getUsername());
                factory.setPassword(getMessageSourcePassword(messageConfig.getId(), rabbitMQConfig.getPassword()));
                if (rabbitMQConfig.getVirtualHost() != null) {
                    factory.setVirtualHost(rabbitMQConfig.getVirtualHost());
                }
                if (rabbitMQConfig.getConnectionTimeout() != null) {
                    factory.setConnectionTimeout(rabbitMQConfig.getConnectionTimeout());
                }

                try (Connection connection = factory.newConnection()) {
                    // 连接成功即返回
                } catch (Exception e) {
                    throw new RuntimeException("RabbitMQ 连接失败: " + e);
                }
                break;
            case KAFKA:
                KafkaConfig kafkaConfig = objectMapper.readValue(messageConfig.getProperties(), KafkaConfig.class);
                String bootstrapServers = kafkaConfig.getBootstrapServers();
                if (StringUtils.isBlank(bootstrapServers)) {
                    throw new IllegalArgumentException("Kafka bootstrapServers 不能为空");
                }

                // 设置 Kafka 客户端属性
                Properties props = new Properties();
                props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

                // 设置一个较短的请求超时时间，以便在无法连接时快速失败
                props.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "3000"); // 5秒
                props.put(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, "3000"); // 5秒
                if (StringUtils.isNotBlank(kafkaConfig.getUsername()) || StringUtils.isNotBlank(kafkaConfig.getPassword())){
                    String jaasConfig = String.format(
                            "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";",
                            kafkaConfig.getUsername(),
                            getMessageSourcePassword(messageConfig.getId(), kafkaConfig.getPassword())
                    );
//                    props.put("sasl.jaas.config", jaasConfig);
                }
                try (AdminClient adminClient = AdminClient.create(props)) {
                    adminClient.listTopics().names().get(4, TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new RuntimeException("Kafka 连接失败: " + e);
                }
                break;
            default:
                throw new IllegalArgumentException("不支持的消息协议类型: " + protocol);
        }
    }

    private String getMessageSourcePassword(Long id, String passwordFromRequest) {
        if (id == null) {
            // ---- 场景1: 创建模式 ----
            return passwordFromRequest;
        } else {
            // ---- 场景2: 编辑模式 ----
            if (passwordFromRequest != null) {
                return passwordFromRequest;
            } else {
                // 如果 passwordFromRequest 是 null，说明前端根本没传 password 字段。
                // 这意味着用户不想修改密码，需要从数据库根据 id 查询。
                String propertiesJson = domain.getMessageSourcePasswordById(id);
                try {
                    // 2. 检查从数据库获取的字符串是否为空
                    if (propertiesJson == null || propertiesJson.isEmpty()) {
                        return ""; // 如果没有属性，可以视为空密码
                    }

                    // 3. 使用 ObjectMapper 解析 JSON 字符串
                    ObjectMapper mapper = new ObjectMapper();
                    // 将 JSON 解析成一个 Map<String, Object>
                    Map<String, Object> propertiesMap = mapper.readValue(propertiesJson, new TypeReference<Map<String, Object>>() {});

                    // 4. 从 Map 中获取 password 字段的值并返回
                    //    注意：这里获取到的是加密后的密码，后续流程需要解密
                    Object passwordObject = propertiesMap.get("password");

                    return passwordObject != null ? AESUtils.aesDecrypt(passwordObject.toString()) : "";

                } catch (Exception e) {
                    // 如果JSON解析失败，记录错误并可以抛出异常或返回一个安全默认值
                    log.error("从数据库解析properties JSON失败! ID: " + id + ", JSON: " + propertiesJson);
                    e.printStackTrace();
                    // 抛出运行时异常，让上层捕获并处理
                    throw new RuntimeException("解析数据库中的配置信息失败", e);
                }
            }
        }
    }

    public int updateStatusById(Long id, String status, Date updateTime) {
        return domain.updateStatusById(id, status, updateTime) ;
    }
}