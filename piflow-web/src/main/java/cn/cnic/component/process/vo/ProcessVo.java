package cn.cnic.component.process.vo;

import cn.cnic.base.util.DateUtils;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class ProcessVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private Date crtDttm;
    private String name;
    private String driverMemory;
    private String executorNumber;
    private String executorMemory;
    private String executorCores;
    private String description;
    private String flowId;
    private String appId;
    private String parentProcessId;
    private String processId;
    private ProcessState state;
    private Date startTime;
    private Date endTime;
    private String progress;
    private RunModeType runModeType;
    private String pageId;
    private String viewXml;
    private ProcessGroupVo processGroupVo;
    private MxGraphModelVo mxGraphModelVo;
    private List<ProcessStopVo> processStopVoList = new ArrayList<ProcessStopVo>();
    private List<ProcessPathVo> processPathVoList = new ArrayList<ProcessPathVo>();

    public String getCrtDttmStr() {
        return DateUtils.dateTimesToStr(this.crtDttm);
    }

    public String getStartTimeStr() {
        return DateUtils.dateTimesToStr(this.startTime);
    }

    public String getEndTimeStr() {
        return DateUtils.dateTimesToStr(this.endTime);
    }
}
