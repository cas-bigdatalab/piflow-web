package cn.cnic.component.mxGraph.mapper;

import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.mxGraph.mapper.provider.MxGraphModelProvider;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

@Mapper
public interface MxGraphModelMapper {

    /**
     * add mxGraphModel
     *
     * @param mxGraphModel
     * @return
     */
    @InsertProvider(type = MxGraphModelProvider.class, method = "addMxGraphModel")
    public int addMxGraphModel(MxGraphModel mxGraphModel);

    /**
     * Modify mxgraphmodel
     *
     * @param mxGraphModel
     * @return
     */
    @UpdateProvider(type = MxGraphModelProvider.class, method = "updateMxGraphModel")
    public int updateMxGraphModel(MxGraphModel mxGraphModel);

    /**
     * Query mxgraphmodel according to ID
     *
     * @param id
     * @return
     */
    @SelectProvider(type = MxGraphModelProvider.class, method = "getMxGraphModelById")
    @Results({@Result(id = true, column = "id", property = "id"),
            @Result(column = "mx_dx", property = "dx"),
            @Result(column = "mx_dy", property = "dy"),
            @Result(column = "mx_grid", property = "grid"),
            @Result(column = "mx_gridsize", property = "gridSize"),
            @Result(column = "mx_guides", property = "guides"),
            @Result(column = "mx_tooltips", property = "tooltips"),
            @Result(column = "mx_connect", property = "connect"),
            @Result(column = "mx_arrows", property = "arrows"),
            @Result(column = "mx_fold", property = "fold"),
            @Result(column = "mx_page", property = "page"),
            @Result(column = "mx_pagescale", property = "pageScale"),
            @Result(column = "mx_pagewidth", property = "pageWidth"),
            @Result(column = "mx_pageheight", property = "pageHeight"),
            @Result(column = "mx_background", property = "background"),
            @Result(column = "id", property = "root", many = @Many(select = "cn.cnic.component.mxGraph.mapper.MxCellMapper.getMeCellByMxGraphId", fetchType = FetchType.LAZY))})
    public MxGraphModel getMxGraphModelById(String id);

    /**
     * Query mxGraphModel according to flowId
     *
     * @param flowId
     * @return
     */
    @SelectProvider(type = MxGraphModelProvider.class, method = "getMxGraphModelByFlowId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "mx_dx", property = "dx"),
            @Result(column = "mx_dy", property = "dy"),
            @Result(column = "mx_grid", property = "grid"),
            @Result(column = "mx_gridsize", property = "gridSize"),
            @Result(column = "mx_guides", property = "guides"),
            @Result(column = "mx_tooltips", property = "tooltips"),
            @Result(column = "mx_connect", property = "connect"),
            @Result(column = "mx_arrows", property = "arrows"),
            @Result(column = "mx_fold", property = "fold"),
            @Result(column = "mx_page", property = "page"),
            @Result(column = "mx_pagescale", property = "pageScale"),
            @Result(column = "mx_pagewidth", property = "pageWidth"),
            @Result(column = "mx_pageheight", property = "pageHeight"),
            @Result(column = "mx_background", property = "background"),
            @Result(column = "id", property = "root", many = @Many(select = "cn.cnic.component.mxGraph.mapper.MxCellMapper.getMeCellByMxGraphId", fetchType = FetchType.LAZY))
    })
    public MxGraphModel getMxGraphModelByFlowId(String flowId);

    /**
     * Query mxGraphModel according to flowGroupId
     *
     * @param flowGroupId
     * @return
     */
    @SelectProvider(type = MxGraphModelProvider.class, method = "getMxGraphModelByFlowGroupId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "mx_dx", property = "dx"),
            @Result(column = "mx_dy", property = "dy"),
            @Result(column = "mx_grid", property = "grid"),
            @Result(column = "mx_gridsize", property = "gridSize"),
            @Result(column = "mx_guides", property = "guides"),
            @Result(column = "mx_tooltips", property = "tooltips"),
            @Result(column = "mx_connect", property = "connect"),
            @Result(column = "mx_arrows", property = "arrows"),
            @Result(column = "mx_fold", property = "fold"),
            @Result(column = "mx_page", property = "page"),
            @Result(column = "mx_pagescale", property = "pageScale"),
            @Result(column = "mx_pagewidth", property = "pageWidth"),
            @Result(column = "mx_pageheight", property = "pageHeight"),
            @Result(column = "mx_background", property = "background"),
            @Result(column = "id", property = "root", many = @Many(select = "cn.cnic.component.mxGraph.mapper.MxCellMapper.getMeCellByMxGraphId", fetchType = FetchType.LAZY))
    })
    public MxGraphModel getMxGraphModelByFlowGroupId(String flowGroupId);
    /**
     * Query mxGraphModel according to flowGroupId
     *
     * @param processId
     * @return
     */
    @SelectProvider(type = MxGraphModelProvider.class, method = "getMxGraphModelByProcessId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "mx_dx", property = "dx"),
            @Result(column = "mx_dy", property = "dy"),
            @Result(column = "mx_grid", property = "grid"),
            @Result(column = "mx_gridsize", property = "gridSize"),
            @Result(column = "mx_guides", property = "guides"),
            @Result(column = "mx_tooltips", property = "tooltips"),
            @Result(column = "mx_connect", property = "connect"),
            @Result(column = "mx_arrows", property = "arrows"),
            @Result(column = "mx_fold", property = "fold"),
            @Result(column = "mx_page", property = "page"),
            @Result(column = "mx_pagescale", property = "pageScale"),
            @Result(column = "mx_pagewidth", property = "pageWidth"),
            @Result(column = "mx_pageheight", property = "pageHeight"),
            @Result(column = "mx_background", property = "background"),
            @Result(column = "id", property = "root", many = @Many(select = "cn.cnic.component.mxGraph.mapper.MxCellMapper.getMeCellByMxGraphId", fetchType = FetchType.LAZY))
    })
    public MxGraphModel getMxGraphModelByProcessId(String processId);

    /**
     * Query mxGraphModel according to processGroupId
     *
     * @param processGroupId
     * @return
     */
    @SelectProvider(type = MxGraphModelProvider.class, method = "getMxGraphModelByProcessGroupId")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "mx_dx", property = "dx"),
            @Result(column = "mx_dy", property = "dy"),
            @Result(column = "mx_grid", property = "grid"),
            @Result(column = "mx_gridsize", property = "gridSize"),
            @Result(column = "mx_guides", property = "guides"),
            @Result(column = "mx_tooltips", property = "tooltips"),
            @Result(column = "mx_connect", property = "connect"),
            @Result(column = "mx_arrows", property = "arrows"),
            @Result(column = "mx_fold", property = "fold"),
            @Result(column = "mx_page", property = "page"),
            @Result(column = "mx_pagescale", property = "pageScale"),
            @Result(column = "mx_pagewidth", property = "pageWidth"),
            @Result(column = "mx_pageheight", property = "pageHeight"),
            @Result(column = "mx_background", property = "background"),
            @Result(column = "id", property = "root", many = @Many(select = "cn.cnic.component.mxGraph.mapper.MxCellMapper.getMeCellByMxGraphId", fetchType = FetchType.LAZY))
    })
    public MxGraphModel getMxGraphModelByProcessGroupId(String processGroupId);


    @UpdateProvider(type = MxGraphModelProvider.class, method = "updateEnableFlagByFlowId")
    public int updateEnableFlagByFlowId(@Param("username") String username,@Param("flowId") String flowId);

}
