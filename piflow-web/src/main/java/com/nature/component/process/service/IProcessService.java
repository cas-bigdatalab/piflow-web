package com.nature.component.process.service;

import com.nature.base.vo.StatefulRtnBase;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.Flow;
import com.nature.component.process.model.Process;
import com.nature.component.process.vo.ProcessVo;
import org.springframework.data.annotation.Transient;

import java.util.List;

public interface IProcessService {

    /**
     * Query processVoList (Query contains its subtables)
     *
     * @return
     */
    @Transient
    public List<ProcessVo> getProcessAllVoList();

    /**
     * Query processVoList (query process table only)
     *
     * @return
     */
    @Transient
    public List<ProcessVo> getProcessVoList();

    /**
     * Query processVo according to ID (query contains its subtable)
     *
     * @param id
     * @return
     */
    @Transient
    public ProcessVo getProcessAllVoById(String id);

    /**
     * Query processVo according to ID (query process table only)
     *
     * @param id
     * @return
     */
    @Transient
    public ProcessVo getProcessVoById(String id);

    /**
     * Query process according to ID
     *
     * @param id
     * @return
     */
    @Transient
    public Process getProcessById(String id);

    /**
     * Query process according to Appid
     *
     * @param appId
     * @return
     */
    public ProcessVo getProcessVoByAppId(String appId);

    /**
     * Query process based on array Appid
     *
     * @param appIDs
     * @return
     */
    public List<ProcessVo> getProcessVoByAppIds(String appIDs);

    /**
     * Query appInfo at third-party interface according to appID and save it
     *
     * @param appID
     * @return
     */
    public ProcessVo getAppInfoByThirdAndSave(String appID);

    /**
     * Query appInfo according to appID
     *
     * @param appID
     * @return
     */
    public String getAppInfoByAppId(String appID);

    /**
     * Query and save process at the third-party interface according to appID
     *
     * @param appIDs
     * @return
     */
    public String getProgressByThirdAndSave(String[] appIDs);

    /**
     * Query  process according to appID
     *
     * @param appIDs
     * @return
     */
    public String getProgressByAppIds(String[] appIDs);

    /**
     * Update process
     *
     * @param processVo
     * @return
     */
    public int updateProcess(ProcessVo processVo, UserVo currentUser);

    /**
     * Call the start interface and save the return information
     *
     * @param flow
     * @return
     */
    public Process startFlowAndUpdateProcess(Flow flow, UserVo currentUser);

    /**
     * Copy process and create a new one
     *
     * @param processId
     * @return
     */
    public Process processCopyProcessAndAdd(String processId, UserVo currentUser);

    /**
     * Generate Process from flowId and save it
     *
     * @param flowId
     * @return
     */
    @Transient
    public Process flowToProcessAndSave(String flowId);

    /**
     * Logical deletion
     *
     * @param processId
     * @return
     */
    @Transient
    public StatefulRtnBase updateProcessEnableFlag(String processId, UserVo currentUser);

    /**
     * Query the running process List (process List) according to flowId
     *
     * @param flowId
     * @return
     */
    public List<ProcessVo> getRunningProcessVoList(String flowId);

    /**
     * Query processVoList (parameter space-time non-paging)
     *
     * @param offset
     * @param limit
     * @param param
     * @return
     */
    @Transient
    public String getProcessVoListPage(Integer offset, Integer limit, String param);

    /**
     * Start processes
     *
     * @param processId
     * @param checkpoint
     * @param currentUser
     * @return
     */
    @Transient
    public String startProcess(String processId, String checkpoint, UserVo currentUser);

    /**
     * Stop running processes
     *
     * @param processId
     * @return
     */
    @Transient
    public String stopProcess(String processId);
}
