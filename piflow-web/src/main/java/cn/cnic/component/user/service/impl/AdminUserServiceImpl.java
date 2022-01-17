package cn.cnic.component.user.service.impl;

import cn.cnic.base.utils.*;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.system.entity.SysUser;
import cn.cnic.component.system.vo.SysUserVo;
import cn.cnic.component.user.mapper.AdminUserMapper;
import cn.cnic.component.user.service.AdminUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();


    private final AdminUserMapper userMapper;

    @Autowired
    public AdminUserServiceImpl(AdminUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     *
     * @param isAdmin  is admin
     * @param username username
     * @param offset   Number of pages
     * @param limit    Number each page
     * @param param    Search content
     * @return
     */
    @Override
    public String getUserListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG(MessageConfig.LANGUAGE));
        }
        Page<SysUserVo> page = PageHelper.startPage(offset, limit, "crt_dttm desc");
        userMapper.getUserList(isAdmin, username, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG(MessageConfig.LANGUAGE));
        rtnMap = PageHelperUtils.setLayTableParam(page, rtnMap);
        return JsonUtils.toJsonNoException(rtnMap);
    }


    @Override
    public String getUserById(boolean isAdmin, String username, String userId) {
        SysUserVo sysUser = userMapper.getUserVoById(isAdmin,username,userId);
        String name = sysUser.getUsername();
        String password = PasswordUtils.getPassword(name);
        sysUser.setPassword(password);

        if (null == sysUser) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("no data");
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("sysUserVo", sysUser);
    }

    @Override
    public String update(boolean isAdmin, String username, SysUserVo sysUserVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (null == sysUserVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Parameter is empty");
        }
        Integer id = sysUserVo.getId();
        if (null != id) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is empty");
        }
        SysUser sysUserById = userMapper.getUserById(isAdmin, username, id+"");
        if (null == sysUserById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The task for which the current Id does not exist");
        }
        try {
            String name = sysUserVo.getUsername();
            String password = sysUserVo.getPassword();
            PasswordUtils.updatePassword(name,password);
            password = new BCryptPasswordEncoder().encode(password);
            sysUserById.setName(sysUserVo.getName());
            sysUserById.setUsername(name);
            sysUserById.setPassword(password);
            sysUserById.setStatus(sysUserVo.getStatus());

            int update = userMapper.update(sysUserById);
            if (update <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG(MessageConfig.LANGUAGE));
            }
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG(MessageConfig.LANGUAGE));
        } catch (Exception e) {
            logger.error("update failed", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr("update failed");
        }
    }



    @Override
    public String delUser(boolean isAdmin, String username, String sysUserId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(sysUserId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is empty");
        }
        SysUser sysUserById = userMapper.getUserById(isAdmin,username,sysUserId);
        if (null == sysUserById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The task for which the current Id does not exist");
        }
        try {

            sysUserById.setLastUpdateDttm(new Date());
            sysUserById.setLastUpdateUser(username);
            if ("admin".equals(sysUserById.getUsername())) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG(MessageConfig.LANGUAGE));
            }
            sysUserById.setEnableFlag(false);
            int update = userMapper.update(sysUserById);

            if (update <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG(MessageConfig.LANGUAGE));
            }
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Started successfully");
        } catch (Exception e) {
            logger.error("delete failed", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr("delete failed");
        }
    }
}
