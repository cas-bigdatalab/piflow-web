package cn.cnic.component.dashboard.mapper;

import cn.cnic.component.dashboard.mapper.provider.StatisticProvider;
import cn.cnic.component.stopsComponent.mapper.provider.StopsHubMapperProvider;
import cn.cnic.component.stopsComponent.model.StopsHub;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface StatisticMapper {

    /**
     * query all StopsHub
     *
     * @return
     */
    @SelectProvider(type = StatisticProvider.class, method = "getProcessStatisticInfo")
    public List<Map<String, String>>getProcessStatisticInfo();

    @SelectProvider(type = StatisticProvider.class, method = "getFlowCount")
    public int getFlowCount();


}
