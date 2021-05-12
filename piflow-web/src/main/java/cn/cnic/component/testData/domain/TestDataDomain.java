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
     * @return int
     * @throws Exception
     */
    public int saveOrUpdate(TestData testData, String username) throws Exception {
        if (StringUtils.isBlank(testData.getId())) {
            return addTestData(testData, username);
        }
        return updateTestData(testData, username);
    }
    
    /**
     * save or update TestDataSchemaValues list
     * 
     * @param username
     * @param schemaValuesList
     * @param testData
     * @return
     * @throws Exception
     */
    public int saveOrUpdateTestDataSchemaValuesList(String username, List<TestDataSchemaValues> schemaValuesList, TestData testData) throws Exception {

        if (null == schemaValuesList) {
            return 0;
        }
        int affectedRows = 0;
        for (TestDataSchemaValues testDataSchemaValues : schemaValuesList) {
            if (StringUtils.isBlank(testDataSchemaValues.getId())) {
                affectedRows += addTestDataSchemaValues(username, testDataSchemaValues, testData);
            }
            affectedRows += updateTestDataSchemaValues(username, testDataSchemaValues, testDataSchemaValues.getTestDataSchema(), testData);
        }
        return affectedRows;

    }
    
    /**
     * add TestData
     * 
     * @param testData
     * @param username
     * @return int
     * @throws Exception
     */
    public int addTestData(TestData testData, String username) throws Exception {
        if (null == testData) {
            throw new Exception("testData is null");
        }
        if (StringUtils.isBlank(testData.getId())) {
            testData.setId(UUIDUtils.getUUID32());
        }
        testData.setCrtUser(username);
        testData.setCrtDttm(new Date());
        testData.setLastUpdateUser(username);
        testData.setLastUpdateDttm(new Date());
        int affectedRows = testDataMapper.addTestData(testData);
        if (affectedRows < 0) {
            throw new Exception("save testData failed");
        }
        affectedRows += addSchemaList(testData.getSchemaList(), testData, username);
        return affectedRows;
    }

    /**
     * add TestDataSchema
     * 
     * @param testDataSchema
     * @param username
     * @return int
     * @throws Exception
     */
    public int addTestDataSchema(TestDataSchema testDataSchema, String username) throws Exception {
        if (null == testDataSchema) {
            throw new Exception("testDataSchema is null");
        }
        if (StringUtils.isBlank(testDataSchema.getId())) {
            testDataSchema.setId(UUIDUtils.getUUID32());
        }
        testDataSchema.setCrtUser(username);
        testDataSchema.setCrtDttm(new Date());
        testDataSchema.setLastUpdateUser(username);
        testDataSchema.setLastUpdateDttm(new Date());
        int affectedRows = testDataSchemaMapper.addTestDataSchema(testDataSchema);
        if (affectedRows < 0) {
            throw new Exception("save testData failed");
        }
        //affectedRows += addTestDataSchemaValuesList(username, testDataSchema.getSchemaValuesList(), testDataSchema, testDataSchema.getTestData());
        return affectedRows;
    }

    /**
     * add schemaList
     *
     * @param schemaList
     * @param username
     * @return int
     * @throws Exception
     */
    public int addSchemaList(List<TestDataSchema> schemaList, TestData testData, String username) throws Exception {
        if (null == schemaList || schemaList.size() <= 0) {
            return 0;
        }
        int affectedRows = 0;
        for (TestDataSchema testDataSchema : schemaList) {
        	testDataSchema.setTestData(testData);
        	affectedRows += addTestDataSchema(testDataSchema, username);
        }
        return affectedRows;
    }

    /**
     * add TestDataSchemaValues
     * 
     * @param username
     * @param schemaValuesList
     * @param testData
     * @return
     * @throws Exception
     */
    public int addTestDataSchemaValuesList(String username, List<TestDataSchemaValues> schemaValuesList, TestData testData) throws Exception {
        if (null == schemaValuesList) {
            throw new Exception("testDataSchemaValues is null");
        }
        if (null == testData) {
            throw new Exception("testData is null");
        }
        int affectedRows = 0;
        for (TestDataSchemaValues testDataSchemaValues : schemaValuesList) {
            affectedRows += addTestDataSchemaValues(username, testDataSchemaValues, testData);
        }
        if (affectedRows < 0) {
            throw new Exception("save testData failed");
        }
        return affectedRows;
    }

    /**
     * add TestDataSchemaValues
     * 
     * @param username
     * @param testDataSchemaValues
     * @param testData
     * @return
     * @throws Exception
     */
    public int addTestDataSchemaValues(String username, TestDataSchemaValues testDataSchemaValues, TestData testData) throws Exception {
        if (null == testDataSchemaValues) {
            throw new Exception("testDataSchemaValues is null");
        }
        if (null == testDataSchemaValues.getTestDataSchema()) {
            throw new Exception("testDataSchema is null");
        }
        if (null == testData) {
            throw new Exception("testData is null");
        }
        if (StringUtils.isBlank(testDataSchemaValues.getId())) {
            testDataSchemaValues.setId(UUIDUtils.getUUID32());
        }
        testDataSchemaValues.setCrtUser(username);
        testDataSchemaValues.setCrtDttm(new Date());
        testDataSchemaValues.setLastUpdateUser(username);
        testDataSchemaValues.setLastUpdateDttm(new Date());
        testDataSchemaValues.setTestData(testData);
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
     * @return int
     * @throws Exception
     */
    public int updateTestData(TestData testData, String username) throws Exception {
        if (null == testData) {
            throw new Exception("testData is null");
        }
        if (StringUtils.isBlank(testData.getId())) {
            throw new Exception("testDataSchema id is null");
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
     * @return int
     * @throws Exception
     */
    public int updateTestDataSchema(TestDataSchema testDataSchema, String username) throws Exception {
        if (null == testDataSchema) {
            throw new Exception("testDataSchema is null");
        }
        if (StringUtils.isBlank(testDataSchema.getId())) {
            throw new Exception("testDataSchema id is null");
        }
        testDataSchema.setLastUpdateUser(username);
        testDataSchema.setLastUpdateDttm(new Date());
        int affectedRows = testDataSchemaMapper.updateTestDataSchema(testDataSchema);
        //affectedRows += saveOrUpdateTestDataSchemaValuesList(username, false, testDataSchema.getSchemaValuesList(), testDataSchema.getTestData());
        return affectedRows;

    }

    /**
     * update TestDataSchemaValues
     * 
     * @param username
     * @param testDataSchemaValues
     * @param testDataSchema
     * @param testData
     * @return
     * @throws Exception
     */
    public int updateTestDataSchemaValues(String username, TestDataSchemaValues testDataSchemaValues, TestDataSchema testDataSchema, TestData testData) throws Exception {
        if (null == testDataSchemaValues) {
            throw new Exception("testDataSchemaValues is null");
        }
        if (null == testDataSchema) {
            throw new Exception("testDataSchema is null");
        }
        if (null == testData) {
            throw new Exception("testData is null");
        }
        if (StringUtils.isBlank(testDataSchemaValues.getId())) {
            throw new Exception("testDataSchemaValues id is null");
        }
        testDataSchemaValues.setTestDataSchema(testDataSchema);
        testDataSchemaValues.setTestData(testData);
        testDataSchemaValues.setLastUpdateUser(username);
        testDataSchemaValues.setLastUpdateDttm(new Date());
        testDataSchemaValues.setTestData(testData);
        testDataSchemaValues.setTestDataSchema(testDataSchema);
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
     * update TestDataSchema
     *
     * @param schemaIdList
     * @param username
     * @return int
     * @throws Exception
     */
    public int delTestDataSchemaList(List<String> schemaIdList, boolean isAdmin, String username) throws Exception {
        if (null == schemaIdList || schemaIdList.size() <=0) {
            throw new Exception("schemaIdList is null");
        }
        if (StringUtils.isBlank(username)) {
            throw new Exception("username is null");
        }
        int affectedRows = 0;
        for (String schemaId : schemaIdList) {
            if (StringUtils.isBlank(schemaId)) {
                throw new Exception("testDataSchema id is null");
            }
            Integer integer = testDataSchemaMapper.delTestDataSchemaById(isAdmin, username, schemaId);
            if (integer > 0){
                affectedRows += testDataSchemaValuesMapper.delTestDataSchemaValuesBySchemaId(isAdmin, username, schemaId);
            }
            affectedRows += integer;
        }
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
     * @return List<Map<String,String>> key1=ID key2=FIELD_NAME
     * @throws
     */
    public List<LinkedHashMap<String, Object>> getTestDataSchemaIdAndNameListByTestDataId(String testDataId) {
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
    public List<LinkedHashMap<String, Object>> getTestDataSchemaValuesCustomList(boolean isAdmin, String username, String testDataId, List<LinkedHashMap<String, Object>> fieldNameList) {
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
    public List<LinkedHashMap<String, Object>> getTestDataSchemaValuesCustomListId(boolean isAdmin, String username, String testDataId, List<LinkedHashMap<String, Object>> fieldNameList) {
        if (null == fieldNameList) {
            return null;
        }
        return testDataSchemaValuesMapper.getTestDataSchemaValuesCustomListId(isAdmin, username, testDataId, fieldNameList);
    }
    
    
    /**
     * getTestDataSchemaValuesListByTestDataId
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

    /**
     * getTestDataSchemaValuesListBySchemaId
     *
     * @param schemaId
     * @throws
     */
    public List<TestDataSchemaValues> getTestDataSchemaValuesListBySchemaId(String schemaId) {
        if (StringUtils.isBlank(schemaId)) {
            return null;
        }
        return testDataSchemaValuesMapper.getTestDataSchemaValuesListBySchemaId(schemaId);
    }

    public String getTestDataName(String testDataName){
        return testDataMapper.getTestDataName(testDataName);
    }

}
