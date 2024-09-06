package cn.cnic.component.process.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.FetchType;

import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.mapper.provider.ProcessGroupMapperProvider;
import cn.cnic.component.process.vo.ProcessGroupVo;

@Mapper
public interface ProcessGroupMapper {

    @InsertProvider(type = ProcessGroupMapperProvider.class, method = "addProcessGroup")
    public int addProcessGroup(ProcessGroup processGroup, @Param("company") String company);

    /**
     * update updateProcessGroup
     *
     * @param processGroup
     * @return
     */
    @UpdateProvider(type = ProcessGroupMapperProvider.class, method = "updateProcessGroup")
    public int updateProcessGroup(ProcessGroup processGroup);

    
    /**
     * Query processGroup by processGroup ID
     *
     * @param id
     * @return
     */
    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getProcessGroupByIdAndUser")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "fk_flow_process_group_id", property = "processGroup", one = @One(select = "cn.cnic.component.process.mapper.ProcessGroupMapper.getProcessGroupById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGraphModelMapper.getMxGraphModelByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessMapper.getProcessByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessGroupMapper.getProcessGroupByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupPathList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessGroupPathMapper.getProcessPathByProcessGroupId", fetchType = FetchType.LAZY))
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
            @Result(column = "fk_flow_process_group_id", property = "processGroup", one = @One(select = "cn.cnic.component.process.mapper.ProcessGroupMapper.getProcessGroupById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGraphModelMapper.getMxGraphModelByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessMapper.getProcessByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessGroupMapper.getProcessGroupByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupPathList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessGroupPathMapper.getProcessPathByProcessGroupId", fetchType = FetchType.LAZY))
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
            @Result(column = "fk_flow_process_group_id", property = "processGroup", one = @One(select = "cn.cnic.component.process.mapper.ProcessGroupMapper.getProcessGroupById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGraphModelMapper.getMxGraphModelByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessMapper.getProcessByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessGroupMapper.getProcessGroupByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupPathList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessGroupPathMapper.getProcessPathByProcessGroupId", fetchType = FetchType.LAZY))
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
            @Result(column = "fk_flow_process_group_id", property = "processGroup", one = @One(select = "cn.cnic.component.process.mapper.ProcessGroupMapper.getProcessGroupById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessMapper.getProcessByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessGroupMapper.getProcessGroupByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupPathList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessGroupPathMapper.getProcessPathByProcessGroupId", fetchType = FetchType.LAZY))

    })
    public ProcessGroup getProcessGroupByAppId(String appID);

    /**
     * Query process according to process appId
     *
     * @param appID
     * @return
     */
    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getProcessGroupIdByAppId")
    public List<String> getProcessGroupIdByAppId(String appID);

    /**
     * Query process list according to process appid array
     *
     * @param appIDs
     * @return
     */
    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getProcessGroupListByAppIDs")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessMapper.getProcessByProcessGroupId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processGroupPathList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessGroupPathMapper.getProcessPathByProcessGroupId", fetchType = FetchType.LAZY))
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
    public List<ProcessGroupVo> getProcessGroupListByParam(@Param("username") String username, @Param("isAdmin") boolean isAdmin,
                                                           @Param("param") String param, @Param("name") String name,
                                                           @Param("String") String state, @Param("crtUser") String crtUser,
                                                           @Param("company") String company);

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

    @Select("select app_id from flow_process_group where enable_flag=1 and app_id is not null and ( (state!='COMPLETED' and state!='FINISHED' and state!='FAILED' and state!='KILLED') or state is null )")
    public List<String> getRunningProcessGroupAppId();

    @SelectProvider(type = ProcessGroupMapperProvider.class, method = "getProcessGroupNamesAndPageIdsByPageIds")
    public List<Map<String, Object>> getProcessGroupNamesAndPageIdsByPageIds(@Param("fid") String fid, @Param("pageIds") List<String> pageIds);
    
    @Select("select s.id from flow_process_group s where s.enable_flag=1 and s.fk_flow_process_group_id=#{fid} and s.page_id=#{pageId}")
    public String getProcessGroupIdByPageId(@Param("fid") String fid, @Param("pageId") String pageId);
    
    @Select("select * from flow_process_group s where s.enable_flag=1 and s.fk_flow_process_group_id=#{fid} and s.page_id=#{pageId}")
    public ProcessGroup getProcessGroupByPageId(@Param("fid") String fid, @Param("pageId") String pageId);
    

}
