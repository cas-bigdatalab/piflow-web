package cn.cnic.controller;

import cn.cnic.base.util.HttpUtils;
import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.base.vo.UserVo;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.component.process.service.IProcessPathService;
import cn.cnic.component.process.service.IProcessService;
import cn.cnic.component.process.service.IProcessStopService;
import cn.cnic.component.process.vo.*;
import cn.cnic.third.service.IFlow;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/process")
public class ProcessCtrl {

    /**
     * Introduce the log, note that all are under the "org.slf4j" package
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    IProcessService processServiceImpl;

    @Resource
    IFlow flowImpl;

    @Resource
    IProcessStopService processStopServiceImpl;

    @Resource
    IProcessPathService processPathServiceImpl;


    /**
     * Query and enter the process list
     *
     * @param request
     * @return
     */
    @RequestMapping("/processListPage")
    @ResponseBody
    public String processListPage(HttpServletRequest request, Integer start, Integer length, Integer draw, String extra_search) {
        return processServiceImpl.getProcessVoListPage(start / length + 1, length, extra_search);
    }

    /**
     * Query based on id and enter process details
     *
     * @param request
     * @param modelAndView
     * @return
     */
    @RequestMapping("/getProcessById")
    public ModelAndView getProcessById(HttpServletRequest request, ModelAndView modelAndView) {
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        modelAndView.addObject("currentUser", currentUser);
        String processId = request.getParameter("processId");
        String parentAccessPath = request.getParameter("parentAccessPath");
        modelAndView.addObject("parentAccessPath", parentAccessPath);
        // Determine whether there is a flow id (load), if it exists, load it, otherwise generate UUID to return to the return page
        if (StringUtils.isNotBlank(processId)) {
            // Query process by load id
            ProcessVo processVo = processServiceImpl.getProcessAllVoById(processId);
            if (null != processVo) {
                String processGroupId = "";
                if (null != processVo.getProcessGroupVo()) {
                    processGroupId = processVo.getProcessGroupVo().getId();
                }
                String svgStr = processVo.getViewXml();
                if (StringUtils.isNotBlank(svgStr)) {
                    modelAndView.addObject("xmlDate", svgStr);
                }
                ProcessState processState = processVo.getState();
                if (null != processState) {
                    modelAndView.addObject("processState", processState.name());
                }
                List<ProcessStopVo> processStopVoList = processVo.getProcessStopVoList();
                if (null != processStopVoList && processStopVoList.size() > 0) {
                    modelAndView.addObject("processStopVoListInit", processStopVoList);
                }
                modelAndView.addObject("percentage", (null != processVo.getProgress() ? processVo.getProgress() : 0.00));
                modelAndView.addObject("appId", processVo.getAppId());
                modelAndView.addObject("processId", processId);
                modelAndView.addObject("parentProcessId", processVo.getParentProcessId());
                modelAndView.addObject("pID", processVo.getProcessId());
                modelAndView.addObject("processVo", processVo);
                modelAndView.addObject("processGroupId", processGroupId);
                modelAndView.setViewName("process/processContent");
                return modelAndView;
            }
        }
        modelAndView.setViewName("errorPage");
        return modelAndView;
    }

    /**
     * Query Process basic information
     *
     * @param request
     * @param modelAndView
     * @return
     */
    @RequestMapping("/queryProcess")
    public ModelAndView queryProcess(HttpServletRequest request, ModelAndView modelAndView) {
        String processId = request.getParameter("processId");
        modelAndView.setViewName("process/inc/process_Info_Inc");
        if (StringUtils.isNotBlank(processId)) {
            // Query process by load id
            // logger.info("Query process by load id");
            ProcessVo processVo = processServiceImpl.getProcessVoById(processId);
            modelAndView.addObject("processVo", processVo);
        } else {
            logger.warn("Parameter passed in incorrectly");
        }
        return modelAndView;
    }

    /**
     * Query ProcessStop basic information
     *
     * @param request
     * @param modelAndView
     * @return
     */
    @RequestMapping("/queryProcessStop")
    public ModelAndView queryProcessStop(HttpServletRequest request, ModelAndView modelAndView) {
        String processId = request.getParameter("processId");
        String pageId = request.getParameter("pageId");
        modelAndView.setViewName("process/inc/process_Property_Inc");
        if (!StringUtils.isAnyEmpty(processId, pageId)) {
            ProcessStopVo processStopVoByPageId = processStopServiceImpl.getProcessStopVoByPageId(processId, pageId);
            modelAndView.addObject("processStopVo", processStopVoByPageId);
        } else {
            logger.warn("Parameter passed in incorrectly");
        }
        return modelAndView;
    }

    /**
     * Query ProcessPath basic information
     *
     * @param request
     * @param modelAndView
     * @return
     */
    @RequestMapping("/queryProcessPath")
    public ModelAndView queryProcessPath(HttpServletRequest request, ModelAndView modelAndView) {
        String processId = request.getParameter("processId");
        String pageId = request.getParameter("pageId");
        modelAndView.setViewName("process/inc/process_Path_Inc");
        if (!StringUtils.isAnyEmpty(processId, pageId)) {
            ProcessPathVo processPathVoByPageId = processPathServiceImpl.getProcessPathVoByPageId(processId, pageId);
            ProcessVo processById = processServiceImpl.getProcessById(processId);
            if (null != processById) {
                modelAndView.addObject("runModeType", processById.getRunModeType());
            }
            modelAndView.addObject("processPathVo", processPathVoByPageId);
        } else {
            logger.info("Parameter passed in incorrectly");
        }
        return modelAndView;
    }

