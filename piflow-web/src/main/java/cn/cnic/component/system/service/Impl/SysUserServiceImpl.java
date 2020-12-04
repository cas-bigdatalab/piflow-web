package cn.cnic.component.system.service.Impl;

import cn.cnic.base.config.jwt.common.JwtUtils;
import cn.cnic.base.config.jwt.common.ResultJson;
import cn.cnic.base.config.jwt.exception.CustomException;
import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.base.util.UUIDUtils;
import cn.cnic.base.vo.UserVo;
import cn.cnic.common.Eunm.ResultCode;
import cn.cnic.common.Eunm.SysRoleType;
import cn.cnic.component.system.entity.SysRole;
import cn.cnic.component.system.entity.SysUser;
import cn.cnic.component.system.service.ISysUserService;
import cn.cnic.component.system.transactional.SysUserTransactional;
import cn.cnic.component.system.vo.SysUserVo;
import cn.cnic.component.system.jpa.domain.SysUserDomain;
import lombok.extern.slf4j.Slf4j;
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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("unused")
public class SysUserServiceImpl implements ISysUserService {


    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtTokenUtil;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    public SysUserServiceImpl(AuthenticationManager authenticationManager, @Qualifier("customUserDetailsService") UserDetailsService userDetailsService, JwtUtils jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private SysUserDomain sysUserDomain;

    @Resource
    private SysUserTransactional sysUserTransactional;

    @Override
    public SysUser findByUsername(String username) {
        return sysUserTransactional.findUserByUserName(username);
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
    public List<SysUser> findByName(String name) {
        if (StringUtils.isBlank(name)) {
            name = "";
        }
        return sysUserTransactional.findUserByName(name);
    }

    @Override
    public List<SysUser> getUserList() {
        List<SysUser> listUser = sysUserTransactional.getUserList();
        return listUser;
    }

    @Override
    public SysUser addUser(SysUser user) {
        sysUserDomain.saveOrUpdate(user);
        return user;
    }

    @Override
    public int saveOrUpdate(SysUser user) {
        sysUserDomain.saveOrUpdate(user);
        return 1;
    }

    @Override
    public int deleteUser(String id) {
        sysUserDomain.delete(id);
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
        long maxId = sysUserTransactional.getSysRoleMaxId();
        sysRole.setId(maxId + 1);
        sysRole.setRole(SysRoleType.USER);
        sysRole.setSysUser(sysUser);

        sysRoleList.add(sysRole);
        sysUser.setRoles(sysRoleList);

        try {
            sysUserTransactional.addSysUser(sysUser);
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
