package cn.cnic.component.process.jpa.repository;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.jpa.domain.ProcessDomain;
import cn.cnic.component.process.mapper.ProcessMapper;
import cn.cnic.third.service.IFlow;
import cn.cnic.third.utils.ThirdFlowInfoVoUtils;
import cn.cnic.third.vo.flow.ThirdFlowInfoVo;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;


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
