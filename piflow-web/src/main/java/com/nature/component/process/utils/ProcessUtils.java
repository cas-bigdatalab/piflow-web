package com.nature.component.process.utils;

import com.nature.base.util.SqlUtils;
import com.nature.base.util.SvgUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Paths;
import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.process.model.Process;
import com.nature.component.process.model.ProcessPath;
import com.nature.component.process.model.ProcessStop;
import com.nature.component.process.model.ProcessStopProperty;
import com.nature.component.process.vo.ProcessPathVo;
import com.nature.component.process.vo.ProcessStopPropertyVo;
import com.nature.component.process.vo.ProcessStopVo;
import com.nature.component.process.vo.ProcessVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcessUtils {

    public static ProcessVo processPoToVo(Process process) {
        ProcessVo processVo = null;
        if (null != process) {
            processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            List<ProcessStop> processStopList = process.getProcessStopList();
            List<ProcessPath> processPathList = process.getProcessPathList();
            if (CollectionUtils.isNotEmpty(processStopList)) {
                List<ProcessStopVo> processStopVos = processStopListPoToVo(processStopList);
                if (CollectionUtils.isNotEmpty(processStopVos)) {
                    processVo.setProcessStopVoList(processStopVos);
                }
            }
            if (CollectionUtils.isNotEmpty(processPathList)) {
                List<ProcessPathVo> processPathVos = processPathListPoToVo(processPathList);
                if (CollectionUtils.isNotEmpty(processPathVos)) {
                    processVo.setProcessPathVoList(processPathVos);
                }
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

    public static Process flowToProcess(Flow flow, UserVo user) {
        Process process = null;
        if (null != flow) {
            String username = (null != user) ? user.getUsername() : "-1";
            process = new Process();
            // copy flow信息到process
            BeanUtils.copyProperties(flow, process);
            // set基本信息
            process.setId(SqlUtils.getUUID32());
            process.setCrtDttm(new Date());
            process.setCrtUser(username);
            process.setLastUpdateDttm(new Date());
            process.setLastUpdateUser(username);
            process.setEnableFlag(true);
            // 取出flow的画板信息
            MxGraphModel mxGraphModel = flow.getMxGraphModel();
            // flow画板信息转为ViewXml
            String viewXml = SvgUtils.mxGraphModelToViewXml(mxGraphModel);
            // set viewXml
            process.setViewXml(viewXml);
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
                    // isEmpty
                    if (null != stops) {
                        ProcessStop processStop = new ProcessStop();
                        // copy stops的信息到processStop中
                        BeanUtils.copyProperties(stops, processStop);
                        // set基本信息
                        processStop.setId(SqlUtils.getUUID32());
                        processStop.setCrtDttm(new Date());
                        processStop.setCrtUser(username);
                        processStop.setLastUpdateDttm(new Date());
                        processStop.setLastUpdateUser(username);
                        processStop.setEnableFlag(true);
                        // 关联外键
                        processStop.setProcess(process);
                        // 取出stops的属性
                        List<Property> properties = stops.getProperties();
                        // stops的属性判空
                        if (null != properties && properties.size() > 0) {
                            List<ProcessStopProperty> processStopPropertyList = new ArrayList<ProcessStopProperty>();
                            // Attributes of loop stops
                            for (Property property : properties) {
                                // isEmpty
                                if (null != property) {
                                    ProcessStopProperty processStopProperty = new ProcessStopProperty();
                                    // Copy property information into processStopProperty
                                    BeanUtils.copyProperties(property, processStopProperty);
                                    // Set basic information
                                    processStopProperty.setId(SqlUtils.getUUID32());
                                    processStopProperty.setCrtDttm(new Date());
                                    processStopProperty.setCrtUser(username);
                                    processStopProperty.setLastUpdateDttm(new Date());
                                    processStopProperty.setLastUpdateUser(username);
                                    processStopProperty.setEnableFlag(true);
                                    // Associated foreign key
                                    processStopProperty.setProcessStop(processStop);
                                    processStopPropertyList.add(processStopProperty);
                                }
                            }
                            processStop.setProcessStopPropertyList(processStopPropertyList);
                        }
                        processStopList.add(processStop);
                    }
                }
                process.setProcessStopList(processStopList);
            }
            // Get the paths information of flow
            List<Paths> pathsList = flow.getPathsList();
            // isEmpty
            if (null != pathsList && pathsList.size() > 0) {
                List<ProcessPath> processPathList = new ArrayList<ProcessPath>();
                // Loop paths information
                for (Paths paths : pathsList) {
                    // isEmpty
                    if (null != paths) {
                        ProcessPath processPath = new ProcessPath();
                        // Copy paths information into processPath
                        BeanUtils.copyProperties(paths, processPath);
                        // Set basic information
                        processPath.setId(SqlUtils.getUUID32());
                        processPath.setCrtDttm(new Date());
                        processPath.setCrtUser(username);
                        processPath.setLastUpdateDttm(new Date());
                        processPath.setLastUpdateUser(username);
                        processPath.setEnableFlag(true);
                        // Associated foreign key
                        processPath.setProcess(process);
                        processPathList.add(processPath);
                    }
                }
                process.setProcessPathList(processPathList);
            }
        }
        return process;
    }
}
