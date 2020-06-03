package cn.cnic.controller;

import cn.cnic.base.util.*;
import cn.cnic.base.vo.StatefulRtnBase;
import cn.cnic.base.vo.UserVo;
import cn.cnic.component.flow.model.Flow;
import cn.cnic.component.flow.service.ICustomizedPropertyService;
import cn.cnic.component.flow.service.IFlowService;
import cn.cnic.component.flow.service.IPropertyService;
import cn.cnic.component.flow.service.IStopsService;
import cn.cnic.component.flow.vo.StopsCustomizedPropertyVo;
import cn.cnic.component.flow.vo.StopsVo;
import cn.cnic.component.stopsComponent.service.IStopGroupService;
import cn.cnic.component.mxGraph.model.MxGraphModel;
import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.ui.Model;
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
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        stopGroupServiceImpl.updateGroupAndStopsListByServer(username);
        rtnMap.put("code", 200);
        rtnMap.put("load", load);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @RequestMapping("/queryIdInfo")
    public StopsVo getStopGroup(String fid, String stopPageId) {
        if (StringUtils.isNotBlank(fid) && StringUtils.isNotBlank(stopPageId)) {
            StopsVo queryInfo = propertyServiceImpl.queryAll(fid, stopPageId);
            if (null != queryInfo) {
                //Compare the stops template properties and make changes
                //propertyServiceImpl.checkStopTemplateUpdate(queryInfo.getId());
                return queryInfo;
            }
        }
        return null;
    }
    @RequestMapping("/deleteLastReloadData")
    public String deleteLastReloadData(String stopId) {
        return propertyServiceImpl.deleteLastReloadDataByStopsId(stopId);
    }

    /**
     * Get the usage of the current connection port
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/getStopsPort")
    @ResponseBody
    public String getStopsPort(HttpServletRequest request, Model model) {
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
    public Integer updateStops(String[] content, String id) {
        int updateStops = 0;
        if (null != content && content.length > 0) {
            for (String string : content) {
                //Use the #id# tag to intercept the data, the first is the content, and the second is the id of the record to be modified.
                String[] split = string.split("#id#");
                if (null != split && split.length == 2) {
                    String updateContent = split[0];
                    String updateId = split[1];
                    updateStops = propertyServiceImpl.updateProperty(updateContent, updateId);
                }
            }
        }
        if (updateStops > 0) {
            logger.info("The stops attribute was successfully modified.:" + updateStops);
            return updateStops;
        } else {
            return 0;
        }
    }

    @RequestMapping("/updateStopsOne")
    public String updateStops(String content, String id) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        int updateStops = 0;
        updateStops = propertyServiceImpl.updateProperty(content, id);
        if (updateStops > 0) {
            rtnMap.put("code", 200);
            rtnMap.put("errorMsg", "Saved successfully");
            rtnMap.put("value", content);
            logger.info("The stops attribute was successfully modified:" + updateStops);
        } else {
            rtnMap.put("errorMsg", "Database save failed");
            logger.info("Database save failed");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @RequestMapping("/updateStopsById")
    public String updateStopsById(HttpServletRequest request) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        String id = request.getParameter("stopId");
        String isCheckpointStr = request.getParameter("isCheckpoint");
        if (!StringUtils.isAnyEmpty(id, isCheckpointStr)) {
            boolean isCheckpoint = false;
            if ("1".equals(isCheckpointStr)) {
                isCheckpoint = true;
            }
            int updateStopsCheckpoint = stopsServiceImpl.updateStopsCheckpoint(id, isCheckpoint);
            if (updateStopsCheckpoint > 0) {
                rtnMap.put("code", 200);
                rtnMap.put("errorMsg", "Saved successfully");
                logger.info("Saved successfully");
            } else {
                rtnMap.put("errorMsg", "Database save failed");
                logger.info("Database save failed");
            }
        } else {
            rtnMap.put("errorMsg", "Partial incoming parameters are empty");
            logger.info("Partial incoming parameters are empty");
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @RequestMapping("/updateStopsNameById")
    public String updateStopsNameById(HttpServletRequest request) {
        String id = request.getParameter("stopId");
        String flowId = request.getParameter("flowId");
        String stopName = request.getParameter("name");
        String pageId = request.getParameter("pageId");
        if (StringUtils.isAnyEmpty(id, stopName, flowId, pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The incoming parameter is empty");
        }
        Flow flowById = flowServiceImpl.getFlowById(flowId);
        if (null == flowById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("flow information is empty");
        }
        StatefulRtnBase updateStopName = stopsServiceImpl.updateStopName(id, flowById, stopName, pageId);
        // addFlow is not empty and the value of ReqRtnStatus is true, then the save is successful.
        if (null == updateStopName || !updateStopName.isReqRtnStatus()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(updateStopName.getErrorMsg());
        }
        Map<String, Object> rtnMap = new HashMap<>();
        MxGraphModel mxGraphModel = flowById.getMxGraphModel();
        if (null != mxGraphModel) {
            MxGraphModelVo mxGraphModelVo = FlowXmlUtils.mxGraphModelPoToVo(mxGraphModel);
            // Convert the mxGraphModelVo from the query to XML
            String loadXml = MxGraphUtils.mxGraphModelToMxGraphXml(mxGraphModelVo);
            loadXml = StringUtils.isNotBlank(loadXml) ? loadXml : "";
            rtnMap.put("XmlData", loadXml);
        }
        rtnMap.put("code", 200);
        rtnMap.put("errorMsg", updateStopName.getErrorMsg());
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @RequestMapping("/addStopCustomizedProperty")
    public String addStopCustomizedProperty(StopsCustomizedPropertyVo stopsCustomizedPropertyVo) {
        return customizedPropertyServiceImpl.addStopCustomizedProperty(stopsCustomizedPropertyVo);
    }

    @RequestMapping("/updateStopsCustomizedProperty")
    public String updateStopsCustomizedProperty(StopsCustomizedPropertyVo stopsCustomizedPropertyVo) {
        return customizedPropertyServiceImpl.updateStopsCustomizedProperty(stopsCustomizedPropertyVo);
    }

    @RequestMapping("/deleteStopsCustomizedProperty")
    public String deleteStopsCustomizedProperty(String customPropertyId) {
        return customizedPropertyServiceImpl.deleteStopsCustomizedProperty(customPropertyId);
    }

    @RequestMapping("/deleteRouterStopsCustomizedProperty")
    public String deleteRouterStopsCustomizedProperty(String customPropertyId) {
        return customizedPropertyServiceImpl.deleteRouterStopsCustomizedProperty(customPropertyId);
    }

    @RequestMapping("/getRouterStopsCustomizedProperty")
    public String getRouterStopsCustomizedProperty(String customPropertyId) {
        return customizedPropertyServiceImpl.getRouterStopsCustomizedProperty(customPropertyId);
    }
}
