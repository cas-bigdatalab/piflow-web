package cn.cnic.component.testData.domain;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.component.testData.entity.TestData;
import cn.cnic.component.testData.entity.TestDataSchema;
import cn.cnic.component.testData.entity.TestDataSchemaValues;
import cn.cnic.component.testData.mapper.TestDataMapper;
import cn.cnic.component.testData.mapper.TestDataSchemaMapper;
import cn.cnic.component.testData.mapper.TestDataSchemaValuesMapper;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class TestDataDomain {

	@Autowired
	private TestDataMapper testDataMapper;

	@Autowired
	private TestDataSchemaMapper testDataSchemaMapper;

	@Autowired
	private TestDataSchemaValuesMapper testDataSchemaValuesMapper;

	public int saveOrUpdate(TestData testData) throws Exception {
		if (StringUtils.isBlank(testData.getId())) {
			return addTestData(testData);
		}
		return 0;
	}

	public int addTestData(TestData testData) throws Exception {
		if (null == testData) {
			throw new Exception("testData is null");
		}
		int affectedRows = testDataMapper.addTestData(testData);
		if (affectedRows < 0) {
			throw new Exception("save testData failed");
		}
		List<TestDataSchema> schemaList = testData.getSchemaList();
		if (null != schemaList) {
			for (TestDataSchema testDataSchema : schemaList) {
				testDataSchema.setTestData(testData);
				affectedRows += addTestDataSchema(testDataSchema);

			}
		}
		return affectedRows;
	}

	public int addTestDataSchema(TestDataSchema testDataSchema) throws Exception {
		if (null == testDataSchema) {
			throw new Exception("testDataSchema is null");
		}

		int affectedRows = testDataSchemaMapper.addTestDataSchema(testDataSchema);
		if (affectedRows < 0) {
			throw new Exception("save testData failed");
		}
		List<TestDataSchemaValues> schemaValuesList = testDataSchema.getSchemaValuesList();
		if (null != schemaValuesList) {
			for (TestDataSchemaValues testDataSchemaValues : schemaValuesList) {
				testDataSchemaValues.setTestDataSchema(testDataSchema);
				affectedRows += addTestDataSchemaValues(testDataSchemaValues);
			}
		}
		return affectedRows;
	}

	public int addTestDataSchemaValues(TestDataSchemaValues testDataSchemaValues) throws Exception {
		if (null == testDataSchemaValues) {
			throw new Exception("testDataSchema is null");
		}

		int affectedRows = testDataSchemaValuesMapper.addTestDataSchemaValues(testDataSchemaValues);
		if (affectedRows < 0) {
			throw new Exception("save testData failed");
		}
		return affectedRows;
	}

	public int updateTestData(TestData testData) throws Exception {
		if (null == testData) {
			throw new Exception("testData is null");
		}
		int affectedRows = testDataMapper.updateTestData(testData);
		List<TestDataSchema> schemaList = testData.getSchemaList();
		if (null != schemaList) {
			for (TestDataSchema testDataSchema : schemaList) {
				testDataSchema.setTestData(testData);
				if (StringUtils.isBlank(testDataSchema.getId())) {
					affectedRows += addTestDataSchema(testDataSchema);
					continue;
				}
				affectedRows += updateTestDataSchema(testDataSchema);
			}
		}
		return affectedRows;
	}

	public int updateTestDataSchema(TestDataSchema testDataSchema) throws Exception {
		if (null == testDataSchema) {
			throw new Exception("testDataSchema is null");
		}
		if (StringUtils.isBlank(testDataSchema.getId())) {
			
		}
		int affectedRows = testDataSchemaMapper.updateTestDataSchema(testDataSchema);
		List<TestDataSchemaValues> schemaValuesList = testDataSchema.getSchemaValuesList();
		if (null != schemaValuesList) {
			for (TestDataSchemaValues testDataSchemaValues : schemaValuesList) {
				testDataSchemaValues.setTestDataSchema(testDataSchema);
				if(StringUtils.isBlank(testDataSchema.getId())) {
					affectedRows += addTestDataSchemaValues(testDataSchemaValues);
				}
				affectedRows += updateTestDataSchemaValues(testDataSchemaValues);
				
			}
			
		}
		return affectedRows;
		
	}
	
	public int updateTestDataSchemaValues(TestDataSchemaValues testDataSchemaValues) throws Exception {
		if (null == testDataSchemaValues) {
			throw new Exception("testDataSchema is null");
		}
		return testDataSchemaValuesMapper.updateTestDataSchemaValues(testDataSchemaValues);
	}


}
