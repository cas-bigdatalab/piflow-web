package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.nature.component.workFlow.model.StopGroup;

@Mapper
public interface StopGroupMapper {
	/**
	 * 查詢所有組
	 * 
	 * @return
	 */
	@Select("select * from flow_sotps_groups")
	@Results({ @Result(id = true, column = "id", property = "id"),
			@Result(property = "stopsTemplateList", column = "id", many = @Many(select = "com.nature.mapper.StopsTemplateMapper.getStopsTemplateListByGroupId")) })
	List<StopGroup> getStopGroupList();

	/**
	 * 根据组id查询stops模板组
	 * 
	 * @param stopsId
	 * @return
	 */

	StopGroup getStopGroupById(String stopsId);
	
	/**
	 * add flow_sotps_groups 
	 * @param flow
	 * @return
	 */
	@Insert("insert into flow_sotps_groups(id,crt_dttm,crt_user,enable_flag,last_update_dttm,last_update_user,version,group_name) values (#{id},#{crtDttm},#{crtUser},#{enableFlag},#{lastUpdateDttm},#{lastUpdateUser},#{version},#{groupName})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	int insertStopGroup(StopGroup stopGroup);
	
	@Insert("insert into association_groups_stops_template(groups_id,stops_template_id) values (#{groups_id},#{stops_template_id})")
	int insertAssociationGroupsStopsTemplate(@Param("groups_id") String stopGroupId,@Param("stops_template_id") String stopsTemplateId);
	
}
