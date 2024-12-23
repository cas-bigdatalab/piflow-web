package cn.cnic.component.schedule.service.Impl;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import cn.cnic.base.utils.*;
import cn.cnic.common.Eunm.*;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.flow.service.IFlowService;
import cn.cnic.component.process.domain.ProcessDomain;
import cn.cnic.component.schedule.domain.FileScheduleDomain;
import cn.cnic.component.schedule.entity.FileSchedule;
import cn.cnic.component.schedule.entity.FileScheduleOrigin;
import cn.cnic.component.schedule.vo.FileScheduleVo;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.cnic.component.flow.domain.FlowDomain;
import cn.cnic.component.flow.domain.FlowGroupDomain;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.process.domain.ProcessDomain;
import cn.cnic.component.process.domain.ProcessGroupDomain;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.utils.ProcessGroupUtils;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.schedule.domain.ScheduleDomain;
import cn.cnic.component.schedule.entity.Schedule;
import cn.cnic.component.schedule.service.IScheduleService;
import cn.cnic.component.schedule.vo.ScheduleVo;
import cn.cnic.third.service.ISchedule;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ScheduleServiceImpl implements IScheduleService {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    private final ProcessGroupDomain processGroupDomain;
    private final FlowGroupDomain flowGroupDomain;
    private final ScheduleDomain scheduleDomain;
    private final ProcessDomain processDomain;
    private final FlowDomain flowDomain;
    private final ISchedule scheduleImpl;
    private final FileScheduleDomain fileScheduleDomain;

    private final IFlowService flowServiceImpl;

    @Autowired
    public ScheduleServiceImpl(ProcessGroupDomain processGroupDomain,
                               FlowGroupDomain flowGroupDomain,
                               ScheduleDomain scheduleDomain,
                               ProcessDomain processDomain,
                               FlowDomain flowDomain,
                               ISchedule scheduleImpl,
                               FileScheduleDomain fileScheduleDomain,
                               IFlowService flowServiceImpl) {
        this.processGroupDomain = processGroupDomain;
        this.flowGroupDomain = flowGroupDomain;
        this.scheduleDomain = scheduleDomain;
        this.processDomain = processDomain;
        this.flowDomain = flowDomain;
        this.scheduleImpl = scheduleImpl;
        this.fileScheduleDomain = fileScheduleDomain;
        this.flowServiceImpl = flowServiceImpl;
    }

    @Override
    public String getScheduleVoListPage(boolean isAdmin, String username, Integer offset, Integer limit, String param) {
        //Determine paging conditions
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        // Load paging plug-in
        Page<ScheduleVo> page = PageHelper.startPage(offset, limit, "crt_dttm desc");
        // search
        scheduleDomain.getScheduleVoList(isAdmin, username, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
    }

    @Override
    public String addSchedule(String username, ScheduleVo scheduleVo) {
        // Judge whether the 'username' is empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        // Judge whether the 'scheduleVo' is empty
        if (null == scheduleVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        Schedule schedule = new Schedule();
        // Copy scheduleVo to schedule
        BeanUtils.copyProperties(scheduleVo, schedule);

        // basic properties (required when creating)
        schedule.setCrtDttm(new Date());
        schedule.setCrtUser(username);
        // basic properties
        schedule.setEnableFlag(true);
        schedule.setLastUpdateUser(username);
        schedule.setLastUpdateDttm(new Date());
        schedule.setVersion(0L);

        //set uuid
        schedule.setId(UUIDUtils.getUUID32());

        // save
        int insert = scheduleDomain.insert(schedule);

        if (insert <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("add failed");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    /**
     * get ScheduleVo by id
     *
     * @param isAdmin  is admin
     * @param username username
     * @param id       schedule id
     * @return json
     */
    @Override
    public String getScheduleVoById(boolean isAdmin, String username, String id) {
        // Judge whether the 'username' is empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        // Judge whether the 'id' is empty
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        }
        // search
        ScheduleVo scheduleVoById = scheduleDomain.getScheduleVoById(isAdmin, username, id);
        // Judge whether the query result is empty
        if (null == scheduleVoById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("scheduleVo", scheduleVoById);
    }

    @Override
    public String updateSchedule(boolean isAdmin, String username, ScheduleVo scheduleVo) {
        // Judge whether the 'username' is empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        // Judge whether the 'scheduleVo' is empty
        if (null == scheduleVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        // Judge whether the 'scheduleVo Id' is empty
        if (StringUtils.isBlank(scheduleVo.getId())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        }
        // query
        Schedule scheduleById = scheduleDomain.getScheduleById(isAdmin, username, scheduleVo.getId());
        // Judge whether the query result is empty
        if (null == scheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No data with ID " + scheduleVo.getId());
        }
        ScheduleState status = scheduleById.getStatus();
        if (ScheduleState.RUNNING == status || ScheduleState.SUSPEND == status) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.SCHEDULED_TASK_ERROR_MSG());
        }
        // Copy scheduleVo data to scheduleById
        BeanUtils.copyProperties(scheduleVo, scheduleById);
        //set Operator information
        scheduleById.setLastUpdateDttm(new Date());
        scheduleById.setLastUpdateUser(username);
        int update = scheduleDomain.update(scheduleById);
        if (update <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPDATE_ERROR_MSG());
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    @Override
    public String delSchedule(boolean isAdmin, String username, String id) {
        // Judge whether the 'username' is empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        // Judge whether the 'id' is empty
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        }
        // query
        Schedule scheduleById = scheduleDomain.getScheduleById(isAdmin, username, id);
        // Judge whether the query result is empty
        if (null == scheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data does not exist");
        }
        ScheduleState status = scheduleById.getStatus();
        if (ScheduleState.RUNNING == status || ScheduleState.SUSPEND == status) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.SCHEDULED_TASK_ERROR_MSG());
        }
        // delete
        int delSchedule = scheduleDomain.delScheduleById(isAdmin, username, id);
        // Judge whether it is successful or not
        if (delSchedule <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("del failed");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    @Override
    public String startSchedule(boolean isAdmin, String username, String id) {
        // Judge whether the 'id' is empty
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        }
        // Judge whether the 'username' is empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        // query
        Schedule scheduleById = scheduleDomain.getScheduleById(isAdmin, username, id);
        // Judge whether the query result is empty
        if (null == scheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data does not exist");
        }
        String scheduleRunTemplateId = scheduleById.getScheduleRunTemplateId();
        // Judge whether the 'scheduleRunTemplateId' is empty
        if (StringUtils.isBlank(scheduleRunTemplateId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, Flow or Group is null");
        }
        String type = scheduleById.getType();
        // Judge whether the 'type' is empty
        if (StringUtils.isBlank(type)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, type is null");
        }
        String scheduleProcessTemplateId;
        Process process = null;
        ProcessGroup processGroup = null;
        try {
            // Distinguish types
            if ("FLOW".equals(type)) {
                // query
                Flow flowById = flowDomain.getFlowById(scheduleRunTemplateId);
                // Judge whether the query result is empty
                if (null == flowById) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, flow data is null");
                }
                // flow convert process
                process = ProcessUtils.flowToProcess(flowById, username, true);
                if (null == process) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, process convert failed");
                }
                //补充定时触发信息
                process.setTriggerMode(ProcessTriggerMode.TIMING.getValue());
                process.setScheduleId(scheduleById.getId());
                process.setScheduleName(flowById.getName());

                int addProcess_i = processDomain.addProcess(process);
                if (addProcess_i <= 0) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, process convert failed");
                }
                scheduleProcessTemplateId = process.getId();
            } else if ("FLOW_GROUP".equals(type)) {
                // query
                FlowGroup flowGroupById = flowGroupDomain.getFlowGroupById(scheduleRunTemplateId);
                // Judge whether the query result is empty
                if (null == flowGroupById) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, Flow data is null");
                }
                // flowGroup convert processGroup
                processGroup = ProcessGroupUtils.flowGroupToProcessGroup(flowGroupById, username, RunModeType.RUN, true);
                if (null == processGroup) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, process convert failed");
                }
                int addProcessGroup_i = processGroupDomain.addProcessGroup(processGroup);
                if (addProcessGroup_i <= 0) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, processGroup convert failed");
                }
                scheduleProcessTemplateId = processGroup.getId();
            } else {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("type error or process is null");
            }
        } catch (Exception e) {
            logger.error("error", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        // call start schedule
        Map<String, Object> thirdScheduleMap = scheduleImpl.scheduleStart(scheduleById, process, processGroup);
        // Judge whether it is successful or not
        if (200 != (int) thirdScheduleMap.get("code")) {
            return ReturnMapUtils.toJson(thirdScheduleMap);
        }

        // update
        scheduleById.setStatus(ScheduleState.RUNNING);
        scheduleById.setLastUpdateDttm(new Date());
        scheduleById.setLastUpdateUser(username);
        scheduleById.setScheduleId((String) thirdScheduleMap.get("scheduleId"));
        scheduleById.setScheduleProcessTemplateId(scheduleProcessTemplateId);
        // save
        int update = scheduleDomain.update(scheduleById);
        if (update <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : " + MessageConfig.INTERFACE_CALL_SUCCEEDED_SAVE_ERROR_MSG());
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    @Override
    public String stopSchedule(boolean isAdmin, String username, String id) {
        // Judge whether the 'username' is empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : illegal user");
        }
        // Judge whether the 'id' is empty
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : id is null");
        }
        // query
        Schedule scheduleById = scheduleDomain.getScheduleById(isAdmin, username, id);
        // Judge whether the query result is empty
        if (null == scheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : Data does not exist");
        }
        // Judge whether the 'scheduleId' is empty
        if (StringUtils.isBlank(scheduleById.getScheduleId())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : scheduleId is null");
        }
        // call stop schedule
        String scheduleStopMsg = scheduleImpl.scheduleStop(scheduleById.getScheduleId());
        // Judge whether it is successful or not
        if (StringUtils.isBlank(scheduleStopMsg) || scheduleStopMsg.contains("Exception") || scheduleStopMsg.contains("error")) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : " + MessageConfig.INTERFACE_CALL_ERROR_MSG());
        }
        // Judge whether it is successful or not
        if (StringUtils.isBlank(scheduleStopMsg) || scheduleStopMsg.contains("failed")) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : " + MessageConfig.INTERFACE_CALL_ERROR_MSG());
        }
        // update
        scheduleById.setStatus(ScheduleState.STOP);
        scheduleById.setLastUpdateDttm(new Date());
        scheduleById.setLastUpdateUser(username);
        // save
        int update = scheduleDomain.update(scheduleById);
        if (update <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : " + MessageConfig.INTERFACE_CALL_SUCCEEDED_SAVE_ERROR_MSG());
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    @Override
    public String getFileScheduleListByPage(FileScheduleVo fileScheduleVo) {
        String username = SessionUserUtil.getCurrentUsername();
        // Load paging plug-in
        Page<FileScheduleVo> page = PageHelper.startPage(fileScheduleVo.getPage(), fileScheduleVo.getLimit(), "crt_dttm desc");
        // search
        fileScheduleDomain.getFileScheduleListByPage(fileScheduleVo.getKeyword(), username);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        rtnMap = PageHelperUtils.setLayTableParam(page, rtnMap);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String saveFileSchedule(FileScheduleVo fileScheduleVo) {
        String idStr = fileScheduleVo.getId();
        Date now = new Date();
        String username = SessionUserUtil.getCurrentUsername();
        if (StringUtils.isBlank(idStr)) {
            //新增
            FileSchedule fileSchedule = new FileSchedule();
            BeanUtils.copyProperties(fileScheduleVo, fileSchedule);
            fileSchedule.setAssociateType(FileScheduleAssociateType.FLOW.getValue());

            if (StringUtils.isBlank(fileSchedule.getName()) || StringUtils.isBlank(fileSchedule.getDescription())) {
                Flow flowById = flowDomain.getFlowById(fileSchedule.getAssociateId());
                // Judge whether the query result is empty
                if (null == flowById) {
                    return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, flow data is null");
                }
                if (StringUtils.isBlank(fileSchedule.getName())) fileSchedule.setName(flowById.getName());
                if (StringUtils.isBlank(fileSchedule.getDescription()))
                    fileSchedule.setDescription(flowById.getDescription());
            }
            // basic properties (required when creating)
            fileSchedule.setCrtDttm(now);
            fileSchedule.setCrtUser(username);
            // basic properties
            fileSchedule.setEnableFlag(true);
            fileSchedule.setLastUpdateUser(username);
            fileSchedule.setLastUpdateDttm(now);
            fileSchedule.setVersion(0L);
            fileSchedule.setState(FileScheduleState.INIT.getValue());

            // save
            int insert = fileScheduleDomain.insert(fileSchedule);

            if (insert <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("add failed");
            }
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());

        } else {
            //编辑
            // query
            FileSchedule fileSchedule = fileScheduleDomain.getById(Long.parseLong(idStr));
            // Judge whether the query result is empty
            if (null == fileSchedule) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("No data with ID " + idStr);
            }
            Integer state = fileSchedule.getState();
            if (FileScheduleState.RUNNING.getValue().equals(state)) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("正在运行中的任务无法编辑，请先停止后再操作！！");
            }
            // Copy scheduleVo data to scheduleById
            BeanUtils.copyProperties(fileScheduleVo, fileSchedule);
            //set Operator information
            fileSchedule.setLastUpdateDttm(now);
            fileSchedule.setLastUpdateUser(username);
            int update = fileScheduleDomain.update(fileSchedule);
            if (update <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("update failed");
            }
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());

        }
    }

    @Override
    public String getFileScheduleById(String id) {
        // search
        FileSchedule fileSchedule = fileScheduleDomain.getById(Long.parseLong(id));
        // Judge whether the query result is empty
        if (null == fileSchedule) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null");
        }
        FileScheduleVo fileScheduleVo = new FileScheduleVo();
        BeanUtils.copyProperties(fileSchedule, fileScheduleVo);
        fileScheduleVo.setId(String.valueOf(fileSchedule.getId()));
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", fileScheduleVo);
    }

    @Override
    public String delFileSchedule(String id) {
        // Judge whether the 'id' is empty
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is null");
        }
        // query
        FileSchedule fileSchedule = fileScheduleDomain.getById(Long.parseLong(id));
        // Judge whether the query result is empty
        if (null == fileSchedule) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data does not exist");
        }
        Integer state = fileSchedule.getState();
        if (FileScheduleState.RUNNING.getValue().equals(state)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("正在运行中的任务无法删除，请先停止后再操作！！");
        }
        // delete
        int delSchedule = fileScheduleDomain.deleteById(Long.parseLong(id));
        // Judge whether it is successful or not
        if (delSchedule <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("del failed");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public String startFileSchedule(String id) {

        String username = SessionUserUtil.getCurrentUsername();
        // query
        FileSchedule scheduleById = fileScheduleDomain.getById(Long.parseLong(id));
        // Judge whether the query result is empty
        if (null == scheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data does not exist");
        }
        String scheduleRunTemplateId = scheduleById.getAssociateId();
        // Judge whether the 'scheduleRunTemplateId' is empty
        if (StringUtils.isBlank(scheduleRunTemplateId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, Flow is null");
        }

        // query
        Flow flowById = flowDomain.getFlowById(scheduleRunTemplateId);
        // Judge whether the query result is empty
        if (null == flowById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("failed, flow data is null");
        }
        //记录路径原始文件信息
        Map<String, Long> allFilePathMap = FileUtils.getAllFilePathInDict(scheduleById.getFileDict(), scheduleById.getFilePrefix(), scheduleById.getFileSuffix());
        Set<String> allFilePath = allFilePathMap.keySet();
        FileScheduleOrigin originByScheduleId = fileScheduleDomain.getOriginByScheduleId(scheduleById.getId());
        if (ObjectUtils.isEmpty(originByScheduleId)) {
            FileScheduleOrigin fileScheduleOrigin = new FileScheduleOrigin();
            fileScheduleOrigin.setScheduleId(scheduleById.getId());
            fileScheduleOrigin.setOriginFileRecord(String.join(",", allFilePath));
            fileScheduleDomain.insertOrigin(fileScheduleOrigin);
        } else {
            originByScheduleId.setOriginFileRecord(String.join(",", allFilePath));
            fileScheduleDomain.updateOrigin(originByScheduleId);
        }
        // update
        scheduleById.setState(FileScheduleState.RUNNING.getValue());
        scheduleById.setLastUpdateDttm(new Date());
        scheduleById.setLastUpdateUser(username);
        // save
        int update = fileScheduleDomain.update(scheduleById);
        if (update <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : Interface call succeeded, save error");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    @Override
    public String stopFileSchedule(String id) {

        String username = SessionUserUtil.getCurrentUsername();
        // Judge whether the 'id' is empty
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : id is null");
        }
        // query
        FileSchedule scheduleById = fileScheduleDomain.getById(Long.parseLong(id));
        // Judge whether the query result is empty
        if (null == scheduleById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : Data does not exist");
        }
        // update
        scheduleById.setState(FileScheduleState.STOP.getValue());
        scheduleById.setLastUpdateDttm(new Date());
        scheduleById.setLastUpdateUser(username);
        // save
        int update = fileScheduleDomain.update(scheduleById);
        if (update <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error : Interface call succeeded, save error");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    /**
     * @param :
     * @return void
     * @author tianyao
     * @description 定期扫描所有RUNNING状态的file schedule,更新 file_path字段
     * 每10分钟扫描一次，既要看是否有新增文件，也要看是否有待处理的文件，如果有，查看最近进程是否完成，完成后触发下个文件运行
     * 并行如果有多个文件，同时触发多个进程；串行如果有多个文件，需要一个一个触发
     * @date 2024/5/15 10:12
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void run() {
        List<FileSchedule> allRunning = fileScheduleDomain.getAllRunning();
        for (FileSchedule fileSchedule : allRunning) {
            Integer triggerMode = fileSchedule.getTriggerMode();
            Map<String, Long> allFilePathMap = FileUtils.getAllFilePathInDict(fileSchedule.getFileDict(), fileSchedule.getFilePrefix(), fileSchedule.getFileSuffix());
            Set<String> allFilePath = allFilePathMap.keySet();
            FileScheduleOrigin origin = fileScheduleDomain.getOriginByScheduleId(fileSchedule.getId());
            List<String> originFileRecordList = new ArrayList<>();
            String originFileRecord = origin.getOriginFileRecord();
            if (StringUtils.isNotBlank(originFileRecord)) {
                originFileRecordList.addAll(Arrays.asList(originFileRecord.split(",")));
            }

            List<String> difference;
            difference = allFilePath.stream()
                    .filter(a -> !originFileRecordList.contains(a))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(difference)) {
                logger.warn("diffrence:{}", JSON.toJSONString(difference));
                //运行流水线,更新文件触发相关表
                try {
                    if (FileScheduleTriggerMode.PARALLEL.getValue().equals(triggerMode)) {
                        List<String> processIdList = new ArrayList<>();
                        //并行，所有的待处理文件同时触发流水线
                        for (String filePath : difference) {
                            String processId = flowServiceImpl.runFlowByFileSchedule(fileSchedule, filePath);
                            processIdList.add(processId);
                        }

                        fileSchedule.setFilePath(String.join(",", difference));
                        fileSchedule.setLastUpdateDttm(new Date());
                        fileSchedule.setProcessId(String.join(",", processIdList));
                        origin.setOriginFileRecord(String.join(",", allFilePath));
                        updateFileScheduleAndOrigin(fileSchedule, origin);
                    } else if (FileScheduleTriggerMode.SERIAL.getValue().equals(triggerMode)) {
                        List<String> pendingFileRecordList = new ArrayList<>();
                        String oldPendingFileRecord = origin.getPendingFileRecord();
                        if (StringUtils.isNotBlank(oldPendingFileRecord)) {
                            pendingFileRecordList.addAll(Arrays.asList(oldPendingFileRecord.split(",")));
                        }

                        //根据排序规则，给difference排序,排序之后加入pendingFileRecord
                        Integer serialRule = fileSchedule.getSerialRule();
                        Integer serialOrder = fileSchedule.getSerialOrder();
                        if (FileScheduleSerialRule.LAST_MODIFY_TIME.getValue().equals(serialRule)) {
                            //抽取map中key为difference的所有entry，根据升序降序排列difference
                            Map<String, Long> differenceMap = new HashMap<>();
                            for (String s : difference) {
                                differenceMap.put(s, allFilePathMap.get(s));
                            }
                            if (FileScheduleSerialOrder.ASC.getValue().equals(serialOrder)) {
                                difference = differenceMap.entrySet().stream()
                                        .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                                        .map(Map.Entry::getKey)
                                        .collect(Collectors.toList());
                            } else {
                                //降序排序
                                difference = differenceMap.entrySet().stream()
                                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                                        .map(Map.Entry::getKey)
                                        .collect(Collectors.toList());
                            }
                        } else {
                            //根据文件名的匹配规则以及排序顺序排列difference
                            String regex = fileSchedule.getRegex();
                            if (FileScheduleSerialOrder.ASC.getValue().equals(serialOrder)) {
                                if (StringUtils.isBlank(regex)) {
                                    difference = difference.stream()
                                            // 对匹配到的结果进行升序排序
                                            .sorted(Comparator.naturalOrder())
                                            // 收集到List中
                                            .collect(Collectors.toList());
                                } else {
                                    Pattern pattern = Pattern.compile(regex);
                                    Map<String, String> differenceMap = new HashMap<>();
                                    for (String s : difference) {
                                        String fileName = s.substring(s.lastIndexOf("/") + 1);
                                        Matcher matcher = pattern.matcher(fileName);
                                        StringBuilder sb = new StringBuilder();
                                        while (matcher.find()) {
                                            sb.append(matcher.group());
                                            // 如果找到了，则添加到结果map中；否则，value为空字符串
                                            differenceMap.put(s, sb.toString());
                                        }
                                    }
                                    if (!differenceMap.isEmpty()) {
                                        difference = differenceMap.entrySet().stream()
                                                .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                                                .map(Map.Entry::getKey)
                                                .collect(Collectors.toList());
                                    } else {
                                        difference.clear();
                                    }
                                }
                            } else {
                                //降序排序
                                if (StringUtils.isBlank(regex)) {
                                    difference = difference.stream()
                                            // 对匹配到的结果进行降序排序
                                            .sorted(Comparator.reverseOrder())
                                            // 收集到List中
                                            .collect(Collectors.toList());
                                } else {
                                    Pattern pattern = Pattern.compile(regex);
                                    Map<String, String> differenceMap = new HashMap<>();
                                    for (String s : difference) {
                                        String fileName = s.substring(s.lastIndexOf("/") + 1);
                                        Matcher matcher = pattern.matcher(fileName);
                                        StringBuilder sb = new StringBuilder();
                                        while (matcher.find()) {
                                            sb.append(matcher.group());
                                            // 如果找到了，则添加到结果map中；否则，value为空字符串
                                            differenceMap.put(s, sb.toString());
                                        }
                                    }
                                    if (!differenceMap.isEmpty()) {
                                        //降序排序
                                        difference = differenceMap.entrySet().stream()
                                                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                                                .map(Map.Entry::getKey)
                                                .collect(Collectors.toList());
                                    } else {
                                        difference.clear();
                                    }
                                }
                            }
                        }
                        if (CollectionUtils.isNotEmpty(difference)) pendingFileRecordList.addAll(difference);

                        //串行，一个一个触发，
                        //先查看最近运行的一个进程的状态是否是已完成，如果未完成，只将所有的文件按照排序顺序追加到待处理文件记录中，如果已经完成或者是还没有进程记录，取出待处理文件记录的第一个，触发流水线
                        String oldProcessId = fileSchedule.getProcessId();
                        boolean isRun = false;
                        if (StringUtils.isBlank(oldProcessId)) {
                            isRun = true;
                        } else {
                            Process processById = null;
                            try {
                                processById = processDomain.getProcessById(null, true, oldProcessId);
                                if (ProcessState.COMPLETED.equals(processById.getState()) || ProcessState.FAILED.equals(processById.getState())
                                        || ProcessState.KILLED.equals(processById.getState())) {
                                    isRun = true;
                                }
                            } catch (Exception e) {
                                logger.warn("file schedule process not be found,process id:{},file schedule i:{},error massage:{}", oldProcessId, fileSchedule.getId(), e.getMessage());
                                isRun = true;
                            }
                        }
                        if (isRun) {
                            if (CollectionUtils.isNotEmpty(pendingFileRecordList)) {
                                String filePath = pendingFileRecordList.get(0);
                                String processId = flowServiceImpl.runFlowByFileSchedule(fileSchedule, filePath);

                                fileSchedule.setFilePath(filePath);
                                fileSchedule.setLastUpdateDttm(new Date());
                                fileSchedule.setProcessId(processId);
                                origin.setOriginFileRecord(String.join(",", allFilePath));
                                List<String> restPendingFileRecordList = pendingFileRecordList.stream().skip(1).collect(Collectors.toList());
                                origin.setPendingFileRecord(String.join(",", restPendingFileRecordList));
                                updateFileScheduleAndOrigin(fileSchedule, origin);
                            } else {
                                //没有待办，一直保持origin_file_record 为当前所有文件，走全覆盖
                                origin.setOriginFileRecord(String.join(",", allFilePath));
                                updateFileScheduleAndOrigin(null, origin);
                            }
                        } else {
                            origin.setOriginFileRecord(String.join(",", allFilePath));
                            origin.setPendingFileRecord(String.join(",", pendingFileRecordList));
                            updateFileScheduleAndOrigin(fileSchedule, origin);
                        }

                    }

                } catch (Exception e) {
                    logger.error("file schedule start flow error! flow schedule id:{}, flow id:{},errorMessage:{}", fileSchedule.getId(), fileSchedule.getAssociateId(), e.getMessage());
                }
            } else {
                //如果没有新增文件，且是串行的话，需要查看是否有待处理的文件，有的话需要触发
                if (FileScheduleTriggerMode.SERIAL.getValue().equals(triggerMode)) {
                    try {
                        //串行，一个一个触发，
                        //先查看最近运行的一个进程的状态是否是已完成，如果未完成，只将所有的文件按照排序顺序追加到待处理文件记录中，如果已经完成或者是还没有进程记录，取出待处理文件记录的第一个，触发流水线
                        List<String> pendingFileRecordList = new ArrayList<>();
                        String oldPendingFileRecord = origin.getPendingFileRecord();
                        if (StringUtils.isNotBlank(oldPendingFileRecord)) {
                            pendingFileRecordList.addAll(Arrays.asList(oldPendingFileRecord.split(",")));
                        }
                        String oldProcessId = fileSchedule.getProcessId();
                        boolean isRun = false;
                        if (StringUtils.isBlank(oldProcessId)) {
                            isRun = true;
                        } else {
                            try {
                                Process processById = processDomain.getProcessById(null, true, oldProcessId);
                                if (ProcessState.COMPLETED.equals(processById.getState()) || ProcessState.FAILED.equals(processById.getState())
                                        || ProcessState.KILLED.equals(processById.getState())) {
                                    isRun = true;
                                }
                            } catch (Exception e) {
                                //考虑到调度过程中上一次任务被手动删除，有可能找不到process,此时放行，继续运行
                                logger.warn("file schedule process not be found,process id:{},file schedule i:{},error massage:{}", oldProcessId, fileSchedule.getId(), e.getMessage());
                                isRun = true;
                            }
                        }
                        if (isRun) {
                            if (CollectionUtils.isNotEmpty(pendingFileRecordList)) {
                                String filePath = pendingFileRecordList.get(0);
                                String processId = flowServiceImpl.runFlowByFileSchedule(fileSchedule, filePath);

                                fileSchedule.setFilePath(filePath);
                                fileSchedule.setLastUpdateDttm(new Date());
                                fileSchedule.setProcessId(processId);
                                origin.setOriginFileRecord(String.join(",", allFilePath));
                                List<String> restPendingFileRecordList = pendingFileRecordList.stream().skip(1).collect(Collectors.toList());
                                origin.setPendingFileRecord(String.join(",", restPendingFileRecordList));
                                updateFileScheduleAndOrigin(fileSchedule, origin);
                            } else {
                                //没有新增，也没有待办，一直保持origin_file_record 为当前所有文件，走全覆盖
                                origin.setOriginFileRecord(String.join(",", allFilePath));
                                updateFileScheduleAndOrigin(null, origin);
                            }
                        } else {
                            //不运行新的任务，一直保持origin_file_record 为当前所有文件，走全覆盖
                            origin.setOriginFileRecord(String.join(",", allFilePath));
                            updateFileScheduleAndOrigin(null, origin);
                        }
                    } catch (Exception e) {
                        logger.error("file schedule start flow error! flow schedule id:{}, flow id:{},errorMessage:{}", fileSchedule.getId(), fileSchedule.getAssociateId(), e.getMessage());
                    }
                } else {
                    //并行，没有新增，一直保持origin_file_record 为当前所有文件，走全覆盖
                    origin.setOriginFileRecord(String.join(",", allFilePath));
                    updateFileScheduleAndOrigin(null, origin);
                }
            }
        }

    }

    @Override
    public String test(String id) {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(100);
                flowServiceImpl.runFlow("admin", true, "9553d716af274775ad51665d233e64c4", null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    private int updateFileScheduleAndOrigin(FileSchedule fileSchedule, FileScheduleOrigin origin) {
        int update = fileScheduleDomain.update(fileSchedule);
        fileScheduleDomain.updateOrigin(origin);
        return update;
    }


}
