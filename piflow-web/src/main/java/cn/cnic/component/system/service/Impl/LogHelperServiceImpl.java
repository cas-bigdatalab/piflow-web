package cn.cnic.component.system.service.Impl;

import cn.cnic.base.utils.IpUtil;
import cn.cnic.component.system.entity.SysLog;
import cn.cnic.component.system.service.AdminLogService;
import cn.cnic.component.system.service.ILogHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class LogHelperServiceImpl implements ILogHelperService {
    public static final Integer LOG_TYPE_AUTH = 1;

    private final AdminLogService logService;

    @Autowired
    public LogHelperServiceImpl(AdminLogService logService) {
        this.logService = logService;
    }

    @Override
    public void logAuthSucceed(String action, String result) {
        logAdmin(LOG_TYPE_AUTH, action, true, result, "");
    }

    @Override
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
