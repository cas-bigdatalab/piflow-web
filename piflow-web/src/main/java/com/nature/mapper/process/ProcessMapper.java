package com.nature.mapper.process;

import com.nature.component.process.model.Process;
import com.nature.provider.process.ProcessMapperProvider;
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
     * 根据进程Id查询进程
     *
     * @param id
     * @return
     */
    @SelectProvider(type = ProcessMapperProvider.class, method = "getProcessById")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processStopList", many = @Many(select = "com.nature.mapper.process.ProcessStopMapper.getProcessStopByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processPathList", many = @Many(select = "com.nature.mapper.process.ProcessPathMapper.getProcessPathByProcessId", fetchType = FetchType.LAZY))

    })
    public Process getProcessById(String id);

    /**
     * 查询进程List(processList)
     *
     * @return
     */
    @SelectProvider(type = ProcessMapperProvider.class, method = "getProcessList")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processStopList", many = @Many(select = "com.nature.mapper.process.ProcessStopMapper.getProcessStopByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processPathList", many = @Many(select = "com.nature.mapper.process.ProcessPathMapper.getProcessPathByProcessId", fetchType = FetchType.LAZY))

    })
    public List<Process> getProcessList();

    /**
     * 根据进程AppId查询进程
     *
     * @param appID
     * @return
     */
    @SelectProvider(type = ProcessMapperProvider.class, method = "getProcessByAppId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processStopList", many = @Many(select = "com.nature.mapper.process.ProcessStopMapper.getProcessStopByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processPathList", many = @Many(select = "com.nature.mapper.process.ProcessPathMapper.getProcessPathByProcessId", fetchType = FetchType.LAZY))

    })
    public Process getProcessByAppId(String appID);

    /**
     * 根据进程AppId数组查询进程list
     *
     * @param appIDs
     * @return
     */
    @SelectProvider(type = ProcessMapperProvider.class, method = "getProcessListByAppIDs")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "id", property = "processStopList", many = @Many(select = "com.nature.mapper.process.ProcessStopMapper.getProcessStopByProcessId", fetchType = FetchType.LAZY)),
            @Result(column = "id", property = "processPathList", many = @Many(select = "com.nature.mapper.process.ProcessPathMapper.getProcessPathByProcessId", fetchType = FetchType.LAZY))

    })
    public List<Process> getProcessListByAppIDs(@Param("appIDs") String[] appIDs);

    /**
     * 修改process
     *
     * @param process
     * @return
     */
    @UpdateProvider(type = ProcessMapperProvider.class, method = "updateProcess")
    public int updateProcess(Process process);

    /**
     * 修改process
     *
     * @param id
     * @return
     */
    @UpdateProvider(type = ProcessMapperProvider.class, method = "updateEnableFlag")
    public int updateEnableFlag(String id, String username);

}
