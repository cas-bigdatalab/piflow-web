package com.nature.component.process;

import com.nature.ApplicationTests;
import com.nature.base.util.LoggerUtil;
import com.nature.component.process.model.Process;
import com.nature.component.process.service.IProcessService;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class IProcessServiceTest extends ApplicationTests {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    IProcessService processServiceImpl;

    @Test
    public void testFlowToProcessAndSave() {
        Process process = processServiceImpl.flowToProcessAndSave("d97aa10691db4b8da2680cb5b56a7ea0");
    }


}
