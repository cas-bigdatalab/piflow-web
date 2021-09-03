package cn.cnic.component.flow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.FetchType;

import cn.cnic.component.flow.entity.FlowGroupPaths;
import cn.cnic.component.flow.mapper.provider.FlowGroupPathsMapperProvider;

@Mapper
public interface FlowGroupPathsMapper {

    @InsertProvider(type = FlowGroupPathsMapperProvider.class, method = "addFlowGroupPaths")
    public int addFlowGroupPaths(FlowGroupPaths flowGroupPaths);

    @UpdateProvider(type = FlowGroupPathsMapperProvider.class, method = "updateFlowGroupPaths")
    public int updateFlowGroupPaths(FlowGroupPaths flowGroupPaths);

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
