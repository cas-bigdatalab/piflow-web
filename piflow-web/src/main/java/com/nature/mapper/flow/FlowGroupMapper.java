package com.nature.mapper.flow;

import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.FlowGroup;
import com.nature.component.flow.vo.FlowGroupVo;
import com.nature.provider.flow.FlowGroupMapperProvider;
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
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "com.nature.mapper.mxGraph.MxGraphModelMapper.getMxGraphModelByFlowGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "flowList", many = @Many(select = "com.nature.mapper.flow.FlowMapper.getFlowListGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "flowGroupPathsList", many = @Many(select = "com.nature.mapper.flow.FlowGroupPathsMapper.getFlowGroupPathsByFlowGroupId", fetchType = FetchType.LAZY))
    })
    public FlowGroup getFlowGroupById(@Param("id") String id);

}
