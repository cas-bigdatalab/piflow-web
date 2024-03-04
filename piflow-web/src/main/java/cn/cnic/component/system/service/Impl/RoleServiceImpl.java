package cn.cnic.component.system.service.Impl;

import cn.cnic.base.utils.*;
import cn.cnic.common.Eunm.FileAssociateType;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.dataProduct.entity.DataProductType;
import cn.cnic.component.dataProduct.vo.DataProductTypeVo;
import cn.cnic.component.system.domain.RoleDomain;
import cn.cnic.component.system.entity.File;
import cn.cnic.component.system.entity.Role;
import cn.cnic.component.system.service.IRoleService;
import cn.cnic.component.system.vo.FileVo;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class RoleServiceImpl implements IRoleService {

    private Logger logger = LoggerUtil.getLogger();

    private final RoleDomain roleDomain;

    private final SnowflakeGenerator snowflakeGenerator;

    @Autowired
    public RoleServiceImpl(RoleDomain roleDomain, SnowflakeGenerator snowflakeGenerator) {
        this.roleDomain = roleDomain;
        this.snowflakeGenerator = snowflakeGenerator;
    }


    @Override
    public String getRoleInfo() {
        String username = SessionUserUtil.getCurrentUsername();
        List<Role> roleList = roleDomain.getRoleInfo(username);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data",roleList);
    }
}
