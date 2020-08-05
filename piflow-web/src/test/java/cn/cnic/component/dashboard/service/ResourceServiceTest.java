package cn.cnic.component.dashboard.service;

import cn.cnic.ApplicationTests;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;

public class ResourceServiceTest  extends ApplicationTests {

    @Resource
    private IResourceService resourceServiceImpl;

    @Test
    @Rollback(false)
    public void testGetResourceInfo() {
        String result = resourceServiceImpl.getResourceInfo();
        System.out.println(result);

    }
}
