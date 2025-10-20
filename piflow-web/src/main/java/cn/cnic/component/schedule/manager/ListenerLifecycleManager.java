package cn.cnic.component.schedule.manager;

import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.component.process.domain.ProcessDomain;
import cn.cnic.component.schedule.domain.MessageSourceConfigDomain;
import cn.cnic.component.schedule.domain.TaskTriggerInstanceDomain;
import cn.cnic.component.schedule.entity.MessageTriggerTaskDefinition;
import cn.cnic.component.schedule.entity.TaskTriggerInstance;
import cn.cnic.component.schedule.mapper.TaskTriggerInstanceMapper;
import cn.cnic.component.schedule.utils.JsonConverter;
import cn.cnic.component.schedule.vo.TaskExecutionInfo;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.EnableScheduling; // 启用定时任务
import org.springframework.scheduling.annotation.Scheduled; // 标记定时任务方法
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

/**
 * 监听器生命周期管理定时器。
 * 定期扫描数据库中的 MessageTriggerTaskDefinition，并与 MessageDrivenSchedulerManager
 * 中实际运行的监听器状态进行同步，确保配置与运行时一致。
 */
@Log4j2
@Component
@EnableScheduling
public class ListenerLifecycleManager implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private MessageSourceConfigDomain definitionDomain;
    @Autowired
    private TaskTriggerInstanceDomain triggerInstanceDomain;
    @Autowired
    private ProcessDomain processDomain;
    @Autowired
    private MessageDrivenSchedulerManager schedulerManager;
    @Autowired
    private TriggerConcurrencyManager concurrencyManager;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("[ListenerLifecycleManager-INIT] 首次同步监听器状态...");
        initialize();
        syncListenerStates();
        syncTriggerTaskStates();
        processPendingTasks();
    }

    /**
     * 定时任务：每隔一段时间（例如，60秒）执行一次监听器状态同步。
     * 可以根据实际需求调整 cron 表达式，例如 "0 * * * * ?" 表示每分钟执行一次。
     */
    @Scheduled(fixedRate = 60000) // 每 60 秒执行一次
    public void scheduledSync() {
        log.info("[ListenerLifecycleManager-SCHEDULED] 定时同步监听器状态...");
        syncListenerStates();
        syncTriggerTaskStates();
        processPendingTasks();
    }

    public void initialize() {
        log.info("开始按任务定义恢复 Semaphore 状态...");

        // 1. 查询所有“正在运行中”的任务
        List<TaskTriggerInstance> runningTasks = triggerInstanceDomain.selectByStatus(null ,"RUNNING");
        if (runningTasks.isEmpty()) {
            log.info("没有正在运行的任务，Semaphore 状态无需恢复。");
            return;
        }

        // 2. 按 definitionId 分组并计数
        Map<Long, Long> runningCountsByDefinition = runningTasks.stream()
                .collect(Collectors.groupingBy(
                        TaskTriggerInstance::getMessageSourceId, // 假设 TaskTriggerInstance 有 getDefinitionId() 方法
                        Collectors.counting()
                ));

        log.warn("发现 {} 个定义存在未完成的任务。将逐一恢复其并发许可状态。", runningCountsByDefinition.size());

        // 3. 遍历每个分组，恢复对应的 Semaphore
        runningCountsByDefinition.forEach((definitionId, count) -> {
            // 从数据库获取该定义的并发配置
            MessageTriggerTaskDefinition definition = definitionDomain.getById(definitionId);
            if (definition == null) {
                log.error("无法找到正在运行任务对应的定义，无法恢复信号量。definitionId={}", definitionId);
                return;
            }

            Integer concurrencyLimit = definition.getConcurrencyLimit();
            // 如果定义没有设置并发限制，则跳过
            if (concurrencyLimit == null || concurrencyLimit <= 0) {
                log.info("定义 {} 未配置并发限制，跳过恢复。", definitionId);
                return;
            }

            // 调用管理器来恢复状态
            concurrencyManager.reacquireOnRestart(definitionId, concurrencyLimit, count.intValue());
        });

        log.info("Semaphore 状态恢复流程完成。");
    }

    /**
     * 同步监听器状态的核心逻辑。
     */
    private void syncListenerStates() {
        try {
            // 1. 从数据库获取所有消息触发任务定义
            List<MessageTriggerTaskDefinition> dbDefinitions = definitionDomain.getAll(null, null, null);
            // 将数据库定义转换为ID到定义的Map，方便查找
            Map<Long, MessageTriggerTaskDefinition> dbDefinitionMap = dbDefinitions.stream()
                    .collect(Collectors.toMap(MessageTriggerTaskDefinition::getId, def -> def));

            // 2. 获取 MessageDrivenSchedulerManager 当前管理的监听器配置
            Map<Long, MessageTriggerTaskDefinition> currentManagedConfigs = schedulerManager.getManagedListenerConfigs();

            // 3. 处理需要停止或更新的监听器
            // 遍历当前管理中的监听器，检查其在DB中的状态
            for (Map.Entry<Long, MessageTriggerTaskDefinition> entry : currentManagedConfigs.entrySet()) {
                Long sourceId = entry.getKey();
                MessageTriggerTaskDefinition managedConfig = entry.getValue();
                MessageTriggerTaskDefinition dbConfig = dbDefinitionMap.get(sourceId);

                if (dbConfig == null) {
                    // DB中已不存在该配置，需要停止并移除监听器
                    log.info("[SYNC-STOP] DB中已删除，停止监听器。sourceId={}", sourceId);
                    schedulerManager.stopListener(sourceId);
                } else if ("INACTIVE".equalsIgnoreCase(dbConfig.getStatus())) {
                    // DB中状态为INACTIVE，但监听器仍在运行，需要停止
                    log.info("[SYNC-STOP] DB状态为INACTIVE，停止监听器。sourceId={}", sourceId);
                    schedulerManager.stopListener(sourceId);
                } else if (!dbConfig.getUpdateTime().equals(managedConfig.getUpdateTime()) || !dbConfig.equals(managedConfig)) {
                    // DB中的配置有更新（根据updateTime或完整对象比较），需要热更新（先停止再启动）
                    // 这里的 !dbConfig.equals(managedConfig) 是为了确保即使updateTime相同，如果其他字段有变化也能检测到
                    log.info("[SYNC-UPDATE] DB配置已更新，热更新监听器。sourceId={}", sourceId);
                    // schedulerManager.startListener 会处理停止旧的再启动新的
                    schedulerManager.startListener(dbConfig);
                }
            }

            // 4. 处理需要启动的新监听器
            // 遍历DB中的定义，检查其是否已在运行且状态匹配
            for (MessageTriggerTaskDefinition dbConfig : dbDefinitions) {
                Long sourceId = dbConfig.getId();
                if (!"INACTIVE".equalsIgnoreCase(dbConfig.getStatus())) {
                    if (!currentManagedConfigs.containsKey(sourceId)) {
                        // DB中不是INACTIVE，但当前未运行，需要启动
                        log.info("[SYNC-START] DB状态不是INACTIVE，启动新监听器。sourceId={}", sourceId);
                        schedulerManager.startListener(dbConfig);
                    } else {
                        // 已经处理过更新的场景，这里只是确保如果由于某种原因监听器没有运行但DB状态是ACTIVE的被启动
                        // 或者简单地再次调用startListener，它内部会判断是否需要重启
                        // 考虑到上面的更新逻辑已经包含了这个，这里可以简化，只处理完全新的ACTIVE
                        // 实际上，MessageDrivenSchedulerManager.startListener 已经包含了检查和热更新逻辑
                        // 所以这里只要是ACTIVE的，都尝试调用一次startListener即可，它内部会判断是否跳过或更新
                        log.debug("[SYNC-CHECK] 检查ACTIVE监听器。sourceId={}", sourceId);
                        schedulerManager.startListener(dbConfig);
                    }
                }
            }
            log.info("[ListenerLifecycleManager-SYNC] 监听器状态同步完成。");

        } catch (Exception e) {
            log.error("[ListenerLifecycleManager-SYNC-ERROR] 同步监听器状态时发生错误: {}", e.getMessage(), e);
        }
    }

    private void syncTriggerTaskStates() {
        List<TaskTriggerInstance> taskTriggerInstances = triggerInstanceDomain.selectByStatus(null, "RUNNING");
        for (TaskTriggerInstance taskTriggerInstance : taskTriggerInstances) {
            String triggerInstanceId = taskTriggerInstance.getTriggerInstanceId();
            MDC.put("triggerInstanceId", triggerInstanceId);
            MDC.put("sourceId", String.valueOf(taskTriggerInstance.getMessageSourceId()));
            MDC.put("workflowId", taskTriggerInstance.getTargetWorkflowId());
            log.info("[CHECK_PROCESS] 进行PROCESS执行结果查询，triggerInstanceId={}, sourceId={}, processId={}", triggerInstanceId,
                    taskTriggerInstance.getMessageSourceId(), taskTriggerInstance.getProcessId());
            ProcessState state = processDomain.getProcessStateByIdIgnoreFlag(taskTriggerInstance.getProcessId());
            Long definitionId = taskTriggerInstance.getMessageSourceId();
            Integer concurrencyLimit = definitionDomain.getConcurrencyLimitById(definitionId);
            if (ProcessState.isFinalFailState(state)) {
                log.info("[CHECK_PROCESS] PROCESS执行结果为失败，triggerInstanceId={}, sourceId={}, processId={}", triggerInstanceId,
                        taskTriggerInstance.getMessageSourceId(), taskTriggerInstance.getProcessId());
                taskTriggerInstance.setStatus("FAILED");

                int runningCount = triggerInstanceDomain.countByMessageSourceIdAndStatus(definitionId, "RUNNING");
                concurrencyManager.release(definitionId, concurrencyLimit, runningCount);
                log.info("[SEMAPHORE_RELEASE] 流水线已完成，释放许可。triggerInstanceId={}",
                        triggerInstanceId);
            }else if (state == ProcessState.COMPLETED) {
                taskTriggerInstance.setStatus("SUCCESS");
                log.info("[CHECK_PROCESS] PROCESS执行结果为成功，triggerInstanceId={}, sourceId={}, processId={}", triggerInstanceId,
                        taskTriggerInstance.getMessageSourceId(), taskTriggerInstance.getProcessId());

                int runningCount = triggerInstanceDomain.countByMessageSourceIdAndStatus(definitionId, "RUNNING");
                concurrencyManager.release(definitionId, concurrencyLimit, runningCount);
                log.info("[SEMAPHORE_RELEASE] 流水线已完成，释放许可。triggerInstanceId={}",
                        triggerInstanceId);
            }
            Date now = new Date(System.currentTimeMillis());
            taskTriggerInstance.setUpdateTime(now);
            long durationMillis = now.getTime() - taskTriggerInstance.getCreateTime().getTime();
            String executionListJson = taskTriggerInstance.getExecutionList();
            List<TaskExecutionInfo> taskExecutionList = JsonConverter.jsonToList(executionListJson, TaskExecutionInfo.class);
            if (taskExecutionList.size() > 0) {
                int lastIndex = taskExecutionList.size()-1;
                TaskExecutionInfo lastTaskExecutionInfo = taskExecutionList.get(lastIndex);
                lastTaskExecutionInfo.setDurationMillis(durationMillis);
                lastTaskExecutionInfo.setStatus(taskTriggerInstance.getStatus());
                taskExecutionList.set(lastIndex, lastTaskExecutionInfo);
            }
            taskTriggerInstance.setExecutionList(JsonConverter.listToJson(taskExecutionList));
            taskTriggerInstance.setDurationMillis(durationMillis);
            triggerInstanceDomain.update(taskTriggerInstance);
            MDC.remove("triggerInstanceId");
            MDC.remove("sourceId");
            MDC.remove("workflowId");
        }
    }

    /**
     * 处理PENDING状态的任务。
     * 定期检查PENDING状态的任务，尝试获取许可并触发工作流。
     */
    private void processPendingTasks() {
        try {
            // 1. 查询所有PENDING状态的任务
            List<TaskTriggerInstance> pendingTasks = triggerInstanceDomain.selectByStatus(null, "PENDING");
            if (pendingTasks.isEmpty()) {
                log.debug("[PENDING_PROCESS] 没有PENDING状态的任务需要处理。");
                return;
            }

            log.info("[PENDING_PROCESS] 发现 {} 个PENDING状态的任务，开始处理。", pendingTasks.size());

            // 2. 按definitionId分组处理
            Map<Long, List<TaskTriggerInstance>> pendingTasksByDefinition = pendingTasks.stream()
                    .collect(Collectors.groupingBy(TaskTriggerInstance::getMessageSourceId));

            // 3. 遍历每个定义的任务
            for (Map.Entry<Long, List<TaskTriggerInstance>> entry : pendingTasksByDefinition.entrySet()) {
                Long definitionId = entry.getKey();
                List<TaskTriggerInstance> definitionPendingTasks = entry.getValue();

                // 获取该定义的并发配置
                MessageTriggerTaskDefinition definition = definitionDomain.getById(definitionId);
                if (definition == null) {
                    log.warn("[PENDING_PROCESS] 无法找到定义，跳过处理。definitionId={}", definitionId);
                    continue;
                }

                Integer concurrencyLimit = definition.getConcurrencyLimit();
                if (concurrencyLimit == null || concurrencyLimit <= 0) {
                    log.info("[PENDING_PROCESS] 定义未配置并发限制，将所有PENDING任务转为RUNNING。definitionId={}", definitionId);
                    // 如果没有并发限制，直接将所有PENDING任务转为RUNNING
                    for (TaskTriggerInstance task : definitionPendingTasks) {
                        processPendingTask(task, definition, true);
                    }
                    continue;
                }

                // 4. 尝试处理PENDING任务（按创建时间排序，先处理早的）
                definitionPendingTasks.sort((t1, t2) -> t1.getCreateTime().compareTo(t2.getCreateTime()));
                
                for (TaskTriggerInstance task : definitionPendingTasks) {
                    // 尝试获取许可
                    boolean acquired = concurrencyManager.tryAcquire(definitionId, task.getTriggerInstanceId(), concurrencyLimit);
                    if (acquired) {
                        log.info("[PENDING_PROCESS] 成功获取许可，处理PENDING任务。triggerInstanceId={}, definitionId={}", 
                                task.getTriggerInstanceId(), definitionId);
                        processPendingTask(task, definition, true);
                    } else {
                        log.debug("[PENDING_PROCESS] 无法获取许可，任务继续等待。triggerInstanceId={}, definitionId={}", 
                                task.getTriggerInstanceId(), definitionId);
                        // 无法获取许可，停止处理后续任务（因为按时间排序，后续任务应该等待）
                        break;
                    }
                }
            }

        } catch (Exception e) {
            log.error("[PENDING_PROCESS_ERROR] 处理PENDING任务时发生异常: {}", e.getMessage(), e);
        }
    }

    /**
     * 处理单个PENDING任务。
     * 
     * @param task PENDING状态的任务实例
     * @param definition 任务定义
     * @param acquired 是否已获取并发许可
     */
    private void processPendingTask(TaskTriggerInstance task, MessageTriggerTaskDefinition definition, boolean acquired) {
        try {
            String triggerInstanceId = task.getTriggerInstanceId();
            Long definitionId = definition.getId();
            
            if (acquired) {
                // 获取到许可，触发工作流
                log.info("[PENDING_TASK_PROCESS] 开始触发PENDING任务的工作流。triggerInstanceId={}", triggerInstanceId);
                
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
                    log.info("[PENDING_TASK_SUCCESS] PENDING任务成功转为RUNNING状态。triggerInstanceId={}, processId={}", 
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
                    log.error("[PENDING_TASK_FAIL] PENDING任务触发失败。triggerInstanceId={}", triggerInstanceId);
                }
            } else {
                log.warn("[PENDING_TASK_SKIP] 无法获取许可，跳过PENDING任务处理。triggerInstanceId={}", triggerInstanceId);
            }
            
        } catch (Exception e) {
            log.error("[PENDING_TASK_ERROR] 处理PENDING任务时发生异常。triggerInstanceId={}", 
                    task.getTriggerInstanceId(), e);
            
            // 设置失败状态
            task.setStatus("FAILED");
            task.setErrorMessage("处理PENDING任务时发生异常: " + e.getMessage());
            task.setUpdateTime(new Date(System.currentTimeMillis()));
            
            try {
                triggerInstanceDomain.update(task);
            } catch (Exception updateException) {
                log.error("[PENDING_TASK_UPDATE_ERROR] 更新失败状态时发生异常。triggerInstanceId={}", 
                        task.getTriggerInstanceId(), updateException);
            }
        }
    }

}