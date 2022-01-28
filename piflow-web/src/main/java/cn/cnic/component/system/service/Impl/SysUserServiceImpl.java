package cn.cnic.component.system.service.Impl;

import cn.cnic.base.config.jwt.common.JwtUtils;
import cn.cnic.base.config.jwt.common.ResultJson;
import cn.cnic.base.config.jwt.exception.CustomException;
import cn.cnic.base.utils.*;
import cn.cnic.base.vo.UserVo;
import cn.cnic.common.Eunm.ResultCode;
import cn.cnic.common.Eunm.SysRoleType;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.system.domain.SysUserDomain;
import cn.cnic.component.system.entity.SysRole;
import cn.cnic.component.system.entity.SysUser;
import cn.cnic.component.system.service.ISysUserService;
import cn.cnic.component.system.vo.SysUserVo;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@SuppressWarnings("unused")
public class SysUserServiceImpl implements ISysUserService {

    @Value("${jwt.tokenHead}")
    private String tokenHead;

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtTokenUtil;
    private final SysUserDomain sysUserDomain;

    @Autowired
    public SysUserServiceImpl(AuthenticationManager authenticationManager,
                              @Qualifier("customUserDetailsService") UserDetailsService userDetailsService,
                              JwtUtils jwtTokenUtil,
                              SysUserDomain sysUserDomain) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.sysUserDomain = sysUserDomain;
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
        sysUserDomain.getSysUserVoList(isAdmin, username, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG(MessageConfig.LANGUAGE));
        rtnMap = PageHelperUtils.setLayTableParam(page, rtnMap);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String getUserById(boolean isAdmin, String username, String userId) {
        SysUserVo sysUser = sysUserDomain.getSysUserVoById(isAdmin,username,userId);
        if (null == sysUser) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG(MessageConfig.LANGUAGE));
        }
        sysUser.setPassword("");
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
        String id = sysUserVo.getId();
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is empty");
        }
        SysUser sysUserById = sysUserDomain.getSysUserById(isAdmin, username, id);
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

            int update = sysUserDomain.updateSysUser(sysUserById);
            if (update <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG(MessageConfig.LANGUAGE));
            }
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG(MessageConfig.LANGUAGE));
        } catch (Exception e) {
            logger.error("update failed", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr("update failed");
        }
    }

    /**
     * Update user
     *
     * @param username   username
     * @param oldPassword   old password
     * @param password   new  password
     * @return json
     */
    public String updatePassword(String username, String oldPassword, String password){
        if(StringUtils.isBlank(username) || StringUtils.isBlank(oldPassword) || StringUtils.isBlank(password)){
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_OPERATION_MSG(MessageConfig.LANGUAGE));
        }
        SysUser userByUserName = sysUserDomain.findUserByUserName(username);
        if (userByUserName == null || StringUtils.isBlank(userByUserName.getUsername())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG(MessageConfig.LANGUAGE));
        }
        boolean matches = new BCryptPasswordEncoder().matches(oldPassword, userByUserName.getPassword());
        if (!matches) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG(MessageConfig.LANGUAGE));
        }
        String encodePassword = new BCryptPasswordEncoder().encode(password);
        userByUserName.setPassword(encodePassword);
        try {
            sysUserDomain.updateSysUser(userByUserName);
        } catch (Exception e) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG(MessageConfig.LANGUAGE));
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG(MessageConfig.LANGUAGE));
    }

    @Override
    public String delUser(boolean isAdmin, String username, String sysUserId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(sysUserId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("id is empty");
        }
        SysUser sysUserById = sysUserDomain.getSysUserById(isAdmin,username,sysUserId);
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
            int update = sysUserDomain.updateSysUser(sysUserById);

            if (update <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG(MessageConfig.LANGUAGE));
            }
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Started successfully");
        } catch (Exception e) {
            logger.error("delete failed", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr("delete failed");
        }
    }

    @Override
    public String checkUserName(String username) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Username can not be empty");
        }
        String addUser = sysUserDomain.checkUsername(username);
        if (StringUtils.isNotBlank(addUser)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Username is already taken");
        } else {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Username is available");
        }
    }

    @Override
    public int deleteUser(String id) {
        sysUserDomain.deleteUserById(id);
        return 1;
    }

    @Override
    public String registerUser(SysUserVo sysUserVo) {
        if (null == sysUserVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Registration failed, username or password is empty");
        }
        String username = sysUserVo.getUsername();
        String password = sysUserVo.getPassword();
        // Determine if it is empty
        if (StringUtils.isAllEmpty(username, password)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Registration failed, username or password is empty");
        }
        String addUser = sysUserDomain.checkUsername(username);
        if (StringUtils.isNotBlank(addUser)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Username is already taken");
        }
        //Encrypted password
        password = new BCryptPasswordEncoder().encode(password);
        SysUser sysUser = new SysUser();
        sysUser.setId(UUIDUtils.getUUID32());
        sysUser.setCrtDttm(new Date());
        sysUser.setCrtUser("system");
        sysUser.setLastUpdateDttm(new Date());
        sysUser.setLastUpdateUser("system");
        sysUser.setEnableFlag(true);
        sysUser.setUsername(username);
        sysUser.setPassword(password);
        sysUser.setName(sysUserVo.getName());
        sysUser.setAge(sysUserVo.getAge());
        sysUser.setSex(sysUserVo.getSex());

        List<SysRole> sysRoleList = new ArrayList<>();
        SysRole sysRole = new SysRole();
        long maxId = sysUserDomain.getSysRoleMaxId();
        sysRole.setId(maxId + 1);
        sysRole.setRole(SysRoleType.USER);
        sysRole.setSysUser(sysUser);

        sysRoleList.add(sysRole);
        sysUser.setRoles(sysRoleList);

        try {
            sysUserDomain.addSysUser(sysUser);
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Congratulations, registration is successful");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr("save failed");
        }
    }

    @Override
    public String jwtLogin(String username, String password) {
        //用户验证
        final Authentication authentication = authenticate(username, password);
        //存储认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //生成token
        final UserVo userVo = (UserVo) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateAccessToken(userVo);
        //存储token
        jwtTokenUtil.putToken(username, token);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededCustomParam("token", token);
        userVo.setPassword("");
        rtnMap.put("jwtUser", userVo);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    private Authentication authenticate(String username, String password) {
        try {
            //该方法会去调用userDetailsService.loadUserByUsername()去验证用户名和密码，如果正确，则存储该用户名密码到“security 的 context中”
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException | BadCredentialsException e) {
            throw new CustomException(ResultJson.failure(ResultCode.LOGIN_ERROR, e.getMessage()));
        }
    }
}
