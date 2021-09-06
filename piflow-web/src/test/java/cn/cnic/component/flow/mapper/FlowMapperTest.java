package cn.cnic.component.flow.mapper;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.base.utils.UUIDUtils;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.utils.ProcessUtils;


public class FlowMapperTest extends ApplicationTests {

    private Logger logger = LoggerUtil.getLogger();

    @Autowired
    private FlowMapper flowMapper;

    @Test
    public void testGetFlowById() {
        Flow flow = flowMapper.getFlowById("85f90a18423245b09cde371cbb333021");
        if (null == flow) {
            logger.info("The query result is empty");
            flow = new Flow();
        }
        logger.info(flow.toString());
    }

    @Test
    public void test() {
        Flow flow = flowMapper.getFlowById("0cbda2327bca445c809d5c3b8b14f5a2");
        Process process = ProcessUtils.flowToProcess(flow, "test", true);
        String s = ProcessUtils.processToJson(process, "", RunModeType.RUN, flow.getFlowGlobalParamsList());
        logger.info(s);
    }

    @Test
    @Rollback(true)
    public void testAddFlow() {
        Flow flow = new Flow();
        flow.setId(UUIDUtils.getUUID32());
        flow.setCrtUser("Nature");
        flow.setCrtDttm(new Date());
        flow.setLastUpdateUser("Nature");
        flow.setLastUpdateDttm(new Date());
        flow.setEnableFlag(true);
        //flow.setAppId("kongkong");
        flow.setUuid(flow.getId());
        flow.setName("sss");

        int addFlow = flowMapper.addFlow(flow);
        logger.info(addFlow + "");
    }

}
