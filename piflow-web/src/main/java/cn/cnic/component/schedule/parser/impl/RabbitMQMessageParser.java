package cn.cnic.component.schedule.parser.impl;

import cn.cnic.common.Eunm.MessageProtocol;
import cn.cnic.component.schedule.listener.RabbitMQ.RabbitMQRawMessageWrapper;
import cn.cnic.component.schedule.message.GenericMessage;
import cn.cnic.component.schedule.message.IMessage;
import cn.cnic.component.schedule.parser.IMessageParser;
import cn.cnic.component.schedule.parser.MessageParsingException;
import com.rabbitmq.client.AMQP;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * RabbitMQ 消息解析器实现。
 * 将 RabbitMQ 的原始消息（Delivery, Properties, body）解析为 IMessage。
 */
@Log4j2
@Component
public class RabbitMQMessageParser implements IMessageParser {

    @Override
    public boolean supports(MessageProtocol protocol) {
        return MessageProtocol.RABBITMQ.equals(protocol);
    }

    @Override
    public IMessage parse(Long id, Object rawMessageData) throws MessageParsingException {
        if (!(rawMessageData instanceof RabbitMQRawMessageWrapper)) {
            throw new MessageParsingException("期望 RabbitMQRawMessageWrapper 类型，但接收到 " +
                    (rawMessageData != null ? rawMessageData.getClass().getName() : "null"));
        }

        RabbitMQRawMessageWrapper wrapper = (RabbitMQRawMessageWrapper) rawMessageData;
        AMQP.BasicProperties properties = wrapper.getProperties();
        byte[] body = wrapper.getBody();
//        long deliveryTag = wrapper.getEnvelope().getDeliveryTag();
//        String consumerQueue = wrapper.getEnvelope().getExchange().isEmpty() ? wrapper.getEnvelope().getRoutingKey() : wrapper.getEnvelope().getExchange() + "/" + wrapper.getEnvelope().getRoutingKey();

        String messageId = null;
        // 优先级 1: 尝试获取标准的 messageId
        if (properties != null && properties.getMessageId() != null && !properties.getMessageId().isEmpty()) {
            messageId = properties.getMessageId();
            log.debug("使用 'messageId' 属性作为唯一ID: {}", messageId);
        }

        // 优先级 2: 如果 messageId 为空，尝试获取 correlationId
        if (messageId == null && properties != null && properties.getCorrelationId() != null && !properties.getCorrelationId().isEmpty()) {
            messageId = properties.getCorrelationId();
            log.debug("使用 'correlationId' 属性作为唯一ID: {}", messageId);
        }

        // 优先级 3: 如果以上都为空，尝试从自定义头中获取
        if (messageId == null && properties != null && properties.getHeaders() != null) {
            Object eventIdHeader = properties.getHeaders().get("x-event-id"); // 与生产者约定好的头名称
            if (eventIdHeader != null) {
                messageId = eventIdHeader.toString();
                log.debug("使用 'x-event-id' 自定义头作为唯一ID: {}", messageId);
            }
        }

        // 优先级 4: 如果以上全部失败，使用组合哈希
        if (messageId == null) {
            messageId = generateCompositeHash(wrapper);
            log.warn("在消息属性或头中未找到唯一ID，已根据[内容+路由键]生成组合哈希作为回退ID: {}", messageId);
        }
        //RabbitMQ消息id格式为RABBITMQ-id-deliveryTag
        String idStr = String.valueOf(id);
        messageId = String.format("RABBITMQ-%s-%s", idStr, messageId);

        String messageBody = new String(body, StandardCharsets.UTF_8);

        Map<String, String> headers = new HashMap<>();
        if (properties != null && properties.getHeaders() != null) {
            properties.getHeaders().forEach((key, value) -> {
                // RabbitMQ Headers value 可以是多种类型，这里简单转为String
                headers.put(key, Optional.ofNullable(value).map(Object::toString).orElse(""));
            });
        }

        Date timestamp = null;

        if (properties != null && properties.getTimestamp() != null) {
            timestamp = properties.getTimestamp();
        }


        return GenericMessage.builder()
                .messageId(messageId)
                .body(messageBody)
                .headers(Collections.unmodifiableMap(headers)) // 返回不可修改的Map
                .timestamp(timestamp)
                .protocolType(MessageProtocol.RABBITMQ.getName())
                .rawMessage(wrapper) // 保留原始消息 wrapper
                .rawContentString(messageBody) // 初始解析时可设置，或者由DefaultMessageProcessor设置
                .build();
    }

    private String generateCompositeHash(RabbitMQRawMessageWrapper wrapper) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            //添加消息体
            digest.update(wrapper.getBody());

            String routingKey = wrapper.getEnvelope().getRoutingKey();
            if (routingKey != null) {
                digest.update(routingKey.getBytes(StandardCharsets.UTF_8));
            }

            byte[] hash = digest.digest();
            return "rabbitmq-" + bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}