package cn.cnic.component.schedule.listener;

import cn.cnic.component.schedule.entity.MessageTriggerTaskDefinition;
import cn.cnic.component.schedule.manager.MessageDrivenSchedulerManager; // 引入调度管理器
import cn.cnic.component.schedule.processor.IMessageProcessor; // 引入消息处理器接口

/**
 * 消息监听器接口。
 * 定义了不同消息队列（如RabbitMQ, Kafka）监听器的通用行为。
 */
public interface IMessageListener {

    /**
     * 初始化监听器。
     * 在此方法中，监听器获取其配置（如连接信息、监听目标、过滤规则）和消息处理器实例。
     *
     * @param definition 消息触发任务的定义
     */
    void init(MessageTriggerTaskDefinition definition) throws Exception; // 移除 manager 参数，因为 manager 会调用 setProcessor

    /**
     * 设置消息处理器。
     * @param processor 消息处理器实例
     */
    void setMessageProcessor(IMessageProcessor processor);

    /**
     * 启动消息监听，开始消费消息。
     * 此方法应是非阻塞的，通常会在后台线程中启动消费循环。
     */
    void startListening();

    /**
     * 停止消息监听，优雅地关闭连接和释放资源。
     * 此方法应是阻塞的，直到所有资源被关闭。
     */
    void stopListening();

    /**
     * 获取此监听器管理的消息源ID。
     * @return 消息源ID
     */
    Long getMessageSourceId();

    /**
     * 获取此监听器的协议类型。
     * @return 协议类型字符串
     */
    String getProtocolType();

    /**
     * **新增：** 获取此监听器当前正在使用的消息触发任务定义。
     * @return 当前的消息触发任务定义
     */
    MessageTriggerTaskDefinition getDefinition();
}