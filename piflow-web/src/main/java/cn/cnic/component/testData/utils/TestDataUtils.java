package cn.cnic.component.testData.utils;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import cn.cnic.base.util.UUIDUtils;
import cn.cnic.component.testData.entity.TestData;
import cn.cnic.component.testData.entity.TestDataSchema;
import cn.cnic.component.testData.vo.TestDataVo;

public class TestDataUtils {

	public static TestData setTestDataBasicInformation(TestData testData, boolean isSetId, String username) {
		if (null == testData) {
			testData = new TestData();
		}
		if (isSetId) {
			testData.setId(UUIDUtils.getUUID32());
		}
		// set MxGraphModel basic information
		testData.setCrtDttm(new Date());
		testData.setCrtUser(username);
		testData.setLastUpdateDttm(new Date());
		testData.setLastUpdateUser(username);
		testData.setVersion(0L);
		return testData;
	}

	/**
	 * testDataVo data to testData
	 * 
	 * @param testDataVo
	 * @param testData
	 * @return
	 */
	public static TestData copyDataToTestData(TestDataVo testDataVo, TestData testData, String username) {
		if (null == testDataVo || StringUtils.isBlank(username)) {
			return null;
		}
		if (null == testData) {
			testData = setTestDataBasicInformation(null, false, username);
		}
		// copy
		BeanUtils.copyProperties(testDataVo, testData);
		
		testData.setLastUpdateDttm(new Date());
		testData.setLastUpdateUser(username);
		
		List<TestDataSchema> copyDataToTestDataSchemaList = TestDataSchemaUtils.copyDataToTestDataSchemaList(testDataVo.getSchemaVoList(), testData.getSchemaList(), username);
		testData.setSchemaList(copyDataToTestDataSchemaList);
		return testData;
	}
}
