package com.nature.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.FetchType;

import com.nature.component.flow.model.Paths;
import com.nature.provider.PathsMapperProvider;

@Mapper
public interface PathsMapper {
    /**
     * 插入list<Paths> 注意拼sql的方法必须用map接 Param内容为键值
     *
     * @param pathsList
     * @return
     */
    @InsertProvider(type = PathsMapperProvider.class, method = "addPathsList")
    public int addPathsList(@Param("pathsList") List<Paths> pathsList);

    /**
     * 修改paths
     *
     * @param paths
     * @return
     */
    @UpdateProvider(type = PathsMapperProvider.class, method = "updatePaths")
    public int updatePaths(Paths paths);

    /**
     * 根据flowId查询
     *
     * @param flowId
     * @return
     */
    @SelectProvider(type = PathsMapperProvider.class, method = "getPathsListByFlowId")
    @Results({
            @Result(column = "LINE_FROM", property = "from"),
            @Result(column = "LINE_TO", property = "to"),
            @Result(column = "LINE_OUTPORT", property = "outport"),
            @Result(column = "LINE_INPORT", property = "inport")
    })
    public List<Paths> getPathsListByFlowId(String flowId);


    /**
     * 查询连线信息
     *
     * @param flowId 流水线Id
     * @param pageId 线的pageID
     * @param from   线的from
     * @param to     线的from
     * @return
     */
    @SelectProvider(type = PathsMapperProvider.class, method = "getPaths")
    @Results({

            @Result(column = "LINE_FROM", property = "from"),
            @Result(column = "LINE_TO", property = "to"),
            @Result(column = "LINE_OUTPORT", property = "outport"),
            @Result(column = "LINE_INPORT", property = "inport"),
            @Result(column = "LINE_PORT", property = "port"),
			@Result(column = "fk_flow_id", property = "flow", many = @Many(select = "com.nature.mapper.FlowMapper.getFlowById", fetchType = FetchType.LAZY))
    })
    public List<Paths> getPaths(String flowId, String pageId, String from, String to);

    /**
     * 查询连线的数量
     *
     * @param flowId 流水线Id
     * @param pageId 线的pageID
     * @param from   线的from
     * @param to     线的from
     * @return
     */
    @SelectProvider(type = PathsMapperProvider.class, method = "getPathsCounts")
    public Integer getPathsCounts(String flowId, String pageId, String from, String to);

    /**
     * 根据id查询paths
     *
     * @param id
     * @return
     */
    @SelectProvider(type = PathsMapperProvider.class, method = "getPathsById")
    public Paths getPathsById(String id);

    /**
     * 
     * 根据flowId逻辑删除flowInfo
     *
     * @param id
     * @return
     */
    @UpdateProvider(type = PathsMapperProvider.class, method = "updateEnableFlagByFlowId")
    public int updateEnableFlagByFlowId(String id);
}
