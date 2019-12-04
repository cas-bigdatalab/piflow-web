package com.nature.base.util;

import org.slf4j.Logger;

import java.io.File;

public class CheckPathUtils {

    static Logger logger = LoggerUtil.getLogger();

    public static void isChartPathExist(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
