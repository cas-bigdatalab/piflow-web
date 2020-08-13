package cn.cnic.controller.modify.user;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.controller.modify.utils.UserUtils;
import cn.cnic.component.flow.service.ICustomizedPropertyService;
import cn.cnic.component.flow.service.IPropertyService;
import cn.cnic.component.flow.service.IStopsService;
import cn.cnic.component.flow.vo.StopsCustomizedPropertyVo;
import cn.cnic.component.stopsComponent.service.IStopGroupService;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user/stops")
public class UserStopsCtrl {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private IStopGroupService stopGroupServiceImpl;

    @Resource
    private IPropertyService propertyServiceImpl;

    @Resource
    private IStopsService stopsServiceImpl;

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
    public String reloadStops(HttpServletRequest request, String load) {
        String username = UserUtils.getUsername(request);
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
        String username = UserUtils.getUsername(request);
        return propertyServiceImpl.updatePropertyList(username, content);
    }

    @RequestMapping("/updateStopsOne")
    @ResponseBody
    public String updateStops(HttpServletRequest request, String content, String id) {
        String username = UserUtils.getUsername(request);
        return propertyServiceImpl.updateProperty(username, content, id);
    }

    @RequestMapping("/updateStopsById")
    @ResponseBody
    public String updateStopsById(HttpServletRequest request) {
        String id = request.getParameter("stopId");
        String isCheckpointStr = request.getParameter("isCheckpoint");
        String username = UserUtils.getUsername(request);
        return stopsServiceImpl.updateStopsCheckpoint(username, id, isCheckpointStr);
    }

    @RequestMapping("/updateStopsNameById")
    @ResponseBody
    public String updateStopsNameById(HttpServletRequest request) {
        String id = request.getParameter("stopId");
        String flowId = request.getParameter("flowId");
        String stopName = request.getParameter("name");
        String pageId = request.getParameter("pageId");
        String username = UserUtils.getUsername(request);
        return stopsServiceImpl.updateStopName(username, false, id, flowId, stopName, pageId);
    }

    @RequestMapping("/addStopCustomizedProperty")
    @ResponseBody
    public String addStopCustomizedProperty(HttpServletRequest request, StopsCustomizedPropertyVo stopsCustomizedPropertyVo) {
        String username = UserUtils.getUsername(request);
        return customizedPropertyServiceImpl.addStopCustomizedProperty(username, stopsCustomizedPropertyVo);
    }

    @RequestMapping("/updateStopsCustomizedProperty")
    @ResponseBody
    public String updateStopsCustomizedProperty(HttpServletRequest request, StopsCustomizedPropertyVo stopsCustomizedPropertyVo) {
        String username = UserUtils.getUsername(request);
        return customizedPropertyServiceImpl.updateStopsCustomizedProperty(username, stopsCustomizedPropertyVo);
    }

    @RequestMapping("/deleteStopsCustomizedProperty")
    @ResponseBody
    public String deleteStopsCustomizedProperty(HttpServletRequest request, String customPropertyId) {
        String username = UserUtils.getUsername(request);
        return customizedPropertyServiceImpl.deleteStopsCustomizedProperty(username, customPropertyId);
    }

    @RequestMapping("/deleteRouterStopsCustomizedProperty")
    @ResponseBody
    public String deleteRouterStopsCustomizedProperty(HttpServletRequest request, String customPropertyId) {
        String username = UserUtils.getUsername(request);
        return customizedPropertyServiceImpl.deleteRouterStopsCustomizedProperty(username, customPropertyId);
    }

    @RequestMapping("/getRouterStopsCustomizedProperty")
    @ResponseBody
    public String getRouterStopsCustomizedProperty(String customPropertyId) {
        return customizedPropertyServiceImpl.getRouterStopsCustomizedProperty(customPropertyId);
    }
}
