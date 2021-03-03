package cn.cnic.component.sparkJar.service.impl;

import cn.cnic.base.util.*;
import cn.cnic.common.Eunm.SparkJarState;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.sparkJar.mapper.SparkJarMapper;
import cn.cnic.component.sparkJar.model.SparkJarComponent;
import cn.cnic.component.sparkJar.service.ISparkJarService;
import cn.cnic.component.sparkJar.utils.SparkJarUtils;
import cn.cnic.third.service.ISparkJar;
import cn.cnic.third.vo.sparkJar.SparkJarVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class SparkJarServiceImpl implements ISparkJarService {

    Logger logger = LoggerUtil.getLogger();


    @Resource
    private SparkJarMapper sparkJarMapper;

    @Resource
    private ISparkJar sparkJarImpl;


    @Override
    public String uploadSparkJarFile(String username, MultipartFile file) {

        //call piflow server api: sparkJar/path
        String sparkJarPath = sparkJarImpl.getSparkJarPath();

        //upload jar file to sparkJar path
        String sparkJarName = file.getOriginalFilename();

        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (file.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Upload failed, please try again later");
        }
        Map<String, Object> uploadMap = FileUtils.uploadRtnMap(file, sparkJarPath, sparkJarName);
        if (null == uploadMap || uploadMap.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Upload failed, please try again later");
        }
        Integer code = (Integer) uploadMap.get("code");
        if (500 == code) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("failed to upload file");
        }

        //save stopsHub to db
        SparkJarComponent sparkJarComponent = SparkJarUtils.sparkJarNewNoId(username);
        sparkJarComponent.setId(UUIDUtils.getUUID32());
        sparkJarComponent.setJarName(sparkJarName);
        sparkJarComponent.setJarUrl(sparkJarPath + sparkJarName);
        sparkJarComponent.setStatus(SparkJarState.UNMOUNT);
        sparkJarMapper.addSparkJarComponent(sparkJarComponent);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("successful jar upload");
    }

    @Override
    @Transactional
    public String mountSparkJar(String username, Boolean isAdmin, String id) {

        SparkJarComponent sparkJarComponent = sparkJarMapper.getSparkJarById(username, isAdmin, id);

        SparkJarVo sparkJarVo = sparkJarImpl.mountSparkJar(sparkJarComponent.getJarName());
        if (sparkJarVo == null) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Mount failed, please try again later");
        }

        sparkJarComponent.setMountId(sparkJarVo.getMountId());
        sparkJarComponent.setStatus(SparkJarState.MOUNT);
        sparkJarComponent.setLastUpdateUser(username);
        sparkJarComponent.setLastUpdateDttm(new Date());
        sparkJarMapper.updateSparkJarComponent(sparkJarComponent);

        return ReturnMapUtils.setSucceededMsgRtnJsonStr("Mount successful");
    }

    @Override
    @Transactional
    public String unmountSparkJar(String username, Boolean isAdmin, String id) {

        SparkJarComponent sparkJarComponent = sparkJarMapper.getSparkJarById(username, isAdmin, id);
        if (sparkJarComponent.getStatus() == SparkJarState.UNMOUNT) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Spark jar have been UNMounted already!");
        }

        SparkJarVo sparkJarVo = sparkJarImpl.unmountSparkJar(sparkJarComponent.getMountId());
        if (sparkJarVo == null) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("UNMount failed, please try again later");
        }
        sparkJarComponent.setMountId(sparkJarVo.getMountId());
        sparkJarComponent.setStatus(SparkJarState.UNMOUNT);
        sparkJarComponent.setLastUpdateUser(username);
        sparkJarComponent.setLastUpdateDttm(new Date());
        sparkJarMapper.updateSparkJarComponent(sparkJarComponent);


        return ReturnMapUtils.setSucceededMsgRtnJsonStr("Unmount successful");
    }

    /**
     * sparkJarListPage
     *
     * @param username username
     * @param isAdmin  is admin
     * @param pageNo   Number of pages
     * @param limit    Number each page
     * @param param    Search content
     * @return
     */
    public String sparkJarListPage(String username, Boolean isAdmin, Integer pageNo, Integer limit, String param) {
        if (null == pageNo || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(ReturnMapUtils.ERROR_MSG);
        }
        Page<Process> page = PageHelper.startPage(pageNo, limit, "crt_dttm desc");
        sparkJarMapper.getSparkJarListParam(username, isAdmin, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);
        rtnMap = PageHelperUtils.setLayTableParam(page, rtnMap);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * del spark jar
     *
     * @param username username
     * @param id       id
     * @return json
     */
    public String delSparkJar(String username, Boolean isAdmin, String id) {
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is null");
        }
        SparkJarComponent sparkJarComponent = sparkJarMapper.getSparkJarById(username, isAdmin, id);
        if (null == sparkJarComponent) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("no data");
        }
        SparkJarState status = sparkJarComponent.getStatus();
        if (SparkJarState.MOUNT == status) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The status is MOUNT and deletion is prohibited ");
        }
        int i = sparkJarMapper.deleteSparkJarById(username, id);
        if (i <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("delete failed");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(ReturnMapUtils.SUCCEEDED_MSG);
    }

}
