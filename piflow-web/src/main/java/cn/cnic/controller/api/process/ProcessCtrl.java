package cn.cnic.controller.api.process;

import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.process.vo.ProcessVo;
import cn.cnic.component.system.service.ILogHelperService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import cn.cnic.base.utils.HttpUtils;
import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.process.service.IProcessPathService;
import cn.cnic.component.process.service.IProcessService;
import cn.cnic.component.process.service.IProcessStopService;
import cn.cnic.component.process.vo.DebugDataRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import javax.servlet.http.HttpServletResponse;


@Api(value = "process api", tags = "process api")
@Controller
@RequestMapping("/process")
public class ProcessCtrl {


    private final IProcessStopService processStopServiceImpl;
    private final IProcessPathService processPathServiceImpl;
    private final IProcessService processServiceImpl;
    private final ILogHelperService logHelperServiceImpl;

    @Autowired
    public ProcessCtrl(IProcessStopService processStopServiceImpl,
                       IProcessPathService processPathServiceImpl,
                       IProcessService processServiceImpl,
                       ILogHelperService logHelperServiceImpl) {
        this.processStopServiceImpl = processStopServiceImpl;
        this.processPathServiceImpl = processPathServiceImpl;
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
    @RequestMapping(value = "/processListPage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "processListPage", notes = "Process list page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "page", required = true, paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "limit", required = true, paramType = "query"),
            @ApiImplicitParam(name = "param", value = "param", required = false, paramType = "query")
    })
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
    @RequestMapping(value = "/drawingBoardData", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "drawingBoardData", notes = "drawingBoard data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loadId", value = "loadId", required = true, paramType = "query"),
            @ApiImplicitParam(name = "parentAccessPath", value = "parentAccessPath", required = false, paramType = "query")
    })
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
    @RequestMapping(value = "/queryProcessData", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "queryProcessData", notes = "query Process data")
    @ApiImplicitParam(name = "processId", value = "processId", required = true, paramType = "query")
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
    @RequestMapping(value = "/queryProcessStopData", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "queryProcessStopData", notes = "query ProcessStop data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "processId", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageId", value = "pageId", required = true, paramType = "query")
    })
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
    @RequestMapping(value = "/queryProcessPathData", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "queryProcessPathData", notes = "query ProcessPath data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processId", value = "processId", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageId", value = "pageId", required = true, paramType = "query")
    })
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
     * @throws Exception
     */
    @RequestMapping(value = "/runProcess", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "runProcess", notes = "Run Process")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "checkpointStr", value = "checkpointStr", required = false, paramType = "query"),
            @ApiImplicitParam(name = "runMode", value = "runMode", required = false, paramType = "query")
    })
    public String runProcess(String id, String checkpointStr, String runMode) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelperServiceImpl.logAuthSucceed("runProcess" + id, username);
        return processServiceImpl.startProcess(isAdmin, username, id, checkpointStr, runMode);
    }

    /**
     * Stop Process
     *
     * @param processId
     * @return
     */
    @RequestMapping(value = "/stopProcess", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "stopProcess", notes = "stop Process")
    @ApiImplicitParam(name = "processId", value = "processId", required = true, paramType = "query")
    public String stopProcess(String processId) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelperServiceImpl.logAuthSucceed("stopProcess" + processId, username);
        return processServiceImpl.stopProcess(username, isAdmin, processId);
    }

    /**
     * Delete Process
     *
     * @param processID
     * @return
     */
    @RequestMapping(value = "/delProcess", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "delProcess", notes = "delete Process")
    @ApiImplicitParam(name = "processID", value = "processID", required = true, paramType = "query")
    public String delProcess(String processID) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelperServiceImpl.logAuthSucceed("delProcess" + processID, username);
        return processServiceImpl.delProcess(isAdmin, username, processID);
    }

    /**
     * Get the address of the log of the flow
     *
     * @param appId
     * @return
     */
    @RequestMapping(value = "/getLogUrl", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "getLogUrl", notes = "get log data")
    @ApiImplicitParam(name = "appId", value = "appId", required = true, paramType = "query")
    public String getLogUrl(String appId) {
        return processServiceImpl.getLogUrl(appId);
    }

    /**
     * Climb to the log by the address of the flow log
     *
     * @param url
     * @return
     */
    @RequestMapping(value = "/getLog", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "getLog", notes = "get log data")
    @ApiImplicitParam(name = "url", value = "url", required = true, paramType = "query")
    public String getLog(String url) {
        if (StringUtils.isBlank(url)) {
            return "urlStr is null";
        }
        if (MessageConfig.INTERFACE_CALL_ERROR_MSG().equals(url)) {
            return MessageConfig.INTERFACE_CALL_ERROR_MSG();
        }
        return HttpUtils.getHtml(url);
    }

    /**
     * Monitoring query appinfo
     *
     * @param appid
     * @return
     */
    @RequestMapping(value = "/getAppInfo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "getAppInfo", notes = "get app info")
    @ApiImplicitParam(name = "appid", value = "appid", required = true, paramType = "query")
    public String getAppInfo(String appid) {
        return processServiceImpl.getAppInfoByAppId(appid);
    }

    /**
     * Monitoring query appinfoList
     *
     * @param arrayObj
     * @return
     */
    @RequestMapping(value = "/getAppInfoList", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "getAppInfoList", notes = "get app info list")
    @ApiImplicitParam(name = "arrayObj", value = "arrayObj", required = true, paramType = "query", allowMultiple = true)
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
    @RequestMapping(value = "/getCheckpointData", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "getCheckpointData", notes = "get checkpoint data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pID", value = "pID", required = true, paramType = "query"),
            @ApiImplicitParam(name = "parentProcessId", value = "parentProcessId", required = true, paramType = "query")
    })
    public String getCheckpoint(String pID, String parentProcessId) {
        return processServiceImpl.getCheckpoints(parentProcessId, pID);
    }

    @RequestMapping(value = "/getDebugData", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "getDebugData", notes = "get debug data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "appId", required = true, paramType = "query"),
            @ApiImplicitParam(name = "stopName", value = "stopName", required = true, paramType = "query"),
            @ApiImplicitParam(name = "portName", value = "portName", required = true, paramType = "query"),
            @ApiImplicitParam(name = "startFileName", value = "startFileName", required = false, paramType = "query"),
            @ApiImplicitParam(name = "startLine", value = "startLine", example = "0", dataType = "long", required = true, paramType = "query")
    })
    public String getDebugData(String appId, String stopName, String portName, String startFileName, int startLine) {
        if ("Default".equals(portName)) {
            portName = portName.toLowerCase();
        }
        return processServiceImpl.getDebugData(new DebugDataRequest(appId, stopName, portName, startFileName, startLine));
    }

    @RequestMapping(value = "/getVisualizationData", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "getVisualizationData", notes = "get visualization data")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "appId", required = true, paramType = "query"),
            @ApiImplicitParam(name = "stopName", value = "stopName", required = true, paramType = "query"),
            @ApiImplicitParam(name = "visualizationType", value = "visualizationType", required = true, paramType = "query"),
            @ApiImplicitParam(name = "isSoft", value = "isSoft", required = true, example = "false", paramType = "query")
    })
    public String getVisualizationData(String appId, String stopName, String visualizationType, boolean isSoft) {
        return processServiceImpl.getVisualizationData(appId, stopName, visualizationType, isSoft);
    }

    /**
     * Get the `list'running under `flow'
     *
     * @param flowId
     * @return
     */
    @RequestMapping(value = "/getRunningProcessList", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "getRunningProcessList", notes = "get running Process list")
    @ApiImplicitParam(name = "flowId", value = "flowId", required = true, paramType = "query")
    public String getRunningProcessList(String flowId) {
        return processServiceImpl.getRunningProcessVoList(flowId);
    }

    /**
     * show View Data
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/showViewStopData/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "showViewStopData", notes = "show view Stop data")
    public void showViewData(HttpServletResponse response, @PathVariable String id) throws Exception {
        processStopServiceImpl.showViewData(response, id);
    }

    /**
     * @param appId:
     * @return String
     * @author tianyao
     * @description 获取报错信息提示
     * @date 2024/2/21 17:45
     */
    @RequestMapping(value = "/getErrorLogInfo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "getErrorLogInfo", notes = "获取报错信息提示")
    public String getErrorLogInfo(String appId) {
        return processServiceImpl.getErrorLogInfo(appId);
    }

    /**
     * @param processVo:
     * @return String
     * @author tianyao
     * @description 根据发布流水线Id分页获取运行历史列表
     * @date 2024/2/21 17:49
     */
    @RequestMapping(value = "/getProcessPageByPublishingId", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "getProcessPageByPublishingId", notes = "根据发布流水线Id分页获取运行历史列表")
    public String getProcessPageByPublishingId(@RequestBody ProcessVo processVo) {
        return processServiceImpl.getProcessPageByPublishingId(processVo);
    }

    /**
     * @param processVo:
     * @return String
     * @author tianyao
     * @description 获取自己的运行历史, 运行发布流水线的记录
     * @date 2024/2/29 9:40
     */
    @RequestMapping(value = "/getProcessHistoryPageOfSelf", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "getProcessHistoryPageOfSelf", notes = "获取自己的运行历史")
    public String getProcessHistoryPageOfSelf(@RequestBody ProcessVo processVo) {
        return processServiceImpl.getProcessHistoryPageOfSelf(processVo);
    }

    /**
     * @param processId:
     * @return String
     * @author tianyao
     * @description 根据processId获取进程详情，发布流水线运行的进程
     * @date 2024/3/1 10:56
     */
    @RequestMapping(value = "/getByProcessId", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "getByProcessId", notes = "获取自己的运行历史")
    public String getByProcessId(String processId) throws Exception {
        return processServiceImpl.getByProcessId(processId);
    }

    @RequestMapping(value = "/getAppIdByProcessId", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "getAppIdByProcessId", notes = "获取自己的运行历史")
    public String getAppIdByProcessId(String processId) throws Exception {
        return processServiceImpl.getAppIdByProcessId(processId);
    }


}
