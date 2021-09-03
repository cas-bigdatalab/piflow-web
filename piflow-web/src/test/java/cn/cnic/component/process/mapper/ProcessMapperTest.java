package cn.cnic.component.process.mapper;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.component.process.entity.Process;


public class ProcessMapperTest extends ApplicationTests {

    private Logger logger = LoggerUtil.getLogger();

    @Autowired
    private ProcessMapper processMapper;

    @Test
    public void testGetFlowById() {
        List<Process> processList = processMapper.getProcessList();
        logger.info("size = " + processList.size());
    }
    
    @Test
    public void getProcessStateById() {
        ProcessState processStateById = processMapper.getProcessStateById("8a80d82b72a20ae50172a21425a00002");
        logger.info("size = " + processStateById);
    }
}
