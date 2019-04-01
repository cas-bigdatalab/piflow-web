package com.nature.provider.flow;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SqlUtils;
import com.nature.component.flow.model.PropertyTemplate;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class PropertyTemplateMapperProvider {

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
    private String stopsTemplateId;

    private void preventSQLInjectionPropertyTemplate(PropertyTemplate propertyTemplate) {
        if (null != propertyTemplate && StringUtils.isNotBlank(propertyTemplate.getLastUpdateUser())) {
            // Mandatory Field
            String id = propertyTemplate.getId();
            String crtUser = propertyTemplate.getCrtUser();
            String lastUpdateUser = propertyTemplate.getLastUpdateUser();
            Boolean enableFlag = propertyTemplate.getEnableFlag();
            Long version = propertyTemplate.getVersion();
            Date crtDttm = propertyTemplate.getCrtDttm();
            Date lastUpdateDttm = propertyTemplate.getLastUpdateDttm();
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
            this.allowableValues = SqlUtils.preventSQLInjection(propertyTemplate.getAllowableValues());
            this.defaultValue = SqlUtils.preventSQLInjection(propertyTemplate.getDefaultValue());
            this.description = SqlUtils.preventSQLInjection(propertyTemplate.getDescription());
            this.displayName = SqlUtils.preventSQLInjection(propertyTemplate.getDisplayName());
            this.name = SqlUtils.preventSQLInjection(propertyTemplate.getName());
            this.required = (null == propertyTemplate.getRequired() ? 0 : (propertyTemplate.getRequired() ? 1 : 0));
            this.sensitive = (null == propertyTemplate.getSensitive() ? 0 : (propertyTemplate.getSensitive() ? 1 : 0));
            this.stopsTemplateId = SqlUtils.preventSQLInjection(propertyTemplate.getStopsTemplate());
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
        this.stopsTemplateId = null;
    }

    /**
     * 根据stops模板id查询对应的stops的所有属性
     *
     * @param stopsId
     * @return
     */
    public String getPropertyTemplateBySotpsId(String stopsId) {

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

    public String insertPropertyTemplate(Map map) {
        String sqlStr = "SELECT 0";
        List<PropertyTemplate> propertyTemplateList = (List<PropertyTemplate>) map.get("propertyTemplateList");
        if (null != propertyTemplateList && propertyTemplateList.size() > 0) {
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
                    "fk_stops_id"
            );
            StringBuffer sqlValuesStr = new StringBuffer();
            sqlValuesStr.append("\nVALUES\n");
            for (int i = 0; i < propertyTemplateList.size(); i++) {
                PropertyTemplate propertyTemplate = propertyTemplateList.get(i);
                if (null != propertyTemplate) {
                    this.preventSQLInjectionPropertyTemplate(propertyTemplate);
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
                    sqlValuesStr.append(stopsTemplateId);
                    sqlValuesStr.append(")");
                    if (i < propertyTemplateList.size() - 1) {
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
