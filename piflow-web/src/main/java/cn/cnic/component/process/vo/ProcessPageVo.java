package cn.cnic.component.process.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ProcessPageVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String crtDttm;
    private String name;
    private String state;
    private Integer triggerMode;
    private String scheduleName;
//    private Integer isAvailable;
}
