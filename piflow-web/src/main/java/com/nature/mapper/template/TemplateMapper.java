package com.nature.mapper.template;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import com.nature.component.flow.model.Template;
import com.nature.provider.TemplateMapperProvider;

@Mapper
public interface TemplateMapper {
    /**
     * 新增Template
     *
     * @param template
     * @return
     */
    @InsertProvider(type = TemplateMapperProvider.class, method = "addTemplate")
    public int addFlow(Template template);

    @Select("SELECT * FROM flow_template where enable_flag = '1' ORDER BY crt_dttm DESC ")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "fk_flow_id", property = "flow", one = @One(select = "com.nature.mapper.FlowMapper.getFlowById", fetchType = FetchType.EAGER))
    })
    public List<Template> findTemPlateListAll();

    @Select("SELECT * FROM flow_template where enable_flag = '1' ORDER BY crt_dttm DESC ")
    @Results({
            @Result(id = true, column = "id", property = "id")
    })
    public List<Template> findTemPlateList();

    @SelectProvider(type = TemplateMapperProvider.class, method = "findTemPlateListPage")
    @Results({
            @Result(id = true, column = "id", property = "id")
    })
    public List<Template> findTemPlateListPage(String param);

    /**
     * 根据id删除模板或修改模板为无效
     *
     * @param id
     * @return
     */
    @UpdateProvider(type = TemplateMapperProvider.class, method = "updateEnableFlagById")
    public int updateEnableFlagById(String id);

    /**
     * 根据templateId查询所有template信息
     *
     * @param id
     * @return
     */
    @Select("select * from flow_template where enable_flag = '1' and id = #{id} ")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "stopsList", many = @Many(select = "com.nature.mapper.template.FlowAndStopsTemplateVoMapper.getStopsListByTemPlateId", fetchType = FetchType.EAGER)),
            @Result(column = "fk_flow_id", property = "flow", one = @One(select = "com.nature.mapper.FlowMapper.getFlowById", fetchType = FetchType.EAGER))

    })
    public Template queryTemplate(String id);
}
