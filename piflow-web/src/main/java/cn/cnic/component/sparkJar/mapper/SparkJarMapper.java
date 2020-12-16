package cn.cnic.component.sparkJar.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.cnic.component.sparkJar.mapper.provider.SparkJarMapperProvider;
import cn.cnic.component.sparkJar.model.SparkJarComponent;

@Mapper
public interface SparkJarMapper {
    /**
     * add SparkJarComponent
     *
     * @param sparkJarComponent
     * @return
     */
    @InsertProvider(type = SparkJarMapperProvider.class, method = "addSparkJarComponent")
    public int addSparkJarComponent(SparkJarComponent sparkJarComponent);

    /**
     * update SparkJarComponent
     *
     * @param sparkJarComponent
     * @return
     */
    @UpdateProvider(type = SparkJarMapperProvider.class, method = "updateSparkJarComponent")
    public int updateSparkJarComponent(SparkJarComponent sparkJarComponent);

    /**
     * query all SparkJarComponent
     *
     * @return
     */
    @SelectProvider(type = SparkJarMapperProvider.class, method = "getSparkJarList")
    public List<SparkJarComponent> getSparkJarList(@Param("username") String username, @Param("isAdmin") boolean isAdmin);

    @SelectProvider(type = SparkJarMapperProvider.class, method = "getSparkJarListByName")
    public List<SparkJarComponent> getSparkJarListByName(@Param("username") String username, @Param("isAdmin") boolean isAdmin, String jarName);

    @SelectProvider(type = SparkJarMapperProvider.class, method = "getSparkJarById")
    public SparkJarComponent getSparkJarById(@Param("username") String username, @Param("isAdmin") boolean isAdmin, String id);


    @UpdateProvider(type = SparkJarMapperProvider.class, method = "updateEnableFlagById")
    public int deleteSparkJarById(@Param("username") String username, @Param("id") String id);

    @SelectProvider(type = SparkJarMapperProvider.class, method = "getSparkJarListParam")
    public List<SparkJarComponent> getSparkJarListParam(@Param("username") String username, @Param("isAdmin") boolean isAdmin, @Param("param") String param);

}
