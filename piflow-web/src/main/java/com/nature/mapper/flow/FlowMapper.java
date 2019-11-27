package com.nature.mapper.flow;

import com.nature.component.flow.model.Flow;
import com.nature.provider.flow.FlowMapperProvider;
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
    public List<Flow> getFlowListParam(@Param("param")String param);

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
            @Result(column = "fk_flow_group_id", property = "flowGroup", one = @One(select = "com.nature.mapper.flow.FlowGroupMapper.getFlowGroupById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "com.nature.mapper.mxGraph.MxGraphModelMapper.getMxGraphModelByFlowId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "appId", one = @One(select = "com.nature.mapper.flow.FlowInfoDbMapper.getAppByAppFlowId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "stopsList", many = @Many(select = "com.nature.mapper.flow.StopsMapper.getStopsListByFlowId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "pathsList", many = @Many(select = "com.nature.mapper.flow.PathsMapper.getPathsListByFlowId", fetchType = FetchType.LAZY))

    })
    public Flow getFlowById(@Param("id")String id);

    @UpdateProvider(type = FlowMapperProvider.class, method = "updateEnableFlagById")
    public int updateEnableFlagById(@Param("id") String id);

    /**
     * According to the flow query PageId maximum
     *
     * @param flowId
     * @return
     */
    @Select("select MAX(page_id) from flow_stops where fk_flow_id = #{flowId} and enable_flag = 1 ")
    public String getMaxStopPageId(@Param("flowId") String flowId);    /**
     * According to the flow query PageId maximum
     *
     * @param flowGroupId
     * @return
     */
    @Select("select MAX(page_id) from flow where enable_flag = 1 and fk_flow_group_id = #{flowGroupId} ")
    public String getMaxFlowPageIdByFlowGroupId(@Param("flowGroupId") String flowGroupId);

    @Select("select * from flow s where s.enable_flag = 1 and s.fk_flow_group_id = #{fid} and s.page_id = #{pageId}")
    Flow getFlowByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    /**
     * Query flow by flowGroupId
     *
     * @param flowGroupId
     * @return
     */
    @SelectProvider(type = FlowMapperProvider.class, method = "getFlowListGroupId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "com.nature.mapper.mxGraph.MxGraphModelMapper.getMxGraphModelByFlowId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "stopsList", many = @Many(select = "com.nature.mapper.flow.StopsMapper.getStopsListByFlowId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "pathsList", many = @Many(select = "com.nature.mapper.flow.PathsMapper.getPathsListByFlowId", fetchType = FetchType.LAZY))
    })
    public List<Flow> getFlowListGroupId(String flowGroupId);
}
