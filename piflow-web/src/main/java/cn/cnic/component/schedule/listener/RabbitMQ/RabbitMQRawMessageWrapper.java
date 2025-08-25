package cn.cnic.component.schedule.listener.RabbitMQ;
import com.rabbitmq.client.AMQP; // 引入 RabbitMQ AMQP.BasicProperties
import com.rabbitmq.client.Envelope; // 引入 RabbitMQ Envelope
import lombok.AllArgsConstructor; // Lombok注解，生成全参构造函数
import lombok.Getter;           // Lombok注解，生成所有字段的Getter方法
import lombok.Setter;           // Lombok注解，生成所有字段的Setter方法
import lombok.ToString;         // Lombok注解，生成toString方法

import java.util.Map; // 用于headers

/**
 * RabbitMQ 原始消息包装器。
 * 用于封装从 RabbitMQ 接收到的原始消息体、属性、头部和信封信息，
 * 以便在消息解析器和过滤器中使用。
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class RabbitMQRawMessageWrapper {

    private byte[] body; // 原始消息体
    private AMQP.BasicProperties properties; // 消息属性
    private Envelope envelope; // 消息信封，包含 deliveryTag, exchange, routingKey
    private Map<String, Object> headers; // 消息头部 (通常从properties中获取，这里单独列出方便)

    /**
     * 辅助构造函数，如果不需要单独传递headers，它会从properties中获取。
     * @param body 消息体
     * @param properties 消息属性
     * @param envelope 消息信封
     */
    public RabbitMQRawMessageWrapper(byte[] body, AMQP.BasicProperties properties, Envelope envelope) {
        this.body = body;
        this.properties = properties;
        this.envelope = envelope;
        this.headers = properties != null ? properties.getHeaders() : null;
    }

    /**
     * 获取消息体字符串形式 (UTF-8)
     * @return 消息体字符串
     */
    public String getBodyAsString() {
        return body != null ? new String(body, java.nio.charset.StandardCharsets.UTF_8) : null;
    }
}