package cn.cnic.component.stop.service;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.stopsComponent.model.StopsComponent;
import cn.cnic.component.stopsComponent.service.IStopsComponentService;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class StopsComponentServiceTest extends ApplicationTests {

    @Autowired
    private IStopsComponentService stopsTemplateService;

    Logger logger = LoggerUtil.getLogger();

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
