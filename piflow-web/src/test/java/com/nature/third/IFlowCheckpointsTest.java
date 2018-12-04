package com.nature.third;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.third.inf.IFlowCheckpoints;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class IFlowCheckpointsTest extends ApplicationTests {

    @Resource
    private IFlowCheckpoints flowCheckpointsImpl;

    Logger logger = LoggerUtil.getLogger();

    @Test
    public void testGetCheckpoints() {
        String checkpoints = flowCheckpointsImpl.getCheckpoints("process_4885bd7e-a369-4531-9649-e41e2d5990b9_1");
        logger.info(checkpoints);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void testAddFlow() {
    }

}
