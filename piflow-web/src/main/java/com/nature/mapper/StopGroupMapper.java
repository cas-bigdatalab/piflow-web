package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.nature.component.workFlow.model.StopGroup;

@Mapper
public interface StopGroupMapper {
	/**
	 * 查詢所有組
	 * 
	 * @return
	 */
	@Select("select * from flow_sotps_groups")
	@Results({ @Result(id = true, column = "id", property = "id"),
			@Result(property = "stopsTemplateList", column = "id", many = @Many(select = "com.nature.mapper.StopsTemplateMapper.getStopsTemplateListByGroupId")) })
	List<StopGroup> getStopGroupList();

	/**
	 * 根据组id查询stops模板组
	 * 
	 * @param stopsId
	 * @return
	 */

	StopGroup getStopGroupById(String stopsId);
}
