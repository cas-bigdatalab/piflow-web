package cn.cnic.base.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

public class ExcelUtils {

    private static Logger logger = LoggerUtil.getLogger();

    /**
     * 把对象中的属性依次写入到excel文件中,写到第一个sheet, 第一行
     *
     * @param pojo
     * @param excelFilePath
     * @throws IOException
     * @throws IllegalAccessException
     */
    public static void writePojoToExcel(Object pojo, String excelFilePath) throws IOException, IllegalAccessException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        Row row = sheet.createRow(0);

        Field[] fields = pojo.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            Object value = fields[i].get(pojo);

            Cell cell = row.createCell(i);
            if (value != null) {
                cell.setCellValue(value.toString());
            } else {
                cell.setCellValue("");
            }
        }

        try (FileOutputStream fos = new FileOutputStream(excelFilePath)) {
            workbook.write(fos);
        }

        workbook.close();
    }

    /**
     * 把对象中的属性依次追加到excel文件中,写到第一个sheet, 最后一行
     * @param pojo
     * @param excelFilePath
     * @param originFilePath
     * @throws IOException
     * @throws IllegalAccessException
     */
    public static void appendPojoToExcel(Object pojo, String excelFilePath, String originFilePath) throws IOException, IllegalAccessException {
        Workbook workbook;
        Sheet sheet;
        try (FileInputStream fis = new FileInputStream(originFilePath)) {
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
        } catch (IOException e) {

            e.printStackTrace();
            logger.error("读取源文件失败", e);
            return;
        }

        int rowIndex = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(rowIndex);

        Field[] fields = pojo.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            Object value = fields[i].get(pojo);

            Cell cell = row.createCell(i);
            if (value != null) {
                cell.setCellValue(value.toString());
            } else {
                cell.setCellValue("");
            }
        }

        try (FileOutputStream fos = new FileOutputStream(excelFilePath)) {
            workbook.write(fos);
        }

        workbook.close();
    }

    public static void appendIconAndDocumentPathToExcel(String str1, String str2, String filePath) throws IOException {
        File excelFile = new File(filePath);

        // 检查文件是否存在且是Excel文件
        if (!excelFile.exists() || !(filePath.endsWith(".xls") || filePath.endsWith(".xlsx"))) {
            throw new IllegalArgumentException("文件不存在或不是Excel文件");
        }

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            // 检查是否只有一个Sheet
            if (workbook.getNumberOfSheets() != 1) {
                throw new IllegalArgumentException("Excel文件必须只有一个Sheet");
            }

            Sheet sheet = workbook.getSheetAt(0);

            // 检查是否只有一行
            if (sheet.getLastRowNum() != 0) {
                throw new IllegalArgumentException("Excel文件必须只有一行");
            }

            Row row = sheet.getRow(0);
            if (row == null) {
                row = sheet.createRow(0);
            }

            // 获取最后一列的索引
            int lastCellNum = row.getLastCellNum() == -1 ? 0 : row.getLastCellNum();

            // 在最后两列写入字符串
            Cell cell1 = row.createCell(lastCellNum);
            cell1.setCellValue(str1);

            Cell cell2 = row.createCell(lastCellNum + 1);
            cell2.setCellValue(str2);

            // 写回文件
            try (FileOutputStream fos = new FileOutputStream(excelFile)) {
                workbook.write(fos);
            }
        }
    }
}
