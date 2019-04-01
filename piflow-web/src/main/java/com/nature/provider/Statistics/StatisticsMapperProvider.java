package com.nature.provider.Statistics;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.ProcessState;
import com.nature.component.Statistics.model.Statistics;
import com.nature.component.process.model.Process;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.Map;

public class StatisticsMapperProvider {

    private String id;
    private String loginUser;
    private String loginIp;
    private String loginTimeStr;

    private void preventSQLInjectionStatistics(Statistics statistics) {
        if (null != statistics) {
            // Mandatory Field
            String id = statistics.getId();
            String loginUser = statistics.getLoginUser();
            String loginIp = statistics.getLoginIp();
            Date loginTime = statistics.getLoginTime();
            this.id = SqlUtils.preventSQLInjection(null == id ? SqlUtils.getUUID32() : id);
            this.loginUser = (null != loginUser ? SqlUtils.preventSQLInjection(loginUser) : null);
            this.loginIp = SqlUtils.preventSQLInjection(loginIp);
            String loginTimeStr = DateUtils.dateTimesToStr(loginTime);
            this.loginTimeStr = (null != loginTimeStr ? SqlUtils.preventSQLInjection(loginTimeStr) : null);
        }
    }

    private void reset() {
        this.id = null;
        this.loginUser = null;
        this.loginIp = null;
        this.loginTimeStr = null;
    }

    /**
     * addProcess
     *
     * @param statistics
     * @return
     */
    public String addStatistics(Statistics statistics) {
        String sqlStr = "select 0";
        this.preventSQLInjectionStatistics(statistics);
        if (null != statistics) {
            SQL sql = new SQL();
            // INSERT_INTO括号中为数据库表名
            sql.INSERT_INTO("STATISTICS");

            sql.VALUES("ID", id);
            sql.VALUES("LOGIN_IP", loginIp);
            sql.VALUES("LOGIN_USER", loginUser);
            sql.VALUES("LOGIN_TIME", loginTimeStr);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

}
