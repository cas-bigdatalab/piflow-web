package cn.cnic.component.sparkJar.service;

import cn.cnic.ApplicationTests;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;

public class SparkJarTest extends ApplicationTests {

    @Resource
    private ISparkJarService sparkJarServiceImpl;


    @Test
    @Rollback(false)
    public void testMountSparkJar() {
        String result = sparkJarServiceImpl.mountSparkJar("Nature", true, "111");
        System.out.println(result);

    }

    @Test
    @Rollback(false)
    public void testUnMountSparkJar() {
        String result = sparkJarServiceImpl.unmountSparkJar("Nature", true, "111");
        System.out.println(result);
    }

}
