package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nature.component.workFlow.model.Paths;
import com.nature.provider.PathsMapperProvider;

@Mapper
public interface PathsMapper {
	/**
	 * Insert list 注意拼sql的方法必须用map接 Param内容为键值
	 * 
	 * @param pathsList
	 * @return
	 */
	@InsertProvider(type = PathsMapperProvider.class, method = "addStopsList")
	public int addPathsList(@Param("pathsList") List<Paths> pathsList);
}
