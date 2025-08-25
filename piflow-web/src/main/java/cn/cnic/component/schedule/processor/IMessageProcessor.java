package cn.cnic.component.schedule.processor;

import cn.cnic.component.schedule.entity.MessageTriggerTaskDefinition;

/**
 * 消息处理器接口。
 * 负责对监听器接收到的原始消息进行解析、过滤、上下文映射，并最终提交给调度器。
 */
public interface IMessageProcessor {
    /**
     * 处理接收到的原始消息。
     * 该方法将执行以下步骤：
     * 1. 将原始消息数据解析为标准化IMessage对象。
     * 2. 根据定义进行消息过滤。
     * 3. 将处理后的任务提交给MessageDrivenSchedulerManager。
     *
     * @param definition       消息源的配置定义
     * @param rawMessageData   原始消息数据（具体类型取决于MQ，例如 RabbitMQ 的 Delivery 和 Properties，Kafka 的 ConsumerRecord）
     * @param rawContentString 原始消息内容的字符串表示（用于日志和存储）
     * @param ackCallback
     * @throws MessageProcessingException 如果处理过程中发生错误（解析、过滤、映射或提交失败）
     */
    void process(MessageTriggerTaskDefinition definition, Object rawMessageData, String rawContentString, String triggerInstanceId, Runnable ackCallback) throws MessageProcessingException;
}