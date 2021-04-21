package cn.cnic.component.process.mapper.provider;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.common.Eunm.ProcessParentType;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.component.process.entity.Process;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.Map;

public class ProcessMapperProvider {

    private String id;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String name;
    private String driverMemory;
    private String executorNumber;
    private String executorMemory;
    private String executorCores;
    private String description;
    private String appId;
    private String pageId;
    private String progress;
    private String runModeTypeStr;
    private String flowId;
    private String parentProcessId;
    private String processParentType;
    private String processId;
    private String stateName;
    private String startTimeStr;
    private String endTimeStr;
    private String schedule_id;
    private String processGroup_id;
    private String viewXml;


    private boolean preventSQLInjectionProcess(Process process) {
        if (null == process || StringUtils.isBlank(process.getLastUpdateUser())) {
            return false;
        }

        // Mandatory Field
        this.id = SqlUtils.preventSQLInjection(process.getId());
        this.lastUpdateUser = SqlUtils.preventSQLInjection(process.getLastUpdateUser());
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != process.getLastUpdateDttm() ? process.getLastUpdateDttm() : new Date());
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);
        this.enableFlag = ((null != process.getEnableFlag() && process.getEnableFlag()) ? 1 : 0);
        this.version = (null != process.getVersion() ? process.getVersion() : 0L);

        // Selection field
        this.name = SqlUtils.preventSQLInjection(process.getName());
        this.driverMemory = SqlUtils.preventSQLInjection(process.getDriverMemory());
        this.executorNumber = SqlUtils.preventSQLInjection(process.getExecutorNumber());
        this.executorMemory = SqlUtils.preventSQLInjection(process.getExecutorMemory());
        this.executorCores = SqlUtils.preventSQLInjection(process.getExecutorCores());
        this.viewXml = SqlUtils.preventSQLInjection(process.getViewXml());
        this.description = SqlUtils.preventSQLInjection(process.getDescription());
        this.appId = SqlUtils.preventSQLInjection(process.getAppId());
        this.pageId = SqlUtils.preventSQLInjection(process.getPageId());
        this.processId = SqlUtils.preventSQLInjection(process.getProcessId());
        this.stateName = SqlUtils.preventSQLInjection(null != process.getState() ? process.getState().name() : null);
        String startTime = (null != process.getStartTime() ? DateUtils.dateTimesToStr(process.getStartTime()) : null);
        this.startTimeStr = SqlUtils.preventSQLInjection(startTime);
        String endTime = (null != process.getEndTime() ? DateUtils.dateTimesToStr(process.getEndTime()) : null);
        this.endTimeStr = SqlUtils.preventSQLInjection(endTime);
        this.progress = SqlUtils.preventSQLInjection(process.getProgress());
        this.flowId = SqlUtils.preventSQLInjection(process.getFlowId());
        this.runModeTypeStr = SqlUtils.preventSQLInjection(null != process.getRunModeType() ? process.getRunModeType().name() : null);
        this.parentProcessId = SqlUtils.preventSQLInjection(process.getParentProcessId());
        this.processParentType = SqlUtils.preventSQLInjection(null != process.getProcessParentType() ? process.getProcessParentType().name() : null);
        this.schedule_id = SqlUtils.preventSQLInjection(null != process.getSchedule() ? process.getSchedule().getId() : null);
        this.processGroup_id = SqlUtils.preventSQLInjection(null != process.getProcessGroup() ? process.getProcessGroup().getId() : null);
        return true;
    }

    private void reset() {
        this.id = null;
        this.lastUpdateDttmStr = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.name = null;
        this.driverMemory = null;
        this.executorNumber = null;
        this.executorMemory = null;
        this.executorCores = null;
        this.description = null;
        this.appId = null;
        this.pageId = null;
        this.progress = null;
        this.runModeTypeStr = null;
        this.flowId = null;
        this.parentProcessId = null;
        this.processParentType = null;
        this.processId = null;
        this.stateName = null;
        this.startTimeStr = null;
        this.endTimeStr = null;
        this.schedule_id = null;
        this.processGroup_id = null;
        this.viewXml = null;
    }

    /**
     * addProcess
     *
     * @param process
     * @return
     */
    public String addProcess(Process process) {
        String sqlStr = "SELECT 0";
        if (this.preventSQLInjectionProcess(process)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO flow_process ");
            strBuf.append("( ");
            strBuf.append(SqlUtils.baseFieldName() + ", ");
            strBuf.append("name, ");
            strBuf.append("driver_memory, ");
            strBuf.append("executor_number, ");
            strBuf.append("executor_memory, ");
            strBuf.append("executor_cores, ");
            strBuf.append("description, ");
            strBuf.append("app_id, ");
            strBuf.append("page_id, ");
            strBuf.append("process_id, ");
            strBuf.append("state, ");
            strBuf.append("start_time, ");
            strBuf.append("end_time, ");
            strBuf.append("progress, ");
            strBuf.append("flow_id, ");
            strBuf.append("run_mode_type, ");
            strBuf.append("parent_process_id, ");
            strBuf.append("process_parent_type, ");
            strBuf.append("fk_group_schedule_id, ");
            strBuf.append("fk_flow_process_group_id, ");
            strBuf.append("view_xml ");
            strBuf.append(") ");

            strBuf.append("VALUES ");
            strBuf.append("(");
            strBuf.append(SqlUtils.baseFieldValues(process) + ", ");
            strBuf.append(name + ", ");
            strBuf.append(driverMemory + ", ");
            strBuf.append(executorNumber + ", ");
            strBuf.append(executorMemory + ", ");
            strBuf.append(executorCores + ", ");
            strBuf.append(description + ", ");
            strBuf.append(appId + ", ");
            strBuf.append(pageId + ", ");
            strBuf.append(processId + ", ");
            strBuf.append(stateName + ", ");
            strBuf.append(startTimeStr + ", ");
            strBuf.append(endTimeStr + ", ");
            strBuf.append(progress + ", ");
            strBuf.append(flowId + ", ");
            strBuf.append(runModeTypeStr + ", ");
            strBuf.append(parentProcessId + ", ");
            strBuf.append(processParentType + ", ");
            strBuf.append(schedule_id + ", ");
            strBuf.append(processGroup_id + ", ");
            strBuf.append(viewXml + " ");
            strBuf.append(") ");
            this.reset();
            return strBuf.toString() + ";";
        }
        this.reset();
        return sqlStr;
    }


    /**
     * update process
     *
     * @param process
     * @return
     */
    public String updateProcess(Process process) {
        String sqlStr = "SELECT 0";
        if (this.preventSQLInjectionProcess(process)) {
            SQL sql = new SQL();
            sql.UPDATE("flow_process");

            // Process the required fields first
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
     * Query process by process ID
     *
     * @param id
     * @return
     */
    public String getProcessById(String username, boolean isAdmin, String id) {
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(id)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from flow_process ");
            strBuf.append("where enable_flag = 1 ");
            strBuf.append("and id= " + SqlUtils.preventSQLInjection(id));
            if (!isAdmin) {
                strBuf.append("and crt_user = " + SqlUtils.preventSQLInjection(username));
            }
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
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(processGroupId)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from flow_process ");
            strBuf.append("where enable_flag = 1 ");
            strBuf.append("and fk_flow_process_group_id= " + SqlUtils.preventSQLInjection(processGroupId));
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
    public String getProcessListByParam(String username, boolean isAdmin, String param) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("SELECT * ");
        strBuf.append("FROM flow_process ");
        strBuf.append("WHERE ");
        strBuf.append("enable_flag = 1 ");
        strBuf.append("AND app_id IS NOT NULL ");
        strBuf.append("AND process_parent_type = " + SqlUtils.addSqlStrAndReplace(ProcessParentType.PROCESS.name()));
        strBuf.append("AND fk_flow_process_group_id IS NULL ");
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("and ( ");
            strBuf.append("app_id LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            strBuf.append("OR name LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            strBuf.append("OR state LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            strBuf.append("OR description LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            strBuf.append(") ");
        }
        if (!isAdmin) {
            strBuf.append("AND crt_user = " + SqlUtils.preventSQLInjection(username));
        }
        strBuf.append("ORDER BY crt_dttm DESC,last_update_dttm DESC ");

        return strBuf.toString();
    }

    /**
     * Query processGroup list according to param(processList)
     *
     * @param param
     * @return
     */
    public String getProcessGroupListByParam(String username, boolean isAdmin, String param) {
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
            strBuf.append("app_id like CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            strBuf.append("or name like CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            strBuf.append("or state like CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            strBuf.append("or description like CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            strBuf.append(") ");
        }
        if (!isAdmin) {
            strBuf.append("and crt_user = " + SqlUtils.preventSQLInjection(username));
        }
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
        String sqlStr = "SELECT 0";
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
     * Query process id according to process appId
     *
     * @param appID
     * @return
     */
    public String getProcessIdByAppId(String appID) {
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(appID)) {
            SQL sql = new SQL();
            sql.SELECT("id");
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
        String sqlStr = "SELECT 0";
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
        String sqlStr = "SELECT 0";
        String[] appIDs = map.get("appIDs");
        if (null != appIDs && appIDs.length > 0) {
            SQL sql = new SQL();
            String appIDsStr = SqlUtils.strArrayToStr(appIDs);
            if (StringUtils.isNotBlank(appIDsStr)) {
                // appIDsStr = appIDsStr.replace(",", "','");
                // appIDsStr = "'" + appIDsStr + "'";

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
     * Tombstone
     *
     * @param id
     * @return
     */
    public String updateEnableFlag(String id, String username) {
        String sqlStr = "SELECT 0";
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
    public String getProcessByPageId(String username, boolean isAdmin, String processGroupId, String pageId) {
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(processGroupId) && StringUtils.isNotBlank(pageId)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from flow_process ");
            strBuf.append("where enable_flag = 1 ");
            strBuf.append("and page_id= " + pageId + " ");
            strBuf.append("and fk_flow_process_group_id= " + SqlUtils.preventSQLInjection(processGroupId));
            if (!isAdmin) {
                strBuf.append("and crt_user = " + SqlUtils.preventSQLInjection(username));
            }
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
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(processId) && null != pageIds && pageIds.length > 0) {
            String pageIdsStr = SqlUtils.strArrayToStr(pageIds);
            if (StringUtils.isNotBlank(pageIdsStr)) {

                // pageIdsStr = pageIdsStr.replace(",", "','");
                // pageIdsStr = "'" + pageIdsStr + "'";
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
