package cn.cnic.component.flow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.cnic.component.flow.entity.FlowGlobalParams;
import cn.cnic.component.flow.mapper.provider.FlowGlobalParamsMapperProvider;

@Mapper
public interface FlowGlobalParamsMapper {

    @InsertProvider(type = FlowGlobalParamsMapperProvider.class, method = "addGlobalParams")
    public int addGlobalParams(FlowGlobalParams globalParams);

    @UpdateProvider(type = FlowGlobalParamsMapperProvider.class, method = "updateGlobalParams")
    public int updateGlobalParams(FlowGlobalParams globalParams);

    @UpdateProvider(type = FlowGlobalParamsMapperProvider.class, method = "updateEnableFlagById")
    public int updateEnableFlagById(@Param("username") String username, @Param("id") String id, @Param("enableFlag") boolean enableFlag);

    @SelectProvider(type = FlowGlobalParamsMapperProvider.class, method = "getGlobalParamsListParam")
    public List<FlowGlobalParams> getGlobalParamsListParam(@Param("username") String username, @Param("isAdmin") boolean isAdmin, @Param("param") String param);

    /**
     * Query FlowGlobalParams based on FlowGroup Id
     *
     * @param id
     * @return
     */
    @SelectProvider(type = FlowGlobalParamsMapperProvider.class, method = "getGlobalParamsById")
    public FlowGlobalParams getGlobalParamsById(@Param("username") String username, @Param("isAdmin") boolean isAdmin, @Param("id") String id);
    
    @SelectProvider(type = FlowGlobalParamsMapperProvider.class, method = "getFlowGlobalParamsByIds")
    public List<FlowGlobalParams> getFlowGlobalParamsByIds(@Param("ids") String[] ids);

}
