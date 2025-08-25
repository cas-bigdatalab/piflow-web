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
    private String advancedConfig;

    @Override
    public MessageProtocol getProtocolType() {
        return MessageProtocol.KAFKA;
    }
}
@Data
@AllArgsConstructor
@NoArgsConstructor
class KafkaAdvancedConfig {
    private String groupId;
}