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

import cn.cnic.base.utils.*;
import cn.cnic.common.constant.MessageConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private final SysInitRecordsDomain sysInitRecordsDomain;
    private final StopsComponentDomain stopsComponentDomain;
    private final StopsDomain stopsDomain;
    private final IStop stopImpl;

    @Autowired
    public SysInitRecordsServiceImpl(SysInitRecordsDomain sysInitRecordsDomain,
                                     StopsComponentDomain stopsComponentDomain,
                                     StopsDomain stopsDomain,
                                     IStop stopImpl) {
        this.sysInitRecordsDomain = sysInitRecordsDomain;
        this.stopsComponentDomain = stopsComponentDomain;
        this.stopsDomain = stopsDomain;
        this.stopImpl = stopImpl;
    }


    @Override
    public boolean isInBootPage() {
        // Determine if the boot flag is true
        if (SysParamsCache.IS_BOOT_COMPLETE) {
            return false;
        }
        // Query is boot record
        SysInitRecords sysInitRecordsLastNew = sysInitRecordsDomain.getSysInitRecordsLastNew(1);
        if (null != sysInitRecordsLastNew && sysInitRecordsLastNew.getIsSucceed()) {
            return false;
        }
        return true;
    }

    @Override
    public String initComponents(String currentUser) {
        boolean inBootPage = isInBootPage();
        if (!inBootPage) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.INIT_COMPONENTS_COMPLETED_MSG());
        }
        SysParamsCache.INIT_STOP_THREAD_POOL_EXECUTOR = ThreadPoolExecutorUtils.createThreadPoolExecutor(1, 5, 0L);
        List<String> stopsBundleList = loadStopGroup(currentUser);
        if (null == stopsBundleList) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.INTERFACE_CALL_ERROR_MSG());
        }
        if (stopsBundleList.isEmpty()) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.INTERFACE_RETURN_VALUE_IS_NULL_MSG());
        }
        if (null != stopsBundleList && stopsBundleList.size() > 0) {
            for (String stopListInfos : stopsBundleList) {
                SysParamsCache.INIT_STOP_THREAD_POOL_EXECUTOR.execute(() -> {
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
                SysParamsCache.INIT_STOP_THREAD_POOL_EXECUTOR.execute(() -> {
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
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    @Override
    public String threadMonitoring(String currentUser) {
        if (null == SysParamsCache.INIT_STOP_THREAD_POOL_EXECUTOR) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.INIT_COMPONENTS_ERROR_MSG());
        }
        //Total number of threads
        double taskCount = SysParamsCache.INIT_STOP_THREAD_POOL_EXECUTOR.getTaskCount();
        //Number of execution completion threads
        double completedTaskCount = SysParamsCache.INIT_STOP_THREAD_POOL_EXECUTOR.getCompletedTaskCount();
        if (0 == taskCount) {
            taskCount = 1;
            completedTaskCount = 1;
        }
        double progressNum = ((completedTaskCount / taskCount) * 40);
        if (39 < progressNum && progressNum < 40) {
            progressNum = 39;
        }
        double progressNumLong = DecimalFormatUtils.formatTwoDecimalPlaces(progressNum) + 60;

        if (100 == progressNumLong) {
            addSysInitRecordsAndSave();
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("progress", progressNumLong);
    }

    private List<String> loadStopGroup(String currentUser) {
        Map<String, List<String>> stopsListWithGroup = stopImpl.getStopsListWithGroup();
        if (null == stopsListWithGroup) {
            return null;
        }
        if (stopsListWithGroup.isEmpty()) {
            return new ArrayList<>();
        }
        // The call is successful, empty the "StopsComponentGroup" and "StopsComponent" message and insert
        int deleteGroup = stopsComponentDomain.deleteStopsComponentGroup();
        logger.info("Successful deletion Group" + deleteGroup + "piece of data!!!");
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
        logger.info("Successful insert Group" + addStopsComponentGroupRows + "piece of data!!!");
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
        SysParamsCache.INIT_STOP_THREAD_POOL_EXECUTOR.shutdown();
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
                String allowableValues = stopsComponentProperty.getAllowableValues();
                //Indicates "select"
                if (allowableValues.contains(",") && allowableValues.length() > 4) {
                    property.setIsSelect(true);
                    //Determine if there is a default value in "select"
                    if (!allowableValues.contains(stopsComponentProperty.getDefaultValue())) {
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
