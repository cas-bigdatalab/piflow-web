package cn.cnic.component.stopsComponent.service;

import cn.cnic.component.stopsComponent.model.StopsComponent;

public interface IStopsComponentService {

    public StopsComponent getStopsTemplateById(String id);

    public StopsComponent getStopsPropertyById(String id);
}
