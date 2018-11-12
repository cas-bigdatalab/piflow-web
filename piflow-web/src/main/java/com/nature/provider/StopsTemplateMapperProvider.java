package com.nature.provider;

import com.nature.base.util.DateUtils;
import com.nature.component.workFlow.model.Stops;
import com.nature.component.workFlow.model.StopsTemplate;
import org.apache.ibatis.jdbc.SQL;

import com.nature.base.util.Utils;

import java.util.List;
import java.util.Map;

public class StopsTemplateMapperProvider {

    /**
     * 查詢所有stops模板
     * 
     * @return
     */
    public String getStopsTemplateList() {
        String sqlStr = "SELECT ''";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_stops_template");
        sql.WHERE("enable_flag = 1 ");
        sqlStr = sql.toString() + ";";
        return sqlStr;
    }

    /**
     * 根據stops模板id查詢模板
     * 
     * @param id
     * @return
     */
    public String getStopsTemplateById(String id) {
        String sqlStr = "SELECT 0";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_stops_template");
        sql.WHERE("enable_flag = 1 ");
        sql.WHERE("id = " + Utils.addSqlStr(id));
        sqlStr = sql.toString() + ";";
        return sqlStr;
    }

    /**
     * 根据stopsName查询StopsTemplate
     * 
     * @param stopsName
     * @return
     */
    public String getStopsTemplateByName(String stopsName) {
        String sqlStr = "SELECT ''";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_stops_template");
        sql.WHERE("enable_flag = 1 ");
        sql.WHERE("name = " + Utils.addSqlStr(stopsName));
        sqlStr = sql.toString() + ";";
        return sqlStr;
    }

    public String insertStopsTemplate(Map map) {
        String sqlStr = "SELECT ''";
        List<StopsTemplate> stopsTemplateList = (List<StopsTemplate>) map.get("stopsTemplateList");
        if (null != stopsTemplateList && stopsTemplateList.size() > 0) {
            SQL sqlColumns = new SQL();
            sqlColumns.INSERT_INTO("flow_stops_template");
            sqlColumns.INTO_COLUMNS(
                    "id",
                    "crt_dttm",
                    "crt_user",
                    "enable_flag",
                    "last_update_dttm",
                    "last_update_user",
                    "version",
                    "bundel",
                    "description",
                    "groups",
                    "name",
                    "owner",
                    "inports",
                    "in_port_type",
                    "outports",
                    "out_port_type"
            );
            StringBuffer sqlValuesStr = new StringBuffer();
            sqlValuesStr.append("\nVALUES\n");
            for (int i = 0; i < stopsTemplateList.size(); i++) {
                StopsTemplate stopsTemplate = stopsTemplateList.get(i);
                if (null != stopsTemplate) {
                    sqlValuesStr.append("(");
                    sqlValuesStr.append(Utils.addSqlStr(stopsTemplate.getId()) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(DateUtils.dateTimesToStr(stopsTemplate.getCrtDttm())) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(stopsTemplate.getCrtUser()) + ",");
                    sqlValuesStr.append(stopsTemplate.getEnableFlag() ? 1 + "," : 0 + ",");
                    sqlValuesStr.append(Utils.addSqlStr(DateUtils.dateTimesToStr(stopsTemplate.getLastUpdateDttm())) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(stopsTemplate.getLastUpdateUser()) + ",");
                    sqlValuesStr.append((null != stopsTemplate.getVersion() ? stopsTemplate.getVersion() : 0) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(stopsTemplate.getBundel()) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(stopsTemplate.getDescription().equals("null") ? "" : stopsTemplate.getDescription()) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(stopsTemplate.getGroups()) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(stopsTemplate.getName()) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(stopsTemplate.getOwner()) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(stopsTemplate.getInports()) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(null != stopsTemplate.getInPortType() ? stopsTemplate.getInPortType().name() : "") + ",");
                    sqlValuesStr.append(Utils.addSqlStr(stopsTemplate.getOutports()) + ",");
                    sqlValuesStr.append(Utils.addSqlStr(null != stopsTemplate.getOutPortType() ? stopsTemplate.getOutPortType().name() : "") );
                    sqlValuesStr.append(")");
                    if (i < stopsTemplateList.size() - 1) {
                        sqlValuesStr.append(",\n");
                    }
                }
            }
            sqlStr = sqlColumns.toString() + sqlValuesStr.toString() +";";
        }
        return sqlStr;
    }

}
