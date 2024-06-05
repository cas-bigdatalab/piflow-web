package cn.cnic.component.process.entity;

import cn.cnic.base.BaseModelIDNoCorpAgentId;
import cn.cnic.component.dataProduct.entity.ProductUser;
import cn.cnic.component.system.entity.File;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorLogMapping extends BaseModelIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String name;//名称
    private String originAbstract;//原始报错摘要
    private String explainZh;//中文解释映射
    private String regexPattern;//关键词匹配
}
