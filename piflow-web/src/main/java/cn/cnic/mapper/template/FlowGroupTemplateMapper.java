package cn.cnic.mapper.template;

import cn.cnic.component.template.vo.FlowGroupTemplateVo;
import cn.cnic.provider.template.FlowGroupTemplateMapperProvider;
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
