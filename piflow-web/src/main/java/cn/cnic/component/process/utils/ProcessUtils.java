package cn.cnic.component.process.utils;

import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.UUIDUtils;
import cn.cnic.common.Eunm.*;
import cn.cnic.component.dataSource.utils.DataSourceUtils;
import cn.cnic.component.flow.entity.*;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.mxGraph.utils.MxGraphModelUtils;
import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.*;
import cn.cnic.component.process.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.*;

public class ProcessUtils {

    public static Process processNewNoId(String username) {
        Process process = new Process();
        // basic properties (required when creating)
        process.setCrtDttm(new Date());
        process.setCrtUser(username);
        // basic properties
        process.setEnableFlag(true);
        process.setLastUpdateUser(username);
        process.setLastUpdateDttm(new Date());
        process.setVersion(0L);
        return process;
    }

    public static Process initProcessBasicPropertiesNoId(Process process, String username) {
        if (null == process) {
            return processNewNoId(username);
        }
        process.setId(null);
        // basic properties (required when creating)
        process.setCrtDttm(new Date());
        process.setCrtUser(username);
        // basic properties
        process.setEnableFlag(true);
        process.setLastUpdateUser(username);
        process.setLastUpdateDttm(new Date());
        process.setVersion(0L);
        return process;
    }

    public static ProcessVo processOnePoToVo(Process process) {
        ProcessVo processVo = null;
        if (null != process) {
            processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVo.setCrtDttm(process.getCrtDttm());
        }
        return processVo;
    }

    public static ProcessVo processPoToVo(Process process) {
        if (null == process) {
            return null;
        }
        ProcessVo processVo = new ProcessVo();
        BeanUtils.copyProperties(process, processVo);
        //
        ProcessGroup processGroup = process.getProcessGroup();
        if (null != processGroup) {
            ProcessGroupVo processGroupVo = new ProcessGroupVo();
            processGroupVo.setId(processGroup.getId());
            processVo.setProcessGroupVo(processGroupVo);
        }
        MxGraphModel mxGraphModel = process.getMxGraphModel();
        if (null != mxGraphModel) {
            MxGraphModelVo mxGraphModelVo = MxGraphModelUtils.mxGraphModelPoToVo(mxGraphModel);
            processVo.setMxGraphModelVo(mxGraphModelVo);
        }
        List<ProcessStop> processStopList = process.getProcessStopList();
        if (CollectionUtils.isNotEmpty(processStopList)) {
            List<ProcessStopVo> processStopVos = processStopListPoToVo(processStopList);
            if (CollectionUtils.isNotEmpty(processStopVos)) {
                processVo.setProcessStopVoList(processStopVos);
            }
        }
        List<ProcessPath> processPathList = process.getProcessPathList();
        if (CollectionUtils.isNotEmpty(processPathList)) {
            List<ProcessPathVo> processPathVos = processPathListPoToVo(processPathList);
            if (CollectionUtils.isNotEmpty(processPathVos)) {
                processVo.setProcessPathVoList(processPathVos);
            }
        }
        return processVo;
    }

    public static List<ProcessStopVo> processStopListPoToVo(List<ProcessStop> processStopList) {
        List<ProcessStopVo> processStopVoList = null;
        if (null != processStopList && processStopList.size() > 0) {
            processStopVoList = new ArrayList<ProcessStopVo>();
            for (ProcessStop processStop : processStopList) {
                ProcessStopVo processStopVo = processStopPoToVo(processStop);
                if (null != processStopVo) {
                    processStopVoList.add(processStopVo);
                }
            }
        }
        return processStopVoList;
    }

