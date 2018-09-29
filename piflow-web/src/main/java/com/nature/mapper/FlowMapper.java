package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.nature.component.workFlow.model.Flow;

@Mapper
public interface FlowMapper {

	@Insert("insert into flow(id,crt_dttm,crt_user,last_update_dttm,last_update_user,enable_flag,version,app_id,name,uuid) values (#{id},#{crtDttm},#{crtUser},#{lastUpdateDttm},#{lastUpdateUser},#{enableFlag},#{version},#{appId},#{name},#{uuid})")
	public int addFlow(Flow flow);

	/**
	 * 查詢所有工作流
	 * 
	 * @return
	 */
	@Select("select * from flow")
	List<Flow> getFlowList();

	/**
	 * 根據工作流Id查詢工作流
	 * 
	 * @param id
	 * @return
	 */
	@Select("select * from flow where id = #{id}")
	Flow getFlowById(String id);
}
