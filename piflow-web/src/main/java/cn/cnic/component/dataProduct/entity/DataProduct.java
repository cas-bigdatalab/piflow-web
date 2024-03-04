package cn.cnic.component.dataProduct.entity;

import cn.cnic.base.BaseModelIDNoCorpAgentId;
import cn.cnic.component.system.entity.File;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DataProduct extends BaseModelIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String processId;
    private Long propertyId;
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


    private Long productTypeId;
    private String productTypeName;
    private File coverFile; //关联的封面文件
    private File file; //关联的数据产品文件记录
    private  ProductUser applyRecord; //申请记录
}
