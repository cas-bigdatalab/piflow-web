package cn.cnic.component.visual.service.impl;

import cn.cnic.base.utils.FileUtils;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.component.visual.entity.ExcelNameAsso;
import cn.cnic.component.visual.entity.GraphTemplate;
import cn.cnic.component.visual.entity.ProductTemplateGraphAssoDto;
import cn.cnic.component.visual.mapper.piflowdata.ExcelSourceMapper;
import cn.cnic.component.visual.mapper.piflowdb.ExcelNameAssoMapper;
import cn.cnic.component.visual.mapper.piflowdb.GraphConfMapper;
import cn.cnic.component.visual.mapper.piflowdb.GraphTemplateMapper;
import cn.cnic.component.visual.mapper.piflowdb.ProductTemplateGraphAssoMapper;
import cn.cnic.component.visual.service.ExcelSourceService;
import cn.cnic.component.visual.util.RequestData;
import cn.cnic.component.visual.util.ResponseResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static cn.cnic.component.dataProduct.util.DataProductUtil.getFileNameFromPath;

/**
 * TODO
 * author:hmh
 * date:2023-11-03
 */
@Service
public class ExcelSourceServiceImpl implements ExcelSourceService {

    private static Logger logger = LoggerUtil.getLogger();

    @Autowired
    private ExcelSourceMapper excelSourceMapper;
    @Autowired
    private GraphConfMapper graphConfMapper;
    @Autowired
    private GraphTemplateMapper graphTemplateMapper;
    @Autowired
    private ExcelNameAssoMapper excelNameAssoMapper;
    @Autowired
    private ProductTemplateGraphAssoMapper productTemplateGraphAssoMapper;


