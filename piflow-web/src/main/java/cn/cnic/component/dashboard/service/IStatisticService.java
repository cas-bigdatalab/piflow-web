package cn.cnic.component.dashboard.service;

import java.util.Map;

public interface IStatisticService {

    /**
     * get flow statistic info
     *
     * @return statisticInfo
     */
    public Map<String,String> getFlowStatisticInfo();

    /**
     * get group statistic info
     *
     * @return statisticInfo
     */
    public Map<String,String> getGroupStatisticInfo();
}
