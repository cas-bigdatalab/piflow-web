package com.nature.mapper.flow;

import com.nature.component.flow.model.Flow;
import com.nature.provider.flow.FlowMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface FlowMapper {
    /**
     * 新增Flow
     *
     * @param flow
     * @return
     */
    @InsertProvider(type = FlowMapperProvider.class, method = "addFlow")
    public int addFlow(Flow flow);

    /**
     * 修改Flow
     *
     * @param flow
     * @return
     */
    @UpdateProvider(type = FlowMapperProvider.class, method = "updateFlow")
    public int updateFlow(Flow flow);

    /**
     * 查詢所有工作流
     *
     * @return
     */
    @SelectProvider(type = FlowMapperProvider.class, method = "getFlowList")
    public List<Flow> getFlowList();

    /**
     * 查詢所有工作流分页查询
     *
     * @param param
     * @return
     */
    @SelectProvider(type = FlowMapperProvider.class, method = "getFlowListParam")
    public List<Flow> getFlowListParma(@Param("param")String param);

    /**
     * 查詢所有样例工作流
     *
     * @return
     */
    @SelectProvider(type = FlowMapperProvider.class, method = "getFlowExampleList")
    public List<Flow> getFlowExampleList();

    /**
     * 根据工作流Id查询工作流
     *
     * @param id
     * @return
     */
    @SelectProvider(type = FlowMapperProvider.class, method = "getFlowById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "com.nature.mapper.mxGraph.MxGraphModelMapper.getMxGraphModelByFlowId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "appId", one = @One(select = "com.nature.mapper.flow.FlowInfoDbMapper.getAppByAppFlowId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "stopsList", many = @Many(select = "com.nature.mapper.flow.StopsMapper.getStopsListByFlowId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "pathsList", many = @Many(select = "com.nature.mapper.flow.PathsMapper.getPathsListByFlowId", fetchType = FetchType.LAZY))

    })
    public Flow getFlowById(@Param("id")String id);

    @UpdateProvider(type = FlowMapperProvider.class, method = "updateEnableFlagById")
    public int updateEnableFlagById(@Param("id") String id);

    /**
     * 根据flow查询PageId最大值
     *
     * @param flowId
     * @return
     */
    @Select("SELECT MAX(page_id) from flow_stops where fk_flow_id = #{flowId} and enable_flag = 1 ")
    public String getMaxStopPageId(@Param("flowId") String flowId);
}
