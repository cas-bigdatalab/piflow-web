package cn.cnic.component.flow.mapper;

import cn.cnic.component.flow.entity.FlowGroupPaths;
import cn.cnic.component.flow.mapper.provider.FlowGroupPathsMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface FlowGroupPathsMapper {


    /**
     * Query flowGroupPath by flowGroupId
     *
     * @param flowGroupId
     * @return
     */
    @SelectProvider(type = FlowGroupPathsMapperProvider.class, method = "getFlowGroupPathsByFlowGroupId")
    @Results({

            @Result(column = "line_from", property = "from"),
            @Result(column = "line_to", property = "to"),
            @Result(column = "line_outport", property = "outport"),
            @Result(column = "line_inport", property = "inport"),
            @Result(column = "line_port", property = "port"),
    })
    public List<FlowGroupPaths> getFlowGroupPathsByFlowGroupId(String flowGroupId);

    /**
     * Query connection information
     *
     * @param flowGroupId flow group Id
     * @param pageId      path pageID
     * @param from        path from
     * @param to          path to
     * @return
     */
    @SelectProvider(type = FlowGroupPathsMapperProvider.class, method = "getFlowGroupPaths")
    @Results({
            @Result(column = "line_from", property = "from"),
            @Result(column = "line_to", property = "to"),
            @Result(column = "line_outport", property = "outport"),
            @Result(column = "line_inport", property = "inport"),
            @Result(column = "line_port", property = "port"),
            @Result(column = "fk_flow_group_id", property = "flowGroup", many = @Many(select = "cn.cnic.component.flow.mapper.FlowGroupMapper.getFlowGroupById", fetchType = FetchType.LAZY))
    })
    public List<FlowGroupPaths> getFlowGroupPaths(String flowGroupId, String pageId, String from, String to);


    @Select("select MAX(page_id+0) from flow_group_path where enable_flag = 1 and fk_flow_group_id = #{flowGroupId} ")
    public Integer getMaxFlowGroupPathPageIdByFlowGroupId(@Param("flowGroupId") String flowGroupId);

}
