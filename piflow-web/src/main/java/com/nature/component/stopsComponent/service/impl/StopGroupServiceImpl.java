package com.nature.component.stopsComponent.service.impl;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.stopsComponent.vo.PropertyTemplateVo;
import com.nature.component.stopsComponent.vo.StopGroupVo;
import com.nature.component.stopsComponent.vo.StopsTemplateVo;
import com.nature.component.stopsComponent.model.PropertyTemplate;
import com.nature.component.stopsComponent.model.StopGroup;
import com.nature.component.stopsComponent.model.StopsTemplate;
import com.nature.component.stopsComponent.service.IStopGroupService;
import com.nature.mapper.stopsComponent.PropertyTemplateMapper;
import com.nature.mapper.stopsComponent.StopGroupMapper;
import com.nature.mapper.stopsComponent.StopsTemplateMapper;
import com.nature.third.service.IStop;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StopGroupServiceImpl implements IStopGroupService {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    StopGroupMapper stopGroupMapper;


    @Autowired
    private StopsTemplateMapper stopsTemplateMapper;

    @Autowired
    private PropertyTemplateMapper propertyTemplateMapper;

    @Autowired
    private IStop stopImpl;

    /**
     * Query all groups and all stops under it
     *
     * @return
     */
    @Override
    public List<StopGroupVo> getStopGroupAll() {
        List<StopGroupVo> stopGroupVoList = null;
        List<StopGroup> stopGroupList = stopGroupMapper.getStopGroupList();
        if (null != stopGroupList && stopGroupList.size() > 0) {
            stopGroupVoList = new ArrayList<StopGroupVo>();
            for (StopGroup stopGroup : stopGroupList) {
                if (null != stopGroup) {
                    StopGroupVo stopGroupVo = new StopGroupVo();
                    BeanUtils.copyProperties(stopGroup, stopGroupVo);
                    List<StopsTemplate> stopsTemplateList = stopGroup.getStopsTemplateList();
                    if (null != stopsTemplateList && stopsTemplateList.size() > 0) {
                        List<StopsTemplateVo> stopsTemplateVoList = new ArrayList<StopsTemplateVo>();
                        for (StopsTemplate stopsTemplate : stopsTemplateList) {
                            if (null != stopsTemplate) {
                                StopsTemplateVo stopsTemplateVo = new StopsTemplateVo();
                                BeanUtils.copyProperties(stopsTemplate, stopsTemplateVo);
                                List<PropertyTemplate> properties = stopsTemplate.getProperties();
                                if (null != properties && properties.size() > 0) {
                                    List<PropertyTemplateVo> propertiesVo = new ArrayList<PropertyTemplateVo>();
                                    for (PropertyTemplate propertyTemplate : properties) {
                                        if (null != propertiesVo) {
                                            PropertyTemplateVo propertyTemplateVo = new PropertyTemplateVo();
                                            BeanUtils.copyProperties(propertyTemplate, propertyTemplateVo);
                                            propertiesVo.add(propertyTemplateVo);
                                        }
                                    }
                                    stopsTemplateVo.setPropertiesVo(propertiesVo);
                                }
                                stopsTemplateVoList.add(stopsTemplateVo);
                            }
                        }
                        stopGroupVo.setStopsTemplateVoList(stopsTemplateVoList);
                    }
                    stopGroupVoList.add(stopGroupVo);
                }
            }
        }
        return stopGroupVoList;
    }

    @Override
    @Transactional
    public void addGroupAndStopsList(UserVo user) {
        getGroupAndSave(user);
        getStopsAndProperty(user);
    }

    @Transactional
    public void getGroupAndSave(UserVo user) {
        String username = (null != user) ? user.getUsername() : "-1";

        String[] group = stopImpl.getAllGroup();
        if (null != group && group.length > 0) {
            // The call is successful, the group table information is cleared and then inserted.
            stopGroupMapper.deleteGroupCorrelation();
            int deleteGroup = stopGroupMapper.deleteGroup();
            logger.debug("Successful deletion Group" + deleteGroup + "piece of data!!!");
            int a = 0;
            for (String string : group) {
                if (string.length() > 0) {
                    StopGroup stopGroup = new StopGroup();
                    stopGroup.setId(SqlUtils.getUUID32());
                    stopGroup.setCrtDttm(new Date());
                    stopGroup.setCrtUser(username);
                    stopGroup.setLastUpdateUser(username);
                    stopGroup.setEnableFlag(true);
                    stopGroup.setLastUpdateDttm(new Date());
                    stopGroup.setGroupName(string);
                    int insertStopGroup = stopGroupMapper.insertStopGroup(stopGroup);
                    a += insertStopGroup;
                }
            }
            logger.debug("Successful insert Group" + a + "piece of data!!!");
        }
    }

    @Transactional
    public void getStopsAndProperty(UserVo user) {
        // 1.First adjust the "stop" interface to get the "getAllStops" data;
        String[] stopNameList = stopImpl.getAllStops();
        if (null != stopNameList && stopNameList.length > 0) {
            // The call is successful, empty the "Stop" message and insert
            stopGroupMapper.deleteStopsPropertyInfo();
            int deleteStopsInfo = stopGroupMapper.deleteStopsInfo();
            logger.info("Successful deletion StopsInfo" + deleteStopsInfo + "piece of data!!!");
            int num = 0;
            for (String stopListInfos : stopNameList) {
                num++;
                // 2.First query "stopInfo" according to "bundle"
                logger.info("Now the call is：" + stopListInfos);
                StopsTemplate stopsTemplate = stopImpl.getStopInfo(stopListInfos);
                if (null != stopsTemplate) {
                    List<StopsTemplate> listStopsTemplate = new ArrayList<>();
                    listStopsTemplate.add(stopsTemplate);
                    int insertStopsTemplate = stopsTemplateMapper.insertStopsTemplate(listStopsTemplate);
                    logger.info("flow_stops_template affects the number of rows : " + insertStopsTemplate);
                    logger.info("=============================association_groups_stops_template=====start==================");
                    List<StopGroup> stopGroupList = stopsTemplate.getStopGroupList();
                    for (StopGroup stopGroup : stopGroupList) {
                        String stopGroupId = stopGroup.getId();
                        String stopsTemplateId = stopsTemplate.getId();
                        int insertAssociationGroupsStopsTemplate = stopGroupMapper.insertAssociationGroupsStopsTemplate(stopGroupId, stopsTemplateId);
                        logger.info("association_groups_stops_template Association table insertion affects the number of rows : " + insertAssociationGroupsStopsTemplate);
                    }
                    List<PropertyTemplate> properties = stopsTemplate.getProperties();
                    int insertPropertyTemplate = propertyTemplateMapper.insertPropertyTemplate(properties);
                    logger.info("flow_stops_property_template affects the number of rows : " + insertPropertyTemplate);
                }
            }
            logger.info(num + "num");
        }
    }

}
