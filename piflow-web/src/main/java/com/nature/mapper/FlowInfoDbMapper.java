package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.FlowInfoDb;
import com.nature.provider.FlowInfoDbMapperProvider;

@Mapper
public interface FlowInfoDbMapper {
	/**
	 * @Title 新增 FlowInfo
	 * 
	 * @param App
	 * @return
	 */
	@Insert("insert into flow_info(id, crt_dttm,crt_user,enable_flag,last_update_dttm,last_update_user,version,end_time,name,start_time,state,progress) VALUES  "
			+ "( #{app.id},#{app.crtDttm},#{app.crtUser},#{app.enableFlag},#{app.lastUpdateDttm} , #{app.lastUpdateUser},#{app.version},#{app.endTime},#{app.name},#{app.startTime},#{app.state},#{app.progress}"
			+ ")")
	public int addFlowInfo(@Param("app") FlowInfoDb app);

	/**
	 * 查询flow以及flowinfoDb信息
	 * 
	 * @return
	 */
	@Select("SELECT * FROM flow where enable_flag = '1' ORDER BY crt_dttm DESC ")
	@Results({
			@Result(property = "appId", column = "app_id", javaType = FlowInfoDb.class, one = @One(select = "com.nature.mapper.FlowInfoDbMapper.getAppByAppId")) })
	public List<Flow> findAppList();

	@Select("SELECT id,name,end_time,start_time,state,progress FROM flow_info where enable_flag = '1' and id = #{id}")
	public List<FlowInfoDb> getAppByAppId(@Param("id") String appId);

	/**
	 * 根据appId查询
	 * 
	 * @param appId
	 * @return
	 */
	@Select("SELECT * FROM flow_info where enable_flag = '1' and id = #{id} ")
	public FlowInfoDb flowInfoDb(@Param("id") String appId);
	
	/**
	 * 修改
	 * @param app
	 * @return
	 */
	@Update("update flow_info set last_update_dttm = #{app.lastUpdateDttm},last_update_user = #{app.lastUpdateUser},version = version+1,end_time = #{app.endTime},name = #{app.name},"
			+ "start_time = #{app.startTime},state = #{app.state},progress = #{app.progress}  where id  = #{app.id}    "
			+ ")")
	public int updateFlowInfo(@Param("app") FlowInfoDb app);
	
	/**
	 * 根据flowID查询appId信息
	 * @param flowId
	 * @return
	 */
	@SelectProvider(type = FlowInfoDbMapperProvider.class, method = "getFlowInfoDbListByFlowId")
	public List<FlowInfoDb> getAppListByFlowId(String flowId);
	
	@Select({
			"<script>",
			"select ",
			"id,state,progress ",
			"from flow_info ",
			"where id in",
			"<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
			"#{id}", "</foreach>", "</script>" })
	List<FlowInfoDb> getFlowInfoByIds(@Param("ids") List<String> ids);
	
}
