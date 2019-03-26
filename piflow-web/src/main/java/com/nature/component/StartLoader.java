package com.nature.component;

import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SpringContextUtil;
import com.nature.base.vo.UserVo;
import com.nature.common.constant.SysParamsCache;
import com.nature.schedule.ProcessInfoSync;
import com.nature.third.service.GetGroupsAndStops;
import org.slf4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
@Order(value=1)
public class StartLoader implements ApplicationRunner {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    GetGroupsAndStops getGroupsAndStops;

    @Resource
    ProcessInfoSync processInfoSync;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadStop();
        startSync();
    }

    private void loadStop() {
        if (SysParamsCache.IS_LOAD_STOP) {
            logger.warn(new Date() + ":Loading components");
            UserVo userVo = new UserVo();
            userVo.setUsername("system");
            getGroupsAndStops.addGroupAndStopsList(userVo);
            logger.warn(new Date() + ":Loading Component Completion");
        }

    }

    private void startSync() {
        logger.warn("Start Sync:" + new Date());
        processInfoSync.executeAsync();
        logger.warn("Start Sync Completion:" + new Date());
    }
}
