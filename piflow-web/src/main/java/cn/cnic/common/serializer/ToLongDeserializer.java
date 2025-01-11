package cn.cnic.common.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;

/**
 * @projectName: piflow-web
 * @package: cn.cnic.common.serializer
 * @className: ToLongDeserializer
 * @author: tianyao
 * @description: TODO
 * @date: 2024/2/20 10:40
 * @version: 1.0
 */
public class ToLongDeserializer extends JsonDeserializer {
    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (ObjectUtils.isEmpty(jsonParser)) {
            return null;
        }
        return Long.parseLong(jsonParser.getText());
    }
}
