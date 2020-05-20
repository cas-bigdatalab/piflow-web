package com.nature.provider.process;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SqlUtils;
import com.nature.common.Eunm.ProcessParentType;
import com.nature.common.Eunm.ProcessState;
import com.nature.component.process.model.Process;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.Map;

public class ProcessMapperProvider {

    private String id;
    private String crtUser;
    private String crtDttmStr;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String name;
    private String driverMemory;
    private String executorNumber;
    private String executorMemory;
    private String executorCores;
    private String viewXml;
    private String description;
    private String appId;
    private String processId;
    private String stateName;
    private String startTimeStr;
    private String endTimeStr;
    private String progress;
    private String flowId;
    private String runModeTypeStr;
    private String parentProcessId;
    private String processParentType;

    private void preventSQLInjectionProcess(Process process) {
        if (null != process && StringUtils.isNotBlank(process.getLastUpdateUser())) {
            // Mandatory Field
            String id = process.getId();
            String crtUser = process.getCrtUser();
            String lastUpdateUser = process.getLastUpdateUser();
            Boolean enableFlag = process.getEnableFlag();
            Long version = process.getVersion();
            Date crtDttm = process.getCrtDttm();
            Date lastUpdateDttm = process.getLastUpdateDttm();
            this.id = SqlUtils.preventSQLInjection(id);
            this.crtUser = (null != crtUser ? SqlUtils.preventSQLInjection(crtUser) : null);
            this.lastUpdateUser = SqlUtils.preventSQLInjection(lastUpdateUser);
            this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
            this.version = (null != version ? version : 0L);
            String crtDttmStr = DateUtils.dateTimesToStr(crtDttm);
            String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
            this.crtDttmStr = (null != crtDttm ? SqlUtils.preventSQLInjection(crtDttmStr) : null);
            this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

            // Selection field
            this.name = SqlUtils.preventSQLInjection(process.getName());
            this.driverMemory = SqlUtils.preventSQLInjection(process.getDriverMemory());
            this.executorNumber = SqlUtils.preventSQLInjection(process.getExecutorNumber());
            this.executorMemory = SqlUtils.preventSQLInjection(process.getExecutorMemory());
            this.executorCores = SqlUtils.preventSQLInjection(process.getExecutorCores());
            this.viewXml = SqlUtils.preventSQLInjection(process.getViewXml());
            this.description = SqlUtils.preventSQLInjection(process.getDescription());
            this.appId = SqlUtils.preventSQLInjection(process.getAppId());
            this.processId = SqlUtils.preventSQLInjection(process.getProcessId());
            this.stateName = SqlUtils.preventSQLInjection(null != process.getState() ? process.getState().name() : null);
            String startTime = (null != process.getStartTime() ? DateUtils.dateTimesToStr(process.getStartTime()) : null);
            String endTime = (null != process.getEndTime() ? DateUtils.dateTimesToStr(process.getEndTime()) : null);
            this.startTimeStr = SqlUtils.preventSQLInjection(startTime);
            this.endTimeStr = SqlUtils.preventSQLInjection(endTime);
            this.progress = SqlUtils.preventSQLInjection(process.getProgress());
            this.flowId = SqlUtils.preventSQLInjection(process.getFlowId());
            this.runModeTypeStr = SqlUtils.preventSQLInjection(null != process.getRunModeType() ? process.getRunModeType().name() : null);
            this.parentProcessId = SqlUtils.preventSQLInjection(process.getParentProcessId());
            this.processParentType = SqlUtils.preventSQLInjection(null != process.getProcessParentType() ? process.getProcessParentType().name() : null);
        }
    }

    private void reset() {
        this.id = null;
        this.crtUser = null;
        this.crtDttmStr = null;
        this.lastUpdateDttmStr = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.name = null;
        this.driverMemory = null;
        this.executorNumber = null;
        this.executorMemory = null;
        this.executorCores = null;
        this.viewXml = null;
        this.description = null;
        this.appId = null;
        this.processId = null;
        this.stateName = null;
        this.startTimeStr = null;
        this.endTimeStr = null;
        this.progress = null;
        this.flowId = null;
        this.parentProcessId = null;
        this.processParentType = null;
    }

