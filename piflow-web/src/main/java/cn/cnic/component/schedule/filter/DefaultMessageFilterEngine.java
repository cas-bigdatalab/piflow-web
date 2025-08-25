package cn.cnic.component.schedule.filter;

import cn.cnic.component.schedule.message.IMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 基于 键值对(K-V) 的消息过滤引擎实现。
 * 规则是一个JSON数组，所有规则之间是 AND 关系。
 */
@Log4j2
@Component
public class DefaultMessageFilterEngine implements IMessageFilterEngine {

    public static final String RULE_TYPE = "DEFAULT"; // 或者 "KV"

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Configuration JSONPATH_CONF = Configuration.builder()
            .jsonProvider(new com.jayway.jsonpath.spi.json.JacksonJsonProvider())
            .options(EnumSet.noneOf(Option.class))
            .build();

    @Override
    public boolean supports(String ruleType) {
        return RULE_TYPE.equalsIgnoreCase(ruleType);
    }

    @Override
    public String getRuleType() {
        return RULE_TYPE;
    }

    @Override
    public boolean matches(IMessage message, String filterRuleJson) throws MessageFilterException {
        if (filterRuleJson == null || filterRuleJson.trim().isEmpty() || "[]".equals(filterRuleJson.trim())) {
            log.warn("Default 模式过滤规则为空，默认通过。MessageId: {}", message.getMessageId());
            return true; // 没有规则默认通过
        }

        try {
            // 1. 将规则JSON字符串反序列化为规则对象列表
            List<FilterRule> rules = objectMapper.readValue(filterRuleJson, new TypeReference<List<FilterRule>>() {});
            if (rules.isEmpty()) {
                return true; // 规则列表为空也默认通过
            }

            // 2. 所有规则必须全部匹配 (AND logic)
            for (FilterRule rule : rules) {
                if (!isRuleMatched(message, rule)) {
                    log.debug("消息 {} 未匹配规则 {}，过滤不通过。", message.getMessageId(), rule);
                    return false; // 只要有一条规则不匹配，则整体不匹配
                }
            }

            log.debug("消息 {} 成功匹配所有 {} 条规则，过滤通过。", message.getMessageId(), rules.size());
            return true; // 所有规则都匹配成功

        } catch (Exception e) {
            log.error("执行Default过滤规则失败，规则JSON：{}，消息ID：{}，错误：{}", filterRuleJson, message.getMessageId(), e.getMessage(), e);
            throw new MessageFilterException("执行Default过滤规则失败", e);
        }
    }

    private boolean isRuleMatched(IMessage message, FilterRule rule) {
        // 从消息体中用 JsonPath 提取实际值
        Object actualValue = extractValue(message, rule.getKey());
        String expectedValue = rule.getValue();

        // 根据操作符进行比较
        switch (rule.getOp()) {
            case "==":
                return Objects.equals(actualValue != null ? actualValue.toString() : null, expectedValue);
            case "!=":
                return !Objects.equals(actualValue != null ? actualValue.toString() : null, expectedValue);
            case "contains":
                return actualValue instanceof String && ((String) actualValue).contains(expectedValue);
            case "regex":
                return actualValue instanceof String && Pattern.matches(expectedValue, (String) actualValue);
            // 注意: 数字比较需要处理类型转换，这里做简化处理
            case ">":
                return compareAsDouble(actualValue, expectedValue) > 0;
            case "<":
                return compareAsDouble(actualValue, expectedValue) < 0;
            case ">=":
                return compareAsDouble(actualValue, expectedValue) >= 0;
            case "<=":
                return compareAsDouble(actualValue, expectedValue) <= 0;
            default:
                log.warn("不支持的操作符: {}", rule.getOp());
                return false;
        }
    }

    private Object extractValue(IMessage message, String jsonPath) {
        try {
            // JsonPath 表达式需要以 `$.` 或 `$[` 开头
            String pathToUse = jsonPath.trim();
            if (!pathToUse.startsWith("$.")) {
                // 假设用户可能只填写了顶级字段名，为其自动添加 `$.`
                pathToUse = "$." + pathToUse;
            }
            return JsonPath.using(JSONPATH_CONF).parse(message.getBody()).read(pathToUse);
        } catch (PathNotFoundException e) {
            log.debug("JsonPath '{}' 在消息 {} 中未找到。", jsonPath, message.getMessageId());
            return null; // 路径未找到时返回 null
        } catch (Exception e) {
            log.error("使用 JsonPath '{}' 提取值失败，消息ID：{}，错误：{}", jsonPath, message.getMessageId(), e.getMessage());
            return null;
        }
    }

    private int compareAsDouble(Object actual, String expected) {
        try {
            Double actualDouble = Double.valueOf(actual.toString());
            Double expectedDouble = Double.valueOf(expected);
            return actualDouble.compareTo(expectedDouble);
        } catch (NumberFormatException | NullPointerException e) {
            log.warn("无法将值 '{}' 和 '{}' 作为数字进行比较", actual, expected);
            return -1; // 类型不匹配或为null时，默认不满足条件
        }
    }

    /**
     * 用于反序列化规则的内部 DTO (Data Transfer Object)
     */
    @Getter
    @Setter
    public static class FilterRule implements Serializable {
        private String key; // JsonPath 表达式, e.g., "$.eventType"
        private String op;  // 操作符, e.g., "==", "contains"
        private String value; // 期望值

        @Override
        public String toString() {
            return String.format("['%s' %s '%s']", key, op, value);
        }
    }
}