package com.nature.provider.process;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SqlUtils;
import org.apache.commons.lang3.StringUtils;
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
    public String getProcessGroupById(String processGroupId) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(processGroupId)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from flow_process_group ");
            strBuf.append("where enable_flag = 1 ");
            strBuf.append("and id= " + SqlUtils.preventSQLInjection(processGroupId));
            strBuf.append(SqlUtils.addQueryByUserRole(true, false));
            sqlStr = strBuf.toString();
        }
        return sqlStr;
    }

    /**
     * getRunModeTypeById
     *
     * @param processGroupId
     * @return
     */
    public String getRunModeTypeById(String processGroupId) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(processGroupId)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select run_mode_type ");
            strBuf.append("from flow_process_group ");
            strBuf.append("where enable_flag = 1 ");
            strBuf.append("and id= " + SqlUtils.preventSQLInjection(processGroupId));
            strBuf.append(SqlUtils.addQueryByUserRole(true, false));
            sqlStr = strBuf.toString();
        }
        return sqlStr;
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
        sqlStrBuf.append("select ");
        sqlStrBuf.append("app_id ");
        sqlStrBuf.append("from ");
        sqlStrBuf.append("flow_process_group ");
        sqlStrBuf.append("where ");
        sqlStrBuf.append("enable_flag = 1 ");
        sqlStrBuf.append("and app_id is not null ");
        sqlStrBuf.append("and ( ");
        sqlStrBuf.append("state = 'STARTED' ");
        sqlStrBuf.append("or ( state = 'COMPLETED'and end_time is null ) ");
        sqlStrBuf.append(") ");
        return sqlStrBuf.toString();
    }

    /**
     * Query process list according to param(processList)
     *
     * @param param
     * @return
     */
    public String getProcessGroupListByParam(String param) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from flow_process_group ");
        strBuf.append("where ");
        strBuf.append("enable_flag = 1 ");
        strBuf.append("and app_id is not null ");
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
     * Query processGroup list
     *
     * @return
     */
    public String getProcessGroupList() {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from flow_process_group ");
        strBuf.append("where ");
        strBuf.append("enable_flag = 1 ");
        strBuf.append("and app_id is not null ");
        strBuf.append(SqlUtils.addQueryByUserRole(true, false));
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
