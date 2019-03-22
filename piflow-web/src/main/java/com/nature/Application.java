package com.nature;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SpringContextUtil;
import com.nature.base.vo.UserVo;
import com.nature.common.constant.SysParamsCache;
import com.nature.mapper.sysUser.SysUserMapper;
import com.nature.schedule.ProcessInfoSync;
import com.nature.third.service.GetGroupsAndStops;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.Date;

@MapperScan(basePackages = "com.nature.mapper.*.*")
@EnableTransactionManagement
@SpringBootApplication
public class Application {

    static Logger logger = LoggerUtil.getLogger();

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        SpringContextUtil.setApplicationContext(context);
        logger.warn("***************************************************************");
        logger.warn("******************  Spring Boot 启动成功  **************************");
        logger.warn("***************************************************************");
        loadStop();
        startSync();
    }

    private static void loadStop() {
        if (SysParamsCache.IS_LOAD_STOP) {
            logger.warn(new Date() + ":Loading components");
            UserVo userVo = new UserVo();
            userVo.setUsername("system");
            GetGroupsAndStops getGroupsAndStops = (GetGroupsAndStops) SpringContextUtil.getBean("getGroupsAndStops");
            getGroupsAndStops.addGroupAndStopsList(userVo);
            logger.warn(new Date() + ":Loading Component Completion");
        }

    }

    private static void startSync() {
        logger.warn("Start Sync:" + new Date());
        ProcessInfoSync processInfoSync = (ProcessInfoSync) SpringContextUtil.getBean("processInfoSync");
        processInfoSync.executeAsync();
        logger.warn("Start Sync Completion:" + new Date());
        /*if (SysParamsCache.IS_LOAD_STOP) {
        }*/

    }

}
