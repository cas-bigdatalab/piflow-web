package cn.cnic.component.testData.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
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

}
