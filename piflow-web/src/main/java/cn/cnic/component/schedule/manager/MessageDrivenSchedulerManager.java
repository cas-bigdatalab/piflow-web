package cn.cnic.component.schedule.manager;

import cn.cnic.common.Eunm.MessageProtocol;
import cn.cnic.component.schedule.domain.TaskTriggerInstanceDomain;
import cn.cnic.component.schedule.entity.MessageTriggerTaskDefinition;
import cn.cnic.component.schedule.entity.TaskTriggerInstance;
import cn.cnic.component.schedule.listener.IMessageListener;
import cn.cnic.component.schedule.factory.MessageListenerFactory;
import cn.cnic.component.schedule.processor.IMessageProcessor;
import cn.cnic.component.schedule.message.IMessage;
import cn.cnic.component.schedule.trigger.WorkflowTriggerService;
import cn.cnic.component.schedule.utils.JsonConverter;
import cn.cnic.component.schedule.vo.TaskExecutionInfo;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.log4j.Log4j2;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Component
public class MessageDrivenSchedulerManager {

    private static final int QUEUE_CAPACITY = 1000;
    private static final int TRIGGER_THREAD_NUM = 5;
    private final PriorityBlockingQueue<TriggerTask> triggerTaskQueue = new PriorityBlockingQueue<>(QUEUE_CAPACITY);
    private final ExecutorService triggerExecutor = Executors.newFixedThreadPool(TRIGGER_THREAD_NUM, r -> new Thread(r, "TriggerExecutor"));

    private final Map<Long, IMessageListener> listenerMap = new ConcurrentHashMap<>();
    
    // 用于标记正在处理的PENDING任务，避免重复处理
    private final Map<Long, Long> pendingProcessingMap = new ConcurrentHashMap<>();

    @Autowired
    private TriggerConcurrencyManager concurrencyManager;

    @Autowired
    private TaskTriggerInstanceDomain triggerInstanceDomain;

    @Autowired
    private MessageListenerFactory listenerFactory;

    @Autowired
    private IMessageProcessor messageProcessor;

    @Autowired
    private WorkflowTriggerService workflowTriggerService;

    @PostConstruct
    public void init() {
        log.info("[SchedulerManager-INIT] 初始化消息驱动调度管理器...");
        for (int i = 0; i < TRIGGER_THREAD_NUM; i++) {
            triggerExecutor.submit(new TriggerWorker());
        }
        log.info("[SchedulerManager-INIT] 任务消费线程已启动 {} 个。", TRIGGER_THREAD_NUM);
    }

