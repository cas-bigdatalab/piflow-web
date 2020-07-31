package cn.cnic.component.schedule.entity;


import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.ScheduleState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "GROUP_SCHEDULE")
public class Schedule extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "varchar(255) COMMENT 'service schedule id'")
    private String scheduleId;

    @Column(columnDefinition = "varchar(255) COMMENT 'schedule content Flow or FlowGroup'")
    private String type;

    @Column(columnDefinition = "varchar(255) COMMENT 'schedule task status'")
    @Enumerated(EnumType.STRING)
    private ScheduleState status;

    @Column(columnDefinition = "varchar(255) COMMENT 'cron expression'")
    private String cronExpression;

    @Column(columnDefinition = "datetime COMMENT 'plan start time'")
    private Date planStartTime;

    @Column(columnDefinition = "datetime COMMENT 'plan end time'")
    private Date planEndTime;

    @Column(columnDefinition = "varchar(255) COMMENT 'Template ID for generating Process'")
    private String scheduleProcessTemplateId;

    @Column(columnDefinition = "varchar(255) COMMENT 'Start template ID'")
    private String scheduleRunTemplateId;

}