package cn.cnic.component.dataProduct.util;

import cn.cnic.base.utils.LoggerUtil;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataProductUtil {

    private static Logger logger = LoggerUtil.getLogger();


    public static List<String> splitDatasetUrl(String input) {
        List<String> result = new ArrayList<>();
        String[] parts = input.split(";");
        for (String part : parts) {
            result.add(part.trim()); // 去除空格并添加到列表
        }
        return result;
    }

    public static  Set<String> findExcelFiles(String[] paths) {
        Set<String> excelFiles = new HashSet<>();
        for (String path : paths) {
            File file = new File(path);
            if (file.exists()) {
                if (file.isDirectory()) {
                    // 如果是文件夹，递归查找.xlsx文件
                    findExcelFilesInDirectory(file, excelFiles);
                } else {
                    // 如果是.xlsx文件，直接添加到列表中
                    if (file.getName().endsWith(".xlsx")) {
                        excelFiles.add(file.getAbsolutePath());
                    }
                }
            } else {
                logger.error("文件或文件夹不存在: " + path);
            }
        }
        return excelFiles;
    }

    private static void findExcelFilesInDirectory(File directory, Set<String> excelFiles) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果是文件夹，递归查找.xlsx文件
                    findExcelFilesInDirectory(file, excelFiles);
                } else {
                    // 如果是.xlsx文件，直接添加到列表中
                    if (file.getName().endsWith(".xlsx")) {
                        excelFiles.add(file.getAbsolutePath());
                    }
                }
            }
        }
    }
}