    @PreDestroy
    public void destroy() {
        log.info("[SchedulerManager-DESTROY] 销毁消息驱动调度管理器...");
        listenerMap.values().forEach(IMessageListener::stopListening);
        listenerMap.clear();

        triggerExecutor.shutdown();
        try {
            if (!triggerExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                triggerExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            triggerExecutor.shutdownNow();
        }
        log.info("[TRIGGER_WORKER] 所有任务线程已关闭。");
        log.info("[SchedulerManager-DESTROY] 消息驱动调度管理器已销毁。");
    }

    /**
     * 动态启动或更新一个消息监听器。
     * 如果已存在，会先停止再启动，实现热更新。
     */
    public Boolean startListener(MessageTriggerTaskDefinition definition) {
        Long definitionId = definition.getId();
        IMessageListener existingListener = listenerMap.get(definitionId);


        // 如果已经存在监听器，并且其配置与新配置相同，则不进行操作
        if (existingListener != null) {
            Date existingUpdateTime = existingListener.getDefinition().getUpdateTime();
            Date newUpdateTime = definition.getUpdateTime();
            if (existingUpdateTime != null && newUpdateTime != null &&
                    existingUpdateTime.getTime() == newUpdateTime.getTime()) {
                log.info("[LISTENER_START_SKIP] 监听器已存在且配置无变化 (基于毫秒时间戳)，sourceId={}", definitionId);
                return true;
            }
        }

        // 如果存在，先停止旧的
        if (existingListener != null) {
            log.info("[LISTENER_UPDATE_STOP] 检测到配置更新，停止旧监听器。sourceId={}", definitionId);
            // 然后停止监听器
            existingListener.stopListening();
            // 从map中移除旧实例
            listenerMap.remove(definitionId);
            // 清理旧的Semaphore，避免状态不一致
            concurrencyManager.cleanupOldSemaphore(definitionId);
        }

        IMessageListener listener = listenerFactory.createListener(definition.getProtocol());
        if (listener != null) {
            try {
                // 初始化监听器配置
                listener.init(definition);
                //初始化并发控制器
                int runningCount = triggerInstanceDomain.countByMessageSourceIdAndStatus(definitionId, "RUNNING");
                Integer concurrencyLimit = definition.getConcurrencyLimit();
                if (concurrencyLimit == null || concurrencyLimit <=0) {
                    concurrencyLimit = Integer.MAX_VALUE;
                }
                concurrencyManager.reacquireOnRestart(definitionId, concurrencyLimit, runningCount);

                // 注入消息处理器
                listener.setMessageProcessor(messageProcessor);
                // 启动监听
                listener.startListening();
                listenerMap.put(definitionId, listener);
                
                // 启动完成后，触发PENDING任务检测
                log.info("[LISTENER_START_COMPLETE] 监听器启动完成，触发PENDING任务检测。sourceId={}, isHotUpdate={}", 
                        definitionId, existingListener != null);
                triggerPendingTaskCheck(definitionId, concurrencyLimit);
                
                log.info("[LISTENER_START] 动态启动/更新监听器成功，sourceId={}, protocol={}", definitionId, definition.getProtocol().getName());
            } catch (Exception e) {
                log.error("[LISTENER_START_FAIL] 启动/更新监听器失败，sourceId={}, protocol={}, error={}",
                        definitionId, definition.getProtocol().getName(), e.getMessage(), e);
                // 启动失败则从map中移除，避免僵尸实例
                listenerMap.remove(definitionId);
                // 确保在启动失败时也尝试停止，避免资源泄露
                if (listener != null) {
                    listener.stopListening();
                }
                return false;
            }
        } else {
            log.error("[LISTENER_START_FAIL] 未找到协议 {} 对应的监听器实现。sourceId={}",
                    definition.getProtocol() != null ? definition.getProtocol().getName() : "UNKNOWN", definitionId);
            return false;
        }
        return true;
    }

    /**
     * 动态停止一个消息监听器。
     */
    public void stopListener(Long messageSourceId) {
        IMessageListener listener = listenerMap.remove(messageSourceId);
        if (listener != null) {
            try {
                listener.stopListening();
                log.info("[LISTENER_STOP] 动态停止监听器，sourceId={}", messageSourceId);
            } catch (Exception e) {
                log.error("[LISTENER_STOP_FAIL] 停止监听器失败，sourceId={}, error={}",
                        messageSourceId, e.getMessage(), e);
            }
        } else {
            log.info("[LISTENER_STOP_SKIP] 监听器未运行或不存在，sourceId={}", messageSourceId);
        }
    }

    /**
     * 获取当前所有正在管理中的监听器及其对应的配置。
     * 供 ListenerLifecycleManager 用于比对数据库状态。
     * @return Map, key为sourceId, value为MessageTriggerTaskDefinition
     */
    public Map<Long, MessageTriggerTaskDefinition> getManagedListenerConfigs() {
        return listenerMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getDefinition() // 获取监听器内部存储的配置
                ));
    }

    /**
     * 获取当前listenerMap是否存在该实例
     * @return Boolean
     */
    public Boolean isExistInListenerMap(Long sourceId) {
        return listenerMap.get(sourceId) != null;
    }

    /**
     * 触发指定定义ID的PENDING任务检测。
     * 在监听器启动完成时调用（包括热更新和重新启动），立即检查并处理PENDING状态的任务。
     *
     * @param definitionId     任务定义的唯一ID
     * @param concurrencyLimit 并发限制数量
     */
    public void triggerPendingTaskCheck(Long definitionId, Integer concurrencyLimit) {
        try {
            log.info("[TRIGGER_PENDING_CHECK] 开始触发PENDING任务检测。definitionId={}", definitionId);
            
            // 检查是否已经在处理中，避免重复处理
            Long currentTime = System.currentTimeMillis();
            Long lastProcessTime = pendingProcessingMap.get(definitionId);
            if (lastProcessTime != null && (currentTime - lastProcessTime) < 5000) { // 5秒内不重复处理
                log.info("[TRIGGER_PENDING_CHECK] 该定义正在处理中，跳过重复处理。definitionId={}", definitionId);
                return;
            }
            
            // 标记开始处理
            pendingProcessingMap.put(definitionId, currentTime);
            
            // 查询该定义的PENDING状态任务
            List<TaskTriggerInstance> pendingTasks = triggerInstanceDomain.selectByStatus(
                    definitionId, "PENDING");
            
            if (pendingTasks.isEmpty()) {
                log.info("[TRIGGER_PENDING_CHECK] 没有PENDING状态的任务需要处理。definitionId={}", definitionId);
                return;
            }

            log.info("[TRIGGER_PENDING_CHECK] 发现 {} 个PENDING状态的任务，开始处理。definitionId={}", 
                    pendingTasks.size(), definitionId);

            // 获取该定义的配置
            MessageTriggerTaskDefinition definition = null;
            IMessageListener listener = listenerMap.get(definitionId);
            if (listener != null) {
                definition = listener.getDefinition();
            }
            
            if (definition == null) {
                log.warn("[TRIGGER_PENDING_CHECK] 无法找到定义配置，跳过处理。definitionId={}", definitionId);
                return;
            }

            if (concurrencyLimit == null || concurrencyLimit <= 0) {
                log.info("[TRIGGER_PENDING_CHECK] 定义未配置并发限制，将所有PENDING任务转为RUNNING。definitionId={}", definitionId);
                // 如果没有并发限制，直接将所有PENDING任务转为RUNNING
                for (TaskTriggerInstance task : pendingTasks) {
                    processPendingTaskImmediately(task, definition);
                }
                return;
            }

            // 按创建时间排序，先处理早的
            pendingTasks.sort((t1, t2) -> t1.getCreateTime().compareTo(t2.getCreateTime()));
            
            // 尝试处理PENDING任务
            for (TaskTriggerInstance task : pendingTasks) {
                boolean acquired = concurrencyManager.tryAcquire(definitionId, task.getTriggerInstanceId(), concurrencyLimit);
                if (acquired) {
                    log.info("[TRIGGER_PENDING_CHECK] 成功获取许可，处理PENDING任务。triggerInstanceId={}, definitionId={}", 
                            task.getTriggerInstanceId(), definitionId);
                    processPendingTaskImmediately(task, definition);
                } else {
                    log.debug("[TRIGGER_PENDING_CHECK] 无法获取许可，任务继续等待。triggerInstanceId={}, definitionId={}", 
                            task.getTriggerInstanceId(), definitionId);
                    // 无法获取许可，停止处理后续任务
                    break;
                }
            }

        } catch (Exception e) {
            log.error("[TRIGGER_PENDING_CHECK_ERROR] 触发PENDING任务检测时发生异常。definitionId={}", definitionId, e);
        }
    }

    /**
     * 立即处理单个PENDING任务。
     * 
     * @param task PENDING状态的任务实例
     * @param definition 任务定义
     */
    private void processPendingTaskImmediately(TaskTriggerInstance task, MessageTriggerTaskDefinition definition) {
        try {
            String triggerInstanceId = task.getTriggerInstanceId();
            Long definitionId = definition.getId();
            
            log.info("[PENDING_TASK_PROCESS_IMMEDIATE] 开始触发PENDING任务的工作流。triggerInstanceId={}", triggerInstanceId);
            
            // 触发工作流
            String processId = "111"; // 临时硬编码，实际应该调用工作流引擎
            
            if (processId != null) {
                // 触发成功，更新状态为 RUNNING
                task.setStatus("RUNNING");
                task.setProcessId(processId);
                task.setUpdateTime(new Date(System.currentTimeMillis()));
                
                // 更新执行列表
                String executionListJson = task.getExecutionList();
                List<TaskExecutionInfo> taskExecutionList = JsonConverter.jsonToList(executionListJson, TaskExecutionInfo.class);
                if (!taskExecutionList.isEmpty()) {
                    TaskExecutionInfo lastExecution = taskExecutionList.get(taskExecutionList.size() - 1);
                    lastExecution.setStatus("RUNNING");
                    taskExecutionList.set(taskExecutionList.size() - 1, lastExecution);
                    task.setExecutionList(JsonConverter.listToJson(taskExecutionList));
                }
                
                triggerInstanceDomain.update(task);
                log.info("[PENDING_TASK_SUCCESS_IMMEDIATE] PENDING任务成功转为RUNNING状态。triggerInstanceId={}, processId={}", 
                        triggerInstanceId, processId);
            } else {
                // 触发失败
                task.setStatus("FAILED");
                task.setErrorMessage("工作流引擎未返回有效的Job ID");
                task.setUpdateTime(new Date(System.currentTimeMillis()));
                
                // 释放许可
                int runningCount = triggerInstanceDomain.countByMessageSourceIdAndStatus(definitionId, "RUNNING");
                concurrencyManager.release(definitionId, definition.getConcurrencyLimit(), runningCount);
                
                triggerInstanceDomain.update(task);
                log.error("[PENDING_TASK_FAIL_IMMEDIATE] PENDING任务触发失败。triggerInstanceId={}", triggerInstanceId);
            }
            
        } catch (Exception e) {
            log.error("[PENDING_TASK_ERROR_IMMEDIATE] 处理PENDING任务时发生异常。triggerInstanceId={}", 
                    task.getTriggerInstanceId(), e);
            
            // 设置失败状态
            task.setStatus("FAILED");
            task.setErrorMessage("处理PENDING任务时发生异常: " + e.getMessage());
            task.setUpdateTime(new Date(System.currentTimeMillis()));
            
            try {
                triggerInstanceDomain.update(task);
            } catch (Exception updateException) {
                log.error("[PENDING_TASK_UPDATE_ERROR_IMMEDIATE] 更新失败状态时发生异常。triggerInstanceId={}", 
                        task.getTriggerInstanceId(), updateException);
            }
        }
    }


    /**
     * 提交一个触发任务到队列。
     *
     * @param definition  消息源的配置定义
     * @param message     经过处理的标准化IMessage对象
     * @param ackCallback
     * @return 是否成功加入队列
     */
    public boolean submitTriggerTask(MessageTriggerTaskDefinition definition, IMessage message, String triggerInstanceId, Runnable ackCallback) {
        TriggerTask task = new TriggerTask(definition, message, triggerInstanceId, ackCallback);
        try {
            boolean offered = triggerTaskQueue.offer(task);
            if (offered) {
                log.info("[SUBMIT_SUCCESS] 任务已成功加入队列。triggerInstanceId={}, sourceId={}, messageId={}, queueSize={}",
                        triggerInstanceId, definition.getId(), message.getMessageId(), triggerTaskQueue.size());
            } else {
                log.warn("[SUBMIT_FAIL] 任务队列已满，无法加入。triggerInstanceId={}, sourceId={}, messageId={}, queueCapacity={}",
                       triggerInstanceId, definition.getId(), message.getMessageId(), QUEUE_CAPACITY);
            }
            return offered;
        } catch (Exception e) {
            log.error("[SUBMIT_ERROR] 提交任务到队列时发生错误。triggerInstanceId={}, sourceId={}, messageId={}, error={}",
                    triggerInstanceId, definition.getId(), message.getMessageId(), e.getMessage(), e);
            return false;
        }
    }
    /**
     * 根据protocol，messageId检测是否是重复消息
     */
    public boolean checkIsDuplicateMessage(MessageProtocol protocol, String messageId) {
        //根据protocol，messageId检测是否是重复消息
        int taskTriggerInstances = triggerInstanceDomain.checkIsDuplicateMessage(protocol, messageId);
        if (taskTriggerInstances == 0) {
            return false;
        }
        return true;
    }

    /**
     * 内部类：代表一个待触发的工作流任务。
     */
    private static class TriggerTask implements Comparable<TriggerTask> {
        private static final java.util.concurrent.atomic.AtomicLong sequencer = new java.util.concurrent.atomic.AtomicLong();

        final MessageTriggerTaskDefinition definition;
        final IMessage message;
        final Date creationTime;
        final String triggerInstanceId;
        final Runnable ackAction;
        final long seqNum; // 新增一个唯一的序号


        TriggerTask(MessageTriggerTaskDefinition definition, IMessage message, String triggerInstanceId, Runnable ackCallback) {
            this.definition = definition;
            this.message = message;
            this.triggerInstanceId = triggerInstanceId;
            this.creationTime = new Date(System.currentTimeMillis());
            this.ackAction = ackCallback;
            this.seqNum = sequencer.getAndIncrement(); // 获取并增加序号

        }

        @Override
        public int compareTo(TriggerTask other) {
            int timeCompare = this.creationTime.compareTo(other.creationTime);
            if (timeCompare != 0) {
                return timeCompare;
            }
            // 如果创建时间完全相同（同一毫秒内），则按序号比较，序号小的优先级高
            // 这确保了严格的 FIFO，并避免了 compareTo 返回 0 带来的潜在问题
            return Long.compare(this.seqNum, other.seqNum);
        }
    }

    /**
     * 内部类：工作流触发执行器。
     */
    private class TriggerWorker implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    TriggerTask task = triggerTaskQueue.take();
                    ThreadContext.putIfNull("sourceId", String.valueOf(task.definition.getId()));
                    ThreadContext.putIfNull("triggerInstanceId", task.triggerInstanceId);
                    ThreadContext.putIfNull("workflowId", task.definition.getTargetWorkflowId());
                    ThreadContext.putIfNull("messageId", task.message.getMessageId());

                    log.info("[TRIGGER_START] 从队列中获取任务。triggerInstanceId={}, sourceId={}, messageId={}, queueSize={}",
                           task.triggerInstanceId, task.definition.getId(), task.message.getMessageId(), triggerTaskQueue.size());

                    workflowTriggerService.triggerByMessage(task.definition, task.message,  task.creationTime, task.triggerInstanceId, task.ackAction);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log.error("[TRIGGER_WORKER_ERROR] 任务执行过程中发生错误。error={}", e.getMessage());
                } finally {
                    ThreadContext.remove("sourceId");
                    ThreadContext.remove("triggerInstanceId");
                    ThreadContext.remove("workflowId");
                    ThreadContext.remove("messageId");
                }
            }
        }
    }
}