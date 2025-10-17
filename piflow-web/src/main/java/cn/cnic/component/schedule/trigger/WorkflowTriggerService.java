package cn.cnic.component.schedule.trigger;

import cn.cnic.component.schedule.domain.TaskTriggerInstanceDomain;
import cn.cnic.component.schedule.manager.TriggerConcurrencyManager;
import cn.cnic.component.schedule.utils.JsonConverter;
import cn.cnic.component.schedule.vo.ResponseObject;
import cn.cnic.component.schedule.vo.TaskExecutionInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.cnic.common.Eunm.SysRoleType;
import cn.cnic.component.flow.service.IFlowGroupService;
import cn.cnic.component.flow.service.IFlowService;
import cn.cnic.component.schedule.entity.MessageTriggerTaskDefinition;
import cn.cnic.component.schedule.entity.TaskTriggerInstance;
import cn.cnic.component.schedule.message.IMessage;
import cn.cnic.component.system.domain.SysUserDomain;
import cn.cnic.component.system.entity.SysRole;
import cn.cnic.component.system.entity.SysUser;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Semaphore;

/**
 * 工作流触发服务。
 * 负责接收消息驱动的任务定义和处理后的消息，实际调用工作流引擎触发任务。
 */
@Log4j2
@Service
public class WorkflowTriggerService {

    @Autowired
    private TaskTriggerInstanceDomain triggerInstanceDomain;
    @Autowired
    private final IFlowService flowServiceImpl;
    @Autowired
    private final IFlowGroupService groupServiceImpl;
    @Autowired
    private SysUserDomain sysUserDomain;
    @Autowired
    private TriggerConcurrencyManager concurrencyManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public WorkflowTriggerService(IFlowService flowServiceImpl, IFlowGroupService groupServiceImpl) {
        this.flowServiceImpl = flowServiceImpl;
        this.groupServiceImpl = groupServiceImpl;
    }