    /**
     *
     * @param path 路径
     * @param graphTemplate
     * @return 上传成功后的graphTemplateId,即vis_graph_template表的id
     */
    @Override
    public ResponseResult uploadExcelFromPath(String path, GraphTemplate graphTemplate) {
        //接excel数据
        InputStream inputStream = null;
        Workbook workbook = null;
        String newName = null;
        int createTable = 0; //建表成功标记
        int graphTemplateId = 0;
        try {
            //校验模板名
            String templateName = graphTemplate.getName();
            if((null == templateName) || ("".equals(templateName))){
                return ResponseResult.error("从路径上传失败!" + "该模板名称不能为空！");
            }else{
                //判断模板名是否已存在
                if(checkTemplateName(templateName)){
                    return ResponseResult.error("从路径上传失败!" + "该模板名称已存在！");
                }
            }

//            File file = new File(path);
//            // 创建FileInputStream对象，以便从文件读取数据
//            FileInputStream fis = new FileInputStream(file);
//            // 创建BufferedInputStream对象，用于提高读取效率
//            BufferedInputStream bis = new BufferedInputStream(fis);
//            // 通过BufferedInputStream获取InputStream对象
//           inputStream = new DataInputStream(bis);

            inputStream = FileUtils.getFileInputStream(path, FileUtils.getDefaultFs());

            String originalFilename = getFileNameFromPath(path);

            workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = null;
            //判断是否填写sheetName
            if((null != graphTemplate.getSheetName()) && (!"".equals(graphTemplate.getSheetName()))){
                //根据sheet名称取获取表格内容
                sheet = workbook.getSheet(graphTemplate.getSheetName());
                if(null == sheet){
                    return ResponseResult.error("从路径上传失败!" + "输入的sheet页名在表格中不存在，请核对表格！");
                }
            }else{
                //未填写，则默认取第一个sheet
                sheet = workbook.getSheetAt(0);
                //保存sheet名称
                graphTemplate.setSheetName(workbook.getSheetName(0));
            }
            Row row1 = sheet.getRow(0);
            Iterator<Cell> iterator = row1.iterator();
            StringBuffer columnContentB = new StringBuffer("");
            while (iterator.hasNext()){
                String column = iterator.next().toString();
                column = column.trim();
                if(column.lastIndexOf("（") != -1){
                    column = column.substring(0,column.lastIndexOf("（"));
                }
                columnContentB.append(column).append(" TEXT, ");
            }
            String columnContent = columnContentB.toString();
            //定义表名
            newName = "vs_table_" + System.nanoTime();
            //取表头，新建表
            createTable = excelSourceMapper.createDataTable(newName,columnContent);
            //存execl数据
            excelSourceMapper.insertDataTable(newName,sheet);
            //存储excel表名关联数据
            ExcelNameAsso excelNameAssoNew = new ExcelNameAsso();
            excelNameAssoNew.setExcelName(originalFilename);
            excelNameAssoNew.setAssoName(newName);
            excelNameAssoNew.setCreateTime(getNowTime());
            excelNameAssoNew.setUpdateTime(getNowTime());
            excelNameAssoMapper.insert(excelNameAssoNew);
            //保存图表模板，上传excel表格，默认传到自己库
            QueryWrapper<ExcelNameAsso> wrapper = new QueryWrapper<>();
            wrapper.eq("asso_name",newName);
            ExcelNameAsso excelNameAsso = excelNameAssoMapper.selectOne(wrapper);
            graphTemplate.setDataBaseId(0);//写为固定值
            graphTemplate.setTableName(originalFilename);//excel原名作为表名
            graphTemplate.setExcelAssoId(excelNameAsso.getId());//设置新增的excel名称关联表id
            graphTemplate.setCreateTime(getNowTime());
            graphTemplate.setUpdateTime(getNowTime());
            graphTemplateMapper.insert(graphTemplate);
            //插入成功之后会修改pojo,设置Id属性的值
            graphTemplateId = graphTemplate.getId();
        } catch (Exception e) {
//            throw new RuntimeException();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            //删除创建的表
            if((newName != null) && createTable > 0){
                excelSourceMapper.delDataTable(newName);
            }
            return ResponseResult.error("从路径上传失败!" + "上传失败！");
        }finally {
            try{
                if(null != inputStream){
                    inputStream.close();

                }
                if(null != workbook){
                    workbook.close();
                }
            }catch (Exception e){
                e.printStackTrace();
                return ResponseResult.error("从路径上传失败!" + "未知失败！");
            }
        }
        return ResponseResult.success(graphTemplateId);
    }

    @Override
    public ResponseResult checkProductExist(Long productId, String path, String username) {
        QueryWrapper<ProductTemplateGraphAssoDto> wrapper = new QueryWrapper<>();
        wrapper.eq("product_id", productId);
        wrapper.eq("owner", username);
        wrapper.eq("path", path);
        Map<String, Object> resMap = new HashMap<>();
        ProductTemplateGraphAssoDto res = productTemplateGraphAssoMapper.selectOne(wrapper);
        if (res == null || res.getId() <= 0) {
            resMap.put("hasAdded", false);
        } else {
            resMap.put("hasAdded", true);
            //这里要返回可视化id,用来进入可视化页面
            resMap.put("graphConfigId", res.getGraphConfId());
        }
        return ResponseResult.success(resMap);
    }

