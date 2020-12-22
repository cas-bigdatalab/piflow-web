package cn.cnic.component.system.mapper.provider;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.common.Eunm.ScheduleState;
import cn.cnic.component.system.entity.SysSchedule;

public class SysScheduleMapperProvider {

    private String id;
    private String lastUpdateUser;
    private String lastUpdateDttmStr;
    private long version;
    private int enableFlag;
    private String jobName;
    private String jobClass;
    private String cronExpression;
    private String status = "'INIT'";
    private String lastRunResult = "'INIT'";

    private boolean preventSQLInjectionSysSchedule(SysSchedule sysSchedule) {
        if (null == sysSchedule || StringUtils.isBlank(sysSchedule.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        this.id = SqlUtils.preventSQLInjection(sysSchedule.getId());
        this.enableFlag = ((null != sysSchedule.getEnableFlag() && sysSchedule.getEnableFlag()) ? 1 : 0);
        this.version = (null != sysSchedule.getVersion() ? sysSchedule.getVersion() : 0L);
        String lastUpdateDttm = DateUtils.dateTimesToStr(null != sysSchedule.getLastUpdateDttm() ? sysSchedule.getLastUpdateDttm() : new Date());
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttm);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(sysSchedule.getLastUpdateUser());

        // Selection field
        this.jobName = SqlUtils.preventSQLInjection(sysSchedule.getJobName());
        this.jobClass = SqlUtils.preventSQLInjection(sysSchedule.getJobClass());
        this.cronExpression = SqlUtils.preventSQLInjection(sysSchedule.getCronExpression());
        this.status = SqlUtils.preventSQLInjection(null != sysSchedule.getStatus() ? sysSchedule.getStatus().name() : "INIT");
        this.lastRunResult = SqlUtils.preventSQLInjection(null != sysSchedule.getLastRunResult() ? sysSchedule.getLastRunResult().name() : "INIT");


        return true;
    }

    private void resetSysSchedule() {
        this.id = null;
        this.lastUpdateUser = null;
        this.lastUpdateDttmStr = null;
        this.version = 0L;
        this.enableFlag = 1;
        this.jobName = null;
        this.jobClass = null;
        this.cronExpression = null;
        this.status = "'INIT'";
        this.lastRunResult = "'INIT'";
    }

    public String insert(SysSchedule sysSchedule) {
        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionSysSchedule(sysSchedule);
        if (flag) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO sys_schedule ");

            strBuf.append("( ");
            strBuf.append(SqlUtils.baseFieldName() + ", ");
            strBuf.append("job_name, job_class, cron_expression, status, last_run_result ");
            strBuf.append(") ");

            strBuf.append("values ");
            strBuf.append("(");
            strBuf.append(SqlUtils.baseFieldValues(sysSchedule) + ", ");
            strBuf.append(this.jobName + "," + this.jobClass + "," + this.cronExpression + "," + this.status + "," + this.lastRunResult);
            strBuf.append(")");
            this.resetSysSchedule();
            sqlStr = strBuf.toString() + ";";
        }
        return sqlStr;
    }

    /**
     * update sysSchedule
     *
     * @param sysSchedule
     * @return
     */
    public String update(SysSchedule sysSchedule) {

        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionSysSchedule(sysSchedule);
        if (flag && StringUtils.isNotBlank(this.id)) {
            SQL sql = new SQL();
            // INSERT_INTO brackets is table name
            sql.UPDATE("sys_schedule");
            // The first string in the SET is the name of the field corresponding to the table in the database
            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag = " + enableFlag);
            sql.SET("job_name = " + this.jobName);
            sql.SET("job_class = " + this.jobClass);
            sql.SET("cron_expression = " + this.cronExpression);
            sql.SET("status = " + this.status);
            sql.SET("last_run_result = " + this.lastRunResult);
            sql.WHERE("version = " + this.version);
            sql.WHERE("id = " + this.id);
            sqlStr = sql.toString();
        }
        this.resetSysSchedule();
        return sqlStr;
    }

    public String getSysScheduleListByStatus(@Param("isAdmin") boolean isAdmin, @Param("status") ScheduleState status) {
        if (!isAdmin || null == status) {
            return "SELECT 0";
        }
        StringBuffer sqlStrbuf = new StringBuffer();
        sqlStrbuf.append("SELECT * ");
        sqlStrbuf.append("FROM sys_schedule ");
        sqlStrbuf.append("WHERE enable_flag = 1 ");
        sqlStrbuf.append("AND ");
        sqlStrbuf.append("status = " + SqlUtils.preventSQLInjection(status.name()) + " ");
        sqlStrbuf.append("ORDER BY crt_dttm asc,last_update_dttm DESC");
        String sqlStr = sqlStrbuf.toString();
        return sqlStr;
    }


    /**
     * getSysScheduleList
     *
     * @param isAdmin
     * @param param
     * @return
     */
    public String getSysScheduleList(boolean isAdmin, String param) {
        String sqlStr = "SELECT 0";
        if (isAdmin) {
            StringBuffer sqlStrbuf = new StringBuffer();
            sqlStrbuf.append("SELECT * ");
            sqlStrbuf.append("FROM sys_schedule ");
            sqlStrbuf.append("WHERE enable_flag = 1 ");
            if (StringUtils.isNotBlank(param)) {
                sqlStrbuf.append("AND ");
                sqlStrbuf.append("( ");
                sqlStrbuf.append("job_name like CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') OR ");
                sqlStrbuf.append("job_class like CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') OR ");
                sqlStrbuf.append("status like CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') OR ");
                sqlStrbuf.append("cron_expression like CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
                sqlStrbuf.append(") ");
            }
            sqlStrbuf.append("ORDER BY crt_dttm asc,last_update_dttm DESC");
            sqlStr = sqlStrbuf.toString();
        }
        return sqlStr;
    }

    /**
     * getSysScheduleById
     *
     * @param id
     * @return
     */
    public String getSysScheduleById(boolean isAdmin, String id) {
        String sqlStr = "SELECT 0";
        if (isAdmin && StringUtils.isNotBlank(id)) {
            StringBuffer sqlStrbuf = new StringBuffer();
            sqlStrbuf.append("SELECT * ");
            sqlStrbuf.append("FROM sys_schedule ");
            sqlStrbuf.append("WHERE enable_flag = 1 ");
            sqlStrbuf.append("AND ");
            sqlStrbuf.append("id = " + SqlUtils.addSqlStrAndReplace(id) + " ");
            sqlStr = sqlStrbuf.toString();
        }
        return sqlStr;
    }

}
