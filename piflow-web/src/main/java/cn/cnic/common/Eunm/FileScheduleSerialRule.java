package cn.cnic.common.Eunm;

import cn.cnic.base.TextureEnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//文件触发调度触发模式
@JsonSerialize(using = TextureEnumSerializer.class)
public enum FileScheduleSerialRule {
    LAST_MODIFY_TIME(0),//最后更新时间
    FILE_NAME(1);//文件名

    private final Integer value;

    //
    private FileScheduleSerialRule(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name();
    }

    public static FileScheduleSerialRule fromValue(Integer value) {
        for (FileScheduleSerialRule productTypeAssociateType : FileScheduleSerialRule.values()) {
            if (productTypeAssociateType.getValue() == value) {
                return productTypeAssociateType;
            }
        }
        return null;
    }

    public static FileScheduleSerialRule formName(String name) {
        try {
            return FileScheduleSerialRule.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
