package cn.cnic.base;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import cn.cnic.base.utils.LoggerUtil;

import java.io.IOException;

import org.slf4j.Logger;


public class TextureEnumSerializer extends JsonSerializer<Object> {

	/**
     * Introducing logs, note that they are all packaged under "org.slf4j"
     */
    private Logger logger = LoggerUtil.getLogger();

    @Override
    public void serialize(Object value, JsonGenerator generator, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        if (value == null) {
            return;
        }
        generator.writeStartObject();
        generator.writeFieldName("stringValue");
        generator.writeString(value.toString());
        generator.writeFieldName("text");
        try {
            generator.writeString(org.apache.commons.beanutils.BeanUtils.getProperty(value, "text"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        generator.writeEndObject();
    }
}