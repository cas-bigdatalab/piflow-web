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

    private RabbitMQAdvancedConfig advancedConfig;

    @Override
    public MessageProtocol getProtocolType() {
        return MessageProtocol.RABBITMQ;
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class RabbitMQAdvancedConfig {
    /**
     * 请求心跳间隔（秒）
     */
    private Integer requestedHeartbeat;
    
    /**
     * 网络恢复间隔（秒）
     */
    private Integer networkRecoveryInterval;
    
    /**
     * 交换机名称
     */
    private String exchangeName;
    
    /**
     * 交换机类型
     */
    private String exchangeType;
    
    /**
     * 路由键
     */
    private String routingKey;
}