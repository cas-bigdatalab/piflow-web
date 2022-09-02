package cn.cnic.component.stopsComponent.domain;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.component.stopsComponent.entity.StopsComponent;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


public class StopsComponentDomainTest extends ApplicationTests {

    private Logger logger = LoggerUtil.getLogger();

    private final StopsComponentDomain stopsComponentDomain;

    @Autowired
    public StopsComponentDomainTest(StopsComponentDomain stopsComponentDomain) {
        this.stopsComponentDomain = stopsComponentDomain;
    }

    @Test
    public void testDeleteStopsComponent() {
        StopsComponent stopsComponent = stopsComponentDomain.getStopsComponentByBundle("cn.piflow.bundle.visualization.CsvSaveCustomView");
        int i = stopsComponentDomain.deleteStopsComponent(stopsComponent);
        logger.info(i + "");
    }

}
