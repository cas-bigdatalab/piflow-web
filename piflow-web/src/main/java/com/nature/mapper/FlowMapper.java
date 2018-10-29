package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.FetchType;

import com.nature.component.workFlow.model.Flow;
import com.nature.provider.FlowMapperProvider;

@Mapper
public interface FlowMapper {
	/**
	 * @Title 新增Flow
	 * 
	 * @param flow
	 * @return
	 */
	@InsertProvider(type = FlowMapperProvider.class, method = "addFlow")
	public int addFlow(Flow flow);

	/**
	 * @Title 修改Flow
	 * 
	 * @param flow
	 * @return
	 */
	@UpdateProvider(type = FlowMapperProvider.class, method = "updateFlow")
	public int updateFlow(Flow flow);

	/**
	 * @Title 查詢所有工作流
	 * 
	 * @return
	 */
	@SelectProvider(type = FlowMapperProvider.class, method = "getFlowList")
	public List<Flow> getFlowList();

	/**
	 * @Title 根據工作流Id查詢工作流
	 * 
	 * @param id
	 * @return
	 */
	@SelectProvider(type = FlowMapperProvider.class, method = "getFlowById")
	@Results({ @Result(id = true, column = "id", property = "id"),
			@Result(column = "fk_mx_graph_model_id", property = "mxGraphModel", one = @One(select = "com.nature.mapper.mxGraph.MxGraphModelMapper.getMxGraphModelById", fetchType = FetchType.EAGER)),
			@Result(column = "app_id", property = "appId", one = @One(select = "com.nature.mapper.FlowInfoDbMapper.getAppByAppId", fetchType = FetchType.EAGER)),
			@Result(column = "id", property = "stopsList", many = @Many(select = "com.nature.mapper.StopsMapper.getStopsListByFlowId", fetchType = FetchType.EAGER)),
			@Result(column = "id", property = "pathsList", many = @Many(select = "com.nature.mapper.PathsMapper.getPathsListByFlowId", fetchType = FetchType.EAGER))

	})
	public Flow getFlowById(String id);
}
