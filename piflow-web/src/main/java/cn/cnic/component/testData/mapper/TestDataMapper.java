package cn.cnic.component.testData.mapper;

import cn.cnic.component.testData.entity.TestData;
import cn.cnic.component.testData.mapper.provider.TestDataMapperProvider;
import cn.cnic.component.testData.vo.TestDataVo;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface TestDataMapper {

    /**
     * add TestData
     * 
     * @param testData
     * @return Integer
     */
    @InsertProvider(type = TestDataMapperProvider.class, method = "addTestData")
    public Integer addTestData(TestData testData);

    /**
     * update TestData
     * 
     * @param testData
     * @return Integer
     */
    @UpdateProvider(type = TestDataMapperProvider.class, method = "updateTestData")
    public Integer updateTestData(TestData testData);

    /**
     * update TestData enable_flag
     * 
     * @param isAdmin
     * @param username
     * @param id
     * @return Integer
     */
    @UpdateProvider(type = TestDataMapperProvider.class,method = "delTestDataById")
    public Integer delTestDataById(@Param("isAdmin") boolean isAdmin, @Param("username") String username, @Param("id") String id);

    /**
     * get TestData by id
     * 
     * @param id
     * @return TestData
     */
    @Select("select * from test_data where enable_flag=1 and id=#{id} ")
    @Results({
        @Result(id = true, column = "id", property = "id"),
        @Result(column = "id", property = "schemaList", many = @Many(select = "cn.cnic.component.testData.mapper.TestDataSchemaMapper.getTestDataSchemaListByTestDataId", fetchType = FetchType.LAZY)),
        @Result(column = "id", property = "schemaValuesList", many = @Many(select = "cn.cnic.component.testData.mapper.TestDataSchemaValuesMapper.getTestDataSchemaValuesListByTestDataId", fetchType = FetchType.LAZY))
    })
    public TestData getTestDataById(String id);
    
    /**
     * get TestData by id, Do not perform related queries
     * 
     * @param id
     * @return TestData
     */
    @Select("select * from test_data where enable_flag=1 and id=#{id} ")
    public TestData getTestDataByIdOnly(String id);
    
    /**
     * get TestDataVo by id
     * 
     * @param id
     * @return TestDataVo
     */
    @Select("select * from test_data where enable_flag=1 and id=#{id} ")
    public TestDataVo getTestDataVoById(String id);
    
    /**
     * search TestData List
     * 
     * @param isAdmin
     * @param username
     * @param param
     * @return
     */
    @SelectProvider(type = TestDataMapperProvider.class,method = "getTestDataList")
    public List<TestData> getTestDataList(boolean isAdmin, String username, String param);
    
    /**
     * search TestDataVo List
     * 
     * @param isAdmin
     * @param username
     * @param param
     * @return
     */
    @SelectProvider(type = TestDataMapperProvider.class,method = "getTestDataList")
    public List<TestDataVo> getTestDataVoList(boolean isAdmin, String username, String param);

    /**
     * get TestDataVo by id
     *
     * @param testDataName
     * @return TestDataVo
     */
    @Select("SELECT name FROM test_data WHERE enable_flag=1 and name=#{testDataName} ")
    public String getTestDataName(String testDataName);

}
