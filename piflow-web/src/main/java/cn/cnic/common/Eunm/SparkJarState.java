package cn.cnic.common.Eunm;

import cn.cnic.base.TextureEnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = TextureEnumSerializer.class)
public enum SparkJarState {
    MOUNT("MOUNT", "MOUNT"),
    UNMOUNT("UNMOUNT", "UNMOUNT");

    private final String value;
    private final String text;

    private SparkJarState(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    public static SparkJarState selectGender(String name) {
        for (SparkJarState portType : SparkJarState.values()) {
            if (name.equalsIgnoreCase(portType.name())) {
                return portType;
            }
        }
        return null;
    }
}
