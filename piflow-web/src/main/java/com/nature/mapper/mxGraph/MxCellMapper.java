package com.nature.mapper.mxGraph;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.FetchType;

import com.nature.component.mxGraph.model.MxCell;
import com.nature.provider.mxGraph.MxCellMapperProvider;

@Mapper
public interface MxCellMapper {

	/**
	 * @Title 添加mxCell
	 * 
	 * @param mxCell
	 * @return
	 */
	@InsertProvider(type = MxCellMapperProvider.class, method = "addMxCell")
	public int addMxCell(MxCell mxCell);

	/**
	 * @Title 修改mxCell
	 * 
	 * @param mxCell
	 * @return
	 */
	@UpdateProvider(type = MxCellMapperProvider.class, method = "updateMxCell")
	public int updateMxCell(MxCell mxCell);

	/**
	 * @Title 根据mxGraphId查询MxCell的list
	 * 
	 * @param mxGraphId
	 * @return
	 */
	@SelectProvider(type = MxCellMapperProvider.class, method = "getMeCellByMxGraphId")
	@Results({ @Result(column = "MX_PAGEID", property = "pageId"), @Result(column = "MX_PARENT", property = "parent"),
			@Result(column = "MX_STYLE", property = "style"), @Result(column = "MX_EDGE", property = "edge"),
			@Result(column = "MX_SOURCE", property = "source"), @Result(column = "MX_TARGET", property = "target"),
			@Result(column = "MX_VALUE", property = "value"), @Result(column = "MX_VERTEX", property = "vertex"),
			@Result(column = "fk_mx_geometry_id", property = "mxGeometry", one = @One(select = "com.nature.mapper.mxGraph.MxGeometryMapper.getMxGeometryById", fetchType = FetchType.EAGER)) })
	public List<MxCell> getMeCellByMxGraphId(String mxGraphId);

	/**
	 * @Title 根据Id查询MxCell
	 * 
	 * @param id
	 * @return
	 */
	@SelectProvider(type = MxCellMapperProvider.class, method = "getMeCellById")
	@Results({ @Result(column = "MX_PAGEID", property = "pageId"), @Result(column = "MX_PARENT", property = "parent"),
			@Result(column = "MX_STYLE", property = "style"), @Result(column = "MX_EDGE", property = "edge"),
			@Result(column = "MX_SOURCE", property = "source"), @Result(column = "MX_TARGET", property = "target"),
			@Result(column = "MX_VALUE", property = "value"), @Result(column = "MX_VERTEX", property = "vertex"),
			@Result(column = "fk_mx_geometry_id", property = "mxGeometry", one = @One(select = "com.nature.mapper.mxGraph.MxGeometryMapper.getMxGeometryById", fetchType = FetchType.EAGER)) })
	public MxCell getMeCellById(String id);
	
	
	@Delete("delete from mx_cell where fk_mx_graph_id = #{id}")
	public int deleteMxCellByMxId(String id);

}
