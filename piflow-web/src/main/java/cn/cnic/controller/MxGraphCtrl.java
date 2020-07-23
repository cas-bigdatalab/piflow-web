package cn.cnic.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.cnic.base.util.FlowXmlUtils;
import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.MxGraphUtils;
import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.base.vo.UserVo;
import cn.cnic.common.Eunm.DrawingBoardType;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.component.flow.model.Flow;
import cn.cnic.component.flow.model.FlowGroup;
import cn.cnic.component.flow.service.IFlowGroupService;
import cn.cnic.component.flow.service.IFlowService;
import cn.cnic.component.mxGraph.model.MxGraphModel;
import cn.cnic.component.mxGraph.service.IMxGraphModelService;
import cn.cnic.component.mxGraph.service.IMxNodeImageService;
import cn.cnic.component.mxGraph.utils.MxCellUtils;
import cn.cnic.component.mxGraph.vo.MxCellVo;
import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import cn.cnic.component.mxGraph.vo.MxGraphVo;
import cn.cnic.component.process.service.IProcessGroupService;
import cn.cnic.component.process.service.IProcessService;
import cn.cnic.component.process.vo.ProcessGroupVo;
import cn.cnic.component.process.vo.ProcessStopVo;
import cn.cnic.component.process.vo.ProcessVo;
import cn.cnic.component.stopsComponent.service.IStopGroupService;
import cn.cnic.component.stopsComponent.vo.StopGroupVo;

/**
 * grapheditorctrl
 */
@Controller
@RequestMapping("/mxGraph")
public class MxGraphCtrl {
    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    Logger logger = LoggerUtil.getLogger();

    @Resource
    private IFlowService flowServiceImpl;

    @Resource
    private IFlowGroupService flowGroupServiceImpl;

    @Resource
    private IStopGroupService stopGroupServiceImpl;

    @Resource
    private IMxGraphModelService mxGraphModelServiceImpl;

    @Resource
    private IMxNodeImageService mxNodeImageServiceImpl;

    @Resource
    private IProcessService processServiceImpl;

    @Resource
    private IProcessGroupService processGroupServiceImpl;


    /**
     * Enter the front page of the drawing board
     *
     * @param request
     * @param model
     * @param drawingBoardType
     * @return
     */
    @RequestMapping("/drawingBoard")
    public String drawingBoard(HttpServletRequest request, Model model, DrawingBoardType drawingBoardType, String processType) {
        String load = request.getParameter("load");
        //set parentAccessPath
        String parentAccessPath = request.getParameter("parentAccessPath");
        model.addAttribute("parentAccessPath", parentAccessPath);
        // set current user
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        //set drawingBoardType
        model.addAttribute("drawingBoardType", drawingBoardType);

        String pagePath = "errorPage";
        // Determine whether there is an'id'('load') of'Flow', and if there is, load it, otherwise generate'UUID' to return to the return page.
        if (StringUtils.isBlank(load)) {
            return "errorPage";
        }
        switch (drawingBoardType) {
            case PROCESS: {
                Model processHandleModel = processHandle(model, load, processType);
                if (null != processHandleModel) {
                    model = processHandleModel;
                    pagePath = "mxGraph/index";
                }
                break;
            }
            default:
                break;
        }
        return pagePath;
    }

