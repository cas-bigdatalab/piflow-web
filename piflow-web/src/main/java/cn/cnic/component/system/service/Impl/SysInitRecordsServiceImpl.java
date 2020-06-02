package cn.cnic.component.system.service.Impl;

import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.flow.model.Property;
import cn.cnic.component.flow.model.Stops;
import cn.cnic.component.flow.utils.PropertyUtils;
import cn.cnic.component.stopsComponent.mapper.PropertyTemplateMapper;
import cn.cnic.component.stopsComponent.mapper.StopGroupMapper;
import cn.cnic.component.stopsComponent.mapper.StopsTemplateMapper;
import cn.cnic.component.stopsComponent.model.PropertyTemplate;
import cn.cnic.component.stopsComponent.model.StopsComponentGroup;
import cn.cnic.component.stopsComponent.model.StopsTemplate;
import cn.cnic.component.system.model.SysInitRecords;
import cn.cnic.component.system.service.ISysInitRecordsService;
import cn.cnic.domain.system.SysInitRecordsDomain;
import cn.cnic.mapper.flow.PropertyMapper;
import cn.cnic.mapper.flow.StopsMapper;
import cn.cnic.third.service.IStop;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
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
    private StopGroupMapper stopGroupMapper;

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
                    if (null == stops) {
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
                    StopsComponentGroup stopGroup = new StopsComponentGroup();
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
                /*
                // Whether the comparison has changed
                List<Map<String, Object>> listMaps = ComparedUtils.compareTwoClass(property, propertyTemplate);
                // If there is data, there is a change, marking the current stop attribute
                // If there is no data, it means no change, remove the current attribute in the map
                if (null != listMaps && listMaps.size() > 0) {
                    property.setIsOldData(true);
                    propertyMapper.updateStopsProperty(property);
                    continue;
                }
                */
                propertiesTemplateMap.remove(property.getName());
            }
        }
        List<Property> addProperties = new ArrayList<>();
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
