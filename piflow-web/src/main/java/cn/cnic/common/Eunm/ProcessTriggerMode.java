package cn.cnic.common.Eunm;

import cn.cnic.base.TextureEnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//进程触发模式
@JsonSerialize(using = TextureEnumSerializer.class)
public enum ProcessTriggerMode {
    MANUAL(0),//手动触发
    TIMING(1),//定时调度
    FILE(2);//文件触发调度

    private final Integer value;

    //
    private ProcessTriggerMode(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name();
    }

    public static ProcessTriggerMode fromValue(Integer value) {
        for (ProcessTriggerMode productTypeAssociateType : ProcessTriggerMode.values()) {
            if (productTypeAssociateType.getValue() == value) {
                return productTypeAssociateType;
            }
        }
        return null;
    }

    public static ProcessTriggerMode formName(String name) {
        try {
            return ProcessTriggerMode.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
