package cn.cnic.component.testData.service;

public interface ITestDataService {


    /**
     * add FlowTemplate
     *
     * @param username
     * @param name
     * @param loadId
     * @param templateType
     * @return
     */
    public String addTestData(String username, String name, String loadId, String templateType);
    
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
    public String getTestDataList(String username, boolean isAdmin, Integer offset, Integer limit, String param);
    
    /**
     * getTestDataSchemaList
     * 
     * @param username
     * @param isAdmin
     * @param offset
     * @param limit
     * @param param
     * @return
     */
    public String getTestDataSchemaList(String username, boolean isAdmin, Integer offset, Integer limit, String param);
    
    /**
     * getTestDataSchemaValuesCustomList
     * 
     * @param username
     * @param isAdmin
     * @param offset
     * @param limit
     * @param param
     * @return
     */
    public String getTestDataSchemaValuesCustomList(String username, boolean isAdmin, Integer offset, Integer limit, String param);


}
