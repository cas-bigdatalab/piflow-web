package cn.cnic.component.schedule.parser;

import cn.cnic.common.Eunm.MessageProtocol;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息解析器工厂。
 * 负责管理和提供不同协议类型的IMessageParser实例。
 */
@Log4j2
@Component
public class MessageParserFactory {

    // Spring会自动注入所有IMessageParser的实现
    @Autowired
    private List<IMessageParser> parsers;

    // 存储协议类型到具体解析器的映射
    private final Map<MessageProtocol, IMessageParser> parserRegistry = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        if (parsers != null && !parsers.isEmpty()) {
            for (IMessageParser parser : parsers) {
                // 遍历支持的所有协议，注册到Map中
                for (MessageProtocol protocol : MessageProtocol.values()) {
                    if (parser.supports(protocol)) {
                        if (parserRegistry.containsKey(protocol)) {
                            log.warn("检测到重复的消息解析器注册，协议类型: {}，已存在的解析器: {}，当前解析器: {}。请检查配置。",
                                    protocol.getName(), parserRegistry.get(protocol).getClass().getName(), parser.getClass().getName());
                            // 可以选择抛出异常或覆盖，这里选择日志警告并覆盖（以最后一个注册的为准）
                        }
                        parserRegistry.put(protocol, parser);
                        log.info("注册消息解析器：{} 支持协议 {}", parser.getClass().getSimpleName(), protocol.getName());
                    }
                }
            }
        } else {
            log.warn("未发现任何 IMessageParser 实现，请检查Spring配置。");
        }
    }

    /**
     * 根据消息协议类型获取对应的IMessageParser实例。
     * @param protocol 消息协议类型
     * @return 对应的IMessageParser实例
     * @throws MessageParsingException 如果不支持该协议或未找到对应的解析器
     */
    public IMessageParser getParser(MessageProtocol protocol) {
        if (protocol == null) {
            throw new MessageParsingException("消息协议类型不能为空。");
        }
        IMessageParser parser = parserRegistry.get(protocol);
        if (parser == null) {
            log.error("未找到支持协议 {} 的消息解析器。", protocol.getName());
            throw new MessageParsingException("未找到支持协议 [" + protocol.getName() + "] 的消息解析器。");
        }
        return parser;
    }
}