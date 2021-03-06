package cn.cnic.component.testData.mapper;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.FetchType;

import cn.cnic.component.testData.entity.TestDataSchemaValues;
import cn.cnic.component.testData.mapper.provider.TestDataSchemaValuesMapperProvider;

@Mapper
public interface TestDataSchemaValuesMapper {

	/**
	 * add TestDataSchemaValues
	 * 
	 * @param testDataSchemaValues
	 * @return
	 */
	@InsertProvider(type = TestDataSchemaValuesMapperProvider.class, method = "addTestDataSchemaValues")
	public Integer addTestDataSchemaValues(TestDataSchemaValues testDataSchemaValues);

	/**
	 * add TestDataSchemaValues list
	 * 
	 * @param testDataSchemaValuesList
	 * @return
	 */
	@InsertProvider(type = TestDataSchemaValuesMapperProvider.class, method = "addTestDataSchemaValuesList")
	public Integer addTestDataSchemaValuesList(List<TestDataSchemaValues> testDataSchemaValuesList);

	/**
	 * update TestDataSchemaValues
	 * 
	 * @param testDataSchemaValues
	 * @return
	 */
	@UpdateProvider(type = TestDataSchemaValuesMapperProvider.class, method = "updateTestDataSchemaValues")
	public Integer updateTestDataSchemaValues(TestDataSchemaValues testDataSchemaValues);

	/**
	 * update TestDataSchemaValues enable_flag
	 * 
	 * @param isAdmin
	 * @param username
	 * @param testDataId
	 * @return
	 */
	@UpdateProvider(type = TestDataSchemaValuesMapperProvider.class,method = "delTestDataSchemaValuesByTestDataId")
	public Integer delTestDataSchemaValuesByTestDataId(@Param("isAdmin") boolean isAdmin, @Param("username") String username, @Param("testDataId") String testDataId);
	
	/**
	 * get TestDataSchemaValues list
	 * @return
	 */
	@Select("select * from test_data_list where enable_flag=1 order by data_row asc ")
	@Results({
			@Result(column = "fk_test_data_schema_id", property = "testDataSchema", one = @One(select = "cn.cnic.component.testData.mapper.TestDataSchemaMapper.getTestDataSchemaById", fetchType = FetchType.LAZY)) 
	})
	public List<TestDataSchemaValues> getTestDataSchemaValuesList();
	
	/**
	 * get TestDataSchemaValuesList by schemaId
	 * 
	 * @param schemaId
	 * @return
	 */
	@Select("select * from test_data_list where enable_flag=1 and fk_test_data_schema_id=#{schemaId} order by data_row asc ")
	@Results({
			@Result(column = "fk_test_data_schema_id", property = "testDataSchema", one = @One(select = "cn.cnic.component.testData.mapper.TestDataSchemaMapper.getTestDataSchemaById", fetchType = FetchType.LAZY)) 
	})
	public List<TestDataSchemaValues> getTestDataSchemaValuesListBySchemaId(String schemaId);
	
	@Select("select * from test_data_list where enable_flag=1 and id=#{id} order by data_row asc ")
	@Results({
			@Result(column = "fk_test_data_schema_id", property = "testDataSchema", one = @One(select = "cn.cnic.component.testData.mapper.TestDataSchemaMapper.getTestDataSchemaById", fetchType = FetchType.LAZY)) 
	})
	public TestDataSchemaValues getTestDataSchemaValuesById(String id);
	
	/**
	 * get TestDataSchemaValuesList by schemaId
	 * 
	 * @param schemaId
	 * @return
	 */
	@Select("select * from test_data_list where enable_flag=1 and fk_test_data_id=#{testDataId} order by data_row asc ")
	@Results({
			@Result(column = "fk_test_data_schema_id", property = "testDataSchema", one = @One(select = "cn.cnic.component.testData.mapper.TestDataSchemaMapper.getTestDataSchemaById", fetchType = FetchType.LAZY)) 
	})
	public List<TestDataSchemaValues> getTestDataSchemaValuesListByTestDataId(String testDataId);

	/**
	 * get TestDataSchemaValues custom list
	 * 
	 * @param isAdmin
	 * @param username
	 * @param testDataId
	 * @param map
	 * @return
	 */
	@SelectProvider(type = TestDataSchemaValuesMapperProvider.class, method = "getTestDataSchemaValuesCustomList")
	public List<LinkedHashMap<String, String>> getTestDataSchemaValuesCustomList(@Param("isAdmin") boolean isAdmin, @Param("username") String username, @Param("testDataId") String testDataId, @Param("fieldNameList") List<LinkedHashMap<String, String>> map);

	/**
	 * get testDataSchemaValuesId custom list
	 * @param isAdmin
	 * @param username
	 * @param testDataId
	 * @param map
	 * @return
	 */
	@SelectProvider(type = TestDataSchemaValuesMapperProvider.class, method = "getTestDataSchemaValuesCustomListId")
	public List<LinkedHashMap<String, String>> getTestDataSchemaValuesCustomListId(@Param("isAdmin") boolean isAdmin, @Param("username") String username, @Param("testDataId") String testDataId, @Param("fieldNameList") List<LinkedHashMap<String, String>> map);

}
