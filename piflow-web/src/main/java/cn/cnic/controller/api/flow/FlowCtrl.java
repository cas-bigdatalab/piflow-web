package cn.cnic.controller.api.flow;

import cn.cnic.component.user.service.LogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.flow.service.IFlowService;
import cn.cnic.controller.requestVo.FlowInfoVoRequestAdd;
import cn.cnic.controller.requestVo.FlowInfoVoRequestUpdate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;

@Api(value = "flow api")
@Controller
@RequestMapping("/flow")
public class FlowCtrl {

    @Autowired
    private IFlowService flowServiceImpl;

    @Autowired
    private LogHelper logHelper;

    /**
     * flowList page query
     *
     * @param page 
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping(value = "/getFlowListPage", method = RequestMethod.GET)
    @ResponseBody
    public String getFlowListPage(Integer page, Integer limit, String param) {
        boolean isAdmin = SessionUserUtil.isAdmin();
        String username = SessionUserUtil.getCurrentUsername();
        return flowServiceImpl.getFlowListPage(username, isAdmin, page, limit, param);
    }

    /**
     * Enter the front page of the drawing board
     *
     * @param load
     * @param parentAccessPath
     * @return
     */
    @RequestMapping(value = "/drawingBoardData", method = RequestMethod.GET)
    @ResponseBody
    public String drawingBoardData(String load, String parentAccessPath) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return flowServiceImpl.drawingBoardData(username, isAdmin, load, parentAccessPath);
    }

    /**
     * run Flow
     *
     * @param flowId
     * @param runMode
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/runFlow", method = RequestMethod.POST)
    @ResponseBody
    public String runFlow(String flowId, String runMode) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelper.logAuthSucceed("run flow",username);
        return flowServiceImpl.runFlow(username, isAdmin, flowId, runMode);
    }

    @RequestMapping(value = "/queryFlowData", method = RequestMethod.POST)
    @ResponseBody
    public String queryFlowData(String load) {
        return flowServiceImpl.getFlowVoById(load);
    }

    /**
     * save flow
     *
     * @param flowVo
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/saveFlowInfo", method = RequestMethod.GET)
    @ResponseBody
    public String saveFlowInfo(FlowInfoVoRequestAdd flowVo) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        logHelper.logAuthSucceed("save flow",username);
        return flowServiceImpl.addFlow(username, flowVo);
    }

    /**
     * Delete flow association information according to flowId
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteFlow", method = RequestMethod.GET)
    @ResponseBody
    public String deleteFlow(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelper.logAuthSucceed("delete flow " + id,username);
        return flowServiceImpl.deleteFLowInfo(username, isAdmin, id);
    }

    @RequestMapping(value = "/updateFlowBaseInfo", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParam(name = "fId", value="fId")
    public String updateFlowBaseInfo(String fId, FlowInfoVoRequestUpdate flowVo) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        logHelper.logAuthSucceed("update flow base "+ flowVo.getName(),username);
        return flowServiceImpl.updateFlowBaseInfo(username, fId, flowVo);
    }

}
