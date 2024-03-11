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
            @Result(column = "fk_flow_process_group_id", property = "processGroup", one = @One(select = "cn.cnic.component.process.mapper.ProcessGroupMapper.getProcessGroupById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "flowGlobalParamsList", many = @Many(select = "cn.cnic.component.flow.mapper.FlowGlobalParamsMapper.getFlowGlobalParamsByProcessId", fetchType = FetchType.LAZY))

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
    public List<Process> getProcessNoGroupByAppId(String appID);

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

    /**
     * get globalParams ids by process id
     *
     * @param flowName
     * @return
     */
    @SelectProvider(type = ProcessMapperProvider.class, method = "getGlobalParamsIdsByProcessId")
    public String[] getGlobalParamsIdsByProcessId(@Param("processId") String processId);

    /**
     * link GlobalParams
     *
     * @param processId
     * @param globalParamsIds
     * @return
     */
    @InsertProvider(type = ProcessMapperProvider.class, method = "linkGlobalParams")
    public int linkGlobalParams(@Param("processId") String processId, @Param("globalParamsIds") String[] globalParamsIds);

    /**
     * unlink GlobalParams
     *
     * @param processId
     * @param globalParamsIds
     * @return
     */
    @DeleteProvider(type = ProcessMapperProvider.class, method = "unlinkGlobalParams")
    public int unlinkGlobalParams(@Param("processId") String processId, @Param("globalParamsIds") String[] globalParamsIds);


    /**
     * @param publishingId:
     * @param keyword:
     * @param username:
     * @return List<Process>
     * @author tianyao
     * @description 根据publishingId获取发布流水线的历史记录，加上生成的数据产品
     * @date 2024/2/23 9:41
     */
    @Select("<script>"
            +"select fp.* from flow_process as fp"
            +"<where>"
            +" fp.crt_user = #{username} and fp.flow_id = #{publishingId} and fp.enable_flag = 1 "
            +"<if test=\"keyword != null and keyword != '' \">"
            +" and `name` like CONCAT('%', #{keyword}, '%')"
            +"</if>"
            +"</where>"
            +"order by fp.crt_dttm desc"
            +"</script>")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processStopList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessStopMapper.getProcessStopByProcessId", fetchType = FetchType.LAZY)),
//            @Result(column = "flow_id", property = "flowPublishing", one = @One(select = "cn.cnic.component.flow.mapper.FlowPublishingMapper.getFullInfoById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "dataProductList", many = @Many(select = "cn.cnic.component.dataProduct.mapper.DataProductMapper.getListByProcessId", fetchType = FetchType.LAZY))

    })
    List<Process> getProcessListByPublishingIdAndUserName(@Param("publishingId") Long publishingId, @Param("keyword") String keyword, @Param("username") String username);

    @Select("<script>"
            +"select fp.* from flow_process as fp"
            +"<where>"
            +" fp.id = #{processId} and fp.enable_flag = 1 "
            +"</where>"
            +"</script>")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processStopList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessStopMapper.getProcessStopByProcessId", fetchType = FetchType.LAZY)),
//            @Result(column = "flow_id", property = "flowPublishing", one = @One(select = "cn.cnic.component.flow.mapper.FlowPublishingMapper.getFullInfoById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "dataProductList", many = @Many(select = "cn.cnic.component.dataProduct.mapper.DataProductMapper.getListByProcessId", fetchType = FetchType.LAZY))

    })
    Process getProcessWithFlowPublishingById(@Param("processId") String processId);
    @Select("<script>"
            +"select fp.* from flow_process as fp"
            +"<where>"
            +" fp.crt_user = #{username} and fp.enable_flag = 1 and fp.app_id is not null"
            +"<if test=\"keyword != null and keyword != '' \">"
            +" and `name` like CONCAT('%', #{keyword}, '%')"
            +"</if>"
            +"</where>"
            +"order by fp.crt_dttm desc"
            +"</script>")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processStopList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessStopMapper.getProcessStopByProcessId", fetchType = FetchType.LAZY)),
//            @Result(column = "flow_id", property = "flowPublishing", one = @One(select = "cn.cnic.component.flow.mapper.FlowPublishingMapper.getFullInfoById", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "dataProductList", many = @Many(select = "cn.cnic.component.dataProduct.mapper.DataProductMapper.getListByProcessId", fetchType = FetchType.LAZY))

    })
    List<Process> getProcessHistoryPageOfSelf(@Param("keyword") String keyword, @Param("username") String username);

    @SelectProvider(type = ProcessMapperProvider.class, method = "getProcessByPageIds")
    @Select("select * from flow_process where flow_id = #{flowId} and crt_user = #{username} and state is null ")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "mxGraphModel", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGraphModelMapper.getMxGraphModelByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processStopList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessStopMapper.getProcessStopByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processPathList", many = @Many(select = "cn.cnic.component.process.mapper.ProcessPathMapper.getProcessPathByProcessId", fetchType = FetchType.LAZY))

    })
    Process getByFlowIdAndCrtUserWithoutState(@Param("flowId") String flowId, @Param("username") String username);
}