    /**
     * Start Process
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/runProcess")
    @ResponseBody
    public String runProcess(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        String checkpoint = request.getParameter("checkpointStr");
        String runMode = request.getParameter("runMode");
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        return processServiceImpl.startProcess(id, checkpoint, runMode, currentUser);
    }

    /**
     * Stop Process
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/stopProcess")
    @ResponseBody
    public String stopProcess(HttpServletRequest request, Model model) {
        String processId = request.getParameter("processId");
        return processServiceImpl.stopProcess(processId);
    }

    /**
     * Delete Process
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/delProcess")
    @ResponseBody
    public String delProcess(HttpServletRequest request, Model model) {
        String processID = request.getParameter("processID");
        String username = SessionUserUtil.getCurrentUsername();
        return processServiceImpl.delProcess(username, processID);
    }

    /**
     * Get the address of the log of the flow
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/getLogUrl")
    @ResponseBody
    public Map<String, Object> getLogUrl(HttpServletRequest request, Model model) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        String appId = request.getParameter("appId");
        if (StringUtils.isNotBlank(appId)) {
            String amContainerLogs = flowImpl.getFlowLog(appId);
            if (StringUtils.isNotBlank(amContainerLogs)) {
                rtnMap.put("code", 200);
                rtnMap.put("stdoutLog", amContainerLogs + "/stdout/?start=0");
                rtnMap.put("stderrLog", amContainerLogs + "/stderr/?start=0");
            } else {
                rtnMap.put("code", 200);
                rtnMap.put("stdoutLog", "Interface call failed");
                rtnMap.put("stderrLog", "Interface call failed");
            }
        } else {
            logger.warn("appId is null");
        }
        return rtnMap;
    }

    /**
     * Climb to the log by the address of the flow log
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/getLog")
    @ResponseBody
    public String getLog(HttpServletRequest request, Model model) {
        String rtnMsg = "";
        String urlStr = request.getParameter("url");
        if (StringUtils.isNotBlank(urlStr)) {
            if ("Interface call failed".equals(urlStr)) {
                rtnMsg = "Interface call failed";
            } else {
                rtnMsg = HttpUtils.getHtml(urlStr);
            }
        } else {
            logger.warn("urlStr is null");
        }

        return rtnMsg;
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
        String appid = request.getParameter("appid");
        return processServiceImpl.getAppInfoByAppId(appid);
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
        //return processServiceImpl.getProgressByThirdAndSave(arrayObj);
        return processServiceImpl.getProgressByAppIds(arrayObj);
    }


    /**
     * Call the interface to return Checkpoint for the user to choose
     *
     * @param request
     * @param modelAndView
     * @return
     */
    @RequestMapping("/getCheckpoint")
    public ModelAndView getCheckpoint(HttpServletRequest request, ModelAndView modelAndView) {
        String pID = request.getParameter("pID");
        String parentProcessId = request.getParameter("parentProcessId");
        modelAndView.setViewName("process/inc/process_Checkpoint_Inc");
        String checkpoints = "";
        if (StringUtils.isNotBlank(parentProcessId) && !"null".equals(parentProcessId)) {
            checkpoints = flowImpl.getCheckpoints(parentProcessId);
        } else if (StringUtils.isNotBlank(pID)) {
            checkpoints = flowImpl.getCheckpoints(pID);
        }
        if (StringUtils.isNotBlank(checkpoints)) {
            String[] checkpointsSplit = checkpoints.split(",");
            modelAndView.addObject("checkpointsSplit", checkpointsSplit);
        } else {
            logger.warn("No checkpoints found");
        }
        return modelAndView;
    }

    @RequestMapping("/getDebugDataHtml")
    public ModelAndView getDebugDataHtml(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("process/inc/debug_Data_Inc");
        modelAndView.addObject("appId", request.getParameter("appId"));
        modelAndView.addObject("stopName", request.getParameter("stopName"));
        modelAndView.addObject("portName", request.getParameter("portName"));
        return modelAndView;
    }

    @RequestMapping("/getDebugData")
    @ResponseBody
    public String getDebugData(HttpServletRequest request) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        String appId = request.getParameter("appId");
        String stopName = request.getParameter("stopName");
        String portName = request.getParameter("portName");
        String startFileName = request.getParameter("startFileName");
        String startLineStr = request.getParameter("startLine");
        int startLine = 0;
        if (StringUtils.isNotBlank(startLineStr)) {
            startLine = Integer.parseInt(startLineStr);
        }
        if ("Default".equals(portName)) {
            portName = portName.toLowerCase();
        }
        DebugDataResponse debugData = processServiceImpl.getDebugData(new DebugDataRequest(appId, stopName, portName, startFileName, startLine));
        rtnMap.put("code", 200);
        rtnMap.put("debugData", debugData);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Get the `list'running under `flow'
     *
     * @param request
     * @return
     */
    @RequestMapping("/getRunningProcessList")
    public ModelAndView getRunningProcessList(HttpServletRequest request, ModelAndView modelAndView) {
        modelAndView.setViewName("mxGraph/rightPage/runningProcess");
        String flowId = request.getParameter("flowId");
        List<ProcessVo> runningProcessVoList = processServiceImpl.getRunningProcessVoList(flowId);
        modelAndView.addObject("runningProcessVoList", runningProcessVoList);
        return modelAndView;
    }
}
