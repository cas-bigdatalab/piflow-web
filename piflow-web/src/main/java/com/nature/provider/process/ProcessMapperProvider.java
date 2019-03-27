package com.nature.provider.process;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.ProcessState;
import com.nature.common.Eunm.SysRoleType;
import com.nature.component.process.model.Process;
import com.nature.component.sysUser.model.SysRole;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProcessMapperProvider {

    /**
     * addProcess
     *
     * @param process
     * @return
     */
    public String addProcess(Process process) {
        String sqlStr = "select 0";
        if (null != process) {
            String id = process.getId();
            String crtUser = process.getCrtUser();
            Date crtDttm = process.getCrtDttm();
            String lastUpdateUser = process.getLastUpdateUser();
            Date lastUpdateDttm = process.getLastUpdateDttm();
            Long version = process.getVersion();
            Boolean enableFlag = process.getEnableFlag();
            String name = process.getName();
            String driverMemory = process.getDriverMemory();
            String executorNumber = process.getExecutorNumber();
            String executorMemory = process.getExecutorMemory();
            String executorCores = process.getExecutorCores();
            String viewXml = process.getViewXml();
            String description = process.getDescription();
            String appId = process.getAppId();
            String processId = process.getProcessId();
            ProcessState state = process.getState();
            Date startTime = process.getStartTime();
            Date endTime = process.getEndTime();
            String progress = process.getProgress();
            String flowId = process.getFlowId();
            String parentProcessId = process.getParentProcessId();

            SQL sql = new SQL();
            // INSERT_INTO括号中为数据库表名
            sql.INSERT_INTO("FLOW_PROCESS");
            // value中的第一个字符串为数据库中表对应的字段名
            // 除数字类型的字段外其他类型必须加单引号

            //先处理修改必填字段
            if (null == crtDttm) {
                crtDttm = new Date();
            }
            if (StringUtils.isBlank(crtUser)) {
                crtUser = "-1";
            }
            if (null == lastUpdateDttm) {
                lastUpdateDttm = new Date();
            }
            if (StringUtils.isBlank(lastUpdateUser)) {
                lastUpdateUser = "-1";
            }
            if (null == version) {
                version = 0L;
            }
            if (null == enableFlag) {
                enableFlag = true;
            }
            sql.VALUES("ID", SqlUtils.addSqlStrAndReplace(id));
            sql.VALUES("CRT_DTTM", SqlUtils.addSqlStrAndReplace(DateUtils.dateTimesToStr(crtDttm)));
            sql.VALUES("CRT_USER", SqlUtils.addSqlStrAndReplace(crtUser));
            sql.VALUES("LAST_UPDATE_DTTM", SqlUtils.addSqlStrAndReplace(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.VALUES("LAST_UPDATE_USER", SqlUtils.addSqlStrAndReplace(lastUpdateUser));
            sql.VALUES("VERSION", version + "");
            sql.VALUES("ENABLE_FLAG", (enableFlag ? 1 : 0) + "");

            // 处理其他字段
            if (null != name) {
                sql.VALUES("NAME", SqlUtils.addSqlStrAndReplace(name));
            }
            if (null != driverMemory) {
                sql.VALUES("DRIVER_MEMORY", SqlUtils.addSqlStrAndReplace(driverMemory));
            }
            if (null != executorNumber) {
                sql.VALUES("EXECUTOR_NUMBER", SqlUtils.addSqlStrAndReplace(executorNumber));
            }
            if (null != executorMemory) {
                sql.VALUES("EXECUTOR_MEMORY", SqlUtils.addSqlStrAndReplace(executorMemory));
            }
            if (null != executorCores) {
                sql.VALUES("EXECUTOR_CORES", SqlUtils.addSqlStrAndReplace(executorCores));
            }
            if (null != viewXml) {
                sql.VALUES("view_xml", SqlUtils.addSqlStrAndReplace(viewXml));
            }
            if (null != description) {
                sql.VALUES("DESCRIPTION", SqlUtils.addSqlStrAndReplace(description));
            }
            if (null != appId) {
                sql.VALUES("APP_ID", SqlUtils.addSqlStrAndReplace(appId));
            }
            if (null != processId) {
                sql.VALUES("PROCESS_ID", SqlUtils.addSqlStrAndReplace(processId));
            }
            if (null != state) {
                sql.VALUES("STATE", SqlUtils.addSqlStrAndReplace(state.name()));
            }
            if (null != startTime) {
                String startTimeStr = DateUtils.dateTimesToStr(startTime);
                if (StringUtils.isNotBlank(startTimeStr)) {
                    sql.VALUES("START_TIME", SqlUtils.addSqlStrAndReplace(startTimeStr));
                }
            }
            if (null != endTime) {
                String endTimeStr = DateUtils.dateTimesToStr(endTime);
                if (StringUtils.isNotBlank(endTimeStr)) {
                    sql.VALUES("END_TIME", SqlUtils.addSqlStrAndReplace(endTimeStr));
                }
            }
            if (null != progress) {
                sql.VALUES("progress", SqlUtils.addSqlStrAndReplace(progress));
            }
            if (null != flowId) {
                sql.VALUES("flow_id", SqlUtils.addSqlStrAndReplace(flowId));
            }
            if (null != parentProcessId) {
                sql.VALUES("parent_process_id", SqlUtils.addSqlStrAndReplace(parentProcessId));
            }

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * 根据进程Id查询进程
     *
     * @param map
     * @return
     */
    public String getProcessById(Map map) {
        String id = (String) map.get("id");
        String sqlStr = "select 0";
        UserVo currentUser = (UserVo) map.get("currentUser");
        if (StringUtils.isNotBlank(id) && null != currentUser) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("SELECT * ");
            strBuf.append("FROM FLOW_PROCESS ");
            strBuf.append("WHERE ENABLE_FLAG = 1 ");
            strBuf.append("AND ID= " + SqlUtils.addSqlStrAndReplace(id));
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
     * @param map
     * @return
     */
    public String getProcessListByParam(Map map) {
        String sqlStr = "SELECT 0";
        UserVo currentUser = (UserVo) map.get("currentUser");
        if (null != currentUser) {
            String param = (String) map.get("param");
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
        }

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
        sql.WHERE("FLOW_ID = " + SqlUtils.addSqlStr(flowId));
        sql.WHERE("STATE = " + SqlUtils.addSqlStr(ProcessState.STARTED.name()));
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
            sql.WHERE("APP_ID = " + SqlUtils.addSqlStrAndReplace(appID));
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
        if (null != process) {
            String id = process.getId();
            String lastUpdateUser = process.getLastUpdateUser();
            Date lastUpdateDttm = process.getLastUpdateDttm();
            Long version = process.getVersion();
            Boolean enableFlag = process.getEnableFlag();
            String name = process.getName();
            String driverMemory = process.getDriverMemory();
            String executorNumber = process.getExecutorNumber();
            String executorMemory = process.getExecutorMemory();
            String executorCores = process.getExecutorCores();
            String viewXml = process.getViewXml();
            String description = process.getDescription();
            String appId = process.getAppId();
            String processId = process.getProcessId();
            ProcessState state = process.getState();
            Date startTime = process.getStartTime();
            Date endTime = process.getEndTime();
            String progress = process.getProgress();
            SQL sql = new SQL();
            sql.UPDATE("FLOW_PROCESS");

            //先处理修改必填字段
            if (null == lastUpdateDttm) {
                lastUpdateDttm = new Date();
            }
            if (StringUtils.isBlank(lastUpdateUser)) {
                lastUpdateUser = "-1";
            }
            if (null == version) {
                version = 0L;
            }
            String lastUpdateDttmStr = DateUtils.dateTimesToStr(lastUpdateDttm);
            sql.SET("LAST_UPDATE_DTTM = " + SqlUtils.addSqlStr(lastUpdateDttmStr));
            sql.SET("LAST_UPDATE_USER = " + SqlUtils.addSqlStr(lastUpdateUser));
            sql.SET("VERSION = " + (version + 1));

            // 处理其他字段
            if (null != enableFlag) {
                sql.SET("ENABLE_FLAG=" + (enableFlag ? 1 : 0));
            }
            if (null != name) {
                sql.SET("NAME=" + SqlUtils.addSqlStrAndReplace(name));
            }
            if (null != driverMemory) {
                sql.SET("DRIVER_MEMORY=" + SqlUtils.addSqlStrAndReplace(driverMemory));
            }
            if (null != executorNumber) {
                sql.SET("EXECUTOR_NUMBER=" + SqlUtils.addSqlStrAndReplace(executorNumber));
            }
            if (null != executorMemory) {
                sql.SET("EXECUTOR_MEMORY=" + SqlUtils.addSqlStrAndReplace(executorMemory));
            }
            if (null != executorCores) {
                sql.SET("EXECUTOR_CORES=" + SqlUtils.addSqlStrAndReplace(executorCores));
            }
            if (null != viewXml) {
                sql.SET("view_xml=" + SqlUtils.addSqlStrAndReplace(viewXml));
            }
            if (null != description) {
                sql.SET("DESCRIPTION=" + SqlUtils.addSqlStrAndReplace(description));
            }
            if (null != appId) {
                sql.SET("APP_ID=" + SqlUtils.addSqlStrAndReplace(appId));
            }
            if (null != processId) {
                sql.SET("PROCESS_ID=" + SqlUtils.addSqlStrAndReplace(processId));
            }
            if (null != state) {
                sql.SET("STATE=" + SqlUtils.addSqlStrAndReplace(state.name()));
            }
            if (null != startTime) {
                String startTimeStr = DateUtils.dateTimesToStr(startTime);
                if (StringUtils.isNotBlank(startTimeStr)) {
                    sql.SET("START_TIME=" + SqlUtils.addSqlStrAndReplace(startTimeStr));
                }
            }
            if (null != endTime) {
                String endTimeStr = DateUtils.dateTimesToStr(endTime);
                if (StringUtils.isNotBlank(endTimeStr)) {
                    sql.SET("END_TIME=" + SqlUtils.addSqlStrAndReplace(endTimeStr));
                }
            }
            if (null != progress) {
                sql.SET("progress=" + SqlUtils.addSqlStrAndReplace(progress));
            }
            sql.WHERE("VERSION = " + version);
            sql.WHERE("id = " + SqlUtils.addSqlStr(id));
            if (StringUtils.isNotBlank(id)) {
                sqlStr = sql.toString();
            }
        }
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
            SQL sql = new SQL();
            StringBuffer sqlStrBuf = new StringBuffer();
            sqlStrBuf.append("UPDATE FLOW_PROCESS ");
            sqlStrBuf.append("SET LAST_UPDATE_DTTM = " + SqlUtils.addSqlStr(DateUtils.dateTimesToStr(new Date())));
            sqlStrBuf.append("SET LAST_UPDATE_DTTM = " + SqlUtils.addSqlStr(DateUtils.dateTimesToStr(new Date())));
            sqlStrBuf.append("SET LAST_UPDATE_USER = " + SqlUtils.addSqlStr(username));
            sqlStrBuf.append("SET VERSION=(VERSION+1) ");
            sqlStrBuf.append("SET ENABLE_FLAG = 0 ");
            sqlStrBuf.append("WHERE ENABLE_FLAG = 1 ");
            sqlStrBuf.append("WHERE ID = " + SqlUtils.addSqlStrAndReplace(id));

            sqlStr = sql.toString();
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
        sqlStrBuf.append("STATE = " + SqlUtils.addSqlStr(ProcessState.STARTED.getText()));
        sqlStrBuf.append("OR ");
        sqlStrBuf.append("( ");
        sqlStrBuf.append("STATE = " + SqlUtils.addSqlStr(ProcessState.COMPLETED.getText()));
        sqlStrBuf.append("AND ");
        sqlStrBuf.append("END_TIME IS NULL ");
        sqlStrBuf.append(") ");
        sqlStrBuf.append(") ");
        return sqlStrBuf.toString();
    }

}
