package cn.cnic.component.testData.mapper;

import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;


public class TestDataSchemaTest extends ApplicationTests {

    private Logger logger = LoggerUtil.getLogger();

    private final TestDataSchemaMapper testDataSchemaMapper;

    @Autowired
    public TestDataSchemaTest(TestDataSchemaMapper testDataSchemaMapper) {
        this.testDataSchemaMapper = testDataSchemaMapper;
    }

    @Test
    public void testGetTestDataSchemaIdList() {
        List<LinkedHashMap<String, Object>> testDataSchemaIdListByTestDataId = testDataSchemaMapper.getTestDataSchemaIdAndNameListByTestDataId("0106c60c7e8a4dd3b866483fd1c14d67");
        logger.info("length:" + testDataSchemaIdListByTestDataId.size());
    }
}
