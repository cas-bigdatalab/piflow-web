package cn.cnic.component.testData.mapper;

import java.util.List;
import java.util.Map;

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
	
	@Select("SELECT * FROM test_data_schema WHERE enable_flag=1 AND id=#{id} ")
	public TestDataSchema getTestDataSchemaById(String id);
	
	@Select("SELECT TDS.id,TDS.field_name FROM test_data_schema TDS WHERE TDS.enable_flag=1 AND TDS.fk_test_data_id=#{testDataId} ORDER BY TDS.field_soft ASC ")
	public List<Map<String,String>> getTestDataSchemaIdAndNameListByTestDataId(String testDataId);

}
