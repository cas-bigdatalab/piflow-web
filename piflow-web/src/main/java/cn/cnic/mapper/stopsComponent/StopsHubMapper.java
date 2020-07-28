package cn.cnic.mapper.stopsComponent;

import cn.cnic.component.dataSource.model.DataSource;
import cn.cnic.component.dataSource.vo.DataSourceVo;
import cn.cnic.component.stopsComponent.model.StopsHub;
import cn.cnic.provider.dataSource.DataSourceMapperProvider;
import cn.cnic.provider.stopsComponent.StopsHubMapperProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StopsHubMapper {
    /**
     * add StopsHub
     *
     * @param stopsHub
     * @return
     */
    //@Insert({"insert into stops_hub(id,crt_dttm,crt_user,enable_flag,last_update_dttm,last_update_user,version,jar_name,jar_url,status) values (#{id},#{crtDttm},#{crtUser},#{enableFlag},#{lastUpdateDttm},#{lastUpdateUser},#{version}, #{jarName}, #{jarUrl},#{status})"})
    @InsertProvider(type = StopsHubMapperProvider.class, method = "addStopsHub")
    public int addStopHub(StopsHub stopsHub);

    /**
     * update StopsHub
     *
     * @param stopsHub
     * @return
     */
    @UpdateProvider(type = StopsHubMapperProvider.class, method = "updateStopsHub")
    public int updateStopHub(StopsHub stopsHub);

    /**
     * query all StopsHub
     *
     * @return
     */
    @SelectProvider(type = StopsHubMapperProvider.class, method = "getStopsHubList")
    public List<StopsHub> getStopsHubList(@Param("username") String username, @Param("isAdmin") boolean isAdmin);

    @SelectProvider(type = StopsHubMapperProvider.class, method = "getStopsHubListByName")
    public List<StopsHub> getStopsHubListByName(@Param("username") String username, @Param("isAdmin") boolean isAdmin, String jarName);

    @SelectProvider(type = StopsHubMapperProvider.class, method = "getStopsHubById")
    public StopsHub getStopsHubListById(@Param("username") String username, @Param("isAdmin") boolean isAdmin, String id);


    @UpdateProvider(type = StopsHubMapperProvider.class, method = "updateEnableFlagById")
    public int deleteStopsHubById(@Param("username") String username, @Param("id") String id);
}
