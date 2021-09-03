package cn.cnic.component.process.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.cnic.component.process.entity.ProcessGroupPath;
import cn.cnic.component.process.entity.ProcessPath;
import cn.cnic.component.process.mapper.provider.ProcessGroupPathMapperProvider;

@Mapper
public interface ProcessGroupPathMapper {

    @InsertProvider(type = ProcessGroupPathMapperProvider.class,method = "addProcessGroupPath")
    public int addProcessGroupPath(ProcessGroupPath processGroupPath);
    
    @InsertProvider(type = ProcessGroupPathMapperProvider.class,method = "addProcessGroupPathList")
    public int addProcessGroupPathList(@Param("processGroupPathList") List<ProcessGroupPath> processGroupPathList);

    /**
     * update updateProcessGroupPath
     *
     * @param processGroupPath
     * @return
     */
    @UpdateProvider(type = ProcessGroupPathMapperProvider.class,method = "updateProcessGroupPath")
    public int updateProcessGroupPath(ProcessGroupPath processGroupPath);

    /**
     * Query processGroupPath according to processGroup Id
     *
     * @param processGroupId
     * @return
     */
    @SelectProvider(type = ProcessGroupPathMapperProvider.class, method = "getProcessPathByProcessGroupId")
    @Results({
            @Result(column = "line_from", property = "from"),
            @Result(column = "line_outport", property = "outport"),
            @Result(column = "line_inport", property = "inport"),
            @Result(column = "line_to", property = "to")
    })
    public ProcessGroupPath getProcessPathByProcessGroupId(String processGroupId);

    @UpdateProvider(type = ProcessGroupPathMapperProvider.class, method = "updateEnableFlagByProcessGroupId")
    public int updateEnableFlagByProcessGroupId(String processGroupId, String userName);

    /**
     * Query based on processGroupId and pageId
     *
     * @param processGroupId
     * @param pageId
     * @return
     */
    @SelectProvider(type = ProcessGroupPathMapperProvider.class, method = "getProcessPathByPageIdAndProcessGroupId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "line_from", property = "from"),
            @Result(column = "line_outport", property = "outport"),
            @Result(column = "line_inport", property = "inport"),
            @Result(column = "line_to", property = "to")
    })
    public ProcessPath getProcessPathByPageIdAndProcessGroupId(String processGroupId, String pageId);
    
    @Select("select * from flow_process_group_path where enable_flag=1 and fk_flow_process_group_id=#{fid} and page_id=#{pageId}")
    @Results({
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "line_from", property = "from"),
        @Result(column = "line_outport", property = "outport"),
        @Result(column = "line_inport", property = "inport"),
        @Result(column = "line_to", property = "to")
    })
    ProcessGroupPath getProcessGroupPathByPageId(@Param("fid") String fid, @Param("pageId") String pageId);

}
