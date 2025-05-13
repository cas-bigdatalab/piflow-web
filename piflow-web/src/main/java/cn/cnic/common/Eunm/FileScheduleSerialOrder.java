package cn.cnic.common.Eunm;

import cn.cnic.base.TextureEnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//文件触发调度触发模式
@JsonSerialize(using = TextureEnumSerializer.class)
public enum FileScheduleSerialOrder {
    ASC(0),//升序
    DESC(1);//降序

    private final Integer value;

    //
    private FileScheduleSerialOrder(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name();
    }

    public static FileScheduleSerialOrder fromValue(Integer value) {
        for (FileScheduleSerialOrder productTypeAssociateType : FileScheduleSerialOrder.values()) {
            if (productTypeAssociateType.getValue() == value) {
                return productTypeAssociateType;
            }
        }
        return null;
    }

    public static FileScheduleSerialOrder formName(String name) {
        try {
            return FileScheduleSerialOrder.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
