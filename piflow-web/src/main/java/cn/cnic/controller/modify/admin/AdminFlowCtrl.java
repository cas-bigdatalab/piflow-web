package cn.cnic.controller.modify.admin;

import cn.cnic.controller.modify.utils.UserUtils;
import cn.cnic.component.flow.model.Flow;
import cn.cnic.component.flow.service.IFlowGroupService;
import cn.cnic.component.flow.service.IFlowService;
import cn.cnic.component.flow.vo.FlowVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Controller
@RequestMapping("/admin/flow")
public class AdminFlowCtrl {

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
    public String getFlowListPage(HttpServletRequest request,Integer page, Integer limit, String param) {
        String username = UserUtils.getUsername(request);
        return flowServiceImpl.getFlowListPage(username, true, page, limit, param);
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
        String username = UserUtils.getUsername(request);
        return flowServiceImpl.runFlow(username, true, flowId, runMode);
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
    public String saveFlowInfo(HttpServletRequest request,FlowVo flowVo) {
        String username = UserUtils.getUsername(request);
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
    public String updateFlowInfo(HttpServletRequest request,Flow flow) {
        String username = UserUtils.getUsername(request);
        return flowServiceImpl.updateFlow(username, flow);
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
    public String deleteFlow(HttpServletRequest request,String id) {
        String username = UserUtils.getUsername(request);
        return flowServiceImpl.deleteFLowInfo(username, true, id);
    }

    @RequestMapping("/queryFlowGroupData")
    @ResponseBody
    public String queryFlowGroupData(String load) {
        return flowGroupServiceImpl.getFlowGroupVoInfoById(load);
    }

    @RequestMapping("/queryIdInfo")
    @ResponseBody
    public String queryIdInfo(String fid, String flowPageId) {
        return flowGroupServiceImpl.queryIdInfo(fid,flowPageId);
    }

    @RequestMapping("/updateFlowBaseInfo")
    @ResponseBody
    public String updateFlowBaseInfo(HttpServletRequest request,String fId,FlowVo flowVo) {
        String username = UserUtils.getUsername(request);
        return flowServiceImpl.updateFlowBaseInfo(username,fId, flowVo);
    }

    @RequestMapping("/updateFlowNameById")
    @ResponseBody
    public String updateFlowNameById(HttpServletRequest request) {
        String flowId = request.getParameter("flowId");
        String flowGroupId = request.getParameter("flowGroupId");
        String name = request.getParameter("name");
        String pageId = request.getParameter("pageId");
        String updateType = request.getParameter("updateType");
        String username = UserUtils.getUsername(request);
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
