package com.nature.third;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.common.Eunm.RunModeType;
import com.nature.component.flow.model.Flow;
import com.nature.component.process.model.Process;
import com.nature.component.process.utils.ProcessUtils;
import com.nature.mapper.flow.FlowMapper;
import com.nature.third.service.IFlow;
import com.nature.third.vo.flow.ThirdProgressVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoVo;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

public class IFlowTest extends ApplicationTests {

    @Autowired
    private IFlow flowImpl;

    @Resource
    private FlowMapper flowMapper;

    Logger logger = LoggerUtil.getLogger();

    @Test
    @Transactional
    @Rollback(value = false)
    public void testStartFlow() {
        Flow flowById = flowMapper.getFlowById("0641076d5ae840c09d2be5b71fw00001");
        Process process = ProcessUtils.flowToProcess(flowById, null);
        //flowImpl.startFlow(processById,null, RunModeType.RUN);
        String s = ProcessUtils.processToJson(process, null, RunModeType.DEBUG);
        logger.info(s);
    }

    @Test
    public void testGetFlowProgress() {
        String appId = "application_1540442049798_0095";
        ThirdProgressVo startFlow2 = flowImpl.getFlowProgress(appId);
        logger.info("Test return information：" + startFlow2);
    }

    @Test
    public void testGetCheckpoints() {
        String checkpoints = flowImpl.getCheckpoints("process_4885bd7e-a369-4531-9649-e41e2d5990b9_1");
        logger.info(checkpoints);
    }

    @Test
    public void testGetFlowLog() {
        String appId = "application_1539850523117_0159";
        flowImpl.getFlowLog(appId);
    }

    @Test
    public void testStopFlow() {
        String appId = "application_1562293222869_0097";
        String startFlow2 = flowImpl.stopFlow(appId);
        logger.info("Test return information：" + startFlow2);
    }

    @Test
    public void testGetDebugData() {
        String getDebugData = flowImpl.getDebugData("application_1562293222869_0031", "XmlParser", "default");
        //String getDebugData = flowImpl.getDebugData("application_1562293222869_0031","Fork","out2");
        logger.info("Test return information：" + getDebugData);
    }

    @Test
    public void testGetFlowInfo() {
        String appId = "application_1544066083705_0409";
        ThirdFlowInfoVo startFlow2 = flowImpl.getFlowInfo(appId);
        logger.info("Test return information：" + startFlow2);
    }

}
