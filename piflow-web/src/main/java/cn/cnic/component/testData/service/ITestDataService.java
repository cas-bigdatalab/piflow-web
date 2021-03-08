package cn.cnic.component.testData.service;

import cn.cnic.component.testData.vo.TestDataSchemaVo;
import cn.cnic.component.testData.vo.TestDataVo;

public interface ITestDataService {

    /**
     * saveOrUpdateTestDataSchema
     *
     * @param username
     * @param isAdmin
     * @param testDataVo
     * @return String
     */
    public String saveOrUpdateTestDataAndSchema(String username, boolean isAdmin, TestDataVo testDataVo);


    /**
     * saveOrUpdateTestDataSchemaValues
     *
     * @param username
     * @param isAdmin
     * @param testDataSchemaVo
     * @return String
     */
    public String saveOrUpdateTestDataSchemaValues(String username, boolean isAdmin, TestDataSchemaVo testDataSchemaVo);

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


}
