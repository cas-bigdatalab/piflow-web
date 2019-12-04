package com.nature.mapper.template;

import com.nature.component.template.model.Template;
import com.nature.provider.template.TemplateMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface TemplateMapper {
    /**
     * add Template
     *
     * @param template
     * @return
     */
    @InsertProvider(type = TemplateMapperProvider.class, method = "addTemplate")
    public int addFlow(Template template);

    @Select("select* from flow_template where enable_flag = 1 order by crt_dttm desc ")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "fk_flow_id", property = "flow", one = @One(select = "com.nature.mapper.flow.FlowMapper.getFlowById", fetchType = FetchType.EAGER))
    })
    public List<Template> findTemPlateListAll();

    @Select("select* from flow_template where enable_flag = 1 order by crt_dttm desc ")
    @Results({
            @Result(id = true, column = "id", property = "id")
    })
    public List<Template> findTemPlateList();

    @SelectProvider(type = TemplateMapperProvider.class, method = "findTemPlateListPage")
    @Results({
            @Result(id = true, column = "id", property = "id")
    })
    public List<Template> findTemPlateListPage(@Param("param") String param);

    /**
     * Delete template based on id or modify template to invalid
     *
     * @param id
     * @return
     */
    @UpdateProvider(type = TemplateMapperProvider.class, method = "updateEnableFlagById")
    public int updateEnableFlagById(String id);

    /**
     * Query all template information according to templateId
     *
     * @param id
     * @return
     */
    @Select("select * from flow_template where enable_flag = 1 and id = #{id} ")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "stopsList", many = @Many(select = "com.nature.mapper.template.FlowAndStopsTemplateVoMapper.getStopsListByTemPlateId", fetchType = FetchType.EAGER)),
            @Result(column = "fk_flow_id", property = "flow", one = @One(select = "com.nature.mapper.flow.FlowMapper.getFlowById", fetchType = FetchType.EAGER))

    })
    public Template queryTemplate(String id);
}
