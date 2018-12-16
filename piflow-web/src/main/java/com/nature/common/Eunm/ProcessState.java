package com.nature.common.Eunm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nature.base.TextureEnumSerializer;

@JsonSerialize(using = TextureEnumSerializer.class)
public enum ProcessState {
    STARTED("STARTED", "启动"),
    COMPLETED("COMPLETED", "完成"),
    FAILED("FAILED", "失败"),
    ABORTED("ABORTED","中止"),
    FORK("FORK","FORK"),
    KILLED("KILLED","停止");

    private final String value;
    private final String text;

    private ProcessState(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    public static ProcessState selectGender(String name) {
        for (ProcessState portType : ProcessState.values()) {
            if (name.equalsIgnoreCase(portType.name())) {
                return portType;
            }
        }
        return null;
    }
}
