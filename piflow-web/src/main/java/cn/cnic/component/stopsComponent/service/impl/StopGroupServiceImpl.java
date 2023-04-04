package cn.cnic.component.stopsComponent.service.impl;

import java.util.*;

import cn.cnic.common.Eunm.ComponentFileType;
import cn.cnic.component.dataSource.domain.DataSourceDomain;
import cn.cnic.component.stopsComponent.vo.StopsComponentVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.component.stopsComponent.domain.StopsComponentDomain;
import cn.cnic.component.stopsComponent.domain.StopsComponentGroupDomain;
import cn.cnic.component.stopsComponent.entity.StopsComponent;
import cn.cnic.component.stopsComponent.entity.StopsComponentGroup;
import cn.cnic.component.stopsComponent.entity.StopsComponentProperty;
import cn.cnic.component.stopsComponent.service.IStopGroupService;
import cn.cnic.component.stopsComponent.utils.StopsComponentGroupUtils;
import cn.cnic.component.stopsComponent.utils.StopsComponentUtils;
import cn.cnic.component.stopsComponent.vo.PropertyTemplateVo;
import cn.cnic.component.stopsComponent.vo.StopGroupVo;
import cn.cnic.component.stopsComponent.vo.StopsComponentGroupVo;
import cn.cnic.component.stopsComponent.vo.StopsTemplateVo;
import cn.cnic.third.service.IStop;
import cn.cnic.third.vo.stop.ThirdStopsComponentVo;


@Service
public class StopGroupServiceImpl implements IStopGroupService {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    private final IStop stopImpl;
    private final StopsComponentDomain stopsComponentDomain;
    private final StopsComponentGroupDomain stopsComponentGroupDomain;
    private final DataSourceDomain dataSourceDomain;

    @Autowired
    public StopGroupServiceImpl(IStop stopImpl,
                                StopsComponentDomain stopsComponentDomain,
                                StopsComponentGroupDomain stopsComponentGroupDomain, DataSourceDomain dataSourceDomain) {
        this.stopImpl = stopImpl;
        this.stopsComponentDomain = stopsComponentDomain;
        this.stopsComponentGroupDomain = stopsComponentGroupDomain;
        this.dataSourceDomain = dataSourceDomain;
    }


    /**
     * Query all groups and all stops under it
     *
     * @return StopGroupVo list
     */
    @Override
    public List<StopGroupVo> getStopGroupAll() {
        List<StopsComponentGroup> stopGroupList = stopsComponentDomain.getStopGroupList();
        if (null == stopGroupList || stopGroupList.size() <= 0) {
            return null;
        }
        List<StopGroupVo> stopGroupVoList = new ArrayList<>();
        for (StopsComponentGroup stopGroup : stopGroupList) {
            if (null == stopGroup) {
                continue;
            }
            List<StopsComponent> stopsComponentList = stopGroup.getStopsComponentList();
            if (null == stopsComponentList || stopsComponentList.size() <= 0) {
                continue;
            }
            StopGroupVo stopGroupVo = new StopGroupVo();
            BeanUtils.copyProperties(stopGroup, stopGroupVo);

            List<StopsTemplateVo> stopsTemplateVoList = new ArrayList<>();
            for (StopsComponent stopsComponent : stopsComponentList) {
                if (null == stopsComponent) {
                    continue;
                }
                StopsTemplateVo stopsTemplateVo = new StopsTemplateVo();
                BeanUtils.copyProperties(stopsComponent, stopsTemplateVo);
                List<StopsComponentProperty> properties = stopsComponent.getProperties();
                if (null != properties && properties.size() > 0) {
                    List<PropertyTemplateVo> propertiesVo = new ArrayList<>();
                    for (StopsComponentProperty stopsComponentProperty : properties) {
                        if (null == propertiesVo) {
                            continue;
                        }
                        PropertyTemplateVo propertyTemplateVo = new PropertyTemplateVo();
                        BeanUtils.copyProperties(stopsComponentProperty, propertyTemplateVo);
                        propertiesVo.add(propertyTemplateVo);
                    }
                    stopsTemplateVo.setPropertiesVo(propertiesVo);
                }
                stopsTemplateVoList.add(stopsTemplateVo);
            }
            stopGroupVo.setStopsTemplateVoList(stopsTemplateVoList);

            stopGroupVoList.add(stopGroupVo);
        }
        return stopGroupVoList;
    }

