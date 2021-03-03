package cn.cnic.component.process.mapper.provider;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.component.process.entity.ProcessGroup;

public class ProcessGroupMapperProvider {


    private String name;
    private String description;
    private String pageId;
    private String flowId;
    private String appId;
    private String parentProcessId;
    private String processId;
    private String stateStr;
    private String startTimeStr;
    private String endTimeStr;
    private String progress;
    private String runModeType;
    private String processParentType;
    private String processGroupId;
    private String viewXml;

    private boolean preventSQLInjectionProcessGroup(ProcessGroup processGroup) {
        if (null == processGroup || StringUtils.isBlank(processGroup.getLastUpdateUser())) {
            return false;
        }
        // Selection field
        this.name = SqlUtils.preventSQLInjection(processGroup.getName());
        this.description = SqlUtils.preventSQLInjection(processGroup.getDescription());
        this.pageId = SqlUtils.preventSQLInjection(processGroup.getPageId());
        this.flowId = SqlUtils.preventSQLInjection(processGroup.getFlowId());
        this.appId = SqlUtils.preventSQLInjection(processGroup.getAppId());
        this.parentProcessId = SqlUtils.preventSQLInjection(processGroup.getParentProcessId());
        this.processId = SqlUtils.preventSQLInjection(processGroup.getProcessId());
        String state = (null == processGroup.getState() ? null : processGroup.getState().name());
        this.stateStr = SqlUtils.preventSQLInjection(state);
        String startTime = (null != processGroup.getStartTime() ? DateUtils.dateTimesToStr(processGroup.getStartTime()) : null);
        this.startTimeStr = SqlUtils.preventSQLInjection(startTime);
        String endTime = (null != processGroup.getEndTime() ? DateUtils.dateTimesToStr(processGroup.getEndTime()) : null);
        this.endTimeStr = SqlUtils.preventSQLInjection(endTime);
        this.progress = SqlUtils.preventSQLInjection(processGroup.getProgress());
        this.runModeType = SqlUtils.preventSQLInjection(null != processGroup.getRunModeType() ? processGroup.getRunModeType().name() : null);
        this.processParentType = SqlUtils.preventSQLInjection(null != processGroup.getProcessParentType() ? processGroup.getProcessParentType().name() : null);
        this.processGroupId = SqlUtils.preventSQLInjection(null == processGroup.getProcessGroup() ? null : processGroup.getProcessGroup().getId());
        this.viewXml = SqlUtils.preventSQLInjection(processGroup.getViewXml());

        return true;
    }

    private void resetProcessGroup() {
        this.name = null;
        this.description = null;
        this.pageId = null;
        this.flowId = null;
        this.appId = null;
        this.parentProcessId = null;
        this.processId = null;
        this.stateStr = null;
        this.startTimeStr = null;
        this.endTimeStr = null;
        this.progress = null;
        this.runModeType = "RUN";
        this.processParentType = null;
        this.processGroupId = null;
        this.viewXml = null;
    }


