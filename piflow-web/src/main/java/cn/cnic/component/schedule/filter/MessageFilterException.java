package cn.cnic.component.schedule.filter;

/**
 * 消息过滤异常类。
 * 当消息过滤规则评估失败或发生其他过滤相关错误时抛出。
 */
public class MessageFilterException extends RuntimeException {

    public MessageFilterException(String message) {
        super(message);
    }

    public MessageFilterException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageFilterException(Throwable cause) {
        super(cause);
    }
}