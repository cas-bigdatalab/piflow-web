package cn.cnic.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.common.Eunm.DrawingBoardType;
import cn.cnic.component.flow.service.IFlowGroupService;
import cn.cnic.component.flow.service.IFlowService;
import cn.cnic.component.flow.vo.FlowGroupVo;

@Controller
@RequestMapping("/flowGroup")
public class FlowGroupCtrl {

    @Autowired
    private IFlowGroupService flowGroupServiceImpl;

    @Autowired
    private IFlowService flowServiceImpl;

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

    @RequestMapping("/queryFlowGroupData")
    @ResponseBody
    public String queryFlowGroupData(String load) {
        return flowGroupServiceImpl.getFlowGroupVoInfoById(load);
    }

    @RequestMapping("/queryIdInfo")
    @ResponseBody
    public String queryIdInfo(String fId, String pageId) {
        return flowGroupServiceImpl.queryIdInfo(fId, pageId);
    }

    /**
     * findFlowByGroup
     *
     * @param fId
     * @param pageId
     * @return
     */
    @RequestMapping("/findFlowByGroup")
    @ResponseBody
    public String findFlowByGroup(String fId, String pageId) {
        return flowGroupServiceImpl.queryIdInfo(fId, pageId);
    }

    /**
     * Enter the front page of the drawing board
     *
     * @param request
     * @param model
     * @param drawingBoardType
     * @return
     */
    @RequestMapping("/drawingBoardData")
    @ResponseBody
    public String drawingBoardData(HttpServletRequest request, Model model, DrawingBoardType drawingBoardType, String processType) {
        String load = request.getParameter("load");
        //set parentAccessPath
        String parentAccessPath = request.getParameter("parentAccessPath");
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return flowGroupServiceImpl.drawingBoardData(username, isAdmin, load, parentAccessPath);
    }

    /**
     * run Flow Group
     *
     * @param request
     * @return
     */
    @RequestMapping("/runFlowGroup")
    @ResponseBody
    public String runFlowGroup(String flowGroupId, String runMode) {
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
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return flowGroupServiceImpl.deleteFLowGroupInfo(isAdmin, username, id);
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
    public String updateFlowGroupBaseInfo(String fId, FlowGroupVo flowGroupVo) {
        String username = SessionUserUtil.getCurrentUsername();
        return flowGroupServiceImpl.updateFlowGroupBaseInfo(username, fId, flowGroupVo);
    }

    @RequestMapping("/updateFlowNameById")
    @ResponseBody
    public String updateFlowNameById(HttpServletRequest request) {
        String updateType = request.getParameter("updateType");
        String parentId = request.getParameter("parentId");
        String currentNodeId = request.getParameter("currentNodeId");
        String currentNodePageId = request.getParameter("currentNodePageId");
        String name = request.getParameter("name");
        String username = SessionUserUtil.getCurrentUsername();
        if ("flowGroup".equals(updateType)) {
            return flowGroupServiceImpl.updateFlowGroupNameById(username, currentNodeId, parentId, name, currentNodePageId);
        }
        return flowServiceImpl.updateFlowNameById(username, currentNodeId, parentId, name, currentNodePageId);
    }

}
