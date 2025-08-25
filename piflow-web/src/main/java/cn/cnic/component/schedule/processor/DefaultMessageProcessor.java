package cn.cnic.component.schedule.processor;

import cn.cnic.common.Eunm.MessageProtocol;
import cn.cnic.component.schedule.entity.MessageTriggerTaskDefinition;
import cn.cnic.component.schedule.filter.AviatorMessageFilterEngine;
import cn.cnic.component.schedule.filter.DefaultMessageFilterEngine;
import cn.cnic.component.schedule.filter.IMessageFilterEngine;
import cn.cnic.component.schedule.filter.MessageFilterException;
import cn.cnic.component.schedule.manager.MessageDrivenSchedulerManager;
import cn.cnic.component.schedule.message.GenericMessage;
import cn.cnic.component.schedule.message.IMessage;
import cn.cnic.component.schedule.parser.IMessageParser;
import cn.cnic.component.schedule.parser.MessageParserFactory;
import cn.cnic.component.schedule.parser.MessageParsingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的消息处理器实现。
 * 负责串联消息的解析、过滤、上下文映射，并将最终任务提交给调度器。
 */
@Log4j2
@Component
public class DefaultMessageProcessor implements IMessageProcessor {

    @Autowired
    private MessageParserFactory parserFactory; // 注入消息解析器工厂

    private final Map<String, IMessageFilterEngine> filterEngines;

    @Autowired
    private MessageDrivenSchedulerManager schedulerManager; // 注入调度管理器


    @Autowired
    public DefaultMessageProcessor(
            MessageParserFactory parserFactory,
            MessageDrivenSchedulerManager schedulerManager,
            List<IMessageFilterEngine> filterEngineList
    ) {
        this.parserFactory = parserFactory;
        this.schedulerManager = schedulerManager;

        this.filterEngines = new ConcurrentHashMap<>();
        filterEngineList.forEach(engine -> this.filterEngines.put(engine.getRuleType(), engine));
    }


    @Override
    public void process(MessageTriggerTaskDefinition definition, Object rawMessageData, String rawContentString, String triggerInstanceId, Runnable ackCallback) throws MessageProcessingException {
        if (definition == null || definition.getId() == null) {
            log.error("triggerInstanceId={},消息触发任务定义或其ID为空，无法处理消息。", triggerInstanceId);
            throw new MessageProcessingException("消息触发任务定义或其ID为空。");
        }
        Long id = definition.getId();

        IMessage message = null;
        String messageId = null;
        try {
            // 1. 消息解析
            MessageProtocol protocol = definition.getProtocol();
            if (protocol == null) {
                throw new MessageProcessingException("消息定义中协议类型为空，无法获取解析器。");
            }
            IMessageParser parser = parserFactory.getParser(protocol);
            message = parser.parse(id, rawMessageData);
            messageId = message.getMessageId();
            //根据protocol，messageId检测是否是重复消息
            boolean isDuplicate = schedulerManager.checkIsDuplicateMessage(protocol, messageId);
            if (isDuplicate) {
                ackCallback.run();
                log.info("[MESSAGE_PROCESS_SKIP] 消息已处理过，自动ack。triggerInstanceId={}, sourceId={}, messageId={}", triggerInstanceId, id, messageId);
                return;
            }
            
            ((GenericMessage)message).setRawContentString(rawContentString);

            log.info("[MESSAGE_PARSE] 消息解析成功。triggerInstanceId={}, sourceId={}, messageId={}",triggerInstanceId, id, messageId);

            // 2. 消息过滤
            boolean passedFilter = true;
            String filterRuleJson = definition.getFilterRuleJson();

            if (StringUtils.hasText(filterRuleJson)) {
                // 1. 定义一个变量，用于存放最终传递给引擎的规则字符串
                String ruleForEngine = null;
                String filterRuleType = definition.getFilterRuleType();

                // 2. 首先根据规则类型来处理 filterRuleJson
                ruleForEngine = filterRuleJson;


                // 3. 检查是否成功获取了规则字符串，然后再执行过滤
                if (StringUtils.hasText(ruleForEngine)) {
                    IMessageFilterEngine filterEngine;
                    if ("default".equalsIgnoreCase(filterRuleType)) {
                        filterEngine = filterEngines.get(DefaultMessageFilterEngine.RULE_TYPE);
                    } else {
                        // 默认回退到 Aviator 引擎
                        filterEngine = filterEngines.get(AviatorMessageFilterEngine.RULE_TYPE);
                    }

                    if (filterEngine == null) {
                        log.warn("[FILTER_WARN] triggerInstanceId={}, 未找到对应的过滤引擎 '{}'，跳过过滤。", triggerInstanceId, filterRuleType);
                    } else {
                        // 4. 使用 ruleForEngine 和对应的引擎进行匹配
                        passedFilter = filterEngine.matches(message, ruleForEngine); // 假设 matches 方法也接收 ruleType
                        if (!passedFilter) {
                            log.info("[MESSAGE_FILTER] triggerInstanceId={}, 消息未通过过滤规则，将被丢弃。sourceId={}, messageId={}, ruleType={}, rule={}",
                                    triggerInstanceId, id, messageId, filterRuleType, ruleForEngine);
                            return; // 未通过过滤，直接返回
                        }
                        log.info("[MESSAGE_FILTER] triggerInstanceId={}, 消息通过过滤规则。sourceId={}, messageId={}, ruleType={}, rule={}",
                                triggerInstanceId, id, messageId, filterRuleType, ruleForEngine);
                    }
                } else {
                    // 如果 ruleForEngine 为空（例如JSON解析失败），则不执行过滤，默认通过
                    passedFilter = true;
                }

            } else {
                log.debug("[MESSAGE_FILTER] triggerInstanceId={}, 未配置过滤规则，消息默认通过。sourceId={}, messageId={}", triggerInstanceId, id, messageId);
            }


            // 3. 提交任务给调度管理器
            schedulerManager.submitTriggerTask(definition, message, triggerInstanceId, ackCallback);
            log.info("[MESSAGE_PROCESS_COMPLETE] 消息处理并提交任务成功。 triggerInstanceId={}, sourceId={}, messageId={}", triggerInstanceId, id, messageId);

        } catch (MessageParsingException | MessageFilterException e) {
            log.error("[MESSAGE_PROCESS_FAIL]  消息处理失败（解析/过滤/映射异常）。triggerInstanceId={}, sourceId={}, messageId={}, error={}",
                   triggerInstanceId, id, message != null ? messageId : "N/A", e.getMessage(), e);
            throw new MessageProcessingException("消息处理失败: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("[MESSAGE_PROCESS_FAIL] 消息处理过程中发生未知错误。triggerInstanceId={}, sourceId={}, messageId={}, error={}",
                    triggerInstanceId, id, message != null ? messageId : "N/A", e.getMessage(), e);
            throw new MessageProcessingException("消息处理未知错误: " + e.getMessage(), e);
        }
    }
}