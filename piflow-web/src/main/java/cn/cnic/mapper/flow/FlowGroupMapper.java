package cn.cnic.mapper.flow;

import cn.cnic.component.flow.model.FlowGroup;
import cn.cnic.provider.flow.FlowGroupMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

@Mapper
public interface FlowGroupMapper {

    /**
     * Query FlowGroup based on FlowGroup Id
     *
     * @param id
     * @return
     */
    @SelectProvider(type = FlowGroupMapperProvider.class, method = "getFlowGroupById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.mapper.mxGraph.MxGraphModelMapper.getMxGraphModelByFlowGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "flowList", many = @Many(select = "cn.cnic.mapper.flow.FlowMapper.getFlowListGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "flowGroupPathsList", many = @Many(select = "cn.cnic.mapper.flow.FlowGroupPathsMapper.getFlowGroupPathsByFlowGroupId", fetchType = FetchType.LAZY))
    })
    public FlowGroup getFlowGroupById(@Param("id") String id);

    /**
     * Query FlowGroup based on FlowGroup Id
     *
     * @param id
     * @return
     */
    @SelectProvider(type = FlowGroupMapperProvider.class, method = "getFlowGroupById")
    public FlowGroup getFlowGroupBaseInfoById(@Param("id") String id);
}
