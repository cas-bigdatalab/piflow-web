package cn.cnic.common.Eunm;

import cn.cnic.base.TextureEnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 组件类型枚举，用于区分上传的算法包是什么类型和系统自带组件
 * @author leilei
 * @date 2023-2-2
 */
@JsonSerialize(using = TextureEnumSerializer.class)
public enum ComponentFileType {
    DEFAULT("DEFAULT","DEFAULT"),       //默认类型(系统自带组件)
    SCALA("SCALA", "SCALA"),            //上传的算法包为scala类型
    PYTHON("PYTHON", "PYTHON");         //上传的算法包为python类型

    private final String value;
    private final String text;

    private ComponentFileType(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    public static ComponentFileType selectGender(String name) {
        for (ComponentFileType portType : ComponentFileType.values()) {
            if (name.equalsIgnoreCase(portType.name())) {
                return portType;
            }
        }
        return null;
    }
}
