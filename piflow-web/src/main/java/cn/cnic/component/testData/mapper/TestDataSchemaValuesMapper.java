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

	@InsertProvider(type = TestDataSchemaValuesMapperProvider.class, method = "addTestDataSchemaValues")
	public Integer addTestDataSchemaValues(TestDataSchemaValues testDataSchemaValues);

	@InsertProvider(type = TestDataSchemaValuesMapperProvider.class, method = "addTestDataSchemaValuesList")
	public Integer addTestDataSchemaValuesList(List<TestDataSchemaValues> testDataSchemaValuesList);

	@UpdateProvider(type = TestDataSchemaValuesMapperProvider.class, method = "updateTestDataSchemaValues")
	public Integer updateTestDataSchemaValues(TestDataSchemaValues testDataSchemaValues);

	@Select("select * from test_data_list where enable_flag=1 order by data_row asc ")
	@Results({
			@Result(column = "fk_test_data_schema_id", property = "testDataSchema", one = @One(select = "cn.cnic.component.testData.mapper.TestDataSchemaMapper.getTestDataSchemaById", fetchType = FetchType.LAZY)) 
	})
	public List<TestDataSchemaValues> getTestDataSchemaValuesList();
	
	@Select("select * from test_data_list where enable_flag=1 and id=#{id} order by data_row asc ")
	@Results({
			@Result(column = "fk_test_data_schema_id", property = "testDataSchema", one = @One(select = "cn.cnic.component.testData.mapper.TestDataSchemaMapper.getTestDataSchemaById", fetchType = FetchType.LAZY)) 
	})
	public TestDataSchemaValues getTestDataSchemaValuesById(String id);

	@SelectProvider(type = TestDataSchemaValuesMapperProvider.class, method = "getTestDataSchemaValuesCustomList")
	public List<LinkedHashMap<String, String>> getTestDataSchemaValuesCustomList(@Param("isAdmin") boolean isAdmin, @Param("username") String username, @Param("testDataId") String testDataId, @Param("fieldNameList") List<LinkedHashMap<String, String>> map);

	@UpdateProvider(type = TestDataSchemaValuesMapperProvider.class,method = "delTestDataSchemaValuesByTestDataId")
	public Integer delTestDataSchemaValuesByTestDataId(@Param("isAdmin") boolean isAdmin, @Param("username") String username, @Param("testDataId") String testDataId);
	
	@SelectProvider(type = TestDataSchemaValuesMapperProvider.class, method = "getTestDataSchemaValuesCustomListId")
	public List<LinkedHashMap<String, String>> getTestDataSchemaValuesCustomListId(@Param("isAdmin") boolean isAdmin, @Param("username") String username, @Param("testDataId") String testDataId, @Param("fieldNameList") List<LinkedHashMap<String, String>> map);

}
