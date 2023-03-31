package cn.cnic.component.stopsComponent.service.impl;

import cn.cnic.base.utils.*;
import cn.cnic.common.Eunm.ComponentFileType;
import cn.cnic.common.Eunm.StopsHubState;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.stopsComponent.domain.StopsComponentDomain;
import cn.cnic.component.stopsComponent.domain.StopsHubDomain;
import cn.cnic.component.stopsComponent.domain.StopsHubFileRecordDomain;
import cn.cnic.component.stopsComponent.entity.StopsComponent;
import cn.cnic.component.stopsComponent.entity.StopsComponentGroup;
import cn.cnic.component.stopsComponent.entity.StopsHub;
import cn.cnic.component.stopsComponent.entity.StopsHubFileRecord;
import cn.cnic.component.stopsComponent.service.IStopsHubService;
import cn.cnic.component.stopsComponent.utils.StopsComponentGroupUtils;
import cn.cnic.component.stopsComponent.utils.StopsComponentUtils;
import cn.cnic.component.stopsComponent.utils.StopsHubUtils;
import cn.cnic.component.stopsComponent.vo.StopsHubInfoVo;
import cn.cnic.component.system.domain.SysUserDomain;
import cn.cnic.component.system.entity.SysUser;
import cn.cnic.third.market.service.IMarket;
import cn.cnic.third.service.IStop;
import cn.cnic.third.vo.stop.StopsHubVo;
import cn.cnic.third.vo.stop.ThirdStopsComponentVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class StopsHubServiceImpl implements IStopsHubService {
    private Logger logger = LoggerUtil.getLogger();

    private final StopsComponentDomain stopsComponentDomain;
    private final StopsHubDomain stopsHubDomain;
    private final SysUserDomain sysUserDomain;
    private final IStop stopImpl;
    private final IMarket marketImpl;
    private final StopsHubFileRecordDomain stopsHubFileRecordDomain;

    @Autowired
    public StopsHubServiceImpl(StopsComponentDomain stopsComponentDomain,
                               StopsHubDomain stopsHubDomain,
                               SysUserDomain sysUserDomain, IStop stopImpl, IMarket marketImpl, StopsHubFileRecordDomain stopsHubFileRecordDomain) {
        this.stopsComponentDomain = stopsComponentDomain;
        this.stopsHubDomain = stopsHubDomain;
        this.sysUserDomain = sysUserDomain;
        this.stopImpl = stopImpl;
        this.marketImpl = marketImpl;
        this.stopsHubFileRecordDomain = stopsHubFileRecordDomain;
    }

    /**
     *
     * @param username
     * @param file
     * @param type Component type:Python/Scala
     * @param languageVersion
     * @return
     */
    @Override
    public String uploadStopsHubFile(String username, MultipartFile file, String type, String languageVersion) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (file.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Upload failed, please try again later");
        }

        //call piflow server api: plunin/path
        String stopsHubPath = stopImpl.getStopsHubPath();
        if (!stopsHubPath.endsWith("/")){
            stopsHubPath = stopsHubPath+"/";
        }
        //upload jar file to plugin path
        String stopsHubName = file.getOriginalFilename();
        ComponentFileType fileType = ComponentFileType.selectGender(type);
        if (fileType ==null){
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Please select a file type or an incorrect file type");
        }

        Map<String, Object> uploadMap = FileUtils.uploadRtnMap(file, stopsHubPath, stopsHubName);
        if (null == uploadMap || uploadMap.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPLOAD_FAILED_MSG());
        }
        Integer code = (Integer) uploadMap.get("code");
        if (500 == code) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("failed to upload file");
        }

        //save stopsHub to db
        StopsHub stopsHub = StopsHubUtils.stopsHubNewNoId(username);
        stopsHub.setId(UUIDUtils.getUUID32());
        stopsHub.setJarName(stopsHubName);
        stopsHub.setJarUrl(stopsHubPath + stopsHubName);
        stopsHub.setStatus(StopsHubState.UNMOUNT);
        stopsHub.setType(fileType);
        stopsHub.setLanguageVersion(languageVersion);
        stopsHubDomain.addStopHub(stopsHub);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("successful jar upload");
    }

    @Override
    public String mountStopsHub(String username, Boolean isAdmin, String id) {

        StopsHub stopsHub = stopsHubDomain.getStopsHubById(username, isAdmin, id);
//        if(stopsHub.getStatus() == StopsHubState.MOUNT){
//            return ReturnMapUtils.setFailedMsgRtnJsonStr("StopsHub have been Mounted already!");
//        }
        if (stopsHub == null ){
            return ReturnMapUtils.setFailedMsgRtnJsonStr("no data");
        }else if (stopsHub.getType() == null){
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Data type error,Delete it and upload it again or contact the administrator");
        }

        switch (stopsHub.getType()){
            case SCALA:
                //mount scala stopsHub, parse on server
                return mountScalaStopsHub(stopsHub,username);
            case PYTHON:
                //mount python stopsHub,parse on web
                return mountPythonStopsZip(stopsHub,username);
            default:
                return ReturnMapUtils.setFailedMsgRtnJsonStr("Data type error,Delete it and upload it again or contact the administrator");
        }
    }

    /**
     * mount scala.jar
     * @param stopsHub
     * @param username
     * @data 2022-02-03
     * @author leilei
     * @return
     */
    private String mountScalaStopsHub(StopsHub stopsHub, String username){
        StopsHubVo stopsHubVo = stopImpl.mountStopsHub(stopsHub.getJarName());
        if (stopsHubVo == null) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Mount failed, please try again later");
        }


        //TODO: remove stops and groups from db
        List<ThirdStopsComponentVo> stops = stopsHubVo.getStops();
        List<String> groupNameList = new ArrayList<>();
        Map<String, StopsComponentGroup> stopsComponentGroupMap = new HashMap<>();
        for (ThirdStopsComponentVo s : stops) {
            groupNameList.addAll(Arrays.asList(s.getGroups().split(",")));
        }
        List<String> distinctGroupNameList = groupNameList.stream().distinct().collect(Collectors.toList());
        List<StopsComponentGroup> stopsComponentGroupList = stopsComponentDomain.getStopGroupByGroupNameList(distinctGroupNameList);
        for (StopsComponentGroup sGroup : stopsComponentGroupList) {
            stopsComponentGroupMap.put(sGroup.getGroupName(), sGroup);
        }
        StringBuffer bundles = new StringBuffer();
        for (ThirdStopsComponentVo s : stops) {

            List<String> stopGroupNameList = Arrays.asList(s.getGroups().split(","));
            for (String groupName : stopGroupNameList) {

                StopsComponentGroup stopsComponentGroup = stopsComponentGroupMap.get(groupName);
                if (stopsComponentGroup == null) {
                    // add group into db
                    stopsComponentGroup = StopsComponentGroupUtils.stopsComponentGroupNewNoId(username);
                    stopsComponentGroup.setId(UUIDUtils.getUUID32());
                    stopsComponentGroup.setGroupName(groupName);
                    stopsComponentDomain.addStopsComponentGroup(stopsComponentGroup);
                    stopsComponentGroupMap.put(groupName, stopsComponentGroup);
                }
            }

            //add stop into db
            List<StopsComponentGroup> stopGroupByName = new ArrayList<>();
            for (String groupName : stopGroupNameList) {
                stopGroupByName.add(stopsComponentGroupMap.get(groupName));
            }
            StopsComponent stopsComponent = stopsComponentDomain.getStopsComponentByBundle(s.getBundle());
            if (stopsComponent == null) {
                stopsComponent = StopsComponentUtils.thirdStopsComponentVoToStopsTemplate(username, s, stopGroupByName);
                stopsComponent.setComponentType(ComponentFileType.SCALA);
                stopsComponent.setStopsHubId(stopsHub.getId());
                stopsComponentDomain.addStopsComponentAndChildren(stopsComponent);
                //stopsComponentMapper.insertStopsComponent(stopsComponent);

            } else {//update stop group
                //stopsComponent.setStopGroupList(stopGroupByName);
                //TODO: Update group info
                logger.info("bundle is already exists, bundle name is "+s.getBundle());
            }
            //add stop and group relationship
            for (StopsComponentGroup sGroup : stopGroupByName) {
                stopsComponentDomain.deleteGroupCorrelationByGroupIdAndStopId(sGroup.getId(), stopsComponent.getId());
                stopsComponentDomain.insertAssociationGroupsStopsTemplate(sGroup.getId(), stopsComponent.getId());
            }
            if (bundles.length() > 0) {
                bundles.append(",");
            }
            bundles.append(s.getBundle());
        }

        stopsHub.setMountId(stopsHubVo.getMountId());
        stopsHub.setStatus(StopsHubState.MOUNT);
        stopsHub.setLastUpdateUser(username);
        stopsHub.setLastUpdateDttm(new Date());
        stopsHub.setBundles(bundles.toString());
        stopsHubDomain.updateStopHub(stopsHub);

        return ReturnMapUtils.setSucceededMsgRtnJsonStr("Mount successful");

    }


    /**
     * mount python.zip
     * @param stopsHub 算法包
     * @param username
     * @data 2022-02-03
     * @author leilei
     * @return
     */
    private String mountPythonStopsZip(StopsHub stopsHub, String username){
        if (StringUtils.isNotEmpty(stopsHub.getJarUrl()) && StringUtils.isNotBlank(stopsHub.getJarName())) {
            List<StopsHubFileRecord> insertList = new ArrayList<>();
            String jarName = stopsHub.getJarName();
            try {
                FileInputStream input = new FileInputStream(stopsHub.getJarUrl());
                ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(input), Charset.forName("GBK"));

                ZipEntry zipEntry = null;
                String dockerImagesName = null;   //下面通过代码打docker镜像时,重新赋值,用于 算法包的具体文件记录字段
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    if (zipEntry.isDirectory()) {
                        continue;
                    } else {
                        String zipEntryFileName = zipEntry.getName();
                        if (zipEntryFileName.endsWith(".py")) {
                            StopsHubFileRecord stopsHubFileRecord = new StopsHubFileRecord();
                            stopsHubFileRecord.setId(UUIDUtils.getUUID32());
                            String fileName = zipEntryFileName.contains("/") ? zipEntryFileName.substring(zipEntryFileName.lastIndexOf("/") + 1) : zipEntryFileName;
                            String stopName = fileName.endsWith(".py")? fileName.substring(0,fileName.length()-3):fileName;
                            stopsHubFileRecord.setFileName(stopName);
                            stopsHubFileRecord.setFilePath(zipEntryFileName);
                            stopsHubFileRecord.setStopsHubId(stopsHub.getId());
                            insertList.add(stopsHubFileRecord);
                        } else if (zipEntryFileName.endsWith("requirements.txt")) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(zipInputStream));
                            StringBuffer dockerFileSb = new StringBuffer();
                            dockerFileSb.append("FROM python:" + stopsHub.getLanguageVersion() + System.lineSeparator());
                            dockerFileSb.append("MAINTAINER " + jarName + System.lineSeparator());
                            dockerFileSb.append("COPY " + stopsHub.getJarUrl() + " /usr/local" + System.lineSeparator());
                            dockerFileSb.append("RUN apt update" + System.lineSeparator());
                            dockerFileSb.append("&& apt-get install -y zip" + System.lineSeparator());
                            dockerFileSb.append("RUN set -ex" + System.lineSeparator());
                            dockerFileSb.append("&& mkdir -p /pythonDir" + System.lineSeparator());
                            dockerFileSb.append("&& unzip /usr/local/"+ jarName +" -d /pythonDir/" + System.lineSeparator());
                            String line;
                            while ((line = br.readLine()) != null) {
                                //之前python的依赖,有个whl结尾的离线安装包,安装命令有所不同,所以这里用if-else判断下
                                if (line.endsWith(".whl")) {
                                    dockerFileSb.append("&& pip install " + line + System.lineSeparator());
                                } else {
                                    dockerFileSb.append("&& pip install -i https://mirrors.aliyun.com/pypi/simple/ " + line + System.lineSeparator());
                                }
                            }
                            dockerFileSb.append("&& rm -rf  ~/.cache/pip/*" + System.lineSeparator());
                            dockerFileSb.append("&& rm -rf /usr/local/"+jarName + System.lineSeparator());
                            //write dockerfile
                            String stopsHubPath = stopImpl.getStopsHubPath();
                            String dockerFileSavePath = stopsHubPath +"/dockerFile/DockerFile-" + stopsHub.getId();
                            FileUtils.writeData(dockerFileSavePath, dockerFileSb.toString());
                            //TODO create docker images and push



                        }
                    }
                }
                //add records
                if (insertList.size() > 0) {
                    for (StopsHubFileRecord stopsHubFileRecord : insertList) {
                        stopsHubFileRecord.setDockerImagesName(dockerImagesName);
                        stopsHubFileRecordDomain.addStopsHubFileRecord(stopsHubFileRecord);
                    }
                }

                stopsHub.setStatus(StopsHubState.MOUNT);
                stopsHub.setLastUpdateUser(username);
                stopsHub.setLastUpdateDttm(new Date());
                stopsHubDomain.updateStopHub(stopsHub);

                //close stream
                input.close();
                zipInputStream.close();
            } catch (Exception e) {
                logger.error("init PythonStopsComponent error,error message:" + e.getMessage(), e);
                return ReturnMapUtils.setFailedMsgRtnJsonStr("Mount failed, please try again later");
            }
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Mount failed, please try again later");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("Mount successful");
    }

    @Override
    public String unmountStopsHub(String username, Boolean isAdmin, String id) {

        StopsHub stopsHub = stopsHubDomain.getStopsHubById(username, isAdmin, id);
        if (stopsHub.getStatus() == StopsHubState.UNMOUNT) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("StopsHub have been UNMounted already!");
        }

        switch (stopsHub.getType()) {
            case SCALA:
                //unmount scala.jar
                return unMountScalaStopsHub(stopsHub, username);
            case PYTHON:
                //unmount python.zip
                return unMountPythonStopsZip(stopsHub, username);
            default:
                return ReturnMapUtils.setFailedMsgRtnJsonStr("UNMount failed, Data type error, contact the administrator");
        }
    }
    /**
     * @Description unmount scala.jar

     * @Param stopsHub
     * @Param username

     * @Return java.lang.String
     * @Author TY
     * @Date 17:03 2023/3/30
     **/

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public String unMountScalaStopsHub(StopsHub stopsHub,String username){
        StopsHubVo stopsHubVo = stopImpl.unmountStopsHub(stopsHub.getMountId());
        if (stopsHubVo == null) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("UNMount failed, please try again later");
        }

        //remove stops and groups into db,
        List<ThirdStopsComponentVo> stops = stopsHubVo.getStops();
        for (ThirdStopsComponentVo s : stops) {
            StopsComponent stopsComponent = stopsComponentDomain.getStopsComponentByBundle(s.getBundle());
            if (stopsComponent !=null){
                stopsComponentDomain.deleteStopsComponent(stopsComponent);
            }
        }

        stopsHub.setMountId(stopsHubVo.getMountId());
        stopsHub.setStatus(StopsHubState.UNMOUNT);
        stopsHub.setLastUpdateUser(username);
        stopsHub.setLastUpdateDttm(new Date());
        stopsHubDomain.updateStopHub(stopsHub);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("UNMount successful");
    }

    /**
     * unMount python.zip
     * @param stopsHub
     * @param username
     * @data 2022-02-03
     * @author leilei
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public String unMountPythonStopsZip(StopsHub stopsHub, String username){
        //1.search stops_hub_file_record
        List<StopsHubFileRecord> fileRecordList = stopsHubFileRecordDomain.getStopsHubFileRecordByHubId(stopsHub.getId());
        //2.delete flow_stops_template、flow_stops_property_template、flow_stops_groups
        for (StopsHubFileRecord s : fileRecordList) {
            //file path = bundle
            StopsComponent stopsComponent = stopsComponentDomain.getStopsComponentByBundle(s.getFilePath());
            if (stopsComponent !=null){
                stopsComponentDomain.deleteStopsComponent(stopsComponent);
            }
            stopsHubFileRecordDomain.deleteStopsHubFileRecord(s.getId());
        }

//        stopsHub.setMountId(stopsHubVo.getMountId());
        stopsHub.setStatus(StopsHubState.UNMOUNT);
        stopsHub.setLastUpdateUser(username);
        stopsHub.setLastUpdateDttm(new Date());
        stopsHubDomain.updateStopHub(stopsHub);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("UNMount successful");
    }

    /**
     * stopsHubListPage
     *
     * @param username username
     * @param isAdmin  is admin
     * @param pageNo   Number of pages
     * @param limit    Number each page
     * @param param    Search content
     * @return
     */
    @Override
    public String stopsHubListPage(String username, Boolean isAdmin, Integer pageNo, Integer limit, String param) {
        if (null == pageNo || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        Page<Process> page = PageHelper.startPage(pageNo, limit, "crt_dttm desc");
        stopsHubDomain.getStopsHubListParam(username, isAdmin, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
    }

    /**
     * del stopsHub
     *
     * @param username username
     * @param id       id
     * @return json
     */
    @Override
    public String delStopsHub(String username, Boolean isAdmin, String id) {
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        }
        StopsHub stopsHubById = stopsHubDomain.getStopsHubById(username, isAdmin, id);
        if (null == stopsHubById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        StopsHubState status = stopsHubById.getStatus();
        if (StopsHubState.MOUNT == status) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The status is MOUNT and deletion is prohibited ");
        }
        int i = stopsHubDomain.deleteStopsHubById(username, id);
        if (i <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DELETE_ERROR_MSG());
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    /**
     * stopsHub publishing
     *
     * @param username username
     * @param id       id
     * @return json
     */
    @Override
    public String stopsHubPublishing(String username, Boolean isAdmin, String id) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        SysUser user = sysUserDomain.findUserByUserName(username);
        if (StringUtils.isBlank(user.getDeveloperAccessKey())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PLEASE_BIND_THE_DEVELOPER_ACCOUNT_MSG());
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        }
        StopsHub stopsHub = stopsHubDomain.getStopsHubById(username, isAdmin, id);
        if (null == stopsHub) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        if (StopsHubState.MOUNT != stopsHub.getStatus()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        if (StringUtils.isBlank(stopsHub.getBundles())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        String[] bundlesArray = stopsHub.getBundles().split(",");
        if (null == bundlesArray || bundlesArray.length <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        if (bundlesArray.length > 1) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        ThirdStopsComponentVo stopInfo = stopImpl.getStopInfo(bundlesArray[0]);
        if (null == stopInfo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        File file = new File(stopsHub.getJarUrl());
        Map<String, Object> rtnMap = marketImpl.publishComponents(user.getDeveloperAccessKey(), stopInfo.getBundle(), stopInfo.getGroups(), stopInfo.getDescription(), stopInfo.getIcon(), stopInfo.getName(), file);
        String code = rtnMap.get("code").toString();
        if (!"200".equals(code)) {
            return ReturnMapUtils.toJson(rtnMap);
        }
        stopsHub.setIsPublishing(true);
        stopsHubDomain.updateStopHub(stopsHub);
        return ReturnMapUtils.toJson(rtnMap);
    }

    /**
     *
     * @param stopsHubId
     * @return
     */
    @Override
    public String getStopsHubInfoByStopHubId(String username, Boolean isAdmin, String stopsHubId) {
        if (StringUtils.isEmpty(stopsHubId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        } else {
            StopsHub stopsHub = stopsHubDomain.getStopsHubById(username, isAdmin, stopsHubId);
            switch (stopsHub.getType()){
                case PYTHON:
                    //search from stops_hub_file_record
                    List<StopsHubFileRecord> fileRecordList = stopsHubFileRecordDomain.getStopsHubFileRecordByHubId(stopsHubId);
                    //这里根据是否有组件信息,修改isComponent字段的值,然后根据此字段排序,将为true的放在前面,用于页面列表为组件的在前
                    fileRecordList.forEach(a -> {
                        if (a.getStopsComponent() != null) {
                            a.setIsComponent(true);
                        }
                    });
                    fileRecordList.sort(Comparator.comparing(StopsHubFileRecord::getIsComponent).reversed());
                    List<StopsHubInfoVo> list = new ArrayList<>();
                    for (StopsHubFileRecord stopsHubFileRecord : fileRecordList) {
                        StopsHubInfoVo stopsHubInfoVo = new StopsHubInfoVo();
                        stopsHubInfoVo.setId(stopsHubFileRecord.getId());
                        stopsHubInfoVo.setStopName(stopsHubFileRecord.getFileName());
                        stopsHubInfoVo.setStopBundle(stopsHubFileRecord.getFilePath());
                        stopsHubInfoVo.setStopHubId(stopsHubFileRecord.getStopsHubId());
                        list.add(stopsHubInfoVo);
                    }
                    return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", list);
                case SCALA:
                    //search from flow_stops_template
                    List<StopsComponent> stopsComponentByStopsHubId = stopsComponentDomain.getStopsComponentByStopsHubId(stopsHubId);
                    List<StopsHubInfoVo> stopsHubInfoVos = stopsComponentByStopsHubId.stream().map(stop -> {
                        StopsHubInfoVo stopsHubInfoVo = new StopsHubInfoVo();
                        stopsHubInfoVo.setId(stop.getId());
                        stopsHubInfoVo.setStopName(stop.getName());
                        stopsHubInfoVo.setStopBundle(stop.getBundel());
                        stopsHubInfoVo.setStopHubId(stop.getStopsHubId());
                        return stopsHubInfoVo;
                    }).collect(Collectors.toList());
                    return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", stopsHubInfoVos);
                default:
                    return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
            }
        }
    }

}
