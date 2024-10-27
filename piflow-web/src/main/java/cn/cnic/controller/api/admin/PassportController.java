package cn.cnic.controller.api.admin;

import cn.cnic.base.utils.*;
import cn.cnic.component.system.service.ISysUserService;
import cn.cnic.component.system.vo.KejiyunAuthResponse;
import cn.cnic.component.system.vo.SysUserVo;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

@Controller
@RequestMapping("/passport")
public class PassportController {

    private static Logger logger = LoggerUtil.getLogger();


    private Environment environment;
    private final ISysUserService sysUserServiceImpl;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final ConcurrentHashMap<String, ScheduledFuture<?>> resetTasks = new ConcurrentHashMap<>();

    public PassportController(Environment environment, ISysUserService sysUserServiceImpl) {
        this.environment = environment;
        this.sysUserServiceImpl = sysUserServiceImpl;
    }

    private String deviceId;

    private boolean kejiyunLoginStatus = false;

    private String kejiyunUserName = "";


    /**
     * 是否启用科技云登录功能
     *
     * @return
     */
    @RequestMapping(value = "/getEnabled", method = RequestMethod.GET)
    @ResponseBody
    public String getEnabled() {
        String passportLogin = environment.getProperty("passport.enable.login");
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("enabled", passportLogin != null && passportLogin.equals("true"));
    }


    /**
     * 获取登录跳转地址
     *
     * @return
     */
    @RequestMapping(value = "/getRedirect", method = RequestMethod.GET)
    @ResponseBody
    public String getRedirect(String deviceId) {
        if (StringUtils.isNotBlank(this.deviceId)) {
            // 科技云回调不带用户信息,所以一个用户登录时,排斥其他用户登录
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("isBusy", true);
        }
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put("isBusy", false);
        String passportLogin = environment.getProperty("passport.enable.login");
        if (passportLogin != null && passportLogin.equals("true")) {
            // 一个设备登录时,排斥其他设备登录
            this.deviceId = deviceId;
            setCountDownTask(deviceId);
            String clientId = environment.getProperty("passport.client.id");
            String redirectUri = environment.getProperty("syspara.local.datacenter.address") + "piflow-web/passport/receiveAuthCode";
            rtnMap.put("client_id", clientId);
            rtnMap.put("redirect_uri", redirectUri);
        }
        return ReturnMapUtils.setSucceededCustomMap(rtnMap);
    }

    private void setCountDownTask(String deviceId) {
        // 取消已有的任务
        ScheduledFuture<?> scheduledTask = resetTasks.get(deviceId);
        if (scheduledTask != null && !scheduledTask.isDone()) {
            scheduledTask.cancel(true);
        }

        // 创建新的任务，在1分钟后将deviceId置空
        ScheduledFuture<?> newTask = scheduler.schedule(() -> {
            resetTasks.remove(deviceId);
            this.deviceId = "";  // 将deviceId置为空
        }, 1, TimeUnit.MINUTES); // 1分钟后重置

        resetTasks.put(deviceId, newTask);
    }

    @RequestMapping(value = "/getStatus", method = RequestMethod.GET)
    @ResponseBody
    public String getStatus(String deviceId) {
        if (!StringUtils.equalsIgnoreCase(this.deviceId, deviceId)) {
            return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("loginThirdParty", false);
        }
        Map<String, Object> rtnMap = new HashMap<>();
        if (kejiyunLoginStatus) {
            String userName = kejiyunUserName;
            rtnMap.put("loginThirdParty", true);
            rtnMap.put("userName", userName);
            if (!sysUserServiceImpl.checkUserNameExist(userName)) {
                String pw = registerUser(userName);
                logger.info("科技云用户未注册,注册用户成功,用户名:{},密码:{}", userName, pw);
                rtnMap.put("password", pw);
            } else {
                String password = userName + "password"; // 固定的
                logger.info("科技云用户已注册,用户名:{},密码:{}", userName, password);
                rtnMap.put("password", password);
            }
            kejiyunLoginStatus = false; // 重置登录状态
            this.deviceId = ""; // 重置设备id
        } else {
            rtnMap.put("loginThirdParty", false);
        }
        return ReturnMapUtils.setSucceededCustomMap(rtnMap);

    }


    @RequestMapping(value = "/receiveAuthCode", method = RequestMethod.GET)
    @ResponseBody
    public String receiveAuthCode(String code) {
        logger.info("receiveAuthCode code: {}", code);
        // 异步调用
        CompletableFuture.runAsync(() -> callKejiyunHttpGetUserInfo(code))
                .exceptionally(e -> {
                    logger.error("Asynchronous request failed: {}", e.getMessage());
                    return null;
                });
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("登录成功,请关闭当前页面!");
    }


    /**
     * 获得科技云回调后,调用科技云接口获得当前登录的用户信息
     */
    private void callKejiyunHttpGetUserInfo(String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", environment.getProperty("passport.client.id"));
        params.put("client_secret", environment.getProperty("passport.client.secret"));
        params.put("grant_type", "authorization_code");
        String redirectUri = environment.getProperty("syspara.local.datacenter.address") + "piflow-web/passport/receiveAuthCode";
        params.put("redirect_uri", redirectUri);
        params.put("code", code);
        String result = "";
        try {
            result = HttpUtils.doPostFromComCustomizeHeader("https://passport.escience.cn/oauth2/token", params, 60 * 1000, HttpUtils.setHeaderContentType("application/x-www-form-urlencoded"));
            KejiyunAuthResponse kejiyunAuthResponse = JsonUtils.toObject(result, KejiyunAuthResponse.class);
            // 验证用户是否激活
            if (kejiyunAuthResponse != null && StringUtils.equalsIgnoreCase(kejiyunAuthResponse.getUserInfo().getCstnetIdStatus(), "active")) {
                // 验证登录成功, 并设置登录状态
                logger.info("科技云登录成功,设置登录状态, 用户名:{},", kejiyunAuthResponse.getUserInfo().getTruename());
                kejiyunLoginStatus = true;
                kejiyunUserName = "科技云-" + kejiyunAuthResponse.getUserInfo().getTruename();
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
