package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import com.nature.component.workFlow.model.Stops;
import com.nature.provider.StopsMapperProvider;

/**
 * stop组建表
 * 
 * @author Nature
 *
 */
@Mapper
public interface StopsMapper {

	/**
	 * Insert 单个stops
	 * 
	 * @param stops
	 * @return
	 */
	@InsertProvider(type = StopsMapperProvider.class, method = "addStops")
	public int addStops(Stops stops);

	/**
	 * Insert list 注意拼sql的方法必须用map接 Param内容为键值
	 * 
	 * @param stopsList
	 * @return
	 */
	@InsertProvider(type = StopsMapperProvider.class, method = "addStopsList")
	public int addStopsList(@Param("stopsList") List<Stops> stopsList);

	/**
	 * get 查询所有的stops数据
	 * 
	 * @return
	 */
	@SelectProvider(type = StopsMapperProvider.class, method = "getStopsAll")
	public List<Stops> getStopsAll();

}
