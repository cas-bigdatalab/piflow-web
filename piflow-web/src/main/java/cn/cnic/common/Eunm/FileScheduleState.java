package cn.cnic.common.Eunm;

import cn.cnic.base.TextureEnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//文件触发调度状态
@JsonSerialize(using = TextureEnumSerializer.class)
public enum FileScheduleState {
    INIT(0),
    RUNNING(1),
    STOP(2);

    private final Integer value;

    //
    private FileScheduleState(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name();
    }

    public static FileScheduleState fromValue(Integer value) {
        for (FileScheduleState productTypeAssociateType : FileScheduleState.values()) {
            if (productTypeAssociateType.getValue() == value) {
                return productTypeAssociateType;
            }
        }
        return null;
    }

    public static FileScheduleState formName(String name) {
        try {
            return FileScheduleState.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