    @Override
    public void updateGroupAndStopsListByServer(String username) {
        //get all default and scala components
        Map<String, List<String>> stopsListWithGroup = stopImpl.getStopsListWithGroup();
        if (null == stopsListWithGroup || stopsListWithGroup.isEmpty()) {
            return;
        }
        /*1.get all default components and delete
         *2.
         * */
        List<StopsComponent> systemDefaultStops = stopsComponentDomain.getSystemDefaultStops();
        if (systemDefaultStops !=null && systemDefaultStops.size()>0){
            for (StopsComponent systemDefaultStop : systemDefaultStops) {
                stopsComponentDomain.deleteStopsComponent(systemDefaultStop);
            }
        }

        int addStopsComponentGroupRows = 0;
        // StopsComponent bundle list
        List<String> stopsBundleList = new ArrayList<>();
        // Loop stopsListWithGroup
        //compare server groups and old groups
        Map<String, StopsComponentGroup> stopsComponentGroupMap = new HashMap<>();
        //get old groups
        List<StopsComponentGroup> stopsComponentGroupList = stopsComponentDomain.getStopGroupByGroupNameList(new ArrayList<String>(stopsListWithGroup.keySet()));
        for (StopsComponentGroup sGroup : stopsComponentGroupList) {
            stopsComponentGroupMap.put(sGroup.getGroupName(), sGroup);
        }
        for (String groupName : stopsListWithGroup.keySet()) {
            if (StringUtils.isBlank(groupName)) {
                continue;
            }
            StopsComponentGroup stopsComponentGroup = stopsComponentGroupMap.get(groupName);
            //insert new group
            if (stopsComponentGroup == null) {
                stopsComponentGroup = StopsComponentGroupUtils.stopsComponentGroupNewNoId(username);
                stopsComponentGroup.setId(UUIDUtils.getUUID32());
                stopsComponentGroup.setGroupName(groupName);
                addStopsComponentGroupRows += stopsComponentDomain.addStopsComponentGroup(stopsComponentGroup);
            }
            // add group info
            // get current group stops bundle list
            List<String> list = stopsListWithGroup.get(groupName);
            stopsBundleList.addAll(list);
        }
        logger.debug("Successful insert Group" + addStopsComponentGroupRows + "piece of data!!!");

        // Determine if it is empty
        if (stopsBundleList.isEmpty()) {
            return;
        }
        // Deduplication
        HashSet<String> stopsBundleListDeduplication = new HashSet<String>(stopsBundleList);
        stopsBundleList.clear();
        stopsBundleList.addAll(stopsBundleListDeduplication);
        int updateStopsComponentNum = 0;
        for (String bundle : stopsBundleList) {

            if (StringUtils.isBlank(bundle))
                // 2.First query "stopInfo" according to "bundle"
                logger.info("Now the call is：" + bundle);

            StopsComponent ifExistStop = stopsComponentDomain.getOnlyStopsComponentByBundle(bundle);
            //new default component,insert
            if (ifExistStop == null){
                ThirdStopsComponentVo thirdStopsComponentVo = stopImpl.getStopInfo(bundle);

                if (null == thirdStopsComponentVo) {
                    logger.warn("bundle:" + bundle + " is not data");
                    continue;
                }
                List<String> list = Arrays.asList(thirdStopsComponentVo.getGroups().split(","));
                // Query group information according to groupName in stops
                List<StopsComponentGroup> stopGroupByName = stopsComponentDomain.getStopGroupByNameList(list);
                StopsComponent stopsComponent = StopsComponentUtils.thirdStopsComponentVoToStopsTemplate(username, thirdStopsComponentVo, stopGroupByName);
                if (null == stopsComponent) {
                    continue;
                }
                stopsComponent.setComponentType(ComponentFileType.DEFAULT);
                stopsComponentDomain.addStopsComponentAndChildren(stopsComponent);
                logger.debug("=============================association_groups_stops_template=====start==================");
                stopsComponent.getStopGroupList();
                stopsComponentDomain.stopsComponentLinkStopsComponentGroupList(stopsComponent, stopsComponent.getStopGroupList());
                updateStopsComponentNum++;
            }else{
                logger.info("==========component exists,just skip,bundle is "+bundle+"method is reload stops==========");
            }
        }
//        //查询出所有dataSource中不为空且去重后的stops_template_bundle
//        List<String> dataSourceStopTemplateBundleList = dataSourceDomain.getAllStopDataSourceBundle();
//        if (dataSourceStopTemplateBundleList != null && dataSourceStopTemplateBundleList.size() >0 ){
//            for (String stopsTemplateBundle:dataSourceStopTemplateBundleList){
//                //1.根据stopsTemplateBundle查询stop组件(返回的只有stopsComponent表中字段,无其他任何多余字段)
//                StopsComponent stopsComponent = stopsComponentDomain.getOnlyStopsComponentByBundle(stopsTemplateBundle);
//                //2. 如果返回值不为空且为数据源组件,那么把对应stopDataSource全部改为可用
//                if (stopsComponent != null && stopsComponent.getIsDataSource()) {
//                    dataSourceDomain.updateDataSourceIsAvailableByBundle(stopsTemplateBundle,1);
//                    //修改一次图片地址：这里之所以没在上面的修改是否可用中修改图片地址,
//                    // 单独写个方法,虽然会多一次连接,为了和修改是否可用功能分开
//                    dataSourceDomain.updateDataSourceImageUrlByBundle(stopsTemplateBundle,stopsComponent.getImageUrl());
//                } else {
//                    //3.反之,改为不可用
//                    dataSourceDomain.updateDataSourceIsAvailableByBundle(stopsTemplateBundle,0);
//                }
//            }
//        }
        logger.info("update StopsComponent Num :" + updateStopsComponentNum);
    }

    @Override
    public String stopsComponentList(String username, boolean isAdmin) {
        if (!isAdmin) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Permission error");
        }
        List<StopsComponentGroupVo> stopGroupList = stopsComponentGroupDomain.getManageStopGroupList();
        for (StopsComponentGroupVo stopsComponentGroupVo : stopGroupList) {
            List<StopsComponentVo> stopsComponentVoList = stopsComponentGroupVo.getStopsComponentVoList();
            for (StopsComponentVo stopsComponentVo : stopsComponentVoList) {
                stopsComponentVo.setGroups(stopsComponentGroupVo.getGroupName());
            }
            stopsComponentGroupVo.setStopsComponentVoList(stopsComponentVoList);
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("stopGroupList", stopGroupList);
    }

}
