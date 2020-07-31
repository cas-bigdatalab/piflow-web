package cn.cnic.component.process.service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessPath;
import cn.cnic.component.process.entity.ProcessStop;
import cn.cnic.component.process.service.IProcessPathService;
import cn.cnic.component.process.vo.ProcessPathVo;
import cn.cnic.component.process.mapper.ProcessMapper;
import cn.cnic.component.process.mapper.ProcessPathMapper;
import cn.cnic.component.process.mapper.ProcessStopMapper;

@Service
public class ProcessPathServiceImpl implements IProcessPathService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private ProcessPathMapper processPathMapper;

    @Resource
    private ProcessStopMapper processStopMapper;

    /**
     * Query processPath based on processId and pageId
     *
     * @param processId
     * @param pageId
     * @return
     */
    @Override
    public String getProcessPathVoByPageId(String processId, String pageId) {
        if (StringUtils.isAnyEmpty(processId, pageId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Parameter passed in incorrectly");
        }
        // Find ProcessPath
        ProcessPath processPathByPageId = processPathMapper.getProcessPathByPageIdAndPid(processId, pageId);
        if (null == processPathByPageId) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("No Data");
        }
        // get from PageId and to PageId
        String[] pageIds = new String[2];
        String pathTo = processPathByPageId.getTo();
        String pathFrom = processPathByPageId.getFrom();
        if (StringUtils.isNotBlank(pathFrom)) {
            pageIds[0] = pathFrom;
        }
        if (StringUtils.isNotBlank(pathTo)) {
            pageIds[1] = pathTo;
        }
        if (StringUtils.isBlank(processId) || null == pageIds || pageIds.length <= 0) {
            return null;
        }
        // Find from ProcessStop and to ProcessStop
        List<ProcessStop> processStopByPageIds = processStopMapper.getProcessStopByPageIdAndPageIds(processId, pageIds);
        if (null == processStopByPageIds || processStopByPageIds.size() <= 0) {
            return null;
        }
        ProcessPathVo processPathVo = new ProcessPathVo();
        pathTo = (null == pathTo ? "" : pathTo);
        pathFrom = (null == pathTo ? "" : pathFrom);
        for (ProcessStop processStop : processStopByPageIds) {
            if (null != processStop) {
                if (pathTo.equals(processStop.getPageId())) {
                    processPathVo.setTo(processStop.getName());
                } else if (pathFrom.equals(processStop.getPageId())) {
                    processPathVo.setFrom(processStop.getName());
                }
            }
        }
        processPathVo.setInport(StringUtils.isNotBlank(processPathByPageId.getInport()) ? processPathByPageId.getInport() : PortType.DEFAULT.getText());
        processPathVo.setOutport(StringUtils.isNotBlank(processPathByPageId.getOutport()) ? processPathByPageId.getOutport() : PortType.DEFAULT.getText());

        // Find Process RunModeType
        RunModeType runModeType = processMapper.getProcessRunModeTypeById(processId);
        Map<String, Object> rtnMap = new HashMap<>();
        rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
        if (null != runModeType) {
            rtnMap.put("runModeType", runModeType);
        }
        rtnMap.put("processPathVo", processPathVo);

        return JsonUtils.toJsonNoException(rtnMap);
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