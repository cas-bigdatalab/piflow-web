package com.nature.component.template.service.impl;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.component.template.model.PropertyTemplateModel;
import com.nature.component.template.model.StopTemplateModel;
import com.nature.component.template.model.Template;
import com.nature.component.template.service.IFlowAndStopsTemplateVoService;
import com.nature.component.template.vo.FlowTemplateModelVo;
import com.nature.mapper.flow.PropertyMapper;
import com.nature.mapper.flow.StopsMapper;
import com.nature.mapper.template.FlowAndStopsTemplateVoMapper;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FlowAndStopsTemplateVoServiceImpl implements IFlowAndStopsTemplateVoService {

    Logger logger = LoggerUtil.getLogger();


    @Autowired
    private FlowAndStopsTemplateVoMapper flowAndStopsTemplateVoMapper;

    @Autowired
    private StopsMapper stopsMapper;

    @Autowired
    private PropertyMapper propertyMapper;

    @Override
    public int addStops(StopTemplateModel stops) {
        return flowAndStopsTemplateVoMapper.addStops(stops);
    }

    @Override
    public int addFlow(FlowTemplateModelVo flow) {
        return flowAndStopsTemplateVoMapper.addFlow(flow);
    }

    @Override
    public int addPropertyList(List<PropertyTemplateModel> propertyList) {
        return flowAndStopsTemplateVoMapper.addPropertyList(propertyList);
    }

    @Override
    public int deleteStopTemByTemplateId(String templateId) {
        return flowAndStopsTemplateVoMapper.deleteStopTemByTemplateId(templateId);
    }

    @Override
    public int deleteStopPropertyTemByStopId(String stopId) {
        return flowAndStopsTemplateVoMapper.deleteStopPropertyTemByStopId(stopId);
    }

    @Override
    public List<StopTemplateModel> getStopsListByTemPlateId(String templateId) {
        return flowAndStopsTemplateVoMapper.getStopsListByTemPlateId(templateId);
    }

    @Override
    public List<PropertyTemplateModel> getPropertyListByStopsId(String stopsId) {
        return flowAndStopsTemplateVoMapper.getPropertyListByStopsId(stopsId);
    }

    @Override
    @Transactional
    public void addTemplateStopsToFlow(Template template, Flow flow, int maxPageId) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        int addPropertyList = 0;
        List<Property> list = new ArrayList<Property>();
        // Get stop information
        List<StopTemplateModel> stopsList = template.getStopsList();
        // Start traversing save stop and attribute information
        if (null != stopsList && stopsList.size() > 0) {
            for (StopTemplateModel stopsVo : stopsList) {
                Stops stop = new Stops();
                BeanUtils.copyProperties(stopsVo, stop);
                // The pageId maximum starts to increase
                stop.setPageId((Integer.parseInt(stopsVo.getPageId()) + maxPageId) + "");
                stop.setId(SqlUtils.getUUID32());
                stop.setCrtUser(username);
                stop.setLastUpdateUser(username);
                stop.setFlow(flow);
                stop.setIsCheckpoint(stopsVo.getIsCheckpoint());
                int addStops = stopsMapper.addStops(stop);
                logger.info("AddStops affects the number of rows : " + addStops);
                if (addStops > 0) {
                    if (null != stopsVo.getProperties()) {
                        List<PropertyTemplateModel> properties = stopsVo.getProperties();
                        if (null != properties && properties.size() > 0) {
                            for (PropertyTemplateModel property : properties) {
                                Property propertyModel = new Property();
                                BeanUtils.copyProperties(property, propertyModel);
                                propertyModel.setStops(stop);
                                propertyModel.setId(SqlUtils.getUUID32());
                                propertyModel.setCrtUser(username);
                                propertyModel.setLastUpdateUser(username);
                                list.add(propertyModel);
                            }
                        }
                    }
                } else {
                    logger.error("New save failed propertyModel", new Exception("New save failed propertyModel"));
                }


            }
            if (null != list && list.size() > 0) {
                logger.debug(list.size() + "attributes");
                addPropertyList = propertyMapper.addPropertyList(list);
                logger.debug("AddStops affects the number of rows : " + addPropertyList);
            }
        }
    }

    @Override
    @Transactional
    public void addStopsList(List<Stops> stopsList, Template template) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        List<PropertyTemplateModel> list = new ArrayList<PropertyTemplateModel>();
        // Save stop, attribute information
        if (null != stopsList && stopsList.size() > 0) {
            for (Stops stops : stopsList) {
                StopTemplateModel stopTemplate = new StopTemplateModel();
                BeanUtils.copyProperties(stops, stopTemplate);
                stopTemplate.setTemplate(template);
                stopTemplate.setId(SqlUtils.getUUID32());
                stopTemplate.setCrtDttm(new Date());
                stopTemplate.setCrtUser(username);
                stopTemplate.setIsCheckpoint(stops.getIsCheckpoint());
                flowAndStopsTemplateVoMapper.addStops(stopTemplate);
                if (null != stops.getProperties()) {
                    List<Property> properties = stops.getProperties();
                    if (null != properties && properties.size() > 0) {
                        for (Property property : properties) {
                            PropertyTemplateModel propertyVo = new PropertyTemplateModel();
                            BeanUtils.copyProperties(property, propertyVo);
                            propertyVo.setStopsVo(stopTemplate);
                            propertyVo.setId(SqlUtils.getUUID32());
                            propertyVo.setCrtDttm(new Date());
                            propertyVo.setCrtUser(username);
                            list.add(propertyVo);
                        }
                    }
                }
            }
            if (null != list && list.size() > 0) {
                flowAndStopsTemplateVoMapper.addPropertyList(list);
            }
        }
    }
}
