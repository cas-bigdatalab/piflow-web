package com.nature.component.system.service.Impl;

import com.nature.base.util.*;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.component.flow.utils.PropertyUtils;
import com.nature.component.stopsComponent.model.PropertyTemplate;
import com.nature.component.stopsComponent.model.StopGroup;
import com.nature.component.stopsComponent.model.StopsTemplate;
import com.nature.component.system.model.SysInitRecords;
import com.nature.component.system.service.ISysInitRecordsService;
import com.nature.domain.flow.PropertyDomain;
import com.nature.domain.flow.StopsDomain;
import com.nature.domain.system.SysInitRecordsDomain;
import com.nature.mapper.flow.PropertyMapper;
import com.nature.mapper.flow.StopsMapper;
import com.nature.mapper.stopsComponent.PropertyTemplateMapper;
import com.nature.mapper.stopsComponent.StopGroupMapper;
import com.nature.mapper.stopsComponent.StopsTemplateMapper;
import com.nature.third.service.IStop;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class SysInitRecordsServiceImpl implements ISysInitRecordsService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private SysInitRecordsDomain sysInitRecordsDomain;

    @Resource
    private IStop stopImpl;

    @Resource
    StopGroupMapper stopGroupMapper;

    @Resource
    private StopsTemplateMapper stopsTemplateMapper;

    @Resource
    private PropertyTemplateMapper propertyTemplateMapper;

    @Resource
    private StopsMapper stopsMapper;

    @Resource
    private PropertyMapper propertyMapper;

    @Transactional
    @Override
    public String initComponents(String currentUser) {
        Map<String, Object> rtnMap = new HashMap<>();
        ExecutorService es = new ThreadPoolExecutor(1, 5, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(100000));
        Boolean aBoolean = loadStopGroup(currentUser);
        if (aBoolean) {
            String[] stopNameList = stopImpl.getAllStops();
            // The call is successful, empty the "Stop" message and insert
            stopGroupMapper.deleteStopsPropertyInfo();
            int deleteStopsInfo = stopGroupMapper.deleteStopsInfo();
            logger.info("Successful deletion StopsInfo" + deleteStopsInfo + "piece of data!!!");
            if (null != stopNameList && stopNameList.length > 0) {
                for (String stopListInfos : stopNameList) {
                    es.execute(() -> {
                        Boolean aBoolean1 = loadStop(stopListInfos);
                        if (!aBoolean1) {
                            logger.warn("stop load failed, bundel : " + stopListInfos);
                        }
                    });
                }
            }
            List<Stops> stopsList = stopsMapper.getStopsList();
            if (null != stopsList && stopsList.size() > 0) {
                for (Stops stops : stopsList) {
                    if (null != stops) {
                        continue;
                    }
                    es.execute(() -> {
                        try {
                            syncStopsProperties(stops, currentUser);
                        } catch (IllegalAccessException e) {
                            logger.error("update stops data error", e);
                        } catch (ClassNotFoundException e) {
                            logger.error("update stops data error", e);
                        }
                    });
                }
            }
        }
        SysParamsCache.THREAD_POOL_EXECUTOR = ((ThreadPoolExecutor) es);
        rtnMap.put("code", 200);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String threadMonitoring(String currentUser) {
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("code", 500);
        if (null == SysParamsCache.THREAD_POOL_EXECUTOR) {
            return JsonUtils.toJsonNoException(rtnMap);
        }
        //Total number of threads
        double taskCount = SysParamsCache.THREAD_POOL_EXECUTOR.getTaskCount();
        //Number of execution completion threads
        double completedTaskCount = SysParamsCache.THREAD_POOL_EXECUTOR.getCompletedTaskCount();
        double progressNum = ((completedTaskCount / taskCount) * 40);
        if (39 < progressNum && progressNum < 40) {
            progressNum = 39;
        }
        long progressNumLong = (long) Math.ceil(progressNum) + 60;

        if (100 == progressNumLong) {
            addSysInitRecordsAndSave();
        }
        rtnMap.put("progress", progressNumLong);
        rtnMap.put("code", 200);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    private Boolean loadStopGroup(String currentUser) {
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
                    stopGroup.setCrtUser(currentUser);
                    stopGroup.setLastUpdateUser(currentUser);
                    stopGroup.setEnableFlag(true);
                    stopGroup.setLastUpdateDttm(new Date());
                    stopGroup.setGroupName(string);
                    int insertStopGroup = stopGroupMapper.insertStopGroup(stopGroup);
                    a += insertStopGroup;
                }
            }
            logger.debug("Successful insert Group" + a + "piece of data!!!");
        }
        return true;
    }

    private Boolean loadStop(String stopListInfos) {
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
        return true;
    }

    private Boolean addSysInitRecordsAndSave() {
        SysInitRecords sysInitRecords = new SysInitRecords();
        sysInitRecords.setId(SqlUtils.getUUID32());
        sysInitRecords.setInitDate(new Date());
        sysInitRecords.setIsSucceed(true);
        sysInitRecordsDomain.saveOrUpdate(sysInitRecords);
        SysParamsCache.setIsBootComplete(true);
        return true;
    }

    private void syncStopsProperties(Stops stops, String currentUser) throws IllegalAccessException, ClassNotFoundException {
        if (null == stops) {
            return;
        }
        String bundle = stops.getBundel();
        StopsTemplate stopsTemplateByBundle = stopsTemplateMapper.getStopsTemplateByBundle(bundle);
        if (null == stopsTemplateByBundle) {
            logger.info("The Stops component (" + bundle + ") has been deleted");
            return;
        }
        // propertiesTemplate to map
        List<PropertyTemplate> propertiesTemplate = stopsTemplateByBundle.getProperties();
        Map<String, PropertyTemplate> propertiesTemplateMap = new HashMap<>();
        if (null != propertiesTemplate && propertiesTemplate.size() > 0) {
            for (PropertyTemplate propertyTemplate : propertiesTemplate) {
                if (null == propertyTemplate) {
                    continue;
                }
                propertiesTemplateMap.put(propertyTemplate.getName(), propertyTemplate);
            }
        }
        List<Property> properties = stops.getProperties();
        List<Property> addProperties = stops.getProperties();
        if (null != properties && properties.size() > 0) {
            for (Property property : properties) {
                if (null == property) {
                    continue;
                }
                //Use name to get the value in the propertiesTemplateMap.
                // If it is, it means that it has the same property.
                // If it is not, it means the property is deleted.
                PropertyTemplate propertyTemplate = propertiesTemplateMap.get(property.getName());
                if (null == propertyTemplate) {
                    property.setIsOldData(true);
                    propertyMapper.updateStopsProperty(property);
                    continue;
                }
                // Whether the comparison has changed
                List<Map<String, Object>> listMaps = ComparedUtils.compareTwoClass(property, propertyTemplate);
                // If there is data, there is a change, marking the current stop attribute
                // If there is no data, it means no change, remove the current attribute in the map
                if (null != listMaps && listMaps.size() > 0) {
                    property.setIsOldData(true);
                    propertyMapper.updateStopsProperty(property);
                    continue;
                }
                propertiesTemplateMap.remove(property.getName());
            }
        }
        // If there is still data in the map, it means that these are to be added
        if (propertiesTemplateMap.keySet().size() > 0) {
            for (String key : propertiesTemplateMap.keySet()) {
                PropertyTemplate propertyTemplate = propertiesTemplateMap.get(key);
                Property property = PropertyUtils.propertyNewNoId(currentUser);
                BeanUtils.copyProperties(propertyTemplate, property);
                property.setId(SqlUtils.getUUID32());
                property.setStops(stops);
                property.setCustomValue(propertyTemplate.getDefaultValue());
                //Indicates "select"
                if (propertyTemplate.getAllowableValues().contains(",") && propertyTemplate.getAllowableValues().length() > 4) {
                    property.setIsSelect(true);
                    //Determine if there is a default value in "select"
                    if (!propertyTemplate.getAllowableValues().contains(propertyTemplate.getDefaultValue())) {
                        //Default value if not present
                        property.setCustomValue("");
                    }
                } else {
                    property.setIsSelect(false);
                }
                addProperties.add(property);
            }
        }
        stops.setProperties(properties);
        propertyMapper.addPropertyList(addProperties);
    }

}
