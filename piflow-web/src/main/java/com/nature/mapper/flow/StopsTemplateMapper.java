package com.nature.mapper.flow;

import com.nature.component.group.model.StopsTemplate;
import com.nature.provider.flow.StopsTemplateMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface StopsTemplateMapper {
    /**
     * Query all stops templates
     *
     * @return
     */
    @SelectProvider(type = StopsTemplateMapperProvider.class, method = "getStopsTemplateList")
    public List<StopsTemplate> getStopsTemplateList();

    /**
     * Query template based on the stops template
     *
     * @param id
     * @return
     */
    @SelectProvider(type = StopsTemplateMapperProvider.class, method = "getStopsTemplateById")
    public StopsTemplate getStopsTemplateById(String id);

    /**
     * Query the stops template based on the id of the stops template (including the attribute list)
     *
     * @param id
     * @return
     */
    @SelectProvider(type = StopsTemplateMapperProvider.class, method = "getStopsTemplateById")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(property = "properties", column = "id", many = @Many(select = "com.nature.mapper.flow.PropertyTemplateMapper.getPropertyTemplateBySotpsId"))})
    public StopsTemplate getStopsTemplateAndPropertyById(String id);

    /**
     * Query the stops template according to the id of the stops group
     *
     * @param groupId
     * @return
     */
    @Select("select * from flow_stops_template where id in (select agst.stops_template_id from association_groups_stops_template agst where agst.groups_id = #{groupId})")
    public List<StopsTemplate> getStopsTemplateListByGroupId(String groupId);

    /**
     * Query the stops template according to the id of the stops group...
     *
     * @param stopsName
     * @return
     */
    @SelectProvider(type = StopsTemplateMapperProvider.class, method = "getStopsTemplateByName")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "properties", many = @Many(select = "com.nature.mapper.flow.PropertyTemplateMapper.getPropertyTemplateBySotpsId", fetchType = FetchType.LAZY))

    })
    public List<StopsTemplate> getStopsTemplateByName(String stopsName);

    /**
     * Add more than one FLOW_STOPS_TEMPLATE List.
     *
     * @param stopsTemplateList
     * @return
     */
    @InsertProvider(type = StopsTemplateMapperProvider.class, method = "insertStopsTemplate")
    public int insertStopsTemplate(@Param("stopsTemplateList") List<StopsTemplate> stopsTemplateList);
}
