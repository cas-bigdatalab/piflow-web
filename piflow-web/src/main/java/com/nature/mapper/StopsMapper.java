package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.FetchType;

import com.nature.component.workFlow.model.Stops;
import com.nature.provider.StopsMapperProvider;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopVo2;

/**
 * stop组建表
 * 
 * @author Nature
 *
 */
@Mapper
public interface StopsMapper {

	/**
	 * 添加单个stops
	 * 
	 * @param stops
	 * @return
	 */
	@InsertProvider(type = StopsMapperProvider.class, method = "addStops")
	public int addStops(Stops stops);

	/**
	 * 插入list<Stops> 注意拼sql的方法必须用map接 Param内容为键值
	 * 
	 * @param stopsList
	 * @return
	 */
	@InsertProvider(type = StopsMapperProvider.class, method = "addStopsList")
	public int addStopsList(@Param("stopsList") List<Stops> stopsList);

	/**
	 * 修改stops
	 * 
	 * @param stops
	 * @return
	 */
	@UpdateProvider(type = StopsMapperProvider.class, method = "updateStops")
	public int updateStops(Stops stops);

	/**
	 * 查询所有的stops数据
	 * 
	 * @return
	 */
	@SelectProvider(type = StopsMapperProvider.class, method = "getStopsAll")
	public List<Stops> getStopsAll();

	/**
	 * 根据flowId查询StopsList
	 * @param flowId
	 * @return
	 */
	@SelectProvider(type = StopsMapperProvider.class, method = "getStopsListByFlowId")
	@Results({ @Result(id = true, column = "id", property = "id"),
			@Result(column = "id", property = "properties", many = @Many(select = "com.nature.mapper.PropertyMapper.getPropertyListByStopsId", fetchType = FetchType.EAGER))

	})
	public List<Stops> getStopsListByFlowId(String flowId);

	@SelectProvider(type = StopsMapperProvider.class, method = "getStopsListByFlowIdAndPageIds")
	@Results({ @Result(id = true, column = "id", property = "id"),
			@Result(column = "id", property = "properties", many = @Many(select = "com.nature.mapper.PropertyMapper.getPropertyListByStopsId", fetchType = FetchType.EAGER))

	})
	public List<Stops> getStopsListByFlowIdAndPageIds(@Param("flowId")String flowId, @Param("pageIds")String[] pageIds);
	
	@Update("update flow_stops set enable_flag = 0 where fk_flow_id=#{flowId}")
	public int deleteStopsByFlowId(@Param("flowId") String id);
	
	/**
	 * 根据stopsId 查询stop和属性信息
	 * @param Id
	 * @return
	 */
	@SelectProvider(type = StopsMapperProvider.class, method = "getStopsById")
	@Results({ @Result(id = true, column = "id", property = "id"),
			@Result(column = "id", property = "properties", many = @Many(select = "com.nature.mapper.PropertyMapper.getPropertyListByStopsId", fetchType = FetchType.EAGER))

	})
	public Stops getStopsById(String Id);
	
	
	@UpdateProvider(type = StopsMapperProvider.class, method = "updateStopsByFlowIdAndName")
	public int updateStopsByFlowIdAndName(ThirdFlowInfoStopVo2 stopVo);

}
