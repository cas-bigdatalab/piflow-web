package cn.cnic.component.process.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ProcessGroupPathVo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private ProcessGroupVo processGroupVo;
    private String from;
    private String outport;
    private String inport;
    private String to;
    private String pageId;
}
