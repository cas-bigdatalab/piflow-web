package cn.cnic.component.sparkJar.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.sparkJar.entity.SparkJarComponent;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class SparkJarMapperProvider {

    private String id;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String mountId;
    private String jarName;
    private String jarUrl;
    private String status;

    private boolean preventSQLInjectionSparkJar(SparkJarComponent sparkJarComponent) {
        if (null == sparkJarComponent || StringUtils.isBlank(sparkJarComponent.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        String id = sparkJarComponent.getId();
        String lastUpdateUser = sparkJarComponent.getLastUpdateUser();
        Boolean enableFlag = sparkJarComponent.getEnableFlag();
        Long version = sparkJarComponent.getVersion();
        Date lastUpdateDttm = sparkJarComponent.getLastUpdateDttm();
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
        this.id = SqlUtils.preventSQLInjection(id);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(lastUpdateUser);
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

        // Selection field
        this.mountId = SqlUtils.preventSQLInjection(sparkJarComponent.getMountId());
        this.jarName = SqlUtils.preventSQLInjection(sparkJarComponent.getJarName());
        this.jarUrl = SqlUtils.preventSQLInjection(sparkJarComponent.getJarUrl());
        this.status = SqlUtils.preventSQLInjection(null != sparkJarComponent.getStatus() ? sparkJarComponent.getStatus().name() : null);
        return true;
    }

    private void reset() {
        this.id = null;
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
     * add spark jar component
     *
     * @param sparkJarComponent
     * @return
     */
    public String addSparkJarComponent(SparkJarComponent sparkJarComponent) {
        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionSparkJar(sparkJarComponent);
        if (flag) {
            StringBuffer strBuf = new StringBuffer();
            // INSERT_INTO brackets is table name
            strBuf.append("INSERT INTO spark_jar ");
            strBuf.append("( ");
            strBuf.append(SqlUtils.baseFieldName() + ", ");
            strBuf.append("mount_id, ");
            strBuf.append("jar_name, ");
            strBuf.append("jar_url, ");
            strBuf.append("status ");
            strBuf.append(") VALUES ( ");
            strBuf.append(SqlUtils.baseFieldValues(sparkJarComponent) + ", ");
            strBuf.append(this.mountId + ", ");
            strBuf.append(this.jarName + ", ");
            strBuf.append(this.jarUrl + ", ");
            strBuf.append(this.status + " ");
            strBuf.append(") ");
            sqlStr = strBuf.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * update Spark Jar Component
     *
     * @param sparkJarComponent
     * @return
     */
    public String updateSparkJarComponent(SparkJarComponent sparkJarComponent) {

        String sqlStr = "";
        boolean flag = this.preventSQLInjectionSparkJar(sparkJarComponent);
        if (flag) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("spark_jar");
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
     * Query all spark jars
     *
     * @return
     */
    public String getSparkJarList(String username, boolean isAdmin, String param) {
        String sqlStr = "SELECT 0";
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from spark_jar ");
        strBuf.append("where enable_flag = 1 ");

        if (!isAdmin) {
            strBuf.append("and crt_user = " + SqlUtils.preventSQLInjection(username));
        }
        strBuf.append("order by crt_dttm desc ");
        sqlStr = strBuf.toString();
        return sqlStr;
    }

    /**
     * Query all spark jars by name
     *
     * @return
     */
    public String getSparkJarListByName(String username, boolean isAdmin, String jarName) {
        String sqlStr = "SELECT 0";
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from spark_jar ");
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
     * Query the data by the spark jar Id
     *
     * @param id
     * @return
     */
    public String getSparkJarById(String username, boolean isAdmin, String id) {
        String sqlStr = "";
        if (StringUtils.isNotBlank(id)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from spark_jar ");
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
        sql.UPDATE("spark_jar");
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
    public String getSparkJarListParam(String username, boolean isAdmin, String param) {
        String sqlStr = "SELECT 0";
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from spark_jar ");
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
