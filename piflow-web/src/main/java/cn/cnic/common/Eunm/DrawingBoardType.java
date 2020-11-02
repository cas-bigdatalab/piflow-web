package cn.cnic.common.Eunm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cn.cnic.base.TextureEnumSerializer;

@JsonSerialize(using = TextureEnumSerializer.class)
public enum DrawingBoardType {

    TASK("TASK","Task"),
    GROUP("GROUP","Group"),
    FLOW("FLOW","Flow"),
    PROCESS ("PROCESS","Process");

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
