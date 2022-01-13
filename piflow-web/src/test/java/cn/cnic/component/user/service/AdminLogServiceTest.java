package cn.cnic.component.user.service;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.component.system.entity.SysLog;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class AdminLogServiceTest extends ApplicationTests implements Runnable {
    /**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    @Autowired
    AdminLogService logService;

    @Test
    @Rollback(false)
    public void testAdd() {
       AdminLogServiceTest adminLogServiceTest = new AdminLogServiceTest();
        Thread thread = new Thread(adminLogServiceTest);
        thread.run();
    }


    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SysLog sysLog  = new SysLog();
            sysLog.setId(String.valueOf(i));
            sysLog.setUsername("admin");
            sysLog.setLastLoginIp("127.0.0.1");
            sysLog.setAction("success");
            sysLog.setStatus(true);
            sysLog.setResult("success");
            sysLog.setComment("success");
            logService.add(sysLog);
        }
    }
}