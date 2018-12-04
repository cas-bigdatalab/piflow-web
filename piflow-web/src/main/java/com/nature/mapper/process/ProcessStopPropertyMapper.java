package com.nature.mapper.process;

import com.nature.component.process.model.ProcessStopProperty;
import com.nature.provider.process.ProcessStopPropertyMapperProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProcessStopPropertyMapper {

    @InsertProvider(type = ProcessStopPropertyMapperProvider.class, method = "addProcessStopProperty")
    public int addProcessStopProperty(ProcessStopProperty processStopProperty);

    @InsertProvider(type = ProcessStopPropertyMapperProvider.class, method = "addProcessStopProperties")
    public int addProcessStopProperties(@Param("processStopPropertyList") List<ProcessStopProperty> processStopPropertyList);

    /**
     * 根据processStopId查询processStop属性
     *
     * @param processStopId
     * @return
     */
    @SelectProvider(type = ProcessStopPropertyMapperProvider.class, method = "getStopPropertyByProcessStopId")
    @Results({
            @Result(column = "CUSTOM_VALUE", property = "customValue"),
            @Result(column = "ALLOWABLE_VALUES", property = "allowableValues"),
            @Result(column = "PROPERTY_REQUIRED", property = "required"),
            @Result(column = "PROPERTY_SENSITIVE", property = "sensitive")
    })
    public ProcessStopProperty getStopPropertyByProcessStopId(String processStopId);

    @UpdateProvider(type = ProcessStopPropertyMapperProvider.class, method = "updateProcessStopProperty")
    public int updateProcessStopProperty(ProcessStopProperty processStopProperty);

    @UpdateProvider(type = ProcessStopPropertyMapperProvider.class, method = "updateEnableFlagByProcessStopId")
    public int updateEnableFlagByProcessStopId(String processStopId);

}
