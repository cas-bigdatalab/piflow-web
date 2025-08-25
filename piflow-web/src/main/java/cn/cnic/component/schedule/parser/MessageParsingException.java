package cn.cnic.component.schedule.parser;

/**
 * 消息解析异常类。
 * 当消息从原始格式转换为IMessage模型时发生错误时抛出。
 */
public class MessageParsingException extends RuntimeException {

    public MessageParsingException(String message) {
        super(message);
    }
}