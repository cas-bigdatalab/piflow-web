package cn.cnic.component.testData.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

import cn.cnic.component.testData.vo.TestDataSchemaValuesSaveVo;
import net.sf.json.JSONObject;

public class TestDataSchemaValuesSaveVoUtils {

	public static TestDataSchemaValuesSaveVo StringToTestDataSchemaValuesSaveVo2(String json) {
		if (StringUtils.isBlank(json)) {
			return null;
		}
        // Also convert the json string to a json object, and then convert the json object to a java object, as shown below.
        JSONObject obj = JSONObject.fromObject(json);// Convert a json string to a json object
        // Needed when there is a List in jsonObj
        @SuppressWarnings("rawtypes")
		Map<String, Class> classMap = new HashMap<String, Class>();
        // Key is the name of the List in jsonObj, and the value is a generic class of list
        classMap.put("schemaValuesList", LinkedHashMap.class);
        classMap.put("schemaValuesIdList", LinkedHashMap.class);
        // Convert a json object to a java object
        Object bean = JSONObject.toBean(obj, TestDataSchemaValuesSaveVo.class, classMap);
        TestDataSchemaValuesSaveVo jb = (TestDataSchemaValuesSaveVo) bean;
		return jb;
	}
	
	@SuppressWarnings("unchecked")
	public static TestDataSchemaValuesSaveVo stringToTestDataSchemaValuesSaveVo(String json) {
		Map<String,Object> mapTypes = JSON.parseObject(json);
		if(null == mapTypes) {
			return null;
		}
		TestDataSchemaValuesSaveVo testDataSchemaValuesSaveVo = new TestDataSchemaValuesSaveVo();
		Object testDataIdObj = mapTypes.get("testDataId");
		if(null != testDataIdObj) {
			testDataSchemaValuesSaveVo.setTestDataId(testDataIdObj.toString());
		}
		Object schemaValuesListObj = mapTypes.get("schemaValuesList");
		if(null != schemaValuesListObj) {
			testDataSchemaValuesSaveVo.setSchemaValuesList((List<LinkedHashMap<String,String>>)schemaValuesListObj);
		}
		Object schemaValuesIdListObj = mapTypes.get("schemaValuesIdList");
		if(null != schemaValuesIdListObj) {
			testDataSchemaValuesSaveVo.setSchemaValuesIdList((List<LinkedHashMap<String,String>>) schemaValuesIdListObj);
		}
		return testDataSchemaValuesSaveVo;
	}
}
