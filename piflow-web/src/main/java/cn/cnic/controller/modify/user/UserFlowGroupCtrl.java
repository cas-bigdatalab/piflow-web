package cn.cnic.controller.modify.user;

import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.flow.service.IFlowGroupService;
import cn.cnic.component.flow.vo.FlowGroupVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user/flowGroup")
public class UserFlowGroupCtrl {

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
    public String getFlowGroupListPage(HttpServletRequest request, Integer page, Integer limit, String param) {
        String username = SessionUserUtil.getUsername(request);
        return flowGroupServiceImpl.getFlowGroupListPage(username, false, page, limit, param);
    }

    /**
     * Save add flowGroup
     *
     * @param flowGroupVo
     * @return
     */
    @RequestMapping("/saveOrUpdateFlowGroup")
    @ResponseBody
    public String saveOrUpdateFlowGroup(HttpServletRequest request, FlowGroupVo flowGroupVo) {
        String username = SessionUserUtil.getUsername(request);
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
        String username = SessionUserUtil.getUsername(request);
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
    public String deleteFlowGroup(HttpServletRequest request, String id) {
        return flowGroupServiceImpl.deleteFLowGroupInfo(SessionUserUtil.getUsername(request), id);
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
    public String copyFlowToGroup(HttpServletRequest request, String flowId, String flowGroupId) {
        String username = SessionUserUtil.getUsername(request);
        return flowGroupServiceImpl.copyFlowToGroup(username, flowId, flowGroupId);
    }

    @RequestMapping("/updateFlowGroupBaseInfo")
    @ResponseBody
    public String updateFlowGroupBaseInfo(HttpServletRequest request, FlowGroupVo flowGroupVo) {
        String username = SessionUserUtil.getUsername(request);
        return flowGroupServiceImpl.updateFlowGroupBaseInfo(username, flowGroupVo);
    }

}
