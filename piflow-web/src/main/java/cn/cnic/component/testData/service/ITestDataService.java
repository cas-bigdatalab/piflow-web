package cn.cnic.component.testData.service;

import org.springframework.web.multipart.MultipartFile;

import cn.cnic.component.testData.vo.TestDataVo;
import cn.cnic.controller.requestVo.TestDataSchemaValuesSaveVo;

public interface ITestDataService {

    /**
     * saveOrUpdateTestDataSchema
     *
     * @param username
     * @param isAdmin
     * @param testDataVo
     * @return String
     * @throws Exception 
     */
    public String saveOrUpdateTestDataAndSchema(String username, boolean isAdmin, TestDataVo testDataVo) throws Exception;


    /**
     * saveOrUpdateTestDataSchemaValues
     *
     * @param username
     * @param isAdmin
     * @param schemaValuesVo
     * @return String
     * @throws Exception 
     */
    public String saveOrUpdateTestDataSchemaValues(String username, boolean isAdmin, TestDataSchemaValuesSaveVo schemaValuesVo) throws Exception;

    /**
     * checkTestDataName
     *
     * @param username
     * @param isAdmin
     * @param testDataName
     * @return String
     */

    public String checkTestDataName(String username, boolean isAdmin, String testDataName);
    /**
     * delTestData
     *
     * @param username
     * @param isAdmin
     * @param testDataId
     * @return String
     */
    public String delTestData(String username, boolean isAdmin, String testDataId);

    /**
     * getTestDataList
     * 
     * @param username
     * @param isAdmin
     * @param offset
     * @param limit
     * @param param
     * @return String
     */
    public String getTestDataListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param);
    
    /**
     * getTestDataSchemaListPage
     * 
     * @param username
     * @param isAdmin
     * @param offset
     * @param limit
     * @param param
     * @param testDataId
     * @return String
     */
    public String getTestDataSchemaListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param, String testDataId);

    /**
     * getTestDataSchemaList
     *
     * @param username
     * @param isAdmin
     * @param param
     * @param testDataId
     * @return String
     */
    public String getTestDataSchemaList(String username, boolean isAdmin, String param, String testDataId);
    
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
    public String getTestDataSchemaValuesCustomListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param, String testDataId);
    /**
     * getTestDataSchemaValuesCustomList
     *
     * @param username
     * @param isAdmin
     * @param param
     * @param testDataId
     * @return String
     */
    public String getTestDataSchemaValuesCustomList(String username, boolean isAdmin, String param, String testDataId);
    
    /**
     * Upload csv file and save flowTemplate
     *
     * @param username
     * @param file
     * @param delimiter
     * @return
     */
    public String uploadCsvFile(String username, MultipartFile file, String delimiter);


}
