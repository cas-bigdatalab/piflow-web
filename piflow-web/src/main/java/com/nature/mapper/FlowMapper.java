package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.nature.component.workFlow.model.Flow;
import com.nature.provider.FlowMapperProvider;

@Mapper
public interface FlowMapper {

	@InsertProvider(type = FlowMapperProvider.class, method = "addFlow")
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
