package cn.cnic.component.process.jpa.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;


public class ProcessJpaRepositoryTest extends ApplicationTests {

    @Autowired
    private ProcessJpaRepository processJpaRepository;

    Logger logger = LoggerUtil.getLogger();


    @Test
    @Transactional
    @Rollback(value = false)
    public void testGetRunningProcessAppId() {
        List<String> runningProcessAppId = processJpaRepository.getRunningProcessAppId();
        logger.info(runningProcessAppId.size() + "");
    }

}
