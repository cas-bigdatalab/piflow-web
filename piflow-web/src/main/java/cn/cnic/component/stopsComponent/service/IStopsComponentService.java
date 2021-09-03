package cn.cnic.component.stopsComponent.service;

import cn.cnic.component.stopsComponent.entity.StopsComponent;

public interface IStopsComponentService {

    public StopsComponent getStopsTemplateById(String id);

    public StopsComponent getStopsPropertyById(String id);
}
