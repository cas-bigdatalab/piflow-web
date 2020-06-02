package cn.cnic.component.flow.service;

import cn.cnic.component.flow.vo.StopsCustomizedPropertyVo;


public interface ICustomizedPropertyService {

    public String addStopCustomizedProperty(StopsCustomizedPropertyVo stopsCustomizedPropertyVo);

    public String updateStopsCustomizedProperty(StopsCustomizedPropertyVo stopsCustomizedPropertyVo);

    public String deleteStopsCustomizedProperty(String customPropertyId);

    public String deleteRouterStopsCustomizedProperty(String customPropertyId);

    public String getRouterStopsCustomizedProperty(String customPropertyId);

}
