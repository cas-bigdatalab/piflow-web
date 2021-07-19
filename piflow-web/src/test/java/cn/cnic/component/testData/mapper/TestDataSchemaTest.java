package cn.cnic.component.testData.mapper;

import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import cn.cnic.ApplicationTests;
import cn.cnic.base.utils.LoggerUtil;

public class TestDataSchemaTest extends ApplicationTests {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();
	
    @Autowired
    private TestDataSchemaMapper testDataSchemaMapper;

    @Test
    public void testGetTestDataSchemaIdList() {
        List<LinkedHashMap<String, Object>> testDataSchemaIdListByTestDataId = testDataSchemaMapper.getTestDataSchemaIdAndNameListByTestDataId("0106c60c7e8a4dd3b866483fd1c14d67");
        logger.info("length:" + testDataSchemaIdListByTestDataId.size());
    }
}
