package cn.cnic.mapper.process;

import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.process.model.ProcessGroup;
import cn.cnic.provider.process.ProcessGroupMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ProcessGroupMapper {

    /**
     * Query processGroup by processGroup ID
     *
     * @param id
     * @return
     */
    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getProcessGroupByIdAndUser")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "fk_flow_process_group_id", property = "processGroup", one = @One(select = "cn.cnic.mapper.process.ProcessGroupMapper.getProcessGroupById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.mapper.mxGraph.MxGraphModelMapper.getMxGraphModelByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processList", many = @Many(select = "cn.cnic.mapper.process.ProcessMapper.getProcessByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupList", many = @Many(select = "cn.cnic.mapper.process.ProcessGroupMapper.getProcessGroupByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupPathList", many = @Many(select = "cn.cnic.mapper.process.ProcessGroupPathMapper.getProcessPathByProcessGroupId", fetchType = FetchType.LAZY))
    })
    public ProcessGroup getProcessGroupByIdAndUser(@Param("username") String username, @Param("isAdmin") boolean isAdmin, @Param("id") String id);

    /**
     * Query processGroup by processGroup ID
     *
     * @param id
     * @return
     */
    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getProcessGroupById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "fk_flow_process_group_id", property = "processGroup", one = @One(select = "cn.cnic.mapper.process.ProcessGroupMapper.getProcessGroupById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.mapper.mxGraph.MxGraphModelMapper.getMxGraphModelByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processList", many = @Many(select = "cn.cnic.mapper.process.ProcessMapper.getProcessByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupList", many = @Many(select = "cn.cnic.mapper.process.ProcessGroupMapper.getProcessGroupByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupPathList", many = @Many(select = "cn.cnic.mapper.process.ProcessGroupPathMapper.getProcessPathByProcessGroupId", fetchType = FetchType.LAZY))
    })
    public ProcessGroup getProcessGroupById(@Param("id") String id);

    /**
     * Query processGroup by processGroup ID
     *
     * @param processGroupId
     * @return
     */
    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getProcessGroupByProcessGroupId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "fk_flow_process_group_id", property = "processGroup", one = @One(select = "cn.cnic.mapper.process.ProcessGroupMapper.getProcessGroupById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.mapper.mxGraph.MxGraphModelMapper.getMxGraphModelByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processList", many = @Many(select = "cn.cnic.mapper.process.ProcessMapper.getProcessByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupList", many = @Many(select = "cn.cnic.mapper.process.ProcessGroupMapper.getProcessGroupByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupPathList", many = @Many(select = "cn.cnic.mapper.process.ProcessGroupPathMapper.getProcessPathByProcessGroupId", fetchType = FetchType.LAZY))
    })
    public ProcessGroup getProcessGroupByProcessGroupId(@Param("processGroupId") String processGroupId);

    /**
     * getRunModeTypeById
     *
     * @param processGroupId
     * @return
     */
    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getRunModeTypeById")
    public RunModeType getRunModeTypeById(@Param("username") String username, @Param("isAdmin") boolean isAdmin, @Param("processGroupId") String processGroupId);

    /**
     * Query process according to process appId
     *
     * @param appID
     * @return
     */
    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getProcessGroupByAppId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processList", many = @Many(select = "cn.cnic.mapper.process.ProcessMapper.getProcessByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupPathList", many = @Many(select = "cn.cnic.mapper.process.ProcessGroupPathMapper.getProcessPathByProcessGroupId", fetchType = FetchType.LAZY))

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
            @Result(column = "id", property = "processList", many = @Many(select = "cn.cnic.mapper.process.ProcessMapper.getProcessByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupPathList", many = @Many(select = "cn.cnic.mapper.process.ProcessGroupPathMapper.getProcessPathByProcessGroupId", fetchType = FetchType.LAZY))
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
    public List<ProcessGroup> getProcessGroupListByParam(@Param("username") String username, @Param("isAdmin") boolean isAdmin, @Param("param") String param);

    /**
     * Query processGroup list
     *
     * @return
     */
    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getProcessGroupList")
    @Results({
            @Result(id = true, column = "id", property = "id"),
    })
    public List<ProcessGroup> getProcessGroupList(@Param("username") String username, @Param("isAdmin") boolean isAdmin);

    @UpdateProvider(type = ProcessGroupMapperProvider.class, method = "updateEnableFlagById")
    public int updateEnableFlagById(String id, String userName);

}
