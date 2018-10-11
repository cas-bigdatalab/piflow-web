package com.nature.mapper.mxGraph;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;

import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.provider.mxGraph.MxGraphModelProvider;

@Mapper
public interface MxGraphModelMapper {

	@InsertProvider(type = MxGraphModelProvider.class, method = "addMxGraphModel")
	public int addMxGraphModel(MxGraphModel mxGraphModel);

}
