package cn.cnic.component.process.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.process.entity.ProcessStopCustomizedProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProcessStopCustomizedPropertyMapperProvider {

    private String id;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String name;
    private String customValue;
    private String description;
    private String processStopId;

    private boolean preventSQLInjectionProcessStopCustomizedProperty(ProcessStopCustomizedProperty processStopCustomizedProperty) {
        if (null == processStopCustomizedProperty || StringUtils.isBlank(processStopCustomizedProperty.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        this.id = SqlUtils.preventSQLInjection(processStopCustomizedProperty.getId());
        Long version = processStopCustomizedProperty.getVersion();
        this.version = (null != version ? version : 0L);
        Boolean enableFlag = processStopCustomizedProperty.getEnableFlag();
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(processStopCustomizedProperty.getLastUpdateUser());
        Date lastUpdateDttm = processStopCustomizedProperty.getLastUpdateDttm();
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

        // Selection field
        this.name = SqlUtils.preventSQLInjection(processStopCustomizedProperty.getName());
        this.customValue = SqlUtils.preventSQLInjection(processStopCustomizedProperty.getCustomValue());
        this.description = SqlUtils.preventSQLInjection(processStopCustomizedProperty.getDescription());
        String processStopsIdStr = (null != processStopCustomizedProperty.getProcessStop() ? processStopCustomizedProperty.getProcessStop().getId() : null);
        this.processStopId = (null != processStopsIdStr ? SqlUtils.preventSQLInjection(processStopsIdStr) : null);
        return true;
    }

    private void reset() {
        this.id = null;
        this.lastUpdateDttmStr = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.name = null;
        this.customValue = null;
        this.description = null;
        this.processStopId = null;
    }

    /**
     * Insert "list<ProcessStopCustomizedProperty>" Note that the method of spelling "sql" must use "map" to connect the "Param" content to the key value.
     *
     * @param map (Content: "processStopCustomizedPropertyList" with a value of "List<ProcessStopCustomizedProperty>")
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String addProcessStopCustomizedPropertyList(Map map) {
        List<ProcessStopCustomizedProperty> processStopCustomizedPropertyList = (List<ProcessStopCustomizedProperty>) map.get("processStopCustomizedPropertyList");
        StringBuffer sqlStrBuffer = new StringBuffer();
        if (null != processStopCustomizedPropertyList && processStopCustomizedPropertyList.size() > 0) {
            sqlStrBuffer.append("INSERT INTO ");
            sqlStrBuffer.append("process_stops_customized_property ");
            sqlStrBuffer.append("(");
            sqlStrBuffer.append(SqlUtils.baseFieldName() + ", ");
            sqlStrBuffer.append("name,");
            sqlStrBuffer.append("custom_value,");
            sqlStrBuffer.append("description,");
            sqlStrBuffer.append("fk_flow_process_stop_id");
            sqlStrBuffer.append(") ");
            sqlStrBuffer.append("VALUES");
            int i = 0;
            for (ProcessStopCustomizedProperty processStopCustomizedProperty : processStopCustomizedPropertyList) {
                i++;
                boolean flag = this.preventSQLInjectionProcessStopCustomizedProperty(processStopCustomizedProperty);
                if(flag) {
                    // You can't make a mistake when you splice
                    sqlStrBuffer.append("(");
                    sqlStrBuffer.append(SqlUtils.baseFieldValues(processStopCustomizedProperty) + ", ");
                    sqlStrBuffer.append(name + ",");
                    sqlStrBuffer.append(customValue + ",");
                    sqlStrBuffer.append(description + ",");
                    sqlStrBuffer.append(processStopId + " ");
                    if (i != processStopCustomizedPropertyList.size()) {
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

    public String addProcessStopCustomizedProperty(ProcessStopCustomizedProperty processStopCustomizedProperty) {
        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionProcessStopCustomizedProperty(processStopCustomizedProperty);
        if (flag) {
            StringBuffer sqlStrBuffer = new StringBuffer();
            sqlStrBuffer.append("INSERT INTO process_stops_customized_property");
            sqlStrBuffer.append("(");
            sqlStrBuffer.append(SqlUtils.baseFieldName() + ", ");
            sqlStrBuffer.append("name, ");
            sqlStrBuffer.append("custom_value, ");
            sqlStrBuffer.append("description, ");
            sqlStrBuffer.append("fk_flow_process_stop_id ");
            sqlStrBuffer.append(") ");
            sqlStrBuffer.append("VALUES");
            sqlStrBuffer.append("(");
            sqlStrBuffer.append(SqlUtils.baseFieldValues(processStopCustomizedProperty) + ", ");
            sqlStrBuffer.append(name + ",");
            sqlStrBuffer.append(customValue + ",");
            sqlStrBuffer.append(description + ",");
            sqlStrBuffer.append(processStopId + " ");
            sqlStrBuffer.append(")");
            sqlStr = sqlStrBuffer.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * update processStopCustomizedProperty
     *
     * @param processStopCustomizedProperty
     * @return
     */
    public String updateProcessStopCustomizedProperty(ProcessStopCustomizedProperty processStopCustomizedProperty) {
        String sqlStr = "";
        boolean flag = this.preventSQLInjectionProcessStopCustomizedProperty(processStopCustomizedProperty);
        if (flag) {

            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("process_stops_customized_property");
            // The first string in the SET is the name of the field corresponding to the table in the database

            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag = " + enableFlag);
            sql.SET("name = " + name);
            sql.SET("custom_value = " + customValue);
            sql.SET("description = " + description);
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
    public String updateEnableFlagByProcessStopId(String username, String id) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }
        SQL sql = new SQL();
        sql.UPDATE("process_stops_customized_property");
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
    public String updateProcessStopCustomizedPropertyCustomValue(String username, String content, String id) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }
        SQL sql = new SQL();
        sql.UPDATE("process_stops_customized_property");
        sql.SET("custom_value = " + SqlUtils.preventSQLInjection(content));
        sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
        sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
        sql.SET("version = " + 1);
        sql.WHERE("enable_flag = 1");
        sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));
        return sql.toString();
    }

}
