package com.nature.controller;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.ProcessState;
import com.nature.component.process.service.IProcessGroupService;
import com.nature.component.process.service.IProcessService;
import com.nature.component.process.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/processGroup")
public class ProcessGroupCtrl {

    /**
     * Introduce the log, note that all are under the "org.slf4j" package
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private IProcessGroupService processGroupServiceImpl;

    @Autowired
    private IProcessService processServiceImpl;


    /**
     * Query and enter the process list
     *
     * @param request
     * @return
     */
    @RequestMapping("/processGroupListPage")
    @ResponseBody
    public String processGroupListPage(HttpServletRequest request, Integer start, Integer length, Integer draw, String extra_search) {
        return processGroupServiceImpl.getProcessGroupVoListPage(start / length + 1, length, extra_search);
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
        return processServiceImpl.getProcessGroupVoListPage(start / length + 1, length, extra_search);
    }

    /**
     * Query based on id and enter process details
     *
     * @param request
     * @param modelAndView
     * @return
     */
    @RequestMapping("/getProcessGroupById")
    public ModelAndView getProcessById(HttpServletRequest request, ModelAndView modelAndView) {
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        modelAndView.addObject("currentUser", currentUser);
        String processGroupId = request.getParameter("processGroupId");
        String parentAccessPath = request.getParameter("parentAccessPath");
        modelAndView.addObject("parentAccessPath", parentAccessPath);
        // Determine whether there is a flow id (load), if it exists, load it, otherwise generate UUID to return to the return page
        if (StringUtils.isNotBlank(processGroupId)) {
            // Query process by load id
            ProcessGroupVo processGroupVo = processGroupServiceImpl.getProcessAllVoById(processGroupId);
            if (null != processGroupVo) {
                String svgStr = processGroupVo.getViewXml();
                if (StringUtils.isNotBlank(svgStr)) {
                    modelAndView.addObject("xmlDate", svgStr);
                }
                ProcessState processState = processGroupVo.getState();
                if (null != processState) {
                    modelAndView.addObject("processState", processState.name());
                }
                List<ProcessVo> processVoList = processGroupVo.getProcessVoList();
                if (null != processVoList && processVoList.size() > 0) {
                    modelAndView.addObject("processVoListInit", processVoList);
                }
                modelAndView.addObject("percentage", (null != processGroupVo.getProgress() ? processGroupVo.getProgress() : 0.00));
                modelAndView.addObject("appId", processGroupVo.getAppId());
                modelAndView.addObject("processGroupId", processGroupId);
                modelAndView.addObject("parentProcessId", processGroupVo.getParentProcessId());
                modelAndView.addObject("pID", processGroupVo.getProcessId());
                modelAndView.addObject("processGroupVo", processGroupVo);
                modelAndView.setViewName("processGroup/processGroupContent");
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
    @RequestMapping("/queryProcessGroup")
    public ModelAndView queryProcessGroup(HttpServletRequest request, ModelAndView modelAndView) {
        String processGroupId = request.getParameter("processGroupId");
        modelAndView.setViewName("processGroup/inc/process_group_info_inc");
        if (StringUtils.isNotBlank(processGroupId)) {
            // Query process by load id
            // logger.info("Query process by load id");
            ProcessGroupVo processGroupVo = processGroupServiceImpl.getProcessGroupVoById(processGroupId);
            modelAndView.addObject("processGroupVo", processGroupVo);
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
    @RequestMapping("/queryProcess")
    public ModelAndView queryProcess(HttpServletRequest request, ModelAndView modelAndView) {
        String processGroupId = request.getParameter("processGroupId");
        String pageId = request.getParameter("pageId");
        modelAndView.setViewName("processGroup/inc/process_property_inc");
        if (!StringUtils.isAnyEmpty(processGroupId, pageId)) {
            ProcessVo processVoByPageId = processServiceImpl.getProcessVoByPageId(processGroupId, pageId);
            modelAndView.addObject("processVo", processVoByPageId);
        } else {
            logger.warn("Parameter passed in incorrectly");
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
    @RequestMapping("/runProcessGroup")
    @ResponseBody
    public String runProcessGroup(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        String checkpoint = request.getParameter("checkpointStr");
        String runMode = request.getParameter("runMode");
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        return processGroupServiceImpl.startProcessGroup(id, checkpoint, runMode, currentUser);
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
        return processGroupServiceImpl.stopProcessGroup(processGroupId);
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
        return processGroupServiceImpl.delProcessGroup(processGroupID);
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
        String processGroupId = request.getParameter("processGroupId");
        String pageId = request.getParameter("pageId");
        return processGroupServiceImpl.getProcessIdByPageId(processGroupId, pageId);
    }

}
