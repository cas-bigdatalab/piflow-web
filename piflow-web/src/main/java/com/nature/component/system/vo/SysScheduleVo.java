package com.nature.component.system.vo;

import com.nature.common.Eunm.ScheduleState;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SysScheduleVo implements Serializable {

    private String id;
    private String jobName;
    private String jobClass;
    private String cronExpression;
    private ScheduleState status;
    private String description;
}
