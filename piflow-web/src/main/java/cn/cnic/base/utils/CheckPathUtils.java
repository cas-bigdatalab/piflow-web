package cn.cnic.base.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class CheckPathUtils {

    public static void isChartPathExist(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if(!mkdirs){
                log.warn("Create failed");
            }
        }
    }
}
