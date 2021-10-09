package cn.cnic.component.template.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.cnic.component.template.entity.FlowTemplate;
import cn.cnic.component.template.mapper.provider.FlowTemplateMapperProvider;

@Mapper
public interface FlowTemplateMapper {

    @InsertProvider(type = FlowTemplateMapperProvider.class,method = "insertFlowTemplate")
    public int insertFlowTemplate(FlowTemplate flowTemplate);

    @UpdateProvider(type = FlowTemplateMapperProvider.class, method = "updateEnableFlagById")
    public int updateEnableFlagById(String id, boolean enableFlag);

    @Select("select ft.* from flow_template ft where enable_flag and ft.id=#{id}")
    public FlowTemplate getFlowTemplateById(String id);

    @SelectProvider(type = FlowTemplateMapperProvider.class, method = "getFlowTemplateList")
    public List<FlowTemplate> getFlowTemplateList(String username, boolean isAdmin, String type);

    @SelectProvider(type = FlowTemplateMapperProvider.class, method = "getFlowTemplateListByParam")
    public List<FlowTemplate> getFlowTemplateListByParam(String username, boolean isAdmin, String param);

}
