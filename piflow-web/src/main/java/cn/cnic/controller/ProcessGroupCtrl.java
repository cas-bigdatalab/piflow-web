package cn.cnic.controller;

import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.process.service.IProcessGroupService;
import cn.cnic.component.process.service.IProcessService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/processGroup")
public class ProcessGroupCtrl {

    /**
     * Introduce the log, note that all are under the "org.slf4j" package
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private IProcessGroupService processGroupServiceImpl;

    @Resource
    private IProcessService processServiceImpl;


    /**
     * Query and enter the process list
     *
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping("/processGroupListPage")
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
    @RequestMapping("/processListPage")
    @ResponseBody
    public String processListPage(HttpServletRequest request, Integer start, Integer length, Integer draw, String extra_search) {
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
    @RequestMapping("/drawingBoardData")
    @ResponseBody
    public String drawingBoardData(HttpServletRequest request) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        String loadId = request.getParameter("loadId");
        String parentAccessPath = request.getParameter("parentAccessPath");
        return processGroupServiceImpl.drawingBoardData(currentUsername, isAdmin, loadId, parentAccessPath);
    }

    /**
     * Query Process basic information
     *
     * @param request
     * @return
     */
    @RequestMapping("/queryProcessGroup")
    @ResponseBody
    public String queryProcessGroup(HttpServletRequest request) {
        String processGroupId = request.getParameter("processGroupId");
        return processGroupServiceImpl.getProcessGroupVoById(processGroupId);
    }

    /**
     * Query ProcessStop basic information
     *
     * @param request
     * @return
     */
    @RequestMapping("/queryProcess")
    @ResponseBody
    public String queryProcess(HttpServletRequest request) {
        String processGroupId = request.getParameter("processGroupId");
        String pageId = request.getParameter("pageId");
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
    @RequestMapping("/queryProcessGroupPath")
    @ResponseBody
    public String queryProcessGroupPath(HttpServletRequest request) {
        String processGroupId = request.getParameter("processGroupId");
        String pageId = request.getParameter("pageId");
        return processGroupServiceImpl.getProcessGroupPathVoByPageId(processGroupId, pageId);
    }

    /**
     * Start Process
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/runProcessGroup")
    @ResponseBody
    public String runProcessGroup(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        String checkpoint = request.getParameter("checkpointStr");
        String runMode = request.getParameter("runMode");
        String username = SessionUserUtil.getCurrentUsername();
        return processGroupServiceImpl.startProcessGroup(username, id, checkpoint, runMode);
    }

    /**
     * Stop Process Group
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/stopProcessGroup")
    @ResponseBody
    public String stopProcessGroup(HttpServletRequest request, Model model) {
        String processGroupId = request.getParameter("processGroupId");
        return processGroupServiceImpl.stopProcessGroup(SessionUserUtil.getCurrentUsername(), SessionUserUtil.isAdmin(), processGroupId);
    }

    /**
     * Delete Process
     *
     * @param request
     * @return
     */
    @RequestMapping("/delProcessGroup")
    @ResponseBody
    public String delProcessGroup(HttpServletRequest request) {
        String processGroupID = request.getParameter("processGroupId");
        return processGroupServiceImpl.delProcessGroup(SessionUserUtil.getCurrentUsername(), SessionUserUtil.isAdmin(), processGroupID);
    }

    /**
     * Get log data of group
     *
     * @param request
     * @return
     */
    @RequestMapping("/getGroupLogData")
    @ResponseBody
    public String getGroupLogData(HttpServletRequest request) {
        String appId = request.getParameter("appId");
        return processGroupServiceImpl.getGroupLogData(appId);
    }

    /**
     * Get log data of group
     *
     * @param request
     * @return
     */
    @RequestMapping("/getStartGroupJson")
    @ResponseBody
    public String getStartGroupJson(HttpServletRequest request) {
        String processGroupId = request.getParameter("processGroupId");
        return processGroupServiceImpl.getStartGroupJson(processGroupId);
    }

    /**
     * Monitoring query appinfo
     *
     * @param request
     * @return
     */
    @RequestMapping("/getAppInfo")
    @ResponseBody
    public String getAppInfo(HttpServletRequest request) {
        String appId = request.getParameter("appid");
        return processGroupServiceImpl.getAppInfoByAppId(appId);
    }

    /**
     * Monitoring query appinfoList
     *
     * @param request
     * @return
     */
    @RequestMapping("/getAppInfoList")
    @ResponseBody
    public String getAppInfoList(HttpServletRequest request) {
        String[] arrayObj = request.getParameterValues("arrayObj");
        String appInfoByAppIds = processGroupServiceImpl.getAppInfoByAppIds(arrayObj);
        return appInfoByAppIds;
    }

    @RequestMapping("/getProcessIdByPageId")
    @ResponseBody
    public String getProcessIdByPageId(HttpServletRequest request) {
        String fId = request.getParameter("processGroupId");
        String pageId = request.getParameter("pageId");
        if (StringUtils.isBlank(fId) || StringUtils.isBlank(pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("processGroupID is null");
        }
        String processId = processGroupServiceImpl.getProcessIdByPageId(fId, pageId);
        String processGroupId = processGroupServiceImpl.getProcessGroupIdByPageId(fId, pageId);
        Map<String, Object> rtnMap = new HashMap<>();
        String nodeType = "flow";
        if (StringUtils.isNotBlank(processId)) {
            rtnMap.put("nodeType", nodeType);
        } else if (StringUtils.isNotBlank(processGroupId)) {
            rtnMap.put("nodeType", "flowGroup");
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No query found");
        }
        rtnMap.put("processId", processId);
        rtnMap.put("processGroupId", processGroupId);
        rtnMap.put("code", 200);
        return JsonUtils.toJsonNoException(rtnMap);

    }

}
