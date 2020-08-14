package cn.cnic.component.process.mapper.provider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.cnic.common.Eunm.ProcessParentType;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.entity.ProcessGroupPath;
import cn.cnic.component.schedule.entity.Schedule;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.common.Eunm.ProcessState;
import org.hibernate.annotations.Where;
import sun.rmi.runtime.Log;

import javax.persistence.*;

public class ProcessGroupMapperProvider {


    private String id;
    private Integer enableFlag;
    private Long version;
    private String lastUpdateDataTimeStr;
    private String lastUpdateUser;
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
        // Mandatory Field
        String lastUpdateDttm = DateUtils.dateTimesToStr(null != processGroup.getLastUpdateDttm() ? processGroup.getLastUpdateDttm() : new Date());
        this.id = SqlUtils.preventSQLInjection(processGroup.getId());
        this.version = (null != processGroup.getVersion() ? processGroup.getVersion() : 0L);
        this.enableFlag = ((null != processGroup.getEnableFlag() && processGroup.getEnableFlag()) ? 1 : 0);
        this.lastUpdateDataTimeStr = SqlUtils.preventSQLInjection(lastUpdateDttm);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(processGroup.getLastUpdateUser());

        // Selection field
        this.name = SqlUtils.preventSQLInjection(processGroup.getName());


        return true;
    }

    private void resetProcessGroup() {
        this.id = null;
        this.lastUpdateUser = null;
        this.lastUpdateDataTimeStr = null;
        this.enableFlag = 1;
        this.version = 0L;

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
            strBuf.append("INSERT INTO group_schedule ");
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
            strBuf.append(this.viewXml + ", ");
            strBuf.append(")");
            this.resetProcessGroup();
            return strBuf.toString() + ";";

        }
        return "select 0";
    }

    /**
     * Query processGroup by processGroup ID
     *
     * @param id
     * @return
     */
    public String getProcessGroupByIdAndUser(String username, boolean isAdmin, String id) {
        if (StringUtils.isBlank(id)) {
            return "select 0";
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
            return "select 0";
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
            return "select 0";
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
            return "select 0";
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
        String sqlStr = "select 0";
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
     * Query processGroup list according to the process AppId array
     *
     * @param map
     * @return
     */
    public String getProcessGroupListByAppIDs(Map map) {
        String sqlStr = "select 0";
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
        strBuf.append("select * ");
        strBuf.append("from flow_process_group ");
        strBuf.append("where ");
        strBuf.append("enable_flag = 1 ");
        strBuf.append("and app_id is not null ");
        strBuf.append("and fk_flow_process_group_id is null ");
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("and ( ");
            strBuf.append("app_id like '%" + param + "%' ");
            strBuf.append("or name like '%" + param + "%' ");
            strBuf.append("or state like '%" + param + "%' ");
            strBuf.append("or description like '%" + param + "%' ");
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
        String sqlStr = "select 0";
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
