package com.nature.component.process.service;

import com.nature.base.vo.StatefulRtnBase;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.RunModeType;
import com.nature.component.process.model.Process;
import com.nature.component.process.vo.DebugDataRequest;
import com.nature.component.process.vo.DebugDataResponse;
import com.nature.component.process.vo.ProcessVo;

import javax.transaction.Transactional;
import java.util.List;

public interface IProcessService {

    /**
     * Query processVoList (Query contains its subtables)
     *
     * @return
     */
    @Transactional
    public List<ProcessVo> getProcessAllVoList();

    /**
     * Query processVoList (query process table only)
     *
     * @return
     */
    @Transactional
    public List<ProcessVo> getProcessVoList();

    /**
     * Query processVo according to ID (query contains its subtable)
     *
     * @param id
     * @return
     */
    @Transactional
    public ProcessVo getProcessAllVoById(String id);

    /**
     * Query processVo according to ID (query process table only)
     *
     * @param id
     * @return
     */
    @Transactional
    public ProcessVo getProcessVoById(String id);

    /**
     * Query process according to ID
     *
     * @param id
     * @return
     */
    @Transactional
    public ProcessVo getProcessById(String id);

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
     * Copy process and create a new one
     *
     * @param processId
     * @return
     */
    public Process processCopyProcessAndAdd(String processId, UserVo currentUser, RunModeType runModeType);

    /**
     * Generate Process from flowId and save it
     *
     * @param flowId
     * @return
     */
    @Transactional
    public ProcessVo flowToProcessAndSave(String flowId);

    /**
     * Logical deletion
     *
     * @param processId
     * @return
     */
    @Transactional
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
    @Transactional
    public String getProcessVoListPage(Integer offset, Integer limit, String param);

    /**
     * Query processVoList (parameter space-time non-paging)
     *
     * @param offset
     * @param limit
     * @param param
     * @return
     */
    public String getProcessGroupVoListPage(Integer offset, Integer limit, String param);

    /**
     * Start processes
     *
     * @param processId
     * @param checkpoint
     * @param currentUser
     * @return
     */
    @Transactional
    public String startProcess(String processId, String checkpoint, String runMode, UserVo currentUser);

    /**
     * Stop running processes
     *
     * @param processId
     * @return
     */
    @Transactional
    public String stopProcess(String processId);

    /**
     * get debug data
     *
     * @param debugDataRequest
     * @return
     */
    public DebugDataResponse getDebugData(DebugDataRequest debugDataRequest);

    /**
     * Query process based on processId and pageId
     *
     * @param processGroupId
     * @param pageId
     * @return
     */
    public ProcessVo getProcessVoByPageId(String processGroupId, String pageId);
}
