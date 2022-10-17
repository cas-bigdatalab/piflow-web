package cn.cnic.component.flow.service;

import java.util.List;

import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.vo.FlowVo;
import cn.cnic.controller.requestVo.FlowInfoVoRequestAdd;
import cn.cnic.controller.requestVo.FlowInfoVoRequestUpdate;

public interface IFlowService {

    /**
     * Query flow information based on id
     *
     * @param id
     * @return
     */
    public Flow getFlowById(String username, boolean isAdmin, String id);

    /**
     * Query flow information based on pageId
     *
     * @param fid
     * @param pageId
     * @return
     */
    public FlowVo getFlowByPageId(String fid, String pageId);

    /**
     * Query flow information based on id
     *
     * @param id
     * @return
     */
    public String getFlowVoById(String id);

    /**
     * add flow(Contains drawing board information)
     * 
     * @param username
     * @param flowVo
     * @return
     * @throws Exception
     */
    public String addFlow(String username, FlowInfoVoRequestAdd flowVo) throws Exception ;

    public String deleteFLowInfo(String username, boolean isAdmin, String id);

    public Integer getMaxStopPageId(String flowId);

    public List<FlowVo> getFlowList();

    /**
     * Paging query flow
     *
     * @param username
     * @param isAdmin
     * @param offset   Number of pages
     * @param limit    Number of pages per page
     * @param param    search for the keyword
     * @return
     */
    public String getFlowListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param);

    public String getFlowExampleList();


    /**
     * Call the start interface and save the return information
     *
     * @param flowId
     * @return
     */
    public String runFlow(String username, boolean isAdmin, String flowId, String runMode) throws Exception;

    /**
     * Call the start interface and save the return information
     *
     * @param publishingId
     * @return
     */
    public String runFlowByPublishingId(String username, boolean isAdmin, String publishingId, String runMode) throws Exception;

    public String updateFlowBaseInfo(String username, String fId, FlowInfoVoRequestUpdate flowVo) throws Exception;

    public String updateFlowNameById(String username, String id, String flowGroupId, String flowName, String pageId) throws Exception;

    public Boolean updateFlowNameById(String username, String id, String flowName) throws Exception;

    public Integer getMaxFlowPageIdByFlowGroupId(String flowGroupId);

    public String drawingBoardData(String username, boolean isAdmin, String load, String parentAccessPath);

}
