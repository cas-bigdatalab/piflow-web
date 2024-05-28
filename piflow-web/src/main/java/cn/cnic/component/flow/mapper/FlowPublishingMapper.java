package cn.cnic.component.flow.mapper;

import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.FlowPublishing;
import cn.cnic.component.flow.mapper.provider.FlowMapperProvider;
import cn.cnic.component.flow.mapper.provider.FlowPublishingMapperProvider;
import cn.cnic.component.flow.vo.FlowPublishingVo;
import cn.cnic.component.flow.vo.FlowVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface FlowPublishingMapper {

    @Select("select * from flow_publishing where name = #{name} and flow_id = #{flowId} and enable_flag = 1")
    FlowPublishing getByNameAndFlowId(@Param("name") String name, @Param("flowId") String flowId);

    @InsertProvider(type = FlowPublishingMapperProvider.class, method = "insert")
    int insert(FlowPublishing flowPublishing);

    @UpdateProvider(type = FlowPublishingMapperProvider.class, method = "update")
    int update(FlowPublishing flowPublishing);

    @UpdateProvider(type = FlowPublishingMapperProvider.class, method = "updateSort")
    int updateSort(FlowPublishing flowPublishing);

    @SelectProvider(type = FlowPublishingMapperProvider.class, method = "getFullInfoById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "properties", many = @Many(select = "cn.cnic.component.flow.mapper.FlowStopsPublishingPropertyMapper.getStopsListByPublishingId", fetchType = FetchType.LAZY)),
    })
    FlowPublishing getFullInfoById(@Param("id") String id);

    @Select("<script>"
            + "select CAST(fp.id AS CHAR) AS id, fp.* from flow_publishing as fp "
            + "<where>"
            + " fp.enable_flag = 1"
            + "<if test=\"keyword != null and keyword != '' \">"
            + " and `name` like CONCAT('%', #{keyword}, '%')"
            + "</if>"
            + "</where>"
            + "</script>")
    @Results({
            @Result(column = "id", property = "id", javaType = String.class, jdbcType = JdbcType.BIGINT)
    })
    List<FlowPublishingVo> getByKeywords(@Param("keyword") String keyword);

    @Update("update flow_publishing set enable_flag = 0 where id = #{id}")
    int delete(@Param("id") long id);

    @Select("<script>"
            + "select CAST(fp.id AS CHAR) AS id, fp.* from flow_publishing as fp "
            + "<where>"
            + " fp.product_type_id = #{productTypeId} and fp.enable_flag = 1"
            + "<if test=\"keyword != null and keyword != '' \">"
            + " and `name` like CONCAT('%', #{keyword}, '%')"
            + "</if>"
            + "</where>"
            + "</script>")
    @Results({
            @Result(column = "id", property = "id", javaType = String.class, jdbcType = JdbcType.BIGINT)
    })
    List<FlowPublishingVo> getListByProductTypeId(@Param("keyword") String keyword, @Param("productTypeId") Long productTypeId);

    @Select("<script>"
            + "select CAST(fp.id AS CHAR) AS id, fp.* from flow_publishing as fp "
            + "<where>"
            + " fp.product_type_id in "
            + "<foreach collection='productTypeIds' item='item' index='index' open='(' close=')' separator=','>"
            + "#{item}"
            + "</foreach>"
            + "<if test=\"selectedFlowPublishingIds != null and selectedFlowPublishingIds.size() > 0 \">"
            + " and fp.id in "
            + "<foreach collection='selectedFlowPublishingIds' item='item' index='index' open='(' close=')' separator=','>"
            + "#{item}"
            + "</foreach>"
            + "</if>"
            + " and fp.enable_flag = 1"
            + "<if test=\"keyword != null and keyword != '' \">"
            + " and `name` like CONCAT('%', #{keyword}, '%')"
            + "</if>"
            + "</where>"
            + "</script>")
    @Results({
            @Result(column = "id", property = "id", javaType = String.class, jdbcType = JdbcType.BIGINT)
    })
    List<FlowPublishingVo> getListByProductTypeIds(@Param("keyword") String keyword, @Param("productTypeIds") List<Long> productTypeIds, @Param("selectedFlowPublishingIds") List<String> selectedFlowPublishingIds);
}
