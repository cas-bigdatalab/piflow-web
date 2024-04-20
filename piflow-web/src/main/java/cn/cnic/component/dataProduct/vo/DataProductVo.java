package cn.cnic.component.dataProduct.vo;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.vo.BasePageVo;
import cn.cnic.common.serializer.ToLongDeserializer;
import cn.cnic.component.dataProduct.entity.ProductUser;
import cn.cnic.component.flow.vo.PathsVo;
import cn.cnic.component.flow.vo.StopsVo;
import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import cn.cnic.component.system.entity.File;
import cn.cnic.component.system.vo.FileVo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class DataProductVo extends BasePageVo implements Serializable {

    private static final long serialVersionUID = 1L;

//    @JsonSerialize(using = ToStringSerializer.class)
//    @JsonDeserialize(using = ToLongDeserializer.class)
    private String id;
//    @JsonSerialize(using = ToStringSerializer.class)
//    @JsonDeserialize(using = ToLongDeserializer.class)
    private Long productTypeId;
    private String processId;
//    @JsonSerialize(using = ToStringSerializer.class)
//    @JsonDeserialize(using = ToLongDeserializer.class)
    private String propertyId;  //发布参数id
    private String propertyName;
    private String datasetUrl;
    private String name;
    private String description;
    private Integer permission;
    private String keyword;
    private String sdPublisher;
    private String email;
    private Integer state; //0-已删除 1-生成中 2-生成失败 3-待发布 4-待审核 5-已发布 6-拒绝发布 7-已下架
    private String opinion;
    private String downReason;
    private Integer isShare;
    private String doiId;
    private String cstrId;
    private String subjectTypeId;
    private String timeRange;
    private String spacialRange;
    private String datasetSize;
    private Integer datasetType;  //数据集类型 0-电子表格 1-数据库
    private Integer associateId;

    private Date crtDttm;
    private String crtUser;
    private Date lastUpdateDttm;
    private String lastUpdateUser;

    private Long version;


    private String productTypeName;
    private FileVo coverFile; //关联的封面文件
    private FileVo file; //关联的数据产品文件记录

    private ProductUser applyRecord; //申请记录

}
