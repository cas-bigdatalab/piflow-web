package cn.cnic.component.mxGraph.mapper;

import cn.cnic.component.mxGraph.entity.MxCell;
import cn.cnic.component.mxGraph.mapper.provider.MxCellMapperProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface MxCellMapper {

    /**
     * add mxCell
     *
     * @param mxCell
     * @return
     */
    @InsertProvider(type = MxCellMapperProvider.class, method = "addMxCell")
    public int addMxCell(MxCell mxCell);

    /**
     * update mxCell
     *
     * @param mxCell
     * @return
     */
    @UpdateProvider(type = MxCellMapperProvider.class, method = "updateMxCell")
    public int updateMxCell(MxCell mxCell);

    /**
     * Query MxCell's list based on mxGraphId
     *
     * @param mxGraphId
     * @return
     */
    @SelectProvider(type = MxCellMapperProvider.class, method = "getMeCellByMxGraphId")
    @Results({
            @Result(column = "ID", property = "id"),
            @Result(column = "MX_PAGEID", property = "pageId"),
            @Result(column = "MX_PARENT", property = "parent"),
            @Result(column = "MX_STYLE", property = "style"),
            @Result(column = "MX_EDGE", property = "edge"),
            @Result(column = "MX_SOURCE", property = "source"),
            @Result(column = "MX_TARGET", property = "target"),
            @Result(column = "MX_VALUE", property = "value"),
            @Result(column = "MX_VERTEX", property = "vertex"),
            @Result(column = "id", property = "mxGeometry", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGeometryMapper.getMxGeometryByMxCellId", fetchType = FetchType.LAZY))
    })
    public List<MxCell> getMeCellByMxGraphId(String mxGraphId);

    /**
     * Query MxCell based on Id
     *
     * @param id
     * @return
     */
    @SelectProvider(type = MxCellMapperProvider.class, method = "getMeCellById")
    @Results({
            @Result(column = "ID", property = "id"),
            @Result(column = "MX_PAGEID", property = "pageId"),
            @Result(column = "MX_PARENT", property = "parent"),
            @Result(column = "MX_STYLE", property = "style"),
            @Result(column = "MX_EDGE", property = "edge"),
            @Result(column = "MX_SOURCE", property = "source"),
            @Result(column = "MX_TARGET", property = "target"),
            @Result(column = "MX_VALUE", property = "value"),
            @Result(column = "MX_VERTEX", property = "vertex"),
            @Result(column = "id", property = "mxGeometry", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGeometryMapper.getMxGeometryByMxCellId", fetchType = FetchType.LAZY))
    })
    public MxCell getMeCellById(String id);

    /**
     * Query MxCell based on mxGraphId and pageId
     *
     * @param mxGraphId
     * @param pageId
     * @return
     */
    @SelectProvider(type = MxCellMapperProvider.class, method = "getMxCellByMxGraphIdAndPageId")
    @Results({
            @Result(column = "ID", property = "id"),
            @Result(column = "MX_PAGEID", property = "pageId"),
            @Result(column = "MX_PARENT", property = "parent"),
            @Result(column = "MX_STYLE", property = "style"),
            @Result(column = "MX_EDGE", property = "edge"),
            @Result(column = "MX_SOURCE", property = "source"),
            @Result(column = "MX_TARGET", property = "target"),
            @Result(column = "MX_VALUE", property = "value"),
            @Result(column = "MX_VERTEX", property = "vertex"),
            @Result(column = "id", property = "mxGeometry", one = @One(select = "cn.cnic.component.mxGraph.mapper.MxGeometryMapper.getMxGeometryByMxCellId", fetchType = FetchType.LAZY))
    })
    public MxCell getMxCellByMxGraphIdAndPageId(@Param("mxGraphId") String mxGraphId, @Param("pageId") String pageId);


    /**
     * Delete according to id logic, set to invalid
     *
     * @param username
     * @param id
     * @return
     */
    @UpdateProvider(type = MxCellMapperProvider.class, method = "updateEnableFlagById")
    public int updateMxCellEnableFlagById(String username, String id);

    /**
     * Delete 'MxCell' by 'mxGraphModelId'
     *
     * @param username
     * @param mxGraphModelId
     * @return
     */
    @UpdateProvider(type = MxCellMapperProvider.class, method = "deleteMxCellByFlowId")
    public int deleteMxCellByFlowId(String username, String mxGraphModelId);

    /**
     * query max pageId by mxGraphModelId
     *
     * @param mxGraphModelId
     * @return
     */
    @Select("select MAX(s.mx_pageid+0) from mx_cell s where s.enable_flag=1 and s.fk_mx_graph_id=#{mxGraphModelId}")
    public Integer getMaxPageIdByMxGraphModelId(String mxGraphModelId);

}
