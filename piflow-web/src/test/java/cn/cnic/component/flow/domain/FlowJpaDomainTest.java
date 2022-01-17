package cn.cnic.component.flow.domain;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.component.flow.entity.Flow;
import org.springframework.beans.factory.annotation.Autowired;


public class FlowJpaDomainTest extends ApplicationTests {

    private Logger logger = LoggerUtil.getLogger();

    private final FlowDomain flowDomain;

    @Autowired
    public FlowJpaDomainTest(FlowDomain flowDomain) {
        this.flowDomain = flowDomain;
    }

    @Test
    public void testGetFlowById() {
        Flow ff8081816dd7c769016dd7e95ecc0002 = flowDomain.getFlowById("ff8081816dd7c769016dd7e95ecc0002");
        logger.info(ff8081816dd7c769016dd7e95ecc0002 + "");
    }

}
