package cn.cnic.provider.system;

import cn.cnic.base.util.SqlUtils;
import org.apache.commons.lang3.StringUtils;

public class SysScheduleMapperProvider {


    /**
     * getSysScheduleList
     *
     * @param isAdmin
     * @param param
     * @return
     */
    public String getSysScheduleList(boolean isAdmin, String param) {
        String sqlStr = "select 0";
        if (isAdmin) {
            StringBuffer sqlStrbuf = new StringBuffer();
            sqlStrbuf.append("SELECT * ");
            sqlStrbuf.append("FROM sys_schedule ");
            sqlStrbuf.append("WHERE enable_flag = 1 ");
            if (StringUtils.isNotBlank(param)) {
                String paramLike = SqlUtils.addSqlStrLikeAndReplace(param);
                sqlStrbuf.append("AND ");
                sqlStrbuf.append("( ");
                sqlStrbuf.append("job_name like " + paramLike + " OR ");
                sqlStrbuf.append("job_class like " + paramLike + " OR ");
                sqlStrbuf.append("status like " + paramLike + " OR ");
                sqlStrbuf.append("cron_expression like " + paramLike + " ");
                sqlStrbuf.append(") ");
            }
            sqlStrbuf.append("ORDER BY crt_dttm asc,last_update_dttm DESC");
            sqlStr = sqlStrbuf.toString();
        }
        return sqlStr;
    }

    /**
     * getSysScheduleById
     *
     * @param id
     * @return
     */
    public String getSysScheduleById(boolean isAdmin, String id) {
        String sqlStr = "select 0";
        if (isAdmin && StringUtils.isNotBlank(id)) {
            StringBuffer sqlStrbuf = new StringBuffer();
            sqlStrbuf.append("SELECT * ");
            sqlStrbuf.append("FROM sys_schedule ");
            sqlStrbuf.append("WHERE enable_flag = 1 ");
            sqlStrbuf.append("AND ");
            sqlStrbuf.append("id = " + SqlUtils.addSqlStrAndReplace(id) + " ");
            sqlStr = sqlStrbuf.toString();
        }
        return sqlStr;
    }

}
