package cn.cnic.controller.modify.admin;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.process.service.IProcessAndProcessGroupService;
import cn.cnic.component.process.service.IProcessGroupService;
import cn.cnic.component.process.service.IProcessService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/admin/processAndProcessGroup")
public class AdminProcessAndProcessGroupCtrl {

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
    public String processAndProcessGroupListPage(HttpServletRequest request, Integer page, Integer limit, String param) {
        String username = SessionUserUtil.getUsername(request);
        return processAndProcessGroupServiceImpl.getProcessAndProcessGroupListPage(username, true, page, limit, param);
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
        String username = SessionUserUtil.getUsername(request);
        if ("PROCESS".equals(processType)) {
            return processServiceImpl.startProcess(username, id, checkpoint, runMode);
        } else {
            return processGroupServiceImpl.startProcessGroup(username, id, checkpoint, runMode);
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
        String username = SessionUserUtil.getUsername(request);
        if ("PROCESS".equals(processType)) {
            return processServiceImpl.stopProcess(username, true, id);
        } else {
            return processGroupServiceImpl.stopProcessGroup(username, true, id);
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
        String username = SessionUserUtil.getUsername(request);
        if ("PROCESS".equals(processType)) {
            return processServiceImpl.delProcess(username, id);
        } else {
            return processGroupServiceImpl.delProcessGroup(username, true, id);
        }
    }

}
