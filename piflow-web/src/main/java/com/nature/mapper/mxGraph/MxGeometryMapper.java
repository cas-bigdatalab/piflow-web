package com.nature.mapper.mxGraph;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;

import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.provider.mxGraph.MxGeometryMapperProvider;

@Mapper
public interface MxGeometryMapper {
	@InsertProvider(type = MxGeometryMapperProvider.class, method = "addMxGeometry")
	public int addMxGeometry(MxGeometry mxGeometry);

}
