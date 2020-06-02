package cn.cnic.component.stopsComponent.service;

import cn.cnic.component.stopsComponent.model.StopsTemplate;

public interface IStopsTemplateService {

    public StopsTemplate getStopsTemplateById(String id);

    public StopsTemplate getStopsPropertyById(String id);
}
