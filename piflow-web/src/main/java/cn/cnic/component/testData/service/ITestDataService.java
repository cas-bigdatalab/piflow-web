package cn.cnic.component.testData.service;

public interface ITestDataService {

    /**
     * addTestData
     *
     * @param username
     * @param name
     * @param loadId
     * @param templateType
     * @return
     */
    public String addTestData(String username, String name, String loadId, String templateType);

    /**
     * saveOrUpdateTestDataSchema
     *
     * @param username
     * @param name
     * @param loadId
     * @param templateType
     * @return
     */
    public String saveOrUpdateTestDataSchema(String username, String name, String loadId, String templateType);


    /**
     * saveOrUpdateTestDataSchemaValues
     *
     * @param username
     * @param name
     * @param loadId
     * @param templateType
     * @return
     */
    public String saveOrUpdateTestDataSchemaValues(String username, String name, String loadId, String templateType);

    /**
     * delTestData
     *
     * @param username
     * @param isAdmin
     * @param testDataId
     * @return
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
     * @return
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
     * @return
     */
    public String getTestDataSchemaListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param, String testDataId);

    /**
     * getTestDataSchemaList
     *
     * @param username
     * @param isAdmin
     * @param param
     * @param testDataId
     * @return
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
     * @return
     */
    public String getTestDataSchemaValuesCustomList(String username, boolean isAdmin, String param, String testDataId);


}
