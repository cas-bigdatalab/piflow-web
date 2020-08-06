package cn.cnic.component.dashboard.service;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.JsonUtils;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import javax.annotation.Resource;
import java.util.Map;

public class StatisticTest extends ApplicationTests {

    @Resource
    private IStatisticService statisticServiceImpl;

    @Test
    @Rollback(false)
    public void testGetFlowStatisticInfo() {
        Map<String,String> result = statisticServiceImpl.getFlowStatisticInfo();
        //JsonUtils.toJsonNoException(setSucceededCustomParam(key, value));
        System.out.println(result);

    }
}
