package cn.cnic.component.user.service.impl;

import cn.cnic.base.utils.*;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.dataSource.entity.DataSource;
import cn.cnic.component.dataSource.mapper.DataSourceMapper;
import cn.cnic.component.dataSource.vo.DataSourceVo;
import cn.cnic.component.system.entity.SysLog;
import cn.cnic.component.system.vo.SysLogVo;
import cn.cnic.component.user.mapper.AdminLogMapper;
import cn.cnic.component.user.service.AdminLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AdminLogServiceImpl implements AdminLogService {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    @Resource
    AdminLogMapper logMapper;

    @Override
    public String getLogListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG(MessageConfig.LANGUAGE));
        }
        Page<SysLogVo> page = PageHelper.startPage(offset, limit, "crt_dttm desc");
        logMapper.getLogList(isAdmin,username,param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG(MessageConfig.LANGUAGE));
        rtnMap = PageHelperUtils.setLayTableParam(page, rtnMap);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public void add(SysLog log) {
        logMapper.insertSelective(log);
    }


}
