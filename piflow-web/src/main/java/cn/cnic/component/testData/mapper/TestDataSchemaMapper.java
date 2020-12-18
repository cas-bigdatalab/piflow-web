package cn.cnic.component.testData.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.cnic.component.testData.entity.TestDataSchema;
import cn.cnic.component.testData.mapper.provider.TestDataSchemaMapperProvider;

@Mapper
public interface TestDataSchemaMapper {

	@InsertProvider(type = TestDataSchemaMapperProvider.class, method = "addTestDataSchema")
	public Integer addTestDataSchema(TestDataSchema testDataSchema);

	@InsertProvider(type = TestDataSchemaMapperProvider.class, method = "addTestDataSchemaList")	
	public Integer addTestDataSchemaList(List<TestDataSchema> testDataSchemaList);

	@UpdateProvider(type = TestDataSchemaMapperProvider.class, method = "updateTestDataSchema")
	public Integer updateTestDataSchema(TestDataSchema testDataSchema);
	
	@Select("select * from test_data_schema where enable_flag=1 and id=#{id} ")
	public TestDataSchema getTestDataSchemaById(String id);

}
