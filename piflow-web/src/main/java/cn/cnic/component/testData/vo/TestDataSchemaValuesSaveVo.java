package cn.cnic.component.testData.vo;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TestDataSchemaValuesSaveVo {

	private String testDataId;
	private List<Map<String,String>> schemaValuesList;
	private List<Map<String,String>> schemaValuesIdList;
}
