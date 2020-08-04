package cn.cnic.component.stopsComponent.service.impl;

import cn.cnic.base.util.FileUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.base.util.UUIDUtils;
import cn.cnic.common.Eunm.StopsHubState;
import cn.cnic.component.stopsComponent.mapper.StopsComponentGroupMapper;
import cn.cnic.component.stopsComponent.mapper.StopsComponentMapper;
import cn.cnic.component.stopsComponent.mapper.StopsHubMapper;
import cn.cnic.component.stopsComponent.model.StopsComponent;
import cn.cnic.component.stopsComponent.model.StopsComponentGroup;
import cn.cnic.component.stopsComponent.model.StopsHub;
import cn.cnic.component.stopsComponent.service.IStopsHubService;
import cn.cnic.component.stopsComponent.transactional.StopsComponentGroupTransactional;
import cn.cnic.component.stopsComponent.transactional.StopsComponentTransactional;
import cn.cnic.component.stopsComponent.utils.StopsComponentGroupUtils;
import cn.cnic.component.stopsComponent.utils.StopsComponentUtils;
import cn.cnic.component.stopsComponent.utils.StopsHubUtils;
import cn.cnic.third.service.IStop;
import cn.cnic.third.vo.stop.StopsHubVo;
import cn.cnic.third.vo.stop.ThirdStopsComponentVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StopsHubServiceImpl implements IStopsHubService {

    Logger logger = LoggerUtil.getLogger();


    @Resource
    private StopsHubMapper stopsHubMapper;

    @Resource
    private IStop stopImpl;

    @Resource
    private StopsComponentMapper stopsComponentMapper;

    @Resource
    private StopsComponentGroupMapper stopsComponentGroupMapper;

    @Resource
    private StopsComponentGroupTransactional stopsComponentGroupTransactional;

    @Resource
    private StopsComponentTransactional stopsComponentTransactional;



    @Override
    public String uploadStopsHubFile(String username, MultipartFile file) {

        //call piflow server api: plunin/path
        String stopsHubPath = stopImpl.getStopsHubPath();

        //upload jar file to plugin path
        String stopsHubName = file.getName();

        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (file.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Upload failed, please try again later");
        }
        Map<String, Object> uploadMap = FileUtils.uploadRtnMap(file, stopsHubPath, stopsHubName);
        if (null == uploadMap || uploadMap.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Upload failed, please try again later");
        }
        Integer code = (Integer) uploadMap.get("code");
        if (500 == code) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("failed to upload file");
        }

        //save stopsHub to db
        StopsHub stopsHub = StopsHubUtils.stopsHubNewNoId(username);
        stopsHub.setId(UUIDUtils.getUUID32());
        stopsHub.setJarName(stopsHubName);
        stopsHub.setJarUrl(stopsHubPath);
        stopsHub.setStatus(StopsHubState.UNMOUNT);
        stopsHubMapper.addStopHub(stopsHub);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("successful jar upload");
    }

    @Override
    public String mountStopsHub(String username,Boolean isAdmin, String id) {

        StopsHub stopsHub = stopsHubMapper.getStopsHubById(username,isAdmin,id);
        if(stopsHub.getStatus() == StopsHubState.MOUNT){
            return ReturnMapUtils.setFailedMsgRtnJsonStr("StopsHub have been Mounted already!");
        }
        StopsHubVo stopsHubVo = stopImpl.mountStopsHub(stopsHub.getJarName());
        if(stopsHubVo == null){
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Mount failed, please try again later");
        }

        stopsHub.setMountId(stopsHubVo.getMountId());
        stopsHub.setStatus(StopsHubState.MOUNT);
        stopsHub.setLastUpdateUser(username);
        stopsHub.setLastUpdateDttm(new Date());
        stopsHubMapper.updateStopHub(stopsHub);

        //TODO: add stops and groups into db
        List<ThirdStopsComponentVo> stops = stopsHubVo.getStops();
        List<String> groupNameList = new ArrayList<>();
        Map<String, StopsComponentGroup> stopsComponentGroupMap = new HashMap<>();
        for(ThirdStopsComponentVo s : stops){
            groupNameList.addAll(Arrays.asList(s.getGroups().split(",")));
        }
        List<String> distinctGroupNameList = groupNameList.stream().distinct().collect(Collectors.toList());
        List<StopsComponentGroup> stopsComponentGroupList = stopsComponentGroupMapper.getStopGroupByGroupNameList(distinctGroupNameList);
        for (StopsComponentGroup sGroup : stopsComponentGroupList){
            stopsComponentGroupMap.put(sGroup.getGroupName(), sGroup);
        }
        for(ThirdStopsComponentVo s : stops){

            List<String> stopGroupNameList = Arrays.asList(s.getGroups().split(","));
            for (String groupName : stopGroupNameList) {

                StopsComponentGroup stopsComponentGroup = stopsComponentGroupMap.get(groupName);
                if(stopsComponentGroup == null){
                    // add group into db
                    stopsComponentGroup = StopsComponentGroupUtils.stopsComponentGroupNewNoId(username);
                    stopsComponentGroup.setId(UUIDUtils.getUUID32());
                    stopsComponentGroup.setGroupName(groupName);
                    stopsComponentGroupTransactional.addStopsComponentGroup(stopsComponentGroup);
                    stopsComponentGroupMap.put(groupName, stopsComponentGroup);
                }
            }

            //add stop into db
            List<StopsComponentGroup> stopGroupByName = new ArrayList<>();
            for (String groupName : stopGroupNameList){
                stopGroupByName.add(stopsComponentGroupMap.get(groupName));
            }
            StopsComponent stopsComponent = stopsComponentMapper.getStopsComponentByBundle(s.getBundle());
            if(stopsComponent == null){
                stopsComponent = StopsComponentUtils.thirdStopsComponentVoToStopsTemplate(username, s, stopGroupByName);
                stopsComponentTransactional.addStopsComponentAndChildren(stopsComponent);
                //stopsComponentMapper.insertStopsComponent(stopsComponent);

            }else{//update stop group
                //stopsComponent.setStopGroupList(stopGroupByName);
                //TODO: Update group info
            }
            //add stop and group relationship
            for(StopsComponentGroup sGroup : stopGroupByName){
                stopsComponentGroupMapper.deleteGroupCorrelationById(sGroup.getId(), stopsComponent.getId());
                stopsComponentGroupMapper.insertAssociationGroupsStopsTemplate(sGroup.getId(), stopsComponent.getId());
            }

        }

        return ReturnMapUtils.setSucceededMsgRtnJsonStr("Mount successful");
    }

    @Override
    public String unmountStopsHub(String username, Boolean isAdmin, String id) {

        StopsHub stopsHub = stopsHubMapper.getStopsHubById(username,isAdmin,id);
        if(stopsHub.getStatus() == StopsHubState.UNMOUNT){
            return ReturnMapUtils.setFailedMsgRtnJsonStr("StopsHub have been UNMounted already!");
        }

        StopsHubVo stopsHubVo = stopImpl.unmountStopsHub(stopsHub.getMountId());
        if(stopsHubVo == null){
            return ReturnMapUtils.setFailedMsgRtnJsonStr("UNMount failed, please try again later");
        }
        stopsHub.setMountId(stopsHubVo.getMountId());
        stopsHub.setStatus(StopsHubState.UNMOUNT);
        stopsHub.setLastUpdateUser(username);
        stopsHub.setLastUpdateDttm(new Date());
        stopsHubMapper.updateStopHub(stopsHub);

        //TODO: remove stops and groups into db,
        List<ThirdStopsComponentVo> stops = stopsHubVo.getStops();

        return null;
    }

}
