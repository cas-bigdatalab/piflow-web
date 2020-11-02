package cn.cnic.component.stopsComponent.service.impl;

import cn.cnic.component.stopsComponent.mapper.StopsComponentMapper;
import cn.cnic.component.stopsComponent.model.StopsComponent;
import cn.cnic.component.stopsComponent.service.IStopsComponentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class StopsComponentServiceImpl implements IStopsComponentService {

    @Resource
    private StopsComponentMapper stopsComponentMapper;

    @Override
    public StopsComponent getStopsTemplateById(String id) {
        return stopsComponentMapper.getStopsComponentById(id);
    }

    @Override
    public StopsComponent getStopsPropertyById(String id) {
        return stopsComponentMapper.getStopsComponentAndPropertyById(id);
    }

}
