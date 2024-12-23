package cn.cnic.common.Eunm;

import cn.cnic.base.TextureEnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//文件触发调度关联类型
@JsonSerialize(using = TextureEnumSerializer.class)
public enum FileScheduleAssociateType {
    FLOW(0);

    private final Integer value;

    //
    private FileScheduleAssociateType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name();
    }

    public static FileScheduleAssociateType fromValue(Integer value) {
        for (FileScheduleAssociateType productTypeAssociateType : FileScheduleAssociateType.values()) {
            if (productTypeAssociateType.getValue() == value) {
                return productTypeAssociateType;
            }
        }
        return null;
    }

    public static FileScheduleAssociateType formName(String name) {
        try {
            return FileScheduleAssociateType.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
