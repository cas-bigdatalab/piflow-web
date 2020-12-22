package cn.cnic.component.flow.mapper.provider;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.flow.entity.CustomizedProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class CustomizedPropertyMapperProvider {

    private String id;
    private String crtUser;
    private String crtDttmStr;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String name;
    private String customValue;
    private String description;
    private String stopsId;

    private void preventSQLInjectionCustomizedProperty(CustomizedProperty getPropertyBySotpsId) {
        if (null != getPropertyBySotpsId && StringUtils.isNotBlank(getPropertyBySotpsId.getLastUpdateUser())) {
            // Mandatory Field
            String id = getPropertyBySotpsId.getId();
            String crtUser = getPropertyBySotpsId.getCrtUser();
            String lastUpdateUser = getPropertyBySotpsId.getLastUpdateUser();
            Boolean enableFlag = getPropertyBySotpsId.getEnableFlag();
            Long version = getPropertyBySotpsId.getVersion();
            Date crtDttm = getPropertyBySotpsId.getCrtDttm();
            Date lastUpdateDttm = getPropertyBySotpsId.getLastUpdateDttm();
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
            this.name = SqlUtils.preventSQLInjection(getPropertyBySotpsId.getName());
            this.customValue = SqlUtils.preventSQLInjection(getPropertyBySotpsId.getCustomValue());
            this.description = SqlUtils.preventSQLInjection(getPropertyBySotpsId.getDescription());
            String stopsIdStr = (null != getPropertyBySotpsId.getStops() ? getPropertyBySotpsId.getStops().getId() : null);
            this.stopsId = (null != stopsIdStr ? SqlUtils.preventSQLInjection(stopsIdStr) : null);
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
            sqlStrBuffer.append("insert into ");
            sqlStrBuffer.append("flow_stops_customized_property ");
            sqlStrBuffer.append("(");
            sqlStrBuffer.append("id,");
            sqlStrBuffer.append("crt_dttm,");
            sqlStrBuffer.append("crt_user,");
            sqlStrBuffer.append("last_update_dttm,");
            sqlStrBuffer.append("last_update_user,");
            sqlStrBuffer.append("version,");
            sqlStrBuffer.append("enable_flag,");
            sqlStrBuffer.append("name,");
            sqlStrBuffer.append("custom_value,");
            sqlStrBuffer.append("description,");
            sqlStrBuffer.append("fk_stops_id,");
            sqlStrBuffer.append(") ");
            sqlStrBuffer.append("values");
            int i = 0;
            for (CustomizedProperty customizedProperty : customizedPropertyList) {
                i++;
                this.preventSQLInjectionCustomizedProperty(customizedProperty);
                if (null == crtDttmStr) {
                    String crtDttm = DateUtils.dateTimesToStr(new Date());
                    crtDttmStr = SqlUtils.preventSQLInjection(crtDttm);
                }
                if (StringUtils.isBlank(crtUser)) {
                    crtUser = SqlUtils.preventSQLInjection("-1");
                }
                // You can't make a mistake when you splice
                sqlStrBuffer.append("(");
                sqlStrBuffer.append(id + ",");
                sqlStrBuffer.append(crtDttmStr + ",");
                sqlStrBuffer.append(crtUser + ",");
                sqlStrBuffer.append(lastUpdateDttmStr + ",");
                sqlStrBuffer.append(lastUpdateUser + ",");
                sqlStrBuffer.append(version + ",");
                sqlStrBuffer.append(enableFlag + ",");
                sqlStrBuffer.append(name + ",");
                sqlStrBuffer.append(customValue + ",");
                sqlStrBuffer.append(description + ",");
                sqlStrBuffer.append(stopsId + ",");
                if (i != customizedPropertyList.size()) {
                    sqlStrBuffer.append("),");
                } else {
                    sqlStrBuffer.append(")");
                }
                this.reset();
            }
            sqlStrBuffer.append(";");
        }
        String sqlStr = sqlStrBuffer.toString();
        return sqlStr;
    }

    public String addCustomizedProperty(CustomizedProperty customizedProperty) {
        String sqlStr = "";
        this.preventSQLInjectionCustomizedProperty(customizedProperty);
        if (null != customizedProperty) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.INSERT_INTO("flow_stops_customized_property");
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
            sql.VALUES("name", name);
            sql.VALUES("custom_value", customValue);
            sql.VALUES("description", description);
            sql.VALUES("fk_stops_id", stopsId);
            sqlStr = sql.toString();
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
        this.preventSQLInjectionCustomizedProperty(customizedProperty);
        if (null != customizedProperty) {

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
