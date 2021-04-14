package cn.cnic.component.process.utils;

import cn.cnic.base.util.UUIDUtils;
import cn.cnic.component.flow.entity.Paths;
import cn.cnic.component.process.entity.ProcessPath;
import org.springframework.beans.BeanUtils;

import java.util.Date;

public class ProcessPathUtils {

    public static ProcessPath processPathNewNoId(String username) {
        ProcessPath processPath = new ProcessPath();
        // basic properties (required when creating)
        processPath.setCrtDttm(new Date());
        processPath.setCrtUser(username);
        // basic properties
        processPath.setEnableFlag(true);
        processPath.setLastUpdateUser(username);
        processPath.setLastUpdateDttm(new Date());
        processPath.setVersion(0L);
        return processPath;
    }

    public static ProcessPath initProcessPathBasicPropertiesNoId(ProcessPath processPath, String username) {
        if (null == processPath) {
            return processPathNewNoId(username);
        }
        // basic properties (required when creating)
        processPath.setCrtDttm(new Date());
        processPath.setCrtUser(username);
        // basic properties
        processPath.setEnableFlag(true);
        processPath.setLastUpdateUser(username);
        processPath.setLastUpdateDttm(new Date());
        processPath.setVersion(0L);
        return processPath;
    }

    public static ProcessPath copyProcessPathBasicPropertiesNoIdAndUnlink(ProcessPath processPath, String username) {
        if (null == processPath) {
            return null;
        }
        // ProcessPath
        ProcessPath copyProcessPath = new ProcessPath();
        BeanUtils.copyProperties(processPath, copyProcessPath);
        copyProcessPath = initProcessPathBasicPropertiesNoId(copyProcessPath,username);
        copyProcessPath.setProcess(null);
        return copyProcessPath;
    }
    
    public static ProcessPath pathsToProcessPath(Paths paths, String username, boolean isAddId) {

        // isEmpty
        if (null == paths) {
            return null;
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
        
        return processPath;
    }

}
