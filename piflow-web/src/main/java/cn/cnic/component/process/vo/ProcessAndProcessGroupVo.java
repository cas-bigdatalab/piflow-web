package cn.cnic.component.process.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class ProcessAndProcessGroupVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private Date lastUpdateDttm;
    private Date crtDttm;
    private String appId;
    private String name;
    private String description;
    private String startTime;
    private String endTime;
    private String progress;
    private String state;
    private String parentProcessId;
    private String processType;
}
