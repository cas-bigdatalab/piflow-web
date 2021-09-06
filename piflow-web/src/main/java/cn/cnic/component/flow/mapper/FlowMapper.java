package cn.cnic.component.flow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
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

import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.mapper.provider.FlowMapperProvider;
import cn.cnic.component.flow.vo.FlowVo;

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
    public List<FlowVo> getFlowListParam(@Param("username") String username, @Param("isAdmin") boolean isAdmin, @Param("param") String param);

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
            @Result(column = "id", property = "pathsList", many = @Many(select = "cn.cnic.component.flow.mapper.PathsMapper.getPathsListByFlowId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "flowGlobalParamsList", many = @Many(select = "cn.cnic.component.flow.mapper.FlowGlobalParamsMapper.getFlowGlobalParamsByFlowId", fetchType = FetchType.LAZY))
    })
    public Flow getFlowById(@Param("id") String id);
    
    /**
     * Query workflow based on workflow Id
     *
     * @param id
     * @return
     */
    @SelectProvider(type = FlowMapperProvider.class, method = "getFlowByPageId")
    @Results({
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "fk_flow_group_id", property = "flowGroup", one = @One(select = "cn.cnic.component.flow.mapper.FlowGroupMapper.getFlowGroupById", fetchType = FetchType.LAZY)),
        @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGraphModelMapper.getMxGraphModelByFlowId", fetchType = FetchType.LAZY)),
        @Result(column = "id", property = "stopsList", many = @Many(select = "cn.cnic.component.flow.mapper.StopsMapper.getStopsListByFlowId", fetchType = FetchType.LAZY)),
        @Result(column = "id", property = "pathsList", many = @Many(select = "cn.cnic.component.flow.mapper.PathsMapper.getPathsListByFlowId", fetchType = FetchType.LAZY))
        
    })
    public Flow getFlowByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

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
    @Select("SELECT f.name from flow f WHERE f.enable_flag=1 and f.fk_flow_group_id=#{flowGroupId} and f.name=#{flowName} ")
    public List<String> getFlowNamesByFlowGroupId(@Param("flowGroupId") String flowGroupId, @Param("flowName") String flowName);

    @Select("select name from flow s where s.enable_flag = 1 and s.fk_flow_group_id = #{fid} and s.page_id = #{pageId}")
    public String getFlowNameByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    @Select("select s.id from flow s where s.enable_flag = 1 and s.fk_flow_group_id = #{fid} and s.page_id = #{pageId}")
    public String getFlowIdByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

    @Select("select s.id from flow s where s.enable_flag=1 and s.fk_flow_group_id=#{fid} and s.name=#{flowName}")
    public String getFlowIdByNameAndFlowGroupId(@Param("fid") String fid, @Param("flowName") String flowName);
    
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
    
    @Select("select name from ( " +
            "select f.name from flow f WHERE f.enable_flag=1 and f.fk_flow_group_id=#{flowGroupId} " +
            "UNION ALL " +
            "select fg.name from flow_group fg where fg.enable_flag and fg.fk_flow_group_id=#{flowGroupId} " +
            ") as re ")
    public String[] getFlowAndGroupNamesByFlowGroupId(@Param("flowGroupId") String flowGroupId);


    /**
     * query flow name by flow name
     *
     * @param flowName
     * @return
     */
    @Select("SELECT name FROM flow WHERE enable_flag=1 AND fk_flow_group_id IS NULL AND is_example=0 AND name=#{flowName} ")
    public String getFlowName(@Param("flowName") String flowName);
    

    /**
     * get globalParams ids by flow id
     *
     * @param flowName
     * @return
     */
    @SelectProvider(type = FlowMapperProvider.class, method = "getGlobalParamsIdsByFlowId")
    public String[] getGlobalParamsIdsByFlowId(@Param("flowId") String flowId);

    /**
     * link GlobalParams
     *
     * @param flowName
     * @return
     */
    @InsertProvider(type = FlowMapperProvider.class, method = "linkGlobalParams")
    public int linkGlobalParams(@Param("flowId") String flowId, @Param("globalParamsIds") String[] globalParamsIds);

    /**
     * unlink GlobalParams
     *
     * @param flowName
     * @return
     */
    @DeleteProvider(type = FlowMapperProvider.class, method = "unlinkGlobalParams")
    public int unlinkGlobalParams(@Param("flowId") String flowId, @Param("globalParamsIds") String[] globalParamsIds);
    

}
