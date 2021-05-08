package cn.cnic.controller.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cnic.base.util.HttpUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.process.service.IProcessPathService;
import cn.cnic.component.process.service.IProcessService;
import cn.cnic.component.process.service.IProcessStopService;
import cn.cnic.component.process.vo.DebugDataRequest;
import cn.cnic.third.service.IFlow;

@Controller
@RequestMapping("/process")
public class ProcessCtrl {

    /**
     * Introduce the log, note that all are under the "org.slf4j" package
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    IProcessService processServiceImpl;

    @Autowired
    IFlow flowImpl;

    @Autowired
    IProcessStopService processStopServiceImpl;

    @Autowired
    IProcessPathService processPathServiceImpl;


    /**
     * Query and enter the process list
     *
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping("/processListPage")
    @ResponseBody
    public String processAndProcessGroupListPage(Integer page, Integer limit, String param) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return processServiceImpl.getProcessVoListPage(username, isAdmin, page, limit, param);
    }

    /**
     * Enter the front page of the drawing board
     *
     * @param loadId
     * @param parentAccessPath
     * @return
     */
    @RequestMapping("/drawingBoardData")
    @ResponseBody
    public String drawingBoardData(String loadId, String parentAccessPath) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return processServiceImpl.drawingBoardData(currentUsername, isAdmin, loadId, parentAccessPath);
    }

    /**
     * Query Process basic information
     *
     * @param processId
     * @return
     */
    @RequestMapping("/queryProcessData")
    @ResponseBody
    public String queryProcessData(String processId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return processServiceImpl.getProcessVoById(username, isAdmin, processId);
    }

    /**
     * Query ProcessStop basic information
     *
     * @param processId
     * @param pageId
     * @return
     */
    @RequestMapping("/queryProcessStopData")
    @ResponseBody
    public String queryProcessStopData(String processId, String pageId) {
        return processStopServiceImpl.getProcessStopVoByPageId(processId, pageId);
    }

    /**
     * Query ProcessPath basic information
     *
     * @param processId
     * @param pageId
     * @return
     */
    @RequestMapping("/queryProcessPathData")
    @ResponseBody
    public String queryProcessPathData(String processId, String pageId) {
        return processPathServiceImpl.getProcessPathVoByPageId(processId, pageId);
    }

    /**
     * Start Process
     *
     * @param id
     * @param checkpointStr
     * @param runMode
     * @return
     */
    @RequestMapping("/runProcess")
    @ResponseBody
    public String runProcess(String id, String checkpointStr, String runMode) {
        String username = SessionUserUtil.getCurrentUsername();
        return processServiceImpl.startProcess(username, id, checkpointStr, runMode);
    }

    /**
     * Stop Process
     *
     * @param processId
     * @return
     */
    @RequestMapping("/stopProcess")
    @ResponseBody
    public String stopProcess(String processId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return processServiceImpl.stopProcess(username, isAdmin, processId);
    }

    /**
     * Delete Process
     *
     * @param processID
     * @return
     */
    @RequestMapping("/delProcess")
    @ResponseBody
    public String delProcess(String processID) {
        String username = SessionUserUtil.getCurrentUsername();
        return processServiceImpl.delProcess(username, processID);
    }

    /**
     * Get the address of the log of the flow
     *
     * @param appId
     * @return
     */
    @RequestMapping("/getLogUrl")
    @ResponseBody
    public String getLogUrl(String appId) {
        return processServiceImpl.getLogUrl(appId);
    }

    /**
     * Climb to the log by the address of the flow log
     *
     * @param url
     * @return
     */
    @RequestMapping("/getLog")
    @ResponseBody
    public String getLog(String url) {
        String rtnMsg = "";
        if (StringUtils.isNotBlank(url)) {
            if ("Interface call failed".equals(url)) {
                rtnMsg = "Interface call failed";
            } else {
                rtnMsg = HttpUtils.getHtml(url);
            }
        } else {
            logger.warn("urlStr is null");
        }

        return rtnMsg;
    }

    /**
     * Monitoring query appinfo
     *
     * @param appid
     * @return
     */
    @RequestMapping("/getAppInfo")
    @ResponseBody
    public String getAppInfo(String appid) {
        return processServiceImpl.getAppInfoByAppId(appid);
    }

    /**
     * Monitoring query appinfoList
     *
     * @param arrayObj
     * @return
     */
    @RequestMapping("/getAppInfoList")
    @ResponseBody
    public String getAppInfoList(String[] arrayObj) {
        return processServiceImpl.getProgressByAppIds(arrayObj);
    }


    /**
     * Call the interface to return Checkpoint for the user to choose
     *
     * @param pID
     * @param parentProcessId
     * @return
     */
    @RequestMapping("/getCheckpointData")
    @ResponseBody
    public String getCheckpoint(String pID, String parentProcessId) {
        return processServiceImpl.getCheckpoints(parentProcessId, pID);
    }

    @RequestMapping("/getDebugData")
    @ResponseBody
    public String getDebugData(String appId, String stopName, String portName, String startFileName, int startLine) {
        if ("Default".equals(portName)) {
            portName = portName.toLowerCase();
        }
        return processServiceImpl.getDebugData(new DebugDataRequest(appId, stopName, portName, startFileName, startLine));
    }

    @RequestMapping("/getVisualizationData")
    @ResponseBody
    public String getVisualizationData(String appId, String stopName, String visualizationType, boolean isSoft) {
        return processServiceImpl.getVisualizationData(appId, stopName, visualizationType, isSoft);
    }

    /**
     * Get the `list'running under `flow'
     *
     * @param flowId
     * @return
     */
    @RequestMapping("/getRunningProcessListData")
    @ResponseBody
    public String getRunningProcessList(String flowId) {
        return processServiceImpl.getRunningProcessVoList(flowId);
    }
}
