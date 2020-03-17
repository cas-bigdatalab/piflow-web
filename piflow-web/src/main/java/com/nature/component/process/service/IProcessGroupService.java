package com.nature.component.process.service;

import com.nature.base.vo.UserVo;
import com.nature.component.process.vo.DebugDataRequest;
import com.nature.component.process.vo.DebugDataResponse;
import com.nature.component.process.vo.ProcessGroupVo;


public interface IProcessGroupService {

    /**
     * Query processGroup according to ID
     *
     * @param id
     * @return
     */
    public ProcessGroupVo getProcessGroupById(String id);

    /**
     * Query processGroupVo according to ID (query contains its subtable)
     *
     * @param id
     * @return
     */
    public ProcessGroupVo getProcessAllVoById(String id);

    /**
     * Query processGroupVo according to ID (query process table only)
     *
     * @param id
     * @return
     */
    public ProcessGroupVo getProcessGroupVoById(String id);

    /**
     * Query appInfo according to appID
     *
     * @param appID
     * @return
     */
    public String getAppInfoByAppId(String appID);

    /**
     * Query  appInfo according to appID
     *
     * @param appIDs
     * @return
     */
    public String getAppInfoByAppIds(String[] appIDs);

    /**
     * Query processGroupVoList (parameter space-time non-paging)
     *
     * @param offset
     * @param limit
     * @param param
     * @return
     */
    public String getProcessGroupVoListPage(Integer offset, Integer limit, String param);

    /**
     * Start processesGroup
     *
     * @param processId
     * @param checkpoint
     * @param currentUser
     * @return
     */
    public String startProcessGroup(String processId, String checkpoint, String runMode, UserVo currentUser);

    /**
     * Stop running processGroup
     *
     * @param processGroupId
     * @return
     */
    public String stopProcessGroup(String processGroupId);

    /**
     * get debug data
     *
     * @param debugDataRequest
     * @return
     */
    public DebugDataResponse getDebugData(DebugDataRequest debugDataRequest);

    /**
     * delProcessGroup
     *
     * @param processGroupID
     * @return
     */
    public String delProcessGroup(String processGroupID);

    /**
     * getGroupLogData
     *
     * @param processGroupAppID
     * @return
     */
    public String getGroupLogData(String processGroupAppID);

    /**
     * getStartGroupJson
     *
     * @param processGroupId
     * @return
     */
    public String getStartGroupJson(String processGroupId);

    /**
     * getProcessIdByPageId
     *
     * @param fId
     * @param pageId
     * @return
     */
    public String getProcessIdByPageId(String fId, String pageId);

    /**
     * getProcessGroupIdByPageId
     *
     * @param fId
     * @param pageId
     * @return
     */
    public String getProcessGroupIdByPageId(String fId, String pageId);

    /**
     * getProcessGroupVoByPageId
     *
     * @param processGroupId
     * @param pageId
     * @return
     */
    public ProcessGroupVo getProcessGroupVoByPageId(String processGroupId, String pageId);

}
