package cn.cnic.component.stop.service;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.component.stopsComponent.entity.StopsComponent;
import cn.cnic.component.stopsComponent.service.IStopsComponentService;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class StopsComponentServiceTest extends ApplicationTests {

    private Logger logger = LoggerUtil.getLogger();

    @Autowired
    private IStopsComponentService stopsTemplateService;

    @Test
    public void testGetStopsTemplateById() {
        StopsComponent stopsComponent = stopsTemplateService.getStopsTemplateById("fbb42f0d8ca14a83bfab13e0ba2d7293");
        if (null == stopsComponent) {
            logger.info("The query result is empty");
            stopsComponent = new StopsComponent();
        }
        logger.info(stopsComponent.toString());
    }

    @Test
    public void testGetStopsPropertyById() {
        StopsComponent stopsComponent = stopsTemplateService.getStopsPropertyById("fbb42f0d8ca14a83bfab13e0ba2d7293");
        if (null == stopsComponent) {
            logger.info("The query result is empty");
            stopsComponent = new StopsComponent();
        }
        logger.info(stopsComponent.toString());
    }

}
