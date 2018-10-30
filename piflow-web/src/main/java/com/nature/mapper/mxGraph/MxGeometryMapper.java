package com.nature.mapper.mxGraph;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.provider.mxGraph.MxGeometryMapperProvider;

@Mapper
public interface MxGeometryMapper {

	/**
	 * @Title 新增MxGeometry
	 * 
	 * @param mxGeometry
	 * @return
	 */
	@InsertProvider(type = MxGeometryMapperProvider.class, method = "addMxGeometry")
	public int addMxGeometry(MxGeometry mxGeometry);

	/**
	 * @Title 修改MxGeometry
	 * 
	 * @param mxGeometry
	 * @return
	 */
	@UpdateProvider(type = MxGeometryMapperProvider.class, method = "updateMxGeometry")
	public int updateMxGeometry(MxGeometry mxGeometry);

	/**
	 * @Title 根据id查询MxGeometry
	 * 
	 * @param id
	 * @return
	 */
	@SelectProvider(type = MxGeometryMapperProvider.class, method = "getMxGeometryById")
	@Results({ 
		@Result(column = "MX_RELATIVE", property = "relative"), 
		@Result(column = "MX_AS", property = "as"),
		@Result(column = "MX_X", property = "x"), 
		@Result(column = "MX_Y", property = "y"),
		@Result(column = "MX_WIDTH", property = "width"), 
		@Result(column = "MX_HEIGHT", property = "height")
	})
	public MxGeometry getMxGeometryById(String id);
	
	
	@Delete("delete from mx_geometry where id = #{id}")
	public int deleteMxGraphById(String id);

}
