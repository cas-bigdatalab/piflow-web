package cn.cnic.component.process.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.cnic.base.util.UUIDUtils;
import cn.cnic.component.process.entity.ProcessStop;
import cn.cnic.component.process.entity.ProcessStopCustomizedProperty;
import cn.cnic.component.process.entity.ProcessStopProperty;
import cn.cnic.component.stopsComponent.model.StopsComponent;
import cn.cnic.component.stopsComponent.model.StopsComponentProperty;

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
        processStop.setId(null);
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

    public static ProcessStop copyStopsComponentToProcessStop(StopsComponent stopsComponent, String username, boolean isAddId) {
        if (null == stopsComponent) {
            return null;
        }
        ProcessStop copyProcessStop = new ProcessStop();
        // Copy stops information into processStop
        BeanUtils.copyProperties(stopsComponent, copyProcessStop);
        // Set basic information
        copyProcessStop = ProcessStopUtils.initProcessStopBasicPropertiesNoId(copyProcessStop, username);
        if (isAddId) {
            copyProcessStop.setId(UUIDUtils.getUUID32());
        } else {
            copyProcessStop.setId(null);
        }
        // Remove the properties of stops
        List<StopsComponentProperty> properties = stopsComponent.getProperties();
        // Determine if the stops attribute is empty
        if (null != properties && properties.size() > 0) {
            List<ProcessStopProperty> processStopPropertyList = new ArrayList<>();
            // Attributes of loop stops
            for (StopsComponentProperty property : properties) {
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
                // Associated foreign key
                processStopProperty.setProcessStop(copyProcessStop);
                processStopPropertyList.add(processStopProperty);
            }
            copyProcessStop.setProcessStopPropertyList(processStopPropertyList);
        }
        return copyProcessStop;
    }

    
}
