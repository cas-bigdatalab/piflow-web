package cn.cnic.component.flow.service;

import cn.cnic.component.flow.entity.FlowStopsPublishing;
import cn.cnic.component.flow.vo.FlowStopsPublishingVo;
import cn.cnic.component.flow.vo.StopsCustomizedPropertyVo;
import cn.cnic.controller.requestVo.RunStopsVo;

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
    public String updateFlowStopsPublishing(boolean isAdmin, String username, String publishingId, String name, List<String> stopsIds);

    /**
     * getStopByNameAndFlowId
     * 
     * @param publishingId
     * @return
     */
    public String getFlowStopsPublishingVo(String publishingId);

}
