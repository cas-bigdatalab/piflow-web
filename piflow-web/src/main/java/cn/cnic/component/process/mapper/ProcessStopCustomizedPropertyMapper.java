package cn.cnic.component.process.mapper;

import cn.cnic.component.process.entity.ProcessStopCustomizedProperty;
import cn.cnic.component.process.mapper.provider.ProcessStopCustomizedPropertyMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ProcessStopCustomizedPropertyMapper {

    /**
     * Insert "list<ProcessStopCustomizedProperty>" Note that the method of spelling "sql" must use "map" to connect the "Param" content to the key value.
     *
     * @param processStopCustomizedPropertyList (Content: "processStopCustomizedPropertyList" with a value of "List<ProcessStopCustomizedProperty>")
     * @return
     */
    @InsertProvider(type = ProcessStopCustomizedPropertyMapperProvider.class, method = "addProcessStopCustomizedPropertyList")
    public int addProcessStopCustomizedPropertyList(@Param("processStopCustomizedPropertyList") List<ProcessStopCustomizedProperty> processStopCustomizedPropertyList);

    @InsertProvider(type = ProcessStopCustomizedPropertyMapperProvider.class, method = "addProcessStopCustomizedProperty")
    public int addProcessStopCustomizedProperty(@Param("processStopCustomizedProperty") ProcessStopCustomizedProperty processStopCustomizedProperty);

    @Select("select * from process_stops_customized_property where id = #{id} and enable_flag = 1 ")
    @Results({
            @Result(column = "fk_flow_process_stop_id", property = "stops", many = @Many(select = "cn.cnic.component.flow.mapper.StopsMapper.getStopsById", fetchType = FetchType.LAZY))
    })
    public ProcessStopCustomizedProperty getProcessStopCustomizedPropertyById(String id);

    @Select("select * from process_stops_customized_property where fk_flow_process_stop_id = #{processStopsId} and enable_flag = 1 ")
    public List<ProcessStopCustomizedProperty> getProcessStopCustomizedPropertyListByProcessStopsId(String processStopsId);

    @Select("select * from process_stops_customized_property where fk_flow_process_stop_id = #{processStopsId} and name = #{name} and enable_flag = 1 ")
    public List<ProcessStopCustomizedProperty> getProcessStopCustomizedPropertyListByProcessStopsIdAndName(String processStopsId, String name);

    @UpdateProvider(type = ProcessStopCustomizedPropertyMapperProvider.class, method = "updateProcessStopCustomizedProperty")
    public int updateProcessStopCustomizedProperty(@Param("processStopCustomizedProperty") ProcessStopCustomizedProperty processStopCustomizedProperty);

    @UpdateProvider(type = ProcessStopCustomizedPropertyMapperProvider.class, method = "updateEnableFlagByProcessStopId")
    public int updateEnableFlagByProcessStopId(@Param("username") String username, @Param("id") String id);

    @UpdateProvider(type = ProcessStopCustomizedPropertyMapperProvider.class, method = "updateProcessStopCustomizedPropertyCustomValue")
    public int updateProcessStopCustomizedPropertyCustomValue(String username, String content, String id);


}
