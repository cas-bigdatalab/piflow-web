package cn.cnic.component.system.mapper;

import cn.cnic.component.system.entity.File;
import cn.cnic.component.system.mapper.provider.FileMapperProvider;
import cn.cnic.component.system.vo.FileVo;
import cn.cnic.component.system.vo.SystemInfoVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface SystemInfoMapper {

    @Update("update sys_info set description = #{description} where id = #{id}")
    int update(@Param("description") String description, @Param("id") String id);

    @Select("select * from sys_info")
    SystemInfoVo select();
}
