package cn.cnic.component.stopsComponent.mapper;

import cn.cnic.component.stopsComponent.mapper.provider.StopsComponentGroupProvider;
import cn.cnic.component.stopsComponent.vo.StopsComponentGroupVo;
import cn.cnic.component.stopsComponent.model.StopsComponentGroup;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StopsComponentGroupMapper {
    /**
     * Query all groups
     *
     * @return
     */
    @SelectProvider(type = StopsComponentGroupProvider.class, method = "getStopGroupList")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "stopsComponentList", many = @Many(select = "cn.cnic.component.stopsComponent.mapper.StopsComponentMapper.getStopsComponentListByGroupId"))})
    List<StopsComponentGroup> getStopGroupList();

    /**
     * Query all groups
     *
     * @return
     */
    @SelectProvider(type = StopsComponentGroupProvider.class, method = "getStopGroupList")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "stopsComponentVoList", many = @Many(select = "cn.cnic.component.stopsComponent.mapper.StopsComponentMapper.getManageStopsComponentListByGroupId"))})
    List<StopsComponentGroupVo> getManageStopGroupList();
    
    /**
     * Query the stops template group based on the group id
     *
     * @param stopsId
     * @return
     */

    StopsComponentGroup getStopGroupById(String stopsId);

    /**
     * add flow_stops_groups
     *
     * @param stopGroup
     * @return
     */
    @Insert("INSERT INTO flow_stops_groups(id,crt_dttm,crt_user,enable_flag,last_update_dttm,last_update_user,version,group_name) VALUES (#{id},#{crtDttm},#{crtUser},#{enableFlag},#{lastUpdateDttm},#{lastUpdateUser},#{version},#{groupName})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertStopGroup(StopsComponentGroup stopGroup);

    @Insert("INSERT INTO association_groups_stops_template(groups_id,stops_template_id) VALUES (#{groups_id},#{stops_template_id})")
    int insertAssociationGroupsStopsTemplate(@Param("groups_id") String stopGroupId, @Param("stops_template_id") String stopsTemplateId);

    /**
     * Query flow_stops_groups based on groupName
     *
     * @param groupName
     * @return
     */
    @Select("<script>" +
            "select id, group_name from flow_stops_groups where enable_flag = 1 and group_name in " +
            "<foreach item='groupName' index='index' collection='group_name' open='(' separator=', ' close=')'>" +
            "#{groupName}" +
            "</foreach>" +
            "</script>")
    List<StopsComponentGroup> getStopGroupByNameList(@Param("group_name") List<String> groupName);

    @SelectProvider(type = StopsComponentGroupProvider.class, method="getStopGroupByGroupNameList")
    List<StopsComponentGroup> getStopGroupByGroupNameList(@Param("group_name") List<String> groupName);

    /**
     * Query flow_stops_groups based on groupName
     *
     * @param groupName
     * @return
     */
    @Select("select id from flow_stops_groups where enable_flag = 1 and group_name=#{group_name}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "stopsComponentList", many = @Many(select = "cn.cnic.component.stopsComponent.mapper.StopsComponentMapper.getStopsComponentListByGroupId"))
    })
    List<StopsComponentGroup> getStopGroupByName(@Param("group_name") String groupName);

    @Delete("delete from association_groups_stops_template")
    int deleteGroupCorrelation();


    @Delete("delete from association_groups_stops_template where groups_id =#{groups_id} and stops_template_id = #{stops_template_id}")
    int deleteGroupCorrelationByGroupIdAndStopId(@Param("groups_id") String stopGroupId, @Param("stops_template_id") String stopsTemplateId);


    @Delete("delete from association_groups_stops_template where stops_template_id = #{stops_template_id}")
    int deleteGroupCorrelationByStopId(@Param("stops_template_id") String stopsTemplateId);


    @Delete("delete from flow_stops_groups")
    int deleteGroup();

    @Delete("delete from flow_stops_groups where groups_id =#{groups_id}")
    int deleteGroupById(@Param("groups_id") String groupsId);

    @Select("select count(*) from association_groups_stops_template where groups_id =#{groups_id}")
    int getGroupStopCount(@Param("groups_id") String groupsId);

}
