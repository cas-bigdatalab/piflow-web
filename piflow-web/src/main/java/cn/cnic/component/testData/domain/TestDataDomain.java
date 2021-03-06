package cn.cnic.component.testData.domain;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.base.util.UUIDUtils;
import cn.cnic.component.testData.entity.TestData;
import cn.cnic.component.testData.entity.TestDataSchema;
import cn.cnic.component.testData.entity.TestDataSchemaValues;
import cn.cnic.component.testData.mapper.TestDataMapper;
import cn.cnic.component.testData.mapper.TestDataSchemaMapper;
import cn.cnic.component.testData.mapper.TestDataSchemaValuesMapper;
import cn.cnic.component.testData.vo.TestDataSchemaVo;
import cn.cnic.component.testData.vo.TestDataVo;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class TestDataDomain {

    @Autowired
    private TestDataMapper testDataMapper;

    @Autowired
    private TestDataSchemaMapper testDataSchemaMapper;

    @Autowired
    private TestDataSchemaValuesMapper testDataSchemaValuesMapper;

    /**
     * save or update TestData
     * 
     * @param testData
     * @param username
     * @param @return
     * @throws Exception
     * @return int
     * @throws
     */
    public int saveOrUpdate(TestData testData, String username) throws Exception {
        if (StringUtils.isBlank(testData.getId())) {
            return addTestData(testData, username);
        }
        return updateTestData(testData, username);
    }

    /**
     * add TestData
     * 
     * @param testData
     * @param username
     * @param @return
     * @throws Exception
     * @return int
     * @throws
     */
    public int addTestData(TestData testData, String username) throws Exception {
        if (null == testData) {
            throw new Exception("testData is null");
        }
        testData.setId(UUIDUtils.getUUID32());
        testData.setCrtUser(username);
        testData.setCrtDttm(new Date());
        testData.setLastUpdateUser(username);
        testData.setLastUpdateDttm(new Date());
        int affectedRows = testDataMapper.addTestData(testData);
        if (affectedRows < 0) {
            throw new Exception("save testData failed");
        }
        List<TestDataSchema> schemaList = testData.getSchemaList();
        if (null != schemaList) {
            for (TestDataSchema testDataSchema : schemaList) {
                testDataSchema.setTestData(testData);
                affectedRows += addTestDataSchema(testDataSchema, username);

            }
        }
        return affectedRows;
    }

    /**
     * add TestDataSchema
     * 
     * @param testDataSchema
     * @param username
     * @param @return
     * @throws Exception
     * @return int
     * @throws
     */
    public int addTestDataSchema(TestDataSchema testDataSchema, String username) throws Exception {
        if (null == testDataSchema) {
            throw new Exception("testDataSchema is null");
        }
        testDataSchema.setId(UUIDUtils.getUUID32());
        testDataSchema.setId(UUIDUtils.getUUID32());
        testDataSchema.setCrtUser(username);
        testDataSchema.setCrtDttm(new Date());
        testDataSchema.setLastUpdateUser(username);
        testDataSchema.setLastUpdateDttm(new Date());
        int affectedRows = testDataSchemaMapper.addTestDataSchema(testDataSchema);
        if (affectedRows < 0) {
            throw new Exception("save testData failed");
        }
        List<TestDataSchemaValues> schemaValuesList = testDataSchema.getSchemaValuesList();
        if (null != schemaValuesList) {
            for (TestDataSchemaValues testDataSchemaValues : schemaValuesList) {
                testDataSchemaValues.setTestDataSchema(testDataSchema);
                affectedRows += addTestDataSchemaValues(testDataSchemaValues, username);
            }
        }
        return affectedRows;
    }

    /**
     * add TestDataSchemaValues
     * 
     * @param testDataSchemaValues
     * @param username
     * @param @return
     * @throws Exception
     * @return int
     * @throws
     */
    public int addTestDataSchemaValues(TestDataSchemaValues testDataSchemaValues, String username) throws Exception {
        if (null == testDataSchemaValues) {
            throw new Exception("testDataSchema is null");
        }
        testDataSchemaValues.setId(UUIDUtils.getUUID32());
        testDataSchemaValues.setId(UUIDUtils.getUUID32());
        testDataSchemaValues.setCrtUser(username);
        testDataSchemaValues.setCrtDttm(new Date());
        testDataSchemaValues.setLastUpdateUser(username);
        testDataSchemaValues.setLastUpdateDttm(new Date());
        int affectedRows = testDataSchemaValuesMapper.addTestDataSchemaValues(testDataSchemaValues);
        if (affectedRows < 0) {
            throw new Exception("save testData failed");
        }
        return affectedRows;
    }

    /**
     * update TestData
     * 
     * @param testData
     * @param username
     * @param @return
     * @throws Exception
     * @return int
     * @throws
     */
    public int updateTestData(TestData testData, String username) throws Exception {
        if (null == testData) {
            throw new Exception("testData is null");
        }
        testData.setLastUpdateUser(username);
        testData.setLastUpdateDttm(new Date());
        int affectedRows = testDataMapper.updateTestData(testData);
        List<TestDataSchema> schemaList = testData.getSchemaList();
        if (null != schemaList) {
            for (TestDataSchema testDataSchema : schemaList) {
                testDataSchema.setTestData(testData);
                if (StringUtils.isBlank(testDataSchema.getId())) {
                    affectedRows += addTestDataSchema(testDataSchema, username);
                    continue;
                }
                affectedRows += updateTestDataSchema(testDataSchema, username);
            }
        }
        return affectedRows;
    }

    /**
     * update TestDataSchema
     * 
     * @param testDataSchema
     * @param username
     * @param @return
     * @throws Exception
     * @return int
     * @throws
     */
    public int updateTestDataSchema(TestDataSchema testDataSchema, String username) throws Exception {
        if (null == testDataSchema) {
            throw new Exception("testDataSchema is null");
        }
        if (StringUtils.isBlank(testDataSchema.getId())) {

        }
        testDataSchema.setLastUpdateUser(username);
        testDataSchema.setLastUpdateDttm(new Date());
        int affectedRows = testDataSchemaMapper.updateTestDataSchema(testDataSchema);
        List<TestDataSchemaValues> schemaValuesList = testDataSchema.getSchemaValuesList();
        if (null != schemaValuesList) {
            for (TestDataSchemaValues testDataSchemaValues : schemaValuesList) {
                testDataSchemaValues.setTestDataSchema(testDataSchema);
                if (StringUtils.isBlank(testDataSchema.getId())) {
                    affectedRows += addTestDataSchemaValues(testDataSchemaValues, username);
                }
                affectedRows += updateTestDataSchemaValues(testDataSchemaValues, username);

            }

        }
        return affectedRows;

    }

    /**
     * update TestDataSchemaValues
     * 
     * @param testDataSchemaValues
     * @param username
     * @param @return
     * @throws Exception
     * @return int
     * @throws
     */
    public int updateTestDataSchemaValues(TestDataSchemaValues testDataSchemaValues, String username) throws Exception {
        if (null == testDataSchemaValues) {
            throw new Exception("testDataSchema is null");
        }
        testDataSchemaValues.setLastUpdateUser(username);
        testDataSchemaValues.setLastUpdateDttm(new Date());
        return testDataSchemaValuesMapper.updateTestDataSchemaValues(testDataSchemaValues);
    }

    /**
     * delete TestData
     * 
     * @param username
     * @param isAdmin
     * @param testDataId
     * @return int 
     * @throws
     */
    public int delTestData(String username, boolean isAdmin, String testDataId) {
        int affectedRows = testDataSchemaValuesMapper.delTestDataSchemaValuesByTestDataId(isAdmin, username, testDataId);
        affectedRows += testDataSchemaMapper.delTestDataSchemaByTestDataId(isAdmin, username, testDataId);
        affectedRows += testDataMapper.delTestDataById(isAdmin, username, testDataId);
        return affectedRows;
    }
    
    /**
     * getTestDataVoById
     * 
     * @param id
     * @return TestDataVo 
     * @throws
     */
    public TestDataVo getTestDataVoById(String id) {
    	return testDataMapper.getTestDataVoById(id);
    }
    
    /**
     * getTestDataById
     * 
     * @param id
     * @return TestDataVo 
     * @throws
     */
    public TestData getTestDataById(String id) {
    	return testDataMapper.getTestDataById(id);
    }

    /**
     * getTestDataList
     * @param isAdmin
     * @param username
     * @param param
     * @return List<TestData>
     * @throws
     */
    public List<TestData> getTestDataList(boolean isAdmin, String username, String param) {
        return testDataMapper.getTestDataList(isAdmin, username, param);
    }
    
    /**
     * getTestDataVoList
     * @param isAdmin
     * @param username
     * @param param
     * @return List<TestDataVo>
     * @throws
     */
    public List<TestDataVo> getTestDataVoList(boolean isAdmin, String username, String param) {
        return testDataMapper.getTestDataVoList(isAdmin, username, param);
    }

    /**
     * getTestDataSchemaListByTestDataId
     * 
     * @param isAdmin
     * @param username
     * @param param
     * @param testDataId
     * @return List<TestDataSchema>
     * @throws
     */
    public List<TestDataSchema> getTestDataSchemaListByTestDataIdSearch(boolean isAdmin, String username, String param, String testDataId) {
        return testDataSchemaMapper.getTestDataSchemaListByTestDataIdSearch(isAdmin, username, param, testDataId);
    }
    
    /**
     * getTestDataSchemaVoListByTestDataId
     * 
     * @param isAdmin
     * @param username
     * @param param
     * @param testDataId
     * @return List<TestDataSchemaVo>
     * @throws
     */
    public List<TestDataSchemaVo> getTestDataSchemaVoListByTestDataIdSearch(boolean isAdmin, String username, String param, String testDataId) {
        return testDataSchemaMapper.getTestDataSchemaVoListByTestDataIdSearch(isAdmin, username, param, testDataId);
    }


    /**
     * getTestDataSchemaIdAndNameListByTestDataId
     * 
     * @param testDataId
     * @return List<Map<String,String>>
     * @throws
     */
    public List<LinkedHashMap<String, String>> getTestDataSchemaIdAndNameListByTestDataId(String testDataId) {
        return testDataSchemaMapper.getTestDataSchemaIdAndNameListByTestDataId(testDataId);
    }

    /**
     * getTestDataSchemaValuesCustomList
     * 
     * @param isAdmin
     * @param username
     * @param fieldNameList
     * @param testDataId
     * @return List<Map<String,String>> 
     * @throws
     */
    public List<LinkedHashMap<String, String>> getTestDataSchemaValuesCustomList(boolean isAdmin, String username, String testDataId, List<LinkedHashMap<String, String>> fieldNameList) {
        if (null == fieldNameList) {
            return null;
        }
        return testDataSchemaValuesMapper.getTestDataSchemaValuesCustomList(isAdmin, username, testDataId, fieldNameList);
    }
    
    /**
     * getTestDataSchemaValuesCustomListId
     * 
     * @param isAdmin
     * @param username
     * @param fieldNameList
     * @param testDataId
     * @return List<Map<String,String>> 
     * @throws
     */
    public List<LinkedHashMap<String, String>> getTestDataSchemaValuesCustomListId(boolean isAdmin, String username, String testDataId, List<LinkedHashMap<String, String>> fieldNameList) {
        if (null == fieldNameList) {
            return null;
        }
        return testDataSchemaValuesMapper.getTestDataSchemaValuesCustomListId(isAdmin, username, testDataId, fieldNameList);
    }
    
    
    /**
     * getTestDataSchemaValuesCustomListId
     * 
     * @param testDataId
     * @throws
     */
    public List<TestDataSchemaValues> getTestDataSchemaValuesListByTestDataId(String testDataId) {
        if (StringUtils.isBlank(testDataId)) {
            return null;
        }
        return testDataSchemaValuesMapper.getTestDataSchemaValuesListByTestDataId(testDataId);
    }

}
