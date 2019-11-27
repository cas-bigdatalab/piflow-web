package com.nature.component.process.service.Impl;

import com.nature.base.util.LoggerUtil;
import com.nature.common.Eunm.PortType;
import com.nature.component.process.model.Process;
import com.nature.component.process.model.ProcessPath;
import com.nature.component.process.model.ProcessStop;
import com.nature.component.process.service.IProcessPathService;
import com.nature.component.process.vo.ProcessPathVo;
import com.nature.mapper.process.ProcessMapper;
import com.nature.mapper.process.ProcessPathMapper;
import com.nature.transaction.process.ProcessPathTransaction;
import com.nature.transaction.process.ProcessStopTransaction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProcessPathServiceImpl implements IProcessPathService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private ProcessPathMapper processPathMapper;

    @Autowired
    private ProcessPathTransaction processPathTransaction;

    @Autowired
    private ProcessStopTransaction processStopTransaction;

    /**
     * Query processPath based on processId and pageId
     *
     * @param processId
     * @param pageId
     * @return
     */
    @Override
    public ProcessPathVo getProcessPathVoByPageId(String processId, String pageId) {
        ProcessPathVo processStopVo = null;
        ProcessPath processPathByPageId = processPathTransaction.getProcessPathByPageId(processId, pageId);
        if (null != processPathByPageId) {
            processPathByPageId.getPageId();
            String[] pageIds = new String[2];
            String pathTo = processPathByPageId.getTo();
            String pathFrom = processPathByPageId.getFrom();
            if (StringUtils.isNotBlank(pathFrom)) {
                pageIds[0] = pathFrom;
            }
            if (StringUtils.isNotBlank(pathTo)) {
                pageIds[1] = pathTo;
            }
            List<ProcessStop> processStopByPageIds = processStopTransaction.getProcessStopByPageIds(processId, pageIds);
            if (null != processStopByPageIds && processStopByPageIds.size() > 0) {
                processStopVo = new ProcessPathVo();
                pathTo = (null == pathTo ? "" : pathTo);
                pathFrom = (null == pathTo ? "" : pathFrom);
                for (ProcessStop processStop : processStopByPageIds) {
                    if (null != processStop) {
                        if (pathTo.equals(processStop.getPageId())) {
                            processStopVo.setTo(processStop.getName());
                        } else if (pathFrom.equals(processStop.getPageId())) {
                            processStopVo.setFrom(processStop.getName());
                        }
                    }
                }
                processStopVo.setInport(StringUtils.isNotBlank(processPathByPageId.getInport()) ? processPathByPageId.getInport() : PortType.DEFAULT.getText());
                processStopVo.setOutport(StringUtils.isNotBlank(processPathByPageId.getOutport()) ? processPathByPageId.getOutport() : PortType.DEFAULT.getText());
            }
        }
        return processStopVo;
    }

    /**
     * Query processGroupPath based on processId and pageId
     *
     * @param processGroupId
     * @param pageId
     * @return
     */
    @Override
    public ProcessPathVo getProcessGroupPathVoByPageId(String processGroupId, String pageId) {
        ProcessPathVo processStopVo = null;
        ProcessPath processPathByPageIdAndProcessGroupId = processPathMapper.getProcessPathByPageIdAndProcessGroupId(processGroupId, pageId);
        if (null != processPathByPageIdAndProcessGroupId) {
            processPathByPageIdAndProcessGroupId.getPageId();
            String[] pageIds = new String[2];
            String pathTo = processPathByPageIdAndProcessGroupId.getTo();
            String pathFrom = processPathByPageIdAndProcessGroupId.getFrom();
            if (StringUtils.isNotBlank(pathFrom)) {
                pageIds[0] = pathFrom;
            }
            if (StringUtils.isNotBlank(pathTo)) {
                pageIds[1] = pathTo;
            }
            List<Process> processByPageIds = processMapper.getProcessByPageIds(processGroupId, pageIds);
            if (null != processByPageIds && processByPageIds.size() > 0) {
                processStopVo = new ProcessPathVo();
                pathTo = (null == pathTo ? "" : pathTo);
                pathFrom = (null == pathTo ? "" : pathFrom);
                for (Process process : processByPageIds) {
                    if (null != process) {
                        if (pathTo.equals(process.getPageId())) {
                            processStopVo.setTo(process.getName());
                        } else if (pathFrom.equals(process.getPageId())) {
                            processStopVo.setFrom(process.getName());
                        }
                    }
                }
                processStopVo.setInport(StringUtils.isNotBlank(processPathByPageIdAndProcessGroupId.getInport()) ? processPathByPageIdAndProcessGroupId.getInport() : PortType.DEFAULT.getText());
                processStopVo.setOutport(StringUtils.isNotBlank(processPathByPageIdAndProcessGroupId.getOutport()) ? processPathByPageIdAndProcessGroupId.getOutport() : PortType.DEFAULT.getText());
            }
        }
        return processStopVo;
    }


}