package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
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
	List<StopGroup> getStopGroupList();
}
