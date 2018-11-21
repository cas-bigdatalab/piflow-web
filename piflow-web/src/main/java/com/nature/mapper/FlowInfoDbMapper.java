package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.FlowInfoDb;
import com.nature.provider.FlowInfoDbMapperProvider;

@Mapper
public interface FlowInfoDbMapper {
    /**
     * 新增 FlowInfo
     *
     * @param app
     * @return
     */
    @InsertProvider(type = FlowInfoDbMapperProvider.class, method = "addFlowInfo")
    public int addFlowInfo(FlowInfoDb app);

    /**
     * 查询flow以及flowinfoDb信息
     *
     * @return
     */
    @Select("SELECT * FROM flow where enable_flag = '1' ORDER BY crt_dttm DESC ")
    @Results({
    			@Result(id = true, column = "id", property = "id"),
    			@Result(column = "id", property = "appId", one = @One(select = "com.nature.mapper.FlowInfoDbMapper.getAppByAppFlowId", fetchType = FetchType.EAGER))
            })
    public List<Flow> findAppList();

    @Select("SELECT id,name,end_time,start_time,state,progress FROM flow_info where enable_flag = '1' and id = #{id}")
    public List<FlowInfoDb> getAppByAppId(@Param("id") String appId);

    @Select("SELECT id,name,end_time,start_time,state,progress FROM flow_info where enable_flag = '1' and fk_flow_id = #{flowId} ORDER BY crt_dttm desc  LIMIT 1  ")
    public List<FlowInfoDb> getAppByAppFlowId(@Param("flowId") String flowId);

    /**
     * 根据appId查询
     *
     * @param appId
     * @return
     */
    @Select("SELECT * FROM flow_info where enable_flag = '1' and id = #{id} ")
    @Results({
		@Result(id = true, column = "id", property = "id"),
		@Result(column = "fk_flow_id", property = "flow", one = @One(select = "com.nature.mapper.FlowMapper.getFlowById", fetchType = FetchType.EAGER)),
    })
    public FlowInfoDb flowInfoDb(@Param("id") String appId);

    /**
     * 修改
     *
     * @param app
     * @return
     */
    @UpdateProvider(type = FlowInfoDbMapperProvider.class, method = "updateFlowInfo")
    public int updateFlowInfo(FlowInfoDb app);

    /**
     * 根据flowID查询appId信息
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
		@Result(column = "fk_flow_id", property = "flow", one = @One(select = "com.nature.mapper.FlowMapper.getFlowById", fetchType = FetchType.EAGER)),
    })
    public List<FlowInfoDb> getFlowInfoByIds(@Param("ids") List<String> ids);

    /**
     * 根据id删除flowInfo
     *
     * @param id
     * @return
     */
    @Update("update flow_info set enable_flag = 0 where id = #{id}; ")
    public int deleteFlowInfoById(@Param("id") String id);

}
