package cn.cnic.component.system.service.Impl;

import cn.cnic.base.config.jwt.common.JwtUtils;
import cn.cnic.base.config.jwt.common.ResultJson;
import cn.cnic.base.config.jwt.exception.CustomException;
import cn.cnic.base.utils.*;
import cn.cnic.base.vo.UserVo;
import cn.cnic.common.Eunm.EcosystemTypeAssociateType;
import cn.cnic.common.Eunm.ResultCode;
import cn.cnic.common.Eunm.SysRoleType;
import cn.cnic.common.constant.MessageConfig;
import cn.cnic.component.dataProduct.domain.EcosystemTypeDomain;
import cn.cnic.component.dataProduct.entity.EcosystemType;
import cn.cnic.component.dataProduct.entity.EcosystemTypeAssociate;
import cn.cnic.component.system.domain.SysUserDomain;
import cn.cnic.component.system.entity.SysRole;
import cn.cnic.component.system.entity.SysUser;
import cn.cnic.component.system.service.ISysUserService;
import cn.cnic.component.system.vo.SysUserVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    private final EcosystemTypeDomain ecosystemTypeDomain;

    @Autowired
    public SysUserServiceImpl(AuthenticationManager authenticationManager,
                              @Qualifier("customUserDetailsService") UserDetailsService userDetailsService,
                              JwtUtils jwtTokenUtil,
                              SysUserDomain sysUserDomain, EcosystemTypeDomain ecosystemTypeDomain) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.sysUserDomain = sysUserDomain;
        this.ecosystemTypeDomain = ecosystemTypeDomain;
    }

    /**
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
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        if (!isAdmin) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_PERMISSION_MSG());
        }
        Page<SysUserVo> page = PageHelper.startPage(offset, limit, "crt_dttm desc");
        sysUserDomain.getSysUserVoList(isAdmin, username, param);
        List<SysUserVo> result = page.getResult();
        if(CollectionUtils.isNotEmpty(result)){
            for (SysUserVo sysUserVo : result) {
                //获取用户所属生态系统类型
                List<EcosystemTypeAssociate> associates = ecosystemTypeDomain.getAssociateByAssociateId(sysUserVo.getUsername());
                if (CollectionUtils.isNotEmpty(associates)) {
                    List<EcosystemType> ecosystemTypes = ecosystemTypeDomain.getByIds(associates.stream().map(x -> String.valueOf(x.getEcosystemTypeId())).collect(Collectors.joining(",")));
                    sysUserVo.setEcosystemTypes(ecosystemTypes);
                }
            }
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
    }

    @Override
    public String getUserById(boolean isAdmin, String username, String userId) {
        SysUserVo sysUser = sysUserDomain.getSysUserVoById(isAdmin, username, userId);
        if (null == sysUser) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        sysUser.setPassword("");
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("sysUserVo", sysUser);
    }

    @Override
    public String getByUsername() {
        String username = SessionUserUtil.getCurrentUsername();
        SysUser sysUser = sysUserDomain.getSysUserByUserName(username);
        if (null == sysUser) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_MSG());
        }
        sysUser.setPassword("");
        SysUserVo sysUserVo = new SysUserVo();
        BeanUtils.copyProperties(sysUser, sysUserVo);
        //获取用户所属生态系统类型
        List<EcosystemTypeAssociate> associates = ecosystemTypeDomain.getAssociateByAssociateId(username);
        if (CollectionUtils.isNotEmpty(associates)) {
            List<EcosystemType> ecosystemTypes = ecosystemTypeDomain.getByIds(associates.stream().map(x -> String.valueOf(x.getEcosystemTypeId())).collect(Collectors.joining(",")));
            sysUserVo.setEcosystemTypes(ecosystemTypes);
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("sysUserVo", sysUserVo);
    }

    @Override
    public String update(boolean isAdmin, String username, SysUserVo sysUserVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (null == sysUserVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_ERROR_MSG());
        }
        String id = sysUserVo.getId();
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("id"));
        }
        SysUser sysUserById = sysUserDomain.getSysUserById(isAdmin, username, id);
        if (null == sysUserById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("The task for which the current Id does not exist");
        }
        try {
            String name = sysUserVo.getUsername();
            String password = sysUserVo.getPassword();
            if (StringUtils.isNotBlank(password)) {
                PasswordUtils.updatePassword(name, password);
                password = new BCryptPasswordEncoder().encode(password);
                sysUserById.setPassword(password);
            }

            sysUserById.setName(sysUserVo.getName());
            sysUserById.setUsername(name);
            sysUserById.setStatus(sysUserVo.getStatus());
            sysUserById.setPhoneNumber(sysUserVo.getPhoneNumber());
            sysUserById.setEmail(sysUserVo.getEmail());
            sysUserById.setCompany(sysUserVo.getCompany());

            //校验是否有所属生态系统类型
            List<EcosystemType> ecosystemTypes = sysUserVo.getEcosystemTypes();
            if (CollectionUtils.isEmpty(ecosystemTypes)) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("所属生态系统类型为空，请填写后更新！");
            }
            //更新个人资料
            int update = sysUserDomain.updateSysUser(sysUserById);
            if (update <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
            }

            //更新所属生态系统类型
            List<EcosystemType> ecosystemTypeList = sysUserVo.getEcosystemTypes();
            List<EcosystemTypeAssociate> associates = ecosystemTypeList.stream().map(ecosystemType -> {
                EcosystemTypeAssociate ecosystemTypeAssociate = new EcosystemTypeAssociate();
                ecosystemTypeAssociate.setEcosystemTypeId(ecosystemType.getId());
                ecosystemTypeAssociate.setEcosystemTypeName(ecosystemType.getName());
                ecosystemTypeAssociate.setAssociateId(name);
                ecosystemTypeAssociate.setAssociateType(EcosystemTypeAssociateType.USER.getValue());
                return ecosystemTypeAssociate;
            }).collect(Collectors.toList());
            ecosystemTypeDomain.deleteByAssociateId(name);
            ecosystemTypeDomain.insertAssociateBatch(associates);

            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
        } catch (Exception e) {
            logger.error("update failed", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPDATE_ERROR_MSG());
        }
    }

    /**
     * Update user
     *
     * @param username    username
     * @param oldPassword old password
     * @param password    new  password
     * @return json
     */
    public String updatePassword(String username, String oldPassword, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(oldPassword) || StringUtils.isBlank(password)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_OPERATION_MSG());
        }
        SysUser userByUserName = sysUserDomain.findUserByUserName(username);
        if (userByUserName == null || StringUtils.isBlank(userByUserName.getUsername())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        boolean matches = new BCryptPasswordEncoder().matches(oldPassword, userByUserName.getPassword());
        if (!matches) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        String encodePassword = new BCryptPasswordEncoder().encode(password);
        userByUserName.setPassword(encodePassword);
        try {
            sysUserDomain.updateSysUser(userByUserName);
        } catch (Exception e) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    @Override
    public String delUser(boolean isAdmin, String username, String sysUserId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(sysUserId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("sysUserId"));
        }
        SysUser sysUserById = sysUserDomain.getSysUserById(isAdmin, username, sysUserId);
        if (null == sysUserById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.NO_DATA_BY_ID_XXX_MSG(sysUserId));
        }
        try {

            sysUserById.setLastUpdateDttm(new Date());
            sysUserById.setLastUpdateUser(username);
            if ("admin".equals(sysUserById.getUsername())) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
            }
            sysUserById.setEnableFlag(false);
            int update = sysUserDomain.updateSysUser(sysUserById);

            if (update <= 0) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
            }
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Started successfully");
        } catch (Exception e) {
            logger.error("delete failed", e);
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.DELETE_ERROR_MSG());
        }
    }

    @Override
    public String checkUserName(String username) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("username"));
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
    public String bindDeveloperAccessKey(boolean isAdmin, String username, String accessKey) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ILLEGAL_USER_MSG());
        }
        if (StringUtils.isBlank(accessKey)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.PARAM_IS_NULL_MSG("accessKey"));
        }
        SysUser user = sysUserDomain.findUserByUserName(username);
        user.setDeveloperAccessKey(accessKey);
        try {
            sysUserDomain.updateSysUser(user);
        } catch (Exception e) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public String updateInfo(SysUserVo sysUserVo) {
        String username = SessionUserUtil.getCurrentUsername();
        SysUser oldUser = sysUserDomain.getSysUserByUserName(username);
        //校验username是否被其他人注册
        String newUsername = sysUserVo.getUsername();
        if (!username.equals(newUsername)) {
            String sameUsername = sysUserDomain.getOtherSameUserName(newUsername);
            if (StringUtils.isNotBlank(sameUsername))
                throw new CustomException(ResultJson.failure(ResultCode.SERVER_ERROR, "账号名称已被他人使用，修改后更新！"));
        }
        //校验是否有所属生态系统类型
        List<EcosystemType> ecosystemTypes = sysUserVo.getEcosystemTypes();
        if (CollectionUtils.isEmpty(ecosystemTypes)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("所属生态系统类型为空，请填写后更新！");
        }
        //更新个人资料
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserVo, sysUser);
        sysUser.setEnableFlag(true);
        sysUser.setLastUpdateUser(sysUserVo.getUsername());
        try {
            sysUserDomain.updateSysUser(sysUser);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new CustomException(ResultJson.failure(ResultCode.SERVER_ERROR, "更新失败！"));
        }
        //更新所属生态系统类型
        List<EcosystemType> ecosystemTypeList = sysUserVo.getEcosystemTypes();
        List<EcosystemTypeAssociate> associates = ecosystemTypeList.stream().map(ecosystemType -> {
            EcosystemTypeAssociate ecosystemTypeAssociate = new EcosystemTypeAssociate();
            ecosystemTypeAssociate.setEcosystemTypeId(ecosystemType.getId());
            ecosystemTypeAssociate.setEcosystemTypeName(ecosystemType.getName());
            ecosystemTypeAssociate.setAssociateId(newUsername);
            ecosystemTypeAssociate.setAssociateType(EcosystemTypeAssociateType.USER.getValue());
            return ecosystemTypeAssociate;
        }).collect(Collectors.toList());
        ecosystemTypeDomain.deleteByAssociateId(username);
        ecosystemTypeDomain.insertAssociateBatch(associates);

        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.UPDATE_SUCCEEDED_MSG());
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public String registerUser(SysUserVo sysUserVo, String ecosystemTypeIds) {
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
        sysUser.setStatus(sysUserVo.getStatus());
        sysUser.setPhoneNumber(sysUserVo.getPhoneNumber());
        sysUser.setEmail(sysUserVo.getEmail());
        sysUser.setCompany(sysUserVo.getCompany());

        List<SysRole> sysRoleList = new ArrayList<>();
        SysRole sysRole = new SysRole();
//        long maxId = sysUserDomain.getSysRoleMaxId();
//        sysRole.setId(maxId + 1);
        sysRole.setRole(SysRoleType.USER);
        sysRole.setSysUser(sysUser);

        sysRoleList.add(sysRole);
        sysUser.setRoles(sysRoleList);

        try {
            sysUserDomain.addSysUser(sysUser);

            //生态站逻辑：  保存用户所属生态系统类型
            List<EcosystemType> ecosystemTypeList = ecosystemTypeDomain.getByIds(ecosystemTypeIds);
            List<EcosystemTypeAssociate> associates = ecosystemTypeList.stream().map(ecosystemType -> {
                EcosystemTypeAssociate ecosystemTypeAssociate = new EcosystemTypeAssociate();
                ecosystemTypeAssociate.setEcosystemTypeId(ecosystemType.getId());
                ecosystemTypeAssociate.setEcosystemTypeName(ecosystemType.getName());
                ecosystemTypeAssociate.setAssociateId(username);
                ecosystemTypeAssociate.setAssociateType(EcosystemTypeAssociateType.USER.getValue());
                return ecosystemTypeAssociate;
            }).collect(Collectors.toList());
            ecosystemTypeDomain.deleteByAssociateId(username);
            ecosystemTypeDomain.insertAssociateBatch(associates);

            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Congratulations, registration is successful");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new CustomException(ResultJson.failure(ResultCode.SERVER_ERROR, e.getMessage()));
//            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ADD_ERROR_MSG());
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
        return ReturnMapUtils.toJson(rtnMap);
    }

    @Override
    public String beforeLogin(String username) {
        boolean enableLogin = true;
        boolean b = jwtTokenUtil.containTokenByUsername(username);
        if (b) {
            String token = jwtTokenUtil.getTokenFromUsername(username);
            Boolean tokenExpired = jwtTokenUtil.isTokenExpired(token);
            if(!tokenExpired){
                enableLogin = false;
            }
        }
        if(enableLogin){
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("enable login");
        }else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("disable login");
        }
    }


    @Override
    public String addUser(SysUserVo sysUserVo) {
        boolean isAdmin = SessionUserUtil.isAdmin();
        if (!isAdmin) return ReturnMapUtils.setFailedMsgRtnJsonStr("No permission!!");
        return registerUser(sysUserVo, sysUserVo.getEcosystemTypeIds());
    }

    @Override
    public String updateRole(SysUserVo sysUserVo) {
        int i = sysUserDomain.updateRole(sysUserVo);
        if (i > 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.UPDATE_SUCCEEDED_MSG());
        } else {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.UPDATE_ERROR_MSG());
        }
    }

    @Override
    public String getAllRole() {
        List<SysRole> roles = sysUserDomain.getAllRole();
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", roles);
    }

    @Override
    public String jwtLogout(String username) {
        jwtTokenUtil.deleteToken(username);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.UPDATE_SUCCEEDED_MSG());
    }

    private Authentication authenticate(String username, String password) {
        try {
            //该方法会去调用userDetailsService.loadUserByUsername()去验证用户名和密码，如果正确，则存储该用户名密码到“security 的 context中”
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException | BadCredentialsException e) {
            throw new CustomException(ResultJson.failure(ResultCode.LOGIN_ERROR, e.getMessage()));
        }
    }

    public static void main(String[] args) {
        String password = "PFkj6H@vV";
        String encodePassword = new BCryptPasswordEncoder().encode(password);
        System.out.println(encodePassword);
    }
}
