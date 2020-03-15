package com.nature.domain;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.component.flow.model.Flow;
import com.nature.domain.flow.FlowDomain;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class FlowDomainTest extends ApplicationTests {

    @Autowired
    private FlowDomain flowDomain;

    Logger logger = LoggerUtil.getLogger();

    @Test
    public void testGetFlowListParam() {
		List<Flow> flowListParam = flowDomain.getFlowListParam(null);
        List<Flow> flowListParam1 = flowDomain.getFlowListParam("dd");
        Flow flow = flowListParam.get(0);

        logger.info(flow.getStopsList().size() + "");
		logger.info(flowListParam1 + "");
    }

}
