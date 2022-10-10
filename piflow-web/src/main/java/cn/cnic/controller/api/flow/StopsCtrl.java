package cn.cnic.controller.api.flow;

import cn.cnic.component.flow.service.IFlowStopsPublishingService;
import cn.cnic.component.system.service.ILogHelperService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.flow.service.IPropertyService;
import cn.cnic.component.flow.service.IStopsService;
import cn.cnic.component.flow.vo.StopsCustomizedPropertyVo;
import cn.cnic.component.stopsComponent.service.IStopGroupService;
import cn.cnic.component.stopsComponent.service.IStopsHubService;
import io.swagger.annotations.Api;

import java.util.List;

@Api(value = "stops api", tags = "stops api")
@RestController
@RequestMapping("/stops")
public class StopsCtrl {

    private final IStopGroupService stopGroupServiceImpl;
    private final ILogHelperService logHelperServiceImpl;
    private final IPropertyService propertyServiceImpl;
    private final IStopsHubService stopsHubServiceImpl;
    private final IStopsService stopsServiceImpl;
    private final IFlowStopsPublishingService flowStopsPublishingServiceImpl;

    @Autowired
    public StopsCtrl(IStopGroupService stopGroupServiceImpl,
                     ILogHelperService logHelperServiceImpl,
                     IPropertyService propertyServiceImpl,
                     IStopsHubService stopsHubServiceImpl,
                     IStopsService stopsServiceImpl,
                     IFlowStopsPublishingService flowStopsPublishingServiceImpl) {
        this.stopGroupServiceImpl = stopGroupServiceImpl;
        this.logHelperServiceImpl = logHelperServiceImpl;
        this.propertyServiceImpl = propertyServiceImpl;
        this.stopsHubServiceImpl = stopsHubServiceImpl;
        this.stopsServiceImpl = stopsServiceImpl;
        this.flowStopsPublishingServiceImpl = flowStopsPublishingServiceImpl;
    }

    /**
     * 'stops'and'groups' on the left of'reload'
     *
     * @param load
     * @return
     */
    @RequestMapping(value = "/reloadStops", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="reloadStops", notes="reload Stops")
    public String reloadStops(String load) {
        String username = SessionUserUtil.getCurrentUsername();
        stopGroupServiceImpl.updateGroupAndStopsListByServer(username);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("load", load);
    }

    @RequestMapping(value = "/queryIdInfo", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="queryIdInfo", notes="query id info")
    public String getStopGroup(String fid, String stopPageId) {
        return propertyServiceImpl.queryAll(fid, stopPageId);
    }

    @RequestMapping(value = "/deleteLastReloadData", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="deleteLastReloadData", notes="delete last reload data")
    public String deleteLastReloadData(String stopId) {
        return propertyServiceImpl.deleteLastReloadDataByStopsId(stopId);
    }

    /**
     * Get the usage of the current connection port
     *
     * @param flowId
     * @param sourceId
     * @param targetId
     * @param pathLineId
     * @return
     */
    @RequestMapping(value = "/getStopsPort", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="getStopsPort", notes="get Stops port")
    public String getStopsPort(String flowId, String sourceId, String targetId, String pathLineId) {
        return stopsServiceImpl.getStopsPort(flowId, sourceId, targetId, pathLineId);
    }

    /**
     * Multiple saves to modify
     *
     * @param content
     * @param id
     * @return
     */
    @RequestMapping(value = "/updateStops", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="updateStops", notes="update Stops")
    public String updateStops(String[] content, String id) {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("updateStops " + id,username);
        return propertyServiceImpl.updatePropertyList(username, content);
    }

    @RequestMapping(value = "/updateStopsOne", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="updateStopsOne", notes="update Stops one")
    public String updateStops(String content, String id) {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("updateStopOne " + id, username);
        return propertyServiceImpl.updateProperty(username, content, id);
    }

