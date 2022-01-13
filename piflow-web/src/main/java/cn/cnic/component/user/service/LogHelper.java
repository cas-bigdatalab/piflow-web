package cn.cnic.component.user.service;

import cn.cnic.base.utils.IpUtil;
import cn.cnic.component.system.entity.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class LogHelper {
    public static final Integer LOG_TYPE_AUTH = 1;

    @Autowired
    private AdminLogService logService;

    public void logAuthSucceed(String action, String result) {
        logAdmin(LOG_TYPE_AUTH, action, true, result, "");
    }

    public void logAdmin(Integer type, String action, Boolean succeed, String result, String comment) {
        SysLog log = new SysLog();
        log.setUsername(result);
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request != null) {
            log.setLastLoginIp(IpUtil.getIpAddr(request));
        }
        log.setAction(action);
        log.setStatus(succeed);
        log.setResult(result);
        log.setComment(comment);
        log.setLastUpdateDttm(new Date());
        logService.add(log);
    }
}
