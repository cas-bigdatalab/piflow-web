package cn.cnic.component.stop;

import cn.cnic.ApplicationTests;
import cn.cnic.component.flow.model.Flow;
import cn.cnic.component.stopsComponent.service.IStopsHubService;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;

public class StopsHubTest extends ApplicationTests {

    @Resource
    private IStopsHubService stopsHubServiceImpl;


    @Test
    @Rollback(false)
    public void testMountStopsHub() {
        String result = stopsHubServiceImpl.mountStopsHub("Nature", true, "222","sks-piflow.jar");
        System.out.println(result);
    }

}