    @RequestMapping(value = "/updateStopsById", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="updateStopsById", notes="update Stops by id")
    public String updateStopsById(String stopId, String isCheckpoint) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("updateStopsById" + stopId, username);
        return stopsServiceImpl.updateStopsCheckpoint(username, stopId, isCheckpoint);
    }

    @RequestMapping(value = "/updateStopsNameById", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="updateStopsNameById", notes="update Stops name by id")
    public String updateStopsNameById(String stopId, String flowId, String name, String pageId) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelperServiceImpl.logAuthSucceed("updateStopsNameById" + name, username);
        return stopsServiceImpl.updateStopName(username, isAdmin, stopId, flowId, name, pageId);
    }

    @RequestMapping(value = "/addStopCustomizedProperty", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="addStopCustomizedProperty", notes="add StopsCustomizedProperty")
    public String addStopCustomizedProperty(StopsCustomizedPropertyVo stopsCustomizedPropertyVo) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        return stopsServiceImpl.addStopCustomizedProperty(username, stopsCustomizedPropertyVo);
    }

    @RequestMapping(value = "/updateStopsCustomizedProperty", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="updateStopsCustomizedProperty", notes="update StopsCustomizedProperty")
    public String updateStopsCustomizedProperty(StopsCustomizedPropertyVo stopsCustomizedPropertyVo) {
        String username = SessionUserUtil.getCurrentUsername();
        return stopsServiceImpl.updateStopsCustomizedProperty(username, stopsCustomizedPropertyVo);
    }

    @RequestMapping(value = "/deleteStopsCustomizedProperty", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="deleteStopsCustomizedProperty", notes="delete StopsCustomizedProperty")
    public String deleteStopsCustomizedProperty(String customPropertyId) {
        String username = SessionUserUtil.getCurrentUsername();
        return stopsServiceImpl.deleteStopsCustomizedProperty(username, customPropertyId);
    }

    @RequestMapping(value = "/deleteRouterStopsCustomizedProperty", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="deleteRouterStopsCustomizedProperty", notes="delete RouterStopsCustomizedProperty")
    public String deleteRouterStopsCustomizedProperty(String customPropertyId) {
        String username = SessionUserUtil.getCurrentUsername();
        return stopsServiceImpl.deleteRouterStopsCustomizedProperty(username, customPropertyId);
    }

    @RequestMapping(value = "/getRouterStopsCustomizedProperty", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="getRouterStopsCustomizedProperty", notes="get RouterStopsCustomizedProperty")
    public String getRouterStopsCustomizedProperty(String customPropertyId) {
        return stopsServiceImpl.getRouterStopsCustomizedProperty(customPropertyId);
    }

    /**
     * Query and enter the process list
     *
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping(value = "/stopsHubListPage", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="stopsHubListPage", notes="stopsHub list page")
    public String stopsHubListPage(Integer page, Integer limit, String param) {
        String username = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return stopsHubServiceImpl.stopsHubListPage(username, isAdmin, page, limit, param);
    }


    /**
     * Upload stopsHub jar file and save stopsHub
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/uploadStopsHubFile", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="uploadStopsHubFile", notes="upload StopsHub file")
    public String uploadStopsHubFile(@RequestParam("file") MultipartFile file) {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("uploadStopsHubFile " + file.getName(),username);
        return stopsHubServiceImpl.uploadStopsHubFile(username, file);
    }

    /**
     * Mount stopsHub
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/mountStopsHub", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="mountStopsHub", notes="mount StopsHub")
    public String mountStopsHub(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
        return stopsHubServiceImpl.mountStopsHub(username, isAdmin, id);
    }

    /**
     * unmount stopsHub
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/unmountStopsHub", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="unmountStopsHub", notes="unmount StopsHub")
    public String unmountStopsHub(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
        return stopsHubServiceImpl.unmountStopsHub(username, isAdmin, id);
    }

    /**
     * unmount stopsHub
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delStopsHub", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="delStopsHub", notes="delete StopsHub")
    public String delStopsHub(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
        return stopsHubServiceImpl.delStopsHub(username, isAdmin, id);
    }

    @RequestMapping(value = "/updateStopDisabled", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="updateStopDisabled", notes="update Stops disabled")
    public String updateStopDisabled(String id, Boolean disabled) {
        String username = SessionUserUtil.getCurrentUsername();
        Boolean isAdmin = SessionUserUtil.isAdmin();
        return propertyServiceImpl.updateStopDisabled(username, isAdmin, id, disabled);
    }

    @RequestMapping(value = "/publishingStops", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="publishingStops", notes="Publishing Stops")
    public String publishingStops(String name, List<String> stopsIds) {
        String username = SessionUserUtil.getCurrentUsername();
        return flowStopsPublishingServiceImpl.addFlowStopsPublishing(username, name, stopsIds);
    }

    @RequestMapping(value = "/updatePublishing", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="updatePublishing", notes="Update Publishing")
    public String updatePublishing(String publishingId, String name, List<String> stopsIds) {
        Boolean isAdmin = SessionUserUtil.isAdmin();
        String username = SessionUserUtil.getCurrentUsername();
        return flowStopsPublishingServiceImpl.updateFlowStopsPublishing(isAdmin, username, publishingId, name, stopsIds);
    }

    @RequestMapping(value = "/getPublishingById", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="getPublishingById", notes="Get Publishing by PublishingId")
    public String getPublishingById(String publishingId) {
        return flowStopsPublishingServiceImpl.getFlowStopsPublishingVo(publishingId);
    }

}
