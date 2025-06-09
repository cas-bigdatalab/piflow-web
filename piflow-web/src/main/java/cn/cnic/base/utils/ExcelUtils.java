package cn.cnic.base.utils;

import cn.cnic.common.interfaces.ExcelColumn;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
    /**
     * 将对象列表写入 Excel 的指定 Sheet 并通过 HttpServletResponse 输出
     *
     * @param pojoList 用户对象列表
     * @param response HTTP 响应对象
     * @param sheetName 指定的 Sheet 名称，默认为 "Sheet1"
     * @param fileName 自定义输出文件名（含 .xlsx）
     * @throws IOException 如果写入失败
     * @throws IllegalAccessException 反射访问字段失败
     */
    public static void writeListToExcel(List<?> pojoList, HttpServletResponse response, String sheetName, String fileName)
            throws IOException, IllegalAccessException {

        if (pojoList == null || pojoList.isEmpty()) {
            logger.warn("提供的对象列表为空，未生成 Excel 文件");
            return;
        }

        Class<?> clazz = pojoList.get(0).getClass();
        List<Field> excelFields = new ArrayList<>();
        Map<Integer, String> headerMap = new LinkedHashMap<>();

        // 获取带有 @ExcelColumn 的字段
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                excelFields.add(field);
                headerMap.put(excelFields.size() - 1, annotation.value());
            }
        }

        // 写入表头
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        Row headerRow = sheet.createRow(0);
        for (Map.Entry<Integer, String> entry : headerMap.entrySet()) {
            Cell cell = headerRow.createCell(entry.getKey());
            cell.setCellValue(entry.getValue());
        }

        // 写入数据行
        int rowNum = 1;
        for (Object pojo : pojoList) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < excelFields.size(); i++) {
                Field field = excelFields.get(i);
                field.setAccessible(true);
                Object value = field.get(pojo);

                Cell cell = row.createCell(i);
                if (value != null) {
                    if (value instanceof Date) {
                        cell.setCellValue(DateUtils.dateTimesToStr((Date) value));
                    } else {
                        cell.setCellValue(value.toString());
                    }
                } else {
                    cell.setCellValue("");
                }
            }
        }

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(), StandardCharsets.UTF_8));

        // 输出到响应流
        try (OutputStream os = response.getOutputStream()) {
            workbook.write(os);
            logger.info("成功导出 {} 条记录到 Excel 文件：{}", pojoList.size(), fileName);
        } catch (IOException e) {
            logger.error("写入 Excel 文件失败，文件名：{}", fileName, e);
            throw e;
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                logger.warn("关闭 Excel 工作簿时发生警告", e);
            }
        }
    }



}
