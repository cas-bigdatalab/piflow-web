package com.nature.component.flow.service;

import com.nature.component.flow.model.FlowGroup;
import com.nature.component.flow.vo.FlowGroupVo;

import javax.transaction.Transactional;

public interface IFlowGroupService {


    /**
     * getFlowGroupById
     *
     * @param flowGroupId
     * @return
     */
    public FlowGroup getFlowGroupById(String flowGroupId);

    /**
     * getFlowGroupVoById
     *
     * @param flowGroupId
     * @return
     */
    public String getFlowGroupVoById(String flowGroupId);

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
    public String getFlowGroupListPage(Integer offset, Integer limit, String param);

    /**
     * save or update flowGroupVo
     *
     * @param flowGroupVo
     * @return
     */
    public String saveOrUpdate(FlowGroupVo flowGroupVo);

    /**
     * run flow group
     *
     * @param flowGroupId
     * @param runMode
     * @return
     */
    public String runFlowGroup(String flowGroupId, String runMode);

    /**
     * delete FLowGroup info
     *
     * @param id
     * @return
     */
    public int deleteFLowGroupInfo(String id);

    /**
     * Copy flow to group
     *
     * @param flowId
     * @param flowGroupId
     * @return
     */
    public String copyFlowToGroup(String flowId, String flowGroupId);

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
     * @param id
     * @param parentsId
     * @param flowGroupName
     * @param pageId
     * @return
     */
    public String updateFlowGroupNameById(String id, String parentsId, String flowGroupName, String pageId);

    /**
     * updateFlowGroupNameById
     *
     * @param id
     * @param flowGroupName
     * @return
     */
    public Boolean updateFlowGroupNameById(String id, String flowGroupName);

    /**
     * updateFlowGroupBaseInfo
     *
     * @param flowGroupVo
     * @return
     */
    public String updateFlowGroupBaseInfo(FlowGroupVo flowGroupVo);

    /**
     * Right click to run
     *
     * @param pId
     * @param nodeId
     * @param nodeType
     * @return
     */
    public String rightRun(String pId, String nodeId, String nodeType);

}
