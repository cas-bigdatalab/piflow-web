package cn.cnic.component.visual.mapper.piflowdata;

import cn.cnic.component.visual.entity.DataBaseInfo;
import cn.cnic.component.visual.service.impl.ExcelSourceServiceImpl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.Map;

/**
 * TODO
 * author:hmh
 * date:2023-11-03
 */
@Mapper
public interface ExcelSourceMapper extends BaseMapper<DataBaseInfo> {
    @Update("CREATE TABLE ${tableName}(\n" +
            "    data_id INT NOT NULL AUTO_INCREMENT  COMMENT 'data_id' ,\n" +
            "    ${columnContent}" +
            "    PRIMARY KEY (data_id)\n" +
            ");\n ")
    int createDataTable(String tableName,String columnContent);
    @InsertProvider(type = ExcelSourceServiceImpl.class,method = "insertDataTableSql")
    int insertDataTable(String tableName,Sheet sheet);
    @Update("drop table ${tableName};")
    int delDataTable(String tableName);
    @Select("select * from ${tableName} limit 100;")
    List<Map> selectExcelData(String tableName);
    @Select("show tables;")
    List<String> selectExcel();
    @Select("desc ${tableName};")
    List<String> getExcelCol(String tableName);
    @Select("select * from ${tableName};")
    List<Map> selectExcelTableData(String tableName);

}
