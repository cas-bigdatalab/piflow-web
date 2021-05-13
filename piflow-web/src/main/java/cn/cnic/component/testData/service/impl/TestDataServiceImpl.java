package cn.cnic.component.testData.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.cnic.base.util.FileUtils;
import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.PageHelperUtils;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.base.util.UUIDUtils;
import cn.cnic.common.constant.SysParamsCache;
import cn.cnic.component.testData.domain.TestDataDomain;
import cn.cnic.component.testData.entity.TestData;
import cn.cnic.component.testData.entity.TestDataSchema;
import cn.cnic.component.testData.entity.TestDataSchemaValues;
import cn.cnic.component.testData.service.ITestDataService;
import cn.cnic.component.testData.utils.TestDataSchemaUtils;
import cn.cnic.component.testData.utils.TestDataSchemaValuesUtils;
import cn.cnic.component.testData.utils.TestDataUtils;
import cn.cnic.component.testData.vo.TestDataSchemaVo;
import cn.cnic.component.testData.vo.TestDataVo;
import cn.cnic.controller.requestVo.RequestTestDataSchemaVo;
import cn.cnic.controller.requestVo.RequestTestDataVo;
import cn.cnic.controller.requestVo.SchemaValuesVo;
import cn.cnic.controller.requestVo.TestDataSchemaValuesSaveVo;

@Service
@Transactional
public class TestDataServiceImpl implements ITestDataService {
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private TestDataDomain testDataDomain;

    /**
     * saveOrUpdateTestDataSchema
     *
     * @param username
     * @param isAdmin
     * @param testDataVo
     * @return String
     * @throws Exception
     */
    @Override
    public String saveOrUpdateTestDataAndSchema(String username, boolean isAdmin, RequestTestDataVo testDataVo, boolean flag) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (null == testDataVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param name is empty");
        }
        if (StringUtils.isBlank(testDataVo.getName())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null");
        }
        TestData testData = null;
        String testDataVoId = testDataVo.getId();
        if (StringUtils.isNotBlank(testDataVoId)) {
            testData = testDataDomain.getTestDataById(testDataVoId);
        }
        if (null == testData) {
            testData = TestDataUtils.setTestDataBasicInformation(null, false, username);
        }
        // copy
        BeanUtils.copyProperties(testDataVo, testData);
        // set update info
        testData.setLastUpdateDttm(new Date());
        testData.setLastUpdateUser(username);

        // get testDataSchemaVoList
        List<RequestTestDataSchemaVo> testDataSchemaVoList = testDataVo.getSchemaVoList();

