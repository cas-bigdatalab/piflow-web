package com.nature.third.impl;

import com.alibaba.fastjson.JSON;
import com.nature.base.config.vo.UserVo;
import com.nature.base.util.HttpUtils;
import com.nature.base.util.JsonFormatTool;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.StatefulRtnBaseUtils;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.common.Eunm.ProcessState;
import com.nature.common.constant.SysParamsCache;
import com.nature.component.process.model.Process;
import com.nature.component.process.model.ProcessPath;
import com.nature.component.process.model.ProcessStop;
import com.nature.component.process.model.ProcessStopProperty;
import com.nature.third.inf.IStartFlow;
import com.nature.third.vo.flow.ThirdFlowVo;
import com.nature.third.vo.flow.ThirdPathVo;
import com.nature.third.vo.flow.ThirdStopVo;
import com.nature.transaction.process.ProcessTransaction;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class StartFlowImpl implements IStartFlow {

    Logger logger = LoggerUtil.getLogger();

    @Resource
    ProcessTransaction processTransaction;

    /**
     * 启动process
     *
     * @param process
     * @return
     */
    @Override
    public StatefulRtnBase startProcess(Process process, String checkpoint, UserVo currentUser) {
        StatefulRtnBase statefulRtnBase = new StatefulRtnBase();
        if (null != process && null != currentUser) {
            String json = this.processToJosn(process, checkpoint);
            String encoding = "";
            String formatJson = JsonFormatTool.formatJson(json);
            logger.debug("\n" + formatJson);
            String doPost = HttpUtils.doPost(SysParamsCache.FLOW_START_URL(), formatJson, encoding);
            logger.info("返回信息：" + doPost);
            if (StringUtils.isNotBlank(doPost) && !doPost.contains("Exception")) {
                try {
                    JSONObject obj = JSONObject.fromObject(doPost).getJSONObject("flow");// 将json字符串转换为json对象
                    Process processById = processTransaction.getProcessById(process.getId());
                    processById.setAppId(obj.getString("id"));
                    processById.setProcessId(obj.getString("pid"));
                    processById.setState(ProcessState.STARTED);
                    processById.setLastUpdateUser(currentUser.getUsername());
                    processById.setLastUpdateDttm(new Date());
                    int updateProcess = processTransaction.updateProcess(processById);
                    if (updateProcess <= 0) {
                        statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("保存更新状态失败");
                    }
                } catch (Exception e) {
                    statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("接口调用成功，转换或保存出错");
                }
            } else {
                statefulRtnBase = StatefulRtnBaseUtils.setFailedMsg("接口调用失败");
            }
        }
        return statefulRtnBase;
    }

    private String processToJosn(Process process, String checkpoint) {
        ThirdFlowVo flowVo = new ThirdFlowVo();

        // stops
        List<ThirdStopVo> thirdStopVoList = new ArrayList<ThirdStopVo>();

        // stops
        Map<String, ThirdStopVo> stopsMap = new HashMap<String, ThirdStopVo>();

        flowVo.setName(process.getName());
        flowVo.setUuid(process.getId());

        List<ProcessStop> processStopList = process.getProcessStopList();
        for (ProcessStop processStop : processStopList) {
            ThirdStopVo thirdStopVo = new ThirdStopVo();
            Map<String, Object> properties = new HashMap<String, Object>();
            thirdStopVo.setUuid(processStop.getId());
            thirdStopVo.setName(processStop.getName());
            thirdStopVo.setBundle(processStop.getBundel());
            List<ProcessStopProperty> processStopPropertyList = processStop.getProcessStopPropertyList();
            if (null != processStopPropertyList && processStopPropertyList.size() > 0) {
                for (ProcessStopProperty processStopProperty : processStopPropertyList) {
                    String name = processStopProperty.getName();
                    if (StringUtils.isNotBlank(name)) {
                        String customValue2 = processStopProperty.getCustomValue();
                        String customValue = (null != customValue2 ? customValue2 : "");
                        properties.put(name, customValue);

                    }
                }
            }
            thirdStopVo.setProperties(properties);
            stopsMap.put(processStop.getPageId(), thirdStopVo);
            thirdStopVoList.add(thirdStopVo);
        }
        // paths
        List<ThirdPathVo> thirdPathVoList = new ArrayList<ThirdPathVo>();
        List<ProcessPath> processPathList = process.getProcessPathList();
        if (null != processPathList && processPathList.size() > 0) {
            for (ProcessPath processPath : processPathList) {
                ThirdStopVo fromThirdStopVo = stopsMap.get(processPath.getFrom());
                ThirdStopVo toThirdStopVo = stopsMap.get(processPath.getTo());
                if (null == fromThirdStopVo) {
                    fromThirdStopVo = new ThirdStopVo();
                }
                if (null == toThirdStopVo) {
                    toThirdStopVo = new ThirdStopVo();
                }
                String to = (null != toThirdStopVo.getName() ? toThirdStopVo.getName() : "");
                String outport = (null != processPath.getOutport() ? processPath.getOutport() : "");
                String inport = (null != processPath.getInport() ? processPath.getInport() : "");
                String from = (null != fromThirdStopVo.getName() ? fromThirdStopVo.getName() : "");
                ThirdPathVo pathVo = new ThirdPathVo();
                pathVo.setFrom(from);
                pathVo.setOutport(outport);
                pathVo.setInport(inport);
                pathVo.setTo(to);
                thirdPathVoList.add(pathVo);
            }
        }
        flowVo.setPaths(thirdPathVoList);
        flowVo.setStops(thirdStopVoList);
        if (StringUtils.isNotBlank(checkpoint)) {
            flowVo.setCheckpoint(checkpoint);
            if (StringUtils.isNotBlank(process.getParentProcessId())) {
                flowVo.setCheckpointParentProcessId(process.getParentProcessId());
            }
        }
        String json = JSON.toJSON(flowVo).toString();

        return "{\"flow\":" + json + "}";
    }
}
