package cn.cnic.component.testData.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.cnic.component.testData.entity.TestData;
import cn.cnic.component.testData.mapper.provider.TestDataMapperProvider;

@Mapper
public interface TestDataMapper {

	@InsertProvider(type = TestDataMapperProvider.class, method = "addTestData")
	public Integer addTestData(TestData testData);

	@UpdateProvider(type = TestDataMapperProvider.class, method = "updateTestData")
	public Integer updateTestData(TestData testData);
	
	@Select("select * from test_data where enable_flag=1 and id=#{id} ")
	public TestData getTestDataById(String id);
	
	@SelectProvider(type = TestDataMapperProvider.class,method = "getTestDataList")
	public List<TestData> getTestDataList(boolean isAdmin, String username, String param);

}
