package cn.cnic.component.process.jpa.domain;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.mapper.ProcessMapper;
import cn.cnic.third.service.IFlow;
import cn.cnic.third.utils.ThirdFlowInfoVoUtils;
import cn.cnic.third.vo.flow.ThirdFlowInfoVo;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;


public class ProcessDomainTest extends ApplicationTests {

    @Autowired
    private IFlow flowImpl;

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private ProcessDomain processDomain;


    Logger logger = LoggerUtil.getLogger();

    @Test
    @Transactional
    @Rollback(value = false)
    public void syncTask() {
        List<String> runningProcess = processMapper.getRunningProcess();
        for (String appId : runningProcess) {
            ThirdFlowInfoVo flowInfo = flowImpl.getFlowInfo(appId);
            if (null == flowInfo) {
                continue;
            }
            Process processByAppId = processDomain.getProcessNoGroupByAppId(appId);
            processByAppId = ThirdFlowInfoVoUtils.setProcess(processByAppId, flowInfo);
            if (null != processByAppId) {
                String stateStr = null == processByAppId.getState() ? "null-----" : processByAppId.getState().getText();
                logger.info("===================================================================================");
                logger.info(stateStr);
                processDomain.saveOrUpdate(processByAppId);
                logger.info("===================================================================================");
            }
        }
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void testSave() {
        ThirdFlowInfoVo flowInfo = flowImpl.getFlowInfo("application_1562293222869_0585");
        logger.info(flowInfo.getId());
        logger.info("===================================================================================");
        Process process = processDomain.getProcessNoGroupByAppId("application_1562293222869_0585");
        //processByAppId = ThirdFlowInfoVoUtils.setProcess(processByAppId, flowInfo);
        if (null != process) {
            process.setState(ProcessState.STARTED);
            String stateStr = null == process.getState() ? "null-----" : process.getState().getText();
            logger.info("===================================================================================");
            logger.info(stateStr);
            processDomain.saveOrUpdate(process);
            logger.info("===================================================================================");
        }
    }

}
