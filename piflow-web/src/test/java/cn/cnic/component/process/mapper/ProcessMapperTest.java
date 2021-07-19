package cn.cnic.component.process.mapper;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.component.process.entity.Process;
import org.junit.Test;
import org.slf4j.Logger;

import javax.annotation.Resource;
import java.util.List;

public class ProcessMapperTest extends ApplicationTests {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    @Resource
    private ProcessMapper processMapper;

    @Test
    public void testGetFlowById() {
        List<Process> processList = processMapper.getProcessList();
        logger.info("size = " + processList.size());
    }
}
