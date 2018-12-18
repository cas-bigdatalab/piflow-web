package com.nature.mapper;

import com.nature.component.workFlow.model.Stops;
import com.nature.provider.StopsMapperProvider;
import com.nature.third.vo.flowInfo.ThirdFlowInfoStopVo;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

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
			@Result(column = "is_checkpoint", property = "isCheckpoint"),
			@Result(column = "id", property = "properties", many = @Many(select = "com.nature.mapper.PropertyMapper.getPropertyListByStopsId", fetchType = FetchType.LAZY))

	})
	public List<Stops> getStopsListByFlowId(String flowId);

	@SelectProvider(type = StopsMapperProvider.class, method = "getStopsListByFlowIdAndPageIds")
	@Results({ @Result(id = true, column = "id", property = "id"),
			@Result(column = "id", property = "properties", many = @Many(select = "com.nature.mapper.PropertyMapper.getPropertyListByStopsId", fetchType = FetchType.LAZY))

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
			@Result(column = "id", property = "properties", many = @Many(select = "com.nature.mapper.PropertyMapper.getPropertyListByStopsId", fetchType = FetchType.LAZY))

	})
	public Stops getStopsById(String Id);
	
	
	@UpdateProvider(type = StopsMapperProvider.class, method = "updateStopsByFlowIdAndName")
	public int updateStopsByFlowIdAndName(ThirdFlowInfoStopVo stopVo);
	

	/**
	 * 校验stopname是否重名
	 * @param flowId
	 * @param stopName
	 * @return
	 */
	@Select("select id from flow_stops where name = #{stopName} and fk_flow_id =#{flowId} and enable_flag = 1 ")
	public String getStopByNameAndFlowId(@Param(value = "flowId") String flowId,@Param(value = "stopName")String stopName);

}