    public static ProcessStopVo processStopPoToVo(ProcessStop processStop) {
        ProcessStopVo processStopVo = null;
        if (null != processStop) {
            processStopVo = new ProcessStopVo();
            BeanUtils.copyProperties(processStop, processStopVo);
            processStopVo.setState((null != processStop.getState() ? processStop.getState().name() : ""));
            List<ProcessStopProperty> processStopPropertyList = processStop.getProcessStopPropertyList();
            if (null != processStopPropertyList && processStopPropertyList.size() > 0) {
                List<ProcessStopPropertyVo> processStopPropertyVoList = new ArrayList<ProcessStopPropertyVo>();
                for (ProcessStopProperty processStopProperty : processStopPropertyList) {
                    if (null != processStopProperty) {
                        ProcessStopPropertyVo processStopPropertyVo = new ProcessStopPropertyVo();
                        BeanUtils.copyProperties(processStopProperty, processStopPropertyVo);
                        processStopPropertyVoList.add(processStopPropertyVo);
                    }
                }
                processStopVo.setProcessStopPropertyVoList(processStopPropertyVoList);
            }
        }
        return processStopVo;
    }

    public static List<ProcessPathVo> processPathListPoToVo(List<ProcessPath> processPathList) {
        List<ProcessPathVo> processPathVoList = null;
        if (null != processPathList && processPathList.size() > 0) {
            processPathVoList = new ArrayList<ProcessPathVo>();
            for (ProcessPath processPath : processPathList) {
                ProcessPathVo processPathVo = processPathPoToVo(processPath);
                if (null != processPathVo) {
                    processPathVoList.add(processPathVo);
                }
            }
        }
        return processPathVoList;
    }

    public static ProcessPathVo processPathPoToVo(ProcessPath processPath) {
        ProcessPathVo processPathVo = null;
        if (null != processPath) {
            processPathVo = new ProcessPathVo();
            BeanUtils.copyProperties(processPath, processPathVo);
        }
        return processPathVo;
    }

    public static Process flowToProcess(Flow flow, String username, boolean isAddId) {
        Process process = null;
        if (null != flow) {
            process = new Process();
            // Copy flow information to process
            BeanUtils.copyProperties(flow, process);
            // Set basic information
            process = initProcessBasicPropertiesNoId(process, username);
            if (isAddId) {
                process.setId(UUIDUtils.getUUID32());
            } else {
                process.setId(null);
            }
            FlowGroup flowGroup = flow.getFlowGroup();
            //Set default
            process.setProcessParentType(ProcessParentType.PROCESS);
            if (null != flowGroup) {
                process.setProcessParentType(ProcessParentType.GROUP);
            }
            // Take out the flow board information of the flow
            MxGraphModel mxGraphModel = flow.getMxGraphModel();
            MxGraphModel mxGraphModelProcess = MxGraphModelUtils.copyMxGraphModelAndNewNoIdAndUnlink(mxGraphModel, isAddId);
            mxGraphModelProcess = MxGraphModelUtils.initMxGraphModelBasicPropertiesNoId(mxGraphModelProcess, username, isAddId);
            // add link
            mxGraphModelProcess.setProcess(process);
            process.setMxGraphModel(mxGraphModelProcess);

            // set flowId
            process.setFlowId(flow.getId());
            // Stops to remove flow
            List<Stops> stopsList = flow.getStopsList();
            // stopsList isEmpty
            if (null != stopsList && stopsList.size() > 0) {
                // List of stop of process
                List<ProcessStop> processStopList = new ArrayList<ProcessStop>();
                // Loop stopsList
                for (Stops stops : stopsList) {
                    ProcessStop processStop = stopsToProcessStop(stops, username, isAddId);
                    if(null == processStop) {
                        continue;
                    }
                    // Associate foreign key
                    processStop.setProcess(process);
                    processStopList.add(processStop);
                }
                process.setProcessStopList(processStopList);
            }
            // Get the paths information of flow
            List<Paths> pathsList = flow.getPathsList();
            // isEmpty
            if (null != pathsList && pathsList.size() > 0) {
                List<ProcessPath> processPathList = new ArrayList<>();
                // Loop paths information
                for (Paths paths : pathsList) {
                    // isEmpty
                    if (null == paths) {
                        continue;
                    }
                    ProcessPath processPath = new ProcessPath();
                    // Copy paths information into processPath
                    BeanUtils.copyProperties(paths, processPath);
                    // Set basic information
                    processPath = ProcessPathUtils.initProcessPathBasicPropertiesNoId(processPath, username);
                    if (isAddId) {
                        processPath.setId(UUIDUtils.getUUID32());
                    } else {
                        processPath.setId(null);
                    }
                    // Associated foreign key
                    processPath.setProcess(process);
                    processPathList.add(processPath);
                }
                process.setProcessPathList(processPathList);
            }
        }
        return process;
    }

