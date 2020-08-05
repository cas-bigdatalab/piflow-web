package cn.cnic.component.process.mapper;

import cn.cnic.component.process.entity.ProcessStop;
import cn.cnic.component.process.mapper.provider.ProcessStopMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ProcessStopMapper {

    /**
     * add processStop
     *
     * @param processStop
     * @return
     */
    @InsertProvider(type = ProcessStopMapperProvider.class, method = "addProcessStop")
    public int addProcessStop(ProcessStop processStop);

    /**
     * add processStopList
     *
     * @param processStopList
     * @return
     */
    @InsertProvider(type = ProcessStopMapperProvider.class, method = "addProcessStopList")
    public int addProcessStopList(@Param("processStopList") List<ProcessStop> processStopList);

    /**
     * 根据process查询processStop
     *
     * @param processId
     * @return
     */
    @SelectProvider(type = ProcessStopMapperProvider.class, method = "getProcessStopByProcessId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processStopPropertyList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessStopPropertyMapper.getStopPropertyByProcessStopId", fetchType = FetchType.LAZY))

    })
    public ProcessStop getProcessStopByProcessId(String processId);

    /**
     * Query based on pid and pageId
     *
     * @param processId
     * @param pageId
     * @return
     */
    @SelectProvider(type = ProcessStopMapperProvider.class, method = "getProcessStopByPageIdAndPageId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processStopPropertyList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessStopPropertyMapper.getStopPropertyByProcessStopId", fetchType = FetchType.LAZY))

    })
    public ProcessStop getProcessStopByPageIdAndPageId(String processId, String pageId);

    /**
     * Query based on pid and pageId
     *
     * @param processId
     * @param pageIds
     * @return
     */
    @SelectProvider(type = ProcessStopMapperProvider.class, method = "getProcessStopByPageIdAndPageIds")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processStopPropertyList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessStopPropertyMapper.getStopPropertyByProcessStopId", fetchType = FetchType.LAZY))

    })
    public List<ProcessStop> getProcessStopByPageIdAndPageIds(@Param("processId") String processId, @Param("pageIds") String[] pageIds);

    /**
     * 根据pid和name查询
     *
     * @param processId
     * @param name
     * @return
     */
    @SelectProvider(type = ProcessStopMapperProvider.class, method = "getProcessStopByNameAndPid")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processStopPropertyList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessStopPropertyMapper.getStopPropertyByProcessStopId", fetchType = FetchType.LAZY))

    })
    public ProcessStop getProcessStopByNameAndPid(String processId, String name);

    /**
     * 修改ProcessStop
     *
     * @param processStop
     * @return
     */
    @UpdateProvider(type = ProcessStopMapperProvider.class, method = "updateProcessStop")
    public int updateProcessStop(ProcessStop processStop);

    /**
     * logically delete
     *
     * @param processId
     * @return
     */
    @UpdateProvider(type = ProcessStopMapperProvider.class, method = "updateEnableFlagByProcessId")
    public int updateEnableFlagByProcessId(String processId, String username);

}
