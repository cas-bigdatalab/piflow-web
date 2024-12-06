package cn.cnic.common.Eunm;

import cn.cnic.base.TextureEnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//文件触发调度触发模式
@JsonSerialize(using = TextureEnumSerializer.class)
public enum FileScheduleTriggerMode {
    PARALLEL(0),//并行
    SERIAL(1);//串行

    private final Integer value;

    //
    private FileScheduleTriggerMode(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name();
    }

    public static FileScheduleTriggerMode fromValue(Integer value) {
        for (FileScheduleTriggerMode productTypeAssociateType : FileScheduleTriggerMode.values()) {
            if (productTypeAssociateType.getValue() == value) {
                return productTypeAssociateType;
            }
        }
        return null;
    }

    public static FileScheduleTriggerMode formName(String name) {
        try {
            return FileScheduleTriggerMode.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
