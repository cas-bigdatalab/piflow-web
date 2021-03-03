package cn.cnic.component.testData.service.impl;

import java.util.List;
import java.util.Map;

import cn.cnic.component.testData.entity.TestData;
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

@Service
@Transactional
public class TestDataServiceImpl implements ITestDataService {
    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private TestDataDomain testDataDomain;

    /**
     * add addTestData
     *
     * @param username
     * @param name
     * @param loadId
     * @param templateType
     * @return
     */
    @Override
    public String addTestData(String username, String name, String loadId, String templateType) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(name)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param name is empty");
        }
        if (StringUtils.isBlank(loadId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param 'loadId' is empty");
        }
        if (StringUtils.isBlank(templateType)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param 'templateType' is empty");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("save template success");
    }

    /**
     * saveOrUpdateTestDataSchema
     *
     * @param username
     * @param name
     * @param loadId
     * @param templateType
     * @return
     */
    @Override
    public String saveOrUpdateTestDataSchema(String username, String name, String loadId, String templateType){
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(name)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param name is empty");
        }
        if (StringUtils.isBlank(loadId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param 'loadId' is empty");
        }
        if (StringUtils.isBlank(templateType)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param 'templateType' is empty");
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr("save template success");
    }


    /**
     * saveOrUpdateTestDataSchemaValues
     *
     * @param username
     * @param name
     * @param loadId
     * @param templateType
     * @return
     */
    @Override
    public String saveOrUpdateTestDataSchemaValues(String username, String name, String loadId, String templateType){
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(name)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param name is empty");
        }
        if (StringUtils.isBlank(loadId)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param 'loadId' is empty");
        }
        if (StringUtils.isBlank(templateType)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param 'templateType' is empty");
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

    @Override
    public String getTestDataListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
        }
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is error");
        }
        Page<TestData> page = PageHelper.startPage(offset, limit);
        testDataDomain.getTestDataList(isAdmin, username, param);
        Map<String, Object> rtnMap = PageHelperUtils.setLayTableParam(page, null);
        rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    @Override
    public String getTestDataSchemaList(String username, boolean isAdmin, String param, String testDataId) {
        return "";
    }

    @Override
    public String getTestDataSchemaListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param, String testDataId) {
        return "";
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
        List<Map<String, String>> testDataSchemaIdAndNameListByTestDataId = testDataDomain.getTestDataSchemaIdAndNameListByTestDataId(testDataId);
        Page<DataSourceVo> page = PageHelper.startPage(offset, limit);
        testDataDomain.getTestDataSchemaValuesCustomList(isAdmin, username, testDataSchemaIdAndNameListByTestDataId);
        Map<String, Object> rtnMap = PageHelperUtils.setLayTableParam(page, null);
        rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
        return JsonUtils.toJsonNoException(rtnMap);
    }

    /**
     * getDataSourceVoListPage
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
        List<Map<String, String>> testDataSchemaIdAndNameListByTestDataId = testDataDomain.getTestDataSchemaIdAndNameListByTestDataId(testDataId);
        List<Map<String, String>> testDataSchemaValuesCustomList = testDataDomain.getTestDataSchemaValuesCustomList(isAdmin, username, testDataSchemaIdAndNameListByTestDataId);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("list", testDataSchemaValuesCustomList);
    }

}
