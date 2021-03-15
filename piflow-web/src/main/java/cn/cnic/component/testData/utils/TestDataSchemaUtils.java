package cn.cnic.component.testData.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	/**
	 * testDataSchemaVo data to testDataSchema
	 * 
	 * @param testDataSchemaVo
	 * @param testDataSchema
	 * @return
	 */
	public static List<TestDataSchema> copyDataToTestDataSchemaList(List<TestDataSchemaVo> testDataSchemaVoList, List<TestDataSchema> testDataSchemaList, String username) {
		if (null == testDataSchemaVoList || testDataSchemaVoList.size() <= 0 || StringUtils.isBlank(username)) {
			return null;
		}
		if (null == testDataSchemaList) {
			testDataSchemaList = new ArrayList<>();
		}
		Map<String, TestDataSchema> testDataSchemaDbMap = new HashMap<String, TestDataSchema>();
		for (TestDataSchema testDataSchema : testDataSchemaList) {
			if (null == testDataSchema) {
				continue;
			}
			testDataSchemaDbMap.put(testDataSchema.getId(), testDataSchema);
		}
		List<TestDataSchema> testDataSchemaListNew = new ArrayList<TestDataSchema>();
		for (TestDataSchemaVo testDataSchemaVo : testDataSchemaVoList) {
			if (null == testDataSchemaVo) {
				continue;
			}
			TestDataSchema copyDataToTestDataSchema = copyDataToTestDataSchema(testDataSchemaVo, testDataSchemaDbMap.get(testDataSchemaVo.getId()), username);
			if(null == copyDataToTestDataSchema) {
				
			}
			testDataSchemaListNew.add(copyDataToTestDataSchema);
		}
		return testDataSchemaListNew;
	}
}
