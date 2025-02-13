package cn.cnic.component.process.vo;

import cn.cnic.base.vo.BasePageVo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 更新为常见问题库
 */
@Getter
@Setter
public class ErrorLogMappingVo extends BasePageVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String name;   //问题名称
    private String originAbstract; //原始报错摘要
    private String explainZh;   //问题分析
    private String regexPattern; //关键词匹配
    private String flowPublishId; //关联的发布流水线id
    private String flowPublishName; //关联的发布流水线名称

    private Long version;

    private String keyword;  //检索用



}
