package cn.cnic.component.system.domain;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.FileUtils;
import cn.cnic.common.Eunm.FileAssociateType;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.system.entity.File;
import cn.cnic.component.system.entity.SystemInfo;
import cn.cnic.component.system.mapper.FileMapper;
import cn.cnic.component.system.mapper.SystemInfoMapper;
import cn.cnic.component.system.vo.SystemInfoVo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class SystemInfoDomain {

    @Autowired
    private SystemInfoMapper systemInfoMapper;

    public int update(String description, String id, String content){
        return systemInfoMapper.update(description, id, content);
    }

    public SystemInfoVo select() {
        return systemInfoMapper.select();
    }
}
