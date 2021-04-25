package cn.cnic.component.flow.mapper;

import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.flow.mapper.provider.FlowGroupMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface FlowGroupMapper {

    /**
     * Query FlowGroup based on FlowGroup Id
     *
     * @param id
     * @return
     */
    @SelectProvider(type = FlowGroupMapperProvider.class, method = "getFlowGroupById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGraphModelMapper.getMxGraphModelByFlowGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "flowList", many = @Many(select = "cn.cnic.component.flow.mapper.FlowMapper.getFlowListGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "flowGroupList", many = @Many(select = "cn.cnic.component.flow.mapper.FlowGroupMapper.getFlowGroupListByFkGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "flowGroupPathsList", many = @Many(select = "cn.cnic.component.flow.mapper.FlowGroupPathsMapper.getFlowGroupPathsByFlowGroupId", fetchType = FetchType.LAZY))
    })
    public FlowGroup getFlowGroupById(@Param("id") String id);

    /**
     * Query flow by flowGroupId
     *
     * @param fkFlowGroupId
     * @return
     */
    @SelectProvider(type = FlowGroupMapperProvider.class, method = "getFlowGroupListByFkGroupId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGraphModelMapper.getMxGraphModelByFlowGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "flowList", many = @Many(select = "cn.cnic.component.flow.mapper.FlowMapper.getFlowListGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "flowGroupList", many = @Many(select = "cn.cnic.component.flow.mapper.FlowGroupMapper.getFlowGroupListByFkGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "flowGroupPathsList", many = @Many(select = "cn.cnic.component.flow.mapper.FlowGroupPathsMapper.getFlowGroupPathsByFlowGroupId", fetchType = FetchType.LAZY))
    })
    public List<FlowGroup> getFlowGroupListByFkGroupId(String fkFlowGroupId);

    /**
     * Query FlowGroup based on FlowGroup Id
     *
     * @param id
     * @return
     */
    @SelectProvider(type = FlowGroupMapperProvider.class, method = "getFlowGroupById")
    public FlowGroup getFlowGroupBaseInfoById(@Param("id") String id);

    @Select("select name from flow_group s where s.enable_flag = 1 and s.fk_flow_group_id = #{fid} and s.page_id = #{pageId}")
    String getFlowGroupNameByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    @Select("select s.id from flow_group s where s.enable_flag = 1 and s.fk_flow_group_id = #{fid} and s.page_id = #{pageId}")
    String getFlowGroupIdByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    @Select("select MAX(page_id+0) from flow_group where enable_flag = 1 and fk_flow_group_id = #{flowGroupId} ")
    public Integer getMaxFlowGroupPageIdByFlowGroupId(@Param("flowGroupId") String flowGroupId);

    /**
     * query flowGroup name by flowGroup name
     * @param flowGroupName
     * @return
     */
    @Select("SELECT name FROM flow_group WHERE enable_flag=1 AND fk_flow_group_id IS NULL AND is_example=0 AND name=#{flowGroupName} ")
    public String getFlowGroupName(@Param("flowGroupName") String flowGroupName);

}
