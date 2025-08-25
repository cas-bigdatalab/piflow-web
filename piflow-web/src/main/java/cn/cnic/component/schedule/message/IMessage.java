package cn.cnic.component.schedule.message;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * 系统内部统一消息模型接口。
 * 封装了来自不同消息队列的原始消息，并提供统一的访问方式。
 */
public interface IMessage {
    /**
     * 获取消息的唯一标识符。
     * @return 消息ID
     */
    String getMessageId();

    /**
     * 获取消息的主体内容，通常是字符串形式（如JSON, XML），或者 base64 编码的二进制数据。
     * @return 消息主体内容
     */
    String getBody();

    /**
     * 获取消息的头部信息/元数据。
     * @return 头部信息Map
     */
    Map<String, String> getHeaders();

    /**
     * 获取消息生成时间。
     *
     * @return 消息时间戳
     */
    Optional<Date> getTimestamp();

    /**
     * 获取消息的协议类型（例如：RABBITMQ, KAFKA）。
     * @return 协议类型字符串
     */
    String getProtocolType();

    /**
     * 获取原始消息对象（可选，用于特殊场景）。
     * @return 原始消息对象
     */
    String getRawMessage();

}