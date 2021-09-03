package cn.cnic.component.flow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.FetchType;

import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.flow.mapper.provider.FlowGroupMapperProvider;
import cn.cnic.component.flow.vo.FlowGroupVo;

@Mapper
public interface FlowGroupMapper {

    @InsertProvider(type = FlowGroupMapperProvider.class, method = "addFlowGroup")
    public int addFlowGroup(FlowGroup flowGroup);

    @UpdateProvider(type = FlowGroupMapperProvider.class, method = "updateFlowGroup")
    public int updateFlowGroup(FlowGroup flowGroup);

    @UpdateProvider(type = FlowGroupMapperProvider.class, method = "updateEnableFlagById")
    public int updateEnableFlagById(@Param("username") String username, @Param("id") String id, @Param("enableFlag") boolean enableFlag);

    @SelectProvider(type = FlowGroupMapperProvider.class, method = "getFlowGroupListParam")
    public List<FlowGroupVo> getFlowGroupListParam(@Param("username") String username, @Param("isAdmin") boolean isAdmin, @Param("param") String param);

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
            @Result(column = "id", property = "flowGroupPathsList", many = @Many(select = "cn.cnic.component.flow.mapper.FlowGroupPathsMapper.getFlowGroupPathsByFlowGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "fk_flow_group_id", property = "flowGroup", one = @One(select = "cn.cnic.component.flow.mapper.FlowGroupMapper.getFlowGroupById", fetchType = FetchType.LAZY))
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

    @Select("select * from flow_group s where s.enable_flag=1 and s.fk_flow_group_id=#{fid} and s.page_id=#{pageId}")
    public FlowGroup getFlowGroupByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    @Select("select name from flow_group s where s.enable_flag=1 and s.fk_flow_group_id=#{fid} and s.page_id=#{pageId}")
    public String getFlowGroupNameByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    @Select("select s.id from flow_group s where s.enable_flag=1 and s.fk_flow_group_id=#{fid} and s.page_id=#{pageId}")
    public String getFlowGroupIdByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    @Select("select MAX(page_id+0) from flow_group where enable_flag=1 and fk_flow_group_id=#{flowGroupId} ")
    public Integer getMaxFlowGroupPageIdByFlowGroupId(@Param("flowGroupId") String flowGroupId);

    @Select(value = "select s.id from flow_group s where s.enable_flag=1 and s.fk_flow_group_id =#{fid} and s.name=#{flowGroupName}")
    public String getFlowGroupIdByNameAndFid(@Param("fid") String fid, @Param("flowGroupName") String flowGroupName);

    @Select("select c.name from  flow_group c where c.enable_flag=1 and c.name=#{name} and c.fk_flow_group_id=#{flowGroupId}")
    public String[] getFlowGroupNamesByNameAndEnableFlagInGroup(@Param("flowGroupId") String flowGroupId, @Param("name") String name);

    /**
     * query flowGroup name by flowGroup name
     * @param flowGroupName
     * @return
     */
    @Select("SELECT name FROM flow_group WHERE enable_flag=1 AND fk_flow_group_id IS NULL AND is_example=0 AND name=#{flowGroupName} ")
    public String getFlowGroupName(@Param("flowGroupName") String flowGroupName);

}
