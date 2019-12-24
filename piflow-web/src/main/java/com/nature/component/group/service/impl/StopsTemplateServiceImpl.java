package com.nature.component.group.service.impl;

import com.nature.component.group.model.StopsTemplate;
import com.nature.component.group.service.IStopsTemplateService;
import com.nature.mapper.flow.StopsTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