    /**
     * addProcess
     *
     * @param process
     * @return
     */
    public String addProcess(Process process) {
        String sqlStr = "select 0";
        this.preventSQLInjectionProcess(process);
        if (null != process) {
            SQL sql = new SQL();
            // INSERT_INTO brackets is table name
            sql.INSERT_INTO("flow_process");
            // The first string in the value is the field name corresponding to the table in the database.
            // Process the required fields first
            if (null == crtDttmStr) {
                String crtDttm = DateUtils.dateTimesToStr(new Date());
                crtDttmStr = SqlUtils.preventSQLInjection(crtDttm);
            }
            if (StringUtils.isBlank(crtUser)) {
                crtUser = SqlUtils.preventSQLInjection("-1");
            }
            sql.VALUES("id", id);
            sql.VALUES("crt_dttm", crtDttmStr);
            sql.VALUES("crt_user", crtUser);
            sql.VALUES("last_update_dttm", lastUpdateDttmStr);
            sql.VALUES("last_update_user", lastUpdateUser);
            sql.VALUES("version", version + "");
            sql.VALUES("enable_flag", enableFlag + "");

            // handle other fields
            sql.VALUES("name", name);
            sql.VALUES("driver_memory", driverMemory);
            sql.VALUES("executor_number", executorNumber);
            sql.VALUES("executor_memory", executorMemory);
            sql.VALUES("executor_cores", executorCores);
            sql.VALUES("view_xml", viewXml);
            sql.VALUES("description", description);
            sql.VALUES("app_id", appId);
            sql.VALUES("process_id", processId);
            sql.VALUES("state", stateName);
            sql.VALUES("start_time", startTimeStr);
            sql.VALUES("end_time", endTimeStr);
            sql.VALUES("progress", progress);
            sql.VALUES("flow_id", flowId);
            sql.VALUES("run_mode_type", runModeTypeStr);
            sql.VALUES("parent_process_id", parentProcessId);
            sql.VALUES("process_parent_type", processParentType);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * Query process by process ID
     *
     * @param id
     * @return
     */
    public String getProcessById(String id) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(id)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from flow_process ");
            strBuf.append("where enable_flag = 1 ");
            strBuf.append("and id= " + SqlUtils.preventSQLInjection(id));
            strBuf.append(SqlUtils.addQueryByUserRole(true, false));
            sqlStr = strBuf.toString();
        }
        return sqlStr;
    }