        // update schema
        List<TestDataSchema> testDataSchemaList = testData.getSchemaList();
        List<String> delSchemaIdList = new ArrayList<>();
        if (null == testDataSchemaList) {
            testDataSchemaList = new ArrayList<>();
        }
        if (flag) {
            //update testDataSchemaList
            if (null != testDataSchemaVoList && testDataSchemaVoList.size() > 0) {
                Map<String, TestDataSchema> testDataSchemaDbMap = new HashMap<>();
                for (TestDataSchema testDataSchema : testDataSchemaList) {
                    if (null == testDataSchema) {
                        continue;
                    }
                    testDataSchemaDbMap.put(testDataSchema.getId(), testDataSchema);
                }
                List<TestDataSchema> testDataSchemaListNew = new ArrayList<>();
                for (RequestTestDataSchemaVo testDataSchemaVo : testDataSchemaVoList) {
                    if (null == testDataSchemaVo) {
                        continue;
                    }
                    // Determine if you need to delete it, if necessary, add it to delSchemaIdList
                    if (testDataSchemaVo.isDelete()) {
                        delSchemaIdList.add(testDataSchemaVo.getId());
                        continue;
                    }
                    // get TestDataSchema from testDataSchemaDbMap by testDataSchemaVo id
                    TestDataSchema testDataSchemaNew = testDataSchemaDbMap.get(testDataSchemaVo.getId());
                    // If not, create a new one
                    if (null == testDataSchemaNew) {
                        testDataSchemaNew = TestDataSchemaUtils.setTestDataSchemaBasicInformation(null, false, username);
                    }
                    // copy data to testDataSchemaNew
                    testDataSchemaNew = TestDataSchemaUtils.copyDataToTestDataSchema(testDataSchemaVo, testDataSchemaNew, username);
                    if(null == testDataSchemaNew) {
                        continue;
                    }
                    testDataSchemaListNew.add(testDataSchemaNew);
                }
                testData.setSchemaList(testDataSchemaListNew);
            }
        } else {
            //update testDataSchemaList
            if (null == testDataSchemaVoList || testDataSchemaVoList.size() <= 0) {
                for (TestDataSchema testDataSchema : testDataSchemaList) {
                    if (null == testDataSchema || StringUtils.isBlank(testDataSchema.getId())) {
                        continue;
                    }
                    delSchemaIdList.add(testDataSchema.getId());
                }
            } else {
                Map<String, TestDataSchema> testDataSchemaDbMap = new HashMap<>();
                for (TestDataSchema testDataSchema : testDataSchemaList) {
                    if (null == testDataSchema) {
                        continue;
                    }
                    testDataSchemaDbMap.put(testDataSchema.getId(), testDataSchema);
                }
                List<TestDataSchema> testDataSchemaListNew = new ArrayList<>();
                for (RequestTestDataSchemaVo testDataSchemaVo : testDataSchemaVoList) {
                    if (null == testDataSchemaVo) {
                        continue;
                    }
                    TestDataSchema testDataSchema = testDataSchemaDbMap.get(testDataSchemaVo.getId());
                    if (null == testDataSchema) {
                        testDataSchema = TestDataSchemaUtils.setTestDataSchemaBasicInformation(testDataSchema, flag, username); 
                    }
                    TestDataSchema copyDataToTestDataSchema = TestDataSchemaUtils.copyDataToTestDataSchema(testDataSchemaVo, testDataSchema, username);
                    if(null == copyDataToTestDataSchema) {
                        continue;
                    }
                    testDataSchemaDbMap.remove(testDataSchemaVo.getId());
                    testDataSchemaListNew.add(copyDataToTestDataSchema);
                }
                testData.setSchemaList(testDataSchemaListNew);
                for (TestDataSchema testDataSchema : testDataSchemaDbMap.values()) {
                    if (null == testDataSchema || StringUtils.isBlank(testDataSchema.getId())) {
                        continue;
                    }
                    delSchemaIdList.add(testDataSchema.getId());
                }
            }
        }

        int affectedRows = 0;

