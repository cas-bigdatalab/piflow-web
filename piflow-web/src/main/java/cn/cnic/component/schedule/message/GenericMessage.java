package cn.cnic.component.schedule.message;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * IMessage接口的通用实现类。
 * 用于封装从不同消息队列解析出的标准化消息数据。
 */
@Getter
@Setter
@Builder
@ToString
public class GenericMessage implements IMessage {

    private String messageId;
    private String body; // 消息主体内容
    @Builder.Default // Lombok注解，为headers提供默认空Map，避免NPE
    private Map<String, String> headers = Collections.emptyMap(); // 消息头部信息
    private Date timestamp;
    private String protocolType; // 协议类型
    private Object rawMessage; // 原始消息对象（如果需要保留）
    private String rawContentString; // 原始消息的字符串表示

    @Override
    public Optional<Date> getTimestamp() {
        return Optional.ofNullable(timestamp);
    }

    @Override
    public String getRawMessage() {
        return rawMessage.toString();
    }
}