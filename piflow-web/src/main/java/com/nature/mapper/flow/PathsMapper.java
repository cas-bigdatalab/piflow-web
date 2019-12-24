package com.nature.mapper.flow;

import com.nature.component.flow.model.Paths;
import com.nature.provider.flow.PathsMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface PathsMapper {
    /**
     * Insert "list<Paths>" Note that the method of spelling SQL must use "map" to connect the "Param" content to the key value.
     *
     * @param pathsList
     * @return
     */
    @InsertProvider(type = PathsMapperProvider.class, method = "addPathsList")
    public int addPathsList(@Param("pathsList") List<Paths> pathsList);

    /**
     * update paths
     *
     * @param paths
     * @return
     */
    @UpdateProvider(type = PathsMapperProvider.class, method = "updatePaths")
    public int updatePaths(Paths paths);

    /**
     * Query according to "flowId"
     *
     * @param flowId
     * @return
     */
    @SelectProvider(type = PathsMapperProvider.class, method = "getPathsListByFlowId")
    @Results({
            @Result(column = "line_from", property = "from"),
            @Result(column = "line_to", property = "to"),
            @Result(column = "line_outport", property = "outport"),
            @Result(column = "line_inport", property = "inport")
    })
    public List<Paths> getPathsListByFlowId(String flowId);


    /**
     * Query connection information
     *
     * @param flowId flow Id
     * @param pageId path pageID
     * @param from   path from
     * @param to     path to
     * @return
     */
    @SelectProvider(type = PathsMapperProvider.class, method = "getPaths")
    @Results({

            @Result(column = "line_from", property = "from"),
            @Result(column = "line_to", property = "to"),
            @Result(column = "line_outport", property = "outport"),
            @Result(column = "line_inport", property = "inport"),
            @Result(column = "line_port", property = "port"),
            @Result(column = "fk_flow_id", property = "flow", many = @Many(select = "com.nature.mapper.flow.FlowMapper.getFlowById", fetchType = FetchType.LAZY))
    })
    public List<Paths> getPaths(String flowId, String pageId, String from, String to);

    /**
     * Query connection information
     *
     * @param flowId flow Id
     * @param pageId path pageID
     * @param from   path from
     * @param to     path to
     * @return
     */
    @SelectProvider(type = PathsMapperProvider.class, method = "getPaths")
    @Results({

            @Result(column = "line_from", property = "from"),
            @Result(column = "line_to", property = "to"),
            @Result(column = "line_outport", property = "outport"),
            @Result(column = "line_inport", property = "inport"),
            @Result(column = "line_port", property = "port"),
            @Result(column = "fk_flow_id", property = "flow", many = @Many(select = "com.nature.mapper.flow.FlowMapper.getFlowById", fetchType = FetchType.LAZY))
    })
    public List<Paths> getPathsByFlowIdAndStopPageId(String flowId, String pageId, String from, String to);


    /**
     * Query the number of connections
     *
     * @param flowId flow Id
     * @param pageId path pageID
     * @param from   path from
     * @param to     path to
     * @return
     */
    @SelectProvider(type = PathsMapperProvider.class, method = "getPathsCounts")
    public Integer getPathsCounts(String flowId, String pageId, String from, String to);

    /**
     * Query paths by id
     *
     * @param id
     * @return
     */
    @SelectProvider(type = PathsMapperProvider.class, method = "getPathsById")
    public Paths getPathsById(String id);

    /**
     * Logically delete flowInfo according to flowId
     *
     * @param id
     * @return
     */
    @UpdateProvider(type = PathsMapperProvider.class, method = "updateEnableFlagByFlowId")
    public int updateEnableFlagByFlowId(String id);
}
