package cn.cnic.component.process.service.Impl;

import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.PageHelperUtils;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.service.IProcessAndProcessGroupService;
import cn.cnic.component.process.utils.ProcessGroupUtils;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.process.vo.ProcessGroupVo;
import cn.cnic.component.process.vo.ProcessVo;
import cn.cnic.component.process.mapper.ProcessAndProcessGroupMapper;
import cn.cnic.component.process.mapper.ProcessGroupMapper;
import cn.cnic.component.process.mapper.ProcessMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessAndProcessGroupServiceImpl implements IProcessAndProcessGroupService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ProcessAndProcessGroupMapper processAndProcessGroupMapper;

    @Resource
    private ProcessGroupMapper processGroupMapper;

    @Resource
    private ProcessMapper processMapper;

    /**
     * Query ProcessAndProcessGroupList (parameter space-time non-paging)
     *
     * @param offset Number of pages
     * @param limit  Number each page
     * @param param  Search content
     * @return json
     */
    @Override
    public String getProcessAndProcessGroupListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(ReturnMapUtils.ERROR_MSG);
        }
        Page<Process> page = PageHelper.startPage(offset, limit,"crt_dttm desc");
        if (isAdmin) {
            processAndProcessGroupMapper.getProcessAndProcessGroupList(param);
        } else {
            processAndProcessGroupMapper.getProcessAndProcessGroupListByUser(param, username);
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);
        rtnMap = PageHelperUtils.setLayTableParam(page, rtnMap);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * getAppInfoList
     *
     * @param taskAppIds  task appId array
     * @param groupAppIds group appId array
     * @return json
     */
    public String getAppInfoList(String[] taskAppIds, String[] groupAppIds) {
        if ((null == taskAppIds || taskAppIds.length <= 0) && (null == groupAppIds || groupAppIds.length <= 0)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Incoming parameter is null");
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);
        if (null != taskAppIds && taskAppIds.length > 0) {
            Map<String, Object> taskAppInfoMap = new HashMap<>();
            List<Process> processListByAppIDs = processMapper.getProcessListByAppIDs(taskAppIds);
            if (CollectionUtils.isNotEmpty(processListByAppIDs)){
                for (Process process : processListByAppIDs) {
                    ProcessVo processVo = ProcessUtils.processPoToVo(process);
                    if (null == processVo) {
                        continue;
                    }
                    taskAppInfoMap.put(processVo.getAppId(), processVo);
                }
            }
            rtnMap.put("taskAppInfo",taskAppInfoMap);
        }
        if (null != groupAppIds && groupAppIds.length > 0) {
            Map<String, Object> groupAppInfoMap = new HashMap<>();
            List<ProcessGroup> processGroupListByAppIDs = processGroupMapper.getProcessGroupListByAppIDs(groupAppIds);
            if (CollectionUtils.isNotEmpty(processGroupListByAppIDs)) {
                for (ProcessGroup processGroup : processGroupListByAppIDs) {
                    ProcessGroupVo processGroupVo = ProcessGroupUtils.processGroupPoToVo(processGroup);
                    if (null == processGroupVo) {
                        continue;
                    }
                    groupAppInfoMap.put(processGroupVo.getAppId(), processGroupVo);
                }
            }
            rtnMap.put("groupAppInfo",groupAppInfoMap);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

}