package cn.cnic.component.flow.mapper;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.component.flow.entity.CustomizedProperty;
import org.springframework.beans.factory.annotation.Autowired;


public class CustomizedPropertyMapperMapperTest extends ApplicationTests {

    private Logger logger = LoggerUtil.getLogger();

    private final CustomizedPropertyMapper customizedPropertyMapper;

    @Autowired
    public CustomizedPropertyMapperMapperTest(CustomizedPropertyMapper customizedPropertyMapper) {
        this.customizedPropertyMapper = customizedPropertyMapper;
    }

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
