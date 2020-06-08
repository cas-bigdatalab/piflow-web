package cn.cnic.provider.process;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.common.Eunm.ProcessState;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.Map;

public class ProcessGroupMapperProvider {

    /**
     * Query processGroup by processGroup ID
     *
     * @param processGroupId
     * @return
     */
    public String getProcessGroupById(String username, boolean isAdmin, String processGroupId) {
        if (StringUtils.isBlank(processGroupId)) {
            return "select 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from flow_process_group ");
        strBuf.append("where enable_flag = 1 ");
        strBuf.append("and id= " + SqlUtils.preventSQLInjection(processGroupId));
        if (!isAdmin) {
            strBuf.append("and crt_user = " + SqlUtils.preventSQLInjection(username));
        }
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
    public String getProcessGroupListByAppIDs(Map<String, String[]> map) {
        String sqlStr = "select 0";
        String[] appIDs = map.get("appIDs");
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
