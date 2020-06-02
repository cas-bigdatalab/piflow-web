package cn.cnic.component.stopsComponent.service.impl;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.base.vo.UserVo;
import cn.cnic.component.stopsComponent.mapper.PropertyTemplateMapper;
import cn.cnic.component.stopsComponent.mapper.StopGroupMapper;
import cn.cnic.component.stopsComponent.mapper.StopsTemplateMapper;
import cn.cnic.component.stopsComponent.model.PropertyTemplate;
import cn.cnic.component.stopsComponent.model.StopsComponentGroup;
import cn.cnic.component.stopsComponent.model.StopsTemplate;
import cn.cnic.component.stopsComponent.service.IStopGroupService;
import cn.cnic.component.stopsComponent.utils.StopsComponentUtils;
import cn.cnic.component.stopsComponent.vo.PropertyTemplateVo;
import cn.cnic.component.stopsComponent.vo.StopGroupVo;
import cn.cnic.component.stopsComponent.vo.StopsTemplateVo;
import cn.cnic.third.service.IStop;
import cn.cnic.third.vo.stop.ThirdStopsComponentVo;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class StopGroupServiceImpl implements IStopGroupService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private StopGroupMapper stopGroupMapper;


    @Resource
    private StopsTemplateMapper stopsTemplateMapper;

    @Resource
    private PropertyTemplateMapper propertyTemplateMapper;

    @Resource
    private IStop stopImpl;

    /**
     * Query all groups and all stops under it
     *
     * @return
     */
    @Override
    public List<StopGroupVo> getStopGroupAll() {
        List<StopsComponentGroup> stopGroupList = stopGroupMapper.getStopGroupList();
        if (null == stopGroupList || stopGroupList.size() <= 0) {
            return null;
        }
        List<StopGroupVo> stopGroupVoList = new ArrayList<>();
        for (StopsComponentGroup stopGroup : stopGroupList) {
            if (null == stopGroup) {
                continue;
            }
            StopGroupVo stopGroupVo = new StopGroupVo();
            BeanUtils.copyProperties(stopGroup, stopGroupVo);
            List<StopsTemplate> stopsTemplateList = stopGroup.getStopsTemplateList();
            if (null != stopsTemplateList && stopsTemplateList.size() > 0) {
                List<StopsTemplateVo> stopsTemplateVoList = new ArrayList<>();
                for (StopsTemplate stopsTemplate : stopsTemplateList) {
                    if (null == stopsTemplate) {
                        continue;
                    }
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
                stopGroupVo.setStopsTemplateVoList(stopsTemplateVoList);
            }
            stopGroupVoList.add(stopGroupVo);
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
                    StopsComponentGroup stopGroup = new StopsComponentGroup();
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
                ThirdStopsComponentVo thirdStopsComponentVo = stopImpl.getStopInfoNew(stopListInfos);
                List<String> list = null;
                if (null != thirdStopsComponentVo) {
                    list = Arrays.asList(thirdStopsComponentVo.getGroups().split(","));
                }
                // Query group information according to groupName in stops
                List<StopsComponentGroup> stopGroupByName = stopGroupMapper.getStopGroupByName(list);
                StopsTemplate stopsTemplate = StopsComponentUtils.thirdStopsComponentVoToStopsTemplate(user.getUsername(), thirdStopsComponentVo, stopGroupByName);
                if (null == stopsTemplate) {
                    continue;
                }
                List<StopsTemplate> listStopsTemplate = new ArrayList<>();
                listStopsTemplate.add(stopsTemplate);
                int insertStopsTemplate = stopsTemplateMapper.insertStopsTemplate(listStopsTemplate);
                logger.info("flow_stops_template affects the number of rows : " + insertStopsTemplate);
                logger.info("=============================association_groups_stops_template=====start==================");
                List<StopsComponentGroup> stopGroupList = stopsTemplate.getStopGroupList();
                for (StopsComponentGroup stopGroup : stopGroupList) {
                    String stopGroupId = stopGroup.getId();
                    String stopsTemplateId = stopsTemplate.getId();
                    int insertAssociationGroupsStopsTemplate = stopGroupMapper.insertAssociationGroupsStopsTemplate(stopGroupId, stopsTemplateId);
                    logger.info("association_groups_stops_template Association table insertion affects the number of rows : " + insertAssociationGroupsStopsTemplate);
                }
                List<PropertyTemplate> properties = stopsTemplate.getProperties();
                int insertPropertyTemplate = propertyTemplateMapper.insertPropertyTemplate(properties);
                logger.info("flow_stops_property_template affects the number of rows : " + insertPropertyTemplate);
            }
            logger.info(num + "num");
        }
    }

}
