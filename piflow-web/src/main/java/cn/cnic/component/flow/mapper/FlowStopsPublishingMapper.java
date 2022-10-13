package cn.cnic.component.flow.mapper;

import cn.cnic.component.flow.entity.FlowStopsPublishing;
import cn.cnic.component.flow.mapper.provider.FlowStopsPublishingMapperProvider;
import cn.cnic.component.flow.vo.FlowStopsPublishingVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * Flow Stops Publishing table
 */
@Mapper
public interface FlowStopsPublishingMapper {

    /**
     * Add FlowStopsPublishing
     *
     * @param flowStopsPublishing
     * @return
     */
    @InsertProvider(type = FlowStopsPublishingMapperProvider.class, method = "addFlowStopsPublishing")
    public int addFlowStopsPublishing(FlowStopsPublishing flowStopsPublishing);

    /**
     * update FlowStopsPublishing
     *
     * @param username
     * @param publishingId
     * @param name
     * @return
     */
    @UpdateProvider(type = FlowStopsPublishingMapperProvider.class, method = "updateFlowStopsPublishingName")
    public int updateFlowStopsPublishingName(String username, String publishingId, String name);

    /**
     * update EnableFlag By Id
     *
     * @param username
     * @param publishingId
     * @return
     */
    @UpdateProvider(type = FlowStopsPublishingMapperProvider.class, method = "updateFlowStopsPublishingEnableFlagByPublishingIdAndStopId")
    public int updateFlowStopsPublishingEnableFlagByPublishingIdAndStopId(String username, String publishingId, String stopsId);

    /**
     * update EnableFlag By Id
     *
     * @param username
     * @param publishingId
     * @return
     */
    @UpdateProvider(type = FlowStopsPublishingMapperProvider.class, method = "updateFlowStopsPublishingEnableFlagByPublishingId")
    public int updateFlowStopsPublishingEnableFlagByPublishingId(String username, String publishingId);

    /**
     * Get FlowStopsPublishing List
     *
     * @return
     */
    @SelectProvider(type = FlowStopsPublishingMapperProvider.class, method = "getFlowStopsPublishingList")
    public List<FlowStopsPublishing> getFlowStopsPublishingList(String username, boolean isAdmin, String param);

    /**
     * Get FlowStopsPublishing List By id
     *
     * @param publishingId
     * @return
     */
    @SelectProvider(type = FlowStopsPublishingMapperProvider.class, method = "getFlowStopsPublishingByPublishingId")
    @Results({
            @Result(column = "stops_id", property = "stopsVo", one = @One(select = "cn.cnic.component.flow.mapper.StopsMapper.getStopsVoById", fetchType = FetchType.LAZY))
    })
    public List<FlowStopsPublishingVo> getFlowStopsPublishingVoByPublishingId(String publishingId);


    /**
     * Get FlowStopsPublishing List By id
     *
     * @param publishingId
     * @return
     */
    @SelectProvider(type = FlowStopsPublishingMapperProvider.class, method = "getPublishingStopsIdsByPublishingId")
    public List<String> getPublishingStopsIdsByPublishingId(String publishingId);

    /**
     * Get FlowStopsPublishing List By id
     *
     * @param publishingId
     * @return
     */
    @SelectProvider(type = FlowStopsPublishingMapperProvider.class, method = "getFlowStopsPublishingByPublishingIdAndCreateUser")
    public List<String> getFlowStopsPublishingByPublishingIdAndCreateUser(String username, String publishingId);

    /**
     * Get FlowStopsPublishing List
     *
     * @return
     */
    @SelectProvider(type = FlowStopsPublishingMapperProvider.class, method = "getFlowStopsPublishingListByPublishingIdAndStopsId")
    public List<FlowStopsPublishing> getFlowStopsPublishingListByPublishingIdAndStopsId(String publishingId, String stopsId);

    /**
     * Get FlowStopsPublishing List by flowId
     *
     * @return
     */
    @SelectProvider(type = FlowStopsPublishingMapperProvider.class, method = "getFlowStopsPublishingListByFlowId")
    public List<FlowStopsPublishing> getFlowStopsPublishingListByFlowId(String username, String flowId);


    /**
     * Get FlowStopsPublishing List by flowId
     *
     * @return
     */
    @Select("SELECT DISTINCT fs.fk_flow_id FROM flow_stops_publishing fsp LEFT JOIN flow_stops fs ON fsp.stops_id=fs.id WHERE fsp.enable_flag=1 AND fs.enable_flag=1 AND fsp.publishing_id=#{publishingId}")
    public List<String> getFlowIdByPublishingId(String publishingId);




}
