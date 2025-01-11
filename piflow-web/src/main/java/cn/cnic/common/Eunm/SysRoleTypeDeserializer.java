package cn.cnic.common.Eunm;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SysRoleTypeDeserializer extends JsonDeserializer<SysRoleType> {

    @Override
    public SysRoleType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String textValue = node.get("text").asText();
        try {
            // 尝试根据文本值找到对应的枚举
            return SysRoleType.valueOf(textValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 如果根据文本值找不到，则尝试根据"stringValue"字段反序列化
            JsonNode valueNode = node.get("stringValue");
            if (valueNode != null) {
                String value = valueNode.asText();
                return SysRoleType.selectGenderByValue(value.toUpperCase());
            }
            throw ctxt.weirdStringException(textValue, SysRoleType.class,
                    String.format("无法将'%s'反序列化为SysRoleType，因为它既不是有效的枚举名称也不是已知的'value'", textValue));
        }
    }
}