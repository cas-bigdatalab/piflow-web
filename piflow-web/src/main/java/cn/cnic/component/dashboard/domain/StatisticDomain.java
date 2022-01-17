package cn.cnic.component.dashboard.domain;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.component.dashboard.mapper.StatisticMapper;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class StatisticDomain {

    private final StatisticMapper statisticMapper;

    @Autowired
    public StatisticDomain(StatisticMapper statisticMapper) {
        this.statisticMapper = statisticMapper;
    }

    /**
     * query flow progress statistic info
     *
     * @return statistic info map
     */
    public List<Map<String, String>> getFlowProcessStatisticInfo() {
        return statisticMapper.getFlowProcessStatisticInfo();
    }


    /**
     * query flow count
     *
     * @return flowCount
     */
    public int getFlowCount() {
        return statisticMapper.getFlowCount();
    }

    /**
     * query group progress statistic info
     *
     * @return statistic info map
     */
    public List<Map<String, String>> getGroupProcessStatisticInfo() {
        return statisticMapper.getGroupProcessStatisticInfo();
    }


    /**
     * query group count
     *
     * @return groupCount
     */
    public int getGroupCount() {
        return statisticMapper.getGroupCount();
    }


    /**
     * query schedule statistic info
     *
     * @return statistic info map
     */
    public List<Map<String, String>> getScheduleStatisticInfo() {
        return statisticMapper.getScheduleStatisticInfo();
    }

    /**
     * query template count
     *
     * @return templateCount
     */
    public int getTemplateCount() {
        return statisticMapper.getTemplateCount();
    }

    /**
     * query datasource count
     *
     * @return datasourceCount
     */
    public int getDataSourceCount() {
        return statisticMapper.getDataSourceCount();
    }


    /**
     * query stops hub count
     *
     * @return stopsHubCount
     */
    public int getStopsHubCount() {
        return statisticMapper.getStopsHubCount();
    }

    /**
     * query stops count
     *
     * @return stopsCount
     */
    public int getStopsCount() {
        return statisticMapper.getStopsCount();
    }

    /**
     * query stops group count
     *
     * @return stopsGroupCount
     */
    public int getStopsGroupCount() {
        return statisticMapper.getStopsGroupCount();
    }

}
