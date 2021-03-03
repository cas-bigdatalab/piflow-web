package cn.cnic.component.process.mapper;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.process.entity.Process;
import org.junit.Test;
import org.slf4j.Logger;

import javax.annotation.Resource;
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
