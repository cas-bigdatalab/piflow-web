package com.nature.component.flow.service.impl;

import com.nature.base.util.*;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.PortType;
import com.nature.component.flow.model.Paths;
import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.component.flow.request.UpdatePathRequest;
import com.nature.component.flow.service.IPropertyService;
import com.nature.component.flow.utils.StopsUtils;
import com.nature.component.flow.vo.StopsVo;
import com.nature.component.stopsComponent.model.PropertyTemplate;
import com.nature.component.stopsComponent.model.StopsTemplate;
import com.nature.domain.flow.PropertyDomain;
import com.nature.domain.flow.StopsDomain;
import com.nature.domain.process.ProcessDomain;
import com.nature.mapper.flow.PathsMapper;
import com.nature.mapper.flow.PropertyMapper;
import com.nature.mapper.flow.StopsMapper;
import com.nature.mapper.stopsComponent.StopsTemplateMapper;
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
    private StopsTemplateMapper stopsTemplateMapper;

    @Resource
    private PathsMapper pathsMapper;

    @Resource
    private StopsDomain stopsDomain;

    @Resource
    private PropertyDomain propertyDomain;

    @Override
    public StopsVo queryAll(String fid, String stopPageId) {
        Stops stops = stopsDomain.getStopsByPageId(fid, stopPageId);
        StopsVo stopsVo = StopsUtils.stopPoToVo(stops);
        return stopsVo;
    }

    @Override
    public int updateProperty(String content, String id) {
        return propertyMapper.updatePropertyCustomValue(content, id);
    }

    @Override
    public List<Property> getStopsPropertyList() {
        return propertyMapper.getStopsPropertyList();
    }

    @Override
    public int deleteStopsPropertyById(String id) {
        return propertyMapper.deleteStopsPropertyById(id);
    }

    @Override
    public int deleteStopsPropertyByStopId(String id) {
        return propertyMapper.updateEnableFlagByStopId(id);
    }

    /**
     * Compare the 'stops' template if it is different
     */
    @Override
    public void checkStopTemplateUpdate(String id) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        Map<String, Property> PropertyMap = new HashMap<String, Property>();
        List<Property> addPropertyList = new ArrayList<Property>();
        //Get stop information
        Stops stopsList = stopsMapper.getStopsById(id);
        //Get the StopsTemplate of the current stops
        List<StopsTemplate> stopsTemplateList = stopsTemplateMapper.getStopsTemplateByName(stopsList.getName());
        StopsTemplate stopsTemplate = null;
        List<PropertyTemplate> propertiesTemplateList = null;
        if (null != stopsTemplateList && !stopsTemplateList.isEmpty()) {
            stopsTemplate = stopsTemplateList.get(0);
            logger.info("'stopsTemplateList' record number:" + stopsTemplateList.size());
        }
        //Get the template attribute of 'StopsTemplate'
        if (null != stopsTemplate) {
            propertiesTemplateList = stopsTemplate.getProperties();
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
            for (PropertyTemplate pt : propertiesTemplateList) {
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
                        newProperty.setId(SqlUtils.getUUID32());
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
    public String saveOrUpdateRoutePath(UpdatePathRequest updatePathRequest) {
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
            int pathsCounts = pathsMapper.getPathsCounts(flowId, null, sourceId, null);
            if (updatePathRequest.isSourceRoute()) {
                if (updatePathRequest.isSourceRoute() && PortType.ROUTE == sourceStop.getOutPortType()) {
                    currentPaths.setFilterCondition(updatePathRequest.getSourceFilter());
                    currentPaths.setOutport("port" + (pathsCounts));
                }
            } else if (StringUtils.isNotBlank(sourcePortVal)) {
                currentPaths.setOutport(sourcePortVal);
                updatePropertyBypaths(sourcePortVal, sourceStop, "outports");
            }
            if (StringUtils.isNotBlank(targetPortVal)) {
                currentPaths.setInport(targetPortVal);
                updatePropertyBypaths(targetPortVal, targetStop, "inports");
            }
            currentPaths.setLastUpdateDttm(new Date());
            currentPaths.setLastUpdateUser("-1");
            int i = pathsMapper.updatePaths(currentPaths);
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
    private void updatePropertyBypaths(String sourcePortVal, Stops stops, String propertyName) {
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
                        propertyMapper.updatePropertyCustomValue((ports + sourcePortVal), propertySave.getId());
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
    public String deleteLastReloadDataByStopsId(String stopId){
        int i = propertyDomain.deletePropertiesByIsOldDataAndStopsId(stopId);
        if(i > 0){
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("successfully deleted");
        }
        return ReturnMapUtils.setFailedMsgRtnJsonStr("Failed to delete");
    }

}
