package cn.cnic.component.flow.entity;

import cn.cnic.base.BaseModelIDNoCorpAgentId;
import cn.cnic.common.serializer.ToLongDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FlowStopsPublishingProperty extends BaseModelIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;
//    @JsonSerialize(using = ToStringSerializer.class)
//    @JsonDeserialize(using = ToLongDeserializer.class)
    private Long publishingId;
    private String stopId;
    private String stopName;
    private String stopBundle;
    private String propertyId;
    private String propertyName;
    private String name;
    private Integer type; //参数的发布类型 0-输入 1-其他 2-输出
    private String allowableValues;
    private String customValue;
    private String description;
//    @JsonSerialize(using = ToStringSerializer.class)
//    @JsonDeserialize(using = ToLongDeserializer.class)
    private Long propertySort;
    private String example;

    private Long fileId;
    private String fileName;
    private String filePath;
}
