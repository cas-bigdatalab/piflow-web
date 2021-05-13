package cn.cnic.controller.api;

import javax.annotation.Resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.cnic.base.util.SessionUserUtil;
import cn.cnic.component.testData.service.ITestDataService;
import cn.cnic.controller.requestVo.RequestTestDataVo;
import cn.cnic.controller.requestVo.TestDataSchemaValuesSaveVo;

@Api(value = "testData api")
@RestController
@RequestMapping(value = "/testData")
public class TestDataCtrl {

    @Resource
    private ITestDataService testDataServiceImpl;

    /**
     * saveOrUpdateTestDataSchema
     *
     * @param testDataVo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/saveOrUpdateTestDataSchema", method = RequestMethod.POST)
    @ResponseBody
    public String saveOrUpdateTestDataSchema(@ApiParam(value = "testDataVo", required = true)RequestTestDataVo testDataVo) throws Exception {
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
    public String checkTestDataName(@ApiParam(value = "testDataName", required = true)String testDataName) {
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
    public String delTestData(@ApiParam(value = "testDataId", required = true)String testDataId) {
        String currentUsername = SessionUserUtil.getCurrentUsername();
        boolean isAdmin = SessionUserUtil.isAdmin();
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
    public String saveOrUpdateTestDataSchemaValues(@ApiParam(value = "schemaValuesVo", required = true)TestDataSchemaValuesSaveVo schemaValuesVo) throws Exception {
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
    @RequestMapping(value = "/testDataListPage", method = RequestMethod.POST)
    @ResponseBody
    public String testDataListPage(@ApiParam(value = "page", required = true)Integer page,
                                   @ApiParam(value = "limit", required = true)Integer limit,
                                   @ApiParam(value = "param", required = false)String param) {
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
    public String testDataSchemaLListPage(@ApiParam(value = "page", required = true)Integer page,
                                          @ApiParam(value = "limit", required = true)Integer limit,
                                          @ApiParam(value = "param", required = false)String param,
                                          @ApiParam(value = "testDataId", required = true)String testDataId) {
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
    public String testDataSchemaList(@ApiParam(value = "param", required = false)String param,
                                     @ApiParam(value = "testDataId", required = true)String testDataId) {
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
    public String testDataSchemaValuesListPage(@ApiParam(value = "page", required = true)Integer page,
                                               @ApiParam(value = "limit", required = true)Integer limit,
                                               @ApiParam(value = "param", required = false)String param,
                                               @ApiParam(value = "testDataId", required = true)String testDataId) {
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
    public String testDataSchemaValuesList(@ApiParam(value = "param", required = false)String param,
                                           @ApiParam(value = "testDataId", required = true)String testDataId) {
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
    public String uploadCsvFile(@ApiParam(value = "testDataId", required = true)String testDataId,
                                @ApiParam(value = "header", required = true)boolean header,
                                @ApiParam(value = "schema")String schema,
                                @ApiParam(value = "delimiter", required = true)String delimiter,
                                @ApiParam(value = "file", required = true)@RequestParam("file") MultipartFile file) throws Exception {
        String username = SessionUserUtil.getCurrentUsername();
        return testDataServiceImpl.uploadCsvFile(username, testDataId, header, schema, delimiter, file);
    }

}
