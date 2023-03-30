package cn.cnic.component.stopsComponent.mapper;

import cn.cnic.component.stopsComponent.entity.StopsHubFileRecord;
import cn.cnic.component.stopsComponent.mapper.provider.StopsHubFileRecordMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;


@Mapper
public interface StopsHubFileRecordMapper {
    /**
     * 添加算法包的具体文件记录
     *
     * @param record
     * @return
     */
    @InsertProvider(type = StopsHubFileRecordMapperProvider.class, method = "addStopsHubFileRecord")
    public int addStopsHubFileRecord(StopsHubFileRecord record);

    @Select("select * from stops_hub_file_record where stops_hub_id = #{hubId}")
    @Results(
            @Result(column = "file_path", property = "stopsComponent", many = @Many(select = "cn.cnic.worker.local.stopsComponent.mapper.StopsComponentMapper.getOnlyStopsComponentByBundle", fetchType = FetchType.LAZY))
    )
    public List<StopsHubFileRecord> getStopsHubFileRecordByHubId(String hubId);

    /**
     * 根据id查询算法包的具体文件记录
     *
     * @param id
     * @return
     */
    @Select("select * from stops_hub_file_record where id = #{id}")
    public StopsHubFileRecord getStopsHubFileRecordById(String id);

    /**
     * 根据id查询算法包的具体文件记录
     *
     * @param stopBundle
     * @return
     */
    @Select("select * from stops_hub_file_record where file_Path = #{stopBundle}")
    public StopsHubFileRecord getStopsHubFileRecordByBundle(String stopBundle);

    /**
     * 根据id删除算法包的具体文件记录
     *
     * @param id
     * @return
     */
    @Delete("delete from stops_hub_file_record where id = #{id}")
    public int deleteStopsHubFileRecord(String id);
}
