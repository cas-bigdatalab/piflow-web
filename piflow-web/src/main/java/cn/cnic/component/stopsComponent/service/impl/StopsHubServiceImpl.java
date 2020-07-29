package cn.cnic.component.stopsComponent.service.impl;

import cn.cnic.base.util.*;
import cn.cnic.common.Eunm.StopsHubState;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.dataSource.domain.DataSourceDomain;
import cn.cnic.component.dataSource.domain.DataSourceTransaction;
import cn.cnic.component.dataSource.model.DataSource;
import cn.cnic.component.dataSource.model.DataSourceProperty;
import cn.cnic.component.dataSource.service.IDataSource;
import cn.cnic.component.dataSource.utils.DataSourceUtils;
import cn.cnic.component.dataSource.vo.DataSourcePropertyVo;
import cn.cnic.component.dataSource.vo.DataSourceVo;
import cn.cnic.component.stopsComponent.model.StopsHub;
import cn.cnic.component.stopsComponent.service.IStopsHubService;
import cn.cnic.component.stopsComponent.utils.StopsHubUtils;
import cn.cnic.component.template.utils.FlowTemplateUtils;
import cn.cnic.mapper.dataSource.DataSourceMapper;
import cn.cnic.mapper.stopsComponent.StopsHubMapper;
import cn.cnic.third.service.IStop;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

@Service
public class StopsHubServiceImpl implements IStopsHubService {

    Logger logger = LoggerUtil.getLogger();


    @Resource
    private StopsHubMapper stopsHubMapper;

    @Resource
    private IStop stopImpl;



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
    public String mountStopsHub(String username,Boolean isAdmin, String id, String stopsHubName) {

        String mountId = stopImpl.mountStopsHub(stopsHubName);

        if(mountId == ""){
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Mount failed, please try again later");
        }

        StopsHub stopsHub = stopsHubMapper.getStopsHubListById(username,isAdmin,id);
        stopsHub.setMountId(mountId);
        stopsHub.setStatus(StopsHubState.MOUNT);
        stopsHub.setLastUpdateUser(username);
        stopsHub.setLastUpdateDttm(new Date());
        stopsHubMapper.updateStopHub(stopsHub);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("Mount successful");
    }

}
