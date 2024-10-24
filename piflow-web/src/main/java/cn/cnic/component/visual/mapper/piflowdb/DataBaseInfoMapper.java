package cn.cnic.component.visual.mapper.piflowdb;

import cn.cnic.component.visual.entity.DataBaseInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * TODO
 * author:hmh
 * date:2023-11-13
 */
@Mapper
public interface DataBaseInfoMapper extends BaseMapper<DataBaseInfo> {
    @Select("select * from ${tableName};")
    List<Map> selectTableData(String tableName);
    @Select("show tables;")
    List<String> showTables();
    @Select("desc ${tableName};")
    List<String> getTableCol(String tableName);
}
