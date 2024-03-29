package cn.cnic.controller.api.other;

import cn.cnic.component.system.service.ILogHelperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.cnic.base.utils.SessionUserUtil;
import cn.cnic.component.testData.service.ITestDataService;
import cn.cnic.controller.requestVo.TestDataSchemaValuesSaveVo;
import cn.cnic.controller.requestVo.TestDataVoRequest;

@Api(value = "testData api", tags = "testData api")
@RestController
@RequestMapping(value = "/testData")
public class TestDataCtrl {

    private final ITestDataService testDataServiceImpl;
    private final ILogHelperService logHelperServiceImpl;

    @Autowired
    public TestDataCtrl(ITestDataService testDataServiceImpl,
                        ILogHelperService logHelperServiceImpl) {
        this.testDataServiceImpl = testDataServiceImpl;
        this.logHelperServiceImpl = logHelperServiceImpl;
    }

    /**
     * saveOrUpdateTestDataSchema
     *
     * @param testDataVo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveOrUpdateTestDataSchema", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="saveOrUpdateTestDataSchema", notes="save or update TestDataSchema")
    public String saveOrUpdateTestDataSchema(TestDataVoRequest testDataVo) throws Exception {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return testDataServiceImpl.saveOrUpdateTestDataAndSchema(currentUsername, isAdmin, testDataVo, false);
    }

    /**
     * delTestData
     *
     * @param testDataName
     * @return
     */
    @RequestMapping(value = "/checkTestDataName", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="checkTestDataName", notes="check TestData name")
    @ApiImplicitParam(name="testDataName", value = "testDataName", required = true)
    public String checkTestDataName(String testDataName) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return testDataServiceImpl.checkTestDataName(currentUsername, isAdmin, testDataName);
    }

    /**
     * delTestData
     *
     * @param testDataId
     * @return
     */
    @RequestMapping(value = "/delTestData", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="delTestData", notes="delete TestData")
    @ApiImplicitParam(name="testDataId", value = "testDataId", required = true)
    public String delTestData(String testDataId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelperServiceImpl.logAuthSucceed("delTestData " + testDataId,currentUsername);
        return testDataServiceImpl.delTestData(currentUsername, isAdmin, testDataId);
    }

    /**
     * saveOrUpdateTestDataSchemaValues
     * 
     * @param schemaValuesVo
     * @return String
     * @throws Exception 
     */
    @RequestMapping(value = "/saveOrUpdateTestDataSchemaValues", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="saveOrUpdateTestDataSchemaValues", notes="save or update TestDataSchemaValues")
    public String saveOrUpdateTestDataSchemaValues(TestDataSchemaValuesSaveVo schemaValuesVo) throws Exception {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        logHelperServiceImpl.logAuthSucceed("saveOrUpdateTestDataSchemaValues " + schemaValuesVo.getTestDataId(),currentUsername);
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
    @RequestMapping(value = "/testDataListPage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="testDataListPage", notes="get TestData list page")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="page", value = "page", required = true),
    	@ApiImplicitParam(name="limit", value = "limit", required = true),
    	@ApiImplicitParam(name="param", value = "param", required = false)
    })
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
    @RequestMapping(value = "/testDataSchemaListPage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="testDataSchemaListPage", notes="get TestDataSchema list page")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="testDataId", value = "testDataId", required = true),
    	@ApiImplicitParam(name="page", value = "page", required = true),
    	@ApiImplicitParam(name="limit", value = "limit", required = true),
    	@ApiImplicitParam(name="param", value = "param", required = false)
    })
    public String testDataSchemaLListPage(String testDataId, Integer page, Integer limit, String param) {
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
    @RequestMapping(value = "/testDataSchemaList", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="testDataSchemaList", notes="get TestDataSchema list")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="testDataId", value = "testDataId", required = true),
    	@ApiImplicitParam(name="param", value = "param", required = false)
    })
    public String testDataSchemaList(String testDataId, String param) {
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
    @RequestMapping(value = "/testDataSchemaValuesListPage", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="testDataSchemaValuesListPage", notes="get TestDataSchemaValues list page")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="testDataId", value = "testDataId", required = true),
    	@ApiImplicitParam(name="page", value = "page", required = true),
    	@ApiImplicitParam(name="limit", value = "limit", required = true),
    	@ApiImplicitParam(name="param", value = "param", required = false)
    })
    public String testDataSchemaValuesListPage(String testDataId, Integer page, Integer limit, String param) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return testDataServiceImpl.getTestDataSchemaValuesCustomListPage(currentUsername, isAdmin, page, limit, param,
                testDataId);
    }

    /**
     * testDataSchemaValuesList
     *
     * @param param
     * @param testDataId
     * @return
     */
    @RequestMapping(value = "/testDataSchemaValuesList", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="testDataSchemaValuesList", notes="get TestDataSchemaValues list")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="testDataId", value = "testDataId", required = true),
    	@ApiImplicitParam(name="param", value = "param", required = false)
    })
    public String testDataSchemaValuesList(String testDataId, String param) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
        return testDataServiceImpl.getTestDataSchemaValuesCustomList(currentUsername, isAdmin, param, testDataId);

    }

    /**
     * Upload xml file and save flowTemplate
     *
     * @param testDataId
     * @param header
     * @param schema
     * @param delimiter
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploadCsvFile", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value="uploadCsvFile", notes="upload csv file")
    @ApiImplicitParams({
    	@ApiImplicitParam(name="testDataId", value = "testDataId", required = true),
    	@ApiImplicitParam(name="header", value = "header", required = true),
    	@ApiImplicitParam(name="schema", value = "schema", required = false),
    	@ApiImplicitParam(name="delimiter", value = "delimiter", required = true),
    	@ApiImplicitParam(name="file", value = "file", required = true, paramType = "formData", dataType = "file")
    })
    public String uploadCsvFile(String testDataId, boolean header, String schema, String delimiter, @RequestParam("file") MultipartFile file) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        logHelperServiceImpl.logAuthSucceed("uploadCsvFile" + file.getName(),username);
        return testDataServiceImpl.uploadCsvFile(username, testDataId, header, schema, delimiter, file);
    }

}
