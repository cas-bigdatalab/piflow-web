package cn.cnic.component.schedule.parser;

import cn.cnic.common.Eunm.MessageProtocol;
import cn.cnic.component.schedule.message.IMessage;

/**
 * 消息解析器接口。
 * 负责将特定消息队列的原始消息格式解析为统一的IMessage模型。
 */
public interface IMessageParser {
    /**
     * 判断当前解析器是否支持指定的协议类型。
     * @param protocol 消息协议类型
     * @return 如果支持则返回true
     */
    boolean supports(MessageProtocol protocol);

    /**
     * 将原始消息数据解析为IMessage对象。
     *
     * @param id
     * @param rawMessageData 特定MQ的原始消息数据
     * @return 解析后的IMessage对象
     * @throws MessageParsingException 如果解析失败
     */
    IMessage parse( Long id, Object rawMessageData) throws MessageParsingException;
}