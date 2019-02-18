package com.nature.provider.process;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
import com.nature.common.Eunm.ProcessState;
import com.nature.component.process.model.Process;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
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
            sql.VALUES("ID", Utils.addSqlStrAndReplace(id));
            sql.VALUES("CRT_DTTM", Utils.addSqlStrAndReplace(DateUtils.dateTimesToStr(crtDttm)));
            sql.VALUES("CRT_USER", Utils.addSqlStrAndReplace(crtUser));
            sql.VALUES("LAST_UPDATE_DTTM", Utils.addSqlStrAndReplace(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.VALUES("LAST_UPDATE_USER", Utils.addSqlStrAndReplace(lastUpdateUser));
            sql.VALUES("VERSION", version + "");
            sql.VALUES("ENABLE_FLAG", (enableFlag ? 1 : 0) + "");

            // 处理其他字段
            if (null != name) {
                sql.VALUES("NAME", Utils.addSqlStrAndReplace(name));
            }
            if (null != driverMemory) {
                sql.VALUES("DRIVER_MEMORY", Utils.addSqlStrAndReplace(driverMemory));
            }
            if (null != executorNumber) {
                sql.VALUES("EXECUTOR_NUMBER", Utils.addSqlStrAndReplace(executorNumber));
            }
            if (null != executorMemory) {
                sql.VALUES("EXECUTOR_MEMORY", Utils.addSqlStrAndReplace(executorMemory));
            }
            if (null != executorCores) {
                sql.VALUES("EXECUTOR_CORES", Utils.addSqlStrAndReplace(executorCores));
            }
            if (null != viewXml) {
                sql.VALUES("view_xml", Utils.addSqlStrAndReplace(viewXml));
            }
            if (null != description) {
                sql.VALUES("DESCRIPTION", Utils.addSqlStrAndReplace(description));
            }
            if (null != appId) {
                sql.VALUES("APP_ID", Utils.addSqlStrAndReplace(appId));
            }
            if (null != processId) {
                sql.VALUES("PROCESS_ID", Utils.addSqlStrAndReplace(processId));
            }
            if (null != state) {
                sql.VALUES("STATE", Utils.addSqlStrAndReplace(state.name()));
            }
            if (null != startTime) {
                String startTimeStr = DateUtils.dateTimesToStr(startTime);
                if (StringUtils.isNotBlank(startTimeStr)) {
                    sql.VALUES("START_TIME", Utils.addSqlStrAndReplace(startTimeStr));
                }
            }
            if (null != endTime) {
                String endTimeStr = DateUtils.dateTimesToStr(endTime);
                if (StringUtils.isNotBlank(endTimeStr)) {
                    sql.VALUES("END_TIME", Utils.addSqlStrAndReplace(endTimeStr));
                }
            }
            if (null != progress) {
                sql.VALUES("progress", Utils.addSqlStrAndReplace(progress));
            }
            if (null != flowId) {
                sql.VALUES("flow_id", Utils.addSqlStrAndReplace(flowId));
            }
            if (null != parentProcessId) {
                sql.VALUES("parent_process_id", Utils.addSqlStrAndReplace(parentProcessId));
            }

            sqlStr = sql.toString() + ";";
        }
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
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("FLOW_PROCESS");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("id = " + Utils.addSqlStrAndReplace(id));

            sqlStr = sql.toString() + ";";
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
        sql.ORDER_BY("CRT_DTTM DESC,LAST_UPDATE_DTTM DESC");
        return sql.toString() + ";";
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
    sql.WHERE("ENABLE_FLAG = 1");
    sql.WHERE("FLOW_ID = " + Utils.addSqlStr(flowId));
    sql.WHERE("STATE = " + Utils.addSqlStr(ProcessState.STARTED.name()));
    sql.ORDER_BY("CRT_DTTM DESC,LAST_UPDATE_DTTM DESC");
    return sql.toString() + ";";
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
            sql.WHERE("enable_flag = 1");
            sql.WHERE("app_id = " + Utils.addSqlStrAndReplace(appID));

            sqlStr = sql.toString() + ";";
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
            String appIDsStr = Utils.strArrayToStr(appIDs);
            if (StringUtils.isNotBlank(appIDsStr)) {
                appIDsStr = appIDsStr.replace(",", "','");
                appIDsStr = "'" + appIDsStr + "'";

                sql.SELECT("*");
                sql.FROM("FLOW_PROCESS");
                sql.WHERE("enable_flag = 1");
                sql.WHERE("app_id in (" + appIDsStr + ")");

                sqlStr = sql.toString() + ";";
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
            sql.SET("LAST_UPDATE_DTTM = " + Utils.addSqlStr(lastUpdateDttmStr));
            sql.SET("LAST_UPDATE_USER = " + Utils.addSqlStr(lastUpdateUser));
            sql.SET("VERSION = " + (version + 1));

            // 处理其他字段
            if (null != enableFlag) {
                sql.SET("ENABLE_FLAG=" + (enableFlag ? 1 : 0));
            }
            if (null != name) {
                sql.SET("NAME=" + Utils.addSqlStrAndReplace(name));
            }
            if (null != driverMemory) {
                sql.SET("DRIVER_MEMORY=" + Utils.addSqlStrAndReplace(driverMemory));
            }
            if (null != executorNumber) {
                sql.SET("EXECUTOR_NUMBER=" + Utils.addSqlStrAndReplace(executorNumber));
            }
            if (null != executorMemory) {
                sql.SET("EXECUTOR_MEMORY=" + Utils.addSqlStrAndReplace(executorMemory));
            }
            if (null != executorCores) {
                sql.SET("EXECUTOR_CORES=" + Utils.addSqlStrAndReplace(executorCores));
            }
            if (null != viewXml) {
                sql.SET("view_xml=" + Utils.addSqlStrAndReplace(viewXml));
            }
            if (null != description) {
                sql.SET("DESCRIPTION=" + Utils.addSqlStrAndReplace(description));
            }
            if (null != appId) {
                sql.SET("APP_ID=" + Utils.addSqlStrAndReplace(appId));
            }
            if (null != processId) {
                sql.SET("PROCESS_ID=" + Utils.addSqlStrAndReplace(processId));
            }
            if (null != state) {
                sql.SET("STATE=" + Utils.addSqlStrAndReplace(state.name()));
            }
            if (null != startTime) {
                String startTimeStr = DateUtils.dateTimesToStr(startTime);
                if (StringUtils.isNotBlank(startTimeStr)) {
                    sql.SET("START_TIME=" + Utils.addSqlStrAndReplace(startTimeStr));
                }
            }
            if (null != endTime) {
                String endTimeStr = DateUtils.dateTimesToStr(endTime);
                if (StringUtils.isNotBlank(endTimeStr)) {
                    sql.SET("END_TIME=" + Utils.addSqlStrAndReplace(endTimeStr));
                }
            }
            if (null != progress) {
                sql.SET("progress=" + Utils.addSqlStrAndReplace(progress));
            }
            sql.WHERE("VERSION = " + version);
            sql.WHERE("id = " + Utils.addSqlStr(id));
            if (StringUtils.isNotBlank(id)) {
                sqlStr = sql.toString() + ";";
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
            sql.UPDATE("FLOW_PROCESS");
            sql.SET("LAST_UPDATE_DTTM = " + Utils.addSqlStr(DateUtils.dateTimesToStr(new Date())));
            sql.SET("LAST_UPDATE_USER = " + Utils.addSqlStr(username));
            sql.SET("VERSION=(VERSION+1)");
            sql.SET("ENABLE_FLAG = 0");
            sql.WHERE("ENABLE_FLAG = 1");
            sql.WHERE("ID = " + Utils.addSqlStrAndReplace(id));

            sqlStr = sql.toString() + ";";
        }
        return sqlStr;
    }

}
