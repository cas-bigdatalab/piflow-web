package cn.cnic.component.process.service.Impl;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.component.process.model.ProcessStop;
import cn.cnic.component.process.service.IProcessStopService;
import cn.cnic.component.process.utils.ProcessUtils;
import cn.cnic.component.process.vo.ProcessStopVo;
import cn.cnic.mapper.process.ProcessStopMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProcessStopServiceImpl implements IProcessStopService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ProcessStopMapper processStopMapper;

    /**
     * Query processStop based on processId and pageId
     *
     * @param processId
     * @param pageId
     * @return
     */
    @Override
    public String getProcessStopVoByPageId(String processId, String pageId) {
        if (StringUtils.isAnyEmpty(processId, pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Parameter passed in incorrectly");
        }
        ProcessStop processStopByPageId = processStopMapper.getProcessStopByPageIdAndPageId(processId, pageId);
        ProcessStopVo processStopVo = ProcessUtils.processStopPoToVo(processStopByPageId);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("processStopVo", processStopVo);
    }
}