package cn.cnic.component.testData.utils;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import cn.cnic.base.util.UUIDUtils;
import cn.cnic.component.testData.entity.TestDataSchema;
import cn.cnic.component.testData.vo.TestDataSchemaVo;

public class TestDataSchemaUtils {

	public static TestDataSchema setTestDataSchemaBasicInformation(TestDataSchema testDataSchema, boolean isSetId, String username) {
		if (null == testDataSchema) {
			testDataSchema = new TestDataSchema();
		}
		if (isSetId) {
			testDataSchema.setId(UUIDUtils.getUUID32());
		}
		// set MxGraphModel basic information
		testDataSchema.setCrtDttm(new Date());
		testDataSchema.setCrtUser(username);
		testDataSchema.setLastUpdateDttm(new Date());
		testDataSchema.setLastUpdateUser(username);
		testDataSchema.setVersion(0L);
		return testDataSchema;
	}

	/**
	 * testDataSchemaVo data to testDataSchema
	 * 
	 * @param testDataSchemaVo
	 * @param testDataSchema
	 * @return
	 */
	public static TestDataSchema copyDataToTestDataSchema(TestDataSchemaVo testDataSchemaVo, TestDataSchema testDataSchema, String username) {
		if (null == testDataSchemaVo || StringUtils.isBlank(username)) {
			return null;
		}
		if (null == testDataSchema) {
			testDataSchema = setTestDataSchemaBasicInformation(null, false, username);
		}
		// copy
		BeanUtils.copyProperties(testDataSchemaVo, testDataSchema);
		testDataSchema.setLastUpdateDttm(new Date());
		testDataSchema.setLastUpdateUser(username);
		return testDataSchema;
	}

}
