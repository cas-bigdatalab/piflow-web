package cn.cnic.component.flow.vo;

import cn.cnic.common.serializer.ToLongDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class StopPublishingVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String stopId;
    private String stopBundle;
    private String stopName;
    private String bak1; //分组排序
    private String bak2; //组件分组
    private String bak3; //组内组件排序

    private List<StopPublishingPropertyVo> stopPublishingPropertyVos;

}
