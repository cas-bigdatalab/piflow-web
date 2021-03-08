package cn.cnic.controller;
import cn.cnic.component.testData.service.ITestDataService;
import cn.cnic.component.testData.vo.TestDataSchemaVo;
import cn.cnic.component.testData.vo.TestDataVo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
     */
    @RequestMapping("/saveOrUpdateTestDataSchema")
    @ResponseBody
    public String saveOrUpdateTestDataSchema(TestDataVo testDataVo) {
        //String currentUsername = SessionUserUtil.getCurrentUsername();
        //return testDataServiceImpl.saveOrUpdateTestDataSchema(currentUsername,"", "", "");
        return testDataServiceImpl.saveOrUpdateTestDataAndSchema("admin", true, testDataVo);
    }
    
    @RequestMapping("/delTestData")
    @ResponseBody
    public String delTestData(String testDataId) {
        //String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        //return testDataServiceImpl.delTestData(currentUsername, isAdmin, testDataId);
    	return testDataServiceImpl.delTestData("admin", true, testDataId);
        
    }
    
    /**
     * saveOrUpdateTestDataSchemaValues
     * 
     * @param testDataSchemaVo
     * @return String
     */
    @RequestMapping("/saveOrUpdateTestDataSchemaValues")
    @ResponseBody
    public String saveOrUpdateTestDataSchemaValues(TestDataSchemaVo testDataSchemaVo) {
        //String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        //return testDataServiceImpl.saveOrUpdateTestDataSchemaValues(currentUsername,"", "", "");
    	return testDataServiceImpl.saveOrUpdateTestDataSchemaValues("admin", true, testDataSchemaVo);
    }
    
    /**
     * "testData" list Pagination
     *
     * @param page
     * @param limit
     * @param param
     * @return
     */
    @RequestMapping("/testDataLLstPage")
    @ResponseBody
    public String testDataListPage(Integer page, Integer limit, String param) {
        //String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        //return testDataServiceImpl.getTestDataListPage(currentUsername, isAdmin, page, limit, param);
        return testDataServiceImpl.getTestDataListPage("admin", true, page, limit, param);
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
        //String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        //return testDataServiceImpl.getTestDataSchemaListPage(currentUsername, isAdmin, page, limit, param, testDataId);
        return testDataServiceImpl.getTestDataSchemaListPage("admin", true, page, limit, param, testDataId);
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
        //String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        //return testDataServiceImpl.getTestDataSchemaList(currentUsername, isAdmin, param, testDataId);
        return testDataServiceImpl.getTestDataSchemaList("admin", true, param, testDataId);
        
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
        //String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        //return testDataServiceImpl.getTestDataSchemaValuesCustomListPage(currentUsername, isAdmin, page, limit, param, testDataId);
        return testDataServiceImpl.getTestDataSchemaValuesCustomListPage("admin", true, page, limit, param, testDataId);
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
        //String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        //return testDataServiceImpl.getTestDataSchemaValuesCustomList(currentUsername, isAdmin, param, testDataId);
    	return testDataServiceImpl.getTestDataSchemaValuesCustomList("admin", true, param, testDataId);
        
    }





}
