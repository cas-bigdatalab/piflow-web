package cn.cnic.component.system.service.Impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cnic.base.utils.JsonUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.flow.domain.StopsDomain;
import cn.cnic.component.flow.entity.Property;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.component.flow.utils.PropertyUtils;
import cn.cnic.component.stopsComponent.domain.StopsComponentDomain;
import cn.cnic.component.stopsComponent.entity.StopsComponent;
import cn.cnic.component.stopsComponent.entity.StopsComponentGroup;
import cn.cnic.component.stopsComponent.entity.StopsComponentProperty;
import cn.cnic.component.stopsComponent.utils.StopsComponentGroupUtils;
import cn.cnic.component.stopsComponent.utils.StopsComponentUtils;
import cn.cnic.component.system.domain.SysInitRecordsDomain;
import cn.cnic.component.system.entity.SysInitRecords;
import cn.cnic.component.system.service.ISysInitRecordsService;
import cn.cnic.third.service.IStop;
import cn.cnic.third.vo.stop.ThirdStopsComponentVo;


@Service
public class SysInitRecordsServiceImpl implements ISysInitRecordsService {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    @Autowired
    private SysInitRecordsDomain sysInitRecordsDomain;

    @Autowired
    private StopsDomain stopsDomain;

    @Autowired
    private StopsComponentDomain stopsComponentDomain;

    @Autowired
    private IStop stopImpl;

