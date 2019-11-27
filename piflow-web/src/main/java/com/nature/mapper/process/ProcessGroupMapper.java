package com.nature.mapper.process;

import com.nature.common.Eunm.RunModeType;
import com.nature.component.process.model.ProcessGroup;
import com.nature.provider.process.ProcessGroupMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ProcessGroupMapper {

    /**
     * Query processGroup by processGroup ID
     *
     * @param processGroupId
     * @return
     */
    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getProcessGroupById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processList", many = @Many(select = "com.nature.mapper.process.ProcessMapper.getProcessByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupPathList", many = @Many(select = "com.nature.mapper.process.ProcessGroupPathMapper.getProcessPathByProcessGroupId", fetchType = FetchType.LAZY))

    })
    public ProcessGroup getProcessGroupById(@Param("processGroupId") String processGroupId);

    /**
     * getRunModeTypeById
     *
     * @param processGroupId
     * @return
     */
    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getRunModeTypeById")
    public RunModeType getRunModeTypeById(@Param("processGroupId") String processGroupId);

    /**
     * Query process according to process appId
     *
     * @param appID
     * @return
     */
    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getProcessGroupByAppId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processList", many = @Many(select = "com.nature.mapper.process.ProcessMapper.getProcessByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupPathList", many = @Many(select = "com.nature.mapper.process.ProcessGroupPathMapper.getProcessPathByProcessGroupId", fetchType = FetchType.LAZY))

    })
    public ProcessGroup getProcessGroupByAppId(String appID);

    /**
     * Query process list according to process appid array
     *
     * @param appIDs
     * @return
     */
    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getProcessGroupListByAppIDs")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processList", many = @Many(select = "com.nature.mapper.process.ProcessMapper.getProcessByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupPathList", many = @Many(select = "com.nature.mapper.process.ProcessGroupPathMapper.getProcessPathByProcessGroupId", fetchType = FetchType.LAZY))
    })
    public List<ProcessGroup> getProcessGroupListByAppIDs(@Param("appIDs") String[] appIDs);

    /**
     * getRunningProcessGroup
     *
     * @return
     */
    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getRunningProcessGroup")
    public List<String> getRunningProcessGroup();

    /**
     * Query processGroup list according to param(processGroupList)
     *
     * @param param
     * @return
     */
    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getProcessGroupListByParam")
    @Results({
            @Result(id = true, column = "id", property = "id"),
    })
    public List<ProcessGroup> getProcessGroupListByParam(@Param("param") String param);

    /**
     * Query processGroup list
     *
     * @return
     */
    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getProcessGroupList")
    @Results({
            @Result(id = true, column = "id", property = "id"),
    })
    public List<ProcessGroup> getProcessGroupList();

    /**
     * Query processGroup list
     *
     * @return
     */
    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getProcessGroupList")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processList", many = @Many(select = "com.nature.mapper.process.ProcessMapper.getProcessByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupPathList", many = @Many(select = "com.nature.mapper.process.ProcessGroupPathMapper.updateEnableFlagByProcessGroupId", fetchType = FetchType.LAZY))
    })
    public List<ProcessGroup> getProcessGroupAllList();

    @UpdateProvider(type = ProcessGroupMapperProvider.class, method = "updateEnableFlagById")
    public int updateEnableFlagById(String id, String userName);

}
