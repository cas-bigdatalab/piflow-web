package cn.cnic.component.schedule.processor;

/**
 * 消息处理异常类。
 * 当消息在处理链中（解析、过滤、映射、提交）发生错误时抛出。
 */
public class MessageProcessingException extends RuntimeException {

    public MessageProcessingException(String message) {
        super(message);
    }

    public MessageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageProcessingException(Throwable cause) {
        super(cause);
    }
}