package cn.cnic.component.schedule.mapper;

import cn.cnic.component.schedule.entity.Schedule;
import cn.cnic.component.schedule.mapper.provider.ScheduleMapperProvider;
import cn.cnic.component.schedule.vo.ScheduleVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ScheduleMapper {


    @InsertProvider(type = ScheduleMapperProvider.class, method = "insert")
    int insert(Schedule schedule);

    /**
     * update schedule
     *
     * @param schedule
     * @return
     */
    @UpdateProvider(type = ScheduleMapperProvider.class, method = "update")
    int update(Schedule schedule);

    @SelectProvider(type = ScheduleMapperProvider.class, method = "getScheduleList")
    List<ScheduleVo> getScheduleVoList(boolean isAdmin, String username, String param);

    @SelectProvider(type = ScheduleMapperProvider.class, method = "getScheduleById")
    ScheduleVo getScheduleVoById(boolean isAdmin, String username, String id);

    @SelectProvider(type = ScheduleMapperProvider.class, method = "getScheduleById")
    Schedule getScheduleById(boolean isAdmin, String username, String id);

    @DeleteProvider(type = ScheduleMapperProvider.class,method = "delScheduleById")
    int delScheduleById(boolean isAdmin, String username, String id);

    @SelectProvider(type = ScheduleMapperProvider.class, method = "getScheduleIdListByStateRunning")
    List<ScheduleVo> getScheduleIdListByStateRunning(boolean isAdmin, String username);

    @SelectProvider(type = ScheduleMapperProvider.class, method = "getScheduleIdListByScheduleRunTemplateId")
    int getScheduleIdListByScheduleRunTemplateId(boolean isAdmin, String username, String scheduleRunTemplateId);

}
