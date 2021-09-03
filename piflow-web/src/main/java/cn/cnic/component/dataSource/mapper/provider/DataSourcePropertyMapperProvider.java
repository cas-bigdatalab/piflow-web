package cn.cnic.component.dataSource.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.dataSource.entity.DataSourceProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DataSourcePropertyMapperProvider {

    private String id;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String name;
    private String value;
    private String description;
    private String dataSourceId;

    private boolean preventSQLInjectionDataSourceProperty(DataSourceProperty dataSourceProperty) {
        if (null == dataSourceProperty || StringUtils.isBlank(dataSourceProperty.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        this.id = SqlUtils.preventSQLInjection(dataSourceProperty.getId());
        this.lastUpdateUser = SqlUtils.preventSQLInjection(dataSourceProperty.getLastUpdateUser());
        Boolean enableFlag = dataSourceProperty.getEnableFlag();
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        Long version = dataSourceProperty.getVersion();
        this.version = (null != version ? version : 0L);
        Date lastUpdateDttm = dataSourceProperty.getLastUpdateDttm();
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

        // Selection field
        this.name = SqlUtils.preventSQLInjection(dataSourceProperty.getName());
        this.value = SqlUtils.preventSQLInjection(dataSourceProperty.getValue());
        this.description = SqlUtils.preventSQLInjection(dataSourceProperty.getDescription());
        String dataSourceIdStr = (null != dataSourceProperty.getDataSource() ? dataSourceProperty.getDataSource().getId() : null);
        this.dataSourceId = (null != dataSourceIdStr ? SqlUtils.preventSQLInjection(dataSourceIdStr) : null);
        return true;
    }

    private void reset() {
        this.id = null;
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
     * @param dataSourceProperty
     * @return
     */
    public String addDataSourceProperty(DataSourceProperty dataSourceProperty) {
        String sqlStr = "";
        boolean flag = this.preventSQLInjectionDataSourceProperty(dataSourceProperty);
        if (flag) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("INSERT INTO data_source_property ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldName() + ", ");
            stringBuffer.append("name, ");
            stringBuffer.append("value, ");
            stringBuffer.append("description, ");
            stringBuffer.append("fk_data_source_id ");
            stringBuffer.append(") ");
            stringBuffer.append("VALUES ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldValues(dataSourceProperty) + ", ");
            stringBuffer.append(name + ", ");
            stringBuffer.append(value + ", ");
            stringBuffer.append(description + ", ");
            stringBuffer.append(dataSourceId + " ");
            stringBuffer.append(") ");
            sqlStr = stringBuffer.toString();
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
            sql.append("INSERT INTO ");
            sql.append("data_source_property ");
            sql.append("(");
            sql.append(SqlUtils.baseFieldName() + ", ");
            sql.append("name,");
            sql.append("value,");
            sql.append("description,");
            sql.append("fk_data_source_id");
            sql.append(") ");
            sql.append("VALUES");
            int i = 0;
            for (DataSourceProperty dataSourceProperty : dataSourcePropertyList) {
                i++;
                boolean flag = this.preventSQLInjectionDataSourceProperty(dataSourceProperty);
                if(flag){
                    sql.append("(");

                    //Process the required fields first
                    sql.append(SqlUtils.baseFieldValues(dataSourceProperty) + ", ");
                    // handle other fields
                    sql.append(name + ",");
                    sql.append(value + ",");
                    sql.append(description + ",");
                    sql.append(dataSourceId + " ");
                    if (i != dataSourcePropertyList.size()) {
                        sql.append("),");
                    } else {
                        sql.append(")");
                    }
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
        boolean flag = this.preventSQLInjectionDataSourceProperty(dataSourceProperty);
        if (flag) {
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
