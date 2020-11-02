package cn.cnic.common.Eunm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cn.cnic.base.TextureEnumSerializer;

@JsonSerialize(using = TextureEnumSerializer.class)
public enum SysRoleType {
    ADMIN("ADMIN", "ADMIN", "admin"),
    USER("USER", "USER", "user");

    private final String value;
    private final String text;
    private final String desc;

    private SysRoleType(String text, String value, String desc) {
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

    public static SysRoleType selectGender(String name) {
        for (SysRoleType portType : SysRoleType.values()) {
            if (name.equalsIgnoreCase(portType.name())) {
                return portType;
            }
        }
        return null;
    }

    public static SysRoleType selectGenderByValue(String value) {
        for (SysRoleType portType : SysRoleType.values()) {
            if (value.equalsIgnoreCase(portType.value)) {
                return portType;
            }
        }
        return null;
    }
}
