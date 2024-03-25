package cn.cnic.component.dataProduct.vo;

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
public class DataProductTypeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = ToLongDeserializer.class)
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = ToLongDeserializer.class)
    private Long parentId;
    private Integer level;//级别
    private String name;
    private String description;

    private Integer state; //0-已删除 1-感兴趣 2-不感兴趣
    private String fileId;
    private String fileName;
    private String filePath;
    private List<DataProductTypeVo> children;

}
