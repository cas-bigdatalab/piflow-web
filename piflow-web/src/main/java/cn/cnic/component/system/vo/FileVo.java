package cn.cnic.component.system.vo;

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
public class FileVo implements Serializable {

    private static final long serialVersionUID = 1L;

//    @JsonSerialize(using = ToStringSerializer.class)
//    @JsonDeserialize(using = ToLongDeserializer.class)
    private String id;
    private String fileName;
    private String fileType;
    private String filePath;
    private Integer associateType; //关联类型 0-数据产品类型  1-数据产品 2-数据产品封面 3-流水线发布参数 4-流水线
    private String associateId;

}
