package cn.cnic.mapper.process;

import cn.cnic.component.process.model.ProcessGroupPath;
import cn.cnic.component.process.model.ProcessPath;
import cn.cnic.provider.process.ProcessGroupPathMapperProvider;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ProcessGroupPathMapper {


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

}
