package cn.cnic.component.schedule.service.Impl;

import cn.cnic.base.utils.JsonUtils;
import cn.cnic.component.flow.service.IFlowGroupService;
import cn.cnic.component.flow.service.IFlowService;
import cn.cnic.component.schedule.entity.TaskTriggerInstance;
import cn.cnic.component.schedule.domain.TaskTriggerInstanceDomain;
import cn.cnic.component.schedule.utils.JsonConverter;
import cn.cnic.component.schedule.vo.ResponseObject;
import cn.cnic.component.schedule.vo.TaskExecutionInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.cnic.component.schedule.exception.ResourceNotFoundException;
import cn.cnic.component.schedule.vo.TaskTriggerInstanceVo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import java.util.List;

import static cn.cnic.base.config.Log4j2PathResolver.getLogFileBasePath;

/**
 * 任务触发实例业务服务
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class TaskTriggerInstanceService {

    @Autowired
    private final IFlowService flowServiceImpl;

    @Autowired
    private final IFlowGroupService groupServiceImpl;

    @Autowired
    private TaskTriggerInstanceDomain triggerInstanceDomain;

    private final TaskTriggerInstanceDomain domain;

    /**
     * 分页查询任务实例 (返回MyBatis-Plus Page对象)
     * 注意：这里的Page对象是手动构建的，因为Domain层返回的是List和Total Count。
     * 如果Domain层直接使用MyBatis-Plus的Page插件方法，这里会更简洁。
     */
    public Page<TaskTriggerInstance> pageByMessageSourceId(Long messageSourceId, String status, int page, int size) {
        int offset = (page - 1) * size;
        List<TaskTriggerInstance> list;
        int total;
        if (status != null && !status.isEmpty()) {
            total = domain.countByMessageSourceIdAndStatus(messageSourceId, status);
            list = domain.selectByMessageSourceIdAndStatus(messageSourceId, status, size, offset);

        } else {
            total = domain.countByMessageSourceId(messageSourceId);
            list = domain.selectByMessageSourceId(messageSourceId, size, offset);

        }

        // 手动构建 MyBatis-Plus 的 Page 对象
        Page<TaskTriggerInstance> resultPage = new Page<>(page + 1, size); // MyBatis-Plus Page的current是1-based
        resultPage.setRecords(list);
        resultPage.setTotal(total);
        return resultPage;
    }

    /**
     * 获取单个任务实例
     */
    public TaskTriggerInstance getById(Long id) {
        TaskTriggerInstance instance = domain.getById(id);
        if (instance == null) {
            throw new ResourceNotFoundException("任务实例不存在: " + id);
        }
        return instance;
    }

    /**
     * 分页查询任务实例VO (返回MyBatis-Plus Page<VO>对象)
     */
    public Page<TaskTriggerInstanceVo> pageByMessageSourceIdVo(Long messageSourceId, String status, int page, int size) {
        // 先获取实体Page
        Page<TaskTriggerInstance> entityPage = pageByMessageSourceId(messageSourceId, status, page, size);

        // 将实体列表转换为VO列表
        List<TaskTriggerInstanceVo> voList = entityPage.getRecords().stream()
                .map(this::toVo)
                .collect(Collectors.toList());

        // 创建一个新的 MyBatis-Plus Page<VO> 对象，并复制分页信息
        Page<TaskTriggerInstanceVo> voPage = new Page<>(entityPage.getCurrent(), entityPage.getSize());
        voPage.setRecords(voList);
        voPage.setTotal(entityPage.getTotal());
        voPage.setPages(entityPage.getPages());
        voPage.setSearchCount(entityPage.isSearchCount());

        return voPage;
    }

    /**
     * 获取单个任务实例日志
     */
    public List<String> getLogById(Long id) {
        TaskTriggerInstance taskTriggerInstance = getById(id);
//        String triggerInstanceId = taskTriggerInstance.getTriggerInstanceId();
        Path basePath = getLogFileBasePath("ScheduleTaskInstanceFiles");
        Date createTime = taskTriggerInstance.getCreateTime();
        List<String> matchingLines = new ArrayList<>();
        //如果createTime是当天，则basePath加上/schedule.log, 否则加上/yyyy-MM-dd/schedule.log
//        LocalDate today = LocalDate.now();
        LocalDate createLocalDate = createTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String executionListJson = taskTriggerInstance.getExecutionList();
        List<TaskExecutionInfo> taskExecutionList = JsonConverter.jsonToList(executionListJson, TaskExecutionInfo.class);

        Set<LocalDate> logDates = new TreeSet<>();
        for (TaskExecutionInfo info : taskExecutionList) {
            if (info.getCreateTime() != null) {
                logDates.add(info.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
        }
        List<String> idsToSearch = new ArrayList<>();
        idsToSearch.add(taskTriggerInstance.getTriggerInstanceId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate today = LocalDate.now();

        for (LocalDate logDate : logDates) {
            Path logFilePath;
            if (logDate.isEqual(today)) {
                logFilePath = basePath.resolve("schedule.log");
            } else {
                String dateDirName = dateFormat.format(Date.from(logDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                logFilePath = basePath.resolve(dateDirName).resolve("schedule.log");
            }

            try {
                if (Files.exists(logFilePath) && Files.isRegularFile(logFilePath)) {
                    List<String> allLines = Files.readAllLines(logFilePath);

                    for (String line : allLines) {
                        for (String idToSearch : idsToSearch) {
                            if (line.contains("triggerInstanceId=" + idToSearch)) {
                                matchingLines.add(line);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error reading log file: " + logFilePath.toAbsolutePath() + " - " + e.getMessage());
                matchingLines.add("Error reading log file: " + e.getMessage());
            }
        }
//        for (TaskExecutionInfo taskExecutionInfo : taskExecutionList) {
//            Path logFilePath;
//            if (createLocalDate.isEqual(today)) {
//                logFilePath = basePath.resolve("schedule.log");
//            } else {
//                String dateDirName = dateFormat.format(createTime);
//                logFilePath = basePath.resolve(dateDirName).resolve("schedule.log");
//            }
//            Pattern pattern = Pattern.compile(".*triggerInstanceId=" + Pattern.quote(triggerInstanceId) + ".*");
//            try {
//                if (Files.exists(logFilePath) && Files.isRegularFile(logFilePath)) {
//                    List<String> allLines = Files.readAllLines(logFilePath);
//                    for (String line : allLines) {
//                        Matcher matcher = pattern.matcher(line);
//                        if (matcher.matches()) {
//                            matchingLines.add(line);
//                        }
//                    }
//                } else {
//                    System.out.println("Log file does not exist or is not a regular file: " + logFilePath.toAbsolutePath());
//                }
//            } catch (IOException e) {
//                System.err.println("Error reading log file: " + logFilePath.toAbsolutePath() + " - " + e.getMessage());
//                matchingLines.add("Error reading log file: " + e.getMessage());
//            }
//        }


        return matchingLines;
    }

    /**
     * 将实体转换为VO
     */
    private TaskTriggerInstanceVo toVo(TaskTriggerInstance entity) {
        if (entity == null) return null;
        TaskTriggerInstanceVo vo = new TaskTriggerInstanceVo();
        vo.setId(entity.getId());
        vo.setMessageSourceId(entity.getMessageSourceId());
        vo.setProcessId(entity.getProcessId());
        vo.setTriggerTime(entity.getTriggerTime());
        vo.setStatus(entity.getStatus());
        vo.setDurationMillis(entity.getDurationMillis());
        vo.setErrorMessage(entity.getErrorMessage());
        vo.setRawMessageContent(entity.getRawMessageContent());
        vo.setExecutionList(entity.getExecutionList());
        return vo;
    }

    public boolean retryInstance(Long id, String userName, boolean isAdmin) throws Exception {
        TaskTriggerInstance taskTriggerInstance = getById(id);
        String executionListJson = taskTriggerInstance.getExecutionList();
        List<TaskExecutionInfo> taskExecutionList = JsonConverter.jsonToList(executionListJson, TaskExecutionInfo.class);
        MDC.put("triggerInstanceId", taskTriggerInstance.getTriggerInstanceId());
        MDC.put("sourceId", String.valueOf(taskTriggerInstance.getMessageSourceId()));
        MDC.put("workflowId", taskTriggerInstance.getTargetWorkflowId());
        log.info("[RETRY_INSTANCE] 进行第{}次重试，triggerInstanceId={}, sourceId={}", taskExecutionList.size(), taskTriggerInstance.getTriggerInstanceId(), taskTriggerInstance.getMessageSourceId());
        String type = taskTriggerInstance.getType();
        String targetWorkflowId = taskTriggerInstance.getTargetWorkflowId();
        Date createTime = new Date(System.currentTimeMillis());
        taskTriggerInstance.setCreateTime(createTime);
        ResponseObject responseObject = null;
        if ("FLOW".equals(type)) {
            String response = flowServiceImpl.runFlow(userName, isAdmin, targetWorkflowId, "RUN");
            responseObject = JsonUtils.toObject(response, ResponseObject.class);

        }else if ("FLOW_GROUP".equals(type)) {
            String response = groupServiceImpl.runFlowGroup(isAdmin, userName, targetWorkflowId, "RUN");
            responseObject = JsonUtils.toObject(response, ResponseObject.class);
        }

        if (responseObject == null || (200 != responseObject.getCode()) || !"Succeeded".equals(responseObject.getErrorMsg())) {
            throw new Exception("触发对应workflow失败:");
        }
        String processId = responseObject.getProcessId();
        String status = "FAILED";
        String errorMessage = "";
        if (processId != null) {
            status = "RUNNING";
            log.info("[WORKFLOW_TRIGGER_SUCCESS] 工作流触发成功。triggerInstanceId={}, sourceId={}, workflowId={},  processId={}",
                    taskTriggerInstance.getTriggerInstanceId(), taskTriggerInstance.getId(), taskTriggerInstance.getTargetWorkflowId(), processId);
        } else {
            errorMessage = "工作流引擎未返回有效的Job ID";
            log.error("[WORKFLOW_TRIGGER_FAIL] 工作流触发失败：未返回Job ID。triggerInstanceId={}, sourceId={}, workflowId={}",
                    taskTriggerInstance.getTriggerInstanceId(), taskTriggerInstance.getId(), taskTriggerInstance.getTargetWorkflowId());
        }
        taskTriggerInstance.setProcessId(processId);
        Date updateTime = new Date(System.currentTimeMillis());
        long durationMillis = updateTime.getTime() - createTime.getTime();
        taskTriggerInstance.setUpdateTime(updateTime);
        taskTriggerInstance.setDurationMillis(durationMillis);

        TaskExecutionInfo taskExecutionInfo = new TaskExecutionInfo();
        taskExecutionInfo.setCreateTime(createTime);
        taskExecutionInfo.setProcessId(processId);
        taskExecutionInfo.setDurationMillis(durationMillis);
        taskTriggerInstance.setStatus(status);
        taskTriggerInstance.setErrorMessage(errorMessage);
        taskExecutionInfo.setStatus(status);
        taskExecutionList.add(taskExecutionInfo);
        taskTriggerInstance.setExecutionList(JsonConverter.listToJson(taskExecutionList));

        triggerInstanceDomain.update(taskTriggerInstance);
        MDC.remove("triggerInstanceId");
        MDC.remove("sourceId");
        MDC.remove("workflowId");
        return true;
    }
}