package cn.cnic.component.process.service.Impl;

import cn.cnic.base.utils.*;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.process.domain.ErrorLogMappingDomain;
import cn.cnic.component.process.entity.ErrorLogMapping;
import cn.cnic.component.process.service.IErrorLogMappingService;
import cn.cnic.component.process.vo.ErrorLogMappingVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;


@Service
public class ErrorLogMappingServiceImpl implements IErrorLogMappingService {

    private Logger logger = LoggerUtil.getLogger();

    private final ErrorLogMappingDomain errorLogMappingDomain;

    @Autowired
    public ErrorLogMappingServiceImpl(ErrorLogMappingDomain errorLogMappingDomain) {
        this.errorLogMappingDomain = errorLogMappingDomain;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public String save(ErrorLogMappingVo errorLogMappingVo) {
        String idStr = errorLogMappingVo.getId();
        String username = SessionUserUtil.getCurrentUsername();
        Date now = new Date();
        if (StringUtils.isBlank(idStr)) {
            //新增
            ErrorLogMapping errorLogMapping = new ErrorLogMapping();
            BeanUtils.copyProperties(errorLogMappingVo,errorLogMapping);
            errorLogMapping.setCrtDttm(now);
            errorLogMapping.setCrtUser(username);
            errorLogMapping.setLastUpdateDttm(now);
            errorLogMapping.setLastUpdateUser(username);
            errorLogMapping.setEnableFlag(true);
            errorLogMappingDomain.insert(errorLogMapping);
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.ADD_SUCCEEDED_MSG());
        } else {
            //更新
            ErrorLogMapping errorLogMapping = new ErrorLogMapping();
            BeanUtils.copyProperties(errorLogMappingVo,errorLogMapping);
            errorLogMapping.setLastUpdateDttm(now);
            errorLogMapping.setLastUpdateUser(username);
            errorLogMapping.setId(Long.parseLong(idStr));
            errorLogMappingDomain.update(errorLogMapping);
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.UPDATE_SUCCEEDED_MSG());
        }
    }

    @Override
    public String delete(String id) {
        String username = SessionUserUtil.getCurrentUsername();
        Date now = new Date();
        ErrorLogMapping errorLogMapping = new ErrorLogMapping();
        errorLogMapping.setLastUpdateDttm(now);
        errorLogMapping.setLastUpdateDttmStr(DateUtils.dateTimeSecToStr(now));
        errorLogMapping.setLastUpdateUser(username);
        errorLogMapping.setEnableFlag(false);
        errorLogMapping.setId(Long.parseLong(id));
        int i = errorLogMappingDomain.delete(errorLogMapping);
        if (i > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.DELETE_SUCCEEDED_MSG());
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DELETE_ERROR_MSG() + "update failed!!");
        }
    }

    @Override
    public String getByPage(ErrorLogMappingVo errorLogMappingVo) {
        String username = SessionUserUtil.getCurrentUsername();
        Page<ErrorLogMappingVo> page = PageHelper.startPage(errorLogMappingVo.getPage(), errorLogMappingVo.getLimit(), "last_update_dttm desc");
        errorLogMappingDomain.getByPage(errorLogMappingVo, username);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
    }

    @Override
    public String getById(String id) {
        ErrorLogMappingVo result = errorLogMappingDomain.getById(Long.parseLong(id));
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", result);
    }
}
