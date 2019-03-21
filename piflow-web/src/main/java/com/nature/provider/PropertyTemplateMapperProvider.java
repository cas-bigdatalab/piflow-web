package com.nature.provider;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
import com.nature.component.flow.model.PropertyTemplate;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public class PropertyTemplateMapperProvider {

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
            sql.WHERE("fk_stops_id = " + Utils.addSqlStr(stopsId));
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
                    Boolean required = (null == propertyTemplate.getRequired()) ? false : propertyTemplate.getRequired();
                    Boolean sensitive = (null == propertyTemplate.getSensitive()) ? false : propertyTemplate.getSensitive();
                    sqlValuesStr.append("(");
                    sqlValuesStr.append(Utils.addSqlStr(propertyTemplate.getId()) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(DateUtils.dateTimesToStr(propertyTemplate.getCrtDttm())) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(propertyTemplate.getCrtUser()) + ",");
                    sqlValuesStr.append(propertyTemplate.getEnableFlag() ? 1 + "," : 0 + ",");
                    sqlValuesStr.append(Utils.addSqlStr(DateUtils.dateTimesToStr(propertyTemplate.getLastUpdateDttm())) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(propertyTemplate.getLastUpdateUser()) + ",");
                    sqlValuesStr.append((null != propertyTemplate.getVersion() ? propertyTemplate.getVersion() : 0) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(propertyTemplate.getAllowableValues()) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(propertyTemplate.getDefaultValue()) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(propertyTemplate.getDescription().equals("null") ? "" : propertyTemplate.getDescription()) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(propertyTemplate.getDisplayName()) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(propertyTemplate.getName()) + ",");
                    sqlValuesStr.append(required ? 1 + "," : 0 + ",");
                    sqlValuesStr.append(sensitive ? 1 + "," : 0 + ",");
                    sqlValuesStr.append(Utils.addSqlStr(null != propertyTemplate.getStopsTemplate() ? propertyTemplate.getStopsTemplate() : ""));
                    sqlValuesStr.append(")");
                    if (i < propertyTemplateList.size() - 1) {
                        sqlValuesStr.append(",\n");
                    }
                }
            }
            sqlStr = (sqlColumns.toString() + sqlValuesStr.toString());
        }
        return sqlStr;
    }

}