    @Override
    @Transactional
    public ResponseResult uploadExcel(MultipartFile file, GraphTemplate graphTemplate) {
        //接excel数据
        InputStream inputStream = null;
        Workbook workbook = null;
        String newName = null;
        int createTable = 0; //建表成功标记
        try {
            //校验模板名
            String templateName = graphTemplate.getName();
            if((null == templateName) || ("".equals(templateName))){
                return ResponseResult.error("该模板名称不能为空！");
            }else{
                //判断模板名是否已存在
                if(checkTemplateName(templateName)){
                    return ResponseResult.error("该模板名称已存在！");
                }
            }
            inputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();

            workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = null;
            //判断是否填写sheetName
            if((null != graphTemplate.getSheetName()) && (!"".equals(graphTemplate.getSheetName()))){
                //根据sheet名称取获取表格内容
                sheet = workbook.getSheet(graphTemplate.getSheetName());
                if(null == sheet){
                    return ResponseResult.error("输入的sheet页名在表格中不存在，请核对表格！");
                }
            }else{
                //未填写，则默认取第一个sheet
                sheet = workbook.getSheetAt(0);
                //保存sheet名称
                graphTemplate.setSheetName(workbook.getSheetName(0));
            }
            Row row1 = sheet.getRow(0);
            Iterator<Cell> iterator = row1.iterator();
            StringBuffer columnContentB = new StringBuffer("");
            while (iterator.hasNext()){
                String column = iterator.next().toString();
                column = column.trim();
                if(column.lastIndexOf("（") != -1){
                    column = column.substring(0,column.lastIndexOf("（"));
                }
                columnContentB.append(column).append(" TEXT, ");
            }
            String columnContent = columnContentB.toString();
            //定义表名
            newName = "vs_table_" + System.nanoTime();
            //取表头，新建表
            createTable = excelSourceMapper.createDataTable(newName,columnContent);
            //存execl数据
            excelSourceMapper.insertDataTable(newName,sheet);
            //存储excel表名关联数据
            ExcelNameAsso excelNameAssoNew = new ExcelNameAsso();
            excelNameAssoNew.setExcelName(originalFilename);
            excelNameAssoNew.setAssoName(newName);
            excelNameAssoNew.setCreateTime(getNowTime());
            excelNameAssoNew.setUpdateTime(getNowTime());
            excelNameAssoMapper.insert(excelNameAssoNew);
            //保存图表模板，上传excel表格，默认传到自己库
            QueryWrapper<ExcelNameAsso> wrapper = new QueryWrapper<>();
            wrapper.eq("asso_name",newName);
            ExcelNameAsso excelNameAsso = excelNameAssoMapper.selectOne(wrapper);
            graphTemplate.setDataBaseId(0);//写为固定值
            graphTemplate.setTableName(originalFilename);//excel原名作为表名
            graphTemplate.setExcelAssoId(excelNameAsso.getId());//设置新增的excel名称关联表id
            graphTemplate.setCreateTime(getNowTime());
            graphTemplate.setUpdateTime(getNowTime());
            graphTemplateMapper.insert(graphTemplate);
        } catch (Exception e) {
//            throw new RuntimeException();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            //删除创建的表
            if((newName != null) && createTable > 0){
                excelSourceMapper.delDataTable(newName);
            }
            return ResponseResult.error("上传失败！");
        }finally {
            try{
                if(null != inputStream){
                    inputStream.close();

                }
                if(null != workbook){
                    workbook.close();
                }
            }catch (Exception e){
                e.printStackTrace();
                return ResponseResult.error("未知失败！");
            }
        }
        return ResponseResult.success();
    }

    @Override
    public ResponseResult delExcel(RequestData requestData) {
        final String tableName = requestData.getTableName();
        int result = -1;
        if(null != tableName && !"".equals(tableName)){
            result = excelSourceMapper.delDataTable(tableName);
        }
        if(result != -1){
            //删除对应配置
            int count = graphConfMapper.deleteByName(tableName);
            return ResponseResult.success();
        }else {
            return ResponseResult.error("删除失败！");
        }
    }

    @Override
    public ResponseResult<List<Map>> selectExcelData(RequestData requestData) {
        final String tableName = requestData.getTableName();
        if(null != tableName && !"".equals(tableName)){
            List<Map> list  = excelSourceMapper.selectExcelData(tableName);
            return ResponseResult.success(list,list.size());
        }else {
            return ResponseResult.error("查询失败！");
        }
    }

    @Override
    public ResponseResult<List<String>> selectExcel() {
        List<String> list = excelSourceMapper.selectExcel();
        return ResponseResult.success(list,list.size());
    }

