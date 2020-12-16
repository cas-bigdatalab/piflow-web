package cn.cnic.base.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Json utils
 */
@SuppressWarnings("deprecation")
public class JsonUtils {

    private static Logger logger = LoggerUtil.getLogger();

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        // Remove the default timestamp format
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // Set to Shanghai time zone in China
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        // Null value not serialized
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        // Compatible processing when attributes are not present during deserialization
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Uniform format of dates when serializing
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // It is forbidden to deserialize "Enum" with "int" on behalf of "Enum"
        objectMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        // objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY,
        // true);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // Single quote processing
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

    public static <T> String toFormatJsonNoException(T entity) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(entity);
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
     * Object to "json" string
     *
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    public static String toString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    /**
     * "json" string to object
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
     * When converting "JSON" when using "Jackson", the date format setting "Jackson" two methods set the date format of the output
     * <p>
     * 1. Ordinary way: The default is to convert to "timestamps" form, you can cancel "timestamps" by the following way.
     * objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,
     * false); This will cause the time generation to use the so-called use a [ISO-8601]-compliant notation, which outputs a time similar to the following:
     * "1970-01-01T00:00:00.000+0000". Of course, you can also customize the output format:
     * objectMapper.getSerializationConfig().setDateFormat(myDateFormat);
     * The myDateFormat object is java.text.DateFormat, which uses the annotation method of checking java API 2.annotaion:
     * First define the format you need as follows
     * <p>
     * Then find the date get method on your POJO
     *
     * @JsonSerialize(using = CustomDateSerializer.class) public Date getCreateAt()
     * { return createAt; }
     * <p>
     * "java" date object converted to "JSON" date formatted custom class via "Jackson" library
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
