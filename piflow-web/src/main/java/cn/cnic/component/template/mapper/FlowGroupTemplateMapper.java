package cn.cnic.component.template.mapper;

import cn.cnic.component.template.mapper.provider.FlowGroupTemplateMapperProvider;
import cn.cnic.component.template.vo.FlowGroupTemplateVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FlowGroupTemplateMapper {

    @SelectProvider(type = FlowGroupTemplateMapperProvider.class, method = "getFlowGroupTemplateVoListPage")
    @Results({
            @Result(id = true, column = "id", property = "id")
    })
    public List<FlowGroupTemplateVo> getFlowGroupTemplateVoListPage(@Param("username") String username, @Param("isAdmin") boolean isAdmin, @Param("param") String param);

    @SelectProvider(type = FlowGroupTemplateMapperProvider.class, method = "getFlowGroupTemplateVoById")
    @Results({
            @Result(id = true, column = "id", property = "id")
    })
    public FlowGroupTemplateVo getFlowGroupTemplateVoById(@Param("username") String username, @Param("isAdmin") boolean isAdmin, @Param("id") String id);


}
