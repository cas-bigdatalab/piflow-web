package cn.cnic.component.flow.service;

import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.flow.vo.FlowGroupVo;

import javax.transaction.Transactional;

public interface IFlowGroupService {


    /**
     * getFlowGroupById
     *
     * @param username
     * @param isAdmin
     * @param flowGroupId
     * @return
     */
    public FlowGroup getFlowGroupById(String username, boolean isAdmin, String flowGroupId);

    /**
     * getFlowGroupVoById
     *
     * @param flowGroupId
     * @return
     */
    public String getFlowGroupVoInfoById(String flowGroupId);

    /**
     * getFlowGroupVoAllById
     *
     * @param flowGroupId
     * @return
     */
    public String getFlowGroupVoAllById(String flowGroupId);

    /**
     * Paging query flow
     *
     * @param offset Number of pages
     * @param limit  Number of pages per page
     * @param param  search for the keyword
     * @return
     */
    public String getFlowGroupListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param);

    /**
     * save or update flowGroupVo
     *
     * @param username
     * @param flowGroupVo
     * @return
     */
    public String saveOrUpdate(String username, FlowGroupVo flowGroupVo);

    /**
     * run flow group
     *
     * @param username
     * @param flowGroupId
     * @param runMode
     * @return
     */
    public String runFlowGroup(String username, String flowGroupId, String runMode);

    /**
     * delete FLowGroup info
     *
     * @param id
     * @return
     */
    public String deleteFLowGroupInfo(boolean isAdmin, String username, String id);

    /**
     * Copy flow to group
     *
     * @param username
     * @param flowId
     * @param flowGroupId
     * @return
     */
    public String copyFlowToGroup(String username, String flowId, String flowGroupId);

    /**
     * Query FlowGroupVo information based on pageId
     *
     * @param fid
     * @param pageId
     * @return
     */
    @Transactional
    public FlowGroupVo getFlowGroupByPageId(String fid, String pageId);

    /**
     * updateFlowGroupNameById
     *
     * @param username
     * @param id
     * @param parentsId
     * @param flowGroupName
     * @param pageId
     * @return
     */
    public String updateFlowGroupNameById(String username, String id, String parentsId, String flowGroupName, String pageId);

    /**
     * updateFlowGroupNameById
     *
     * @param id
     * @param flowGroupName
     * @return
     */
    public Boolean updateFlowGroupNameById(String username, String id, String flowGroupName);

    /**
     * updateFlowGroupBaseInfo
     *
     * @param username
     * @param flowGroupVo
     * @return
     */
    public String updateFlowGroupBaseInfo(String username,  String fId,FlowGroupVo flowGroupVo);

    /**
     * Right click to run
     *
     * @param username
     * @param isAdmin
     * @param pId
     * @param nodeId
     * @param nodeType
     * @return
     */
    public String rightRun(String username, boolean isAdmin, String pId, String nodeId, String nodeType);

    /**
     * Query FlowGroupVo or FlowVo information based on pageId
     *
     * @param fid
     * @param pageId
     * @return
     */
    public String queryIdInfo(String fid, String pageId);

    /**
     * drawingBoard Data
     *
     * @param username
     * @param isAdmin
     * @param load
     * @param parentAccessPath
     * @return
     */
    public String drawingBoardData(String username, boolean isAdmin, String load, String parentAccessPath);

}
