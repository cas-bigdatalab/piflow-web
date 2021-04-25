package cn.cnic.component.flow.mapper;

import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.mapper.provider.FlowMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface FlowMapper {
    /**
     * add flow
     *
     * @param flow
     * @return
     */
    @InsertProvider(type = FlowMapperProvider.class, method = "addFlow")
    public int addFlow(Flow flow);

    /**
     * update flow
     *
     * @param flow
     * @return
     */
    @UpdateProvider(type = FlowMapperProvider.class, method = "updateFlow")
    public int updateFlow(Flow flow);

    /**
     * Query all workflows
     *
     * @return
     */
    @SelectProvider(type = FlowMapperProvider.class, method = "getFlowList")
    public List<Flow> getFlowList();

    /**
     * Query all workflow paging queries
     *
     * @param param
     * @return
     */
    @SelectProvider(type = FlowMapperProvider.class, method = "getFlowListParam")
    public List<Flow> getFlowListParam(@Param("username") String username, @Param("isAdmin") boolean isAdmin, @Param("param") String param);

    /**
     * Query all sample workflows
     *
     * @return
     */
    @SelectProvider(type = FlowMapperProvider.class, method = "getFlowExampleList")
    public List<Flow> getFlowExampleList();

    /**
     * Query workflow based on workflow Id
     *
     * @param id
     * @return
     */
    @SelectProvider(type = FlowMapperProvider.class, method = "getFlowById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "fk_flow_group_id", property = "flowGroup", one = @One(select = "cn.cnic.component.flow.mapper.FlowGroupMapper.getFlowGroupById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGraphModelMapper.getMxGraphModelByFlowId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "stopsList", many = @Many(select = "cn.cnic.component.flow.mapper.StopsMapper.getStopsListByFlowId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "pathsList", many = @Many(select = "cn.cnic.component.flow.mapper.PathsMapper.getPathsListByFlowId", fetchType = FetchType.LAZY))

    })
    public Flow getFlowById(@Param("id") String id);

    @UpdateProvider(type = FlowMapperProvider.class, method = "updateEnableFlagById")
    public int updateEnableFlagById(@Param("username") String username, @Param("id") String id);

    /**
     * According to the flow query PageId maximum
     *
     * @param flowId
     * @return
     */
    @Select("select MAX(page_id+0) from flow_stops where fk_flow_id = #{flowId} and enable_flag = 1 ")
    public Integer getMaxStopPageId(@Param("flowId") String flowId);

    /**
     * According to the flow query stopName
     *
     * @param flowId
     * @return
     */
    @Select("SELECT fs.name from flow_stops fs WHERE fs.enable_flag=1 and fs.fk_flow_id = #{flowId}")
    public String[] getStopNamesByFlowId(@Param("flowId") String flowId);

    /**
     * According to the flow query PageId maximum
     *
     * @param flowGroupId
     * @return
     */
    @Select("select MAX(page_id+0) from flow where enable_flag = 1 and fk_flow_group_id = #{flowGroupId} ")
    public Integer getMaxFlowPageIdByFlowGroupId(@Param("flowGroupId") String flowGroupId);

    /**
     * @param flowGroupId
     * @return
     */
    @Select("SELECT f.name from flow f WHERE f.enable_flag=1 and f.fk_flow_group_id = #{flowGroupId} and id != #{id} ")
    public List<String> getFlowNamesByFlowGroupId(@Param("flowGroupId") String flowGroupId, @Param("id") String id);

    @Select("select name from flow s where s.enable_flag = 1 and s.fk_flow_group_id = #{fid} and s.page_id = #{pageId}")
    String getFlowNameByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    @Select("select s.id from flow s where s.enable_flag = 1 and s.fk_flow_group_id = #{fid} and s.page_id = #{pageId}")
    String getFlowIdByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    /**
     * Query flow by flowGroupId
     *
     * @param flowGroupId
     * @return
     */
    @SelectProvider(type = FlowMapperProvider.class, method = "getFlowListGroupId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGraphModelMapper.getMxGraphModelByFlowId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "stopsList", many = @Many(select = "cn.cnic.component.flow.mapper.StopsMapper.getStopsListByFlowId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "pathsList", many = @Many(select = "cn.cnic.component.flow.mapper.PathsMapper.getPathsListByFlowId", fetchType = FetchType.LAZY))
    })
    public List<Flow> getFlowListGroupId(String flowGroupId);

    /**
     * query flow name by flow name
     *
     * @param flowName
     * @return
     */
    @Select("SELECT name FROM flow WHERE enable_flag=1 AND fk_flow_group_id IS NULL AND is_example=0 AND name=#{flowName} ")
    public String getFlowName(@Param("flowName") String flowName);

}
