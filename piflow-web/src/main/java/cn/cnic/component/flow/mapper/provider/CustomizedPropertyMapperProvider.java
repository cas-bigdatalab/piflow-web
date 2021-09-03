package cn.cnic.component.flow.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.flow.entity.CustomizedProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class CustomizedPropertyMapperProvider {

    private String id;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String name;
    private String customValue;
    private String description;
    private String stopsId;

    private boolean preventSQLInjectionCustomizedProperty(CustomizedProperty customizedProperty) {
        if (null == customizedProperty || StringUtils.isBlank(customizedProperty.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        this.id = SqlUtils.preventSQLInjection(customizedProperty.getId());
        Long version = customizedProperty.getVersion();
        this.version = (null != version ? version : 0L);
        Boolean enableFlag = customizedProperty.getEnableFlag();
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(customizedProperty.getLastUpdateUser());
        Date lastUpdateDttm = customizedProperty.getLastUpdateDttm();
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

        // Selection field
        this.name = SqlUtils.preventSQLInjection(customizedProperty.getName());
        this.customValue = SqlUtils.preventSQLInjection(customizedProperty.getCustomValue());
        this.description = SqlUtils.preventSQLInjection(customizedProperty.getDescription());
        String stopsIdStr = (null != customizedProperty.getStops() ? customizedProperty.getStops().getId() : null);
        this.stopsId = (null != stopsIdStr ? SqlUtils.preventSQLInjection(stopsIdStr) : null);
        return true;
    }

    private void reset() {
        this.id = null;
        this.lastUpdateDttmStr = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.name = null;
        this.customValue = null;
        this.description = null;
        this.stopsId = null;
    }

    /**
     * Insert "list<CustomizedProperty>" Note that the method of spelling "sql" must use "map" to connect the "Param" content to the key value.
     *
     * @param map (Content: "customizedPropertyList" with a value of "List<CustomizedProperty>")
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String addCustomizedPropertyList(Map map) {
        List<CustomizedProperty> customizedPropertyList = (List<CustomizedProperty>) map.get("customizedPropertyList");
        StringBuffer sqlStrBuffer = new StringBuffer();
        if (null != customizedPropertyList && customizedPropertyList.size() > 0) {
            sqlStrBuffer.append("INSERT INTO ");
            sqlStrBuffer.append("flow_stops_customized_property ");
            sqlStrBuffer.append("(");
            sqlStrBuffer.append(SqlUtils.baseFieldName() + ", ");
            sqlStrBuffer.append("name,");
            sqlStrBuffer.append("custom_value,");
            sqlStrBuffer.append("description,");
            sqlStrBuffer.append("fk_stops_id");
            sqlStrBuffer.append(") ");
            sqlStrBuffer.append("VALUES");
            int i = 0;
            for (CustomizedProperty customizedProperty : customizedPropertyList) {
                i++;
                boolean flag = this.preventSQLInjectionCustomizedProperty(customizedProperty);
                if(flag) {
                    // You can't make a mistake when you splice
                    sqlStrBuffer.append("(");
                    sqlStrBuffer.append(SqlUtils.baseFieldValues(customizedProperty) + ", ");
                    sqlStrBuffer.append(name + ",");
                    sqlStrBuffer.append(customValue + ",");
                    sqlStrBuffer.append(description + ",");
                    sqlStrBuffer.append(stopsId + " ");
                    if (i != customizedPropertyList.size()) {
                        sqlStrBuffer.append("),");
                    } else {
                        sqlStrBuffer.append(")");
                    }
                }
                this.reset();
            }
            sqlStrBuffer.append(";");
        }
        String sqlStr = sqlStrBuffer.toString();
        return sqlStr;
    }

    public String addCustomizedProperty(CustomizedProperty customizedProperty) {
        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionCustomizedProperty(customizedProperty);
        if (flag) {
            StringBuffer sqlStrBuffer = new StringBuffer();
            sqlStrBuffer.append("INSERT INTO flow_stops_customized_property");
            sqlStrBuffer.append("(");
            sqlStrBuffer.append(SqlUtils.baseFieldName() + ", ");
            sqlStrBuffer.append("name, ");
            sqlStrBuffer.append("custom_value, ");
            sqlStrBuffer.append("description, ");
            sqlStrBuffer.append("fk_stops_id ");
            sqlStrBuffer.append(") ");
            sqlStrBuffer.append("VALUES");
            sqlStrBuffer.append("(");
            sqlStrBuffer.append(SqlUtils.baseFieldValues(customizedProperty) + ", ");
            sqlStrBuffer.append(name + ",");
            sqlStrBuffer.append(customValue + ",");
            sqlStrBuffer.append(description + ",");
            sqlStrBuffer.append(stopsId + " ");
            sqlStrBuffer.append(")");
            sqlStr = sqlStrBuffer.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * update customizedProperty
     *
     * @param customizedProperty
     * @return
     */
    public String updateStopsCustomizedProperty(CustomizedProperty customizedProperty) {
        String sqlStr = "";
        boolean flag = this.preventSQLInjectionCustomizedProperty(customizedProperty);
        if (flag) {

            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("flow_stops_customized_property");
            // The first string in the SET is the name of the field corresponding to the table in the database

            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag = " + enableFlag);
            sql.SET("name = " + name);
            sql.SET("custom_value = " + customValue);
            sql.SET("description = " + description);
            //sql.SET("fk_stops_id = " + stopsId);
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
     * Logic to delete
     *
     * @param id
     * @return
     */
    public String updateEnableFlagByStopId(String username, String id) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }
        SQL sql = new SQL();
        sql.UPDATE("flow_stops_customized_property");
        sql.SET("enable_flag = 0");
        sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
        sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
        sql.WHERE("enable_flag = 1");
        sql.WHERE("ID = " + SqlUtils.preventSQLInjection(id));

        return sql.toString();
    }

    /**
     * Modify the stop property
     *
     * @param id
     * @return
     */
    public String updateCustomizedPropertyCustomValue(String username, String content, String id) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }
        SQL sql = new SQL();
        sql.UPDATE("flow_stops_customized_property");
        sql.SET("custom_value = " + SqlUtils.preventSQLInjection(content));
        sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
        sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
        sql.SET("version = " + 1);
        sql.WHERE("enable_flag = 1");
        sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));
        return sql.toString();
    }

}
