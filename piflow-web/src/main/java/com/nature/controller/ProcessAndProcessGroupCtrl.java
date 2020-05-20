package com.nature.controller;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.vo.UserVo;
import com.nature.component.process.service.IProcessAndProcessGroupService;
import com.nature.component.process.service.IProcessGroupService;
import com.nature.component.process.service.IProcessService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/processAndProcessGroup")
public class ProcessAndProcessGroupCtrl {

    /**
     * Introduce the log, note that all are under the "org.slf4j" package
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private IProcessService processServiceImpl;

    @Resource
    private IProcessGroupService processGroupServiceImpl;

    @Resource
    private IProcessAndProcessGroupService processAndProcessGroupServiceImpl;


    /**
     * Query and enter the process list
     *
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping("/processAndProcessGroupListPage")
    @ResponseBody
    public String processAndProcessGroupListPage(Integer page, Integer limit, String param) {
        return processAndProcessGroupServiceImpl.getProcessAndProcessGroupListPage(page, limit, param);
    }

    /**
     * Start Process Or ProcessGroup
     *
     * @param request
     * @return
     */
    @RequestMapping("/runProcessOrProcessGroup")
    @ResponseBody
    public String runProcessOrProcessGroup(HttpServletRequest request) {
        String id = request.getParameter("id");
        String runMode = request.getParameter("runMode");
        String processType = request.getParameter("processType");
        String checkpoint = request.getParameter("checkpointStr");
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if ("PROCESS".equals(processType)) {
            return processServiceImpl.startProcess(id, checkpoint, runMode, currentUser);
        } else {
            return processGroupServiceImpl.startProcessGroup(id, checkpoint, runMode, currentUser);
        }
    }

    /**
     * Stop Process Group
     *
     * @param request
     * @return
     */
    @RequestMapping("/stopProcessOrProcessGroup")
    @ResponseBody
    public String stopProcessOrProcessGroup(HttpServletRequest request) {
        String id = request.getParameter("id");
        String processType = request.getParameter("processType");
        if ("PROCESS".equals(processType)) {
            return processServiceImpl.stopProcess(id);
        } else {
            return processGroupServiceImpl.stopProcessGroup(id);
        }
    }

    /**
     * Delete Process
     *
     * @param request
     * @return
     */
    @RequestMapping("/delProcessOrProcessGroup")
    @ResponseBody
    public String delProcessGroup(HttpServletRequest request) {
        String id = request.getParameter("id");
        String processType = request.getParameter("processType");
        if ("PROCESS".equals(processType)) {
            return processServiceImpl.delProcess(id);
        } else {
            return processGroupServiceImpl.delProcessGroup(id);
        }
    }

}
