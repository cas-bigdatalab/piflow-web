package com.nature.mapper.system;

import com.nature.component.system.model.SysSchedule;
import com.nature.provider.system.SysScheduleMapperProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface SysScheduleMapper {

    /**
     * getSysScheduleList
     *
     * @param param
     * @return
     */
    @SelectProvider(type = SysScheduleMapperProvider.class, method = "getSysScheduleList")
    public List<SysSchedule> getSysScheduleList(@Param("param") String param);
    

}
