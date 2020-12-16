package cn.cnic.component.testData.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.UpdateProvider;

import cn.cnic.component.testData.entity.TestDataList;
import cn.cnic.component.testData.mapper.provider.TestDataListMapperProvider;

@Mapper
public interface TestDataListMapper {

	@InsertProvider(type = TestDataListMapperProvider.class, method = "addTestDataList")
	public Integer addTestDataList(TestDataList testDataList);

	@InsertProvider(type = TestDataListMapperProvider.class, method = "addTestDataListList")	
	public Integer addTestDataListList(List<TestDataList> testDataListList);

	@UpdateProvider(type = TestDataListMapperProvider.class, method = "updeateTestDataList")
	public Integer updeateTestDataList(TestDataList testDataList);

}
