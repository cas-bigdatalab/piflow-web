package cn.cnic.component.schedule.vo;

import cn.cnic.base.util.DateUtils;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.common.Eunm.ScheduleState;
import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import cn.cnic.component.process.vo.ProcessGroupVo;
import cn.cnic.component.process.vo.ProcessPathVo;
import cn.cnic.component.process.vo.ProcessStopVo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class ScheduleVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private Date crtDttm;
    private String scheduleId;
    private String type;
    private ScheduleState status;
    private String cronExpression;
    private Date planStartTime;
    private Date planEndTime;
    private String scheduleProcessTemplateId;
    private String scheduleRunTemplateId;
    private String scheduleRunTemplateName;

    public String getCrtDttmStr() {
        return DateUtils.dateTimesToStr(this.crtDttm);
    }
    public String getPlanStartTimeStr() {
        return DateUtils.dateTimesToStr(this.planStartTime);
    }
    public void setPlanStartTimeStr(String planStartTimeStr) {
        this.planStartTime = DateUtils.strToTime(planStartTimeStr);
    }
    public String getPlanEndTimeStr() {
        return DateUtils.dateTimesToStr(this.planEndTime);
    }
    public void setPlanEndTimeStr(String planEndTimeStr) {
        this.planEndTime = DateUtils.strToTime(planEndTimeStr);
    }


}
