package cn.cnic.controller;

import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.testData.service.ITestDataService;
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
        String currentUsername = "admin";
        boolean isAdmin = true;
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
        //String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        String currentUsername = "admin";
        boolean isAdmin = true;
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
        //String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        String currentUsername = "admin";
        boolean isAdmin = true;
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
        //String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        String currentUsername = "admin";
        boolean isAdmin = true;
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
        //String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        String currentUsername = "admin";
        boolean isAdmin = true;
        return testDataServiceImpl.getTestDataSchemaValuesCustomList(currentUsername, isAdmin, param, testDataId);
    }

    /**
     * testDataSchemaValuesListPage
     *
     * @param testDataId
     * @return
     */
    @RequestMapping("/saveOrUpdateTestDataSchema")
    @ResponseBody
    public String saveOrUpdateTestDataSchema(String param, String testDataId) {
        //String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        String currentUsername = "admin";
        boolean isAdmin = true;
        return testDataServiceImpl.saveOrUpdateTestDataSchema(currentUsername,"", "", "");
    }

    @RequestMapping("/saveOrUpdateTestDataSchemaValues")
    @ResponseBody
    public String saveOrUpdateTestDataSchemaValues(String testDataId) {
        //String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        String currentUsername = "admin";
        boolean isAdmin = true;
        return testDataServiceImpl.saveOrUpdateTestDataSchemaValues(currentUsername,"", "", "");
    }

    @RequestMapping("/delTestData")
    @ResponseBody
    public String delTestData(String testDataId) {
        //String currentUsername = SessionUserUtil.getCurrentUsername();
        //boolean isAdmin = SessionUserUtil.isAdmin();
        String currentUsername = "admin";
        boolean isAdmin = true;
        return testDataServiceImpl.delTestData(currentUsername, isAdmin, testDataId);
    }
}
