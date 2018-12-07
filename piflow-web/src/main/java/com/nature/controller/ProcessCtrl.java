package com.nature.controller;

import com.nature.base.util.HttpUtils;
import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.common.Eunm.ProcessState;
import com.nature.component.process.model.Process;
import com.nature.component.process.service.IProcessService;
import com.nature.component.process.service.IProcessStopService;
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
     * 引入日志，注意都是"org.slf4j"包下
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
    IGetFlowProgress getFlowProgressImpl;

    @RequestMapping("/getProcessList")
    public ModelAndView getProcessList(HttpServletRequest request, ModelAndView modelAndView) {
        List<ProcessVo> processVoList = processServiceImpl.getProcessVoList();
        modelAndView.setViewName("process/process");
        modelAndView.addObject("processVoList", processVoList);
        return modelAndView;
    }

    @RequestMapping("/getProcessById")
    public ModelAndView getProcessById(HttpServletRequest request, ModelAndView modelAndView) {
        String processId = request.getParameter("processId");
        // 判断是否存在Flow的id(load),如果存在則加载，否則生成UUID返回返回页面
        if (StringUtils.isNotBlank(processId)) {
            // 根据加载id进行查询
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

    @RequestMapping("/queryProcess")
    public ModelAndView queryProcess(HttpServletRequest request, ModelAndView modelAndView) {
        String processId = request.getParameter("processId");
        modelAndView.setViewName("process/inc/process_Info_Inc");
        if (StringUtils.isNotBlank(processId)) {
            // 根据加载id进行查询
            // logger.info("根据加载id进行查询");
            ProcessVo processVo = processServiceImpl.getProcessVoById(processId);
            modelAndView.addObject("processVo", processVo);
        } else {
            logger.info("参数传入不正确");
        }
        return modelAndView;
    }

    @RequestMapping("/queryProcessStop")
    public ModelAndView queryProcessStop(HttpServletRequest request, ModelAndView modelAndView) {
        String processId = request.getParameter("processId");
        String pageId = request.getParameter("pageId");
        modelAndView.setViewName("process/inc/process_Property_Inc");
        if (!StringUtils.isAnyEmpty(processId, pageId)) {
            ProcessStopVo processStopVoByPageId = processStopServiceImpl.getProcessStopVoByPageId(processId, pageId);
            modelAndView.addObject("processStopVo", processStopVoByPageId);
        } else {
            logger.info("参数传入不正确");
        }
        return modelAndView;
    }

    /**
     * 启动Process
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
        String errMsg = "";
        String id = request.getParameter("id");
        String checkpoint = request.getParameter("checkpointStr");
        if (StringUtils.isNotBlank(id)) {
            // 根据processId查询process并copy新建
            Process process = processServiceImpl.processCopyProcessAndAdd(id);
            if (null != process) {
                StatefulRtnBase statefulRtnBase = startFlowImpl.startProcess(process, checkpoint);
                if (null != statefulRtnBase && statefulRtnBase.isReqRtnStatus()) {
                    // 启动成功后直接调一次info
                    processServiceImpl.getAppInfoByThirdAndSave(process.getAppId());
                    errMsg = "启动成功";
                    rtnMap.put("code", "1");
                    rtnMap.put("processId", process.getId());
                    rtnMap.put("errMsg", errMsg);
                } else {
                    processServiceImpl.updateProcessEnableFlag(process.getId());
                    errMsg = "调用接口失败，启动失败";
                    rtnMap.put("errMsg", errMsg);
                    logger.warn(errMsg);
                }
            } else {
                errMsg = "未查询到id为" + id + "的process";
                rtnMap.put("errMsg", errMsg);
                logger.warn(errMsg);
            }
        } else {
            errMsg = "processId为空";
            rtnMap.put("errMsg", errMsg);
            logger.warn(errMsg);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * 停止Process
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
        String appId = request.getParameter("appId");
        if (StringUtils.isNotBlank(appId)) {
            // 根据processId查询process
            ProcessVo processVo = processServiceImpl.getProcessVoByAppId(appId);
            // 判空，看是否保存成功
            if (null != processVo) {
                String processVoId = processVo.getId();
                if (null != processVoId) {
                    String flowStop = stopFlowImpl.stopFlow(appId);
                    if (StringUtils.isNotBlank(flowStop) && !flowStop.contains("Exception")) {
                        rtnMap.put("code", "1");
                        rtnMap.put("errMsg", "停止成功，返回状态为：" + flowStop);
                    } else {
                        logger.warn("接口返回值为空");
                        rtnMap.put("errMsg", "接口返回值为空");
                    }
                } else {
                    logger.warn("查询到的process的Id为空");
                    rtnMap.put("errMsg", "查询到的process的Id为空");
                }
            } else {
                logger.warn("未查询到appId为" + appId + "的process");
                rtnMap.put("errMsg", "未查询到appId为" + appId + "的process");
            }
        } else {
            logger.warn("processId或appId为空");
            rtnMap.put("errMsg", "appId为空");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * 删除Process
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
            // 根据processId查询process
            boolean isSuccess = processServiceImpl.updateProcessEnableFlag(processID);
            // 判空是否删除成功
            if (isSuccess) {
                rtnMap.put("code", "1");
                rtnMap.put("errMsg", "删除成功");
            }
        } else {
            logger.warn("processID为空");
            rtnMap.put("errMsg", "processID为空");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * 获取flow的Log的地址
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
            logger.warn("appId为空");
        }

        return rtnMap;
    }

    /**
     * 通过flow的地址爬到log
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
            logger.warn("urlStr为空");
        }

        return rtnMsg;
    }

    /**
     * 监控查询appinfo
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
            map.put("errMsg", "appID为空");
        }
        return JsonUtils.toJsonNoException(map);
    }

    /**
     * 监控查询appinfoList
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
     * 通过端口返回Checkpoint供用户选择
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
            logger.warn("没有查询到checkpoints");
        }
        return modelAndView;
    }

}
