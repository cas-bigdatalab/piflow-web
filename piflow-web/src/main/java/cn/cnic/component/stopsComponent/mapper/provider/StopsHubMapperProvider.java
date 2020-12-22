package cn.cnic.component.stopsComponent.mapper.provider;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.stopsComponent.model.StopsHub;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class StopsHubMapperProvider {

    private String id;
    private String crtUser;
    private String crtDttmStr;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String mountId;
    private String jarName;
    private String jarUrl;
    private String status;

    private void preventSQLInjectionStopsHub(StopsHub stopsHub) {
        if (null != stopsHub && StringUtils.isNotBlank(stopsHub.getLastUpdateUser())) {
            // Mandatory Field
            String id = stopsHub.getId();
            String crtUser = stopsHub.getCrtUser();
            String lastUpdateUser = stopsHub.getLastUpdateUser();
            Boolean enableFlag = stopsHub.getEnableFlag();
            Long version = stopsHub.getVersion();
            Date crtDttm = stopsHub.getCrtDttm();
            Date lastUpdateDttm = stopsHub.getLastUpdateDttm();
            this.id = SqlUtils.preventSQLInjection(id);
            this.crtUser = (null != crtUser ? SqlUtils.preventSQLInjection(crtUser) : null);
            this.lastUpdateUser = SqlUtils.preventSQLInjection(lastUpdateUser);
            this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
            this.version = (null != version ? version : 0L);
            String crtDttmStr = DateUtils.dateTimesToStr(crtDttm);
            String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
            this.crtDttmStr = (null != crtDttm ? SqlUtils.preventSQLInjection(crtDttmStr) : null);
            this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

            // Selection field
            this.mountId = SqlUtils.preventSQLInjection(stopsHub.getMountId());
            this.jarName = SqlUtils.preventSQLInjection(stopsHub.getJarName());
            this.jarUrl = SqlUtils.preventSQLInjection(stopsHub.getJarUrl());
            this.status = SqlUtils.preventSQLInjection(null != stopsHub.getStatus() ? stopsHub.getStatus().name() : null);

        }
    }

    private void reset() {
        this.id = null;
        this.crtUser = null;
        this.crtDttmStr = null;
        this.lastUpdateDttmStr = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.mountId = null;
        this.jarName = null;
        this.jarUrl = null;
        this.status = null;
    }

    /**
     * add StopsHub
     *
     * @param stopsHub
     * @return
     */
    public String addStopsHub(StopsHub stopsHub) {
        String sqlStr = "";
        this.preventSQLInjectionStopsHub(stopsHub);
        if (null != stopsHub) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.INSERT_INTO("stops_hub");

            //Process the required fields firsts
            if (null == crtDttmStr) {
                String crtDttm = DateUtils.dateTimesToStr(new Date());
                crtDttmStr = SqlUtils.preventSQLInjection(crtDttm);
            }
            if (StringUtils.isBlank(crtUser)) {
                crtUser = SqlUtils.preventSQLInjection("-1");
            }
            sql.VALUES("id", id);
            sql.VALUES("crt_dttm", crtDttmStr);
            sql.VALUES("crt_user", crtUser);
            sql.VALUES("last_update_dttm", lastUpdateDttmStr);
            sql.VALUES("last_update_user", lastUpdateUser);
            sql.VALUES("version", version + "");
            sql.VALUES("enable_flag", enableFlag + "");
            sql.VALUES("mount_id", mountId + "");
            sql.VALUES("jar_name", jarName + "");
            sql.VALUES("jar_url", jarUrl + "");
            sql.VALUES("status", status + "");

            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * update DataSource
     *
     * @param stopsHub
     * @return
     */
    public String updateStopsHub(StopsHub stopsHub) {

        String sqlStr = "";
        this.preventSQLInjectionStopsHub(stopsHub);
        if (null != stopsHub) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("stops_hub");
            // The first string in the SET is the name of the field corresponding to the table in the database
            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag = " + enableFlag);
            if (mountId != null)
                sql.SET("mount_id = " + mountId);
            if (jarName != null)
                sql.SET("jar_name = " + jarName);
            if (jarUrl != null)
                sql.SET("jar_url = " + jarUrl);
            if (status != null)
                sql.SET("status = " + status);
            sql.WHERE("version = " + version);
            sql.WHERE("id = " + id);
            sqlStr = sql.toString();
            if (StringUtils.isBlank(id)) {
                sqlStr = "";
            }
        }
        this.reset();
        return sqlStr;
    }

    /**
     * Query all stopsHub
     *
     * @return
     */
    public String getStopsHubList(String username, boolean isAdmin, String param) {
        String sqlStr = "SELECT 0";
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from stops_hub ");
        strBuf.append("where enable_flag = 1 ");

        if (!isAdmin) {
            strBuf.append("and crt_user = " + SqlUtils.preventSQLInjection(username));
        }
        strBuf.append("order by crt_dttm desc ");
        sqlStr = strBuf.toString();
        return sqlStr;
    }

    /**
     * Query all data sources
     *
     * @return
     */
    public String getStopsHubListByName(String username, boolean isAdmin, String jarName) {
        String sqlStr = "SELECT 0";
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from stops_hub ");
        strBuf.append("where enable_flag = 1 ");
        if (StringUtils.isNotBlank(jarName)) {
            strBuf.append("and ( ");
            strBuf.append("jar_name like CONCAT('%'," + SqlUtils.preventSQLInjection(jarName) + ",'%') ");
            strBuf.append(") ");
        }
        if (!isAdmin) {
            strBuf.append("and crt_user = " + SqlUtils.preventSQLInjection(username));
        }
        strBuf.append("order by crt_dttm desc ");
        sqlStr = strBuf.toString();
        return sqlStr;
    }

    /**
     * Query the data source according to the workflow Id
     *
     * @param id
     * @return
     */
    public String getStopsHubById(String username, boolean isAdmin, String id) {
        String sqlStr = "";
        if (StringUtils.isNotBlank(id)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from stops_hub ");
            strBuf.append("where enable_flag = 1 ");
            strBuf.append("and id = " + SqlUtils.preventSQLInjection(id) + " ");
            if (!isAdmin) {
                strBuf.append("and crt_user = " + SqlUtils.preventSQLInjection(username));
            }
            sqlStr = strBuf.toString();
        }
        return sqlStr;
    }

    /**
     * Delete according to id logic, set to invalid
     *
     * @param id
     * @return
     */
    public String updateEnableFlagById(String username, String id) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }
        SQL sql = new SQL();
        sql.UPDATE("stops_hub");
        sql.SET("enable_flag = 0");
        sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
        sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
        sql.WHERE("enable_flag = 1");
        sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));

        return sql.toString();
    }

    /**
     * Query all data sources
     *
     * @return
     */
    public String getStopsHubListParam(String username, boolean isAdmin, String param) {
        String sqlStr = "SELECT 0";
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from stops_hub ");
        strBuf.append("where enable_flag = 1 ");
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("and ( ");
            strBuf.append("jar_name like CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            strBuf.append("or status like CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            strBuf.append(") ");
        }
        if (!isAdmin) {
            strBuf.append("and crt_user = " + SqlUtils.preventSQLInjection(username));
        }
        strBuf.append("order by crt_dttm desc ");
        sqlStr = strBuf.toString();
        return sqlStr;
    }

}
