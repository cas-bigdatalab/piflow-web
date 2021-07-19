package cn.cnic.component.testData.mapper;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.LoggerUtil;
import cn.cnic.component.testData.entity.TestDataSchemaValues;

public class TestDataSchemaValuesTest extends ApplicationTests {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();
	
    @Autowired
    private TestDataSchemaValuesMapper testDataSchemaValuesMapper;

    @Test
    public void testGetTestDataSchemaValuesList() {
        List<TestDataSchemaValues> testDataSchemaValuesList = testDataSchemaValuesMapper.getTestDataSchemaValuesList();
        logger.info("length:" + testDataSchemaValuesList.size());
    }
}
