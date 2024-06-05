package cn.cnic.component.process.vo;

import cn.cnic.base.vo.BasePageVo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ErrorLogMappingVo extends BasePageVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String originAbstract;
    private String explainZh;
    private String regexPattern;

    private Long version;

    private String keyword;

}
