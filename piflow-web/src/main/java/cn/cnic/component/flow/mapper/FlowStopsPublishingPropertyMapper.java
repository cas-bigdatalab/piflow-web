package cn.cnic.component.flow.mapper;

import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.FlowStopsPublishingProperty;
import cn.cnic.component.flow.mapper.provider.FlowMapperProvider;
import cn.cnic.component.flow.vo.FlowVo;
import cn.cnic.component.flow.vo.StopPublishingVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface FlowStopsPublishingPropertyMapper {


    @Insert("<script>"
            + "insert into flow_stops_publishing_property (id, publishing_id, stop_id, stop_name, stop_bundle, property_id, property_name, `name`, allowable_values, custom_value, description, " +
            "property_sort, example, `type`, crt_dttm, crt_user, enable_flag, last_update_dttm, last_update_user, version, bak1, bak2, bak3) values "
            + "<foreach collection='list' item='item' index='index' separator=','>"
            + "(#{item.id}, #{item.publishingId}, #{item.stopId}, #{item.stopName}, #{item.stopBundle}, #{item.propertyId}, #{item.propertyName}, #{item.name}, #{item.allowableValues}, #{item.customValue}, " +
            "#{item.description}, #{item.propertySort}, #{item.example}, #{item.type}, #{item.crtDttmStr}, #{item.crtUser}, #{item.enableFlagNum}, #{item.lastUpdateDttmStr}, " +
            "#{item.lastUpdateUser}, 0, #{item.bak1}, #{item.bak2}, #{item.bak3})"
            + "</foreach>"
            + "</script>")
    int insertBatch(List<FlowStopsPublishingProperty> properties);

    @Update("<script>"
            + "update flow_stops_publishing_property "
            + "<trim prefix='set' suffixOverrides=','>"
            + "<trim prefix='name=case' suffix='end,'>"
            + "<foreach collection='list' item='i' index='index'>"
            + "<if test=\"i.name != null and i.name != '' \">"
            + " when id=#{i.id} then #{i.name} "
            + "</if>"
            + "</foreach>"
            + "</trim>"
            + "<trim prefix='description=case' suffix='end,'>"
            + "<foreach collection='list' item='i' index='index'>"
            + "<if test=\"i.description != null and i.description != '' \">"
            + " when id=#{i.id} then #{i.description} "
            + "</if>"
            + "</foreach>"
            + "</trim>"
            + "<trim prefix='custom_value=case' suffix='end,'>"
            + "<foreach collection='list' item='i' index='index'>"
            + "<if test=\"i.customValue != null and i.customValue != '' \">"
            + " when id=#{i.id} then #{i.customValue} "
            + "</if>"
            + "</foreach>"
            + "</trim>"
            + "<trim prefix='type=case' suffix='end,'>"
            + "<foreach collection='list' item='i' index='index'>"
            + "<if test=\"i.type != null \">"
            + " when id=#{i.id} then #{i.type} "
            + "</if>"
            + "</foreach>"
            + "</trim>"
            + "<trim prefix='property_sort=case' suffix='end,'>"
            + "<foreach collection='list' item='i' index='index'>"
            + "<if test=\"i.propertySort != null \">"
            + " when id=#{i.id} then #{i.propertySort} "
            + "</if>"
            + "</foreach>"
            + "</trim>"
            + "<trim prefix='bak1=case' suffix='end,'>"
            + "<foreach collection='list' item='i' index='index'>"
            + "<if test=\"i.bak1 != null and i.bak1 != '' \">"
            + " when id=#{i.id} then #{i.bak1} "
            + "</if>"
            + "</foreach>"
            + "</trim>"
            + "<trim prefix='bak2=case' suffix='end,'>"
            + "<foreach collection='list' item='i' index='index'>"
            + "<if test=\"i.bak2 != null and i.bak2 != '' \">"
            + " when id=#{i.id} then #{i.bak2} "
            + "</if>"
            + "</foreach>"
            + "</trim>"
            + "<trim prefix='bak3=case' suffix='end,'>"
            + "<foreach collection='list' item='i' index='index'>"
            + "<if test=\"i.bak3 != null and i.bak3 != '' \">"
            + " when id=#{i.id} then #{i.bak3} "
            + "</if>"
            + "</foreach>"
            + "</trim>"
            + "<trim prefix='last_update_dttm=case' suffix='end,'>"
            + "<foreach collection='list' item='i' index='index'>"
            + "<if test=\"i.lastUpdateDttmStr != null and i.lastUpdateDttmStr != '' \">"
            + " when id=#{i.id} then #{i.lastUpdateDttmStr} "
            + "</if>"
            + "</foreach>"
            + "</trim>"
            + "<trim prefix='last_update_user=case' suffix='end,'>"
            + "<foreach collection='list' item='i' index='index'>"
            + "<if test=\"i.lastUpdateUser != null and i.lastUpdateUser != '' \">"
            + " when id=#{i.id} then #{i.lastUpdateUser} "
            + "</if>"
            + "</foreach>"
            + "</trim>"
            + "<trim prefix='version=case' suffix='end,'>"
            + "<foreach collection='list' item='i' index='index'>"
            + "<if test=\"i.version != null and i.version != '' \">"
            + " when id=#{i.id} then #{i.version} "
            + "</if>"
            + "</foreach>"
            + "</trim>"
            + "</trim>"
            + " where id in"
            + "<foreach collection='list' index='index' item='item' separator=',' open='(' close=')'>"
            + "#{item.id,jdbcType=BIGINT}"
            + "</foreach>"
            + "</script>")
    int updateBatch(List<FlowStopsPublishingProperty> properties);

    /**
     * @param publishingId:
     * @return List<FlowStopsPublishingProperty>
     * @author tianyao
     * @description 获取发布的参数以及样例文件
     * @date 2024/2/26 22:21
     */
    @Select("select fp.*, file.id as fileId, file.file_name as fileName,file.file_path as filePath from flow_stops_publishing_property as fp " +
            "left join file as file on file.associate_id = CAST(fp.id AS CHAR) and file.associate_type =3 and file.enable_flag = 1 " +
            "where fp.publishing_id = #{publishingId} and fp.enable_flag = 1 order by bak1, bak3, property_sort ASC")
    List<FlowStopsPublishingProperty> getStopsListByPublishingId(@Param("publishingId") Long publishingId);

    @Select("select * from flow_stops_publishing_property where publishing_id = #{publishingId} and type = #{propertyType} and enable_flag = 1")
    List<FlowStopsPublishingProperty> getByPublishingIdAndType(@Param("publishingId")Long publishingId, @Param("propertyType")Integer propertyType);

    @Update("update flow_stops_publishing_property set enable_flag = 0 where publishing_id = #{publishingId}")
    int deleteByPublishingId(@Param("publishingId") long flowPublishingId);

    @Select("<script>"
            + "select id from flow_stops_publishing_property where publishing_id = #{publishingId}"
            + " and id not in "
            + "<foreach collection='list' item='item' index='index' open='(' close=')' separator=','>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    List<Long> getToDeteleList(@Param("publishingId") Long flowPublishingId, @Param("list") List<Long> updateIds);
    @Delete("<script>"
            + "delete from flow_stops_publishing_property where id in "
            + "<foreach collection='list' item='item' index='index' open='(' close=')' separator=','>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    int deleteByIds(List<Long> toDeletePropertyIds);

    @Update("update flow_stops_publishing_property set custom_value = #{customValue} where id = #{id}")
    int updateCustomValue(@Param("id") String id, @Param("customValue") String customValue);
}
