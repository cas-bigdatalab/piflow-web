package com.nature.third;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.common.Eunm.RunModeType;
import com.nature.component.flow.model.FlowGroup;
import com.nature.component.process.model.ProcessGroup;
import com.nature.component.process.utils.ProcessGroupUtils;
import com.nature.domain.flow.FlowGroupDomain;
import com.nature.third.service.IGroup;
import org.junit.Test;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Map;

public class IGroupTest extends ApplicationTests {

    @Resource
    private IGroup groupImpl;

    @Resource
    private FlowGroupDomain flowGroupDomain;

    private

    Logger logger = LoggerUtil.getLogger();

    @Test
    @Transactional
    public void testStartFlowGroup() {
        String id = "8a80d5d270e0a92d0170e0b86bba0000";
        FlowGroup flowGroup = flowGroupDomain.getFlowGroupById(id);
        ProcessGroup test = ProcessGroupUtils.flowGroupToProcessGroup(flowGroup, "test", RunModeType.RUN);
		Map<String, Object> stringObjectMap = groupImpl.startFlowGroup(test, RunModeType.RUN);
		logger.info("Test return information：" + stringObjectMap);
    }

}
