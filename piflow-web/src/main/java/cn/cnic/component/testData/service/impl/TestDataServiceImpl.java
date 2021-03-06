package cn.cnic.component.testData.service.impl;

import java.util.HashMap;
import java.util.Iterator;
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
import cn.cnic.component.testData.domain.TestDataDomain;
import cn.cnic.component.testData.entity.TestData;
import cn.cnic.component.testData.entity.TestDataSchema;
import cn.cnic.component.testData.entity.TestDataSchemaValues;
import cn.cnic.component.testData.service.ITestDataService;
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
        int affectedRows = testDataDomain.saveOrUpdate(testData, username);
        if(affectedRows <= 0) {
        	return ReturnMapUtils.setFailedMsgRtnJsonStr("save failed");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("save success");
    }


    /**
     * saveOrUpdateTestDataSchemaValues
     *
     * @param username
     * @param isAdmin
     * @param schemaValuesVo
     * @return String
     */
    @Override
    public String saveOrUpdateTestDataSchemaValues(String username, boolean isAdmin, TestDataSchemaValuesSaveVo schemaValuesVo) {
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
        List<TestDataSchemaValues> testDataSchemaValuesList = testDataDomain.getTestDataSchemaValuesListByTestDataId(testDataId);
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
        List<LinkedHashMap<String,String>> schemaValuesList = schemaValuesVo.getSchemaValuesList();
        List<LinkedHashMap<String,String>> schemaValuesIdList = schemaValuesVo.getSchemaValuesIdList();
        //new schemaValue list
        if (null != schemaValuesList && null != schemaValuesIdList) {
        	if (schemaValuesList.size() != schemaValuesIdList.size()) {
        		return ReturnMapUtils.setFailedMsgRtnJsonStr("Error! 'schemaValuesList' and 'schemaValuesIdList' are not unified");
        	}
        	for (int i = 0; i < schemaValuesList.size(); i++) {
        		LinkedHashMap<String,String> schemaValues = schemaValuesList.get(i);
        		LinkedHashMap<String,String> schemaValuesId = schemaValuesIdList.get(i);
        		for (String fieldName : schemaValues.keySet()) {
        			String fieldNameValue = schemaValuesId.get(fieldName);
				}
        		
    		}	
        }
        
        //List<LinkedHashMap<String, String>> testDataSchemaIdAndNameListByTestDataId = testDataDomain.getTestDataSchemaIdAndNameListByTestDataId(testDataId);
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("save template success");
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
        rtnMap.put("testData",testDataVo);
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
        List<LinkedHashMap<String, String>> testDataSchemaIdAndNameListByTestDataId = testDataDomain.getTestDataSchemaIdAndNameListByTestDataId(testDataId);
        // 
        Page<LinkedHashMap<String, String>> page = PageHelper.startPage(offset, limit);
        testDataDomain.getTestDataSchemaValuesCustomList(isAdmin, username, testDataId, testDataSchemaIdAndNameListByTestDataId);
        Map<String, Object> rtnMap = PageHelperUtils.setLayTableParam(page, null);
        // find id
        Page<LinkedHashMap<String, String>> page1 = PageHelper.startPage(offset, limit);
        testDataDomain.getTestDataSchemaValuesCustomListId(isAdmin, username, testDataId, testDataSchemaIdAndNameListByTestDataId);
        rtnMap = PageHelperUtils.setCustomDataKey(page1, "data_id", rtnMap);
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
        List<LinkedHashMap<String, String>> testDataSchemaIdAndNameListByTestDataId = testDataDomain.getTestDataSchemaIdAndNameListByTestDataId(testDataId);
        List<LinkedHashMap<String, String>> testDataSchemaValuesCustomList_id = testDataDomain.getTestDataSchemaValuesCustomListId(isAdmin, username, testDataId, testDataSchemaIdAndNameListByTestDataId);
        List<LinkedHashMap<String, String>> testDataSchemaValuesCustomList = testDataDomain.getTestDataSchemaValuesCustomList(isAdmin, username, testDataId, testDataSchemaIdAndNameListByTestDataId);
        Map<String, Object> setSucceededMsg = ReturnMapUtils.setSucceededMsg(ReturnMapUtils.SUCCEEDED_MSG);
        setSucceededMsg.put("list_id", testDataSchemaValuesCustomList_id);
        setSucceededMsg.put("list", testDataSchemaValuesCustomList);
        return JsonUtils.toJsonNoException(setSucceededMsg);
    }

}
