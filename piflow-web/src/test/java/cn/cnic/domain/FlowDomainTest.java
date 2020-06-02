package cn.cnic.domain;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.domain.flow.FlowDomain;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class FlowDomainTest extends ApplicationTests {

    @Autowired
    private FlowDomain flowDomain;

    Logger logger = LoggerUtil.getLogger();

    @Test
    public void testGetFlowListParam() {
		//List<Flow> flowListParam = flowDomain.getFlowListParam(null);
        //List<Flow> flowListParam1 = flowDomain.getFlowListParam("dd");
        //Flow flow = flowListParam.get(0);

        //logger.info(flow.getStopsList().size() + "");
		//logger.info(flowListParam1 + "");
    }

}
