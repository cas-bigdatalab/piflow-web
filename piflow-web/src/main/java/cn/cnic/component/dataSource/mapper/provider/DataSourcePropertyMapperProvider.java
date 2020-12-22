package cn.cnic.component.dataSource.mapper.provider;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.dataSource.entity.DataSourceProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DataSourcePropertyMapperProvider {

    private String id;
    private String crtUser;
    private String crtDttmStr;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String name;
    private String value;
    private String description;
    private String dataSourceId;

    private void preventSQLInjectionDataSourceProperty(DataSourceProperty dataSourceProperty) {
        if (null != dataSourceProperty && StringUtils.isNotBlank(dataSourceProperty.getLastUpdateUser())) {
            // Mandatory Field
            String id = dataSourceProperty.getId();
            String crtUser = dataSourceProperty.getCrtUser();
            String lastUpdateUser = dataSourceProperty.getLastUpdateUser();
            Boolean enableFlag = dataSourceProperty.getEnableFlag();
            Long version = dataSourceProperty.getVersion();
            Date crtDttm = dataSourceProperty.getCrtDttm();
            Date lastUpdateDttm = dataSourceProperty.getLastUpdateDttm();
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
            this.name = SqlUtils.preventSQLInjection(dataSourceProperty.getName());
            this.value = SqlUtils.preventSQLInjection(dataSourceProperty.getValue());
            this.description = SqlUtils.preventSQLInjection(dataSourceProperty.getDescription());
            String dataSourceIdStr = (null != dataSourceProperty.getDataSource() ? dataSourceProperty.getDataSource().getId() : null);
            this.dataSourceId = (null != dataSourceIdStr ? SqlUtils.preventSQLInjection(dataSourceIdStr) : null);
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
        this.name = null;
        this.value = null;
        this.description = null;
        this.dataSourceId = null;
    }

    /**
     * add DataSourceProperty
     *
     * @param dataSourcePropertyProperty
     * @return
     */
    public String addDataSourceProperty(DataSourceProperty dataSourcePropertyProperty) {
        String sqlStr = "";
        this.preventSQLInjectionDataSourceProperty(dataSourcePropertyProperty);
        if (null != dataSourcePropertyProperty) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.INSERT_INTO("data_source_property");
            // The first string in the value is the field name corresponding to the table in the database.
            // all types except numeric fields must be enclosed in single quotes

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

            // handle other fields
            sql.VALUES("name", name);
            sql.VALUES("value", value);
            sql.VALUES("description", description);
            sql.VALUES("fk_data_source_id", dataSourceId);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * Insert list<datasourceproperty> note that the way to spell SQL must use a map to connect Param content as a key value</datasourceproperty>
     *
     * @param map (Content: dataSourcePropertyList, the value is List<datasourceproperty></datasourceproperty>)
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String addDataSourcePropertyList(Map map) {
        List<DataSourceProperty> dataSourcePropertyList = (List<DataSourceProperty>) map.get("dataSourcePropertyList");
        StringBuffer sql = new StringBuffer();
        if (null != dataSourcePropertyList && dataSourcePropertyList.size() > 0) {
            sql.append("insert into ");
            sql.append("data_source_property ");
            sql.append("(");
            if (null == crtDttmStr) {
                String crtDttm = DateUtils.dateTimesToStr(new Date());
                crtDttmStr = SqlUtils.preventSQLInjection(crtDttm);
            }
            if (StringUtils.isBlank(crtUser)) {
                crtUser = SqlUtils.preventSQLInjection("-1");
            }
            sql.append("id,");
            sql.append("crt_dttm,");
            sql.append("crt_user,");
            sql.append("last_update_dttm,");
            sql.append("last_update_user,");
            sql.append("version,");
            sql.append("enable_flag,");

            sql.append("name,");
            sql.append("value,");
            sql.append("description,");
            sql.append("fk_data_source_id");
            sql.append(") ");
            sql.append("values");
            int i = 0;
            for (DataSourceProperty dataSourceProperty : dataSourcePropertyList) {
                i++;
                this.preventSQLInjectionDataSourceProperty(dataSourceProperty);
                sql.append("(");

                //Process the required fields first
                sql.append(id + ",");
                sql.append(crtDttmStr + ",");
                sql.append(crtUser + ",");
                sql.append(lastUpdateDttmStr + ",");
                sql.append(lastUpdateUser + ",");
                sql.append(version + ",");
                sql.append(enableFlag + ",");

                // handle other fields
                sql.append(name + ",");
                sql.append(value + ",");
                sql.append(description + ",");
                sql.append(dataSourceId);
                if (i != dataSourcePropertyList.size()) {
                    sql.append("),");
                } else {
                    sql.append(")");
                }
                this.reset();
            }
            sql.append(";");
        }
        return sql.toString();
    }

    /**
     * update DataSourceProperty
     *
     * @param dataSourceProperty
     * @return
     */
    public String updateDataSourceProperty(DataSourceProperty dataSourceProperty) {

        String sqlStr = "";
        this.preventSQLInjectionDataSourceProperty(dataSourceProperty);
        if (null != dataSourceProperty) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("data_source_property");
            // The first string in the SET is the name of the field corresponding to the table in the database
            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag = " + enableFlag);
            sql.SET("name = " + name);
            sql.SET("value = " + value);
            sql.SET("description = " + description);
            sql.SET("fk_data_source_id = " + dataSourceId);
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
     * Query DataSourcePropertyList according to dataSourceId
     *
     * @param dataSourceId
     * @return
     */
    public String getDataSourcePropertyListByDataSourceId(String dataSourceId) {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("data_source_property");
        sql.WHERE("enable_flag = 1");
        sql.WHERE("fk_data_source_id = " + SqlUtils.preventSQLInjection(dataSourceId));
        sql.ORDER_BY("crt_dttm asc");
        sqlStr = sql.toString();
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
        sql.UPDATE("data_source_property");
        sql.SET("enable_flag = 0");
        sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
        sql.SET("last_update_dttm = " + SqlUtils.addSqlStr(DateUtils.dateTimesToStr(new Date())));
        sql.WHERE("enable_flag = 1");
        sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));

        return sql.toString();
    }

    /**
     * Delete according to id logic, set to invalid
     *
     * @param id
     * @return
     */
    public String updateEnableFlagByDatasourceId(String username, String id) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }
        SQL sql = new SQL();
        sql.UPDATE("data_source_property");
        sql.SET("enable_flag = 0");
        sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
        sql.SET("last_update_dttm = " + SqlUtils.addSqlStr(DateUtils.dateTimesToStr(new Date())));
        sql.WHERE("enable_flag = 1");
        sql.WHERE("fk_data_source_id = " + SqlUtils.preventSQLInjection(id));

        return sql.toString();
    }

}