    private Model processHandle(Model model, String load, String processType) {
        if (StringUtils.isBlank(load)) {
            return null;
        }
        if (null == model) {
            return null;
        }
        ProcessGroupVo parentsProcessGroupVo = null;
        MxGraphModelVo mxGraphModelVo = null;
        List<Map<String, String>> nodePageIdAndStates = new ArrayList<>();
        ProcessState processState = ProcessState.INIT;
        if ("PROCESS".equals(processType)) {
            ProcessVo processVo = processServiceImpl.getProcessById(SessionUserUtil.getCurrentUsername(), SessionUserUtil.isAdmin(), load);
            if (null == processVo) {
                return null;
            }
            // processStopVoList
            List<ProcessStopVo> processStopVoList = processVo.getProcessStopVoList();
            if (null != processStopVoList && processStopVoList.size() > 0) {
                Map<String, String> stopNode;
                for (ProcessStopVo processStopVo_i : processStopVoList) {
                    if (null == processStopVo_i) {
                        continue;
                    }
                    stopNode = new HashMap<>();
                    stopNode.put("pageId", processStopVo_i.getPageId());
                    stopNode.put("state", processStopVo_i.getState());
                    nodePageIdAndStates.add(stopNode);
                }
                model.addAttribute("processStopVoListInit", processStopVoList);
                model.addAttribute("percentage", (null != processVo.getProgress() ? processVo.getProgress() : 0.00));
                model.addAttribute("appId", processVo.getAppId());
            }
            parentsProcessGroupVo = processVo.getProcessGroupVo();
            if (null != parentsProcessGroupVo) {
                model.addAttribute("processGroupId", parentsProcessGroupVo.getId());
            }
            mxGraphModelVo = processVo.getMxGraphModelVo();
            processState = processVo.getState();
            model.addAttribute("processType", "TASK");
            model.addAttribute("processId", load);
            model.addAttribute("parentProcessId", processVo.getParentProcessId());
            model.addAttribute("pID", processVo.getProcessId());
            model.addAttribute("processVo", processVo);
        } else {
            ProcessGroupVo processGroupVo = processGroupServiceImpl.getProcessGroupVoAllById(load);
            if (null == processGroupVo) {
                return null;
            }
            parentsProcessGroupVo = processGroupVo.getProcessGroupVo();
            mxGraphModelVo = processGroupVo.getMxGraphModelVo();
            model.addAttribute("processType", "GROUP");

            // processGroupVoList
            List<ProcessGroupVo> processGroupVoList = processGroupVo.getProcessGroupVoList();
            if (null != processGroupVoList && processGroupVoList.size() > 0) {
                Map<String, String> processGroupNode;
                for (ProcessGroupVo processGroupVo_i : processGroupVoList) {
                    if (null == processGroupVo_i) {
                        continue;
                    }
                    processGroupNode = new HashMap<>();
                    processGroupNode.put("pageId", processGroupVo_i.getPageId());
                    processGroupNode.put("state", (null != processGroupVo_i.getState()) ? processGroupVo_i.getState().getText() : ProcessState.INIT.name());
                    nodePageIdAndStates.add(processGroupNode);
                }
                model.addAttribute("processGroupVoListInit", processGroupVoList);
            }
            // processVoList
            List<ProcessVo> processVoList = processGroupVo.getProcessVoList();
            Map<String, String> processNode;
            if (null != processVoList && processVoList.size() > 0) {
                for (ProcessVo process_i : processVoList) {
                    if (null == process_i) {
                        continue;
                    }
                    String process_i_stateStr = (null != process_i.getState() ? process_i.getState().getText() : "INIT");
                    processNode = new HashMap<>();
                    processNode.put("pageId", process_i.getPageId());
                    processNode.put("state", process_i_stateStr);
                    nodePageIdAndStates.add(processNode);
                }
                model.addAttribute("processVoListInit", processVoList);
            }
            processState = processGroupVo.getState();
            model.addAttribute("parentProcessId", processGroupVo.getParentProcessId());
            model.addAttribute("percentage", (null != processGroupVo.getProgress() ? processGroupVo.getProgress() : 0.00));
            model.addAttribute("appId", processGroupVo.getAppId());
            model.addAttribute("pID", processGroupVo.getProcessId());
            model.addAttribute("processGroupVo", processGroupVo);
            model.addAttribute("processGroupId", load);
        }
        if (null != processState) {
            model.addAttribute("processState", processState.name());
        }
        if (null != parentsProcessGroupVo) {
            model.addAttribute("parentsId", parentsProcessGroupVo.getId());
        }
        if (null != mxGraphModelVo) {
            List<MxCellVo> rootVo = mxGraphModelVo.getRootVo();
            if (null != rootVo && rootVo.size() > 0) {
                List<MxCellVo> iconTranslate = new ArrayList<>();
                MxCellVo iconMxCellVo;
                for (MxCellVo mxCellVo : rootVo) {
                    if (null == mxCellVo) {
                        continue;
                    }
                    String style = mxCellVo.getStyle();
                    if (StringUtils.isBlank(style) || style.indexOf("image;") != 0) {
                        continue;
                    }
                    if (null == mxCellVo.getMxGeometryVo()) {
                        continue;
                    }
                    iconMxCellVo = MxCellUtils.initIconMxCellVo(mxCellVo);
                    if (null == iconMxCellVo) {
                        continue;
                    }
                    iconTranslate.add(iconMxCellVo);
                }
                rootVo.addAll(iconTranslate);
            }
            mxGraphModelVo.setRootVo(rootVo);
        }
        String loadXml = MxGraphUtils.mxGraphModelToMxGraphXml(mxGraphModelVo);
        model.addAttribute("xmlDate", loadXml);
        model.addAttribute("load", load);
        model.addAttribute("nodeArr", nodePageIdAndStates);
        return model;
    }

    /**
     * save data
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/saveDataForTask")
    @ResponseBody
    public String saveDataForTask(HttpServletRequest request, Model model) {
        String imageXML = request.getParameter("imageXML");
        String loadId = request.getParameter("load");
        String operType = request.getParameter("operType");
        String username = SessionUserUtil.getCurrentUsername();
        return mxGraphModelServiceImpl.saveDataForTask(username, imageXML, loadId, operType);
    }

    /**
     * save data
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/saveDataForGroup")
    @ResponseBody
    public String saveDataForGroup(HttpServletRequest request, Model model) {
        String imageXML = request.getParameter("imageXML");
        String loadId = request.getParameter("load");
        String operType = request.getParameter("operType");
        String username = SessionUserUtil.getCurrentUsername();
        return mxGraphModelServiceImpl.saveDataForGroup(username, imageXML, loadId, operType, true);
    }

    @RequestMapping("/addMxCellAndData")
    @ResponseBody
    public String addMxCellAndData(@RequestBody MxGraphVo mxGraphVo) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        return mxGraphModelServiceImpl.addMxCellAndData(mxGraphVo, currentUsername);
    }

    @RequestMapping("/uploadNodeImage")
    @ResponseBody
    public String uploadNodeImage(@RequestParam("file") MultipartFile file, String imageType) {
        String username = SessionUserUtil.getCurrentUsername();
        return mxNodeImageServiceImpl.uploadNodeImage(username, file, imageType);
    }

    @RequestMapping("/nodeImageList")
    @ResponseBody
    public String nodeImageList(String imageType) {
        String username = SessionUserUtil.getCurrentUsername();
        return mxNodeImageServiceImpl.getMxNodeImageList(username, imageType);
    }

    @RequestMapping("/groupRightRun")
    @ResponseBody
    public String groupRightRun(String pId, String nodeId, String nodeType) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return flowGroupServiceImpl.rightRun(username, isAdmin, pId, nodeId, nodeType);
    }

    @RequestMapping("/eraseRecord")
    @ResponseBody
    public String eraseRecord() {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("code", 200);
        rtnMap.put("flag", true);
        return JsonUtils.toJsonNoException(rtnMap);
    }

}
