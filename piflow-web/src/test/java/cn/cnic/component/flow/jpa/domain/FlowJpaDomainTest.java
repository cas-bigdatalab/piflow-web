package cn.cnic.component.flow.jpa.domain;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.flow.entity.Flow;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class FlowJpaDomainTest extends ApplicationTests {

    @Autowired
    private FlowDomain flowDomain;

    Logger logger = LoggerUtil.getLogger();

    @Test
    public void testGetFlowById() {
        Flow ff8081816dd7c769016dd7e95ecc0002 = flowDomain.getFlowById("ff8081816dd7c769016dd7e95ecc0002");
        logger.info(ff8081816dd7c769016dd7e95ecc0002 + "");
    }

}
