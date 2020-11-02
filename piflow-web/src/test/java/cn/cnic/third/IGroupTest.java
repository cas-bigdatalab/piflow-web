package cn.cnic.third;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.utils.ProcessGroupUtils;
import cn.cnic.component.flow.jpa.domain.FlowGroupDomain;
import cn.cnic.third.service.IGroup;
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
        ProcessGroup test = ProcessGroupUtils.flowGroupToProcessGroup(flowGroup, "test", RunModeType.RUN, false);
        Map<String, Object> stringObjectMap = groupImpl.startFlowGroup(test, RunModeType.RUN);
        logger.info("Test return information：" + stringObjectMap);
    }

}