    public static ProcessStop stopsToProcessStop(Stops stops, String username, boolean isAddId) {
        // isEmpty
        if (null == stops) {
            return null;
        }
        ProcessStop processStop = new ProcessStop();
        // Copy stops information into processStop
        BeanUtils.copyProperties(stops, processStop);
        // Set basic information
        processStop = ProcessStopUtils.initProcessStopBasicPropertiesNoId(processStop, username);
        if (isAddId) {
            processStop.setId(UUIDUtils.getUUID32());
        } else {
            processStop.setId(null);
        }
        
        // Remove the properties of stops
        List<Property> properties = stops.getProperties();
        // Determine if the stops attribute is empty
        if (null != properties && properties.size() > 0) {
            Map<String, String> dataSourcePropertyMap = DataSourceUtils.dataSourceToPropertyMap(stops.getDataSource());
            List<ProcessStopProperty> processStopPropertyList = new ArrayList<>();
            // Attributes of loop stops
            for (Property property : properties) {
                // isEmpty
                if (null == property) {
                    continue;
                }
                ProcessStopProperty processStopProperty = new ProcessStopProperty();
                // Copy property information into processStopProperty
                BeanUtils.copyProperties(property, processStopProperty);
                // Set basic information
                processStopProperty = ProcessStopPropertyUtils.initProcessStopPropertyBasicPropertiesNoId(processStopProperty, username);
                if (isAddId) {
                    processStopProperty.setId(UUIDUtils.getUUID32());
                } else {
                    processStopProperty.setId(null);
                }
                // "stop" attribute name
                String name = property.getName();
                // Judge empty
                if (StringUtils.isNotBlank(name)) {
                    // Go to the map of the "datasource" attribute
                    String value = dataSourcePropertyMap.get(name.toLowerCase());
                    // Judge empty
                    if (StringUtils.isNotBlank(value)) {
                        // Assignment
                        processStopProperty.setCustomValue(value);
                    }
                }
                // Associated foreign key
                processStopProperty.setProcessStop(processStop);
                processStopPropertyList.add(processStopProperty);
            }
            processStop.setProcessStopPropertyList(processStopPropertyList);
        }

        // Take out the custom properties of stops
        List<CustomizedProperty> customizedPropertyList = stops.getCustomizedPropertyList();
        // Determine if the stops attribute is empty
        if (null != customizedPropertyList && customizedPropertyList.size() > 0) {
            List<ProcessStopCustomizedProperty> processStopCustomizedPropertyList = new ArrayList<>();
            // Attributes of loop stops
            for (CustomizedProperty customizedProperty : customizedPropertyList) {
                // isEmpty
                if (null == customizedProperty) {
                    continue;
                }
                ProcessStopCustomizedProperty processStopCustomizedProperty = new ProcessStopCustomizedProperty();
                // Copy customizedProperty information into processStopCustomizedProperty
                BeanUtils.copyProperties(customizedProperty, processStopCustomizedProperty);
                // Set basic information
                processStopCustomizedProperty = ProcessStopCustomizedPropertyUtils.initProcessStopCustomizedPropertyBasicPropertiesNoId(processStopCustomizedProperty, username);
                if (isAddId) {
                    processStopCustomizedProperty.setId(UUIDUtils.getUUID32());
                } else {
                    processStopCustomizedProperty.setId(null);
                }
                // Associated foreign key
                processStopCustomizedProperty.setProcessStop(processStop);
                processStopCustomizedPropertyList.add(processStopCustomizedProperty);
            }
            processStop.setProcessStopCustomizedPropertyList(processStopCustomizedPropertyList);
        }
        return processStop;
        
    }
    
