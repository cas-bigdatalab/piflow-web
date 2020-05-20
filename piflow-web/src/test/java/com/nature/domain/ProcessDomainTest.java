package com.nature.domain;

import com.nature.ApplicationTests;
import com.nature.base.util.DateUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.SpringContextUtil;
import com.nature.common.Eunm.ProcessState;
import com.nature.common.Eunm.StopState;
import com.nature.component.process.model.Process;
import com.nature.component.process.model.ProcessStop;
import com.nature.domain.process.ProcessDomain;
import com.nature.mapper.process.ProcessMapper;
import com.nature.third.service.IFlow;
import com.nature.third.utils.ThirdFlowInfoVoUtils;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopsVo;
import com.nature.third.vo.flowInfo.ThirdFlowInfoVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
