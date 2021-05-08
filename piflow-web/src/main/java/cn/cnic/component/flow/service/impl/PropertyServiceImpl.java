package cn.cnic.component.flow.service.impl;

import cn.cnic.base.util.*;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.component.flow.entity.Paths;
import cn.cnic.component.flow.entity.Property;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.flow.request.UpdatePathRequest;
import cn.cnic.component.flow.service.IPropertyService;
import cn.cnic.component.flow.utils.StopsUtils;
import cn.cnic.component.flow.vo.StopsVo;
import cn.cnic.component.stopsComponent.mapper.StopsComponentMapper;
import cn.cnic.component.stopsComponent.model.StopsComponent;
import cn.cnic.component.stopsComponent.model.StopsComponentProperty;
import cn.cnic.component.flow.jpa.domain.PropertyDomain;
import cn.cnic.component.flow.jpa.domain.StopsDomain;
import cn.cnic.component.flow.mapper.PathsMapper;
import cn.cnic.component.flow.mapper.PropertyMapper;
import cn.cnic.component.flow.mapper.StopsMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class PropertyServiceImpl implements IPropertyService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private PropertyMapper propertyMapper;

    @Resource
    private StopsMapper stopsMapper;

    @Resource
    private StopsComponentMapper stopsComponentMapper;

    @Resource
    private PathsMapper pathsMapper;

    @Resource
    private StopsDomain stopsDomain;

    @Resource
    private PropertyDomain propertyDomain;

    @Override
    public String queryAll(String fid, String stopPageId) {
        if (StringUtils.isBlank(fid)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("flowId is null");
        }
        if (StringUtils.isBlank(stopPageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("stopPageId is null");
        }
        Stops stops = stopsDomain.getStopsByPageId(fid, stopPageId);
        if (null == stops) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null");
        }
        StopsComponent stopsComponentByBundle = stopsComponentMapper.getStopsComponentByBundle(stops.getBundel());
        StopsVo stopsVo = StopsUtils.stopPoToVo(stops, stopsComponentByBundle);
        if (null == stopsVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null");
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("stopsVo", stopsVo);
    }

    @Override
    public String updatePropertyList(String username, String[] content) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("user Illegality");
        }
        if (null == content || content.length <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is null");
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
            updateStops += propertyMapper.updatePropertyCustomValue(username, updateContent, updateId);
        }
        if (updateStops > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("The stops attribute was successfully modified. counts:" + updateStops);
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("update failed");
        }
    }

    @Override
    public String updateProperty(String username, String content, String id) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("user Illegality");
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is null");
        }
        int updateStops = propertyMapper.updatePropertyCustomValue(username, content, id);
        if (updateStops > 0) {
            logger.info("The stops attribute was successfully modified:" + updateStops);
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("value", content);
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("save failed");
        }
    }

    @Override
    public List<Property> getStopsPropertyList() {
        return propertyMapper.getStopsPropertyList();
    }

    @Override
    public int deleteStopsPropertyById(String id) {
        return propertyMapper.deleteStopsPropertyById(id);
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
        Stops stopsList = stopsMapper.getStopsById(id);
        //Get the StopsTemplate of the current stops
        List<StopsComponent> stopsComponentList = stopsComponentMapper.getStopsComponentByName(stopsList.getName());
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
                if (null != pt) {
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
                        propertyMapper.updateStopsProperty(update);
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
            }
            if (addPropertyList.size() > 0 && !addPropertyList.isEmpty()) {
                propertyMapper.addPropertyList(addPropertyList);
            }
            //All the changes in ‘objectPathsMap’ that need to be modified, left for logical deletion.
            if (null != PropertyMap && PropertyMap.size() > 0)
                for (String pageid : PropertyMap.keySet()) {
                    Property deleteProperty = PropertyMap.get(pageid);
                    if (null != deleteProperty) {
                        logger.info("===============The 'stop' attribute is inconsistent with the template and needs to be deleted.=================");
                        propertyMapper.deleteStopsPropertyById(deleteProperty.getId());
                    }
                }
        }
    }

    @Override
    public String saveOrUpdateRoutePath(String username, UpdatePathRequest updatePathRequest) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        String[] checkFields = new String[]{"flowId", "pathLineId", "sourceId", "targetId"};
        if (CheckFiledUtils.checkObjSpecifiedFieldsIsNull(updatePathRequest, checkFields)) {
            rtnMap.put("errorMsg", "Required field is empty");
            logger.info("Required field is empty");
            return JsonUtils.toJsonNoException(rtnMap);
        } else {
            String flowId = updatePathRequest.getFlowId();
            String pathLineId = updatePathRequest.getPathLineId();
            String sourceId = updatePathRequest.getSourceId();
            String sourcePortVal = updatePathRequest.getSourcePortVal();
            String targetId = updatePathRequest.getTargetId();
            String targetPortVal = updatePathRequest.getTargetPortVal();
            Stops sourceStop = null;
            Stops targetStop = null;
            List<Stops> queryInfoList = stopsMapper.getStopsListByFlowIdAndPageIds(flowId, new String[]{sourceId, targetId});
            // If 'queryInfoList' is empty, or the size of 'queryInfoList' is less than 2, return directly
            if (null == queryInfoList || queryInfoList.size() < 2) {
                rtnMap.put("errorMsg", "Can't find 'source' or 'target'");
                return JsonUtils.toJsonNoException(rtnMap);
            } else {
                //Loop out 'sourceStop' and 'targetStop'
                for (Stops stop : queryInfoList) {
                    if (null != stop) {
                        if (sourceId.equals(stop.getPageId())) {
                            sourceStop = stop;
                        } else if (targetId.equals(stop.getPageId())) {
                            targetStop = stop;
                        }
                    }
                }
            }
            Paths currentPaths = null;
            List<Paths> pathsList = pathsMapper.getPaths(flowId, pathLineId, null, null);
            if (null != pathsList && pathsList.size() == 1) {
                currentPaths = pathsList.get(0);
            }
            pathsMapper.getPathsCounts(flowId, null, sourceId, null);
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
            int i = pathsMapper.updatePaths(username, currentPaths);
            if (i <= 0) {
                rtnMap.put("code", 500);
                rtnMap.put("errorMsg", "Save failed");
            } else {
                rtnMap.put("code", 200);
                rtnMap.put("errorMsg", "Successfully saved");
            }
            return JsonUtils.toJsonNoException(rtnMap);
        }
    }

    /**
     * Modify the port attribute value of port type ‘any’ according to the port information of ‘paths’
     *
     * @param sourcePortVal
     * @param stops
     * @param propertyName
     */
    private void updatePropertyBypaths(String username, String sourcePortVal, Stops stops, String propertyName) {
        if (null != stops) {
            if (PortType.ANY == stops.getInPortType() || PortType.ANY == stops.getOutPortType()) {
                List<Property> propertyList = stops.getProperties();
                if (null != propertyList && propertyList.size() > 0) {
                    String ports = null;
                    Property propertySave = null;
                    for (Property property : propertyList) {
                        if (propertyName.equals(property.getName())) {
                            propertySave = property;
                            break;
                        }
                    }
                    if (null != propertySave) {
                        if (null == propertySave.getCustomValue()) {
                            ports = "";
                        } else {
                            ports = propertySave.getCustomValue();
                        }
                        if (StringUtils.isNotBlank(ports)) {
                            ports = ports + ",";
                        }
                        propertyMapper.updatePropertyCustomValue(username, (ports + sourcePortVal), propertySave.getId());
                    }
                }
            }
        }
    }

    /**
     * deleteLastReloadDataByStopsId
     *
     * @param stopId
     * @return
     */
    @Transactional
    @Override
    public String deleteLastReloadDataByStopsId(String stopId) {
        int i = propertyDomain.deletePropertiesByIsOldDataAndStopsId(stopId);
        if (i > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("successfully deleted");
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr("Failed to delete");
    }

}
