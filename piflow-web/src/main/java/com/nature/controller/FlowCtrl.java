package com.nature.controller;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.service.IFlowService;
import com.nature.component.flow.vo.FlowVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Controller
@RequestMapping("/flow")
public class FlowCtrl {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private IFlowService flowServiceImpl;

    /**
     * flowList分页查询
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
     * 启动flow
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/runFlow")
    @ResponseBody
    public String runFlow(HttpServletRequest request, Model model) {
        String flowId = request.getParameter("flowId");
        return flowServiceImpl.runFlow(flowId);
    }

    @RequestMapping("/queryFlowData")
    @ResponseBody
    public String queryFlowData(String load) {
        return flowServiceImpl.getFlowVoById(load);
    }

    /**
     * 保存添加flow
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
     * 修改Flow
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
     * 根据flowId删除flow关联信息
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
}
