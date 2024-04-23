package cn.cnic.component.flow.vo;

import cn.cnic.base.vo.BasePageVo;
import cn.cnic.common.serializer.ToLongDeserializer;
import cn.cnic.component.dataProduct.entity.EcosystemType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FlowPublishingVo extends BasePageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String keyword;//搜索时的关键字

//    @JsonSerialize(using = ToStringSerializer.class)
//    @JsonDeserialize(using = ToLongDeserializer.class)
    private String id;
    private String name;  //运行时，允许自定义进程名称和备注
    private String flowId;
    private String description;
//    @JsonSerialize(using = ToStringSerializer.class)
//    @JsonDeserialize(using = ToLongDeserializer.class)
    private Long productTypeId;
    private String productTypeName;
    private String productTypeDescription;
    private List<StopPublishingVo> stops;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date crtDttm;
    private String crtUser;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateDttm;
    private String lastUpdateUser;
//    @JsonSerialize(using = ToStringSerializer.class)
//    @JsonDeserialize(using = ToLongDeserializer.class)
    private Long version;
    private Integer flowSort;
    private String ecosystemTypeIds;

    private String coverFileId;
    private String coverFileName;
    private String instructionFileId;
    private String instructionFileName;

    private String bak1; //用来接收运行进程时的备注

    private List<EcosystemType> ecosystemTypes;   //生态站专用，所属生态系统类型

}
