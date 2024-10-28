package cn.cnic.controller.api.admin;

import cn.cnic.base.utils.*;
import cn.cnic.component.system.service.ISysUserService;
import cn.cnic.component.system.vo.KejiyunAuthResponse;
import cn.cnic.component.system.vo.PassportUserLoginStatus;
import cn.cnic.component.system.vo.SysUserVo;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/passport")
public class PassportController {

    private static final Logger logger = LoggerUtil.getLogger();
    private final Environment environment;
    private final ISysUserService sysUserServiceImpl;

    // 使用 ConcurrentHashMap 存储每个设备的登录状态
    private final ConcurrentHashMap<String, PassportUserLoginStatus> loginStatusMap = new ConcurrentHashMap<>();

    public PassportController(Environment environment, ISysUserService sysUserServiceImpl) {
        this.environment = environment;
        this.sysUserServiceImpl = sysUserServiceImpl;
    }

    @RequestMapping(value = "/getRedirect", method = RequestMethod.GET)
    @ResponseBody
    public String getRedirect(String deviceId) {
        Map<String, Object> rtnMap = new HashMap<>();
        if ("true".equals(environment.getProperty("passport.enable.login"))) {
            PassportUserLoginStatus newUserLoginStatus = new PassportUserLoginStatus(false, "");
            loginStatusMap.putIfAbsent(deviceId, newUserLoginStatus);
            rtnMap.put("enabled", true);
            String clientId = environment.getProperty("passport.client.id");
            String redirectUri = environment.getProperty("syspara.local.datacenter.address") + "piflow-web/passport/receiveAuthCode";
            rtnMap.put("client_id", clientId);
            rtnMap.put("redirect_uri", redirectUri);
        } else {
            rtnMap.put("enabled", false);
        }
        return ReturnMapUtils.setSucceededCustomMap(rtnMap);
    }

    @RequestMapping(value = "/getStatus", method = RequestMethod.GET)
    @ResponseBody
    public String getStatus(String deviceId) {
        PassportUserLoginStatus userLoginStatus = loginStatusMap.get(deviceId);
        if (userLoginStatus == null || !userLoginStatus.isAuthenticated()) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("loginThirdParty", false);
        }
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("loginThirdParty", true);
        String userName = userLoginStatus.getUserName();
        rtnMap.put("userName", userName);
        if (!sysUserServiceImpl.checkUserNameExist(userName)) {
            String pw = registerUser(userName);
            logger.info("科技云用户未注册,注册用户成功,用户名:{},密码:{}", userName, pw);
            rtnMap.put("password", pw);
        } else {
            String password = userName + "password";
            logger.info("科技云用户已注册,用户名:{},密码:{}", userName, password);
            rtnMap.put("password", password);
        }
        // 重置设备的登录状态
        userLoginStatus.setAuthenticated(false);
        return ReturnMapUtils.setSucceededCustomMap(rtnMap);
    }

    /**
     *
     * @param code 获取科技云用户信息的授权码,一次有效
     * @param state 科技云回传的deviceId
     * @return
     */
    @RequestMapping(value = "/receiveAuthCode", method = RequestMethod.GET)
    @ResponseBody
    public String receiveAuthCode(String code, String state) {
        logger.info("receiveAuthCode code: {}, state: {}", code, state);
        CompletableFuture.runAsync(() -> callKejiyunHttpGetUserInfo(code, state))
                .exceptionally(e -> {
                    logger.error("Asynchronous request failed: {}", e.getMessage());
                    return null;
                });
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("登录成功,请关闭当前页面!");
    }

    private void callKejiyunHttpGetUserInfo(String code, String deviceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", environment.getProperty("passport.client.id"));
        params.put("client_secret", environment.getProperty("passport.client.secret"));
        params.put("grant_type", "authorization_code");
        String redirectUri = environment.getProperty("syspara.local.datacenter.address") + "piflow-web/passport/receiveAuthCode";
        params.put("redirect_uri", redirectUri);
        params.put("code", code);
        try {
            String result = HttpUtils.doPostFromComCustomizeHeader("https://passport.escience.cn/oauth2/token", params,
                    60 * 1000, HttpUtils.setHeaderContentType("application/x-www-form-urlencoded"));
            KejiyunAuthResponse kejiyunAuthResponse = JsonUtils.toObject(result, KejiyunAuthResponse.class);
            if (kejiyunAuthResponse != null && "active".equalsIgnoreCase(kejiyunAuthResponse.getUserInfo().getCstnetIdStatus())) {
                logger.info("科技云登录成功,用户名:{}", kejiyunAuthResponse.getUserInfo().getTruename());
                PassportUserLoginStatus userLoginStatus = loginStatusMap.computeIfAbsent(deviceId, k -> {
                    PassportUserLoginStatus newUserLoginStatus = new PassportUserLoginStatus(false, "");
                    return newUserLoginStatus;
                });
                userLoginStatus.setAuthenticated(true);
                userLoginStatus.setUserName("科技云-" + kejiyunAuthResponse.getUserInfo().getTruename());
            } else {
                logger.error("科技云登录失败,原因:用户未激活");
            }
        } catch (Exception e) {
            logger.error("科技云登录失败,http请求异常!");
            e.printStackTrace();
        }
    }

    private String registerUser(String username) {
        String pw = username + "password";
        String status = "0";
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setUsername(username);
        sysUserVo.setPassword(pw);
        sysUserVo.setSex("");
        sysUserVo.setName(username);
        sysUserVo.setStatus(Byte.valueOf(status));
        PasswordUtils.getKeyAndValue(username, pw);
        String s = sysUserServiceImpl.registerUser(sysUserVo);
        JSONObject obj = JSONObject.fromObject(s);
        String code = obj.getString("code");
        if (!"200".equals(code)) {
            logger.error("注册用户失败,原因:{}", obj);
            return "";
        }
        logger.info("注册用户成功,用户名:{},密码:{}", username, pw);
        return pw;
    }
}
