package cn.cnic.component.system.mapper;

import cn.cnic.common.Eunm.ScheduleState;
import cn.cnic.component.system.entity.SysSchedule;
import cn.cnic.component.system.vo.SysScheduleVo;
import cn.cnic.component.system.mapper.provider.SysScheduleMapperProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface SysScheduleMapper {

    @InsertProvider(type = SysScheduleMapperProvider.class, method = "insert")
    public int insert(SysSchedule sysSchedule);

    @InsertProvider(type = SysScheduleMapperProvider.class, method = "update")
    public int update(SysSchedule sysSchedule);

    @SelectProvider(type = SysScheduleMapperProvider.class, method = "getSysScheduleById")
    public SysSchedule getSysScheduleById(boolean isAdmin, String id);

    /**
     * getSysScheduleListByStatus
     *
     * @param isAdmin
     * @param status
     * @return
     */
    @SelectProvider(type = SysScheduleMapperProvider.class, method = "getSysScheduleListByStatus")
    public List<SysSchedule> getSysScheduleListByStatus(@Param("isAdmin") boolean isAdmin, @Param("status") ScheduleState status);

    /**
     * getSysScheduleList
     *
     * @param param
     * @return
     */
    @SelectProvider(type = SysScheduleMapperProvider.class, method = "getSysScheduleList")
    public List<SysScheduleVo> getSysScheduleList(@Param("isAdmin") boolean isAdmin, @Param("param") String param);

    @SelectProvider(type = SysScheduleMapperProvider.class, method = "getSysScheduleById")
    public SysScheduleVo getSysScheduleVoById(boolean isAdmin, String id);


}
