package cn.cnic.component.testData.mapper;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.cnic.component.testData.entity.TestDataSchema;
import cn.cnic.component.testData.mapper.provider.TestDataSchemaMapperProvider;
import cn.cnic.component.testData.vo.TestDataSchemaVo;

@Mapper
public interface TestDataSchemaMapper {

    /**
     * add TestDataSchema
     * 
     * @param testDataSchema
     * @return Integer
     */
    @InsertProvider(type = TestDataSchemaMapperProvider.class, method = "addTestDataSchema")
    public Integer addTestDataSchema(TestDataSchema testDataSchema);

    /**
     * add TestDataSchema List
     * 
     * @param testDataSchemaList
     * @return
     */
    @InsertProvider(type = TestDataSchemaMapperProvider.class, method = "addTestDataSchemaList")    
    public Integer addTestDataSchemaList(List<TestDataSchema> testDataSchemaList);

    /**
     * update TestDataSchema
     * 
     * @param testDataSchema
     * @return
     */
    @UpdateProvider(type = TestDataSchemaMapperProvider.class, method = "updateTestDataSchema")
    public Integer updateTestDataSchema(TestDataSchema testDataSchema);

    /**
     * update TestDataSchema enable_flag
     * 
     * @param isAdmin
     * @param username
     * @param testDataId
     * @return
     */
    @UpdateProvider(type = TestDataSchemaMapperProvider.class,method = "delTestDataSchemaByTestDataId")
    public Integer delTestDataSchemaByTestDataId(@Param("isAdmin") boolean isAdmin, @Param("username") String username, @Param("testDataId") String testDataId);

    /**
     * update TestDataSchema enable_flag
     *
     * @param isAdmin
     * @param username
     * @param schemaId
     * @return
     */
    @UpdateProvider(type = TestDataSchemaMapperProvider.class,method = "delTestDataSchemaById")
    public Integer delTestDataSchemaById(@Param("isAdmin") boolean isAdmin, @Param("username") String username, @Param("schemaId") String schemaId);

    /**
     * get TestDataSchema by id
     * 
     * @param id
     * @return
     */
    @SelectProvider(type = TestDataSchemaMapperProvider.class,method = "getTestDataSchemaById")
    public TestDataSchema getTestDataSchemaById(String id);

    /**
     * get TestDataSchema list by testDataId
     * 
     * @param testDataId
     * @return
     */
    @Select("SELECT * FROM test_data_schema WHERE enable_flag=1 AND fk_test_data_id=#{testDataId} ORDER BY  field_soft ASC ")
    @Results({
        @Result(id = true, column = "id", property = "id"),
        //@Result(column = "id", property = "schemaValuesList", many = @Many(select = "cn.cnic.component.testData.mapper.TestDataSchemaValuesMapper.getTestDataSchemaValuesListBySchemaId", fetchType = FetchType.LAZY)) 
    })
    public List<TestDataSchema> getTestDataSchemaListByTestDataId(String testDataId);
    
    /**
     * get TestDataSchema list by testDataId page
     * 
     * @param isAdmin
     * @param username
     * @param param
     * @param testDataId
     * @return
     */
    @SelectProvider(type = TestDataSchemaMapperProvider.class,method = "getTestDataSchemaListByTestDataIdSearch")
    public List<TestDataSchema> getTestDataSchemaListByTestDataIdSearch(boolean isAdmin, String username, String param, String testDataId);

    /**
     * get TestDataSchemaVo list by testDataId search
     * 
     * @param isAdmin
     * @param username
     * @param param
     * @param testDataId
     * @return
     */
    @SelectProvider(type = TestDataSchemaMapperProvider.class,method = "getTestDataSchemaListByTestDataIdSearch")
    public List<TestDataSchemaVo> getTestDataSchemaVoListByTestDataIdSearch(boolean isAdmin, String username, String param, String testDataId);
    
    /**
     * get TestDataSchemaId and NameList by testDataId
     * 
     * @param testDataId
     * @return List<Map<String,String>> key1=ID key2=FIELD_NAME
     */
    @Select("SELECT TDS.id AS ID,TDS.field_name as FIELD_NAME FROM test_data_schema TDS WHERE TDS.enable_flag=1 AND TDS.fk_test_data_id=#{testDataId} ORDER BY TDS.field_soft ASC ")
    public List<LinkedHashMap<String,Object>> getTestDataSchemaIdAndNameListByTestDataId(String testDataId);



}
