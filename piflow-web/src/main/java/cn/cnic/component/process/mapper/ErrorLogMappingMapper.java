package cn.cnic.component.process.mapper;

import cn.cnic.component.process.entity.ErrorLogMapping;
import cn.cnic.component.process.mapper.provider.ErrorLogMappingMapperProvider;
import cn.cnic.component.process.vo.ErrorLogMappingVo;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

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
            + " and (er.regex_pattern like CONCAT('%', #{keyword}, '%') or er.origin_abstract like CONCAT('%', #{keyword}, '%') or er.name like CONCAT('%', #{keyword}, '%'))"
            + "</if>"
            + "</where>"
            + "</script>")
    List<ErrorLogMappingVo> getByPage(@Param("keyword") String keyword);

    @Select("select CAST(er.id as CHAR) as id, er.* from error_log_mapping as er where er.enable_flag = 1 and er.flow_publish_id like CONCAT('%', #{flowPublishId}, '%') and er.regex_pattern is not null")
    List<ErrorLogMappingVo> getAllAvailable(@Param("flowPublishId") String flowPublishId);
}
