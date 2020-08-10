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
     * query flow progress statistic info
     *
     * @return statistic info map
     */
    @SelectProvider(type = StatisticProvider.class, method = "getFlowProcessStatisticInfo")
    public List<Map<String, String>>getFlowProcessStatisticInfo();


    /**
     * query flow count
     *
     * @return flowCount
     */
    @SelectProvider(type = StatisticProvider.class, method = "getFlowCount")
    public int getFlowCount();

    /**
     * query group progress statistic info
     *
     * @return statistic info map
     */
    @SelectProvider(type = StatisticProvider.class, method = "getGroupProcessStatisticInfo")
    public List<Map<String, String>>getGroupProcessStatisticInfo();


    /**
     * query group count
     *
     * @return groupCount
     */
    @SelectProvider(type = StatisticProvider.class, method = "getGroupCount")
    public int getGroupCount();


    /**
     * query template count
     *
     * @return templateCount
     */
    @SelectProvider(type = StatisticProvider.class, method = "getTemplateCount")
    public int getTemplateCount();

    /**
     * query datasource count
     *
     * @return datasourceCount
     */
    @SelectProvider(type = StatisticProvider.class, method = "getDataSourceCount")
    public int getDataSourceCount();


    /**
     * query stops hub count
     *
     * @return stopsHubCount
     */
    @SelectProvider(type = StatisticProvider.class, method = "getStopsHubCount")
    public int getStopsHubCount();

    /**
     * query stops count
     *
     * @return stopsCount
     */
    @SelectProvider(type = StatisticProvider.class, method = "getStopsCount")
    public int getStopsCount();

    /**
     * query stops group count
     *
     * @return stopsGroupCount
     */
    @SelectProvider(type = StatisticProvider.class, method = "getStopsGroupCount")
    public int getStopsGroupCount();

}
