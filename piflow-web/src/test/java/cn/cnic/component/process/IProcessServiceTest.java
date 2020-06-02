package cn.cnic.component.process;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.process.service.IProcessService;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class IProcessServiceTest extends ApplicationTests {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private IProcessService processServiceImpl;

    @Test
    public void testFlowToProcessAndSave() {
        processServiceImpl.flowToProcessAndSave("d97aa10691db4b8da2680cb5b56a7ea0");
    }


}
