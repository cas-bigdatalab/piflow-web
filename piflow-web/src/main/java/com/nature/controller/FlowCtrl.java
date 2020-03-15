package com.nature.controller;

import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.ReturnMapUtils;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.service.IFlowGroupService;
import com.nature.component.flow.service.IFlowService;
import com.nature.component.flow.vo.FlowGroupVo;
import com.nature.component.flow.vo.FlowVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private IFlowService flowServiceImpl;

    @Resource
    private IFlowGroupService flowGroupServiceImpl;

    /**
     * getFlowListHtml
     *
     * @return
     */
    @RequestMapping("/getFlowListHtml")
    public String getFlowListHtml() {
        return "mxGraph/flow_List";
    }

    /**
     * flowList page query
     *
     * @param request
     * @param start
     * @param length
     * @param draw
     * @param extra_search
     * @return
     */
    @RequestMapping("/getFlowListPage")
    @ResponseBody
    public String getFlowListPage(HttpServletRequest request, Integer start, Integer length, Integer draw, String extra_search) {
        return flowServiceImpl.getFlowListPage(start / length + 1, length, extra_search);
    }

    /**
     * run Flow
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/runFlow")
    @ResponseBody
    public String runFlow(HttpServletRequest request, Model model) {
        String flowId = request.getParameter("flowId");
        String runMode = request.getParameter("runMode");
        return flowServiceImpl.runFlow(flowId, runMode);
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
        UserVo user = SessionUserUtil.getCurrentUser();
        return flowServiceImpl.addFlow(flowVo, user);
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
        UserVo user = SessionUserUtil.getCurrentUser();
        int result = flowServiceImpl.updateFlow(flow, user);
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
        return flowServiceImpl.deleteFLowInfo(id);
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
        return flowServiceImpl.updateFlowBaseInfo(flowVo);
    }

    @RequestMapping("/updateFlowNameById")
    @ResponseBody
    public String updateFlowNameById(HttpServletRequest request) {
        //String id = request.getParameter("stopId");
        //String flowId = request.getParameter("flowId");
        String flowId = request.getParameter("flowId");
        String flowGroupId = request.getParameter("flowGroupId");
        String name = request.getParameter("name");
        String pageId = request.getParameter("pageId");
        String updateType = request.getParameter("updateType");
        if ("flowGroup".equals(updateType)) {
            return flowGroupServiceImpl.updateFlowGroupNameById(flowId, flowGroupId, name, pageId);
        }
        return flowServiceImpl.updateFlowNameById(flowId, flowGroupId, name, pageId);
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
