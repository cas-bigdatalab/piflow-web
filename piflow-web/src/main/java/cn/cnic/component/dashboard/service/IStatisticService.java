package cn.cnic.component.dashboard.service;

import java.util.Map;

public interface IStatisticService {

    /**
     * get statistic info
     *
     * @return statisticInfo
     */
    public Map<String,String> getFlowStatisticInfo();
}
