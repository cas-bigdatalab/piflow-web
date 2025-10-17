package cn.cnic.component.schedule.listener.Kafka;

import cn.cnic.common.Eunm.MessageProtocol;
import cn.cnic.component.schedule.listener.IMessageConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class KafkaConfig extends IMessageConfig {

    private String bootstrapServers;
    private String topicName;
    private String groupId;
    private String autoOffsetReset;
    private KafkaAdvancedConfig advancedConfig;

    @Override
    public MessageProtocol getProtocolType() {
        return MessageProtocol.KAFKA;
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class KafkaAdvancedConfig {
    /**
     * 请求超时时间（毫秒）
     */
    private Integer requestTimeout;
    
    /**
     * 重连退避最大时间（毫秒）
     */
    private Integer reconnectBackoffMax;
    
    /**
     * 单次poll最大记录数
     */
    private Integer maxPollRecords;
    
    /**
     * 最大poll间隔时间（毫秒）
     */
    private Integer maxPollInterval;
    
    /**
     * 会话超时时间（毫秒）
     */
    private Integer sessionTimeout;
    
    /**
     * 心跳间隔时间（毫秒）
     */
    private Integer heartbeatInterval;
}