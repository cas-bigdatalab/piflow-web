package cn.cnic.component.system.entity;


import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.ScheduleRunResultType;
import cn.cnic.common.Eunm.ScheduleState;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SysSchedule extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String jobName;
    private String jobClass;
    private ScheduleState status;
    private ScheduleRunResultType lastRunResult;
    private String cronExpression;

}