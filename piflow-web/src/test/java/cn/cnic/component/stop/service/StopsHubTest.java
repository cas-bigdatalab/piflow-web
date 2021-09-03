package cn.cnic.component.stop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import cn.cnic.ApplicationTests;
import cn.cnic.component.stopsComponent.service.IStopsHubService;

public class StopsHubTest extends ApplicationTests {

    @Autowired
    private IStopsHubService stopsHubServiceImpl;


    @Test
    @Rollback(false)
    public void testMountStopsHub() {
        String result = stopsHubServiceImpl.mountStopsHub("Nature", true, "222");
        System.out.println(result);

    }

    @Test
    @Rollback(false)
    public void testUnMountStopsHub() {
        String result = stopsHubServiceImpl.unmountStopsHub("Nature", true, "222");
        System.out.println(result);
    }

}
