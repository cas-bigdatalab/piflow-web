package cn.cnic.component.system.service.Impl;

import cn.cnic.base.utils.*;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.system.entity.SysLog;
import cn.cnic.component.system.vo.SysLogVo;
import cn.cnic.component.system.mapper.AdminLogMapper;
import cn.cnic.component.system.service.AdminLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminLogServiceImpl implements AdminLogService {

    private final AdminLogMapper logMapper;

    @Autowired
    public AdminLogServiceImpl(AdminLogMapper logMapper) {
        this.logMapper = logMapper;
    }

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
