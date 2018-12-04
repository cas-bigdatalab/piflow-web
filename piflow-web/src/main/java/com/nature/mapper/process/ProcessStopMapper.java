package com.nature.mapper.process;

import com.nature.component.process.model.ProcessStop;
import com.nature.provider.process.ProcessStopMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ProcessStopMapper {

    /**
     * 添加processStop
     *
     * @param processStop
     * @return
     */
    @InsertProvider(type = ProcessStopMapperProvider.class, method = "addProcessStop")
    public int addProcessStop(ProcessStop processStop);

    /**
     * 添加processStopList
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
            @Result(column = "id", property = "processStopPropertyList", many = @Many(select = "com.nature.mapper.process.ProcessStopPropertyMapper.getStopPropertyByProcessStopId", fetchType = FetchType.LAZY))

    })
    public ProcessStop getProcessStopByProcessId(String processId);

    /**
     * 根据pid和pageId查询
     *
     * @param processId
     * @param pageId
     * @return
     */
    @SelectProvider(type = ProcessStopMapperProvider.class, method = "getProcessStopByPageIdAndPid")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processStopPropertyList", many = @Many(select = "com.nature.mapper.process.ProcessStopPropertyMapper.getStopPropertyByProcessStopId", fetchType = FetchType.LAZY))

    })
    public ProcessStop getProcessStopByPageIdAndPid(String processId, String pageId);

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
            @Result(column = "id", property = "processStopPropertyList", many = @Many(select = "com.nature.mapper.process.ProcessStopPropertyMapper.getStopPropertyByProcessStopId", fetchType = FetchType.LAZY))

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
     * 逻辑删除
     *
     * @param processId
     * @return
     */
    @UpdateProvider(type = ProcessStopMapperProvider.class, method = "updateEnableFlagByProcessId")
    public int updateEnableFlagByProcessId(String processId);

}
