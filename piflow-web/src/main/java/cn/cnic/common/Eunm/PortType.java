package cn.cnic.common.Eunm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cn.cnic.base.TextureEnumSerializer;
import org.apache.commons.lang3.StringUtils;

@JsonSerialize(using = TextureEnumSerializer.class)
public enum PortType {
    ANY("Any", "Any", "Any quantity"),
    DEFAULT("Default", "Default", "Default number 1"),
    USER_DEFAULT("UserDefault", "UserDefault", " 'stop' defines ports"),
    NONE("None", "None", "prohibit"),
    ROUTE("Route", "Route", "Routing port");

    private final String value;
    private final String text;
    private final String desc;

    private PortType(String text, String value, String desc) {
        this.text = text;
        this.value = value;
        this.desc = desc;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static PortType selectGender(String name) {
        if (StringUtils.isBlank(name)) {
            return PortType.DEFAULT;
        }
        for (PortType portType : PortType.values()) {
            if (name.equalsIgnoreCase(portType.name())) {
                return portType;
            }
        }
        return null;
    }

    public static PortType selectGenderByValue(String value) {
        if (StringUtils.isBlank(value)) {
            return PortType.DEFAULT;
        }
        for (PortType portType : PortType.values()) {
            if (value.equalsIgnoreCase(portType.value)) {
                return portType;
            }
        }
        return null;
    }
}
