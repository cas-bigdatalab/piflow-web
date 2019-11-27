package com.nature.mapper.template;

import com.nature.component.template.vo.FlowGroupTemplateVo;
import com.nature.provider.template.FlowGroupTemplateMapperProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FlowGroupTemplateMapper {

    @SelectProvider(type = FlowGroupTemplateMapperProvider.class, method = "getFlowGroupTemplateVoListPage")
    @Results({
            @Result(id = true, column = "id", property = "id")
    })
    public List<FlowGroupTemplateVo> getFlowGroupTemplateVoListPage(@Param("param") String param);

    @SelectProvider(type = FlowGroupTemplateMapperProvider.class, method = "getFlowGroupTemplateVoById")
    @Results({
            @Result(id = true, column = "id", property = "id")
    })
    public FlowGroupTemplateVo getFlowGroupTemplateVoById(@Param("id") String id);


}
