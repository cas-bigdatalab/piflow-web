package com.nature.mapper.mxGraph;

import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.provider.mxGraph.MxGeometryMapperProvider;

import org.apache.ibatis.annotations.*;

@Mapper
public interface MxGeometryMapper {

    /**
     * 新增MxGeometry
     *
     * @param mxGeometry
     * @return
     */
    @InsertProvider(type = MxGeometryMapperProvider.class, method = "addMxGeometry")
    public int addMxGeometry(MxGeometry mxGeometry);

    /**
     * 修改MxGeometry
     *
     * @param mxGeometry
     * @return
     */
    @UpdateProvider(type = MxGeometryMapperProvider.class, method = "updateMxGeometry")
    public int updateMxGeometry(MxGeometry mxGeometry);

    /**
     * 根据id查询MxGeometry
     *
     * @param id
     * @return
     */
    @SelectProvider(type = MxGeometryMapperProvider.class, method = "getMxGeometryById")
    @Results({
            @Result(column = "MX_RELATIVE", property = "relative"),
            @Result(column = "MX_AS", property = "as"),
            @Result(column = "MX_X", property = "x"),
            @Result(column = "MX_Y", property = "y"),
            @Result(column = "MX_WIDTH", property = "width"),
            @Result(column = "MX_HEIGHT", property = "height")
    })
    public MxGeometry getMxGeometryById(String id);

    /**
     * 根据flowId查询MxGeometry
     *
     * @param flowId
     * @return
     */
    @SelectProvider(type = MxGeometryMapperProvider.class, method = "getMxGeometryByFlowId")
    @Results({
            @Result(column = "MX_RELATIVE", property = "relative"),
            @Result(column = "MX_AS", property = "as"),
            @Result(column = "MX_X", property = "x"),
            @Result(column = "MX_Y", property = "y"),
            @Result(column = "MX_WIDTH", property = "width"),
            @Result(column = "MX_HEIGHT", property = "height")
    })
    public MxGeometry getMxGeometryByFlowId(String flowId);


    @UpdateProvider(type = MxGeometryMapperProvider.class, method = "updateEnableFlagById")
    public int updateEnableFlagById(String id);

}
