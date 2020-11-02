package cn.cnic.component.template.vo;

import cn.cnic.component.flow.vo.FlowVo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FlowTemplateVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private String value;
    private String path;
    private FlowVo flowVo;

}
