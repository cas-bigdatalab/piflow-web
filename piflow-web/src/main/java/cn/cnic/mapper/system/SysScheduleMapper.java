package cn.cnic.mapper.system;

import cn.cnic.component.system.vo.SysScheduleVo;
import cn.cnic.provider.system.SysScheduleMapperProvider;
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
    public List<SysScheduleVo> getSysScheduleList(@Param("isAdmin") boolean isAdmin, @Param("param") String param);

    @SelectProvider(type = SysScheduleMapperProvider.class, method = "getSysScheduleById")
    public SysScheduleVo getSysScheduleById(boolean isAdmin, String id);


}
