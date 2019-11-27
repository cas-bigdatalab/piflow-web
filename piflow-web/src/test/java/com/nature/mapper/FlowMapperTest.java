package com.nature.mapper;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.RunModeType;
import com.nature.component.flow.model.Flow;
import com.nature.component.process.model.Process;
import com.nature.component.process.utils.ProcessUtils;
import com.nature.mapper.flow.FlowMapper;
import com.nature.third.utils.ProcessUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import java.util.Date;

public class FlowMapperTest extends ApplicationTests {

    @Autowired
    private FlowMapper flowMapper;

    Logger logger = LoggerUtil.getLogger();

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
        UserVo user = new UserVo();

        user.setUsername("test");
        Process process = ProcessUtils.flowToProcess(flow, user);
        String s = ProcessUtil.processToJson(process, "", RunModeType.RUN);
        logger.info(s);
    }

    @Test
    @Rollback(true)
    public void testAddFlow() {
        Flow flow = new Flow();
        flow.setId(SqlUtils.getUUID32());
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
