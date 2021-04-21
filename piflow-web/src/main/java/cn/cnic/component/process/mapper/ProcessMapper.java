package cn.cnic.component.process.mapper;

import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.mapper.provider.ProcessMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ProcessMapper {
    /**
     * addProcess
     *
     * @param process
     * @return
     */
    @InsertProvider(type = ProcessMapperProvider.class, method = "addProcess")
    public int addProcess(Process process);

    /**
     * update process
     *
     * @param process
     * @return
     */
    @UpdateProvider(type = ProcessMapperProvider.class, method = "updateProcess")
    public int updateProcess(Process process);

    /**
     * Query process by process ID
     *
     * @param id
     * @return
     */
    @SelectProvider(type = ProcessMapperProvider.class, method = "getProcessById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGraphModelMapper.getMxGraphModelByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processPathList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessPathMapper.getProcessPathByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processStopList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessStopMapper.getProcessStopByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "fk_flow_process_group_id", property = "processGroup", one = @One(select = "cn.cnic.component.process.mapper.ProcessGroupMapper.getProcessGroupById", fetchType = FetchType.LAZY))

    })
    public Process getProcessById(@Param("username") String username, @Param("isAdmin") boolean isAdmin, @Param("id") String id);

    /**
     * Query process by processGroup ID
     *
     * @param processGroupId
     * @return
     */
    @SelectProvider(type = ProcessMapperProvider.class, method = "getProcessByProcessGroupId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGraphModelMapper.getMxGraphModelByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processStopList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessStopMapper.getProcessStopByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processPathList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessPathMapper.getProcessPathByProcessId", fetchType = FetchType.LAZY))

    })
    public List<Process> getProcessByProcessGroupId(@Param("processGroupId") String processGroupId);

    /**
     * Query process List(processList)
     *
     * @return
     */
    @SelectProvider(type = ProcessMapperProvider.class, method = "getProcessList")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGraphModelMapper.getMxGraphModelByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processStopList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessStopMapper.getProcessStopByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processPathList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessPathMapper.getProcessPathByProcessId", fetchType = FetchType.LAZY))

    })
    public List<Process> getProcessList();

    /**
     * Query process list according to param(processList)
     *
     * @param param
     * @return
     */
    @SelectProvider(type = ProcessMapperProvider.class, method = "getProcessListByParam")
    @Results({
            @Result(id = true, column = "id", property = "id"),
    })
    public List<Process> getProcessListByParam(@Param("username") String username, @Param("isAdmin") boolean isAdmin, @Param("param") String param);

    /**
     * Query processGroup list according to param(processList)
     *
     * @param param
     * @return
     */
    @SelectProvider(type = ProcessMapperProvider.class, method = "getProcessGroupListByParam")
    @Results({
            @Result(id = true, column = "id", property = "id"),
    })
    public List<Process> getProcessGroupListByParam(@Param("username") String username, @Param("isAdmin") boolean isAdmin, @Param("param") String param);

    /**
     * Query the running process list according to the flowid(processList)
     *
     * @return
     */
    @SelectProvider(type = ProcessMapperProvider.class, method = "getRunningProcessList")
    public List<Process> getRunningProcessList(@Param("flowId") String flowId);

    /**
     * Query process according to process appid
     *
     * @param appID
     * @return
     */
    @SelectProvider(type = ProcessMapperProvider.class, method = "getProcessByAppId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGraphModelMapper.getMxGraphModelByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processStopList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessStopMapper.getProcessStopByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processPathList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessPathMapper.getProcessPathByProcessId", fetchType = FetchType.LAZY))

    })
    public Process getProcessByAppId(String appID);

    @SelectProvider(type = ProcessMapperProvider.class, method = "getProcessIdByAppId")
    public String getProcessIdByAppId(String appID);

    /**
     * Query process according to process appid
     *
     * @param appID
     * @return
     */
    @SelectProvider(type = ProcessMapperProvider.class, method = "getProcessNoGroupByAppId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGraphModelMapper.getMxGraphModelByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processStopList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessStopMapper.getProcessStopByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processPathList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessPathMapper.getProcessPathByProcessId", fetchType = FetchType.LAZY))

    })
    public Process getProcessNoGroupByAppId(String appID);

    /**
     * Query process list according to process appid array
     *
     * @param appIDs
     * @return
     */
    @SelectProvider(type = ProcessMapperProvider.class, method = "getProcessListByAppIDs")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGraphModelMapper.getMxGraphModelByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processStopList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessStopMapper.getProcessStopByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processPathList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessPathMapper.getProcessPathByProcessId", fetchType = FetchType.LAZY))

    })
    public List<Process> getProcessListByAppIDs(@Param("appIDs") String[] appIDs);


    /**
     * update process EnableFlag
     *
     * @param id
     * @return
     */
    @UpdateProvider(type = ProcessMapperProvider.class, method = "updateEnableFlag")
    public int updateEnableFlag(String id, String username);

    /**
     * Query tasks that need to be synchronized
     *
     * @return
     */
    @SelectProvider(type = ProcessMapperProvider.class, method = "getRunningProcess")
    public List<String> getRunningProcess();

    /**
     * Query process by pageId
     *
     * @param processGroupId
     * @param pageId
     * @return
     */
    @SelectProvider(type = ProcessMapperProvider.class, method = "getProcessByPageId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGraphModelMapper.getMxGraphModelByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processStopList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessStopMapper.getProcessStopByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processPathList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessPathMapper.getProcessPathByProcessId", fetchType = FetchType.LAZY))

    })
    public Process getProcessByPageId(@Param("username") String username, @Param("isAdmin") boolean isAdmin, @Param("processGroupId") String processGroupId, @Param("pageId") String pageId);

    /**
     * Query process by pageIds
     *
     * @param processGroupId
     * @param pageIds
     * @return
     */
    @SelectProvider(type = ProcessMapperProvider.class, method = "getProcessByPageIds")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGraphModelMapper.getMxGraphModelByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processStopList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessStopMapper.getProcessStopByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processPathList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessPathMapper.getProcessPathByProcessId", fetchType = FetchType.LAZY))

    })
    public List<Process> getProcessByPageIds(@Param("processGroupId") String processGroupId, @Param("pageIds") String[] pageIds);

    @Select("select fp.run_mode_type from flow_process fp where fp.enable_flag = 1 and fp.id=#{id}")
    public RunModeType getProcessRunModeTypeById(String id);

    @Select("select app_id from flow_process where enable_flag=1 and fk_flow_process_group_id is null and app_id is not null and ( ( state!='COMPLETED' and state!='FINISHED' and state!='FAILED' and state!='KILLED' ) or state is null )")
    public List<String> getRunningProcessAppId();

    @Select("select s.id from flow_process s where s.enable_flag=1 and s.fk_flow_process_group_id=#{fid} and s.page_id=#{pageId}")
    public String getProcessIdByPageId(@Param("fid") String fid, @Param("pageId") String pageId);
    
    @Select("select state from flow_process where enable_flag=1 and id=#{id} ")
    public ProcessState getProcessStateById(String id);
    
    
    
    
}
