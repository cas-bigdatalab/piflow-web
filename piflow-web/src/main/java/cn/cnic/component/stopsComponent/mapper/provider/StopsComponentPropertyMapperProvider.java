package cn.cnic.component.stopsComponent.mapper.provider;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.stopsComponent.model.StopsComponentProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class StopsComponentPropertyMapperProvider {

    private String id;
    private String crtUser;
    private String crtDttmStr;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String allowableValues;
    private String defaultValue;
    private String description;
    private String displayName;
    private String name;
    private Integer required;
    private Integer sensitive;
    private Long propertySort;
    private String stopsTemplateId;

    private void preventSQLInjectionStopsComponentProperty(StopsComponentProperty stopsComponentProperty) {
        if (null != stopsComponentProperty && StringUtils.isNotBlank(stopsComponentProperty.getLastUpdateUser())) {
            // Mandatory Field
            String id = stopsComponentProperty.getId();
            String crtUser = stopsComponentProperty.getCrtUser();
            String lastUpdateUser = stopsComponentProperty.getLastUpdateUser();
            Boolean enableFlag = stopsComponentProperty.getEnableFlag();
            Long version = stopsComponentProperty.getVersion();
            Date crtDttm = stopsComponentProperty.getCrtDttm();
            Date lastUpdateDttm = stopsComponentProperty.getLastUpdateDttm();
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
            this.allowableValues = SqlUtils.preventSQLInjection(stopsComponentProperty.getAllowableValues());
            this.defaultValue = SqlUtils.preventSQLInjection(stopsComponentProperty.getDefaultValue());
            this.description = SqlUtils.preventSQLInjection(stopsComponentProperty.getDescription());
            this.displayName = SqlUtils.preventSQLInjection(stopsComponentProperty.getDisplayName());
            this.name = SqlUtils.preventSQLInjection(stopsComponentProperty.getName());
            this.required = (null == stopsComponentProperty.getRequired() ? 0 : (stopsComponentProperty.getRequired() ? 1 : 0));
            this.sensitive = (null == stopsComponentProperty.getSensitive() ? 0 : (stopsComponentProperty.getSensitive() ? 1 : 0));
            this.propertySort = (null != stopsComponentProperty.getPropertySort() ? stopsComponentProperty.getPropertySort() : 0L);
            this.stopsTemplateId = SqlUtils.preventSQLInjection(stopsComponentProperty.getStopsTemplate());
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
        this.allowableValues = null;
        this.defaultValue = null;
        this.description = null;
        this.displayName = null;
        this.name = null;
        this.required = null;
        this.sensitive = null;
        this.propertySort = 0L;
        this.stopsTemplateId = null;
    }

    /**
     * 根据stops模板id查询对应的stops的所有属性
     *
     * @param stopsId
     * @return
     */
    public String getStopsComponentPropertyByStopsId(String stopsId) {

        String sqlStr = "select 0";
        if (null != stopsId) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("flow_stops_property_template");
            sql.WHERE("fk_stops_id = " + SqlUtils.addSqlStr(stopsId));
            sql.WHERE("enable_flag = 1");
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public String insertStopsComponentProperty(Map map) {
        String sqlStr = "select 0";
        List<StopsComponentProperty> stopsComponentPropertyList = (List<StopsComponentProperty>) map.get("stopsComponentPropertyList");
        if (null != stopsComponentPropertyList && stopsComponentPropertyList.size() > 0) {
            SQL sqlColumns = new SQL();
            sqlColumns.INSERT_INTO("flow_stops_property_template");
            sqlColumns.INTO_COLUMNS(
                    "id",
                    "crt_dttm",
                    "crt_user",
                    "enable_flag",
                    "last_update_dttm",
                    "last_update_user",
                    "version",
                    "allowable_values",
                    "default_value",
                    "description",
                    "display_name",
                    "name",
                    "property_required",
                    "property_sensitive",
                    "property_sort",
                    "fk_stops_id"
            );
            StringBuffer sqlValuesStr = new StringBuffer();
            sqlValuesStr.append("\nvalues\n");
            for (int i = 0; i < stopsComponentPropertyList.size(); i++) {
                StopsComponentProperty stopsComponentProperty = stopsComponentPropertyList.get(i);
                if (null != stopsComponentProperty) {
                    this.preventSQLInjectionStopsComponentProperty(stopsComponentProperty);
                    if (null == crtDttmStr) {
                        String crtDttm = DateUtils.dateTimesToStr(new Date());
                        crtDttmStr = SqlUtils.preventSQLInjection(crtDttm);
                    }
                    if (StringUtils.isBlank(crtUser)) {
                        crtUser = SqlUtils.preventSQLInjection("-1");
                    }
                    sqlValuesStr.append("(");
                    sqlValuesStr.append(id + ",");
                    sqlValuesStr.append(crtDttmStr + ",");
                    sqlValuesStr.append(crtUser + ",");
                    sqlValuesStr.append(enableFlag + ",");
                    sqlValuesStr.append(lastUpdateDttmStr + ",");
                    sqlValuesStr.append(lastUpdateUser + ",");
                    sqlValuesStr.append(version + ",");
                    sqlValuesStr.append(allowableValues + ",");
                    sqlValuesStr.append(defaultValue + ",");
                    sqlValuesStr.append(description + ",");
                    sqlValuesStr.append(displayName + ",");
                    sqlValuesStr.append(name + ",");
                    sqlValuesStr.append(required + ",");
                    sqlValuesStr.append(sensitive + ",");
                    sqlValuesStr.append(propertySort + ",");
                    sqlValuesStr.append(stopsTemplateId);
                    sqlValuesStr.append(")");
                    if (i < stopsComponentPropertyList.size() - 1) {
                        sqlValuesStr.append(",\n");
                    }
                    this.reset();
                }
            }
            sqlStr = (sqlColumns.toString() + sqlValuesStr.toString());
        }
        return sqlStr;
    }

}