    @Override
    public ResponseResult<List<String>> getExcelCol(RequestData requestData) {
        List<String> list = excelSourceMapper.getExcelCol(requestData.getTableName());
        return ResponseResult.success(list,list.size());
    }

    public static String insertDataTableSql(String tableName, Sheet sheet){
        StringBuffer sql = new StringBuffer("insert into " + tableName + " ( ");
        StringBuffer values = new StringBuffer(" values ");
        int rowCount  = sheet.getPhysicalNumberOfRows();
        int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
        for (int i = 0; i < rowCount; i++) {
            if(i > 0){
                values.append(" ( ");
            }
            // 获取当前行对象
            Row row = sheet.getRow(i);
            if (row != null) {
                //拼接字段名
                // 遍历当前行的每个单元格
                for (int j = 0; j < columnCount; j++) {
                    // 获取当前单元格对象
                    Cell cell = row.getCell(j);
                    // 获取当前单元格中的值
                    String cellValue = "";
                    if (cell != null) {
                        CellType cellTypeEnum = cell.getCellTypeEnum();
                        if(cellTypeEnum == CellType.NUMERIC){
                            cellValue = String.valueOf(date(cell));
                        }else {
                            cellValue = cell.toString();
                        }
                    }
                    if(i == 0){
                        cellValue = cellValue.trim();
                        if(cellValue.lastIndexOf("（") != -1){
                            cellValue = cellValue.substring(0,cellValue.lastIndexOf("（"));
                        }
                        //拼字段
                        sql.append(cellValue).append(",");
                    }else {
                        //拼值
                        values.append("'").append(cellValue).append("',");
                    }
                }
            }
            if(i > 0){
                values.deleteCharAt(values.length() - 1);
                values.append(" ),");
            }

        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" ) ");
        values.deleteCharAt(values.length() - 1);
        values.append(";");
        sql.append(values);
        return sql.toString();
    }

    /**
     * 测试事务
     * @return
     */
    @Transactional
    public String testTransactional(){
        try {
//            excelSourceMapper.createDataTable("test","");
//            int i = 1/0;
//            excelSourceMapper.createDataTable("test1","");
            GraphTemplate a = new GraphTemplate();
            a.setTableName("test");
            a.setType("test");
            a.setDataBaseId(1);
            a.setName("test");
            a.setCreateTime("test");
            a.setUpdateTime("test");
            graphTemplateMapper.insert(a);
            int j = 1/0;
            graphTemplateMapper.insert(a);
        }catch (Exception e){
            e.printStackTrace();
//            throw new  RuntimeException();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        }

        return "test";
    }

    /**
     * 判断是否是日期类型
     * @param cell
     * @return
     */
    private static Object date(Cell cell) {
        Object cellValue;
        if (DateUtil.isCellDateFormatted(cell)) {
            cellValue = cell.getDateCellValue();
            cellValue = cellDate(cellValue);
        } else {
            //数字
            cell.setCellType(CellType.STRING);
            cellValue = cell.getRichStringCellValue();

        }
        return String.valueOf(cellValue);
    }

    /**
     * 格式化日期
     * @param cellValue
     * @return
     */
    private static Object cellDate(Object cellValue) {
        DateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
        cellValue = formater.format(cellValue);
        return cellValue;
    }

    /**
     * 格式化当前时间
     * @return
     */
    private String getNowTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String format = now.format(formatter);
        return format;
    }

    public boolean checkTemplateName(String templateName){
        QueryWrapper<GraphTemplate> wrapper = new QueryWrapper<>();
        wrapper.eq("name",templateName);
        GraphTemplate graphTemplate = graphTemplateMapper.selectOne(wrapper);
        if(null != graphTemplate){
            return true;
        }else {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println(System.nanoTime());
        System.out.println(UUID.randomUUID());
    }
}
