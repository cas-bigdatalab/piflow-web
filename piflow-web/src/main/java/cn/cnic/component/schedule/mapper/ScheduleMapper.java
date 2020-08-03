package cn.cnic.component.schedule.mapper;

import cn.cnic.component.schedule.entity.Schedule;
import cn.cnic.component.schedule.mapper.provider.ScheduleMapperProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

@Mapper
public interface ScheduleMapper {

    @InsertProvider(type = ScheduleMapperProvider.class, method = "insert")
    int insert(Schedule schedule);

    @UpdateProvider(type = ScheduleMapperProvider.class, method = "update")
    int update(Schedule schedule);

    @SelectProvider(type = ScheduleMapperProvider.class, method = "getScheduleList")
    List<Schedule> getScheduleList(boolean isAdmin, String username, String param);

    @SelectProvider(type = ScheduleMapperProvider.class, method = "getScheduleById")
    Schedule getScheduleById(boolean isAdmin, String username, String id);

}
