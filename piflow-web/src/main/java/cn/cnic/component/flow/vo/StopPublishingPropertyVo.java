package cn.cnic.component.flow.vo;

import cn.cnic.common.serializer.ToLongDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class StopPublishingPropertyVo implements Serializable {

    private static final long serialVersionUID = 1L;
    //    @JsonSerialize(using = ToStringSerializer.class)
//    @JsonDeserialize(using = ToLongDeserializer.class)
    private String id;
    //    @JsonSerialize(using = ToStringSerializer.class)
//    @JsonDeserialize(using = ToLongDeserializer.class)
    private String publishingId;
    private String stopId;
    private String stopBundle;
    private String stopName;
    private String propertyId;
    private String propertyName;
    private String name;
    private Integer type; //参数的发布类型 0-文件 1-普通 2-输出
    private String allowableValues;
    private String customValue;
    private String description;
    private Long propertySort;
    private String example;
    private String bak1;
    private String bak2;
    private String bak3;
    //    @JsonSerialize(using = ToStringSerializer.class)
//    @JsonDeserialize(using = ToLongDeserializer.class)
    private String fileId;
    private String fileName;

    private Long Version;

    private String tempSaveValue;
//    private String filePath;

}
