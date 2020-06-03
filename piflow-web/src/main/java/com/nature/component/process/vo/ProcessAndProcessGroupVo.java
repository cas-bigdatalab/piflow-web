package com.nature.component.process.vo;

import com.nature.base.util.DateUtils;
import com.nature.common.Eunm.ProcessState;
import com.nature.common.Eunm.ProcessType;
import com.nature.common.Eunm.RunModeType;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Date startTime;
    private Date endTime;
    private String progress;
    private String state;
    private String parentProcessId;
    private String processType;
}
