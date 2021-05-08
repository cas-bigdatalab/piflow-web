package cn.cnic.component.stopsComponent.mapper.provider;

import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.stopsComponent.model.StopsComponentProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public class StopsComponentPropertyMapperProvider {

    private String allowableValues;
    private String defaultValue;
    private String description;
    private String displayName;
    private String name;
    private Integer required;
    private Integer sensitive;
    private Long propertySort;
    private String example;
    private String language;
    private String stopsTemplateId;

    private boolean preventSQLInjectionStopsComponentProperty(StopsComponentProperty stopsComponentProperty) {
        if (null == stopsComponentProperty || StringUtils.isBlank(stopsComponentProperty.getLastUpdateUser())) {
            return false;
        }
        // Selection field
        this.allowableValues = SqlUtils.preventSQLInjection(stopsComponentProperty.getAllowableValues());
        this.defaultValue = SqlUtils.preventSQLInjection(stopsComponentProperty.getDefaultValue());
        this.description = SqlUtils.preventSQLInjection(stopsComponentProperty.getDescription());
        this.displayName = SqlUtils.preventSQLInjection(stopsComponentProperty.getDisplayName());
        this.name = SqlUtils.preventSQLInjection(stopsComponentProperty.getName());
        this.required = (null == stopsComponentProperty.getRequired() ? 0 : (stopsComponentProperty.getRequired() ? 1 : 0));
        this.sensitive = (null == stopsComponentProperty.getSensitive() ? 0 : (stopsComponentProperty.getSensitive() ? 1 : 0));
        this.propertySort = (null != stopsComponentProperty.getPropertySort() ? stopsComponentProperty.getPropertySort() : 0L);
        this.example = SqlUtils.preventSQLInjection(stopsComponentProperty.getExample());
        this.language = SqlUtils.preventSQLInjection(stopsComponentProperty.getLanguage());
        this.stopsTemplateId = SqlUtils.preventSQLInjection(stopsComponentProperty.getStopsTemplate());
        return true;
    }

    private void reset() {
        this.allowableValues = null;
        this.defaultValue = null;
        this.description = null;
        this.displayName = null;
        this.name = null;
        this.required = null;
        this.sensitive = null;
        this.propertySort = 0L;
        this.example = null;
        this.language = null;
        this.stopsTemplateId = null;
    }

    /**
     * 根据stops模板id查询对应的stops的所有属性
     *
     * @param stopsId
     * @return
     */
    public String getStopsComponentPropertyByStopsId(String stopsId) {

        String sqlStr = "SELECT 0";
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
        String sqlStr = "SELECT 0";
        List<StopsComponentProperty> stopsComponentPropertyList = (List<StopsComponentProperty>) map.get("stopsComponentPropertyList");
        if (null != stopsComponentPropertyList && stopsComponentPropertyList.size() > 0) {
            StringBuffer sqlValuesStr = new StringBuffer();
            // INSERT_INTO brackets is table name
            sqlValuesStr.append("INSERT INTO flow_stops_property_template ");
            sqlValuesStr.append("( ");
            sqlValuesStr.append(SqlUtils.baseFieldName() + ", ");
            sqlValuesStr.append("allowable_values,");
            sqlValuesStr.append("default_value,");
            sqlValuesStr.append("description,");
            sqlValuesStr.append("display_name,");
            sqlValuesStr.append("name,");
            sqlValuesStr.append("property_required,");
            sqlValuesStr.append("property_sensitive,");
            sqlValuesStr.append("property_sort,");
            sqlValuesStr.append("example,");
            sqlValuesStr.append("language,");
            sqlValuesStr.append("fk_stops_id");
            sqlValuesStr.append(")");
            sqlValuesStr.append("\nVALUES\n");

            for (int i = 0; i < stopsComponentPropertyList.size(); i++) {
                StopsComponentProperty stopsComponentProperty = stopsComponentPropertyList.get(i);
                boolean flag = this.preventSQLInjectionStopsComponentProperty(stopsComponentProperty);
                if(flag) {
                    sqlValuesStr.append("(");
                    sqlValuesStr.append(SqlUtils.baseFieldValues(stopsComponentProperty) + ", ");
                    sqlValuesStr.append(allowableValues + ",");
                    sqlValuesStr.append(defaultValue + ",");
                    sqlValuesStr.append(description + ",");
                    sqlValuesStr.append(displayName + ",");
                    sqlValuesStr.append(name + ",");
                    sqlValuesStr.append(required + ",");
                    sqlValuesStr.append(sensitive + ",");
                    sqlValuesStr.append(propertySort + ",");
                    sqlValuesStr.append(example + ",");
                    sqlValuesStr.append(language + ",");
                    sqlValuesStr.append(stopsTemplateId);
                    sqlValuesStr.append(")");
                    if (i < stopsComponentPropertyList.size() - 1) {
                        sqlValuesStr.append(",\n");
                    }
                }
                this.reset();
            }
            sqlStr = sqlValuesStr.toString();
        }
        return sqlStr;
    }

}
