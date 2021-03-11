package cn.cnic.component.testData.service.impl;

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
import cn.cnic.component.dataSource.vo.DataSourceVo;
import cn.cnic.component.testData.domain.TestDataDomain;
import cn.cnic.component.testData.service.ITestDataService;
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
     */
    @Override
    public String saveOrUpdateTestDataAndSchema(String username, boolean isAdmin, TestDataVo testDataVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (null != testDataVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param name is empty");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("save template success");
    }


    /**
     * saveOrUpdateTestDataSchemaValues
     *
     * @param username
     * @param isAdmin
     * @param testDataSchemaVo
     * @return String
     */
    @Override
    public String saveOrUpdateTestDataSchemaValues(String username, boolean isAdmin, TestDataSchemaVo testDataSchemaVo) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (null != testDataSchemaVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param name is empty");
        }
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
        TestDataVo testDataVo = testDataDomain.getTestDataById(testDataId);
        if (null == testDataVo) {
        	return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null");
        }
        List<TestDataSchemaVo> testDataVoList = testDataDomain.getTestDataSchemaVoListByTestDataId(isAdmin, username, param, testDataId);
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
        TestDataVo testDataVo = testDataDomain.getTestDataById(testDataId);
        if (null == testDataVo) {
        	return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null");
        }
        Page<TestDataSchemaVo> page = PageHelper.startPage(offset, limit);
        testDataDomain.getTestDataSchemaVoListByTestDataId(isAdmin, username, param, testDataId);
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
        List<LinkedHashMap<String, String>> testDataSchemaIdAndNameListByTestDataId = testDataDomain.getTestDataSchemaIdAndNameListByTestDataId(testDataId);
        Page<DataSourceVo> page = PageHelper.startPage(offset, limit);
        testDataDomain.getTestDataSchemaValuesCustomList(isAdmin, username, testDataId, testDataSchemaIdAndNameListByTestDataId);
        Map<String, Object> rtnMap = PageHelperUtils.setLayTableParam(page, null);
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
        List<LinkedHashMap<String, String>> testDataSchemaValuesCustomList = testDataDomain.getTestDataSchemaValuesCustomList(isAdmin, username, testDataId, testDataSchemaIdAndNameListByTestDataId);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("list", testDataSchemaValuesCustomList);
    }

}
