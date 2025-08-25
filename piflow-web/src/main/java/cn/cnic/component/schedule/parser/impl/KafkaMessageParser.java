package cn.cnic.component.schedule.parser.impl;

import cn.cnic.common.Eunm.MessageProtocol;
import cn.cnic.component.schedule.listener.Kafka.KafkaRawMessageWrapper;
import cn.cnic.component.schedule.message.GenericMessage;
import cn.cnic.component.schedule.message.IMessage;
import cn.cnic.component.schedule.parser.IMessageParser;
import cn.cnic.component.schedule.parser.MessageParsingException;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Kafka 消息解析器实现。
 * 将 Kafka 的原始消息记录（ConsumerRecord）解析为 IMessage。
 */
@Log4j2
@Component
public class KafkaMessageParser implements IMessageParser {

    /**
     * 判断此解析器是否支持指定的协议类型。
     * @param protocol 协议类型
     * @return 如果支持 Kafka 协议，则返回 true
     */
    @Override
    public boolean supports(MessageProtocol protocol) {
        return MessageProtocol.KAFKA.equals(protocol);
    }

    /**
     * 将原始消息数据解析为通用消息对象。
     *
     * @param rawMessageData 原始消息数据，应为 KafkaRawMessageWrapper 类型
     * @param id
     * @return 通用消息对象 IMessage
     * @throws MessageParsingException 如果数据类型不匹配或解析失败
     */
    @Override
    public IMessage parse(Long id, Object rawMessageData) throws MessageParsingException {
        if (!(rawMessageData instanceof KafkaRawMessageWrapper)) {
            throw new MessageParsingException("期望 KafkaRawMessageWrapper 类型，但接收到 " +
                    (rawMessageData != null ? rawMessageData.getClass().getName() : "null"));
        }

        KafkaRawMessageWrapper wrapper = (KafkaRawMessageWrapper) rawMessageData;
        ConsumerRecord<String, String> record = wrapper.getRawRecord();
        //kafka消息id格式为KAFKA-id-topic-partition-offset
        String idStr = String.valueOf(id);
        String messageId = String.format("KAFKA-%s-%s-%d-%d", idStr, record.topic(), record.partition(), record.offset());
        String messageBody = record.value();

        Map<String, String> headers = new HashMap<>();
        for (Header header : record.headers()) {
            headers.put(header.key(), new String(header.value(), StandardCharsets.UTF_8));
        }

        Date timestamp = new Date(record.timestamp());

        return GenericMessage.builder()
                .messageId(messageId)
                .body(messageBody)
                .headers(Collections.unmodifiableMap(headers))
                .timestamp(timestamp)
                .protocolType(MessageProtocol.KAFKA.getName())
                .rawMessage(wrapper)
                .rawContentString(messageBody)
                .build();
    }
}