    public boolean isInBootPage() {
        // Determine if the boot flag is true
        if (!SysParamsCache.IS_BOOT_COMPLETE) {
            // Query is boot record
            SysInitRecords sysInitRecordsLastNew = sysInitRecordsDomain.getSysInitRecordsLastNew(1);
            if (null == sysInitRecordsLastNew || !sysInitRecordsLastNew.getIsSucceed()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String initComponents(String currentUser) {
        boolean inBootPage = isInBootPage();
        if (!inBootPage) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No initialization, enter the boot page");
        }
        Map<String, Object> rtnMap = new HashMap<>();
        ExecutorService es = new ThreadPoolExecutor(1, 5, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(100000));
        List<String> stopsBundleList = loadStopGroup(currentUser);
        if (null != stopsBundleList && !stopsBundleList.isEmpty()) {
            if (null != stopsBundleList && stopsBundleList.size() > 0) {
                for (String stopListInfos : stopsBundleList) {
                    es.execute(() -> {
                        Boolean aBoolean1 = loadStop(stopListInfos);
                        if (!aBoolean1) {
                            logger.warn("stop load failed, bundle : " + stopListInfos);
                        }
                    });
                }
            }
            List<Stops> stopsList = stopsDomain.getStopsList();
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
        if (null == SysParamsCache.THREAD_POOL_EXECUTOR) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("THREAD_POOL_EXECUTOR is null");
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
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("progress", progressNumLong);
    }

    private List<String> loadStopGroup(String currentUser) {
        Map<String, List<String>> stopsListWithGroup = stopImpl.getStopsListWithGroup();
        if (null == stopsListWithGroup || stopsListWithGroup.isEmpty()) {
            return null;
        }
        // The call is successful, empty the "StopsComponentGroup" and "StopsComponent" message and insert
        int deleteGroup = stopsComponentDomain.deleteStopsComponentGroup();
        logger.debug("Successful deletion Group" + deleteGroup + "piece of data!!!");
        int deleteStopsInfo = stopsComponentDomain.deleteStopsComponent();
        logger.info("Successful deletion StopsInfo" + deleteStopsInfo + "piece of data!!!");

        int addStopsComponentGroupRows = 0;
        // StopsComponent bundle list
        List<String> stopsBundleList = new ArrayList<>();
        // Loop stopsListWithGroup
        for (String groupName : stopsListWithGroup.keySet()) {
            if (StringUtils.isBlank(groupName)) {
                continue;
            }
            // add group info
            StopsComponentGroup stopsComponentGroup = StopsComponentGroupUtils.stopsComponentGroupNewNoId(currentUser);
            stopsComponentGroup.setId(UUIDUtils.getUUID32());
            stopsComponentGroup.setGroupName(groupName);
            addStopsComponentGroupRows += stopsComponentDomain.addStopsComponentGroup(stopsComponentGroup);
            // get current group stops bundle list
            List<String> list = stopsListWithGroup.get(groupName);
            stopsBundleList.addAll(list);
        }
        logger.debug("Successful insert Group" + addStopsComponentGroupRows + "piece of data!!!");
        // Deduplication
        HashSet<String> stopsBundleListDeduplication = new HashSet<String>(stopsBundleList);
        stopsBundleList.clear();
        stopsBundleList.addAll(stopsBundleListDeduplication);
        return stopsBundleList;
    }

    private Boolean loadStop(String stopListInfos) {
        logger.info("Now the call is：" + stopListInfos);
        ThirdStopsComponentVo thirdStopsComponentVo = stopImpl.getStopInfo(stopListInfos);
        if (null == thirdStopsComponentVo) {
            logger.warn("bundle:" + stopListInfos + " is not data");
            return false;
        }
        List<String> list = Arrays.asList(thirdStopsComponentVo.getGroups().split(","));
        // Query group information according to groupName in stops
        List<StopsComponentGroup> stopGroupByName = stopsComponentDomain.getStopGroupByNameList(list);
        StopsComponent stopsComponent = StopsComponentUtils.thirdStopsComponentVoToStopsTemplate("init", thirdStopsComponentVo, stopGroupByName);
        if (null != stopsComponent) {
            int insertStopsTemplate = stopsComponentDomain.addStopsComponentAndChildren(stopsComponent);
            logger.info("flow_stops_template affects the number of rows : " + insertStopsTemplate);
            logger.info("=============================association_groups_stops_template=====start==================");
            List<StopsComponentGroup> stopGroupList = stopsComponent.getStopGroupList();
            for (StopsComponentGroup stopGroup : stopGroupList) {
                String stopGroupId = stopGroup.getId();
                String stopsTemplateId = stopsComponent.getId();
                int insertAssociationGroupsStopsTemplate = stopsComponentDomain.insertAssociationGroupsStopsTemplate(stopGroupId, stopsTemplateId);
                logger.info("association_groups_stops_template Association table insertion affects the number of rows : " + insertAssociationGroupsStopsTemplate);
            }
        }
        return true;
    }

    private Boolean addSysInitRecordsAndSave() {
        SysInitRecords sysInitRecords = new SysInitRecords();
        sysInitRecords.setId(UUIDUtils.getUUID32());
        sysInitRecords.setInitDate(new Date());
        sysInitRecords.setIsSucceed(true);
        sysInitRecordsDomain.insertSysInitRecords(sysInitRecords);
        SysParamsCache.setIsBootComplete(true);
        return true;
    }

    private void syncStopsProperties(Stops stops, String currentUser) throws IllegalAccessException, ClassNotFoundException {
        if (null == stops) {
            return;
        }
        String bundle = stops.getBundel();
        StopsComponent stopsComponentByBundle = stopsComponentDomain.getStopsComponentByBundle(bundle);
        if (null == stopsComponentByBundle) {
            logger.info("The Stops component (" + bundle + ") has been deleted");
            return;
        }
        // propertiesTemplate to map
        List<StopsComponentProperty> propertiesTemplate = stopsComponentByBundle.getProperties();
        Map<String, StopsComponentProperty> propertiesTemplateMap = new HashMap<>();
        if (null != propertiesTemplate && propertiesTemplate.size() > 0) {
            for (StopsComponentProperty stopsComponentProperty : propertiesTemplate) {
                if (null == stopsComponentProperty) {
                    continue;
                }
                propertiesTemplateMap.put(stopsComponentProperty.getName(), stopsComponentProperty);
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
                StopsComponentProperty stopsComponentProperty = propertiesTemplateMap.get(property.getName());
                if (null == stopsComponentProperty) {
                    property.setIsOldData(true);
                    stopsDomain.updateStopsProperty(property);
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
                StopsComponentProperty stopsComponentProperty = propertiesTemplateMap.get(key);
                Property property = PropertyUtils.propertyNewNoId(currentUser);
                BeanUtils.copyProperties(stopsComponentProperty, property);
                property.setId(UUIDUtils.getUUID32());
                property.setStops(stops);
                property.setCustomValue(stopsComponentProperty.getDefaultValue());
                //Indicates "select"
                if (stopsComponentProperty.getAllowableValues().contains(",") && stopsComponentProperty.getAllowableValues().length() > 4) {
                    property.setIsSelect(true);
                    //Determine if there is a default value in "select"
                    if (!stopsComponentProperty.getAllowableValues().contains(stopsComponentProperty.getDefaultValue())) {
                        //Default value if not present
                        property.setCustomValue("");
                    }
                } else {
                    property.setIsSelect(false);
                }
                addProperties.add(property);
            }
            stops.setProperties(properties);
            stopsDomain.addPropertyList(addProperties);
        }
    }

}
