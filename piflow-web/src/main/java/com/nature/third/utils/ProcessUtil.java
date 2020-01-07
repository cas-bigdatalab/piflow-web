package com.nature.third.utils;

import com.nature.base.util.JsonUtils;
import com.nature.common.Eunm.PortType;
import com.nature.common.Eunm.RunModeType;
import com.nature.component.process.model.*;
import com.nature.component.process.model.Process;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessUtil {

    public static String processToJson(Process process, String checkpoint, RunModeType runModeType) {
        Map<String, Object> flowVoMap = processToMap(process, checkpoint, runModeType);
        return JsonUtils.toFormatJsonNoException(flowVoMap);
    }

    public static String processGroupToJson(ProcessGroup processGroup, RunModeType runModeType) {
        Map<String, Object> flowGroupVoMap = processGroupToMap(processGroup, runModeType);
        return JsonUtils.toFormatJsonNoException(flowGroupVoMap);
    }

    public static Map<String, Object> processToMap(Process process, String checkpoint, RunModeType runModeType) {
        Map<String, Object> rtnMap = new HashMap<>();
        Map<String, Object> flowVoMap = new HashMap<>();

        flowVoMap.put("driverMemory", process.getDriverMemory());
        flowVoMap.put("executorMemory", process.getExecutorMemory());
        flowVoMap.put("executorCores", process.getExecutorCores());
        flowVoMap.put("executorNumber", process.getExecutorNumber());
        flowVoMap.put("name", process.getName());
        flowVoMap.put("uuid", process.getId());

        // all stops
        Map<String, ProcessStop> stopsMap = new HashMap<>();

        List<Map<String, Object>> processStopMapList = new ArrayList<>();
        List<ProcessStop> processStopList = process.getProcessStopList();
        for (ProcessStop processStop : processStopList) {
            stopsMap.put(processStop.getPageId(), processStop);
        }

        // paths
        List<Map<String, Object>> thirdPathVoMapList = new ArrayList<>();
        List<ProcessPath> processPathList = process.getProcessPathList();
        if (null != processPathList && processPathList.size() > 0) {
            for (ProcessPath processPath : processPathList) {
                ProcessStop fromProcessStop = stopsMap.get(processPath.getFrom());
                ProcessStop toProcessStop = stopsMap.get(processPath.getTo());
                if (null == fromProcessStop) {
                    fromProcessStop = new ProcessStop();
                }
                if (null == toProcessStop) {
                    toProcessStop = new ProcessStop();
                }
                String to = (null != toProcessStop.getName() ? toProcessStop.getName() : "");
                String outport = (null != processPath.getOutport() ? processPath.getOutport() : "");
                String inport = (null != processPath.getInport() ? processPath.getInport() : "");
                String from = (null != fromProcessStop.getName() ? fromProcessStop.getName() : "");
                Map<String, Object> pathVoMap = new HashMap<>();
                pathVoMap.put("from", from);
                pathVoMap.put("outport", outport);
                pathVoMap.put("inport", inport);
                pathVoMap.put("to", to);
                if (PortType.ROUTE == fromProcessStop.getOutPortType() && StringUtils.isNotBlank(outport)) {

                }
                if (PortType.ROUTE == toProcessStop.getInPortType() && StringUtils.isNotBlank(inport)) {

                }
                thirdPathVoMapList.add(pathVoMap);
            }
        }
        flowVoMap.put("paths", thirdPathVoMapList);

        for (String stopPageId : stopsMap.keySet()) {
            ProcessStop processStop = stopsMap.get(stopPageId);
            Map<String, Object> thirdStopVo = new HashMap<>();
            thirdStopVo.put("uuid", processStop.getId());
            thirdStopVo.put("name", processStop.getName());
            thirdStopVo.put("bundle", processStop.getBundel());

            // StopProperty
            List<ProcessStopProperty> processStopPropertyList = processStop.getProcessStopPropertyList();
            Map<String, Object> properties = new HashMap<String, Object>();
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
            thirdStopVo.put("properties", properties);

            // StopCustomizedProperty
            List<ProcessStopCustomizedProperty> processStopCustomizedPropertyList = processStop.getProcessStopCustomizedPropertyList();
            Map<String, Object> customizedProperties = new HashMap<String, Object>();
            if (null != processStopCustomizedPropertyList && processStopCustomizedPropertyList.size() > 0) {
                for (ProcessStopCustomizedProperty processStopCustomizedProperty : processStopCustomizedPropertyList) {
                    String name = processStopCustomizedProperty.getName();
                    if (StringUtils.isNotBlank(name)) {
                        String customValue2 = processStopCustomizedProperty.getCustomValue();
                        String customValue = (null != customValue2 ? customValue2 : "");
                        customizedProperties.put(name, customValue);
                    }
                }
            }
            thirdStopVo.put("customizedProperties", customizedProperties);

            processStopMapList.add(thirdStopVo);
        }
        flowVoMap.put("stops", processStopMapList);

        //checkpoint
        if (StringUtils.isNotBlank(checkpoint)) {
            flowVoMap.put("checkpoint", checkpoint);
            if (StringUtils.isNotBlank(process.getParentProcessId())) {
                flowVoMap.put("checkpointParentProcessId", process.getParentProcessId());
            }
        }
        if (RunModeType.DEBUG == runModeType) {
            flowVoMap.put("runMode", runModeType.getValue());
        }
        rtnMap.put("flow", flowVoMap);
        return rtnMap;
    }

    public static Map<String, Object> processGroupToMap(ProcessGroup processGroup, RunModeType runModeType) {

        Map<String, Object> rtnMap = new HashMap<>();
        Map<String, Object> flowGroupVoMap = new HashMap<>();
        flowGroupVoMap.put("name", processGroup.getName());
        flowGroupVoMap.put("uuid", processGroup.getId());

        // all process
        Map<String, Process> processesMap = new HashMap<>();

        List<Process> processList = processGroup.getProcessList();
        if (null != processList && processList.size() > 0) {
            List<Map<String, Object>> processesListMap = new ArrayList<>();
            for (Process process : processList) {
                processesMap.put(process.getPageId(), process);
                Map<String, Object> processMap = processToMap(process, null, runModeType);
                processesListMap.add(processMap);
            }
            flowGroupVoMap.put("flows", processesListMap);
        }

        List<ProcessGroupPath> processGroupPathList = processGroup.getProcessGroupPathList();
        if (null != processGroupPathList && processGroupPathList.size() > 0) {
            List<Map<String, Object>> pathListMap = new ArrayList<>();
            for (ProcessGroupPath processGroupPath : processGroupPathList) {
                if (null != processGroupPath) {
                    Map<String, Object> pathMap = new HashMap<>();
                    String from = processGroupPath.getFrom();
                    String to = processGroupPath.getTo();
                    Process processFrom = processesMap.get(from);
                    Process processTo = processesMap.get(to);
                    pathMap.put("after", null != processFrom ? processFrom.getName() : "");
                    pathMap.put("entry", null != processTo ? processTo.getName() : "");
                    pathListMap.add(pathMap);
                }
            }
            flowGroupVoMap.put("conditions", pathListMap);
        }
        rtnMap.put("group", flowGroupVoMap);
        return rtnMap;
    }
}
