package cn.cnic.component.dataProduct.entity;

import cn.cnic.base.BaseModelIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataProductType extends BaseModelIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private Long parentId;
    private Integer level;//级别
    private String name;
    private String description;


    private Integer state; //0-已删除 1-感兴趣 2-不感兴趣

    private String fileId;
    private String fileName;
}
