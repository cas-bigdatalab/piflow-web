package cn.cnic.third.service;

import cn.cnic.component.stopsComponent.model.StopsTemplate;
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

    public StopsTemplate getStopInfo(String bundle);

    public Map<String, List<String>> getStopsListWithGroup();

    public ThirdStopsComponentVo getStopInfoNew(String bundleStr);

}
