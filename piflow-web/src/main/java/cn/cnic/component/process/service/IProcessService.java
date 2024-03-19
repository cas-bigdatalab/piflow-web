package cn.cnic.component.process.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import cn.cnic.component.process.vo.DebugDataRequest;
import cn.cnic.component.process.vo.ProcessVo;

public interface IProcessService {

    /**
     * Query processVoList (Query contains its subtables)
     *
     * @return
     */
    public List<ProcessVo> getProcessAllVoList();

    /**
     * Query processVoList (query process table only)
     *
     * @return
     */
    public List<ProcessVo> getProcessVoList();

    /**
     * Query processVo according to ID (query contains its subtable)
     *
     * @param id
     * @return
     */
    public ProcessVo getProcessAllVoById(String username, boolean isAdmin, String id);

    /**
     * Query processVo according to ID (query process table only)
     *
     * @param id
     * @return
     */
    public String getProcessVoById(String username, boolean isAdmin, String id);

    /**
     * Query process according to ID
     *
     * @param id
     * @return
     */
    public ProcessVo getProcessById(String username, boolean isAdmin, String id);

    /**
     * Query process according to Appid
     *
     * @param appId
     * @return
     */
    public ProcessVo getProcessVoByAppId(String appId);

    /**
     * Query appInfo on a third-party interface based on appID and save
     *
     * @param appID
     * @return
     * @throws Exception 
     */
    public ProcessVo getAppInfoByThirdAndSave(String appID) throws Exception;

    /**
     * Query appInfo according to appID
     *
     * @param appID
     * @return
     */
    public String getAppInfoByAppId(String appID);

    /**
     * Query progress and save on third-party interface according to appID
     *
     * @param appIDs
     * @return
     * @throws Exception 
     */
    public String getProgressByThirdAndSave(String[] appIDs) throws Exception;

    /**
     * Query process according to appID
     *
     * @param appIDs
     * @return
     */
    public String getProgressByAppIds(String[] appIDs);

    /**
     * Modify the process (only update the process table, the subtable is not updated)
     *
     * @param processVo
     * @return
     * @throws Exception 
     */
    public int updateProcess(String username, boolean isAdmin, ProcessVo processVo) throws Exception;

    /**
     * Generate Process from flowId and save it
     *
     * @param isAdmin
     * @param username
     * @param flowId
     * @return
     */
    public ProcessVo flowToProcessAndSave(boolean isAdmin, String username, String flowId) throws Exception;

    /**
     * Logical deletion
     *
     * @param processId
     * @return
     */
    public String delProcess(boolean isAdmin, String username, String processId);

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
     * @param isAdmin
     * @param username
     * @param processId
     * @param checkpoint
     * @param runMode
     * @return
     * @throws Exception 
     */
    public String startProcess(boolean isAdmin, String username, String processId, String checkpoint, String runMode) throws Exception;

    /**
     * Stop running processes
     *
     * @param processId
     * @return
     */
    public String stopProcess(String username, boolean isAdmin, String processId) throws Exception;

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

    /**
     * Query processStop based on processId and pageId
     *
     * @param processId
     * @param pageId
     * @return
     */
    public String getProcessStopVoByPageId(String processId, String pageId);

    String getErrorLogInfo(String appId);

    String getProcessPageByPublishingId(ProcessVo processVo);

    String getProcessHistoryPageOfSelf(ProcessVo processVo);

    String getByProcessId(String processId) throws InvocationTargetException, IllegalAccessException;

    String getAppIdByProcessId(String processId);
}
