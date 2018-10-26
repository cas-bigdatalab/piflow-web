package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.FlowInfoDb;
import com.nature.third.vo.App;

@Mapper
public interface FlowInfoDbMapper {
	/**
	 * @Title 新增 FlowInfo
	 * 
	 * @param App
	 * @return
	 */
	@Insert("insert into flow_info(id, crt_dttm,crt_user,enable_flag,last_update_dttm,last_update_user,version,end_time,name,start_time,state) VALUES  "
			+ "( #{app.id},#{app.crtDttm},#{app.crtUser},#{app.enableFlag},#{app.lastUpdateDttm} , #{app.lastUpdateUser},#{app.version},#{app.endTime},#{app.name},#{app.startTime},#{app.state}"
			+ ")")
	public int addFlowInfo(@Param("app") FlowInfoDb app);
	
	@Select("SELECT * FROM flow where enable_flag = '1' ")
	@Results({
		@Result(property="appId",column="app_id",javaType=App.class,one=@One(select="com.nature.mapper.FlowInfoDbMapper.getAppByAppId"))
 			})
	public List<Flow> findAppList();
 
	@Select("SELECT id,name,end_time,start_time,state FROM flow_info where enable_flag = '1' and id = #{id}")
	public List<FlowInfoDb> getAppByAppId(@Param("id") String appId);
}