    public static List<Process> copyProcessList(List<Process> processList, String username, RunModeType runModeType, ProcessGroup processGroup, boolean isAddId) {
        List<Process> copyProcessList = null;
        if (null != processList && processList.size() > 0) {
            copyProcessList = new ArrayList<>();
            Process copyProcess;
            for (Process process : processList) {
                copyProcess = copyProcess(process, username, runModeType, isAddId);
                if (null == copyProcess) {
                    continue;
                }
                //link processGroup
                copyProcess.setProcessGroup(processGroup);
                copyProcessList.add(copyProcess);
            }
        }
        return copyProcessList;
    }

    public static Process copyProcess(Process process, String username, RunModeType runModeType, boolean isAddId) {
        if (StringUtils.isBlank(username) || null == process) {
            return null;
        }
        // process
        Process copyProcess = new Process();
        BeanUtils.copyProperties(process, copyProcess);
        copyProcess = ProcessUtils.initProcessBasicPropertiesNoId(copyProcess, username);
        if (isAddId) {
            copyProcess.setId(UUIDUtils.getUUID32());
        } else {
            copyProcess.setId(null);
        }
        copyProcess.setState(ProcessState.INIT);
        copyProcess.setRunModeType(null != runModeType ? runModeType : RunModeType.RUN);
        copyProcess.setParentProcessId(StringUtils.isNotBlank(process.getParentProcessId()) ? process.getParentProcessId() : process.getProcessId());
        copyProcess.setProcessParentType(ProcessParentType.GROUP);
        copyProcess.setStartTime(null);
        copyProcess.setEndTime(null);
        copyProcess.setProgress("0.00");

        //unlink processGroup
        copyProcess.setProcessGroup(null);

        // copy processMxGraphModel
        MxGraphModel processMxGraphModel = process.getMxGraphModel();
        MxGraphModel copyMxGraphModel = MxGraphModelUtils.copyMxGraphModelAndNewNoIdAndUnlink(processMxGraphModel, isAddId);
        copyMxGraphModel = MxGraphModelUtils.initMxGraphModelBasicPropertiesNoId(copyMxGraphModel, username,isAddId);
        // add link
        copyMxGraphModel.setProcess(copyProcess);
        copyProcess.setMxGraphModel(copyMxGraphModel);

        //processPathList
        List<ProcessPath> processPathList = process.getProcessPathList();
        if (null != processPathList && processPathList.size() > 0) {
            List<ProcessPath> copyProcessPathList = new ArrayList<>();
            for (ProcessPath processPath : processPathList) {
                if (null != processPath) {
                    ProcessPath copyProcessPath = new ProcessPath();
                    BeanUtils.copyProperties(processPath, copyProcessPath);
                    copyProcessPath = ProcessPathUtils.initProcessPathBasicPropertiesNoId(copyProcessPath, username);
                    if (isAddId) {
                        copyProcessPath.setId(UUIDUtils.getUUID32());
                    } else {
                        copyProcessPath.setId(null);
                    }
                    copyProcessPath.setProcess(copyProcess);
                    copyProcessPathList.add(copyProcessPath);
                }
            }
            copyProcess.setProcessPathList(copyProcessPathList);
        }

        //processStopList
        List<ProcessStop> processStopList = process.getProcessStopList();
        if (null != processStopList && processStopList.size() > 0) {
            List<ProcessStop> copyProcessStopList = new ArrayList<>();
            for (ProcessStop processStop : processStopList) {
                if (null == processStop) {
                    continue;
                }
                ProcessStop copyProcessStop = new ProcessStop();
                BeanUtils.copyProperties(processStop, copyProcessStop);
                copyProcessStop = ProcessStopUtils.initProcessStopBasicPropertiesNoId(copyProcessStop, username);
                if (isAddId) {
                    copyProcessStop.setId(UUIDUtils.getUUID32());
                } else {
                    copyProcessStop.setId(null);
                }
                copyProcessStop.setStartTime(null);
                copyProcessStop.setEndTime(null);
                copyProcessStop.setState(StopState.INIT);
                copyProcessStop.setProcess(copyProcess);
                List<ProcessStopProperty> processStopPropertyList = processStop.getProcessStopPropertyList();
                if (null != processStopPropertyList && processStopPropertyList.size() > 0) {
                    List<ProcessStopProperty> copyProcessStopPropertyList = new ArrayList<>();
                    for (ProcessStopProperty processStopProperty : processStopPropertyList) {
                        if (null == processStopProperty) {
                            continue;
                        }
                        ProcessStopProperty copyProcessStopProperty = new ProcessStopProperty();
                        BeanUtils.copyProperties(processStopProperty, copyProcessStopProperty);
                        copyProcessStopProperty = ProcessStopPropertyUtils.initProcessStopPropertyBasicPropertiesNoId(copyProcessStopProperty, username);
                        if (isAddId) {
                            copyProcessStopProperty.setId(UUIDUtils.getUUID32());
                        } else {
                            copyProcessStopProperty.setId(null);
                        }
                        copyProcessStopProperty.setSensitive(copyProcessStopProperty.getSensitive());
                        copyProcessStopProperty.setProcessStop(copyProcessStop);
                        copyProcessStopPropertyList.add(copyProcessStopProperty);
                    }
                    copyProcessStop.setProcessStopPropertyList(copyProcessStopPropertyList);
                }
                copyProcessStopList.add(copyProcessStop);
            }
            copyProcess.setProcessStopList(copyProcessStopList);
        }
        return copyProcess;
    }

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
        Map<String, ProcessGroup> processGroupsMap = new HashMap<>();

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

