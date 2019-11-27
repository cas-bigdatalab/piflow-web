package com.nature.controller;

import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.FlowGroup;
import com.nature.component.flow.service.IFlowGroupService;
import com.nature.component.flow.service.IFlowService;
import com.nature.component.flow.vo.FlowGroupVo;
import com.nature.component.flow.vo.FlowVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

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
     * @param request
     * @param start
     * @param length
     * @param draw
     * @param extra_search
     * @return
     */
    @RequestMapping("/getFlowGroupListPage")
    @ResponseBody
    public String getFlowGroupListPage(HttpServletRequest request, Integer start, Integer length, Integer draw, String extra_search) {
        return flowGroupServiceImpl.getFlowGroupListPage(start / length + 1, length, extra_search);
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
     * save data
     *
     * @param request
     * @return
     */
    @RequestMapping("/saveFlowGroupData")
    @ResponseBody
    public String saveFlowGroupData(HttpServletRequest request) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        String imageXML = request.getParameter("imageXML");
        String loadId = request.getParameter("load");
        String operType = request.getParameter("operType");
        return flowGroupServiceImpl.saveOrUpdateFlowGroupAll(imageXML, loadId, operType, true);
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

}
