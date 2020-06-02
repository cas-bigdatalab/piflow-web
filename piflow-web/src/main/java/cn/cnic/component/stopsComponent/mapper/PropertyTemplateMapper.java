package cn.cnic.component.stopsComponent.mapper;

import cn.cnic.component.stopsComponent.model.PropertyTemplate;
import cn.cnic.component.stopsComponent.mapper.provider.PropertyTemplateMapperProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PropertyTemplateMapper {

    /**
     * Query all the attributes of the corresponding stops according to the stopsId
     *
     * @param stopsId
     * @return
     */
    @SelectProvider(type = PropertyTemplateMapperProvider.class, method = "getPropertyTemplateBySotpsId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "property_required", property = "required"),
            @Result(column = "property_sensitive", property = "sensitive")})
    List<PropertyTemplate> getPropertyTemplateBySotpsId(String stopsId);

    /**
     * Add more than one FLOW_STOPS_PROPERTY_TEMPLATE List.
     *
     * @param propertyTemplateList
     * @return
     */
    @InsertProvider(type = PropertyTemplateMapperProvider.class, method = "insertPropertyTemplate")
    public int insertPropertyTemplate(@Param("propertyTemplateList") List<PropertyTemplate> propertyTemplateList);

}
