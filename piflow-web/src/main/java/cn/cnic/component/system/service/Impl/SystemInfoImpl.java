package cn.cnic.component.system.service.Impl;

import cn.cnic.base.utils.*;
import cn.cnic.common.Eunm.FileAssociateType;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.dataProduct.entity.DataProductType;
import cn.cnic.component.dataProduct.vo.DataProductTypeVo;
import cn.cnic.component.flow.domain.FlowStopsPublishingPropertyDomain;
import cn.cnic.component.system.domain.FileDomain;
import cn.cnic.component.system.domain.SystemInfoDomain;
import cn.cnic.component.system.entity.File;
import cn.cnic.component.system.service.IFileService;
import cn.cnic.component.system.service.ISystemInfoService;
import cn.cnic.component.system.vo.FileVo;
import cn.cnic.component.system.vo.SystemInfoVo;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.alibaba.fastjson2.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class SystemInfoImpl implements ISystemInfoService {

    private Logger logger = LoggerUtil.getLogger();

    private final SystemInfoDomain systemInfoDomain;
    private final FileDomain fileDomain;


    @Autowired
    public SystemInfoImpl(SystemInfoDomain systemInfoDomain, FileDomain fileDomain) {
        this.systemInfoDomain = systemInfoDomain;
        this.fileDomain = fileDomain;
    }


    @Override
    public String update(SystemInfoVo systemInfoVo) {
        String id = systemInfoVo.getId();
        String description = systemInfoVo.getDescription();
        if(StringUtils.isBlank(id) || StringUtils.isBlank(description)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id or description is null");
        }
        int update = systemInfoDomain.update(description, id, systemInfoVo.getContent());
        if(update > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("update success");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("update fail");
    }

    @Override
    public String select() {
        SystemInfoVo systemInfoVo = systemInfoDomain.select();
        //获取说明书
        File file = fileDomain.getByAssociateId(systemInfoVo.getId(), FileAssociateType.SYSTEM_INSTRUCTION.getValue());
        if (ObjectUtils.isNotEmpty(file)) {
            systemInfoVo.setInstructionFileId(file.getId().toString());
            systemInfoVo.setInstructionFileName(file.getFileName().toString());
        }
        //获取用户申请表
        File userFile = fileDomain.getByAssociateId(systemInfoVo.getId(), FileAssociateType.USER_APPLY.getValue());
        if (ObjectUtils.isNotEmpty(userFile)) {
            systemInfoVo.setUserApplyFileId(userFile.getId().toString());
            systemInfoVo.setUserApplyFileName(userFile.getFileName().toString());
        }
        //获取系统支持的数据产品文件
        File dataProductFile = fileDomain.getByAssociateId(systemInfoVo.getId(), FileAssociateType.SYSTEM_SUPPORTED_DATA_PRODUCT.getValue());
        if (ObjectUtils.isNotEmpty(dataProductFile)) {
            systemInfoVo.setSupportedDataProductFileId(dataProductFile.getId().toString());
            systemInfoVo.setSupportedDataProductFileName(dataProductFile.getFileName().toString());
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data",systemInfoVo);
    }
}
