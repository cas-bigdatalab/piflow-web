package cn.cnic.component.process.mapper.provider;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class ProcessGroupPathMapperProvider {

    /**
     * Query processGroupPath according to processGroup Id
     *
     * @param processGroupId
     * @return
     */
    public String getProcessPathByProcessGroupId(String processGroupId) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(processGroupId)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("flow_process_group_path");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_group_id = " + SqlUtils.preventSQLInjection(processGroupId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    public String updateEnableFlagByProcessGroupId(String processGroupId, String userName) {
        String sqlStr = "select 0";
        if (!StringUtils.isAnyEmpty(processGroupId, userName)) {
            SQL sql = new SQL();
            sql.UPDATE("flow_process_group_path");
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
            sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(userName));
            sql.SET("version=(version+1)");
            sql.SET("enable_flag = 0");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_group_id = " + SqlUtils.preventSQLInjection(processGroupId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * Query based on processGroupId and pageId
     *
     * @param processGroupId
     * @param pageId
     * @return
     */
    public String getProcessPathByPageIdAndProcessGroupId(String processGroupId, String pageId) {
        String sqlStr = "select 0";
        if (!StringUtils.isAnyEmpty(processGroupId, pageId)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("flow_process_group_path");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_group_id = " + SqlUtils.preventSQLInjection(processGroupId));
            sql.WHERE("page_id = " + SqlUtils.preventSQLInjection(pageId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }
}
