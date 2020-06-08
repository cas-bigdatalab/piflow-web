package cn.cnic.controller;

import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.base.vo.UserVo;
import cn.cnic.component.flow.model.Flow;
import cn.cnic.component.flow.service.IFlowGroupService;
import cn.cnic.component.flow.service.IFlowService;
import cn.cnic.component.flow.vo.FlowGroupVo;
import cn.cnic.component.flow.vo.FlowVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/flow")
public class FlowCtrl {

    @Resource
    private IFlowService flowServiceImpl;

    @Resource
    private IFlowGroupService flowGroupServiceImpl;

    /**
     * flowList page query
     *
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping("/getFlowListPage")
    @ResponseBody
    public String getFlowListPage(Integer page, Integer limit, String param) {
        boolean isAdmin = SessionUserUtil.isAdmin();
        String username = SessionUserUtil.getCurrentUsername();
        return flowServiceImpl.getFlowListPage(username, isAdmin, page, limit, param);
    }

    /**
     * run Flow
     *
     * @param request
     * @return
     */
    @RequestMapping("/runFlow")
    @ResponseBody
    public String runFlow(HttpServletRequest request) {
        String flowId = request.getParameter("flowId");
        String runMode = request.getParameter("runMode");
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return flowServiceImpl.runFlow(username, isAdmin, flowId, runMode);
    }

    @RequestMapping("/queryFlowData")
    @ResponseBody
    public String queryFlowData(String load) {
        return flowServiceImpl.getFlowVoById(load);
    }

    /**
     * save flow
     *
     * @param flowVo
     * @return
     */
    @RequestMapping("/saveFlowInfo")
    @ResponseBody
    public String saveFlowInfo(FlowVo flowVo) {
        String username = SessionUserUtil.getCurrentUsername();
        return flowServiceImpl.addFlow(username, flowVo);
    }

    /**
     * update Flow
     *
     * @param flow
     * @return
     */
    @RequestMapping("/updateFlowInfo")
    @ResponseBody
    public int updateFlowInfo(Flow flow) {
        String username = SessionUserUtil.getCurrentUsername();
        int result = flowServiceImpl.updateFlow(username, flow);
        return result;
    }

    /**
     * Delete flow association information according to flowId
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteFlow")
    @ResponseBody
    @Transactional
    public int deleteFlow(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return flowServiceImpl.deleteFLowInfo(username, isAdmin, id);
    }

    @RequestMapping("/queryFlowGroupData")
    @ResponseBody
    public String queryFlowGroupData(String load) {
        return flowGroupServiceImpl.getFlowGroupVoById(load);
    }

    @RequestMapping("/queryIdInfo")
    @ResponseBody
    public String queryIdInfo(String fid, String flowPageId) {
        if (StringUtils.isBlank(fid) || StringUtils.isBlank(flowPageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Missing parameters");
        }
        Map<String, Object> rtnMap = new HashMap<>();
        FlowVo flowVo = flowServiceImpl.getFlowByPageId(fid, flowPageId);
        FlowGroupVo flowGroupVo = flowGroupServiceImpl.getFlowGroupByPageId(fid, flowPageId);
        if (null != flowVo) {
            //Compare the stops template properties and make changes
            //propertyServiceImpl.checkStopTemplateUpdate(queryInfo.getId());
            rtnMap.put("nodeType", "flow");
        } else if (null != flowGroupVo) {
            rtnMap.put("nodeType", "flowGroup");
        }
        rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
        rtnMap.put("flowVo", flowVo);
        rtnMap.put("flowGroupVo", flowGroupVo);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @RequestMapping("/updateFlowBaseInfo")
    @ResponseBody
    public String updateFlowBaseInfo(FlowVo flowVo) {
        String username = SessionUserUtil.getCurrentUsername();
        return flowServiceImpl.updateFlowBaseInfo(username, flowVo);
    }

    @RequestMapping("/updateFlowNameById")
    @ResponseBody
    public String updateFlowNameById(HttpServletRequest request) {
        String flowId = request.getParameter("flowId");
        String flowGroupId = request.getParameter("flowGroupId");
        String name = request.getParameter("name");
        String pageId = request.getParameter("pageId");
        String updateType = request.getParameter("updateType");
        String username = SessionUserUtil.getCurrentUsername();
        if ("flowGroup".equals(updateType)) {
            return flowGroupServiceImpl.updateFlowGroupNameById(username, flowId, flowGroupId, name, pageId);
        }
        return flowServiceImpl.updateFlowNameById(username, flowId, flowGroupId, name, pageId);
    }

    /**
     * findFlowByGroup
     *
     * @param fId
     * @param flowPageId
     * @return
     */
    @RequestMapping("/findFlowByGroup")
    @ResponseBody
    public String findFlowByGroup(String fId, String flowPageId) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        FlowVo flowVo = flowServiceImpl.getFlowByPageId(fId, flowPageId);
        FlowGroupVo flowGroupVo = flowGroupServiceImpl.getFlowGroupByPageId(fId, flowPageId);
        if (null != flowVo) {
            rtnMap.put("nodeType", "flow");
        } else if (null != flowGroupVo) {
            rtnMap.put("nodeType", "flowGroup");
        }
        rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
        rtnMap.put("flowVo", flowVo);
        rtnMap.put("flowGroupVo", flowGroupVo);
        return JsonUtils.toJsonNoException(rtnMap);
    }
}
