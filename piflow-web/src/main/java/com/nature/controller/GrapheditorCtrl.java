//package com.nature.controller;
//
//import com.nature.base.util.FlowXmlUtils;
//import com.nature.base.util.JsonUtils;
//import com.nature.base.util.LoggerUtil;
//import com.nature.base.util.SessionUserUtil;
//import com.nature.base.vo.StatefulRtnBase;
//import com.nature.base.vo.UserVo;
//import com.nature.component.flow.model.Flow;
//import com.nature.component.flow.model.FlowGroup;
//import com.nature.component.flow.request.UpdatePathRequest;
//import com.nature.component.flow.service.IFlowGroupService;
//import com.nature.component.flow.service.IFlowService;
//import com.nature.component.flow.service.IPropertyService;
//import com.nature.component.flow.service.IStopsService;
//import com.nature.component.flow.vo.StopGroupVo;
//import com.nature.component.group.service.IStopGroupService;
//import com.nature.component.mxGraph.model.MxGraphModel;
//import com.nature.component.mxGraph.vo.MxGraphModelVo;
//import com.nature.component.process.service.IProcessService;
//import com.nature.component.process.vo.ProcessVo;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * grapheditorCtrl
// */
//@Controller
//@RequestMapping("/grapheditor")
//public class GrapheditorCtrl {
//
//    /**
//     * Introducing logs, note that they are all packaged under "org.slf4j"
//     */
//    Logger logger = LoggerUtil.getLogger();
//
//    @Autowired
//    private IFlowService flowServiceImpl;
//
//    @Autowired
//    private IFlowGroupService flowGroupServiceImpl;
//
//    @Autowired
//    private IStopGroupService stopGroupServiceImpl;
//
//    @Autowired
//    private IStopsService stopsServiceImpl;
//
//    @Autowired
//    private IPropertyService propertyServiceImpl;
//
//    @Autowired
//    private IProcessService processServiceImpl;
//
//
//    /**
//     * Enter the front page of the drawing board
//     *
//     * @param model
//     * @param load
//     * @return
//     */
//    @RequestMapping("/home")
//    public String kitchenSink(HttpServletRequest request, Model model, String load) {
//        String parentAccessPath = request.getParameter("parentAccessPath");
//        model.addAttribute("parentAccessPath", parentAccessPath);
//        UserVo user = SessionUserUtil.getCurrentUser();
//        model.addAttribute("currentUser", user);
//        // Determine whether there is an'id'('load') of'Flow', and if there is, load it, otherwise generate'UUID' to return to the return page.
//        if (StringUtils.isNotBlank(load)) {
//            // Query by loading'id'
//            Flow flowById = flowServiceImpl.getFlowById(load);
//            if (null != flowById) {
//                String flowGroupId = "";
//                if (null != flowById.getFlowGroup()) {
//                    flowGroupId = flowById.getFlowGroup().getId();
//                }
//                // Group on the left and'stops'
//                List<StopGroupVo> groupsVoList = stopGroupServiceImpl.getStopGroupAll();
//                model.addAttribute("groupsVoList", groupsVoList);
//                String maxStopPageId = flowServiceImpl.getMaxStopPageId(load);
//                //'maxStopPageId'defaults to 2 if it's empty, otherwise'maxStopPageId'+1
//                maxStopPageId = StringUtils.isBlank(maxStopPageId) ? "2" : (Integer.parseInt(maxStopPageId) + 1) + "";
//                model.addAttribute("maxStopPageId", maxStopPageId);
//                MxGraphModelVo mxGraphModelVo = null;
//                MxGraphModel mxGraphModel = flowById.getMxGraphModel();
//                mxGraphModelVo = FlowXmlUtils.mxGraphModelPoToVo(mxGraphModel);
//                // Change the query'mxGraphModelVo'to'XML'
//                String loadXml = FlowXmlUtils.mxGraphModelToXml(mxGraphModelVo);
//                model.addAttribute("xmlDate", loadXml);
//                model.addAttribute("load", load);
//                model.addAttribute("flowGroupId", flowGroupId);
//                model.addAttribute("isExample", (null == flowById.getIsExample() ? false : flowById.getIsExample()));
//                return "grapheditor/index";
//            } else {
//                return "errorPage";
//            }
//        } else {
//            return "errorPage";
//        }
//    }
//
//    /**
//     * save data
//     *
//     * @param request
//     * @param model
//     * @return
//     */
//    @RequestMapping("/saveData")
//    @ResponseBody
//    public String saveData(HttpServletRequest request, Model model) {
//        Map<String, Object> rtnMap = new HashMap<>();
//        rtnMap.put("code", 500);
//        String imageXML = request.getParameter("imageXML");
//        String loadId = request.getParameter("load");
//        String operType = request.getParameter("operType");
//        if (StringUtils.isAnyEmpty(imageXML, loadId, operType)) {
//            rtnMap.put("errorMsg", "The incoming parameters are empty");
//            logger.info("The incoming parameters are empty");
//            return JsonUtils.toJsonNoException(rtnMap);
//        } else {
//            // Change the `XML'from the page to `mxGraphModel'
//            MxGraphModelVo xmlToMxGraphModel = FlowXmlUtils.xmlToMxGraphModel(imageXML);
//            StatefulRtnBase addFlow = flowServiceImpl.saveOrUpdateFlowAll(xmlToMxGraphModel, loadId, operType, true);
//            // addFlow is not empty and the value of ReqRtnStatus is true, then the save is successful.
//            if (null != addFlow && addFlow.isReqRtnStatus()) {
//                rtnMap.put("code", 200);
//                rtnMap.put("errorMsg", "Successful Preservation");
//                logger.info("Successful Preservation");
//            } else {
//                rtnMap.put("errorMsg", "Save failed");
//                logger.info("Save failed");
//            }
//            return JsonUtils.toJsonNoException(rtnMap);
//        }
//
//    }
//
//    /**
//     * 'stops'and'groups' on the left of'reload'
//     *
//     * @param load
//     * @return
//     */
//    @RequestMapping("/reloadStops")
//    @ResponseBody
//    public String reloadStops(String load) {
//        UserVo user = SessionUserUtil.getCurrentUser();
//        Map<String, Object> rtnMap = new HashMap<>();
//        rtnMap.put("code", 500);
//        stopGroupServiceImpl.addGroupAndStopsList(user);
//        rtnMap.put("code", 200);
//        rtnMap.put("load", load);
//        return JsonUtils.toJsonNoException(rtnMap);
//    }
//
//    /**
//     * Get the usage of the current connection port
//     *
//     * @param request
//     * @param model
//     * @return
//     */
//    @RequestMapping("/getStopsPort")
//    @ResponseBody
//    public String getStopsPort(HttpServletRequest request, Model model) {
//        //Take parameters
//        //flowId
//        String flowId = request.getParameter("flowId");
//        //PageId of output's stop
//        String sourceId = request.getParameter("sourceId");
//        //PageId of input stop
//        String targetId = request.getParameter("targetId");
//        // ID of path
//        String pathLineId = request.getParameter("pathLineId");
//        return stopsServiceImpl.getStopsPort(flowId, sourceId, targetId, pathLineId);
//    }
//
//    /**
//     * Save user-selected ports
//     *
//     * @param updatePathRequest
//     * @return
//     */
//    @RequestMapping("/savePathsPort")
//    @ResponseBody
//    public String savePathsPort(UpdatePathRequest updatePathRequest) {
//        return propertyServiceImpl.saveOrUpdateRoutePath(updatePathRequest);
//    }
//
//    /**
//     * Get the `list'running under `flow'
//     *
//     * @param request
//     * @return
//     */
//    @RequestMapping("/getRunningProcessList")
//    public ModelAndView getRunningProcessList(HttpServletRequest request, ModelAndView modelAndView) {
//        modelAndView.setViewName("grapheditor/rightPage/runningProcess");
//        String flowId = request.getParameter("flowId");
//        List<ProcessVo> runningProcessVoList = processServiceImpl.getRunningProcessVoList(flowId);
//        modelAndView.addObject("runningProcessVoList", runningProcessVoList);
//        return modelAndView;
//    }
//
//    /**
//     * Enter the front page of the drawing board
//     *
//     * @param model
//     * @param load
//     * @return
//     */
//    @RequestMapping("/groupDrawingBoard")
//    public String GroupDrawingBoard(Model model, String load) {
//        UserVo user = SessionUserUtil.getCurrentUser();
//        model.addAttribute("currentUser", user);
//        // Query by loading'id'
//        FlowGroup flowGroupById = flowGroupServiceImpl.getFlowGroupById(load);
//        if (null != flowGroupById) {
//            String maxStopPageId = flowServiceImpl.getMaxFlowPageIdByFlowGroupId(load);
//            //'maxStopPageId'defaults to 2 if it's empty, otherwise'maxStopPageId'+1
//            maxStopPageId = StringUtils.isBlank(maxStopPageId) ? "2" : (Integer.parseInt(maxStopPageId) + 1) + "";
//            model.addAttribute("maxStopPageId", maxStopPageId);
//            MxGraphModelVo mxGraphModelVo = null;
//            MxGraphModel mxGraphModel = flowGroupById.getMxGraphModel();
//            mxGraphModelVo = FlowXmlUtils.mxGraphModelPoToVo(mxGraphModel);
//            // Change the query'mxGraphModelVo'to'XML'
//            String loadXml = FlowXmlUtils.mxGraphModelToXml(mxGraphModelVo);
//            model.addAttribute("xmlDate", loadXml);
//            model.addAttribute("load", load);
//            model.addAttribute("isExample", (null == flowGroupById.getIsExample() ? false : flowGroupById.getIsExample()));
//            return "graphEditorGroup/index";
//        } else {
//            return "errorPage";
//        }
//    }
//
//}
