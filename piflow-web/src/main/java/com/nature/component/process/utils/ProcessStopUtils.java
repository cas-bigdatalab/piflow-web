package com.nature.component.process.utils;

import com.nature.base.util.UUIDUtils;
import com.nature.component.process.model.ProcessPath;
import com.nature.component.process.model.ProcessStop;
import com.nature.component.process.model.ProcessStopCustomizedProperty;
import com.nature.component.process.model.ProcessStopProperty;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.parameters.P;

import java.util.*;

public class ProcessStopUtils {

    public static ProcessStop processStopNewNoId(String username) {
        ProcessStop processStop = new ProcessStop();
        // basic properties (required when creating)
        processStop.setCrtDttm(new Date());
        processStop.setCrtUser(username);
        // basic properties
        processStop.setEnableFlag(true);
        processStop.setLastUpdateUser(username);
        processStop.setLastUpdateDttm(new Date());
        processStop.setVersion(0L);
        return processStop;
    }

    public static ProcessStop initProcessStopBasicPropertiesNoId(ProcessStop processStop, String username) {
        if (null == processStop) {
            return processStopNewNoId(username);
        }
        // basic properties (required when creating)
        processStop.setCrtDttm(new Date());
        processStop.setCrtUser(username);
        // basic properties
        processStop.setEnableFlag(true);
        processStop.setLastUpdateUser(username);
        processStop.setLastUpdateDttm(new Date());
        processStop.setVersion(0L);
        return processStop;
    }

    public static ProcessStop copyProcessStopBasicPropertiesNoIdAndUnlink(ProcessStop processStop, String username) {
        if (null == processStop) {
            return null;
        }
        // copy ProcessStop
        ProcessStop copyProcessStop = new ProcessStop();
        BeanUtils.copyProperties(processStop, copyProcessStop);
        copyProcessStop = initProcessStopBasicPropertiesNoId(copyProcessStop, username);
        copyProcessStop.setProcess(null);

        // copy processStopPropertyList
        List<ProcessStopProperty> processStopPropertyList = processStop.getProcessStopPropertyList();
        if (null != processStopPropertyList && processStopPropertyList.size() > 0) {
            List<ProcessStopProperty> copyProcessStopPropertyList = new ArrayList<>();
            ProcessStopProperty copyProcessStopProperty;
            for (ProcessStopProperty processStopProperty : processStopPropertyList) {
                if (null == processStopProperty) {
                    continue;
                }
                copyProcessStopProperty = new ProcessStopProperty();
                BeanUtils.copyProperties(processStopProperty, copyProcessStopProperty);
                copyProcessStopProperty = ProcessStopPropertyUtils.initProcessStopPropertyBasicPropertiesNoId(copyProcessStopProperty, username);
                copyProcessStopProperty.setProcessStop(copyProcessStop);
                copyProcessStopProperty.setId(UUIDUtils.getUUID32());
                copyProcessStopPropertyList.add(copyProcessStopProperty);
            }
            copyProcessStop.setProcessStopPropertyList(copyProcessStopPropertyList);
        }

        // copy processStopCustomizedPropertyList
        List<ProcessStopCustomizedProperty> processStopCustomizedPropertyList = processStop.getProcessStopCustomizedPropertyList();
        if (null != processStopCustomizedPropertyList && processStopCustomizedPropertyList.size() > 0) {
            List<ProcessStopCustomizedProperty> copyProcessStopCustomizedPropertyList = new ArrayList<>();
            ProcessStopCustomizedProperty copyProcessStopCustomizedProperty;
            for (ProcessStopCustomizedProperty processStopCustomizedProperty : processStopCustomizedPropertyList) {
                if (null == processStopCustomizedProperty) {
                    continue;
                }
                copyProcessStopCustomizedProperty = new ProcessStopCustomizedProperty();
                BeanUtils.copyProperties(processStopCustomizedProperty, copyProcessStopCustomizedProperty);
                copyProcessStopCustomizedProperty = ProcessStopCustomizedPropertyUtils.initProcessStopCustomizedPropertyBasicPropertiesNoId(copyProcessStopCustomizedProperty, username);
                copyProcessStopCustomizedProperty.setProcessStop(copyProcessStop);
                copyProcessStopCustomizedProperty.setId(UUIDUtils.getUUID32());
                copyProcessStopCustomizedPropertyList.add(copyProcessStopCustomizedProperty);
            }
            copyProcessStop.setProcessStopCustomizedPropertyList(copyProcessStopCustomizedPropertyList);
        }

        return copyProcessStop;
    }

}
