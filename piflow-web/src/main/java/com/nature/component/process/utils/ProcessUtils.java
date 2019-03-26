package com.nature.component.process.utils;

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
}
