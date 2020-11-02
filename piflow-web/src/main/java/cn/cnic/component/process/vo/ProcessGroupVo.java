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
public class ProcessGroupVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String viewXml;
    private String description;
    private String pageId;
    private String flowId;
    private String appId;
    private String parentProcessId;
    private String processId;
    private ProcessState state;
    private Date startTime;
    private Date endTime;
    private String progress;
    private RunModeType runModeType = RunModeType.RUN;
    private Date crtDttm;
    private MxGraphModelVo mxGraphModelVo;
    private ProcessGroupVo processGroupVo;

    private List<ProcessVo> processVoList = new ArrayList<>();
    private List<ProcessGroupPathVo> processGroupPathVoList = new ArrayList<>();
    private List<ProcessGroupVo> processGroupVoList = new ArrayList<>();

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
