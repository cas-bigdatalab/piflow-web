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
     * @param flowStopsPublishing
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
    @Results({
            @Result(column = "stops_id", property = "stops", one = @One(select = "cn.cnic.component.flow.mapper.StopsMapper.getStopsById", fetchType = FetchType.LAZY))
    })
    public List<FlowStopsPublishingVo> getFlowStopsPublishingList();

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
     * Get FlowStopsPublishing List
     *
     * @return
     */
    @SelectProvider(type = FlowStopsPublishingMapperProvider.class, method = "getFlowStopsPublishingListByPublishingIdAndStopsId")
    public List<FlowStopsPublishing> getFlowStopsPublishingListByPublishingIdAndStopsId(String publishingId, String stopsId);




}
