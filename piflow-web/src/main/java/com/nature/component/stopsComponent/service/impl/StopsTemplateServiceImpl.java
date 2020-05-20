package com.nature.component.stopsComponent.service.impl;

import com.nature.component.stopsComponent.model.StopsTemplate;
import com.nature.component.stopsComponent.service.IStopsTemplateService;
import com.nature.mapper.stopsComponent.StopsTemplateMapper;
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
