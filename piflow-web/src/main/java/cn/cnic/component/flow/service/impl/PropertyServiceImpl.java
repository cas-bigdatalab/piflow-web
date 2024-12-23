package cn.cnic.component.flow.service.impl;

import cn.cnic.base.utils.*;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.flow.domain.FlowDomain;
import cn.cnic.component.flow.domain.FlowStopsPublishingDomain;
import cn.cnic.component.flow.entity.Paths;
import cn.cnic.component.flow.entity.Property;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.flow.request.UpdatePathRequest;
import cn.cnic.component.flow.service.IPropertyService;
import cn.cnic.component.flow.utils.StopsUtils;
import cn.cnic.component.flow.vo.StopsPropertyVo;
import cn.cnic.component.flow.vo.StopsVo;
import cn.cnic.component.stopsComponent.domain.StopsComponentDomain;
import cn.cnic.component.stopsComponent.entity.StopsComponent;
import cn.cnic.component.stopsComponent.entity.StopsComponentProperty;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class PropertyServiceImpl implements IPropertyService {

    private Logger logger = LoggerUtil.getLogger();

    private final FlowDomain flowDomain;
    private final StopsComponentDomain stopsComponentDomain;
    private final FlowStopsPublishingDomain flowStopsPublishingDomain;

    @Autowired
    public PropertyServiceImpl(FlowDomain flowDomain, StopsComponentDomain stopsComponentDomain, FlowStopsPublishingDomain flowStopsPublishingDomain){
        this.flowDomain = flowDomain;
        this.stopsComponentDomain = stopsComponentDomain;
        this.flowStopsPublishingDomain = flowStopsPublishingDomain;
    }
    @Override
    public String queryAll(String fid, String stopPageId) {
        if (StringUtils.isBlank(fid)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("fid"));
        }
        if (StringUtils.isBlank(stopPageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("stopPageId"));
        }
        Stops stops = flowDomain.getStopsByPageId(fid, stopPageId);
        if (null == stops) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        StopsComponent stopsComponentByBundle = stopsComponentDomain.getStopsComponentByBundle(stops.getBundel());
        StopsVo stopsVo = StopsUtils.stopPoToVo(stops, stopsComponentByBundle);
        if (null == stopsVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("stopsVo", stopsVo);
    }

    @Override
    public String updatePropertyList(String username, String[] content) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (null == content || content.length <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        int updateStops = 0;
        for (String string : content) {
            //Use the #id# tag to intercept the data, the first is the content, and the second is the id of the record to be modified.
            String[] split = string.split("#id#");
            if (null == split || split.length != 2) {
                continue;
            }
            String updateContent = split[0];
            String updateId = split[1];
            updateStops += flowDomain.updatePropertyCustomValue(username, updateContent, updateId);
        }
        if (updateStops > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("The stops attribute was successfully modified. counts:" + updateStops);
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPDATE_ERROR_MSG());
        }
    }

    @Override
    public String updateProperty(String username, String content, String id) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        int updateStops = flowDomain.updatePropertyCustomValue(username, content, id);
        if (updateStops > 0) {
            logger.info("The stops attribute was successfully modified:" + updateStops);
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("value", content);
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
        }
    }

    @Override
    public List<Property> getStopsPropertyList() {
        return flowDomain.getStopsPropertyList();
    }

    @Override
    public int deleteStopsPropertyById(String id) {
        return flowDomain.deleteStopsPropertyById(id);
    }

    /**
     * Compare the 'stops' template if it is different
     */
    @Override
    public void checkStopTemplateUpdate(String username, String id) {
        if (StringUtils.isBlank(username)) {
            return;
        }
        Map<String, Property> PropertyMap = new HashMap<String, Property>();
        List<Property> addPropertyList = new ArrayList<Property>();
        //Get stop information
        Stops stopsList = flowDomain.getStopsById(id);
        //Get the StopsTemplate of the current stops
        List<StopsComponent> stopsComponentList = stopsComponentDomain.getStopsComponentByName(stopsList.getName());
        StopsComponent stopsComponent = null;
        List<StopsComponentProperty> propertiesTemplateList = null;
        if (null != stopsComponentList && !stopsComponentList.isEmpty()) {
            stopsComponent = stopsComponentList.get(0);
            logger.info("'stopsTemplateList' record number:" + stopsComponentList.size());
        }
        //Get the template attribute of 'StopsTemplate'
        if (null != stopsComponent) {
            propertiesTemplateList = stopsComponent.getProperties();
        }
        // Current 'stop' attribute
        List<Property> property = stopsList.getProperties();
        if (null != property && property.size() > 0)
            for (Property one : property) {
                PropertyMap.put(one.getName(), one);
            }
        // If the data of the template is larger than the current number of attributes of 'stop',
        // the same modification operation is performed, and the new 'stops' attribute is added.
        if (propertiesTemplateList.size() > 0 && property.size() > 0) {
            for (StopsComponentProperty pt : propertiesTemplateList) {
                if (null == pt) {
                    continue;
                }
                Property ptname = PropertyMap.get(pt.getName());
                if (ptname != null) {
                    PropertyMap.remove(pt.getName());
                    Property update = new Property();
                    String name = ptname.getName();
                    Date crtDttm = ptname.getCrtDttm();
                    String crtUser = ptname.getCrtUser();
                    String displayName = pt.getDisplayName();
                    String description = pt.getDescription();
                    BeanUtils.copyProperties(pt, update);
                    update.setName(name);
                    update.setCrtDttm(crtDttm);
                    update.setCrtUser(crtUser);
                    update.setDisplayName(displayName);
                    update.setDescription(description);
                    update.setId(ptname.getId());
                    flowDomain.updateStopsProperty(update);
                } else {
                    logger.info("===============The 'stop' attribute is inconsistent with the template and needs to be added=================");
                    Property newProperty = new Property();
                    String displayName = pt.getDisplayName();
                    String description = pt.getDescription();
                    BeanUtils.copyProperties(pt, newProperty);
                    newProperty.setId(UUIDUtils.getUUID32());
                    newProperty.setCrtDttm(new Date());
                    newProperty.setCrtUser(username);
                    newProperty.setEnableFlag(true);
                    newProperty.setDisplayName(displayName);
                    newProperty.setDescription(description);
                    newProperty.setStops(stopsList);
                    addPropertyList.add(newProperty);
                }
            }
            if (addPropertyList.size() > 0 && !addPropertyList.isEmpty()) {
                flowDomain.addPropertyList(addPropertyList);
            }
            //All the changes in ‘objectPathsMap’ that need to be modified, left for logical deletion.
            if (null != PropertyMap && PropertyMap.size() > 0)
                for (String pageid : PropertyMap.keySet()) {
                    Property deleteProperty = PropertyMap.get(pageid);
                    if (null == deleteProperty) {
                        continue;
                    }
                    logger.info("===============The 'stop' attribute is inconsistent with the template and needs to be deleted.=================");
                    flowDomain.deleteStopsPropertyById(deleteProperty.getId());
                }
        }
    }

    @Override
    public String saveOrUpdateRoutePath(String username, UpdatePathRequest updatePathRequest) {
        String[] checkFields = new String[]{"flowId", "pathLineId", "sourceId", "targetId"};
        if (CheckFiledUtils.checkObjSpecifiedFieldsIsNull(updatePathRequest, checkFields)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        String flowId = updatePathRequest.getFlowId();
        String pathLineId = updatePathRequest.getPathLineId();
        String sourceId = updatePathRequest.getSourceId();
        String sourcePortVal = updatePathRequest.getSourcePortVal();
        String targetId = updatePathRequest.getTargetId();
        String targetPortVal = updatePathRequest.getTargetPortVal();
        Stops sourceStop = null;
        Stops targetStop = null;
        List<Stops> queryInfoList = flowDomain.getStopsListByFlowIdAndPageIds(flowId, new String[]{sourceId, targetId});
        // If 'queryInfoList' is empty, or the size of 'queryInfoList' is less than 2, return directly
        if (null == queryInfoList || queryInfoList.size() < 2) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Can't find 'source' or 'target'");
        }
        //Loop out 'sourceStop' and 'targetStop'
        for (Stops stop : queryInfoList) {
            if (null == stop) {
                continue;
            }
            if (sourceId.equals(stop.getPageId())) {
                sourceStop = stop;
            } else if (targetId.equals(stop.getPageId())) {
                targetStop = stop;
            }
        }
        Paths currentPaths = null;
        List<Paths> pathsList = flowDomain.getPaths(flowId, pathLineId, null, null);
        if (null != pathsList && pathsList.size() == 1) {
            currentPaths = pathsList.get(0);
        }
        flowDomain.getPathsCounts(flowId, null, sourceId, null);
        if (updatePathRequest.isSourceRoute()) {
            if (updatePathRequest.isSourceRoute() && PortType.ROUTE == sourceStop.getOutPortType()) {
                currentPaths.setFilterCondition(updatePathRequest.getSourceFilter());
                //currentPaths.setOutport("port" + (pathsCounts));
                currentPaths.setOutport(updatePathRequest.getSourceFilter());
            }
        } else if (StringUtils.isNotBlank(sourcePortVal)) {
            currentPaths.setOutport(sourcePortVal);
            updatePropertyBypaths(username, sourcePortVal, sourceStop, "outports");
        }
        if (StringUtils.isNotBlank(targetPortVal)) {
            currentPaths.setInport(targetPortVal);
            updatePropertyBypaths(username, targetPortVal, targetStop, "inports");
        }
        currentPaths.setLastUpdateDttm(new Date());
        currentPaths.setLastUpdateUser("-1");
        int i = flowDomain.updatePaths(currentPaths);
        if (i <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    /**
     * Modify the port attribute value of port type ‘any’ according to the port information of ‘paths’
     *
     * @param sourcePortVal
     * @param stops
     * @param propertyName
     */
    private void updatePropertyBypaths(String username, String sourcePortVal, Stops stops, String propertyName) {
        if (null == stops) {
            return;
        }
        if (PortType.ANY != stops.getInPortType() && PortType.ANY != stops.getOutPortType()) {
            return;
        }
        List<Property> propertyList = stops.getProperties();
        if (null == propertyList || propertyList.size() <= 0) {
            return;
        }
        String ports = null;
        Property propertySave = null;
        for (Property property : propertyList) {
            if (!propertyName.equals(property.getName())) {
                continue;
            }
            propertySave = property;
            break;
        }
        if (null == propertySave) {
            return;
        }
        if (null == propertySave.getCustomValue()) {
            ports = "";
        } else {
            ports = propertySave.getCustomValue();
        }
        if (StringUtils.isNotBlank(ports)) {
            ports = ports + ",";
        }
        flowDomain.updatePropertyCustomValue(username, (ports + sourcePortVal), propertySave.getId());
    }

    /**
     * deleteLastReloadDataByStopsId
     *
     * @param stopId
     * @return
     */
    @Override
    public String deleteLastReloadDataByStopsId(String stopId) {
        int i = flowDomain.deletePropertiesByIsOldDataAndStopsId(stopId);
        if (i > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.DELETE_SUCCEEDED_MSG());
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DELETE_ERROR_MSG());
    }

    /**
     * updateStopDisabled
     *
     * @param username
     * @param isAdmin
     * @param id
     * @param disabled
     */
    @Override
    public String updateStopDisabled(String username, Boolean isAdmin, String id, Boolean disabled) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (null == isAdmin) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        }
        if (null == disabled) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("disabled"));
        }

        if (true == disabled) {
            List<String> publishingNames = flowStopsPublishingDomain.getPublishingNamesByStopsId(id);
            if (null != publishingNames && publishingNames.size() > 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.STOP_PUBLISHED_CANNOT_DISABLED_MSG(publishingNames.toString().replace("[", "'").replace("]", "'")));
            }
        }
        Stops stops = flowDomain.getStopsById(id);
        if (null == stops) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        if (!isAdmin) {
            String crtUser = stops.getCrtUser();
            if (!username.equals(crtUser)) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_OPERATION_MSG());
            }
        }
        stops.setIsDisabled(disabled);
        try {
            int affectedRows = flowDomain.saveOrUpdate(stops);
            if (affectedRows <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
            }
        } catch (Exception e) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    @Override
    public String getStopsInfoByFlowId(String flowId) {
        //flow_stops表和对应的其他的相关信息
        List<Stops> stopsList = flowDomain.getStopsListByFlowId(flowId);
//        List<StopsVo> result = new ArrayList<>();
//        try {
//            if(CollectionUtils.isNotEmpty(stopsList)){
//                for (Stops stops : stopsList) {
//                    StopsVo stopsVo = extendStopsInfo(stops);
//                    result.add(stopsVo);
//                }
//            }
//        } catch (Exception e) {
//            return ReturnMapUtils.setFailedMsgRtnJsonStr(e.getMessage());
//        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("stopsList",stopsList);
    }


}
