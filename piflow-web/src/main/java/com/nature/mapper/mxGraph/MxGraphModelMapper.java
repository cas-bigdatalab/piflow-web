package com.nature.mapper.mxGraph;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.FetchType;

import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.provider.mxGraph.MxGraphModelProvider;

@Mapper
public interface MxGraphModelMapper {

	/**
	 * @Title 新增mxGraphModel
	 * 
	 * @param mxGraphModel
	 * @return
	 */
	@InsertProvider(type = MxGraphModelProvider.class, method = "addMxGraphModel")
	public int addMxGraphModel(MxGraphModel mxGraphModel);

	/**
	 * @Title 修改mxGraphModel
	 * 
	 * @param mxGraphModel
	 * @return
	 */
	@UpdateProvider(type = MxGraphModelProvider.class, method = "updateMxGraphModel")
	public int updateMxGraphModel(MxGraphModel mxGraphModel);

	/**
	 * @Title 根据Id查询mxGraphModel
	 * 
	 * @param id
	 * @return
	 */
	@SelectProvider(type = MxGraphModelProvider.class, method = "getMxGraphModelById")
	@Results({ @Result(id = true, column = "id", property = "id"), 
		@Result(column = "MX_DX", property = "dx"),
			@Result(column = "MX_DY", property = "dy"), 
			@Result(column = "MX_GRID", property = "grid"),
			@Result(column = "MX_GRIDSIZE", property = "gridSize"), 
			@Result(column = "MX_GUIDES", property = "guides"),
			@Result(column = "MX_TOOLTIPS", property = "tooltips"),
			@Result(column = "MX_CONNECT", property = "connect"), 
			@Result(column = "MX_ARROWS", property = "arrows"),
			@Result(column = "MX_FOLD", property = "fold"), 
			@Result(column = "MX_PAGE", property = "page"),
			@Result(column = "MX_PAGESCALE", property = "pageScale"),
			@Result(column = "MX_PAGEWIDTH", property = "pageWidth"),
			@Result(column = "MX_PAGEHEIGHT", property = "pageHeight"),
			@Result(column = "MX_BACKGROUND", property = "background"),
			@Result(column = "id", property = "root", many = @Many(select = "com.nature.mapper.mxGraph.MxCellMapper.getMeCellByMxGraphId", fetchType = FetchType.EAGER)) })
	public MxGraphModel getMxGraphModelById(String id);
	
	
	@Delete("DELETE from mx_graph_model where id = #{id}; ")
	int deleteMxGraphModelById(@Param("id") String id);

}