        List<ProcessGroup> processGroupList = processGroup.getProcessGroupList();
        if (null != processGroupList && processGroupList.size() > 0) {
            List<Map<String, Object>> processesGroupListMap = new ArrayList<>();
            for (ProcessGroup processGroupNew : processGroupList) {
                processGroupsMap.put(processGroupNew.getPageId(), processGroupNew);
                Map<String, Object> processGroupMap = processGroupToMap(processGroupNew, runModeType);
                processesGroupListMap.add(processGroupMap);
            }
            flowGroupVoMap.put("groups", processesGroupListMap);

        }

        List<ProcessGroupPath> processGroupPathList = processGroup.getProcessGroupPathList();
        if (null != processGroupPathList && processGroupPathList.size() > 0) {
            List<Map<String, Object>> pathListMap = new ArrayList<>();
            for (ProcessGroupPath processGroupPath : processGroupPathList) {
                if (null != processGroupPath) {
                    Map<String, Object> pathMap = new HashMap<>();
                    String formName = "";
                    String toName = "";
                    String from = processGroupPath.getFrom();
                    String to = processGroupPath.getTo();
                    if (null != processesMap.get(from)) {
                        formName = processesMap.get(from).getName();
                    } else if (null != processGroupsMap.get(from)) {
                        formName = processGroupsMap.get(from).getName();
                    }
                    if (null != processesMap.get(to)) {
                        toName = processesMap.get(to).getName();
                    } else if (null != processGroupsMap.get(to)) {
                        toName = processGroupsMap.get(to).getName();
                    }
                    pathMap.put("after", formName);
                    pathMap.put("entry", toName);
                    pathListMap.add(pathMap);
                }
            }
            flowGroupVoMap.put("conditions", pathListMap);
        }
        rtnMap.put("group", flowGroupVoMap);
        return rtnMap;
    }

}
