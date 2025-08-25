package cn.cnic.component.schedule.factory;

import cn.cnic.common.Eunm.MessageProtocol;
import cn.cnic.component.schedule.listener.IMessageListener;
import cn.cnic.component.schedule.listener.Kafka.KafkaMessageListener;
import cn.cnic.component.schedule.listener.RabbitMQ.RabbitMQMessageListener; // 导入具体的监听器实现类
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息监听器工厂，用于根据协议类型创建具体的IMessageListener实例。
 * 实现ApplicationContextAware以便从Spring容器获取原型Bean。
 */
@Log4j2
@Component
public class MessageListenerFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final Map<MessageProtocol, Class<? extends IMessageListener>> listenerRegistry = new HashMap<>();

    // 注册所有支持的监听器实现
    public MessageListenerFactory() {
        listenerRegistry.put(MessageProtocol.RABBITMQ, RabbitMQMessageListener.class);
        listenerRegistry.put(MessageProtocol.KAFKA, KafkaMessageListener.class);
        log.info("MessageListenerFactory 初始化完成，已注册 {} 种消息协议监听器。", listenerRegistry.size());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 根据协议类型创建IMessageListener实例。
     * 使用Spring的ApplicationContext获取原型(prototype)或每次都创建新实例的Bean。
     * @param protocolType 消息协议类型
     * @return 对应的IMessageListener实例，如果不支持则返回null
     */
    public IMessageListener createListener(MessageProtocol protocolType) {
        Class<? extends IMessageListener> listenerClass = listenerRegistry.get(protocolType);
        if (listenerClass != null) {
            try {
                return applicationContext.getBean(listenerClass);
            } catch (BeansException e) {
                log.error("无法从Spring容器获取监听器实例: {}。错误: {}", protocolType, e.getMessage(), e);
                return null;
            }
        }
        log.warn("不支持的消息协议类型：{}，无法创建监听器。", protocolType);
        return null;
    }
}