package cn.cnic.component.process.mapper;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.UUIDUtils;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.mapper.FlowMapper;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.utils.ProcessUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

public class ProcessMapperTest extends ApplicationTests {

    @Resource
    private ProcessMapper processMapper;

    Logger logger = LoggerUtil.getLogger();

    @Test
    public void testGetFlowById() {
        List<Process> processList = processMapper.getProcessList();
        logger.info("size = " + processList.size());
    }
}
