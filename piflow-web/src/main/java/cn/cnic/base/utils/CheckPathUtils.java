package cn.cnic.base.utils;


import java.io.File;

import org.slf4j.Logger;


public class CheckPathUtils {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private static Logger logger = LoggerUtil.getLogger();

    public static void isChartPathExist(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if(!mkdirs){
                logger.warn("Create failed");
            }
        }
    }
}
