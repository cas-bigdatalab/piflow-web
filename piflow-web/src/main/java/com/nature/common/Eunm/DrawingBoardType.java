package com.nature.common.Eunm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nature.base.TextureEnumSerializer;

@JsonSerialize(using = TextureEnumSerializer.class)
public enum DrawingBoardType {

    TASK("TASK","Task"),
    GROUP("GROUP","Group"),
    FLOW("FLOW","Flow");

    private final String value;
    private final String text;

    private DrawingBoardType(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    public static ProcessParentType selectGender(String name) {
        for (ProcessParentType portType : ProcessParentType.values()) {
            if (name.equalsIgnoreCase(portType.name())) {
                return portType;
            }
        }
        return null;
    }

}
