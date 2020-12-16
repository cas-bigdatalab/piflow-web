package cn.cnic.component.template.mapper;

import cn.cnic.component.template.entity.PropertyTemplateModel;
import cn.cnic.component.template.entity.StopTemplateModel;
import cn.cnic.component.template.mapper.provider.FlowAndStopsTemplateVoMapperProvider;
import cn.cnic.component.template.vo.FlowTemplateModelVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * Stop component table
 */
@Mapper
public interface FlowAndStopsTemplateVoMapper {

    /**
     * Add a single stops
     *
     * @param stops
     * @return
     */
    @InsertProvider(type = FlowAndStopsTemplateVoMapperProvider.class, method = "addStops")
    public int addStops(StopTemplateModel stops);

    /**
     * add Flow
     *
     * @param flow
     * @return
     */
    @InsertProvider(type = FlowAndStopsTemplateVoMapperProvider.class, method = "addFlow")
    public int addFlow(FlowTemplateModelVo flow);

    /**
     * Insert list<PropertyVo> Note that the method of spelling sql must use Map to connect Param content to key value.
     *
     * @param propertyList (Content: The key is propertyList and the value is List<PropertyVo>)
     * @return
     */
    @InsertProvider(type = FlowAndStopsTemplateVoMapperProvider.class, method = "addPropertyList")
    public int addPropertyList(@Param("propertyList") List<PropertyTemplateModel> propertyList);

    /**
     * Invalid or delete stop according to templateId
     *
     * @param templateId
     * @return
     */
    @Update("update stops_template set enable_flag = 0 where fk_template_id = #{id} ")
    public int deleteStopTemByTemplateId(@Param("id") String templateId);

    /**
     * Modify invalid or delete stop attribute information according to stopId
     *
     * @param stopId
     * @return
     */
    @Update("update property_template set enable_flag = 0 where fk_stops_id = #{id} ")
    public int deleteStopPropertyTemByStopId(@Param("id") String stopId);

    /**
     * Query all stop information according to the template id
     *
     * @param templateId
     * @return
     */
    @Select("select * from stops_template where enable_flag = 1  and fk_template_id = #{templateId};")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "properties", many = @Many(select = "cn.cnic.component.template.mapper.FlowAndStopsTemplateVoMapper.getPropertyListByStopsId", fetchType = FetchType.EAGER))

    })
    public List<StopTemplateModel> getStopsListByTemPlateId(@Param("templateId") String templateId);


    /**
     * Query all stop attribute information according to stopId
     *
     * @param stopsId
     * @return
     */
    @Select("select * from property_template where fk_stops_id = #{stopsId} and enable_flag = 1 ")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "property_required", property = "required"),
            @Result(column = "property_sensitive", property = "sensitive")})
    public List<PropertyTemplateModel> getPropertyListByStopsId(String stopsId);

}
