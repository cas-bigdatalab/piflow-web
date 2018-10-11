package com.nature.mapper.mxGraph;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;

import com.nature.component.mxGraph.model.MxCell;
import com.nature.provider.mxGraph.MxCellMapperProvider;

@Mapper
public interface MxCellMapper {

	@InsertProvider(type = MxCellMapperProvider.class, method = "addMxCell")
	public int addMxCell(MxCell mxCell);

}
