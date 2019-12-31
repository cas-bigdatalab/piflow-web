package com.nature.component.flow.service;

import com.nature.component.flow.model.FlowGroup;
import com.nature.component.flow.vo.FlowGroupVo;

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
     * save or add flowGroup
     *
     * @param imageXML
     * @param loadId
     * @param operType
     * @param flag
     * @return
     */
    public String saveOrUpdateFlowGroupAll(String imageXML, String loadId, String operType, boolean flag);

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


}
