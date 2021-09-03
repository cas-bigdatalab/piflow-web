package cn.cnic.controller.api.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cnic.base.utils.JsonUtils;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.process.service.IProcessGroupService;
import cn.cnic.component.process.service.IProcessService;
import io.swagger.annotations.Api;

@Api(value = "processGroup api")
@Controller
@RequestMapping("/processGroup")
public class ProcessGroupCtrl {

    @Autowired
    private IProcessGroupService processGroupServiceImpl;

    @Autowired
    private IProcessService processServiceImpl;


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
    public String processGroupListPage(Integer page, Integer limit, String param) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return processGroupServiceImpl.getProcessGroupVoListPage(username, isAdmin, page, limit, param);
    }

    /**
     * Query and enter the process list
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/processListPage", method=RequestMethod.POST)
    @ResponseBody
    public String processListPage(Integer start, Integer length, Integer draw, String extra_search) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return processServiceImpl.getProcessGroupVoListPage(username, isAdmin, start / length + 1, length, extra_search);
    }

    /**
     * Enter the front page of the drawing board
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/drawingBoardData", method = RequestMethod.GET)
    @ResponseBody
    public String drawingBoardData(String loadId, String parentAccessPath) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return processGroupServiceImpl.drawingBoardData(currentUsername, isAdmin, loadId, parentAccessPath);
    }

    /**
     * Query Process basic information
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryProcessGroup", method = RequestMethod.POST)
    @ResponseBody
    public String queryProcessGroup(String processGroupId) {
        
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return processGroupServiceImpl.getProcessGroupVoById(username, isAdmin, processGroupId);
    }

    /**
     * Query ProcessStop basic information
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryProcess", method = RequestMethod.POST)
    @ResponseBody
    public String queryProcess(String processGroupId, String pageId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return processGroupServiceImpl.getProcessGroupNode(username, isAdmin, processGroupId, pageId);
    }

    /**
     * Query ProcessPath basic information
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryProcessGroupPath", method = RequestMethod.POST)
    @ResponseBody
    public String queryProcessGroupPath(String processGroupId, String pageId) {
        return processGroupServiceImpl.getProcessGroupPathVoByPageId(processGroupId, pageId);
    }

    /**
     * Start Process
     *
     * @param request
     * @param model
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/runProcessGroup", method = RequestMethod.POST)
    @ResponseBody
    public String runProcessGroup(String id, String checkpointStr, String runMode) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return processGroupServiceImpl.startProcessGroup(isAdmin, username, id, checkpointStr, runMode);
    }

    /**
     * Stop Process Group
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/stopProcessGroup", method = RequestMethod.POST)
    @ResponseBody
    public String stopProcessGroup(String processGroupId) {
        return processGroupServiceImpl.stopProcessGroup(SessionUserUtil.getCurrentUsername(), SessionUserUtil.isAdmin(), processGroupId);
    }

    /**
     * Delete Process
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/delProcessGroup", method = RequestMethod.POST)
    @ResponseBody
    public String delProcessGroup(String processGroupId) {
        return processGroupServiceImpl.delProcessGroup(SessionUserUtil.getCurrentUsername(), SessionUserUtil.isAdmin(), processGroupId);
    }

    /**
     * Get log data of group
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getGroupLogData", method = RequestMethod.POST)
    @ResponseBody
    public String getGroupLogData(String appId) {
        return processGroupServiceImpl.getGroupLogData(appId);
    }

    /**
     * Get log data of group
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getStartGroupJson", method = RequestMethod.POST)
    @ResponseBody
    public String getStartGroupJson(String processGroupId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return processGroupServiceImpl.getStartGroupJson(username, isAdmin, processGroupId);
    }

    /**
     * Monitoring query appinfo
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getAppInfo", method = RequestMethod.GET)
    @ResponseBody
    public String getAppInfo(String appid) {
        return processGroupServiceImpl.getAppInfoByAppId(appid);
    }

    /**
     * Monitoring query appinfoList
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getAppInfoList", method = RequestMethod.GET)
    @ResponseBody
    public String getAppInfoList(String[] arrayObj) {
        String appInfoByAppIds = processGroupServiceImpl.getAppInfoByAppIds(arrayObj);
        return appInfoByAppIds;
    }

    @RequestMapping(value = "/getProcessIdByPageId", method = RequestMethod.POST)
    @ResponseBody
    public String getProcessIdByPageId(String processGroupId, String pageId) {
        
        if (StringUtils.isBlank(processGroupId) || StringUtils.isBlank(pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("processGroupID is null");
        }
        String processId = processGroupServiceImpl.getProcessIdByPageId(processGroupId, pageId);
        String processGroupIdParents = processGroupServiceImpl.getProcessGroupIdByPageId(processGroupId, pageId);
        Map<String, Object> rtnMap = new HashMap<>();
        String nodeType = "flow";
        if (StringUtils.isNotBlank(processId)) {
            rtnMap.put("nodeType", nodeType);
        } else if (StringUtils.isNotBlank(processGroupIdParents)) {
            rtnMap.put("nodeType", "flowGroup");
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No query found");
        }
        rtnMap.put("processId", processId);
        rtnMap.put("processGroupId", processGroupIdParents);
        rtnMap.put("code", 200);
        return JsonUtils.toJsonNoException(rtnMap);

    }

}
