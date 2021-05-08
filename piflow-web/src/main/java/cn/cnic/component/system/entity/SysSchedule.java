package cn.cnic.component.system.entity;


import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.ScheduleRunResultType;
import cn.cnic.common.Eunm.ScheduleState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "SYS_SCHEDULE")
public class SysSchedule extends BaseHibernateModelUUIDNoCorpAgentId {
    
    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "varchar(255) COMMENT 'job name'")
    private String jobName;

    @Column(columnDefinition = "varchar(255) COMMENT 'job class'")
    private String jobClass;

    @Column(columnDefinition = "varchar(255) COMMENT 'task status'")
    @Enumerated(EnumType.STRING)
    private ScheduleState status;

    @Column(columnDefinition = "varchar(255) COMMENT 'task last run result'")
    @Enumerated(EnumType.STRING)
    private ScheduleRunResultType lastRunResult;

    @Column(columnDefinition = "varchar(255) COMMENT 'cron'")
    private String cronExpression;

}