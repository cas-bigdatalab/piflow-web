package cn.cnic.component.testData.mapper;

import cn.cnic.component.testData.entity.TestData;
import cn.cnic.component.testData.mapper.provider.TestDataMapperProvider;
import cn.cnic.component.testData.vo.TestDataVo;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TestDataMapper {

	@InsertProvider(type = TestDataMapperProvider.class, method = "addTestData")
	public Integer addTestData(TestData testData);

	@UpdateProvider(type = TestDataMapperProvider.class, method = "updateTestData")
	public Integer updateTestData(TestData testData);

	@UpdateProvider(type = TestDataMapperProvider.class,method = "delTestDataById")
	public Integer delTestDataById(@Param("isAdmin") boolean isAdmin, @Param("username") String username, @Param("id") String id);

	@Select("select * from test_data where enable_flag=1 and id=#{id} ")
	public TestData getTestDataById(String id);
	
	@SelectProvider(type = TestDataMapperProvider.class,method = "getTestDataList")
	public List<TestData> getTestDataList(boolean isAdmin, String username, String param);
	
	@SelectProvider(type = TestDataMapperProvider.class,method = "getTestDataList")
	public List<TestDataVo> getTestDataVoList(boolean isAdmin, String username, String param);

}
