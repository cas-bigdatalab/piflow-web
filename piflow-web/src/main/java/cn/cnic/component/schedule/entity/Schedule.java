package cn.cnic.component.schedule.entity;


import java.util.Date;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.ScheduleState;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Schedule extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String scheduleId;
    private String type;
    private ScheduleState status;
    private String cronExpression;
    private Date planStartTime;
    private Date planEndTime;
    private String scheduleProcessTemplateId;
    private String scheduleRunTemplateId;

}