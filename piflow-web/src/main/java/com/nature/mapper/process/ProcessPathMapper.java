package com.nature.mapper.process;

import com.nature.component.process.model.ProcessPath;
import com.nature.provider.process.ProcessPathMapperProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProcessPathMapper {


    /**
     * 添加 processPath
     *
     * @param processPath
     * @return
     */
    @InsertProvider(type = ProcessPathMapperProvider.class, method = "addProcessPath")
    public int addProcessPath(ProcessPath processPath);

    /**
     * 添加 processPath
     *
     * @param processPathList
     * @return
     */
    @InsertProvider(type = ProcessPathMapperProvider.class, method = "addProcessPathList")
    public int addProcessPathList(@Param("processPathList") List<ProcessPath> processPathList);

    /**
     * 根据进程Id查询processPath
     *
     * @param processId
     * @return
     */
    @SelectProvider(type = ProcessPathMapperProvider.class, method = "getProcessPathByProcessId")
    @Results({
            @Result(column = "LINE_FROM", property = "from"),
            @Result(column = "LINE_OUTPORT", property = "outport"),
            @Result(column = "LINE_INPORT", property = "inport"),
            @Result(column = "LINE_TO", property = "to")
    })
    public ProcessPath getProcessPathByProcessId(String processId);

    /**
     * 根据pid和pageId查询
     *
     * @param processId
     * @param pageId
     * @return
     */
    @SelectProvider(type = ProcessPathMapperProvider.class, method = "getProcessPathByPageIdAndPid")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "LINE_FROM", property = "from"),
            @Result(column = "LINE_OUTPORT", property = "outport"),
            @Result(column = "LINE_INPORT", property = "inport"),
            @Result(column = "LINE_TO", property = "to")
    })
    public ProcessPath getProcessPathByPageIdAndPid(String processId, String pageId);

    /**
     * 修改processPath
     *
     * @param processPath
     * @return
     */
    @UpdateProvider(type = ProcessPathMapperProvider.class, method = "updateProcessPath")
    public int updateProcessPath(ProcessPath processPath);

    @UpdateProvider(type = ProcessPathMapperProvider.class, method = "updateEnableFlagByProcessId")
    public int updateEnableFlagByProcessId(String processId, String userName);

}
