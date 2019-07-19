package com.nature.common.Eunm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nature.base.TextureEnumSerializer;

@JsonSerialize(using = TextureEnumSerializer.class)
public enum TaskState {
    RUNNING("STARTED", "运行中"),
    STOP("STOP", "停止"),
    FAILED("FAILED", "失败");

    private final String value;
    private final String text;

    private TaskState(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    public static TaskState selectGender(String name) {
        for (TaskState taksState : TaskState.values()) {
            if (name.equalsIgnoreCase(taksState.name())) {
                return taksState;
            }
        }
        return null;
    }
}
