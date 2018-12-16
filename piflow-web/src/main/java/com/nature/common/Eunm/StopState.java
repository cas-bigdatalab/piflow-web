package com.nature.common.Eunm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nature.base.TextureEnumSerializer;

@JsonSerialize(using = TextureEnumSerializer.class)
public enum StopState {
    INIT("INIT", "初始化"),
    STARTED("STARTED", "启动"),
    COMPLETED("COMPLETED", "完成"),
    FAILED("FAILED", "失败"),
    KILLED("KILLED","停止");

    private final String value;
    private final String text;

    private StopState(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    public static StopState selectGender(String name) {
        for (StopState portType : StopState.values()) {
            if (name.equalsIgnoreCase(portType.name())) {
                return portType;
            }
        }
        return null;
    }
}
