package com.nature.provider.system;

import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.common.Eunm.SysRoleType;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

public class SysScheduleMapperProvider {


    /**
     * getSysScheduleList
     *
     * @param param
     * @return
     */
    public String getSysScheduleList(String param) {
        String sqlStr = "select 0";
        boolean admin = SessionUserUtil.isAdmin();
        if (admin) {
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
                sqlStrbuf.append("cron_expression like " + paramLike  + " ");
                sqlStrbuf.append(") ");
            }
            sqlStrbuf.append("ORDER BY crt_dttm asc,last_update_dttm DESC");
            sqlStr = sqlStrbuf.toString();
        }
        return sqlStr;
    }
}
