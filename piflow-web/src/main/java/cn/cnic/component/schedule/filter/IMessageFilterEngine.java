package cn.cnic.component.schedule.filter;

import cn.cnic.component.schedule.message.IMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * 消息过滤引擎接口。
 * 负责根据预定义的规则对IMessage进行过滤判断。
 */
public interface IMessageFilterEngine {

    /**
     * 判断当前过滤引擎是否支持指定的规则类型（例如 "AVIATOR", "JSON_PATH"）。
     * @param ruleType 过滤规则类型
     * @return 如果支持则返回true
     */
    boolean supports(String ruleType);

    /**
     * 根据给定的规则表达式，判断消息是否通过过滤。
     * @param message 经过解析的标准化IMessage对象
     * @param filterRuleExpression 过滤规则表达式（例如 AviatorScript 表达式）
     * @return 如果消息匹配规则则返回true，否则返回false
     * @throws MessageFilterException 如果规则评估失败
     */
    boolean matches(IMessage message, String filterRuleExpression) throws MessageFilterException;

    /**
     * 辅助方法：将IMessage转换为 Map<String, Object> 提供给规则引擎。
     * 某些规则引擎（如Aviator）可能需要Map作为输入环境。
     * @param message IMessage对象
     * @return 转换后的Map，包含body和headers
     */
    default Map<String, Object> toEvaluationContext(IMessage message) {
        Map<String, Object> context = new java.util.HashMap<>();
        Object rawBody = message.getBody();

        // 检查 Body 是否为 JSON 字符串
        if (rawBody instanceof String) {
            String jsonBody = (String) rawBody;
            try {
                // 使用 Jackson 将 JSON 字符串解析为 Map<String, Object>
                Map<String, Object> bodyMap =  new ObjectMapper().readValue(jsonBody, new TypeReference<Map<String, Object>>() {});

                context.putAll(bodyMap);
            } catch (IOException e) {
                // 如果解析失败，可以记录日志，或者进行其他错误处理
            }
        }
        context.put("headers", message.getHeaders());
        context.put("messageId", message.getMessageId());
        context.put("protocolType", message.getProtocolType());
        // 将 headers 中的每个键值对也扁平化到顶层，方便直接访问，例如 `headers.contentType` 或 `contentType`
        if (message.getHeaders() != null) {
            context.putAll(message.getHeaders());
        }
        return context;
    }

    String getRuleType();
}