        if (StringUtils.isBlank(testDataVoId)) {
            String testDataName = testDataDomain.getTestDataName(testData.getName());
            if(StringUtils.isNotBlank(testDataName)){
                return ReturnMapUtils.setFailedMsgRtnJsonStr("save failed, testDataName is already taken");
            }
            testData.setId(UUIDUtils.getUUID32());
            affectedRows = testDataDomain.addTestData(testData, username);
        } else {
            affectedRows = testDataDomain.updateTestData(testData, username);
        }
        if (null != delSchemaIdList && delSchemaIdList.size() > 0) {
            affectedRows += testDataDomain.delTestDataSchemaList(delSchemaIdList, isAdmin, username);
        }
        if (affectedRows <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("save failed");
        }
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededCustomParam("testDataId", testData.getId());
        ReturnMapUtils.appendValues(rtnMap, "testDataName", testData.getName());
        return ReturnMapUtils.appendValuesToJson(rtnMap, "testDataDesc", testData.getDescription());
    }

    /**
     * saveOrUpdateTestDataSchemaValues
     *
     * @param username
     * @param isAdmin
     * @param schemaValuesVo
     * @return String
     * @throws Exception
     */
    @Override
    public String saveOrUpdateTestDataSchemaValues(String username, boolean isAdmin, TestDataSchemaValuesSaveVo schemaValuesVo) throws Exception {
        // Determine whether it is empty
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        // Determine whether it is empty
        if (null == schemaValuesVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param name is empty");
        }
        // Determine whether it is empty
        String testDataId = schemaValuesVo.getTestDataId();
        if (StringUtils.isBlank(testDataId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("testDataId is null");
        }
        // Query "TestData" based on "testDataId"
        TestData testDataById = testDataDomain.getTestDataById(testDataId);
        if (null == testDataById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("testDataId is error, not data");
        }
        // schema List
        List<TestDataSchema> schemaList = testDataById.getSchemaList();
        if (null == schemaList || schemaList.size() <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Error! TestData schema is null");
        }
        // "schemaList" converts "Map" (key is FieldName)
        Map<String, TestDataSchema> schemaMapDB = new HashMap<>();
        for (TestDataSchema testDataSchema : schemaList) {
            if (null == testDataSchema) {
                continue;
            }
            schemaMapDB.put(testDataSchema.getFieldName(), testDataSchema);
        }
        // schemaValues list
        List<TestDataSchemaValues> testDataSchemaValuesList = testDataById.getSchemaValuesList();
        // "schemaValue" converts "Map" (key is id)
        Map<String, TestDataSchemaValues> schemaValuesMapDB = new HashMap<>();
        // cycle
        for (TestDataSchemaValues testDataSchemaValues : testDataSchemaValuesList) {
            if (null == testDataSchemaValues) {
                continue;
            }
            schemaValuesMapDB.put(testDataSchemaValues.getId(), testDataSchemaValues);
        }
        List<TestDataSchemaValues> newTestDataSchemaValuesList = new ArrayList<>();
        SchemaValuesVo[] schemaValuesVoList = schemaValuesVo.getSchemaValuesList();
        //new schemaValue list
        if (null != schemaValuesVoList) {
            // cycle
            for (int i = 0; i < schemaValuesVoList.length; i++) {
                //get data object
                SchemaValuesVo schemaValuesVo_i = schemaValuesVoList[i];
                //fieldName
                String fieldName = schemaValuesVo_i.getSchemaName();
                //schemaValuesId
                String schemaValuesId = schemaValuesVo_i.getSchemaValueId();
                String schemaValues = schemaValuesVo_i.getSchemaValue();
                int dataRow = schemaValuesVo_i.getDataRow();
                boolean isDelete = schemaValuesVo_i.isDelete();
                // "TestDataSchemaValues" after modification
                TestDataSchemaValues testDataSchemaValues = null;
                // Determine if the Id is empty, add it if it is empty, or modify it otherwise
                if (StringUtils.isBlank(schemaValuesId)) {
                    testDataSchemaValues = TestDataSchemaValuesUtils.setTestDataSchemaBasicInformation(testDataSchemaValues, false, username);
                    //  Use "fieldName" to get TestDataSchema from "schemaMapDB"
                    TestDataSchema testDataSchema = schemaMapDB.get(fieldName);
                    // Associative foreign key
                    testDataSchemaValues.setTestData(testDataById);
                    testDataSchemaValues.setTestDataSchema(testDataSchema);
                } else {
                    //  Use "fieldName" to get TestDataSchema from "schemaMapDB"
                    testDataSchemaValues = schemaValuesMapDB.get(schemaValuesId);
                    if (null == testDataSchemaValues) {
                        return ReturnMapUtils.setFailedMsgRtnJsonStr("Error! 'schemaValuesIdList' data is error");
                    }
                    if (isDelete) {
                        testDataSchemaValues.setEnableFlag(false);
                    }
                }
                
                testDataSchemaValues.setFieldValue(schemaValues);
                testDataSchemaValues.setDataRow(dataRow);
                testDataSchemaValues.setLastUpdateDttm(new Date());
                testDataSchemaValues.setLastUpdateUser(username);
                newTestDataSchemaValuesList.add(testDataSchemaValues);

            }
        }
        if (null == newTestDataSchemaValuesList || newTestDataSchemaValuesList.size() < 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("no data update");
        }
        int saveOrUpdateTestDataSchemaValuesList = testDataDomain.saveOrUpdateTestDataSchemaValuesList(username, newTestDataSchemaValuesList, testDataById);
        if(saveOrUpdateTestDataSchemaValuesList <= 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("save failed");    
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("save success");
    }

    /**
     * checkTestDataName
     *
     * @param username
     * @param isAdmin
     * @param testDataName
     * @return String
     */
    @Override
    public String checkTestDataName(String username, boolean isAdmin, String testDataName){
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Username can not be empty");
        }
        String res = testDataDomain.getTestDataName(testDataName);
        if (StringUtils.isNotBlank(res)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Username is already taken");
        } else {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("Username is available");
        }
    }

    /**
     * delTestData
     *
     * @param username
     * @param isAdmin
     * @param testDataId
     * @return
     */
    @Override
    public String delTestData(String username, boolean isAdmin, String testDataId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(testDataId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("testDataId is null");
        }
        int i = testDataDomain.delTestData(username, isAdmin, testDataId);
        if (i <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("delete failed");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("delete success");
    }

    /**
     * Get TestData list page
     *
     * @param username
     * @param isAdmin
     * @param offset
     * @param limit
     * @param param
     * @return
     */
    @Override
    public String getTestDataListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is error");
        }
        Page<TestDataVo> page = PageHelper.startPage(offset, limit);
        testDataDomain.getTestDataVoList(isAdmin, username, param);
        Map<String, Object> rtnMap = PageHelperUtils.setLayTableParam(page, null);
        rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * Get TestDataSchema list by testDataId
     *
     * @param username
     * @param isAdmin
     * @param param
     * @param testDataId
     * @return
     */
    @Override
    public String getTestDataSchemaList(String username, boolean isAdmin, String param, String testDataId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(testDataId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("testDataId is null");
        }
        TestDataVo testDataVo = testDataDomain.getTestDataVoById(testDataId);
        if (null == testDataVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null");
        }
        List<TestDataSchemaVo> testDataVoList = testDataDomain.getTestDataSchemaVoListByTestDataIdSearch(isAdmin, username, param, testDataId);
        testDataVo.setSchemaVoList(testDataVoList);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("testData", testDataVo);
    }

    /**
     * Get TestData list page
     *
     * @param username
     * @param isAdmin
     * @param offset
     * @param limit
     * @param param
     * @param testDataId
     * @return
     */
    @Override
    public String getTestDataSchemaListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param, String testDataId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is error");
        }
        if (StringUtils.isBlank(testDataId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("testDataId is null");
        }
        TestDataVo testDataVo = testDataDomain.getTestDataVoById(testDataId);
        if (null == testDataVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null");
        }
        Page<TestDataSchemaVo> page = PageHelper.startPage(offset, limit);
        testDataDomain.getTestDataSchemaVoListByTestDataIdSearch(isAdmin, username, param, testDataId);
        Map<String, Object> rtnMap = PageHelperUtils.setLayTableParam(page, null);
        rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
        rtnMap.put("testData", testDataVo);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * getTestDataSchemaValuesCustomListPage
     *
     * @param username
     * @param isAdmin
     * @param offset
     * @param limit
     * @param param
     * @param testDataId
     * @return
     */
    @Override
    public String getTestDataSchemaValuesCustomListPage(String username, boolean isAdmin, Integer offset, Integer limit,
                                                        String param, String testDataId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is error");
        }
        if (StringUtils.isBlank(testDataId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("testDataId is error");
        }
        // find title
        List<LinkedHashMap<String, Object>> testDataSchemaIdAndNameListByTestDataId = testDataDomain.getTestDataSchemaIdAndNameListByTestDataId(testDataId);
        // 
        Page<LinkedHashMap<String, Object>> page = PageHelper.startPage(offset, limit);
        testDataDomain.getTestDataSchemaValuesCustomList(isAdmin, username, testDataId, testDataSchemaIdAndNameListByTestDataId);
        Map<String, Object> rtnMap = PageHelperUtils.setCustomDataKey(page, "count","schemaValue", null);
        // find id
        Page<LinkedHashMap<String, Object>> page1 = PageHelper.startPage(offset, limit);
        testDataDomain.getTestDataSchemaValuesCustomListId(isAdmin, username, testDataId, testDataSchemaIdAndNameListByTestDataId);
        rtnMap = PageHelperUtils.setCustomDataKey(page1, "count","schemaValueId", rtnMap);
        rtnMap.put("schema", testDataSchemaIdAndNameListByTestDataId);
        rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * getTestDataSchemaValuesCustomList
     *
     * @param username
     * @param isAdmin
     * @param param
     * @param testDataId
     * @return
     */
    @Override
    public String getTestDataSchemaValuesCustomList(String username, boolean isAdmin, String param, String testDataId) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (StringUtils.isBlank(testDataId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("testDataId is error");
        }
        List<LinkedHashMap<String, Object>> testDataSchemaIdAndNameListByTestDataId = testDataDomain.getTestDataSchemaIdAndNameListByTestDataId(testDataId);
        List<LinkedHashMap<String, Object>> testDataSchemaValuesCustomList_id = testDataDomain.getTestDataSchemaValuesCustomListId(isAdmin, username, testDataId, testDataSchemaIdAndNameListByTestDataId);
        List<LinkedHashMap<String, Object>> testDataSchemaValuesCustomList = testDataDomain.getTestDataSchemaValuesCustomList(isAdmin, username, testDataId, testDataSchemaIdAndNameListByTestDataId);
        Map<String, Object> setSucceededMsg = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);
        setSucceededMsg.put("schema", testDataSchemaIdAndNameListByTestDataId);
        setSucceededMsg.put("schemaValue", testDataSchemaValuesCustomList);
        setSucceededMsg.put("schemaValueId", testDataSchemaValuesCustomList_id);
        return JsonUtils.toJsonNoException(setSucceededMsg);
    }

    /**
     * Upload csv file and save flowTemplate
     *
     * @param username
     * @param testDataId
     * @param header
     * @param schema
     * @param delimiter
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    public String uploadCsvFile(String username, String testDataId, boolean header, String schema, String delimiter, MultipartFile file) throws Exception{
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(testDataId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("testDataId is null");
        }
        if (null ==delimiter) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("delimiter is null");
        }
        if (file.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Upload failed, please try again later");
        }
        TestData testDataDB = testDataDomain.getTestDataById(testDataId);
        if (null == testDataDB) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("testData is null");
        }
        if (!header && StringUtils.isBlank(schema)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("schema is null");
        }
        if (header) {
            schema = null;
        }
        Map<String, Object> uploadMap = FileUtils.uploadRtnMap(file, SysParamsCache.CSV_PATH, null);
        if (null == uploadMap || uploadMap.isEmpty()) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Upload failed, please try again later");
        }
        Integer code = (Integer) uploadMap.get("code");
        if (500 == code) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("failed to upload file");
        }
        String path = (String) uploadMap.get("path");
        //Read the CSV file according to the saved file path and return the CSV string
        LinkedHashMap<String, List<String>> csvMap = FileUtils.ParseCsvFileRtnColumnData(path, delimiter, schema);
        if(null == csvMap) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("save failed");
        }
        List<TestDataSchema> testDataSchemaList = new ArrayList<>();
        List<TestDataSchemaValues> testDataSchemaValuesList = new ArrayList<>();
        int i = 0;
        for (String fieldName : csvMap.keySet()) {
            TestDataSchema testDataSchema = TestDataSchemaUtils.setTestDataSchemaBasicInformation(null, true, username);
            testDataSchema.setFieldName(fieldName);
            testDataSchema.setFieldType("String");
            testDataSchema.setFieldSoft(i + 1);
            testDataSchema.setTestData(testDataDB);
            testDataSchemaList.add(testDataSchema);
            // values 
            List<String> fieldNameValueList = csvMap.get(fieldName);
            for (int j = 0; j < fieldNameValueList.size(); j++) {
                String fieldNameValue_j = fieldNameValueList.get(j);
                TestDataSchemaValues testDataSchemaValues = TestDataSchemaValuesUtils.setTestDataSchemaBasicInformation(null, true, username); 
                testDataSchemaValues.setDataRow(j);
                testDataSchemaValues.setFieldValue(fieldNameValue_j);
                testDataSchemaValues.setTestData(testDataDB);
                testDataSchemaValues.setTestDataSchema(testDataSchema);
                testDataSchemaValuesList.add(testDataSchemaValues);
            }
            i++;
        }
        int affectedRows = testDataDomain.addSchemaList(testDataSchemaList, testDataDB, username);
        if (affectedRows <= 0) {
            testDataDomain.delTestData(username, false, testDataDB.getId());
            return ReturnMapUtils.setFailedMsgRtnJsonStr("save failed");
        }
        affectedRows += testDataDomain.addTestDataSchemaValuesList(username, testDataSchemaValuesList, testDataDB);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("successful template upload");
    }

}
