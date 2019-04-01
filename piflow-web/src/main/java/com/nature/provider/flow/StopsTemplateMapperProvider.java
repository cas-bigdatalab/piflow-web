package com.nature.provider.flow;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SqlUtils;
import com.nature.component.flow.model.Stops;
import com.nature.component.flow.model.StopsTemplate;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class StopsTemplateMapperProvider {

    private String id;
    private String crtUser;
    private String crtDttmStr;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String bundel;
    private String description;
    private String groups;
    private String name;
    private String owner;
    private String inports;
    private String inPortType;
    private String outports;
    private String outPortType;

    private void preventSQLInjectionStops(StopsTemplate stopsTemplate) {
        if (null != stopsTemplate && StringUtils.isNotBlank(stopsTemplate.getLastUpdateUser())) {
            // Mandatory Field
            String id = stopsTemplate.getId();
            String crtUser = stopsTemplate.getCrtUser();
            String lastUpdateUser = stopsTemplate.getLastUpdateUser();
            Boolean enableFlag = stopsTemplate.getEnableFlag();
            Long version = stopsTemplate.getVersion();
            Date crtDttm = stopsTemplate.getCrtDttm();
            Date lastUpdateDttm = stopsTemplate.getLastUpdateDttm();
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
            this.bundel = SqlUtils.preventSQLInjection(stopsTemplate.getBundel());
            this.description = SqlUtils.preventSQLInjection(stopsTemplate.getDescription());
            this.groups = SqlUtils.preventSQLInjection(stopsTemplate.getGroups());
            this.name = SqlUtils.preventSQLInjection(stopsTemplate.getName());
            this.inports = SqlUtils.preventSQLInjection(stopsTemplate.getInports());
            this.inPortType = SqlUtils.preventSQLInjection(null != stopsTemplate.getInPortType() ? stopsTemplate.getInPortType().name() : null);
            this.outports = SqlUtils.preventSQLInjection(stopsTemplate.getOutports());
            this.outPortType = SqlUtils.preventSQLInjection(null != stopsTemplate.getOutPortType() ? stopsTemplate.getOutPortType().name() : null);
            this.owner = SqlUtils.preventSQLInjection(stopsTemplate.getOwner());
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
        this.bundel = null;
        this.description = null;
        this.groups = null;
        this.name = null;
        this.inports = null;
        this.inPortType = null;
        this.outports = null;
        this.outPortType = null;
        this.owner = null;
    }

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
        sqlStr = sql.toString();
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
        sql.WHERE("id = " + SqlUtils.addSqlStr(id));
        sqlStr = sql.toString();
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
        sql.WHERE("name = " + SqlUtils.addSqlStr(stopsName));
        sqlStr = sql.toString();
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
                    this.preventSQLInjectionStops(stopsTemplate);
                    sqlValuesStr.append("(");
                    //先处理修改必填字段
                    if (null == crtDttmStr) {
                        String crtDttm = DateUtils.dateTimesToStr(new Date());
                        crtDttmStr = SqlUtils.preventSQLInjection(crtDttm);
                    }
                    if (StringUtils.isBlank(crtUser)) {
                        crtUser = SqlUtils.preventSQLInjection("-1");
                    }
                    sqlValuesStr.append(id + ",");
                    sqlValuesStr.append(crtDttmStr + ",");
                    sqlValuesStr.append(crtUser + ",");
                    sqlValuesStr.append(enableFlag + ",");
                    sqlValuesStr.append(lastUpdateDttmStr + ",");
                    sqlValuesStr.append(lastUpdateUser + ",");
                    sqlValuesStr.append(version + ",");
                    sqlValuesStr.append(bundel + ",");
                    sqlValuesStr.append(description + ",");
                    sqlValuesStr.append(groups + ",");
                    sqlValuesStr.append(name + ",");
                    sqlValuesStr.append(owner + ",");
                    sqlValuesStr.append(inports + ",");
                    sqlValuesStr.append(inPortType + ",");
                    sqlValuesStr.append(outports + ",");
                    sqlValuesStr.append(outPortType);
                    sqlValuesStr.append(")");
                    if (i < stopsTemplateList.size() - 1) {
                        sqlValuesStr.append(",\n");
                    }
                    this.reset();
                }
            }
            sqlStr = sqlColumns.toString() + sqlValuesStr.toString() + ";";
        }
        return sqlStr;
    }

}
