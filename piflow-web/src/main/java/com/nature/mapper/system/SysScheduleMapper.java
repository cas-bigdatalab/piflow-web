package com.nature.mapper.system;

import com.nature.component.system.vo.SysScheduleVo;
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
    public List<SysScheduleVo> getSysScheduleList(@Param("param") String param);

    @SelectProvider(type = SysScheduleMapperProvider.class, method = "getSysScheduleById")
    public SysScheduleVo getSysScheduleById(String id);


}
