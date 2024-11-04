package cn.cnic.component.process.service.Impl;

import java.util.*;

import cn.cnic.base.utils.*;
import cn.cnic.common.Eunm.SysRoleType;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.flow.domain.FlowPublishDomain;
import cn.cnic.component.flow.entity.FlowPublishing;
import cn.cnic.component.process.domain.ProcessGroupDomain;
import cn.cnic.component.process.service.IProcessService;
import cn.cnic.component.process.vo.ProcessAndProcessGroupVo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.service.IProcessAndProcessGroupService;
import cn.cnic.component.process.utils.ProcessGroupUtils;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.process.vo.ProcessGroupVo;
import cn.cnic.component.process.vo.ProcessVo;

@Service
public class ProcessAndProcessGroupServiceImpl implements IProcessAndProcessGroupService {

    private static Logger logger = LoggerUtil.getLogger();


    private final ProcessGroupDomain processGroupDomain;
    private final IProcessService processServiceImpl;
    private final FlowPublishDomain flowPublishDomain;

    @Autowired
    public ProcessAndProcessGroupServiceImpl(ProcessGroupDomain processGroupDomain,
                                             IProcessService processServiceImpl,
                                             FlowPublishDomain flowPublishDomain) {

        this.processGroupDomain = processGroupDomain;
        this.processServiceImpl = processServiceImpl;
        this.flowPublishDomain = flowPublishDomain;
    }

    /**
     * Query ProcessAndProcessGroupList (parameter space-time non-paging)
     *
     * @param offset Number of pages
     * @param limit  Number each page
     * @param param  Search content
     * @return json
     */
    @Override
    public String getProcessAndProcessGroupListPage(String createUser, Integer offset, Integer limit, String param,
                                                    String name, String state, String company) {
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        Page<ProcessAndProcessGroupVo> page = PageHelper.startPage(offset, limit,"crt_dttm desc");
        SysRoleType currentUserRole = SessionUserUtil.getCurrentUserRole();
        switch (currentUserRole) {
            case ADMIN:
            case ORS_ADMIN:
                processGroupDomain.getProcessAndProcessGroupList(createUser, param, name, state, company);
                break;
            default:
                processGroupDomain.getProcessAndProcessGroupListByUser(param, createUser);

        }
        List<ProcessAndProcessGroupVo> result = injectFlowPublishingInfo(page);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamWithChangeResultRtnStr(page, new ArrayList<>(result), rtnMap);
        //return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
    }

    private List<ProcessAndProcessGroupVo> injectFlowPublishingInfo(Page<ProcessAndProcessGroupVo> page) {
        List<ProcessAndProcessGroupVo> result = page.getResult();
        for (ProcessAndProcessGroupVo vo : result) {
            FlowPublishing flow = flowPublishDomain.getByIdId(vo.getFlowId());
            if (null == flow ) {
                logger.error("FlowPublishDomain.getByIdId is null, flowId:{}", vo.getFlowId());
                continue;
            }
            if (null == flow.getProductTypeName() || null == flow.getProductTypeId()) {
                logger.error("FlowPublishDomain.getProductTypeName or getProductTypeId is null, flowId:{}," +
                        " productTypeName:{}, productTypeId:{}", vo.getFlowId(), flow.getProductTypeName(), flow.getProductTypeId());
            }
            vo.setProductTypeName(flow.getProductTypeName());
            vo.setProductTypeId(flow.getProductTypeId());
        }
        return result;
    }

    /**
     * getAppInfoList
     *
     * @param taskAppIds  task appId array
     * @param groupAppIds group appId array
     * @return json
     */
    @Override
    public String getAppInfoList(String[] taskAppIds, String[] groupAppIds) {
        if ((null == taskAppIds || taskAppIds.length <= 0) && (null == groupAppIds || groupAppIds.length <= 0)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        if (null != taskAppIds && taskAppIds.length > 0) {
            Map<String, Object> taskAppInfoMap = new HashMap<>();
            List<Process> processListByAppIDs = processGroupDomain.getProcessListByAppIDs(taskAppIds);
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
            List<ProcessGroup> processGroupListByAppIDs = processGroupDomain.getProcessGroupListByAppIDs(groupAppIds);
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
        return ReturnMapUtils.toJson(rtnMap);
    }

}