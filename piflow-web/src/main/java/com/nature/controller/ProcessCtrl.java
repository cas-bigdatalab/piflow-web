package com.nature.controller;

import com.nature.base.util.HttpUtils;
import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.StatefulRtnBaseUtils;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.common.Eunm.ProcessState;
import com.nature.component.process.model.Process;
import com.nature.component.process.service.IProcessPathService;
import com.nature.component.process.service.IProcessService;
import com.nature.component.process.service.IProcessStopService;
import com.nature.component.process.vo.ProcessPathVo;
import com.nature.component.process.vo.ProcessStopVo;
import com.nature.component.process.vo.ProcessVo;
import com.nature.third.inf.*;
import com.nature.third.vo.flowLog.ThirdAppVo;
import com.nature.third.vo.flowLog.ThirdFlowLog;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/process/*")
public class ProcessCtrl {

    /**
     * Introduce the log, note that all are under the "org.slf4j" package
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    IProcessService processServiceImpl;

    @Autowired
    IStartFlow startFlowImpl;

    @Autowired
    IGetFlowInfo getFlowInfoImpl;

    @Autowired
    IStopFlow stopFlowImpl;

    @Autowired
    IGetFlowLog getFlowLogImpl;

    @Autowired
    IFlowCheckpoints flowCheckpointsImpl;

    @Autowired
    IProcessStopService processStopServiceImpl;

    @Autowired
    IProcessPathService processPathServiceImpl;

    @Autowired
    IGetFlowProgress getFlowProgressImpl;

    /**
     * Query and enter the process list
     *
     * @param request
     * @param modelAndView
     * @return
     */
    @RequestMapping("/getProcessList")
    public ModelAndView getProcessList(HttpServletRequest request, ModelAndView modelAndView) {
        modelAndView.setViewName("/indexNew");
        modelAndView.addObject("accessPath", "getProcessList");
        // modelAndView.setViewName("process/process");
        List<ProcessVo> processVoList = processServiceImpl.getProcessVoList();
        modelAndView.addObject("processVoList", processVoList);
        return modelAndView;
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
        String processId = request.getParameter("processId");
        String parentAccessPath = request.getParameter("parentAccessPath");
        modelAndView.addObject("parentAccessPath", parentAccessPath);
        // Determine whether there is a flow id (load), if it exists, load it, otherwise generate UUID to return to the return page
        if (StringUtils.isNotBlank(processId)) {
            // Query process by load id
            ProcessVo processVo = processServiceImpl.getProcessAllVoById(processId);
            if (null != processVo) {
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
                modelAndView.setViewName("process/processContent");
            } else {
                modelAndView.setViewName("errorPage");
            }
        } else {
            modelAndView.setViewName("errorPage");
        }
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
            logger.info("Parameter passed in incorrectly");
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
            logger.info("Parameter passed in incorrectly");
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
        Map<String, String> rtnMap = new HashMap<String, String>();
        rtnMap.put("code", "0");
        String id = request.getParameter("id");
        String checkpoint = request.getParameter("checkpointStr");
        if (StringUtils.isNotBlank(id)) {
            // Query Process by 'ProcessId' and copy new
            Process process = processServiceImpl.processCopyProcessAndAdd(id);
            if (null != process) {
                StatefulRtnBase statefulRtnBase = startFlowImpl.startProcess(process, checkpoint);
                if (null != statefulRtnBase && statefulRtnBase.isReqRtnStatus()) {
                    // Call the 'AppInfo' interface once the startup is successful
                    processServiceImpl.getAppInfoByThirdAndSave(process.getAppId());
                    rtnMap.put("code", "1");
                    rtnMap.put("processId", process.getId());
                    rtnMap.put("errMsg", "Successful startup");
                } else {
                    processServiceImpl.updateProcessEnableFlag(process.getId());
                    rtnMap.put("errMsg", "Calling interface failed, startup failed");
                    logger.warn("Calling interface failed, startup failed");
                }
            } else {
                rtnMap.put("errMsg", "No process Id'" + id + "'");
                logger.warn("No process Id'" + id + "'");
            }
        } else {
            rtnMap.put("errMsg", "processId is null");
            logger.warn("processId is null");
        }
        return JsonUtils.toJsonNoException(rtnMap);
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
        Map<String, String> rtnMap = new HashMap<String, String>();
        rtnMap.put("code", "0");
        String processId = request.getParameter("processId");
        if (StringUtils.isNotBlank(processId)) {
            // Query Process by 'ProcessId'
            ProcessVo processVo = processServiceImpl.getProcessVoById(processId);
            // Determine whether it is empty, and determine whether the save is successful.
            if (null != processVo) {
                String appId = processVo.getAppId();
                if (null != appId) {
                    if (ProcessState.STARTED == processVo.getState()) {
                        String flowStop = stopFlowImpl.stopFlow(appId);
                        if (StringUtils.isNotBlank(flowStop) && !flowStop.contains("Exception")) {
                            rtnMap.put("code", "1");
                            rtnMap.put("errMsg", "Stop successful, return status is " + flowStop);
                        } else {
                            logger.warn("Interface return value is null.");
                            rtnMap.put("errMsg", "Interface return value is null.");
                        }
                    } else {
                        logger.warn("The status of the process is " + processVo.getState() + " and cannot be stopped.");
                        rtnMap.put("errMsg", "The status of the process is " + processVo.getState() + " and cannot be stopped.");
                    }
                } else {
                    logger.warn("The 'appId' of the 'process' is empty.");
                    rtnMap.put("errMsg", "The 'appId' of the 'process' is empty.");
                }
            } else {
                logger.warn("No process ID is '" + processId + "' process");
                rtnMap.put("errMsg", " No process ID is '" + processId + "' process");
            }
        } else {
            logger.warn("processId is null");
            rtnMap.put("errMsg", "processId is null");
        }
        return JsonUtils.toJsonNoException(rtnMap);
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
        Map<String, String> rtnMap = new HashMap<String, String>();
        rtnMap.put("code", "0");
        String processID = request.getParameter("processID");
        if (StringUtils.isNotBlank(processID)) {
            // Query Process by 'ProcessId'
            Process processById = processServiceImpl.getProcessById(processID);
            if (null != processById) {
                if (processById.getState() != ProcessState.STARTED) {
                    StatefulRtnBase isSuccess = processServiceImpl.updateProcessEnableFlag(processID);
                    // Determine whether the deletion is successful
                    if (null != isSuccess) {
                        if (isSuccess.isReqRtnStatus()) {
                            rtnMap.put("code", "1");
                            rtnMap.put("errMsg", "Successfully Deleted");
                        } else {
                            logger.warn(isSuccess.getErrorMsg());
                            rtnMap.put("errMsg", isSuccess.getErrorMsg());
                        }
                    } else {
                        logger.warn("Failed to delete");
                        rtnMap.put("errMsg", "Failed to delete");
                    }
                } else {
                    logger.warn("Status is STARTED, cannot be deleted");
                    rtnMap.put("errMsg", "Status is STARTED, cannot be deleted");
                }
            } else {
                logger.warn("No process ID is '" + processID + "' process");
                rtnMap.put("errMsg", "No process ID is '" + processID + "' process");
            }
        } else {
            logger.warn("processID is null");
            rtnMap.put("errMsg", "processID is null");
        }
        return JsonUtils.toJsonNoException(rtnMap);
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
    public Map<String, String> getLogUrl(HttpServletRequest request, Model model) {
        Map<String, String> rtnMap = new HashMap<>();
        rtnMap.put("code", "0");
        String appId = request.getParameter("appId");
        if (StringUtils.isNotBlank(appId)) {
            ThirdFlowLog flowlog = getFlowLogImpl.getFlowLog(appId);
            if (null != flowlog) {
                ThirdAppVo app = flowlog.getApp();
                if (null != app) {
                    rtnMap.put("code", "1");
                    rtnMap.put("stdoutLog", app.getAmContainerLogs() + "/stdout/?start=0");
                    rtnMap.put("stderrLog", app.getAmContainerLogs() + "/stderr/?start=0");
                }
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
            rtnMsg = HttpUtils.getHtml(urlStr);
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
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", "0");
        String appid = request.getParameter("appid");
        if (StringUtils.isNotBlank(appid)) {
            // 查询appinfo
            ProcessVo processVoThird = processServiceImpl.getAppInfoByThirdAndSave(appid);
            if (null != processVoThird) {
                map.put("code", "1");
                map.put("progress", (null != processVoThird.getProgress() ? processVoThird.getProgress() : "0.00"));
                map.put("state", (null != processVoThird.getState() ? processVoThird.getState().name() : "NO_STATE"));
                map.put("processVo", processVoThird);
            }
        } else {
            map.put("errMsg", "appID is null");
        }
        return JsonUtils.toJsonNoException(map);
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
        List<String> list = new ArrayList<String>();
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", "0");
        if (null != arrayObj && arrayObj.length > 0) {
            List<ProcessVo> progressByThirdAndSave = processServiceImpl.getProgressByThirdAndSave(arrayObj);
            if (null != progressByThirdAndSave && progressByThirdAndSave.size() > 0) {
                rtnMap.put("code", "1");
                for (ProcessVo processVo : progressByThirdAndSave) {
                    if (null != processVo) {
                        rtnMap.put(processVo.getAppId(), processVo);
                    }
                }
            }
        }
        return JsonUtils.toJsonNoException(rtnMap);
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
            checkpoints = flowCheckpointsImpl.getCheckpoints(parentProcessId);
        } else if (StringUtils.isNotBlank(pID)) {
            checkpoints = flowCheckpointsImpl.getCheckpoints(pID);
        }
        if (StringUtils.isNotBlank(checkpoints)) {
            String[] checkpointsSplit = checkpoints.split(",");
            modelAndView.addObject("checkpointsSplit", checkpointsSplit);
        } else {
            logger.warn("No checkpoints found");
        }
        return modelAndView;
    }

}
