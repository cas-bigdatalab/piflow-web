package cn.cnic.component.process.mapper;

import cn.cnic.component.process.entity.ProcessPath;
import cn.cnic.component.process.mapper.provider.ProcessPathMapperProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProcessPathMapper {


    /**
     * add processPath
     *
     * @param processPath
     * @return
     */
    @InsertProvider(type = ProcessPathMapperProvider.class, method = "addProcessPath")
    public int addProcessPath(ProcessPath processPath);

    /**
     * add processPath
     *
     * @param processPathList
     * @return
     */
    @InsertProvider(type = ProcessPathMapperProvider.class, method = "addProcessPathList")
    public int addProcessPathList(@Param("processPathList") List<ProcessPath> processPathList);

    /**
     * Query processPath according to process Id
     *
     * @param processId
     * @return
     */
    @SelectProvider(type = ProcessPathMapperProvider.class, method = "getProcessPathByProcessId")
    @Results({
            @Result(column = "line_from", property = "from"),
            @Result(column = "line_outport", property = "outport"),
            @Result(column = "line_inport", property = "inport"),
            @Result(column = "line_to", property = "to")
    })
    public ProcessPath getProcessPathByProcessId(String processId);

    /**
     * Query based on pid and pageId
     *
     * @param processId
     * @param pageId
     * @return
     */
    @SelectProvider(type = ProcessPathMapperProvider.class, method = "getProcessPathByPageIdAndPid")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "line_from", property = "from"),
            @Result(column = "line_outport", property = "outport"),
            @Result(column = "line_inport", property = "inport"),
            @Result(column = "line_to", property = "to")
    })
    public ProcessPath getProcessPathByPageIdAndPid(String processId, String pageId);

    /**
     * update processPath
     *
     * @param processPath
     * @return
     */
    @UpdateProvider(type = ProcessPathMapperProvider.class, method = "updateProcessPath")
    public int updateProcessPath(ProcessPath processPath);

    @UpdateProvider(type = ProcessPathMapperProvider.class, method = "updateEnableFlagByProcessId")
    public int updateEnableFlagByProcessId(String processId, String userName);

    /**
     * Query based on processGroupId and pageId
     *
     * @param processGroupId
     * @param pageId
     * @return
     */
    @SelectProvider(type = ProcessPathMapperProvider.class, method = "getProcessPathByPageIdAndProcessGroupId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "line_from", property = "from"),
            @Result(column = "line_outport", property = "outport"),
            @Result(column = "line_inport", property = "inport"),
            @Result(column = "line_to", property = "to")
    })
    public ProcessPath getProcessPathByPageIdAndProcessGroupId(String processGroupId, String pageId);

}
