package cn.cnic.controller.api.process;

import java.util.Map;

import cn.cnic.common.Eunm.SysRoleType;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.system.domain.SysUserDomain;
import cn.cnic.component.system.service.ILogHelperService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.process.service.IProcessGroupService;
import cn.cnic.component.process.service.IProcessService;
import io.swagger.annotations.Api;

import static cn.cnic.common.Eunm.SysRoleType.ORS_ADMIN;

@Api(value = "processGroup api", tags = "processGroup api")
@Controller
@RequestMapping("/processGroup")
public class ProcessGroupCtrl {

    private final IProcessGroupService processGroupServiceImpl;
    private final ILogHelperService logHelperServiceImpl;
    private final IProcessService processServiceImpl;
    private final SysUserDomain sysUserDomain;


    @Autowired
    public ProcessGroupCtrl(IProcessGroupService processGroupServiceImpl,
                            ILogHelperService logHelperServiceImpl,
                            IProcessService processServiceImpl,
                            SysUserDomain sysUserDomain) {
        this.processGroupServiceImpl = processGroupServiceImpl;
        this.logHelperServiceImpl = logHelperServiceImpl;
        this.processServiceImpl = processServiceImpl;
        this.sysUserDomain = sysUserDomain;
    }

    /**
     * Query and enter the process list
     *
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping(value = "/processGroupListPage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="processGroupListPage", notes="get ProcessGroup list page")
    public String processGroupListPage(Integer page, Integer limit, String param, String name, String state, String crtUser, String company) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdminOrORSAdmin();
        SysRoleType currentUserRole = SessionUserUtil.getCurrentUserRole();
        // 台站管理员只能看到本台站的
        if (ORS_ADMIN.equals(currentUserRole)) {
            String userid = sysUserDomain.findUserByUserName(SessionUserUtil.getCurrentUser().getUsername()).getId();
            company = sysUserDomain.getSysUserCompanyById(userid);
        }
        return processGroupServiceImpl.getProcessGroupVoListPage(username, isAdmin, page, limit, param, name, state, crtUser, company);
    }

    /**
     * Query and enter the process list
     *
     * @param start
     * @param length
     * @param draw
     * @param extra_search
     * @return
     */
    @RequestMapping(value = "/processListPage", method=RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="processListPage", notes="get Process list")
    public String processListPage(Integer start, Integer length, Integer draw, String extra_search) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return processServiceImpl.getProcessGroupVoListPage(username, isAdmin, start / length + 1, length, extra_search);
    }

    /**
     * Enter the front page of the drawing board
     *
     * @param loadId
     * @param parentAccessPath
     * @return
     */
    @RequestMapping(value = "/drawingBoardData", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="drawingBoardData", notes="drawingBoard data")
    public String drawingBoardData(String loadId, String parentAccessPath) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return processGroupServiceImpl.drawingBoardData(currentUsername, isAdmin, loadId, parentAccessPath);
    }

    /**
     * Query Process basic information
     *
     * @param processGroupId
     * @return
     */
    @RequestMapping(value = "/queryProcessGroup", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="queryProcessGroup", notes="query ProcessGroup")
    public String queryProcessGroup(String processGroupId) {
        
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return processGroupServiceImpl.getProcessGroupVoById(username, isAdmin, processGroupId);
    }

    /**
     * Query ProcessStop basic information
     *
     * @param processGroupId
     * @param pageId
     * @return
     */
    @RequestMapping(value = "/queryProcess", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="queryProcess", notes="query Process")
    public String queryProcess(String processGroupId, String pageId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return processGroupServiceImpl.getProcessGroupNode(username, isAdmin, processGroupId, pageId);
    }

    /**
     * Query ProcessPath basic information
     *
     * @param processGroupId
     * @param pageId
     * @return
     */
    @RequestMapping(value = "/queryProcessGroupPath", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="queryProcessGroupPath", notes="query ProcessGroupPath")
    public String queryProcessGroupPath(String processGroupId, String pageId) {
        return processGroupServiceImpl.getProcessGroupPathVoByPageId(processGroupId, pageId);
    }

    /**
     * Start Process
     *
     * @param id
     * @param checkpointStr
     * @param runMode
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/runProcessGroup", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="runProcessGroup", notes="run ProcessGroup")
    public String runProcessGroup(String id, String checkpointStr, String runMode) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelperServiceImpl.logAuthSucceed("runProcessGroup " + runMode,username);
        return processGroupServiceImpl.startProcessGroup(isAdmin, username, id, checkpointStr, runMode);
    }

    /**
     * Stop Process Group
     *
     * @param processGroupId
     * @return
     */
    @RequestMapping(value = "/stopProcessGroup", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="stopProcessGroup", notes="stop ProcessGroup")
    public String stopProcessGroup(String processGroupId) {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("stopProcessGroup " + processGroupId , username);
        return processGroupServiceImpl.stopProcessGroup(username, SessionUserUtil.isAdmin(), processGroupId);
    }

    /**
     * Delete Process
     *
     * @param processGroupId
     * @return
     */
    @RequestMapping(value = "/delProcessGroup", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="delProcessGroup", notes="delete ProcessGroup")
    public String delProcessGroup(String processGroupId) {
        logHelperServiceImpl.logAuthSucceed("delProcessGroup " + processGroupId , SessionUserUtil.getCurrentUsername());
        return processGroupServiceImpl.delProcessGroup(SessionUserUtil.getCurrentUsername(), SessionUserUtil.isAdmin(), processGroupId);
    }

    /**
     * Get log data of group
     *
     * @param appId
     * @return
     */
    @RequestMapping(value = "/getGroupLogData", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="getGroupLogData", notes="get Group log data")
    public String getGroupLogData(String appId) {
        return processGroupServiceImpl.getGroupLogData(appId);
    }

    /**
     * Get log data of group
     *
     * @param processGroupId
     * @return
     */
    @RequestMapping(value = "/getStartGroupJson", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="getStartGroupJson", notes="get start Group json")
    public String getStartGroupJson(String processGroupId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return processGroupServiceImpl.getStartGroupJson(username, isAdmin, processGroupId);
    }

    /**
     * Monitoring query appinfo
     *
     * @param appid
     * @return
     */
    @RequestMapping(value = "/getAppInfo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="getAppInfo", notes="get Api info")
    public String getAppInfo(String appid) {
        return processGroupServiceImpl.getAppInfoByAppId(appid);
    }

    /**
     * Monitoring query appinfoList
     *
     * @param arrayObj
     * @return
     */
    @RequestMapping(value = "/getAppInfoList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="getAppInfoList", notes="get Api info list")
    public String getAppInfoList(String[] arrayObj) {
        String appInfoByAppIds = processGroupServiceImpl.getAppInfoByAppIds(arrayObj);
        return appInfoByAppIds;
    }

    @RequestMapping(value = "/getProcessIdByPageId", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="getProcessIdByPageId", notes="get Process id by page id")
    public String getProcessIdByPageId(String processGroupId, String pageId) {
        
        if (StringUtils.isBlank(processGroupId) || StringUtils.isBlank(pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        String processId = processGroupServiceImpl.getProcessIdByPageId(processGroupId, pageId);
        String processGroupIdParents = processGroupServiceImpl.getProcessGroupIdByPageId(processGroupId, pageId);
        String nodeType;
        if (StringUtils.isNotBlank(processId)) {
            nodeType = "flow";
        } else if (StringUtils.isNotBlank(processGroupIdParents)) {
            nodeType = "flowGroup";
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededCustomParam("nodeType", nodeType);
        rtnMap.put("processId", processId);
        rtnMap.put("processGroupId", processGroupIdParents);
        return ReturnMapUtils.toJson(rtnMap);

    }

}
