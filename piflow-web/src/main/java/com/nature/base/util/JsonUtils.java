package com.nature.base.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Json工具
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("deprecation")
public class JsonUtils {

	private static Logger logger = LoggerUtil.getLogger();

	private static final ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
		// 去掉默认的时间戳格式
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// 设置为中国上海时区
		objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		// 空值不序列化
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		// 反序列化时，属性不存在的兼容处理
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 反序列化时，属性不存在的兼容处理
		// 序列化时，日期的统一格式
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		// 禁止使用int代表Enum的order()来反序列化Enum
		objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		// objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY,
		// true);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// 单引号处理
		objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		// objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);
	}

	public static <T> T toObjectNoException(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonParseException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static <T> String toJsonNoException(T entity) {
		try {
			return objectMapper.writeValueAsString(entity);
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static <T> T toCollectionNoException(String json, TypeReference<T> typeReference) {
		try {
			return objectMapper.readValue(json, typeReference);
		} catch (JsonParseException e) {
			logger.error(e.getMessage(), e);
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 对象转json字符串
	 * 
	 * @param object
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String toString(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}

	/**
	 * json字符串转对象
	 * 
	 * @param jsonString
	 * @param rspValueType
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> T toObject(String jsonString, Class<T> rspValueType)
			throws JsonParseException, JsonMappingException, IOException {
		return objectMapper.readValue(jsonString, rspValueType);
	}

	/**
	 * 使用Jackson时转换JSON时，日期格式设置 Jackson两种方法设置输出的日期格式
	 * 
	 * 1.普通的方式： 默认是转成timestamps形式的，通过下面方式可以取消timestamps。
	 * objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,
	 * false); 这样将使时间生成使用所谓的use a [ISO-8601 ]-compliant notation, 输出类似如下格式的时间:
	 * "1970-01-01T00:00:00.000+0000". 当然也可以自定义输出格式：
	 * objectMapper.getSerializationConfig().setDateFormat(myDateFormat);
	 * myDateFormat对象为java.text.DateFormat，具体使用清查java API 2.annotaion的注释方式：
	 * 先定义自己需要的格式如下类
	 * 
	 * 然后在你的POJO上找到日期的get方法
	 * 
	 * @JsonSerialize(using = CustomDateSerializer.class) public Date getCreateAt()
	 *                      { return createAt; }
	 * 
	 *                      java日期对象经过Jackson库转换成JSON日期格式化自定义类
	 * @author godfox
	 * @date 2010-5-3
	 */
	public class CustomDateSerializer extends JsonSerializer<Date> {
		@Override
		public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = formatter.format(value);
			jgen.writeString(formattedDate);
		}
	}

	public static JsonNode readJsonNode(String jsonStr, String fieldName) {
		if (StringUtils.isEmpty(jsonStr)) {
			return null;
		}
		try {
			JsonNode root = objectMapper.readTree(jsonStr);
			return root.get(fieldName);
		} catch (IOException e) {
			logger.error("parse json string error:" + jsonStr, e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T readJson(JsonNode node, Class<?> parametrized, Class<?>... parameterClasses) throws Exception {
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
		return (T) objectMapper.readValue(toString(node), javaType);
	}

}
