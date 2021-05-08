package cn.cnic.component.flow.mapper;

import cn.cnic.ApplicationTests;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.component.flow.entity.CustomizedProperty;
import org.junit.Test;
import org.slf4j.Logger;

import javax.annotation.Resource;
import java.util.*;

public class CustomizedPropertyMapperMapperTest extends ApplicationTests {

    @Resource
    private CustomizedPropertyMapper customizedPropertyMapper;


    Logger logger = LoggerUtil.getLogger();

    @Test
    public void testGetPropertyListByStopsId() {
        List<CustomizedProperty> customizedPropertyListByStopsId = customizedPropertyMapper.getCustomizedPropertyListByStopsId("66cb0f08e4964d94912b8503f06b73fa");
        if (null == customizedPropertyListByStopsId) {
            logger.info("The query result is empty");
        } else {
            logger.info(customizedPropertyListByStopsId.size() + "");
        }
    }



}
