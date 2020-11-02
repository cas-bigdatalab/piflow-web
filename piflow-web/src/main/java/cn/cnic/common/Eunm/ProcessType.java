package cn.cnic.common.Eunm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cn.cnic.base.TextureEnumSerializer;

@JsonSerialize(using = TextureEnumSerializer.class)
public enum ProcessType {
    GROUP("GROUP", "GROUP", "group"),
    TASK("TASK", "TASK", "group");

    private final String value;
    private final String text;
    private final String desc;

    private ProcessType(String text, String value, String desc) {
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

    public static ProcessType selectGender(String name) {
        for (ProcessType portType : ProcessType.values()) {
            if (name.equalsIgnoreCase(portType.name())) {
                return portType;
            }
        }
        return null;
    }

    public static ProcessType selectGenderByValue(String value) {
        for (ProcessType portType : ProcessType.values()) {
            if (value.equalsIgnoreCase(portType.value)) {
                return portType;
            }
        }
        return null;
    }
}
