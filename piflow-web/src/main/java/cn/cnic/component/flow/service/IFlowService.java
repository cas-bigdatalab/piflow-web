package cn.cnic.component.flow.service;

import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.vo.FlowVo;

import javax.transaction.Transactional;
import java.util.List;

public interface IFlowService {

    /**
     * Query flow information based on id
     *
     * @param id
     * @return
     */
    @Transactional
    public Flow getFlowById(String username, boolean isAdmin, String id);

    /**
     * Query flow information based on pageId
     *
     * @param fid
     * @param pageId
     * @return
     */
    @Transactional
    public FlowVo getFlowByPageId(String fid, String pageId);

    /**
     * Query flow information based on id
     *
     * @param id
     * @return
     */
    @Transactional
    public String getFlowVoById(String id);

    /**
     * add flow(Contains drawing board information)
     *
     * @param flowVo
     * @return
     */
    @Transactional
    public String addFlow(String username, FlowVo flowVo);

    @Transactional
    public String updateFlow(String username, Flow flow);

    @Transactional
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
    public String runFlow(String username, boolean isAdmin, String flowId, String runMode);

    public String updateFlowBaseInfo(String username, String fId, FlowVo flowVo);

    public String updateFlowNameById(String username, String id, String flowGroupId, String flowName, String pageId);

    public Boolean updateFlowNameById(String username, String id, String flowName);

    public Integer getMaxFlowPageIdByFlowGroupId(String flowGroupId);

    public String drawingBoardData(String username, boolean isAdmin, String load, String parentAccessPath);

}
