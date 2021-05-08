package cn.cnic.component.flow.mapper.provider;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.flow.entity.Property;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class PropertyMapperProvider {

    private String id;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String name;
    private String displayName;
    private String description;
    private String customValue;
    private String allowableValues;
    private Integer required;
    private Integer sensitive;
    private String stopsId;
    private Integer isSelect;
    private Integer isLocked;
    private long propertySort;
    private String example;
    private Integer isOldData;

    private boolean preventSQLInjectionProperty(Property property) {
        if (null == property || StringUtils.isBlank(property.getLastUpdateUser())) {
            return false;
        }

        // Mandatory Field
        String id = property.getId();
        String lastUpdateUser = property.getLastUpdateUser();
        Boolean enableFlag = property.getEnableFlag();
        Long version = property.getVersion();
        Date lastUpdateDttm = property.getLastUpdateDttm();
        this.id = SqlUtils.preventSQLInjection(id);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(lastUpdateUser);
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
        
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

        // Selection field
        this.name = SqlUtils.preventSQLInjection(property.getName());
        this.displayName = SqlUtils.preventSQLInjection(property.getDisplayName());
        this.description = SqlUtils.preventSQLInjection(property.getDescription());
        this.customValue = SqlUtils.preventSQLInjection(property.getCustomValue());
        this.allowableValues = SqlUtils.preventSQLInjection(property.getAllowableValues());
        this.required = (null == property.getRequired() ? null : (property.getRequired() ? 1 : 0));
        this.sensitive = (null == property.getSensitive() ? null : (property.getSensitive() ? 1 : 0));
        String stopsIdStr = (null != property.getStops() ? property.getStops().getId() : null);
        this.stopsId = (null != stopsIdStr ? SqlUtils.preventSQLInjection(stopsIdStr) : null);
        this.isSelect = (null == property.getIsSelect() ? null : (property.getIsSelect() ? 1 : 0));
        this.isLocked = (null == property.getIsLocked() ? null : (property.getIsLocked() ? 1 : 0));
        this.propertySort = (null != property.getPropertySort() ? property.getPropertySort() : 0L);
        this.example = SqlUtils.preventSQLInjection(property.getExample());
        this.isOldData = (null == property.getIsOldData() ? null : (property.getIsOldData() ? 1 : 0));
        return true;
    }

    private void reset() {
        this.id = null;
        this.lastUpdateDttmStr = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.name = null;
        this.displayName = null;
        this.description = null;
        this.customValue = null;
        this.allowableValues = null;
        this.required = null;
        this.sensitive = null;
        this.stopsId = null;
        this.isSelect = null;
        this.isLocked = null;
        this.propertySort = 0L;
        this.example = null;
        this.isOldData = null;
    }

    /**
     * Insert list<Property> Note that the method of spelling sql must use Map to
     * connect Param content to key value.
     *
     * @param property (Content: The key is propertyList and the value is List<Property>)
     * @return
     */
    public String addProperty(Property property) {
        String sqlStr = "SELECT 0";
        boolean flag =this.preventSQLInjectionProperty(property);
        if (flag) {
            StringBuffer sqlStrBuffer = new StringBuffer();
            sqlStrBuffer.append("INSERT INTO flow_stops_property ");
            sqlStrBuffer.append("( ");
            sqlStrBuffer.append(SqlUtils.baseFieldName() + ", ");
            sqlStrBuffer.append("name,");
            sqlStrBuffer.append("display_name,");
            sqlStrBuffer.append("description,");
            sqlStrBuffer.append("custom_value,");
            sqlStrBuffer.append("allowable_values,");
            sqlStrBuffer.append("property_required,");
            sqlStrBuffer.append("property_sensitive,");
            sqlStrBuffer.append("fk_stops_id,");
            sqlStrBuffer.append("is_select,");
            sqlStrBuffer.append("is_locked,");
            sqlStrBuffer.append("property_sort,");
            sqlStrBuffer.append("example,");
            sqlStrBuffer.append("is_old_data");
            sqlStrBuffer.append(") ");
            sqlStrBuffer.append("VALUES ");
            sqlStrBuffer.append("( ");
            sqlStrBuffer.append(SqlUtils.baseFieldValues(property) + ", ");
            sqlStrBuffer.append(this.name + ",");
            sqlStrBuffer.append(this.displayName + ",");
            sqlStrBuffer.append(this.description + ",");
            sqlStrBuffer.append(this.customValue + ",");
            sqlStrBuffer.append(this.allowableValues + ",");
            sqlStrBuffer.append(this.required + ",");
            sqlStrBuffer.append(this.sensitive + ",");
            sqlStrBuffer.append(this.stopsId + ",");
            sqlStrBuffer.append(this.isSelect + ",");
            sqlStrBuffer.append(this.isLocked + ",");
            sqlStrBuffer.append(this.propertySort + ",");
            sqlStrBuffer.append(this.example + ",");
            sqlStrBuffer.append(this.isOldData + " ");
            sqlStrBuffer.append(")");
            sqlStr = sqlStrBuffer.toString();
            this.reset();
        }
        return sqlStr;
    }

    /**
     * Insert list<Property> Note that the method of spelling sql must use Map to
     * connect Param content to key value.
     *
     * @param map (Content: The key is propertyList and the value is List<Property>)
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String addPropertyList(Map map) {
        List<Property> propertyList = (List<Property>) map.get("propertyList");
        StringBuffer sqlStrBuffer = new StringBuffer();
        if (null != propertyList && propertyList.size() > 0) {
            sqlStrBuffer.append("INSERT INTO ");
            sqlStrBuffer.append("flow_stops_property ");
            sqlStrBuffer.append("(");
            sqlStrBuffer.append(SqlUtils.baseFieldName() + ", ");
            sqlStrBuffer.append("name,");
            sqlStrBuffer.append("display_name,");
            sqlStrBuffer.append("description,");
            sqlStrBuffer.append("custom_value,");
            sqlStrBuffer.append("allowable_values,");
            sqlStrBuffer.append("property_required,");
            sqlStrBuffer.append("property_sensitive,");
            sqlStrBuffer.append("fk_stops_id,");
            sqlStrBuffer.append("is_select,");
            sqlStrBuffer.append("is_locked,");
            sqlStrBuffer.append("property_sort,");
            sqlStrBuffer.append("example,");
            sqlStrBuffer.append("is_old_data");
            sqlStrBuffer.append(") ");
            sqlStrBuffer.append("VALUES");
            int i = 0;
            for (Property property : propertyList) {
                i++;
                boolean flag =this.preventSQLInjectionProperty(property);
                if (flag) {
                    sqlStrBuffer.append("(");
                    // You can't make a mistake when you splice
                    sqlStrBuffer.append(SqlUtils.baseFieldValues(property) + ", ");
                    sqlStrBuffer.append(name + ",");
                    sqlStrBuffer.append(displayName + ",");
                    sqlStrBuffer.append(description + ",");
                    sqlStrBuffer.append(customValue + ",");
                    sqlStrBuffer.append(allowableValues + ",");
                    sqlStrBuffer.append(required + ",");
                    sqlStrBuffer.append(sensitive + ",");
                    sqlStrBuffer.append(stopsId + ",");
                    sqlStrBuffer.append(isSelect + ",");
                    sqlStrBuffer.append(isLocked + ",");
                    sqlStrBuffer.append(propertySort + ",");
                    sqlStrBuffer.append(example + ",");
                    sqlStrBuffer.append(isOldData);
                    if (i != propertyList.size()) {
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

    /**
     * 修改Property
     *
     * @param property
     * @return
     */
    public String updateStopsProperty(Property property) {
        String sqlStr = "";
        this.preventSQLInjectionProperty(property);
        if (null != property) {

            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("flow_stops_property");
            // The first string in the SET is the name of the field corresponding to the
            // table in the database

            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag = " + enableFlag);
            sql.SET("description = " + description);
            sql.SET("name = " + name);
            sql.SET("allowable_values = " + allowableValues);
            sql.SET("custom_value = " + customValue);
            sql.SET("display_name = " + displayName);
            sql.SET("property_required = " + required);
            sql.SET("property_sensitive = " + sensitive);
            sql.SET("is_locked = " + isLocked);
            sql.SET("property_sort = " + propertySort);
            sql.SET("is_old_data = " + isOldData);
            // sql.SET("fk_stops_id = " + stopsId);
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
     * remove
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
        sql.UPDATE("flow_stops_property");
        sql.SET("enable_flag = 0");
        sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
        sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
        sql.WHERE("enable_flag = 1");
        sql.WHERE("ID = " + SqlUtils.preventSQLInjection(id));

        return sql.toString();
    }

    /**
     * Modify the stop attribute
     *
     * @param id
     * @return
     */
    public String updatePropertyCustomValue(String username, String content, String id) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }
        SQL sql = new SQL();
        sql.UPDATE("flow_stops_property");
        sql.SET("custom_value = " + SqlUtils.preventSQLInjection(content));
        sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
        sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
        sql.SET("version = " + 1);
        sql.WHERE("enable_flag = 1");
        sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));
        return sql.toString();
    }

}