    /**
     * Query process by processGroup ID
     *
     * @param processGroupId
     * @return
     */
    public String getProcessByProcessGroupId(String processGroupId) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(processGroupId)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from flow_process ");
            strBuf.append("where enable_flag = 1 ");
            strBuf.append("and fk_flow_process_group_id= " + SqlUtils.preventSQLInjection(processGroupId));
            strBuf.append(SqlUtils.addQueryByUserRole(true, false));
            sqlStr = strBuf.toString();
        }
        return sqlStr;
    }

    /**
     * Query process list(processList)
     *
     * @return
     */
    public String getProcessList() {
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_process");
        sql.WHERE("enable_flag = 1");
        sql.WHERE("app_id is not null");
        sql.ORDER_BY("crt_dttm desc", "last_update_dttm desc");
        return sql.toString();
    }

    /**
     * Query process list according to param(processList)
     *
     * @param param
     * @return
     */
    public String getProcessListByParam(String param) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from flow_process ");
        strBuf.append("where ");
        strBuf.append("enable_flag = 1 ");
        strBuf.append("and app_id is not null ");
        strBuf.append("and process_parent_type = " + SqlUtils.addSqlStrAndReplace(ProcessParentType.PROCESS.name()));
        strBuf.append("and fk_flow_process_group_id is null ");
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("and ( ");
            strBuf.append("app_id like '%" + param + "%' ");
            strBuf.append("or name like '%" + param + "%' ");
            strBuf.append("or state like '%" + param + "%' ");
            strBuf.append("or description like '%" + param + "%' ");
            strBuf.append(") ");
        }
        strBuf.append(SqlUtils.addQueryByUserRole(true, false));
        strBuf.append("order by crt_dttm desc,last_update_dttm desc ");

        return strBuf.toString();
    }

    /**
     * Query processGroup list according to param(processList)
     *
     * @param param
     * @return
     */
    public String getProcessGroupListByParam(String param) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from flow_process ");
        strBuf.append("where ");
        strBuf.append("enable_flag = 1 ");
        strBuf.append("and app_id is not null ");
        strBuf.append("and process_parent_type = " + SqlUtils.addSqlStrAndReplace(ProcessParentType.GROUP.name()));
        strBuf.append("and fk_flow_process_group_id is null ");
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("and ( ");
            strBuf.append("app_id like '%" + param + "%' ");
            strBuf.append("or name like '%" + param + "%' ");
            strBuf.append("or state like '%" + param + "%' ");
            strBuf.append("or description like '%" + param + "%' ");
            strBuf.append(") ");
        }
        strBuf.append(SqlUtils.addQueryByUserRole(true, false));
        strBuf.append("order by crt_dttm desc,last_update_dttm desc ");

        return strBuf.toString();
    }

    /**
     * Query the running process list according to flowId(processList)
     *
     * @return
     */
    public String getRunningProcessList(String flowId) {
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_process");
        sql.WHERE("app_id is not null");
        sql.WHERE("enable_flag = 1");
        sql.WHERE("flow_id = " + SqlUtils.preventSQLInjection(flowId));
        sql.WHERE("state = " + SqlUtils.preventSQLInjection(ProcessState.STARTED.name()));
        sql.ORDER_BY("crt_dttm desc", "last_update_dttm desc");
        return sql.toString();
    }

    /**
     * Query process according to process appId
     *
     * @param appID
     * @return
     */
    public String getProcessByAppId(String appID) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(appID)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("flow_process");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("app_id = " + SqlUtils.preventSQLInjection(appID));
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * Query process according to process appId
     *
     * @param appID
     * @return
     */
    public String getProcessNoGroupByAppId(String appID) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(appID)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("flow_process");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_group_id is null");
            sql.WHERE("app_id = " + SqlUtils.preventSQLInjection(appID));
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * Query process list according to the process AppId array
     *
     * @param map
     * @return
     */
    public String getProcessListByAppIDs(Map<String, String[]> map) {
        String sqlStr = "select 0";
        String[] appIDs = map.get("appIDs");
        if (null != appIDs && appIDs.length > 0) {
            SQL sql = new SQL();
            String appIDsStr = SqlUtils.strArrayToStr(appIDs);
            if (StringUtils.isNotBlank(appIDsStr)) {
                //appIDsStr = appIDsStr.replace(",", "','");
                //appIDsStr = "'" + appIDsStr + "'";

                sql.SELECT("*");
                sql.FROM("flow_process");
                sql.WHERE("enable_flag = 1");
                sql.WHERE("app_id in (" + appIDsStr + ")");

                sqlStr = sql.toString();
            }
        }
        return sqlStr;
    }

    /**
     * update process
     *
     * @param process
     * @return
     */
    public String updateProcess(Process process) {
        String sqlStr = "select 0";
        this.preventSQLInjectionProcess(process);
        if (null != process) {
            SQL sql = new SQL();
            sql.UPDATE("flow_process");

            //Process the required fields first
            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag=" + enableFlag);
            sql.SET("name=" + name);
            sql.SET("driver_memory=" + driverMemory);
            sql.SET("executor_number=" + executorNumber);
            sql.SET("executor_memory=" + executorMemory);
            sql.SET("executor_cores=" + executorCores);
            sql.SET("view_xml=" + viewXml);
            sql.SET("description=" + description);
            sql.SET("app_id=" + appId);
            sql.SET("process_id=" + processId);
            sql.SET("state=" + stateName);
            sql.SET("start_time=" + startTimeStr);
            sql.SET("end_time=" + endTimeStr);
            sql.SET("progress=" + progress);
            sql.SET("run_mode_type=" + runModeTypeStr);
            sql.WHERE("version = " + version);
            sql.WHERE("id = " + id);
            if (StringUtils.isNotBlank(id)) {
                sqlStr = sql.toString();
            }
        }
        this.reset();
        return sqlStr;
    }

    /**
     * Tombstone
     *
     * @param id
     * @return
     */
    public String updateEnableFlag(String id, String username) {
        String sqlStr = "select 0";
        if (!StringUtils.isAnyEmpty(id, username)) {
            StringBuffer sqlStrBuf = new StringBuffer();
            sqlStrBuf.append("update flow_process ");
            sqlStrBuf.append("set ");
            sqlStrBuf.append("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())) + ", ");
            sqlStrBuf.append("last_update_user = " + SqlUtils.preventSQLInjection(username) + ", ");
            sqlStrBuf.append("version=(version+1), ");
            sqlStrBuf.append("enable_flag = 0 ");
            sqlStrBuf.append("where enable_flag = 1 ");
            sqlStrBuf.append("and id = " + SqlUtils.preventSQLInjection(id));

            sqlStr = sqlStrBuf.toString();
        }
        return sqlStr;
    }

    /**
     * Query tasks that need to be synchronized
     *
     * @return
     */
    public String getRunningProcess() {
        StringBuffer sqlStrBuf = new StringBuffer();
        sqlStrBuf.append("select app_id from flow_process ");
        sqlStrBuf.append("where ");
        sqlStrBuf.append("enable_flag=1 ");
        sqlStrBuf.append("and ");
        sqlStrBuf.append("app_id is not null ");
        sqlStrBuf.append("and ");
        sqlStrBuf.append("( ");
        sqlStrBuf.append("( ");
        sqlStrBuf.append("state!=" + SqlUtils.preventSQLInjection(ProcessState.COMPLETED.name()) + " ");
        sqlStrBuf.append("and ");
        sqlStrBuf.append("state!=" + SqlUtils.preventSQLInjection(ProcessState.FAILED.name()) + "  ");
        sqlStrBuf.append("and ");
        sqlStrBuf.append("state!=" + SqlUtils.preventSQLInjection(ProcessState.KILLED.name()) + " ");
        sqlStrBuf.append(") ");
        sqlStrBuf.append("or ");
        sqlStrBuf.append("state is null ");
        sqlStrBuf.append(") ");
        return sqlStrBuf.toString();
    }

    /**
     * Query process by processGroup ID
     *
     * @param processGroupId
     * @return
     */
    public String getProcessByPageId(String processGroupId, String pageId) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(processGroupId) && StringUtils.isNotBlank(pageId)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from flow_process ");
            strBuf.append("where enable_flag = 1 ");
            strBuf.append("and page_id= " + pageId + " ");
            strBuf.append("and fk_flow_process_group_id= " + SqlUtils.preventSQLInjection(processGroupId));
            strBuf.append(SqlUtils.addQueryByUserRole(true, false));
            sqlStr = strBuf.toString();
        }
        return sqlStr;
    }

    /**
     * Query based on pid and pageIds
     *
     * @param map
     * @return
     */
    @SuppressWarnings("rawtypes")
	public String getProcessByPageIds(Map map) {
        String processId = (String) map.get("processGroupId");
        String[] pageIds = (String[]) map.get("pageIds");
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(processId) && null != pageIds && pageIds.length > 0) {
            String pageIdsStr = SqlUtils.strArrayToStr(pageIds);
            if (StringUtils.isNotBlank(pageIdsStr)) {

                //pageIdsStr = pageIdsStr.replace(",", "','");
                //pageIdsStr = "'" + pageIdsStr + "'";
                SQL sql = new SQL();
                sql.SELECT("*");
                sql.FROM("flow_process");
                sql.WHERE("enable_flag = 1");
                sql.WHERE("fk_flow_process_group_id = " + SqlUtils.preventSQLInjection(processId));
                sql.WHERE("page_id in ( " + pageIdsStr + ")");

                sqlStr = sql.toString();
            }
        }
        return sqlStr;
    }

}
