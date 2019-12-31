package com.nature.repository;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.common.Eunm.ScheduleState;
import com.nature.component.system.model.SysSchedule;
import com.nature.repository.system.SysScheduleJpaRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Optional;

public class SysScheduleJpaRepositoryTest extends ApplicationTests {

    @Autowired
    private SysScheduleJpaRepository sysScheduleJpaRepository;

    Logger logger = LoggerUtil.getLogger();

    @Test
    public void testGetOne() {
        sysScheduleJpaRepository.getOne("ff8081816eaa8a5d016eaa8a77e40000");
        sysScheduleJpaRepository.getOne("ff8081816eaa9317016eaa932dd50000");
        Optional<SysSchedule> ff8081816eaa9317016eaa932dd50001 = sysScheduleJpaRepository.findById("ff8081816eaa9317016eaa932dd50001");
        if ("Optional.empty" != ff8081816eaa9317016eaa932dd50001.toString()) {
            ff8081816eaa9317016eaa932dd50001.get();
        }
        logger.info(ff8081816eaa9317016eaa932dd50001 + "");
        logger.info(ff8081816eaa9317016eaa932dd50001.toString());
    }

    @Test
    public void testSave() {
        SysSchedule sysSchedule = new SysSchedule();
        sysSchedule.setCrtUser("system");
        sysSchedule.setCrtDttm(new Date());
        sysSchedule.setLastUpdateUser("system");
        sysSchedule.setLastUpdateDttm(new Date());
        sysSchedule.setJobName("MyTask");
        sysSchedule.setJobClass("MyTask");
        sysSchedule.setCronExpression("0/5 * * * * ?");
        sysSchedule.setStatus(ScheduleState.RUNNING);
        sysSchedule = sysScheduleJpaRepository.save(sysSchedule);
        logger.info(sysSchedule.getId());
    }

}
