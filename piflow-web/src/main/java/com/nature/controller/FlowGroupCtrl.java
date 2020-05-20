package com.nature.controller;

import com.nature.base.util.LoggerUtil;
import com.nature.component.flow.service.IFlowGroupService;
import com.nature.component.flow.vo.FlowGroupVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/flowGroup")
public class FlowGroupCtrl {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
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
        return flowGroupServiceImpl.getFlowGroupListPage(page, limit, param);
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
        return flowGroupServiceImpl.saveOrUpdate(flowGroupVo);
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
        return flowGroupServiceImpl.runFlowGroup(flowGroupId, runMode);
    }

    /**
     * Delete flow association information according to flowId
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteFlowGroup")
    @ResponseBody
    public int deleteFlowGroup(String id) {
        return flowGroupServiceImpl.deleteFLowGroupInfo(id);
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
        return flowGroupServiceImpl.copyFlowToGroup(flowId, flowGroupId);
    }

    @RequestMapping("/updateFlowGroupBaseInfo")
    @ResponseBody
    public String updateFlowGroupBaseInfo(FlowGroupVo flowGroupVo) {
        return flowGroupServiceImpl.updateFlowGroupBaseInfo(flowGroupVo);
    }

}
