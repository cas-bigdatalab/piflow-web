package cn.cnic.component.testData.vo;

import java.util.LinkedHashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestDataSchemaValuesSaveVo {

	private String testDataId;
	private List<LinkedHashMap<String,String>> schemaValuesList;
	private List<LinkedHashMap<String,String>> schemaValuesIdList;
}
