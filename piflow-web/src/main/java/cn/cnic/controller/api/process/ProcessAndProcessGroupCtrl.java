package cn.cnic.controller.api.process;

import cn.cnic.component.system.service.ILogHelperService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.process.service.IProcessAndProcessGroupService;
import cn.cnic.component.process.service.IProcessGroupService;
import cn.cnic.component.process.service.IProcessService;
import io.swagger.annotations.Api;

@Api(value = "processAndProcessGroup api", tags = "processAndProcessGroup api")
@Controller
@RequestMapping("/processAndProcessGroup")
public class ProcessAndProcessGroupCtrl {

    private final IProcessAndProcessGroupService processAndProcessGroupServiceImpl;
    private final IProcessGroupService processGroupServiceImpl;
    private final IProcessService processServiceImpl;
    private final ILogHelperService logHelperServiceImpl;

    @Autowired
    public ProcessAndProcessGroupCtrl(IProcessAndProcessGroupService processAndProcessGroupServiceImpl,
                                      IProcessGroupService processGroupServiceImpl,
                                      IProcessService processServiceImpl,
                                      ILogHelperService logHelperServiceImpl) {
        this.processAndProcessGroupServiceImpl = processAndProcessGroupServiceImpl;
        this.processGroupServiceImpl = processGroupServiceImpl;
        this.processServiceImpl = processServiceImpl;
        this.logHelperServiceImpl = logHelperServiceImpl;
    }

    /**
     * Query and enter the process list
     *
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping(value = "/processAndProcessGroupListPage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="ProcessAndProcessGroupListPage", notes="Process and ProcessGroup list page")
    public String processAndProcessGroupListPage(Integer page, Integer limit, String name,Integer triggerMode,
                                                 String state, String ctrDttm, String scheduleName) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return processAndProcessGroupServiceImpl.getProcessAndProcessGroupListPage(username, isAdmin, page, limit, name,triggerMode, state, ctrDttm, scheduleName);
    }

    /**
     * Start Process Or ProcessGroup
     *
     * @param id
     * @param runMode
     * @param processType
     * @param checkpointStr
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/runProcessOrProcessGroup", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="runProcessOrProcessGroup", notes="Run Process or ProcessGroup")
    public String runProcessOrProcessGroup(String id, String runMode, String processType, String checkpointStr) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        if ("PROCESS".equals(processType)) {
            logHelperServiceImpl.logAuthSucceed("startProcess " + runMode,username);
            return processServiceImpl.startProcess(isAdmin, username, id, checkpointStr, runMode);
        } else {
            logHelperServiceImpl.logAuthSucceed("startProcessGroup " + runMode,username);
            return processGroupServiceImpl.startProcessGroup(isAdmin, username, id, checkpointStr, runMode);
        }
    }

    /**
     * Stop Process Group
     *
     * @param id
     * @param processType
     * @return
     */
    @RequestMapping(value = "/stopProcessOrProcessGroup", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="stopProcessOrProcessGroup", notes="stop Process or ProcessGroup")
    public String stopProcessOrProcessGroup(String id,String processType) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        if ("PROCESS".equals(processType)) {
            logHelperServiceImpl.logAuthSucceed("stopProcess " + id,username);
            return processServiceImpl.stopProcess(username, isAdmin, id);
        } else {
            logHelperServiceImpl.logAuthSucceed("stopProcessGroup " + id,username);
            return processGroupServiceImpl.stopProcessGroup(SessionUserUtil.getCurrentUsername(), SessionUserUtil.isAdmin(), id);
        }
    }

    /**
     * Delete Process
     *
     * @param id
     * @param processType
     * @return
     */
    @RequestMapping(value = "/delProcessOrProcessGroup", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="delProcessOrProcessGroup", notes="delete Process or ProcessGroup")
    public String delProcessGroup(String id, String processType) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        if ("PROCESS".equals(processType)) {
            logHelperServiceImpl.logAuthSucceed("delProcess"+ processType, username);
            return processServiceImpl.delProcess(isAdmin, username, id);
        } else {
            logHelperServiceImpl.logAuthSucceed("delProcessGroup" + processType,username);
            return processGroupServiceImpl.delProcessGroup(username, isAdmin, id);
        }
    }

    @RequestMapping(value = "/getAppInfoList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="getAppInfoList", notes="get App info list")
    public String getAppInfoList(String[] taskAppIds,String[] groupAppIds) {
        return processAndProcessGroupServiceImpl.getAppInfoList(taskAppIds, groupAppIds);
    }

}
