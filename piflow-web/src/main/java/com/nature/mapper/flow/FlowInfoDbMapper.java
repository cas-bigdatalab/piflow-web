package com.nature.mapper.flow;

import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.FlowInfoDb;
import com.nature.provider.flow.FlowInfoDbMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface FlowInfoDbMapper {
    /**
     * add FlowInfo
     *
     * @param app
     * @return
     */
    @InsertProvider(type = FlowInfoDbMapperProvider.class, method = "addFlowInfo")
    public int addFlowInfo(FlowInfoDb app);

    /**
     * Query flow and flowinfoDb information
     *
     * @return
     */
    @Select("select * from flow where enable_flag = 1 order by crt_dttm desc ")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "appId", one = @One(select = "com.nature.mapper.flow.FlowInfoDbMapper.getAppByAppFlowId", fetchType = FetchType.LAZY))
    })
    public List<Flow> findAppList();

    @Select("select id,name,end_time,start_time,state,progress from flow_info where enable_flag = 1 and id = #{id}")
    public List<FlowInfoDb> getAppByAppId(@Param("id") String appId);

    @Select("select id,name,end_time,start_time,state,progress from flow_info where enable_flag = 1 and fk_flow_id = #{flowId} order by crt_dttm desc  limit 1  ")
    public List<FlowInfoDb> getAppByAppFlowId(@Param("flowId") String flowId);

    /**
     * get FlowInfoDb by appId
     *
     * @param appId
     * @return
     */
    @Select("select * from flow_info where enable_flag = 1 and id = #{id} ")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "fk_flow_id", property = "flow", one = @One(select = "com.nature.mapper.flow.FlowMapper.getFlowById", fetchType = FetchType.LAZY)),
    })
    public FlowInfoDb flowInfoDb(@Param("id") String appId);

    /**
     * update
     *
     * @param app
     * @return
     */
    @UpdateProvider(type = FlowInfoDbMapperProvider.class, method = "updateFlowInfo")
    public int updateFlowInfo(FlowInfoDb app);

    /**
     * get FlowInfoDb list by flowId
     *
     * @param flowId
     * @return
     */
    @SelectProvider(type = FlowInfoDbMapperProvider.class, method = "getFlowInfoDbListByFlowId")
    public List<FlowInfoDb> getAppListByFlowId(String flowId);

    @Select({
            "<script>",
            "select * ",
            "from flow_info ",
            "where id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}", "</foreach>", "</script>"})
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "fk_flow_id", property = "flow", one = @One(select = "com.nature.mapper.flow.FlowMapper.getFlowById", fetchType = FetchType.LAZY)),
    })
    public List<FlowInfoDb> getFlowInfoByIds(@Param("ids") List<String> ids);

    /**
     * Delete the flowInfo according to the id logic
     *
     * @param id
     * @return
     */
    @UpdateProvider(type = FlowInfoDbMapperProvider.class, method = "updateEnableFlagById")
    public int updateEnableFlagById(@Param("id") String id);

}
