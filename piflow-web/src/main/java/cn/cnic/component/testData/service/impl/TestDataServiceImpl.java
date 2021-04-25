package cn.cnic.component.testData.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.PageHelperUtils;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.base.util.UUIDUtils;
import cn.cnic.component.testData.domain.TestDataDomain;
import cn.cnic.component.testData.entity.TestData;
import cn.cnic.component.testData.entity.TestDataSchema;
import cn.cnic.component.testData.entity.TestDataSchemaValues;
import cn.cnic.component.testData.service.ITestDataService;
import cn.cnic.component.testData.utils.TestDataSchemaValuesUtils;
import cn.cnic.component.testData.utils.TestDataUtils;
import cn.cnic.component.testData.vo.TestDataSchemaValuesSaveVo;
import cn.cnic.component.testData.vo.TestDataSchemaVo;
import cn.cnic.component.testData.vo.TestDataVo;

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
    public String saveOrUpdateTestDataAndSchema(String username, boolean isAdmin, TestDataVo testDataVo) throws Exception {
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
        testData = TestDataUtils.copyDataToTestData(testDataVo, testData, username);
        int affectedRows = 0;
        if (StringUtils.isNotBlank(testDataVoId)) {
            String testDataName = testDataDomain.getTestDataName(testData.getName());
            if(StringUtils.isNotBlank(testDataName)){
                return ReturnMapUtils.setFailedMsgRtnJsonStr("save failed, testDataName is already taken");
            }
            testData.setId(UUIDUtils.getUUID32());
            affectedRows = testDataDomain.addTestData(testData, username);
        } else {
            affectedRows = testDataDomain.saveOrUpdate(testData, username);
        }
        if (affectedRows <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("save failed");
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("testDataId", testData.getId());
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
            //First set enable_flag to false, and then modify it according to the data passed from the page
            testDataSchemaValues.setEnableFlag(false);
            schemaValuesMapDB.put(testDataSchemaValues.getId(), testDataSchemaValues);
        }
        List<TestDataSchemaValues> newTestDataSchemaValuesList = new ArrayList<>();
        List<Map<String, String>> schemaValuesList = schemaValuesVo.getSchemaValuesList();
        List<Map<String, String>> schemaValuesIdList = schemaValuesVo.getSchemaValuesIdList();
        //new schemaValue list
        if (null != schemaValuesList && null != schemaValuesIdList) {
            // Determine whether the incoming data is complete, whether the id and the number of data correspond
            if (schemaValuesList.size() != schemaValuesIdList.size()) {
                return ReturnMapUtils.setFailedMsgRtnJsonStr("Error! 'schemaValuesList' and 'schemaValuesIdList' are not unified");
            }
            // cycle
            for (int i = 0; i < schemaValuesList.size(); i++) {
                //get data object
                Map<String, String> schemaValuesMap = schemaValuesList.get(i);
                //get data object
                Map<String, String> schemaValuesIdMap = schemaValuesIdList.get(i);
                //Cycle key
                for (String fieldName : schemaValuesMap.keySet()) {
                    // Use "fieldName" to get id from "schemaValuesMap"
                    String schemaValuesId = schemaValuesIdMap.get(fieldName);
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
                        testDataSchemaValues.setEnableFlag(true);
                    }
                    String schemaValues = schemaValuesMap.get(fieldName);
                    testDataSchemaValues.setFieldValue(schemaValues);
                    testDataSchemaValues.setDataRow(i + 1);
                    testDataSchemaValues.setLastUpdateDttm(new Date());
                    testDataSchemaValues.setLastUpdateUser(username);
                    newTestDataSchemaValuesList.add(testDataSchemaValues);
                }
            }
        }
        if (null == newTestDataSchemaValuesList || newTestDataSchemaValuesList.size() < 0) {
            return ReturnMapUtils.setSucceededMsgRtnJsonStr("no data update");
        }
        testDataDomain.saveOrUpdateTestDataSchemaValuesList(username, false, newTestDataSchemaValuesList, testDataById);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("save template success");
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

}
