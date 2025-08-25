package cn.cnic.component.schedule.listener.RabbitMQ;

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
public class RabbitMQConfig extends IMessageConfig {

    private String queueName;
    private Integer prefetchCount;
    private String virtualHost;
    // ... 其他RabbitMQ特有配置
    private RabbitMQAdvancedConfig rabbitMQAdvancedConfig;
    private String advancedConfig;

    @Override
    public MessageProtocol getProtocolType() {
        return MessageProtocol.RABBITMQ;
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class RabbitMQAdvancedConfig {
    private String exchangeName;
    private String routingKey;
}