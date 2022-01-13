package cn.cnic.controller.api.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.common.Eunm.DrawingBoardType;
import cn.cnic.component.flow.service.IFlowGroupService;
import cn.cnic.component.flow.service.IFlowService;
import cn.cnic.controller.requestVo.FlowGroupInfoVoRequest;
import cn.cnic.controller.requestVo.FlowGroupInfoVoRequestUpDate;
import cn.cnic.component.user.service.LogHelper;
import io.swagger.annotations.Api;


@Api(value = "flowGroup api")
@Controller
@RequestMapping("/flowGroup")
public class FlowGroupCtrl {

    @Autowired
    private IFlowGroupService flowGroupServiceImpl;

    @Autowired
    private IFlowService flowServiceImpl;

    @Autowired
    private LogHelper logHelper;

    /**
     * ‘flowGroupList’ paged query
     *
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping(value = "/getFlowGroupListPage", method = RequestMethod.GET)
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
     * @throws Exception 
     */
    @RequestMapping(value = "/saveOrUpdateFlowGroup", method = RequestMethod.GET)
    @ResponseBody
    public String saveOrUpdateFlowGroup(FlowGroupInfoVoRequest flowGroupVo) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        logHelper.logAuthSucceed("saveOrUpdateFlowGroup " + flowGroupVo.getName() ,username);
        return flowGroupServiceImpl.saveOrUpdate(username, flowGroupVo);
    }
    
    @RequestMapping(value = "/updateFlowGroupBaseInfo", method = RequestMethod.POST)
    @ResponseBody
    public String updateFlowGroupBaseInfo(String fId, FlowGroupInfoVoRequestUpDate flowGroupVo) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        return flowGroupServiceImpl.updateFlowGroupBaseInfo(username, fId, flowGroupVo);
    }

    @RequestMapping(value = "/queryFlowGroupData", method = RequestMethod.POST)
    @ResponseBody
    public String queryFlowGroupData(String load) {
        return flowGroupServiceImpl.getFlowGroupVoInfoById(load);
    }

    @RequestMapping(value = "/queryIdInfo", method = RequestMethod.POST)
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
    @RequestMapping(value = "/findFlowByGroup", method = RequestMethod.POST)
    @ResponseBody
    public String findFlowByGroup(String fId, String pageId) {
        return flowGroupServiceImpl.queryIdInfo(fId, pageId);
    }

    /**
     * Enter the front page of the drawing board
     *
     * @param parentAccessPath
     * @param load
     * @param drawingBoardType
     * @param processType
     * @return
     */
    @RequestMapping(value = "/drawingBoardData", method = RequestMethod.GET)
    @ResponseBody
    public String drawingBoardData(String parentAccessPath, String load, DrawingBoardType drawingBoardType, String processType) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return flowGroupServiceImpl.drawingBoardData(username, isAdmin, load, parentAccessPath);
    }

    /**
     * run Flow Group
     *
     * @param flowGroupId
     * @param runMode
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/runFlowGroup", method = RequestMethod.POST)
    @ResponseBody
    public String runFlowGroup(String flowGroupId, String runMode) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelper.logAuthSucceed("runFlowGroup" + runMode,username);
        return flowGroupServiceImpl.runFlowGroup(isAdmin, username, flowGroupId, runMode);
    }

    /**
     * Delete flow association information according to flowId
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteFlowGroup", method = RequestMethod.GET)
    @ResponseBody
    public String deleteFlowGroup(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelper.logAuthSucceed("deleteFlowGroup" + id, username);
        return flowGroupServiceImpl.deleteFLowGroupInfo(isAdmin, username, id);
    }

    /**
     * Copy flow to group
     *
     * @param flowId
     * @param flowGroupId
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/copyFlowToGroup", method = RequestMethod.POST)
    @ResponseBody
    public String copyFlowToGroup(String flowId, String flowGroupId) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        return flowGroupServiceImpl.copyFlowToGroup(username, flowId, flowGroupId);
    }

    @RequestMapping(value = "/updateFlowNameById", method = RequestMethod.POST)
    @ResponseBody
    public String updateFlowNameById(String updateType, String parentId, String currentNodeId, String currentNodePageId, String name) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        if ("flowGroup".equals(updateType)) {
            return flowGroupServiceImpl.updateFlowGroupNameById(username, currentNodeId, parentId, name, currentNodePageId);
        }
        return flowServiceImpl.updateFlowNameById(username, currentNodeId, parentId, name, currentNodePageId);
    }

}
