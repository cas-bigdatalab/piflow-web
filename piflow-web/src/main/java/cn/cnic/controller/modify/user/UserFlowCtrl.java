package cn.cnic.controller.modify.user;

import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.base.util.SessionUserUtil;
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
@RequestMapping("/user/flow")
public class UserFlowCtrl {

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
    public String getFlowListPage(HttpServletRequest request, Integer page, Integer limit, String param) {
        String username = SessionUserUtil.getUsername(request);
        return flowServiceImpl.getFlowListPage(username, false, page, limit, param);
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
        String username = SessionUserUtil.getUsername(request);
        return flowServiceImpl.runFlow(username, false, flowId, runMode);
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
    public String saveFlowInfo(HttpServletRequest request, FlowVo flowVo) {
        String username = SessionUserUtil.getUsername(request);
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
    public int updateFlowInfo(HttpServletRequest request, Flow flow) {
        String username = SessionUserUtil.getUsername(request);
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
    public int deleteFlow(HttpServletRequest request, String id) {
        String username = SessionUserUtil.getUsername(request);
        return flowServiceImpl.deleteFLowInfo(username, false, id);
    }

    @RequestMapping("/queryFlowGroupData")
    @ResponseBody
    public String queryFlowGroupData(String load) {
        return flowGroupServiceImpl.getFlowGroupVoById(load);
    }

    @RequestMapping("/queryIdInfo")
    @ResponseBody
    public String queryIdInfo(String fid, String flowPageId) {
        return flowGroupServiceImpl.queryIdInfo(fid, flowPageId);
    }

    @RequestMapping("/updateFlowBaseInfo")
    @ResponseBody
    public String updateFlowBaseInfo(HttpServletRequest request, FlowVo flowVo) {
        String username = SessionUserUtil.getUsername(request);
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
        String username = SessionUserUtil.getUsername(request);
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
        return flowGroupServiceImpl.queryIdInfo(fId,flowPageId);
    }
}
