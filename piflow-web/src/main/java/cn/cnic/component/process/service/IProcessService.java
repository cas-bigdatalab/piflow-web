package cn.cnic.component.process.service;

import java.util.List;

import javax.transaction.Transactional;

import cn.cnic.component.process.vo.DebugDataRequest;
import cn.cnic.component.process.vo.ProcessVo;

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
    public ProcessVo getProcessAllVoById(String username, boolean isAdmin, String id);

    /**
     * Query processVo according to ID (query process table only)
     *
     * @param id
     * @return
     */
    @Transactional
    public String getProcessVoById(String username, boolean isAdmin, String id);

    /**
     * Query process according to ID
     *
     * @param id
     * @return
     */
    @Transactional
    public ProcessVo getProcessById(String username, boolean isAdmin, String id);

    /**
     * Query process according to Appid
     *
     * @param appId
     * @return
     */
    public ProcessVo getProcessVoByAppId(String appId);

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
    public int updateProcess(String username, boolean isAdmin, ProcessVo processVo);

    /**
     * Generate Process from flowId and save it
     *
     * @param flowId
     * @return
     */
    @Transactional
    public ProcessVo flowToProcessAndSave(String username, String flowId);

    /**
     * Logical deletion
     *
     * @param processId
     * @return
     */
    @Transactional
    public String delProcess(String username, String processId);

    /**
     * Query the running process List (process List) according to flowId
     *
     * @param flowId
     * @return
     */
    public String getRunningProcessVoList(String flowId);

    /**
     * Query processVoList (parameter space-time non-paging)
     *
     * @param offset
     * @param limit
     * @param param
     * @return
     */
    @Transactional
    public String getProcessVoListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param);

    /**
     * Query processVoList (parameter space-time non-paging)
     *
     * @param offset
     * @param limit
     * @param param
     * @return
     */
    public String getProcessGroupVoListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param);

    /**
     * Start processes
     *
     * @param username
     * @param processId
     * @param checkpoint
     * @return
     */
    @Transactional
    public String startProcess(String username, String processId, String checkpoint, String runMode);

    /**
     * Stop running processes
     *
     * @param processId
     * @return
     */
    @Transactional
    public String stopProcess(String username, boolean isAdmin, String processId);

    /**
     * get debug data
     *
     * @param debugDataRequest
     * @return
     */
    public String getDebugData(DebugDataRequest debugDataRequest);

    /**
     * get visualization data
     *
     * @param appID
     * @param stopName
     * @param visualizationType
     * @return
     */
    public String getVisualizationData(String appID, String stopName, String visualizationType, boolean isSoft);

    /**
     * Query process based on processId and pageId
     *
     * @param processGroupId
     * @param pageId
     * @return
     */
    public ProcessVo getProcessVoByPageId(String username, boolean isAdmin, String processGroupId, String pageId);

    /**
     * getCheckpoints
     *
     * @param parentProcessId
     * @param pID
     * @return
     */
    public String getCheckpoints(String parentProcessId, String pID);

    /**
     * getLogUrl
     *
     * @param appId
     * @return
     */
    public String getLogUrl(String appId);

    /**
     * drawingBoard Data
     *
     * @param username
     * @param isAdmin
     * @param loadId
     * @param parentAccessPath
     * @return
     */
    public String drawingBoardData(String username, boolean isAdmin, String loadId, String parentAccessPath);
}
