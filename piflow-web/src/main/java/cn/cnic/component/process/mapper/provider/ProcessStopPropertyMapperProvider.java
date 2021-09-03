package cn.cnic.component.process.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.process.entity.ProcessStopProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProcessStopPropertyMapperProvider {

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

    private boolean preventSQLInjectionProcessStopProperty(ProcessStopProperty processStopProperty) {
        if (null == processStopProperty || StringUtils.isBlank(processStopProperty.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        String lastUpdateUser = processStopProperty.getLastUpdateUser();
        Boolean enableFlag = processStopProperty.getEnableFlag();
        Long version = processStopProperty.getVersion();
        Date lastUpdateDttm = processStopProperty.getLastUpdateDttm();
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
        this.lastUpdateUser = SqlUtils.preventSQLInjection(lastUpdateUser);
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
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
        return true;
    }

    private void reset() {
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
        boolean flag = this.preventSQLInjectionProcessStopProperty(processStopProperty);
        if (flag) {
            StringBuffer strBuf = new StringBuffer();
            // INSERT_INTO brackets is table name
            strBuf.append("INSERT INTO flow_process_stop_property ");
            strBuf.append("( ");
            strBuf.append(SqlUtils.baseFieldName() + ", ");
            strBuf.append("name, ");
            strBuf.append("display_name, ");
            strBuf.append("description, ");
            strBuf.append("custom_value, ");
            strBuf.append("allowable_values, ");
            strBuf.append("property_required, ");
            strBuf.append("property_sensitive, ");
            strBuf.append("fk_flow_process_stop_id ");
            strBuf.append(") VALUES ( ");
            strBuf.append(SqlUtils.baseFieldValues(processStopProperty) + ", ");
            strBuf.append(this.name + ", ");
            strBuf.append(this.displayName + ", ");
            strBuf.append(this.description + ", ");
            strBuf.append(this.customValue + ", ");
            strBuf.append(this.allowableValues + ", ");
            strBuf.append(this.required + ", ");
            strBuf.append(this.sensitive + ", ");
            strBuf.append(this.processStopId + " ");
            strBuf.append(") ");
            sqlStr = strBuf.toString();
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
            sql.append("flow_process_stop_property ");
            sql.append("(");
            sql.append(SqlUtils.baseFieldName() + ", ");
            sql.append("name,");
            sql.append("display_name,");
            sql.append("description,");
            sql.append("custom_value,");
            sql.append("allowable_values,");
            sql.append("property_required,");
            sql.append("property_sensitive,");
            sql.append("fk_flow_process_stop_id");
            sql.append(")");
            sql.append("VALUES");

            // The order must be guaranteed
            int i = 0;
            for (ProcessStopProperty processStopProperty : processStopProperties) {
                i++;
                boolean flag = this.preventSQLInjectionProcessStopProperty(processStopProperty);
                if (flag) {
                    sql.append("(");
                    sql.append(SqlUtils.baseFieldValues(processStopProperty) + ", ");
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
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(processStopId)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("flow_process_stop_property");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_stop_id = " + SqlUtils.preventSQLInjection(processStopId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    public String updateProcessStopProperty(ProcessStopProperty processStopProperty) {
        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionProcessStopProperty(processStopProperty);
        if (flag) {
            String id = processStopProperty.getId();
            if (StringUtils.isNotBlank(id)) {
                SQL sql = new SQL();
                sql.UPDATE("flow_process_stop_property");

                //Process the required fields first
                sql.SET("last_update_dttm = " + lastUpdateDttmStr);
                sql.SET("last_update_user = " + lastUpdateUser);
                sql.SET("version = " + (version + 1));

                // handle other fields
                sql.SET("enable_flag = " + enableFlag);
                sql.SET("name = " + name);
                sql.SET("display_name = " + displayName);
                sql.SET("description = " + description);
                sql.SET("custom_value = " + customValue);
                sql.SET("allowable_values = " + allowableValues);
                sql.SET("property_required = " + required);
                sql.SET("property_sensitive = " + sensitive);
                if (null != processStopId) {
                    sql.SET("fk_flow_process_stop_id = " + processStopId);
                }
                sql.WHERE("enable_flag = 1");
                sql.WHERE("version = " + version);
                sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));

                sqlStr = sql.toString();
            }
        }
        this.reset();
        return sqlStr;
    }

    public String updateEnableFlagByProcessStopId(String processStopId, String username) {
        String sqlStr = "SELECT 0";
        if (!StringUtils.isAnyEmpty(processStopId, username)) {
            SQL sql = new SQL();
            sql.UPDATE("flow_process_stop_property");
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
            sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
            sql.SET("version=(version+1)");
            sql.SET("enable_flag = 0");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_stop_id = " + SqlUtils.preventSQLInjection(processStopId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }
}
