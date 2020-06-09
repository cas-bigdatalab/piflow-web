package cn.cnic.controller;

import cn.cnic.base.util.*;
import cn.cnic.base.vo.StatefulRtnBase;
import cn.cnic.component.flow.model.Flow;
import cn.cnic.component.flow.service.ICustomizedPropertyService;
import cn.cnic.component.flow.service.IFlowService;
import cn.cnic.component.flow.service.IPropertyService;
import cn.cnic.component.flow.service.IStopsService;
import cn.cnic.component.flow.vo.StopsCustomizedPropertyVo;
import cn.cnic.component.mxGraph.model.MxGraphModel;
import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import cn.cnic.component.stopsComponent.service.IStopGroupService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stops")
public class StopsCtrl {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private IStopGroupService stopGroupServiceImpl;

    @Resource
    private IPropertyService propertyServiceImpl;

    @Resource
    private IStopsService stopsServiceImpl;

    @Resource
    private IFlowService flowServiceImpl;

    @Resource
    private ICustomizedPropertyService customizedPropertyServiceImpl;

    /**
     * 'stops'and'groups' on the left of'reload'
     *
     * @param load
     * @return
     */
    @RequestMapping("/reloadStops")
    @ResponseBody
    public String reloadStops(String load) {
        String username = SessionUserUtil.getCurrentUsername();
        stopGroupServiceImpl.updateGroupAndStopsListByServer(username);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("load", load);
    }

    @RequestMapping("/queryIdInfo")
    @ResponseBody
    public String getStopGroup(String fid, String stopPageId) {
        return propertyServiceImpl.queryAll(fid, stopPageId);
    }

    @RequestMapping("/deleteLastReloadData")
    @ResponseBody
    public String deleteLastReloadData(String stopId) {
        return propertyServiceImpl.deleteLastReloadDataByStopsId(stopId);
    }

    /**
     * Get the usage of the current connection port
     *
     * @param request
     * @return
     */
    @RequestMapping("/getStopsPort")
    @ResponseBody
    public String getStopsPort(HttpServletRequest request) {
        //Take parameters
        //flowId
        String flowId = request.getParameter("flowId");
        //PageId of output's stop
        String sourceId = request.getParameter("sourceId");
        //PageId of input stop
        String targetId = request.getParameter("targetId");
        // ID of path
        String pathLineId = request.getParameter("pathLineId");
        return stopsServiceImpl.getStopsPort(flowId, sourceId, targetId, pathLineId);
    }

    /**
     * Multiple saves to modify
     *
     * @param content
     * @param id
     * @return
     */
    @RequestMapping("/updateStops")
    @ResponseBody
    public String updateStops(HttpServletRequest request, String[] content, String id) {
        String username = SessionUserUtil.getCurrentUsername();
        return propertyServiceImpl.updatePropertyList(username, content);
    }

    @RequestMapping("/updateStopsOne")
    @ResponseBody
    public String updateStops(HttpServletRequest request, String content, String id) {
        String username = SessionUserUtil.getCurrentUsername();
        return propertyServiceImpl.updateProperty(username, content, id);
    }

    @RequestMapping("/updateStopsById")
    @ResponseBody
    public String updateStopsById(HttpServletRequest request) {
        String id = request.getParameter("stopId");
        String isCheckpointStr = request.getParameter("isCheckpoint");
        String username = SessionUserUtil.getCurrentUsername();
        return stopsServiceImpl.updateStopsCheckpoint(username, id, isCheckpointStr);
    }

    @RequestMapping("/updateStopsNameById")
    @ResponseBody
    public String updateStopsNameById(HttpServletRequest request) {
        String id = request.getParameter("stopId");
        String flowId = request.getParameter("flowId");
        String stopName = request.getParameter("name");
        String pageId = request.getParameter("pageId");
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return stopsServiceImpl.updateStopName(username, isAdmin, id, flowId, stopName, pageId);
    }

    @RequestMapping("/addStopCustomizedProperty")
    @ResponseBody
    public String addStopCustomizedProperty(StopsCustomizedPropertyVo stopsCustomizedPropertyVo) {
        String username = SessionUserUtil.getCurrentUsername();
        return customizedPropertyServiceImpl.addStopCustomizedProperty(username, stopsCustomizedPropertyVo);
    }

    @RequestMapping("/updateStopsCustomizedProperty")
    @ResponseBody
    public String updateStopsCustomizedProperty(StopsCustomizedPropertyVo stopsCustomizedPropertyVo) {
        String username = SessionUserUtil.getCurrentUsername();
        return customizedPropertyServiceImpl.updateStopsCustomizedProperty(username, stopsCustomizedPropertyVo);
    }

    @RequestMapping("/deleteStopsCustomizedProperty")
    @ResponseBody
    public String deleteStopsCustomizedProperty(String customPropertyId) {
        String username = SessionUserUtil.getCurrentUsername();
        return customizedPropertyServiceImpl.deleteStopsCustomizedProperty(username, customPropertyId);
    }

    @RequestMapping("/deleteRouterStopsCustomizedProperty")
    @ResponseBody
    public String deleteRouterStopsCustomizedProperty(String customPropertyId) {
        return customizedPropertyServiceImpl.deleteRouterStopsCustomizedProperty(customPropertyId);
    }

    @RequestMapping("/getRouterStopsCustomizedProperty")
    @ResponseBody
    public String getRouterStopsCustomizedProperty(String customPropertyId) {
        return customizedPropertyServiceImpl.getRouterStopsCustomizedProperty(customPropertyId);
    }
}
