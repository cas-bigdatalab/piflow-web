package cn.cnic.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.flow.service.IFlowGroupService;
import cn.cnic.component.flow.vo.FlowGroupVo;

@Controller
@RequestMapping("/flowGroup")
public class FlowGroupCtrl {

    @Resource
    private IFlowGroupService flowGroupServiceImpl;

    /**
     * ‘flowGroupList’ paged query
     *
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping("/getFlowGroupListPage")
    @ResponseBody
    public String getFlowGroupListPage(Integer page, Integer limit, String param) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return flowGroupServiceImpl.getFlowGroupListPage(username, isAdmin, page, limit, param);
    }

    /**
     * Save add flowGroup
     *
     * @param flowGroupVo
     * @return
     */
    @RequestMapping("/saveOrUpdateFlowGroup")
    @ResponseBody
    public String saveOrUpdateFlowGroup(FlowGroupVo flowGroupVo) {
        String username = SessionUserUtil.getCurrentUsername();
        return flowGroupServiceImpl.saveOrUpdate(username, flowGroupVo);
    }

    /**
     * run Flow Group
     *
     * @param request
     * @return
     */
    @RequestMapping("/runFlowGroup")
    @ResponseBody
    public String runFlowGroup(HttpServletRequest request) {
        String flowGroupId = request.getParameter("flowGroupId");
        String runMode = request.getParameter("runMode");
        String username = SessionUserUtil.getCurrentUsername();
        return flowGroupServiceImpl.runFlowGroup(username, flowGroupId, runMode);
    }

    /**
     * Delete flow association information according to flowId
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteFlowGroup")
    @ResponseBody
    public String deleteFlowGroup(String id) {
        return flowGroupServiceImpl.deleteFLowGroupInfo(SessionUserUtil.getCurrentUsername(), id);
    }

    /**
     * Copy flow to group
     *
     * @param flowId
     * @param flowGroupId
     * @return
     */
    @RequestMapping("/copyFlowToGroup")
    @ResponseBody
    public String copyFlowToGroup(String flowId, String flowGroupId) {
        String username = SessionUserUtil.getCurrentUsername();
        return flowGroupServiceImpl.copyFlowToGroup(username, flowId, flowGroupId);
    }

    @RequestMapping("/updateFlowGroupBaseInfo")
    @ResponseBody
    public String updateFlowGroupBaseInfo(FlowGroupVo flowGroupVo) {
        String username = SessionUserUtil.getCurrentUsername();
        return flowGroupServiceImpl.updateFlowGroupBaseInfo(username, flowGroupVo);
    }

}
