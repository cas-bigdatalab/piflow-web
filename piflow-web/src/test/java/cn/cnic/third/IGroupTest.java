package cn.cnic.third;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.flow.domain.FlowGroupDomain;
import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.utils.ProcessGroupUtils;
import cn.cnic.third.service.IGroup;
import org.springframework.beans.factory.annotation.Autowired;


public class IGroupTest extends ApplicationTests {

    private Logger logger = LoggerUtil.getLogger();

    private final FlowGroupDomain flowGroupDomain;
    private final IGroup groupImpl;

    @Autowired
    public IGroupTest(FlowGroupDomain flowGroupDomain, IGroup groupImpl) {
        this.flowGroupDomain = flowGroupDomain;
        this.groupImpl = groupImpl;
    }


    @Test
    public void testStartFlowGroup() {
        String id = "8a80d5d270e0a92d0170e0b86bba0000";
        FlowGroup flowGroup = flowGroupDomain.getFlowGroupById(id);
        ProcessGroup test = ProcessGroupUtils.flowGroupToProcessGroup(flowGroup, "test", RunModeType.RUN, false);
        Map<String, Object> stringObjectMap = groupImpl.startFlowGroup(test, RunModeType.RUN);
        logger.info("Test return information：" + stringObjectMap);
    }

}
