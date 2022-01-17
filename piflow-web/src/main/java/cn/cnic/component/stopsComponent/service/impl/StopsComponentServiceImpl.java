package cn.cnic.component.stopsComponent.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cnic.component.stopsComponent.domain.StopsComponentDomain;
import cn.cnic.component.stopsComponent.entity.StopsComponent;
import cn.cnic.component.stopsComponent.service.IStopsComponentService;

@Service
public class StopsComponentServiceImpl implements IStopsComponentService {

    private final StopsComponentDomain stopsComponentDomain;

    @Autowired
    public StopsComponentServiceImpl(StopsComponentDomain stopsComponentDomain) {
        this.stopsComponentDomain = stopsComponentDomain;
    }

    @Override
    public StopsComponent getStopsTemplateById(String id) {
        return stopsComponentDomain.getStopsComponentById(id);
    }

    @Override
    public StopsComponent getStopsPropertyById(String id) {
        return stopsComponentDomain.getStopsComponentAndPropertyById(id);
    }

}
