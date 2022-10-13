package cn.cnic.component.flow.service;

import java.util.List;

public interface IFlowStopsPublishingService {

    /**
     *
     *
     * @param username
     * @param name
     * @param stopsIds
     * @return
     */
    public String addFlowStopsPublishing(String username, String name, String stopsIds);

    /**
     * update FlowStopsPublishing
     *
     * @param isAdmin
     * @param username
     * @param publishingId
     * @param name
     * @param stopsIds
     * @return
     */
    public String updateFlowStopsPublishing(boolean isAdmin, String username, String publishingId, String name, String stopsIds);

    /**
     * getStopByNameAndFlowId
     * 
     * @param publishingId
     * @return
     */
    public String getFlowStopsPublishingVo(String publishingId);

    /**
     * getFlowStopsPublishingListByFlowId
     *
     * @param username
     * @param flowId
     * @return
     */
    public String getFlowStopsPublishingListByFlowId(String username, String flowId);

    /**
     * getFlowStopsPublishingList
     *
     * @param username
     * @return
     */
    public String getFlowStopsPublishingListPager(String username, boolean isAdmin, Integer offset, Integer limit, String param);

    /**
     * deleteFlowStopsPublishing
     *
     * @param username
     * @param publishingId
     * @return
     */
    public String deleteFlowStopsPublishing(String username, String publishingId);

}
