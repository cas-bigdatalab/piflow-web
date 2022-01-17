package cn.cnic.component.testData.mapper;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.component.testData.entity.TestDataSchemaValues;
import org.springframework.beans.factory.annotation.Autowired;


public class TestDataSchemaValuesTest extends ApplicationTests {

    private Logger logger = LoggerUtil.getLogger();

    private final TestDataSchemaValuesMapper testDataSchemaValuesMapper;

    @Autowired
    public TestDataSchemaValuesTest(TestDataSchemaValuesMapper testDataSchemaValuesMapper) {
        this.testDataSchemaValuesMapper = testDataSchemaValuesMapper;
    }

    @Test
    public void testGetTestDataSchemaValuesList() {
        List<TestDataSchemaValues> testDataSchemaValuesList = testDataSchemaValuesMapper.getTestDataSchemaValuesList();
        logger.info("length:" + testDataSchemaValuesList.size());
    }
}
