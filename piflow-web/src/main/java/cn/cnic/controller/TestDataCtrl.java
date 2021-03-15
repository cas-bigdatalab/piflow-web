package cn.cnic.controller;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.testData.service.ITestDataService;
import cn.cnic.component.testData.vo.TestDataSchemaValuesSaveVo;
import cn.cnic.component.testData.vo.TestDataVo;

@RestController
@RequestMapping("/testData")
public class TestDataCtrl {

    @Resource
    private ITestDataService testDataServiceImpl;

    /**
     * saveOrUpdateTestDataSchema
     *
     * @param testDataId
     * @return String
     * @throws Exception 
     */
    @RequestMapping("/saveOrUpdateTestDataSchema")
    @ResponseBody
    public String saveOrUpdateTestDataSchema(TestDataVo testDataVo) throws Exception {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return testDataServiceImpl.saveOrUpdateTestDataAndSchema(currentUsername, isAdmin, testDataVo);
    }
    
    @RequestMapping("/delTestData")
    @ResponseBody
    public String delTestData(String testDataId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return testDataServiceImpl.delTestData(currentUsername, isAdmin, testDataId);
    }
    
    /**
     * saveOrUpdateTestDataSchemaValues
     * 
     * @param testDataSchemaVo
     * @return String
     */
    @RequestMapping("/saveOrUpdateTestDataSchemaValues")
    @ResponseBody
    public String saveOrUpdateTestDataSchemaValues(TestDataSchemaValuesSaveVo schemaValuesVo) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
    	return testDataServiceImpl.saveOrUpdateTestDataSchemaValues(currentUsername, isAdmin, schemaValuesVo);
    }
    
    /**
     * "testData" list Pagination
     *
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping("/testDataListPage")
    @ResponseBody
    public String testDataListPage(Integer page, Integer limit, String param) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return testDataServiceImpl.getTestDataListPage(currentUsername, isAdmin, page, limit, param);
    }

    /**
     * testDataSchemaListPage
     *
     * @param page
     * @param limit
     * @param param
     * @param testDataId
     * @return
     */
    @RequestMapping("/testDataSchemaListPage")
    @ResponseBody
    public String testDataSchemaLListPage(Integer page, Integer limit, String param, String testDataId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return testDataServiceImpl.getTestDataSchemaListPage(currentUsername, isAdmin, page, limit, param, testDataId);
    }

    /**
     * testDataSchemaList
     *
     * @param param
     * @param testDataId
     * @return
     */
    @RequestMapping("/testDataSchemaList")
    @ResponseBody
    public String testDataSchemaList(String param, String testDataId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return testDataServiceImpl.getTestDataSchemaList(currentUsername, isAdmin, param, testDataId);
        
    }

    /**
     * testDataSchemaValuesListPage
     *
     * @param page
     * @param limit
     * @param param
     * @param testDataId
     * @return
     */
    @RequestMapping("/testDataSchemaValuesListPage")
    @ResponseBody
    public String testDataSchemaValuesListPage(Integer page, Integer limit, String param, String testDataId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return testDataServiceImpl.getTestDataSchemaValuesCustomListPage(currentUsername, isAdmin, page, limit, param, testDataId);
    }

    /**
     * testDataSchemaValuesList
     *
     * @param param
     * @param testDataId
     * @return
     */
    @RequestMapping("/testDataSchemaValuesList")
    @ResponseBody
    public String testDataSchemaValuesList(String param, String testDataId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
    	return testDataServiceImpl.getTestDataSchemaValuesCustomList(currentUsername, isAdmin, param, testDataId);
        
    }





}
