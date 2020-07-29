package cn.cnic.third.service;

import cn.cnic.third.vo.stop.ThirdStopsComponentVo;

import java.util.List;
import java.util.Map;

public interface IStop {
    /**
     * Call the group interface
     *
     * @return
     */
    public String[] getAllGroup();

    public String[] getAllStops();

    public Map<String, List<String>> getStopsListWithGroup();

    public ThirdStopsComponentVo getStopInfo(String bundleStr);

    public String getPluginPath();

}
