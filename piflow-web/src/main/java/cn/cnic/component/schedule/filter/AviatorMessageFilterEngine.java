package cn.cnic.component.schedule.filter;

import cn.cnic.component.schedule.message.IMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 基于 AviatorScript 的消息过滤引擎实现。
 * 支持规则缓存和自定义JsonPath函数。
 */
@Log4j2
@Component
public class AviatorMessageFilterEngine implements IMessageFilterEngine {

    public static final String RULE_TYPE = "AVIATOR"; // 或 "AVIATOR_JSON_PATH"

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 规则缓存，key为规则字符串，value为预编译的Aviator表达式
    private final Cache<String, Expression> ruleCache = CacheBuilder.newBuilder()
            .maximumSize(1000) // 最大缓存1000个规则
            .expireAfterWrite(10, TimeUnit.MINUTES) // 规则10分钟不使用则过期
            .build();

    // JsonPath 配置
    private static final Configuration JSONPATH_CONF = Configuration.builder()
            .jsonProvider(new com.jayway.jsonpath.spi.json.JacksonJsonProvider())
            .options(EnumSet.noneOf(Option.class)) // 默认不配置任何Option
            .build();

    @PostConstruct
    public void init() {
        // 注册自定义 JsonPath 函数
        AviatorEvaluator.addFunction(new JsonPathFunction(objectMapper));
        log.info("AviatorMessageFilterEngine 初始化完成，已注册自定义函数：jsonPath");
    }

    @Override
    public boolean supports(String ruleType) {
        return RULE_TYPE.equalsIgnoreCase(ruleType);
    }

    @Override
    public boolean matches(IMessage message, String filterRuleExpression) throws MessageFilterException {
        if (filterRuleExpression == null || filterRuleExpression.trim().isEmpty()) {
            log.warn("过滤规则为空，默认通过。MessageId: {}", message.getMessageId());
            return true; // 没有规则默认通过
        }

        try {
            // 从缓存获取或编译表达式
            Expression expression = ruleCache.get(filterRuleExpression, () -> {
                log.debug("编译新的Aviator过滤规则: {}", filterRuleExpression);
                return AviatorEvaluator.compile(filterRuleExpression, true); // 开启缓存模式
            });

            // 构造 Aviator 环境
            Map<String, Object> env = toEvaluationContext(message); // 使用IMessage构建上下文

            // 执行过滤
            Object result = expression.execute(env);

            // Aviator表达式的返回值需要转换为Boolean
            if (result instanceof Boolean) {
                return (Boolean) result;
            } else {
                log.warn("Aviator过滤规则表达式返回值非布尔类型，规则：{}，返回结果：{}，消息ID：{}", filterRuleExpression, result, message.getMessageId());
                return false;
            }
        } catch (ExecutionException e) {
            log.error("Aviator 规则缓存获取或编译失败，规则：{}，消息ID：{}，错误：{}", filterRuleExpression, message.getMessageId(), e.getMessage(), e);
            throw new MessageFilterException("Aviator规则缓存或编译失败", e.getCause());
        } catch (Exception e) {
            log.error("执行Aviator过滤规则失败，规则：{}，消息ID：{}，错误：{}", filterRuleExpression, message.getMessageId(), e.getMessage(), e);
            throw new MessageFilterException("执行Aviator过滤规则失败", e);
        }
    }

    /**
     * 自定义 Aviator 函数：jsonPath(path)
     * 用于在 Aviator 表达式中通过 JsonPath 提取 JSON 消息体中的值。
     * Aviator 表达式中可以这样用：`jsonPath('$.data.status') == 'SUCCESS'`
     */
    private class JsonPathFunction extends AbstractFunction {
        private final ObjectMapper funcObjectMapper; // 独立的ObjectMapper，避免静态ObjectMapper可能带来的线程问题

        public JsonPathFunction(ObjectMapper objectMapper) {
            this.funcObjectMapper = objectMapper;
        }

        @Override
        public String getName() {
            return "jsonPath";
        }

        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
            if (arg1 == null) {
                log.warn("jsonPath 函数调用参数为空。");
                return new AviatorString("");
            }
            String path = arg1.stringValue(env);
            if (path == null || path.isEmpty()) {
                log.warn("jsonPath 函数调用路径参数为空。");
                return  new AviatorString("");
            }

            // 从环境中获取消息body
            Object bodyObj = env.get("body");
            if (!(bodyObj instanceof String)) {
                log.warn("jsonPath 函数期望'body'为字符串类型，实际类型: {}", bodyObj != null ? bodyObj.getClass().getName() : "null");
                return  new AviatorString("");// 如果body不是字符串，无法处理
            }
            String jsonContent = (String) bodyObj;

            try {
                // 使用 JsonPath 配置解析
                Object value = JsonPath.using(JSONPATH_CONF).parse(jsonContent).read(path);
                // 对于 JsonPath 返回的 null 或列表等复杂类型，需要转换为 AviatorString
                // JsonPath.read 如果路径不存在且配置未设置Option.SUPPRESS_EXCEPTIONS，会抛出PathNotFoundException
                // 但我们使用的是默认不配置任何Option，所以会返回null，这里直接转换
                return  new AviatorString(value != null ? value.toString() : "");
            } catch (PathNotFoundException e) {
                log.debug("JsonPath '{}' 未在消息中找到，消息ID：{}。", path, env.get("messageId"));
                return new AviatorString(""); // 路径未找到时返回空字符串
            } catch (Exception e) {
                log.error("执行 jsonPath 函数失败，路径 '{}'，消息ID：{}，错误：{}", path, env.get("messageId"), e.getMessage(), e);
                return  new AviatorString(""); // 发生其他异常时返回空字符串
            }
        }
    }
    @Override
    public String getRuleType() {
        return RULE_TYPE;
    }
}