    public String addProcessGroup(ProcessGroup processGroup) {
        boolean flag = this.preventSQLInjectionProcessGroup(processGroup);
        if (flag) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO flow_process_group ");
            strBuf.append("( ");
            strBuf.append(SqlUtils.baseFieldName() + ", ");
            strBuf.append("name, ");
            strBuf.append("description, ");
            strBuf.append("page_id, ");
            strBuf.append("flow_id, ");
            strBuf.append("app_id, ");
            strBuf.append("parent_process_id, ");
            strBuf.append("process_id, ");
            strBuf.append("state, ");
            strBuf.append("start_time, ");
            strBuf.append("end_time, ");
            strBuf.append("progress, ");
            strBuf.append("run_mode_type, ");
            strBuf.append("process_parent_type, ");
            strBuf.append("fk_flow_process_group_id, ");
            strBuf.append("view_xml ");
            strBuf.append(") ");

            strBuf.append("values ");
            strBuf.append("(");
            strBuf.append(SqlUtils.baseFieldValues(processGroup) + ", ");
            strBuf.append(this.name + ", ");
            strBuf.append(this.description + ", ");
            strBuf.append(this.pageId + ", ");
            strBuf.append(this.flowId + ", ");
            strBuf.append(this.appId + ", ");
            strBuf.append(this.parentProcessId + ", ");
            strBuf.append(this.processId + ",");
            strBuf.append(this.stateStr + ", ");
            strBuf.append(this.startTimeStr + ", ");
            strBuf.append(this.endTimeStr + ", ");
            strBuf.append(this.progress + ", ");
            strBuf.append(this.runModeType + ", ");
            strBuf.append(this.processParentType + ", ");
            strBuf.append(this.processGroupId + ", ");
            strBuf.append(this.viewXml + " ");
            strBuf.append(")");
            this.resetProcessGroup();
            return strBuf.toString() + ";";

        }
        return "SELECT 0";
    }

    /**
     * Query processGroup by processGroup ID
     *
     * @param id
     * @return
     */
    public String getProcessGroupByIdAndUser(String username, boolean isAdmin, String id) {
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from flow_process_group ");
        strBuf.append("where enable_flag = 1 ");
        strBuf.append("and id= " + SqlUtils.preventSQLInjection(id));
        if (!isAdmin) {
            strBuf.append("and crt_user = " + SqlUtils.preventSQLInjection(username));
        }
        return strBuf.toString();
    }

    /**
     * Query processGroup by processGroup ID
     *
     * @param id
     * @return
     */
    public String getProcessGroupById(String id) {
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from flow_process_group ");
        strBuf.append("where enable_flag = 1 ");
        strBuf.append("and id= " + SqlUtils.preventSQLInjection(id));
        return strBuf.toString();
    }


    /**
     * Query processGroup by processGroupID
     *
     * @param processGroupId
     * @return
     */
    public String getProcessGroupByProcessGroupId(String processGroupId) {
        if (StringUtils.isBlank(processGroupId)) {
            return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from flow_process_group ");
        strBuf.append("where enable_flag = 1 ");
        strBuf.append("and fk_flow_process_group_id= " + SqlUtils.preventSQLInjection(processGroupId));
        return strBuf.toString();
    }

    /**
     * getRunModeTypeById
     *
     * @param processGroupId
     * @return
     */
    public String getRunModeTypeById(String username, boolean isAdmin, String processGroupId) {
        if (StringUtils.isBlank(processGroupId)) {
            return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select run_mode_type ");
        strBuf.append("from flow_process_group ");
        strBuf.append("where enable_flag = 1 ");
        strBuf.append("and id= " + SqlUtils.preventSQLInjection(processGroupId));
        if (!isAdmin) {
            strBuf.append("and crt_user = " + SqlUtils.preventSQLInjection(username));
        }
        return strBuf.toString();
    }

    /**
     * Query processGroup according to processGroup appId
     *
     * @param appID
     * @return
     */
    public String getProcessGroupByAppId(String appID) {
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(appID)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("flow_process_group");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("app_id = " + SqlUtils.preventSQLInjection(appID));
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * Query id according to processGroup appId
     *
     * @param appID
     * @return
     */
    public String getProcessGroupIdByAppId(String appID) {
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(appID)) {
            SQL sql = new SQL();
            sql.SELECT("id");
            sql.FROM("flow_process_group");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("app_id = " + SqlUtils.preventSQLInjection(appID));
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * Query processGroup list according to the process AppId array
     *
     * @param map
     * @return
     */
    public String getProcessGroupListByAppIDs(@SuppressWarnings("rawtypes") Map map) {
        String sqlStr = "SELECT 0";
        String[] appIDs = (String[]) map.get("appIDs");
        if (null != appIDs && appIDs.length > 0) {
            SQL sql = new SQL();
            String appIDsStr = SqlUtils.strArrayToStr(appIDs);
            if (StringUtils.isNotBlank(appIDsStr)) {
                //appIDsStr = appIDsStr.replace(",", "','");
                //appIDsStr = "'" + appIDsStr + "'";

                sql.SELECT("*");
                sql.FROM("flow_process_group");
                sql.WHERE("enable_flag = 1");
                sql.WHERE("app_id in (" + appIDsStr + ")");

                sqlStr = sql.toString();
            }
        }
        return sqlStr;
    }

    public String getRunningProcessGroup() {
        StringBuffer sqlStrBuf = new StringBuffer();
        sqlStrBuf.append("select app_id from flow_process_group ");
        sqlStrBuf.append("where ");
        sqlStrBuf.append("enable_flag = 1 ");
        sqlStrBuf.append("and app_id is not null ");
        sqlStrBuf.append("and ( ");
        sqlStrBuf.append("state!=" + SqlUtils.preventSQLInjection(ProcessState.COMPLETED.name()) + " ");
        sqlStrBuf.append("and ");
        sqlStrBuf.append("state!=" + SqlUtils.preventSQLInjection(ProcessState.FAILED.name()) + "  ");
        sqlStrBuf.append("and ");
        sqlStrBuf.append("state!=" + SqlUtils.preventSQLInjection(ProcessState.KILLED.name()) + " ");
        sqlStrBuf.append(") ");
        return sqlStrBuf.toString();
    }

    /**
     * Query process list according to param(processList)
     *
     * @param param
     * @return
     */
    public String getProcessGroupListByParam(String username, boolean isAdmin, String param) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("SELECT * ");
        strBuf.append("FROM flow_process_group ");
        strBuf.append("WHERE ");
        strBuf.append("enable_flag = 1 ");
        strBuf.append("AND app_id IS NOT NULL ");
        strBuf.append("AND fk_flow_process_group_id IS NULL ");
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("AND ( ");
            strBuf.append("app_id LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            strBuf.append("OR name LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            strBuf.append("OR state LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            strBuf.append("OR description LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            strBuf.append(") ");
        }
        if (!isAdmin) {
            strBuf.append("and crt_user = " + SqlUtils.preventSQLInjection(username));
        }
        strBuf.append("order by crt_dttm desc,last_update_dttm desc ");

        return strBuf.toString();
    }

    /**
     * Query processGroup list
     *
     * @return
     */
    public String getProcessGroupList(String username, boolean isAdmin) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from flow_process_group ");
        strBuf.append("where ");
        strBuf.append("enable_flag = 1 ");
        strBuf.append("and app_id is not null ");
        if (!isAdmin) {
            strBuf.append("and crt_user = " + SqlUtils.preventSQLInjection(username));
        }
        strBuf.append("order by crt_dttm desc,last_update_dttm desc ");

        return strBuf.toString();
    }

    public String updateEnableFlagById(String id, String userName) {
        String sqlStr = "SELECT 0";
        if (!StringUtils.isAnyEmpty(id, userName)) {
            SQL sql = new SQL();
            sql.UPDATE("flow_process_group");
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
            sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(userName));
            sql.SET("version=(version+1)");
            sql.SET("enable_flag = 0");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

}
