package cn.cnic.component.process.mapper;

import cn.cnic.component.process.entity.ErrorLogMapping;
import cn.cnic.component.process.mapper.provider.ErrorLogMappingMapperProvider;
import cn.cnic.component.process.vo.ErrorLogMappingVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ErrorLogMappingMapper {

    @InsertProvider(type = ErrorLogMappingMapperProvider.class, method = "insert")
    int insert(ErrorLogMapping errorLogMapping);

    @UpdateProvider(type = ErrorLogMappingMapperProvider.class, method = "update")
    int update(ErrorLogMapping errorLogMapping);

    @UpdateProvider(type = ErrorLogMappingMapperProvider.class, method = "delete")
    int delete(ErrorLogMapping errorLogMapping);

    @Select("select CAST(er.id as CHAR) as id, er.* from error_log_mapping as er where id = #{id} and enable_flag = 1")
    ErrorLogMappingVo getById(@Param("id") Long id);

    @Select("<script>"
            + "select CAST(er.id as CHAR) as id, er.* from error_log_mapping as er"
            + "<where>"
            + " er.enable_flag = 1"
            + "<if test=\"keyword != null and keyword != '' \">"
            + " and er.name like CONCAT('%', #{keyword}, '%')"
            + "</if>"
            + "<if test=\"username != null and username != '' \">"
            + " and er.crt_user = #{username}"
            + "</if>"
            + "</where>"
            + "</script>")
    List<ErrorLogMappingVo> getByPage(@Param("keyword") String keyword, @Param("username") String username);

    @Select("select id, name, explain_zh, regex_pattern from error_log_mapping where enable_flag = 1 and regex_pattern is not null")
    List<ErrorLogMapping> getAllAvailable();
}
