package cn.cnic.component.stopsComponent.service.impl;

import cn.cnic.component.stopsComponent.mapper.StopsTemplateMapper;
import cn.cnic.component.stopsComponent.model.StopsTemplate;
import cn.cnic.component.stopsComponent.service.IStopsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StopsTemplateServiceImpl implements IStopsTemplateService {

    @Autowired
    private StopsTemplateMapper stopsTemplateMapper;

    @Override
    public StopsTemplate getStopsTemplateById(String id) {
        return stopsTemplateMapper.getStopsTemplateById(id);
    }

    @Override
    public StopsTemplate getStopsPropertyById(String id) {
        return stopsTemplateMapper.getStopsTemplateAndPropertyById(id);
    }

}
