package cn.cnic.component.dataSource.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.dataSource.entity.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class DataSourceMapperProvider {

    private String id;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String dataSourceType;
    private String dataSourceName;
    private String dataSourceDescription;
    private Integer isTemplate;

    private boolean preventSQLInjectionDataSource(DataSource dataSource) {
        if (null == dataSource || StringUtils.isBlank(dataSource.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        Long version = dataSource.getVersion();
        Boolean enableFlag = dataSource.getEnableFlag();
        Date lastUpdateDttm = dataSource.getLastUpdateDttm();
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
        this.id = SqlUtils.preventSQLInjection(dataSource.getId());
        this.lastUpdateUser = SqlUtils.preventSQLInjection(dataSource.getLastUpdateUser());
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

        // Selection field
        this.dataSourceType = SqlUtils.preventSQLInjection(dataSource.getDataSourceType());
        this.dataSourceName = SqlUtils.preventSQLInjection(dataSource.getDataSourceName());
        this.dataSourceDescription = SqlUtils.preventSQLInjection(dataSource.getDataSourceDescription());
        this.isTemplate = (null == dataSource.getIsTemplate() ? 0 : (dataSource.getIsTemplate() ? 1 : 0));
        return true;
    }

    private void reset() {
        this.id = null;
        this.lastUpdateDttmStr = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.dataSourceType = null;
        this.dataSourceName = null;
        this.dataSourceDescription = null;
        this.isTemplate = null;
    }

    /**
     * add DataSource
     *
     * @param dataSource
     * @return
     */
    public String addDataSource(DataSource dataSource) {
        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionDataSource(dataSource);
        if (flag) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("INSERT INTO data_source ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldName() + ", ");
            stringBuffer.append("is_template, ");
            stringBuffer.append("data_source_type, ");
            stringBuffer.append("data_source_name, ");
            stringBuffer.append("data_source_description ");
            stringBuffer.append(") ");
            stringBuffer.append("VALUES ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldValues(dataSource) + ", ");
            stringBuffer.append(isTemplate + ", ");
            stringBuffer.append(dataSourceType + ", ");
            stringBuffer.append(dataSourceName + ", ");
            stringBuffer.append(dataSourceDescription + " ");
            stringBuffer.append(") ");
            sqlStr = stringBuffer.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * update DataSource
     *
     * @param dataSource
     * @return
     */
    public String updateDataSource(DataSource dataSource) {

        String sqlStr = "";
        boolean flag = this.preventSQLInjectionDataSource(dataSource);
        if (flag) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("data_source");
            // The first string in the SET is the name of the field corresponding to the table in the database
            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag = " + enableFlag);
            sql.SET("data_source_type = " + dataSourceType);
            sql.SET("data_source_name = " + dataSourceName);
            sql.SET("data_source_description = " + dataSourceDescription);
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
     * Query all data sources
     *
     * @return
     */
    public String getDataSourceList(String username, boolean isAdmin) {
        String sqlStr = "SELECT 0";
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from data_source ");
        strBuf.append("where enable_flag = 1 ");
        strBuf.append("and is_template = 0 ");
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
    public String getDataSourceListParam(String username, boolean isAdmin, String param) {
        String sqlStr = "SELECT 0";
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from data_source ");
        strBuf.append("where enable_flag = 1 ");
        strBuf.append("and is_template = 0 ");
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("and ( ");
            strBuf.append("data_source_name LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            
            strBuf.append("or data_source_type LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
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
     * Query all template data sources
     *
     * @return
     */
    public String getDataSourceTemplateList() {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("data_source");
        sql.WHERE("enable_flag = 1");
        sql.WHERE("is_template = 1");
        sql.ORDER_BY(" data_source_type asc  ");
        sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * Query the data source according to the workflow Id
     *
     * @param id
     * @return
     */
    public String getDataSourceByIdAndUser(String username, boolean isAdmin, String id) {
        String sqlStr = "";
        if (StringUtils.isNotBlank(id)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from data_source ");
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
     * Query the data source according to the workflow Id
     *
     * @param id
     * @return
     */
    public String getDataSourceById(String id) {
        String sqlStr = "";
        if (StringUtils.isNotBlank(id)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from data_source ");
            strBuf.append("where enable_flag = 1 ");
            strBuf.append("and id = " + SqlUtils.preventSQLInjection(id) + " ");
            sqlStr = strBuf.toString();
        }
        return sqlStr;
    }

    /**
     * Query the data source according to the workflow Id
     *
     * @param id
     * @return
     */
    public String adminGetDataSourceById(String id) {
        String sqlStr = "";
        if (StringUtils.isNotBlank(id)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from data_source ");
            strBuf.append("where enable_flag = 1 ");
            strBuf.append("and id = " + SqlUtils.preventSQLInjection(id) + " ");
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
        sql.UPDATE("data_source");
        sql.SET("enable_flag = 0");
        sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
        sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
        sql.WHERE("enable_flag = 1");
        sql.WHERE("is_template = 0");
        sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));

        return sql.toString();
    }

}
