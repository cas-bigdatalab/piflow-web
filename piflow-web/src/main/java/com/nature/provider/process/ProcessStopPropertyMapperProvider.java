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

    public String addProcessStopProperty(ProcessStopProperty processStopProperty) {
        String sqlStr = "SELECT 0";
        if (null != processStopProperty) {
            String id = processStopProperty.getId();
            String crtUser = processStopProperty.getCrtUser();
            Date crtDttm = processStopProperty.getCrtDttm();
            String lastUpdateUser = processStopProperty.getLastUpdateUser();
            Date lastUpdateDttm = processStopProperty.getLastUpdateDttm();
            Long version = processStopProperty.getVersion();
            Boolean enableFlag = processStopProperty.getEnableFlag();
            String name = processStopProperty.getName();
            String displayName = processStopProperty.getDisplayName();
            String description = processStopProperty.getDescription();
            String customValue = processStopProperty.getCustomValue();
            String allowableValues = processStopProperty.getAllowableValues();
            Boolean required = processStopProperty.getRequired();
            Boolean sensitive = processStopProperty.getSensitive();
            ProcessStop processStop = processStopProperty.getProcessStop();

            SQL sql = new SQL();

            // INSERT_INTO括号中为数据库表名
            sql.INSERT_INTO("FLOW_PROCESS_STOP_PROPERTY");
            // value中的第一个字符串为数据库中表对应的字段名
            // 除数字类型的字段外其他类型必须加单引号

            //先处理修改必填字段
            if (null == crtDttm) {
                crtDttm = new Date();
            }
            if (StringUtils.isBlank(crtUser)) {
                crtUser = "-1";
            }
            if (null == lastUpdateDttm) {
                lastUpdateDttm = new Date();
            }
            if (StringUtils.isBlank(lastUpdateUser)) {
                lastUpdateUser = "-1";
            }
            if (null == version) {
                version = 0L;
            }
            if (null == enableFlag) {
                enableFlag = true;
            }
            sql.VALUES("ID", SqlUtils.addSqlStrAndReplace(id));
            sql.VALUES("CRT_DTTM", SqlUtils.addSqlStrAndReplace(DateUtils.dateTimesToStr(crtDttm)));
            sql.VALUES("CRT_USER", SqlUtils.addSqlStrAndReplace(crtUser));
            sql.VALUES("LAST_UPDATE_DTTM", SqlUtils.addSqlStrAndReplace(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.VALUES("LAST_UPDATE_USER", SqlUtils.addSqlStrAndReplace(lastUpdateUser));
            sql.VALUES("VERSION", version + "");
            sql.VALUES("ENABLE_FLAG", (enableFlag ? 1 : 0) + "");

            // 处理其他字段
            if (null != name) {
                sql.VALUES("NAME", SqlUtils.addSqlStrAndReplace(name));
            }
            if (null != displayName) {
                sql.VALUES("DISPLAY_NAME", SqlUtils.addSqlStrAndReplace(displayName));
            }
            if (null != description) {
                sql.VALUES("DESCRIPTION", SqlUtils.addSqlStrAndReplace(description));
            }
            if (null != customValue) {
                sql.VALUES("CUSTOM_VALUE", SqlUtils.addSqlStrAndReplace(customValue));
            }
            if (null != allowableValues) {
                sql.VALUES("ALLOWABLE_VALUES", SqlUtils.addSqlStrAndReplace(allowableValues));
            }
            if (null != required) {
                sql.VALUES("PROPERTY_REQUIRED", (required ? 1 : 0) + "");
            }
            if (null != sensitive) {
                sql.VALUES("PROPERTY_SENSITIVE", (sensitive ? 1 : 0) + "");
            }
            if (null != processStop) {
                sql.VALUES("FK_FLOW_PROCESS_STOP_ID", SqlUtils.addSqlStrAndReplace(processStop.getId()));
            }

            sqlStr = sql.toString();
        }
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
                    String id = processStopProperty.getId();
                    String crtUser = processStopProperty.getCrtUser();
                    Date crtDttm = processStopProperty.getCrtDttm();
                    String lastUpdateUser = processStopProperty.getLastUpdateUser();
                    Date lastUpdateDttm = processStopProperty.getLastUpdateDttm();
                    Long version = processStopProperty.getVersion();
                    Boolean enableFlag = processStopProperty.getEnableFlag();
                    String name = processStopProperty.getName();
                    String displayName = processStopProperty.getDisplayName();
                    String description = processStopProperty.getDescription();
                    String customValue = processStopProperty.getCustomValue();
                    String allowableValues = processStopProperty.getAllowableValues();
                    Boolean required = processStopProperty.getRequired();
                    Boolean sensitive = processStopProperty.getSensitive();
                    ProcessStop processStop = processStopProperty.getProcessStop();

                    if (null == crtDttm) {
                        crtDttm = new Date();
                    }
                    if (null == crtUser) {
                        crtUser = "-1";
                    }
                    if (null == lastUpdateDttm) {
                        lastUpdateDttm = new Date();
                    }
                    if (null == lastUpdateUser) {
                        lastUpdateUser = "-1";
                    }
                    if (null == version) {
                        version = 0L;
                    }
                    if (null == enableFlag) {
                        enableFlag = true;
                    }

                    sql.append("(");
                    sql.append(SqlUtils.addSqlStrAndReplace(id) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(DateUtils.dateTimesToStr(crtDttm)) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(crtUser) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(DateUtils.dateTimesToStr(lastUpdateDttm)) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(lastUpdateUser) + ",");
                    sql.append(version + ",");
                    sql.append((enableFlag ? 1 : 0) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(name) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(displayName) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(description) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(customValue) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(allowableValues) + ",");
                    sql.append((null != required ? (required ? 1 : 0) : 0) + ",");
                    sql.append((null != sensitive ? (sensitive ? 1 : 0) : 0) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(processStop.getId()));
                    if (i != processStopProperties.size()) {
                        sql.append("),");
                    } else {
                        sql.append(")");
                    }
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
            sql.WHERE("FK_FLOW_PROCESS_STOP_ID = " + SqlUtils.addSqlStr(processStopId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    public String updateProcessStopProperty(ProcessStopProperty processStopProperty) {
        String sqlStr = "select 0";
        if (null != processStopProperty) {
            String id = processStopProperty.getId();
            if (StringUtils.isNotBlank(id)) {
                String lastUpdateUser = processStopProperty.getLastUpdateUser();
                Date lastUpdateDttm = processStopProperty.getLastUpdateDttm();
                Long version = processStopProperty.getVersion();
                Boolean enableFlag = processStopProperty.getEnableFlag();
                String name = processStopProperty.getName();
                String displayName = processStopProperty.getDisplayName();
                String description = processStopProperty.getDescription();
                String customValue = processStopProperty.getCustomValue();
                String allowableValues = processStopProperty.getAllowableValues();
                Boolean required = processStopProperty.getRequired();
                Boolean sensitive = processStopProperty.getSensitive();
                ProcessStop processStop = processStopProperty.getProcessStop();

                SQL sql = new SQL();
                sql.UPDATE("FLOW_PROCESS_STOP_PROPERTY");

                //先处理修改必填字段
                if (null == lastUpdateDttm) {
                    lastUpdateDttm = new Date();
                }
                if (StringUtils.isBlank(lastUpdateUser)) {
                    lastUpdateUser = "-1";
                }
                if (null == version) {
                    version = 0L;
                }
                String lastUpdateDttmStr = DateUtils.dateTimesToStr(lastUpdateDttm);
                sql.SET("LAST_UPDATE_DTTM = " + SqlUtils.addSqlStr(lastUpdateDttmStr));
                sql.SET("LAST_UPDATE_USER = " + SqlUtils.addSqlStr(lastUpdateUser));
                sql.SET("VERSION = " + (version + 1));

                // 处理其他字段
                if (null != enableFlag) {
                    sql.SET("ENABLE_FLAG = " + (enableFlag ? 1 : 0));
                }
                if (StringUtils.isNotBlank(name)) {
                    sql.SET("NAME = " + SqlUtils.addSqlStr(name));
                }
                if (StringUtils.isNotBlank(displayName)) {
                    sql.SET("DISPLAY_NAME = " + SqlUtils.addSqlStr(displayName));
                }
                if (StringUtils.isNotBlank(description)) {
                    sql.SET("DESCRIPTION = " + SqlUtils.addSqlStr(description));
                }
                if (StringUtils.isNotBlank(customValue)) {
                    sql.SET("CUSTOM_VALUE = " + SqlUtils.addSqlStr(customValue));
                }
                if (StringUtils.isNotBlank(allowableValues)) {
                    sql.SET("ALLOWABLE_VALUES = " + SqlUtils.addSqlStr(allowableValues));
                }
                if (null != required) {
                    sql.SET("PROPERTY_REQUIRED = " + (required ? 1 : 0));
                }
                if (null != sensitive) {
                    sql.SET("PROPERTY_SENSITIVE = " + (sensitive ? 1 : 0));
                }
                if (null != processStop) {
                    sql.SET("FK_FLOW_PROCESS_STOP_ID = " + (processStop.getId()));
                }

                sql.WHERE("ENABLE_FLAG = 1");
                sql.WHERE("VERSION = " + version);
                sql.WHERE("ID = " + SqlUtils.addSqlStr(id));

                sqlStr = sql.toString();
            }
        }
        return sqlStr;
    }

    public String updateEnableFlagByProcessStopId(String processStopId,String username) {
        String sqlStr = "select 0";
        if (!StringUtils.isAnyEmpty(processStopId,username)) {
            SQL sql = new SQL();
            sql.UPDATE("FLOW_PROCESS_STOP_PROPERTY");
            sql.SET("LAST_UPDATE_DTTM = " + SqlUtils.addSqlStr(DateUtils.dateTimesToStr(new Date())));
            sql.SET("LAST_UPDATE_USER = " + SqlUtils.addSqlStr(username));
            sql.SET("VERSION=(VERSION+1)");
            sql.SET("ENABLE_FLAG = 0");
            sql.WHERE("ENABLE_FLAG = 1");
            sql.WHERE("FK_FLOW_PROCESS_STOP_ID = " + SqlUtils.addSqlStr(processStopId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }
}
