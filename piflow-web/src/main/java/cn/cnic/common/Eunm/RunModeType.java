package cn.cnic.common.Eunm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cn.cnic.base.TextureEnumSerializer;

@JsonSerialize(using = TextureEnumSerializer.class)
public enum RunModeType {
    DEBUG("DEBUG", "DEBUG", "debug"),
    RUN("RUN", "RUN", "run");

    private final String value;
    private final String text;
    private final String desc;

    private RunModeType(String text, String value, String desc) {
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

    public static RunModeType selectGender(String name) {
        for (RunModeType portType : RunModeType.values()) {
            if (name.equalsIgnoreCase(portType.name())) {
                return portType;
            }
        }
        return null;
    }

    public static RunModeType selectGenderByValue(String value) {
        for (RunModeType portType : RunModeType.values()) {
            if (value.equalsIgnoreCase(portType.value)) {
                return portType;
            }
        }
        return null;
    }
}