    /**
     * 根据消息触发工作流。
     * 该方法是消息驱动调度链的最终一步，负责实际触发工作流。
     * 使用非阻塞的并发控制，支持PENDING状态。
     *
     * @param definition 消息触发任务的定义
     * @param message    经过解析和过滤的标准化IMessage对象
     * @param ackAction
     * @throws RuntimeException 如果工作流触发失败
     */
    public void triggerByMessage(MessageTriggerTaskDefinition definition, IMessage message, Date creationTime, String triggerInstanceId, Runnable ackAction) {
        String messageId = message.getMessageId();
        TaskTriggerInstance instance = triggerInstanceDomain.findByMessageIdWithLock(messageId);
        if (instance == null) {
            instance = new TaskTriggerInstance(creationTime);
        }
        if (instance.getProcessId() == null) {
            StopWatch watch = new StopWatch();
            watch.start();

            Long definitionId = definition.getId();
            Integer concurrencyLimit = definition.getConcurrencyLimit();

            boolean acquired = false;
            try {
                // 步骤 1: 尝试非阻塞获取并发许可
                log.debug("[SEMAPHORE_TRY_ACQUIRE] 尝试获取许可... triggerInstanceId={}", triggerInstanceId);
                acquired = concurrencyManager.tryAcquire(definitionId, triggerInstanceId, concurrencyLimit);

                // 步骤 2: 先创建基础记录信息（无论是否获取到许可）
                instance.setType(definition.getType());
                instance.setTriggerInstanceId(triggerInstanceId); // 设置生成的唯一ID
                instance.setMessageSourceId(definitionId);
                instance.setTargetWorkflowId(definition.getTargetWorkflowId());
                instance.setTargetWorkflowName(definition.getTargetWorkflowName());
                instance.setTriggerTime(new Date(System.currentTimeMillis()));
                instance.setRawMessageContent(message.getRawMessage()); // 保存原始消息字符串
                instance.setMessageId(messageId); // 保存消息ID
                instance.setProtocolType(message.getProtocolType()); // 保存协议类型
                instance.setUpdateTime(new Date(System.currentTimeMillis()));

                if (acquired) {
                    // 步骤 3a: 成功获取许可，立即触发工作流
                    log.info("[SEMAPHORE_ACQUIRE_SUCCESS] 获取到许可，立即触发工作流。triggerInstanceId={}", triggerInstanceId);
                    
                    // 触发远程工作流
                  String sysUserId = definition.getCreatorId();
                  SysUser user = sysUserDomain.findUserById(sysUserId);
                  SysRole role = sysUserDomain.getSysRoleBySysUserId(sysUserId);
                  String userName = user.getName();
                  boolean isAdmin = role.getRole().equals(SysRoleType.ADMIN);
                  String processId = startWorkflow(definition.getTargetWorkflowId(), definition.getType(), userName, isAdmin);
                    ackAction.run();
                    log.info("[TRIGGER_COMPLETE] triggerInstanceId={}, 完成触发流水线。sourceId={}",
                            definition.getTargetWorkflowId(), definitionId);
                    
                    if (processId != null) {
                        // 触发成功，更新状态为 RUNNING
                        instance.setStatus("RUNNING");
                        instance.setProcessId(processId);
                        log.info("[WORKFLOW_TRIGGER_SUCCESS] 工作流触发成功，任务进入 RUNNING 状态。processId={}", processId);
                    } else {
                        // 触发失败（业务逻辑认为失败，例如未返回ID）
                        instance.setStatus("FAILED");
                        instance.setErrorMessage("工作流引擎未返回有效的Job ID");
                        log.error("[WORKFLOW_TRIGGER_FAIL] 工作流触发失败：未返回Job ID。");
                        // 释放许可
                        int runningCount = triggerInstanceDomain.countByMessageSourceIdAndStatus(definitionId, "RUNNING");
                        concurrencyManager.release(definitionId, concurrencyLimit, runningCount);
                    }
                } else {
                    // 步骤 3b: 无法获取许可，设置为PENDING状态
                    instance.setStatus("PENDING");
                    log.info("[SEMAPHORE_ACQUIRE_FAIL] 无法获取许可，任务进入 PENDING 状态。triggerInstanceId={}", triggerInstanceId);
                    ackAction.run(); // 仍然确认消息，避免重复处理
                }

                watch.stop();
                long watchTime = watch.getTime();
                instance.setDurationMillis(watchTime);

                ArrayList<TaskExecutionInfo> taskExecutionList = new ArrayList<>();
                TaskExecutionInfo taskExecutionInfo = new TaskExecutionInfo();
                taskExecutionInfo.setCreateTime(creationTime);
                taskExecutionInfo.setDurationMillis(watchTime);
                taskExecutionInfo.setStatus(instance.getStatus());
                taskExecutionList.add(taskExecutionInfo);

                String executionListJson = JsonConverter.listToJson(taskExecutionList);
                instance.setExecutionList(executionListJson);

                triggerInstanceDomain.insert(instance);
                
            } catch (Exception e) {
                log.error("[WORKFLOW_TRIGGER_ERROR] 触发工作流时发生异常。triggerInstanceId={}", triggerInstanceId, e);
                // 设置失败状态
                instance.setStatus("FAILED");
                instance.setErrorMessage("触发工作流时发生异常: " + e.getMessage());
                instance.setUpdateTime(new Date(System.currentTimeMillis()));
                
                // 只有在成功获取许可的情况下才释放许可
                if (acquired) {
                    try {
                        int runningCount = triggerInstanceDomain.countByMessageSourceIdAndStatus(definitionId, "RUNNING");
                        concurrencyManager.release(definitionId, concurrencyLimit, runningCount);
                        log.info("[SEMAPHORE_RELEASE] 异常情况下释放许可。triggerInstanceId={}", triggerInstanceId);
                    } catch (Exception releaseException) {
                        log.warn("[SEMAPHORE_RELEASE_ERROR] 释放许可时发生异常。triggerInstanceId={}", triggerInstanceId, releaseException);
                    }
                } else {
                    log.debug("[SEMAPHORE_RELEASE_SKIP] 未获取许可，无需释放。triggerInstanceId={}", triggerInstanceId);
                }
                
                // 仍然保存失败的记录
                try {
                    triggerInstanceDomain.insert(instance);
                } catch (Exception insertException) {
                    log.error("[INSTANCE_INSERT_ERROR] 保存失败记录时发生异常。triggerInstanceId={}", triggerInstanceId, insertException);
                }
            }
        }
    }

    public String startWorkflow(String targetWorkflowId, String type, String userName,Boolean isAdmin) throws Exception {
        ResponseObject responseObject = null;
        if ("FLOW".equals(type)) {
            String response = flowServiceImpl.runFlow(userName, isAdmin, targetWorkflowId, "RUN");
             responseObject = objectMapper.readValue(response, ResponseObject.class);

        }else if ("FLOW_GROUP".equals(type)) {
            String response = groupServiceImpl.runFlowGroup(isAdmin, userName, targetWorkflowId, "RUN");
             responseObject = objectMapper.readValue(response, ResponseObject.class);
        }

        if (responseObject == null || (200 != responseObject.getCode()) || !"Succeeded".equals(responseObject.getErrorMsg())) {
            throw new Exception("触发对应workflow失败:");
        }

        return responseObject.getProcessId();
    }
}
