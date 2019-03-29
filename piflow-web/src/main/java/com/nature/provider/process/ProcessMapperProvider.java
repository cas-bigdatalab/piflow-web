package com.nature.provider.process;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
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
    private String parentProcessId;

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
            this.parentProcessId = SqlUtils.preventSQLInjection(process.getParentProcessId());
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
            // INSERT_INTO括号中为数据库表名
            sql.INSERT_INTO("FLOW_PROCESS");
            // value中的第一个字符串为数据库中表对应的字段名
            //先处理修改必填字段
            if (null == crtDttmStr) {
                String crtDttm = DateUtils.dateTimesToStr(new Date());
                crtDttmStr = SqlUtils.preventSQLInjection(crtDttm);
            }
            if (StringUtils.isBlank(crtUser)) {
                crtUser = SqlUtils.preventSQLInjection("-1");
            }
            sql.VALUES("ID", id);
            sql.VALUES("CRT_DTTM", crtDttmStr);
            sql.VALUES("CRT_USER", crtUser);
            sql.VALUES("LAST_UPDATE_DTTM", lastUpdateDttmStr);
            sql.VALUES("LAST_UPDATE_USER", lastUpdateUser);
            sql.VALUES("VERSION", version + "");
            sql.VALUES("ENABLE_FLAG", enableFlag + "");

            // 处理其他字段
            sql.VALUES("NAME", name);
            sql.VALUES("DRIVER_MEMORY", driverMemory);
            sql.VALUES("EXECUTOR_NUMBER", executorNumber);
            sql.VALUES("EXECUTOR_MEMORY", executorMemory);
            sql.VALUES("EXECUTOR_CORES", executorCores);
            sql.VALUES("view_xml", viewXml);
            sql.VALUES("DESCRIPTION", description);
            sql.VALUES("APP_ID", appId);
            sql.VALUES("PROCESS_ID", processId);
            sql.VALUES("STATE", stateName);
            sql.VALUES("START_TIME", startTimeStr);
            sql.VALUES("END_TIME", endTimeStr);
            sql.VALUES("progress", progress);
            sql.VALUES("flow_id", flowId);
            sql.VALUES("parent_process_id", parentProcessId);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * 根据进程Id查询进程
     *
     * @param id
     * @return
     */
    public String getProcessById(String id) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(id)) {
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("SELECT * ");
            strBuf.append("FROM FLOW_PROCESS ");
            strBuf.append("WHERE ENABLE_FLAG = 1 ");
            strBuf.append("AND ID= " + SqlUtils.preventSQLInjection(id));
            strBuf.append(SqlUtils.addQueryByUserRole(currentUser, true));
            sqlStr = strBuf.toString();
        }
        return sqlStr;
    }

    /**
     * 查询进程List(processList)
     *
     * @return
     */
    public String getProcessList() {
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("FLOW_PROCESS");
        sql.WHERE("ENABLE_FLAG = 1");
        sql.WHERE("APP_ID IS NOT null");
        sql.ORDER_BY("CRT_DTTM DESC,LAST_UPDATE_DTTM DESC");
        return sql.toString();
    }

    /**
     * 查询进程List根据param(processList)
     *
     * @param param
     * @return
     */
    public String getProcessListByParam(String param) {
        String sqlStr = "SELECT 0";
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("SELECT * ");
        strBuf.append("FROM FLOW_PROCESS ");
        strBuf.append("WHERE ");
        strBuf.append("ENABLE_FLAG = 1 ");
        strBuf.append("AND APP_ID IS NOT null ");
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("AND ( ");
            strBuf.append("APP_ID LIKE '%" + param + "%' ");
            strBuf.append("OR NAME LIKE '%" + param + "%' ");
            strBuf.append("OR STATE LIKE '%" + param + "%' ");
            strBuf.append("OR DESCRIPTION LIKE '%" + param + "%' ");
            strBuf.append(") ");
        }
        strBuf.append(SqlUtils.addQueryByUserRole(currentUser, true));
        strBuf.append("ORDER BY CRT_DTTM DESC,LAST_UPDATE_DTTM DESC ");
        sqlStr = strBuf.toString();

        return sqlStr;
    }

    /**
     * 根据flowId查询正在运行的进程List(processList)
     *
     * @return
     */
    public String getRunningProcessList(String flowId) {
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("FLOW_PROCESS");
        sql.WHERE("APP_ID IS NOT null");
        sql.WHERE("ENABLE_FLAG = 1");
        sql.WHERE("FLOW_ID = " + SqlUtils.preventSQLInjection(flowId));
        sql.WHERE("STATE = " + SqlUtils.preventSQLInjection(ProcessState.STARTED.name()));
        sql.ORDER_BY("CRT_DTTM DESC,LAST_UPDATE_DTTM DESC");
        return sql.toString();
    }

    /**
     * 根据进程appId查询进程
     *
     * @param appID
     * @return
     */
    public String getProcessByAppId(String appID) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(appID)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("FLOW_PROCESS");
            sql.WHERE("ENABLE_FLAG = 1");
            sql.WHERE("APP_ID = " + SqlUtils.preventSQLInjection(appID));
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * 根据进程AppId数组查询进程list
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
                appIDsStr = appIDsStr.replace(",", "','");
                appIDsStr = "'" + appIDsStr + "'";

                sql.SELECT("*");
                sql.FROM("FLOW_PROCESS");
                sql.WHERE("enable_flag = 1");
                sql.WHERE("app_id in (" + appIDsStr + ")");

                sqlStr = sql.toString();
            }
        }
        return sqlStr;
    }

    /**
     * 修改process
     *
     * @param process
     * @return
     */
    public String updateProcess(Process process) {
        String sqlStr = "SELECT 0";
        this.preventSQLInjectionProcess(process);
        if (null != process) {
            SQL sql = new SQL();
            sql.UPDATE("FLOW_PROCESS");

            //先处理修改必填字段
            sql.SET("LAST_UPDATE_DTTM = " + lastUpdateDttmStr);
            sql.SET("LAST_UPDATE_USER = " + lastUpdateUser);
            sql.SET("VERSION = " + (version + 1));

            // 处理其他字段
            sql.SET("ENABLE_FLAG=" + enableFlag);
            sql.SET("NAME=" + name);
            sql.SET("DRIVER_MEMORY=" + driverMemory);
            sql.SET("EXECUTOR_NUMBER=" + executorNumber);
            sql.SET("EXECUTOR_MEMORY=" + executorMemory);
            sql.SET("EXECUTOR_CORES=" + executorCores);
            sql.SET("view_xml=" + viewXml);
            sql.SET("DESCRIPTION=" + description);
            sql.SET("APP_ID=" + appId);
            sql.SET("PROCESS_ID=" + processId);
            sql.SET("STATE=" + stateName);
            sql.SET("START_TIME=" + startTimeStr);
            sql.SET("END_TIME=" + endTimeStr);
            sql.SET("progress=" + progress);
            sql.WHERE("VERSION = " + version);
            sql.WHERE("id = " + id);
            if (StringUtils.isNotBlank(id)) {
                sqlStr = sql.toString();
            }
        }
        this.reset();
        return sqlStr;
    }

    /**
     * 逻辑删除
     *
     * @param id
     * @return
     */
    public String updateEnableFlag(String id, String username) {
        String sqlStr = "select 0";
        if (!StringUtils.isAnyEmpty(id, username)) {
            StringBuffer sqlStrBuf = new StringBuffer();
            sqlStrBuf.append("UPDATE FLOW_PROCESS ");
            sqlStrBuf.append("SET ");
            sqlStrBuf.append("LAST_UPDATE_DTTM = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())) + ", ");
            sqlStrBuf.append("LAST_UPDATE_DTTM = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())) + ", ");
            sqlStrBuf.append("LAST_UPDATE_USER = " + SqlUtils.preventSQLInjection(username) + ", ");
            sqlStrBuf.append("VERSION=(VERSION+1), ");
            sqlStrBuf.append("ENABLE_FLAG = 0 ");
            sqlStrBuf.append("WHERE ENABLE_FLAG = 1 ");
            sqlStrBuf.append("AND ID = " + SqlUtils.preventSQLInjection(id));

            sqlStr = sqlStrBuf.toString();
        }
        return sqlStr;
    }

    /**
     * 查询需要同步的任务
     *
     * @return
     */
    public String getRunningProcess() {
        StringBuffer sqlStrBuf = new StringBuffer();
        sqlStrBuf.append("SELECT APP_ID ");
        sqlStrBuf.append("FROM FLOW_PROCESS ");
        sqlStrBuf.append("WHERE ENABLE_FLAG = 1 ");
        sqlStrBuf.append("AND ");
        sqlStrBuf.append("APP_ID IS NOT NULL ");
        sqlStrBuf.append("AND ");
        sqlStrBuf.append("( ");
        sqlStrBuf.append("STATE = " + SqlUtils.preventSQLInjection(ProcessState.STARTED.getText()));
        sqlStrBuf.append("OR ");
        sqlStrBuf.append("( ");
        sqlStrBuf.append("STATE = " + SqlUtils.preventSQLInjection(ProcessState.COMPLETED.getText()));
        sqlStrBuf.append("AND ");
        sqlStrBuf.append("END_TIME IS NULL ");
        sqlStrBuf.append(") ");
        sqlStrBuf.append(") ");
        return sqlStrBuf.toString();
    }

}
