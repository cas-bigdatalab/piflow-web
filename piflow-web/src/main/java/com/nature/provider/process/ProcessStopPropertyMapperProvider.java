package com.nature.provider.process;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SqlUtils;
import com.nature.component.process.model.ProcessStop;
import com.nature.component.process.model.ProcessStopProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProcessStopPropertyMapperProvider {

    private String id;
    private String crtUser;
    private String crtDttmStr;
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
    private String processStopId;

    private void preventSQLInjectionProcessStopProperty(ProcessStopProperty processStopProperty) {
        if (null != processStopProperty && StringUtils.isNotBlank(processStopProperty.getLastUpdateUser())) {
            // Mandatory Field
            String id = processStopProperty.getId();
            String crtUser = processStopProperty.getCrtUser();
            String lastUpdateUser = processStopProperty.getLastUpdateUser();
            Boolean enableFlag = processStopProperty.getEnableFlag();
            Long version = processStopProperty.getVersion();
            Date crtDttm = processStopProperty.getCrtDttm();
            Date lastUpdateDttm = processStopProperty.getLastUpdateDttm();
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
            this.name = SqlUtils.preventSQLInjection(processStopProperty.getName());
            this.displayName = SqlUtils.preventSQLInjection(processStopProperty.getDisplayName());
            this.description = SqlUtils.preventSQLInjection(processStopProperty.getDescription());
            this.customValue = SqlUtils.preventSQLInjection(processStopProperty.getCustomValue());
            this.allowableValues = SqlUtils.preventSQLInjection(processStopProperty.getAllowableValues());
            this.required = (null == processStopProperty.getRequired() ? null : (processStopProperty.getRequired() ? 1 : 0));
            this.sensitive = (null == processStopProperty.getSensitive() ? null : (processStopProperty.getSensitive() ? 1 : 0));
            String processStopIdStr = (null != processStopProperty.getProcessStop() ? processStopProperty.getProcessStop().getId() : null);
            this.processStopId = (null != processStopIdStr ? SqlUtils.preventSQLInjection(processStopIdStr) : null);
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
        this.displayName = null;
        this.description = null;
        this.customValue = null;
        this.allowableValues = null;
        this.required = null;
        this.sensitive = null;
        this.processStopId = null;

    }

    public String addProcessStopProperty(ProcessStopProperty processStopProperty) {
        String sqlStr = "SELECT 0";
        this.preventSQLInjectionProcessStopProperty(processStopProperty);
        if (null != processStopProperty) {
            SQL sql = new SQL();

            // INSERT_INTO括号中为数据库表名
            sql.INSERT_INTO("FLOW_PROCESS_STOP_PROPERTY");
            // value中的第一个字符串为数据库中表对应的字段名
            // 除数字类型的字段外其他类型必须加单引号

            //先处理修改必填字段
            if (null == crtDttmStr) {
                String crtDttm = DateUtils.dateTimesToStr(new Date());
                crtDttmStr = SqlUtils.preventSQLInjection(crtDttm);
            }
            if (StringUtils.isBlank(crtUser)) {
                crtUser = SqlUtils.preventSQLInjection("-1");
            }
            sql.VALUES("ID", id);
            sql.VALUES("CRT_DTTM", crtDttmStr);
            sql.VALUES("CRT_USER", crtUser);
            sql.VALUES("LAST_UPDATE_DTTM", lastUpdateDttmStr);
            sql.VALUES("LAST_UPDATE_USER", lastUpdateUser);
            sql.VALUES("VERSION", version + "");
            sql.VALUES("ENABLE_FLAG", enableFlag + "");

            // 处理其他字段
            sql.VALUES("NAME", name);
            sql.VALUES("DISPLAY_NAME", displayName);
            sql.VALUES("DESCRIPTION", description);
            sql.VALUES("CUSTOM_VALUE", customValue);
            sql.VALUES("ALLOWABLE_VALUES", allowableValues);
            sql.VALUES("PROPERTY_REQUIRED", required + "");
            sql.VALUES("PROPERTY_SENSITIVE", sensitive + "");
            sql.VALUES("FK_FLOW_PROCESS_STOP_ID", processStopId);

            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    public String addProcessStopProperties(Map<String, List<ProcessStopProperty>> processStopPropertyList) {
        String sqlStr = "SELECT 0";
        List<ProcessStopProperty> processStopProperties = processStopPropertyList.get("processStopPropertyList");
        if (null != processStopProperties && processStopProperties.size() > 0) {
            StringBuffer sql = new StringBuffer();
            sql.append("INSERT INTO ");
            sql.append("FLOW_PROCESS_STOP_PROPERTY ");
            sql.append("(");
            sql.append("ID,");
            sql.append("CRT_DTTM,");
            sql.append("CRT_USER,");
            sql.append("LAST_UPDATE_DTTM,");
            sql.append("LAST_UPDATE_USER,");
            sql.append("VERSION,");
            sql.append("ENABLE_FLAG,");
            sql.append("NAME,");
            sql.append("DISPLAY_NAME,");
            sql.append("DESCRIPTION,");
            sql.append("CUSTOM_VALUE,");
            sql.append("ALLOWABLE_VALUES,");
            sql.append("PROPERTY_REQUIRED,");
            sql.append("PROPERTY_SENSITIVE,");
            sql.append("FK_FLOW_PROCESS_STOP_ID");
            sql.append(")");
            sql.append("VALUES");

            // 放值时必须保证先后顺序
            int i = 0;
            for (ProcessStopProperty processStopProperty : processStopProperties) {
                i++;
                if (null != processStopProperty) {
                    this.preventSQLInjectionProcessStopProperty(processStopProperty);
                    if (null == crtDttmStr) {
                        String crtDttm = DateUtils.dateTimesToStr(new Date());
                        crtDttmStr = SqlUtils.preventSQLInjection(crtDttm);
                    }
                    if (StringUtils.isBlank(crtUser)) {
                        crtUser = SqlUtils.preventSQLInjection("-1");
                    }
                    sql.append("(");
                    sql.append(id + ",");
                    sql.append(crtDttmStr + ",");
                    sql.append(crtUser + ",");
                    sql.append(lastUpdateDttmStr + ",");
                    sql.append(lastUpdateUser + ",");
                    sql.append(version + ",");
                    sql.append(enableFlag + ",");
                    sql.append(name + ",");
                    sql.append(displayName + ",");
                    sql.append(description + ",");
                    sql.append(customValue + ",");
                    sql.append(allowableValues + ",");
                    sql.append(required + ",");
                    sql.append(sensitive + ",");
                    sql.append(processStopId);
                    if (i != processStopProperties.size()) {
                        sql.append("),");
                    } else {
                        sql.append(")");
                    }
                    this.reset();
                }
            }
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    public String getStopPropertyByProcessStopId(String processStopId) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(processStopId)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("FLOW_PROCESS_STOP_PROPERTY");
            sql.WHERE("ENABLE_FLAG = 1");
            sql.WHERE("FK_FLOW_PROCESS_STOP_ID = " + SqlUtils.preventSQLInjection(processStopId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    public String updateProcessStopProperty(ProcessStopProperty processStopProperty) {
        String sqlStr = "select 0";
        this.preventSQLInjectionProcessStopProperty(processStopProperty);
        if (null != processStopProperty) {
            String id = processStopProperty.getId();
            if (StringUtils.isNotBlank(id)) {
                SQL sql = new SQL();
                sql.UPDATE("FLOW_PROCESS_STOP_PROPERTY");

                //先处理修改必填字段
                sql.SET("LAST_UPDATE_DTTM = " + lastUpdateDttmStr);
                sql.SET("LAST_UPDATE_USER = " + lastUpdateUser);
                sql.SET("VERSION = " + (version + 1));

                // 处理其他字段
                sql.SET("ENABLE_FLAG = " + enableFlag);
                sql.SET("NAME = " + name);
                sql.SET("DISPLAY_NAME = " + displayName);
                sql.SET("DESCRIPTION = " + description);
                sql.SET("CUSTOM_VALUE = " + customValue);
                sql.SET("ALLOWABLE_VALUES = " + allowableValues);
                sql.SET("PROPERTY_REQUIRED = " + required);
                sql.SET("PROPERTY_SENSITIVE = " + sensitive);
                if (null != processStopId) {
                    sql.SET("FK_FLOW_PROCESS_STOP_ID = " + processStopId);
                }
                sql.WHERE("ENABLE_FLAG = 1");
                sql.WHERE("VERSION = " + version);
                sql.WHERE("ID = " + SqlUtils.preventSQLInjection(id));

                sqlStr = sql.toString();
            }
        }
        this.reset();
        return sqlStr;
    }

    public String updateEnableFlagByProcessStopId(String processStopId, String username) {
        String sqlStr = "select 0";
        if (!StringUtils.isAnyEmpty(processStopId, username)) {
            SQL sql = new SQL();
            sql.UPDATE("FLOW_PROCESS_STOP_PROPERTY");
            sql.SET("LAST_UPDATE_DTTM = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
            sql.SET("LAST_UPDATE_USER = " + SqlUtils.preventSQLInjection(username));
            sql.SET("VERSION=(VERSION+1)");
            sql.SET("ENABLE_FLAG = 0");
            sql.WHERE("ENABLE_FLAG = 1");
            sql.WHERE("FK_FLOW_PROCESS_STOP_ID = " + SqlUtils.preventSQLInjection(processStopId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }
}
