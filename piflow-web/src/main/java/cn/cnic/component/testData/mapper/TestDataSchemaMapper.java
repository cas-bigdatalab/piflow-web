package cn.cnic.component.testData.mapper;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.cnic.component.testData.entity.TestDataSchema;
import cn.cnic.component.testData.mapper.provider.TestDataSchemaMapperProvider;
import cn.cnic.component.testData.vo.TestDataSchemaVo;

@Mapper
public interface TestDataSchemaMapper {

	@InsertProvider(type = TestDataSchemaMapperProvider.class, method = "addTestDataSchema")
	public Integer addTestDataSchema(TestDataSchema testDataSchema);

	@InsertProvider(type = TestDataSchemaMapperProvider.class, method = "addTestDataSchemaList")	
	public Integer addTestDataSchemaList(List<TestDataSchema> testDataSchemaList);

	@UpdateProvider(type = TestDataSchemaMapperProvider.class, method = "updateTestDataSchema")
	public Integer updateTestDataSchema(TestDataSchema testDataSchema);

	@UpdateProvider(type = TestDataSchemaMapperProvider.class,method = "delTestDataSchemaByTestDataId")
	public Integer delTestDataSchemaByTestDataId(@Param("isAdmin") boolean isAdmin, @Param("username") String username, @Param("testDataId") String testDataId);

	@SelectProvider(type = TestDataSchemaMapperProvider.class,method = "getTestDataSchemaById")
	public TestDataSchema getTestDataSchemaById(String id);

	@SelectProvider(type = TestDataSchemaMapperProvider.class,method = "getTestDataSchemaListByTestDataId")
	public List<TestDataSchema> getTestDataSchemaListByTestDataId(boolean isAdmin, String username, String param, String testDataId);
	

	

	@SelectProvider(type = TestDataSchemaMapperProvider.class,method = "getTestDataSchemaListByTestDataId")
	public List<TestDataSchemaVo> getTestDataSchemaVoListByTestDataId(boolean isAdmin, String username, String param, String testDataId);
	
	@Select("SELECT TDS.id,TDS.field_name FROM test_data_schema TDS WHERE TDS.enable_flag=1 AND TDS.fk_test_data_id=#{testDataId} ORDER BY TDS.field_soft ASC ")
	public List<LinkedHashMap<String,String>> getTestDataSchemaIdAndNameListByTestDataId(String testDataId);



}
