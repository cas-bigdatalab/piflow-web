package cn.cnic.component.schedule.mapper;

import cn.cnic.component.schedule.entity.FileSchedule;
import cn.cnic.component.schedule.entity.FileScheduleOrigin;
import cn.cnic.component.schedule.mapper.provider.FileScheduleMapperProvider;
import cn.cnic.component.schedule.vo.FileScheduleVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface FileScheduleMapper {


    @InsertProvider(type = FileScheduleMapperProvider.class, method = "insert")
    int insert(FileSchedule fileSchedule);

    /**
     * update schedule
     *
     * @param fileSchedule
     * @return
     */
    @UpdateProvider(type = FileScheduleMapperProvider.class, method = "update")
    int update(FileSchedule fileSchedule);

    /**
     * @param id:
     * @return int
     * @author tianyao
     * @description 逻辑删除
     * @date 2024/5/15 9:54
     */
    @Update("update file_schedule set enable_flag = 0 where id = #{id}")
    int deleteById(@Param("id") Long id);

    @Select("select * from file_schedule where id = #{id}")
    FileSchedule getById(@Param("id") long id);

    @Select("<script>"
            + "select CAST(fs.id as CHAR) as id, fs.* from file_schedule as fs"
            + "<where>"
            + " crt_user = #{username} and fs.enable_flag = 1 "
            + "<if test=\"keyword != null and keyword != '' \">"
            + "and (fs.name like CONCAT('%', #{keyword}, '%') or dp.description like CONCAT('%', #{keyword}, '%'))"
            + "</if>"
            + "</where>"
            + "</script>")
    @Results({
            @Result(column = "id", property = "id", javaType = String.class, jdbcType = JdbcType.BIGINT)
    })
    List<FileScheduleVo> getFileScheduleListByPage(@Param("keyword") String keyword, @Param("username") String username);

    @Select("select * from file_schedule where state = 1 and enable_flag = 1")
    List<FileSchedule> getAllRunning();

    @Update("update file_schedule_origin set origin_file_record = #{origin.originFileRecord}, pending_file_record = #{origin.pendingFileRecord} where id = #{origin.id}")
    int updateOrigin(@Param("origin") FileScheduleOrigin origin);

    @Select("select * from file_schedule_origin where schedule_id = #{scheduleId}")
    FileScheduleOrigin getOriginByScheduleId(@Param("scheduleId") Long scheduleId);

    @Insert("insert into file_schedule_origin (schedule_id, origin_file_record, pending_file_record) values (#{origin.scheduleId},#{origin.originFileRecord},#{origin.pendingFileRecord})")
    int insertOrigin(@Param("origin") FileScheduleOrigin fileScheduleOrigin);
}
