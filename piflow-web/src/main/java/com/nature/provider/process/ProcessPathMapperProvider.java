package com.nature.provider.process;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
import com.nature.component.process.model.Process;
import com.nature.component.process.model.ProcessPath;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProcessPathMapperProvider {
    /**
     * 添加 processPath
     *
     * @param processPath
     * @return
     */
    public String addProcessPath(ProcessPath processPath) {
        String sqlStr = "select 0";
        if (null != processPath) {
            String id = processPath.getId();
            String crtUser = processPath.getCrtUser();
            Date crtDttm = processPath.getCrtDttm();
            String lastUpdateUser = processPath.getLastUpdateUser();
            Date lastUpdateDttm = processPath.getLastUpdateDttm();
            Long version = processPath.getVersion();
            Boolean enableFlag = processPath.getEnableFlag();
            String from = processPath.getFrom();
            String outport = processPath.getOutport();
            String inport = processPath.getInport();
            String to = processPath.getTo();
            String pageId = processPath.getPageId();
            Process process = processPath.getProcess();

            SQL sql = new SQL();

            // INSERT_INTO括号中为数据库表名
            sql.INSERT_INTO("FLOW_PROCESS_PATH");
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
            sql.VALUES("ID", Utils.addSqlStrAndReplace(id));
            sql.VALUES("CRT_DTTM", Utils.addSqlStrAndReplace(DateUtils.dateTimesToStr(crtDttm)));
            sql.VALUES("CRT_USER", Utils.addSqlStrAndReplace(crtUser));
            sql.VALUES("LAST_UPDATE_DTTM", Utils.addSqlStrAndReplace(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.VALUES("LAST_UPDATE_USER", Utils.addSqlStrAndReplace(lastUpdateUser));
            sql.VALUES("VERSION", version + "");
            sql.VALUES("ENABLE_FLAG", (enableFlag ? 1 : 0) + "");

            // 处理其他字段
            if (null != from) {
                sql.VALUES("LINE_FROM", Utils.addSqlStrAndReplace(from));
            }
            if (null != outport) {
                sql.VALUES("LINE_OUTPORT", Utils.addSqlStrAndReplace(outport));
            }
            if (null != inport) {
                sql.VALUES("LINE_INPORT", Utils.addSqlStrAndReplace(inport));
            }
            if (null != to) {
                sql.VALUES("LINE_TO", Utils.addSqlStrAndReplace(to));
            }
            if (null != pageId) {
                sql.VALUES("PAGE_ID", Utils.addSqlStrAndReplace(pageId));
            }
            if (null != process) {
                sql.VALUES("FK_FLOW_PROCESS_ID", Utils.addSqlStrAndReplace(process.getId()));
            }
            sqlStr = sql.toString() + ";";
        }
        return sqlStr;
    }

    /**
     * 添加 processPath
     *
     * @param processPathList
     * @return
     */
    public String addProcessPathList(Map<String, List<ProcessPath>> processPathList) {
        List<ProcessPath> processPaths = processPathList.get("processPathList");
        String sqlStr = "select 0";
        if (null != processPaths && processPaths.size() > 0) {
            StringBuffer sql = new StringBuffer();
            sql.append("INSERT INTO ");
            sql.append("FLOW_PROCESS_PATH ");
            sql.append("(");
            sql.append("ID,");
            sql.append("CRT_DTTM,");
            sql.append("CRT_USER,");
            sql.append("LAST_UPDATE_DTTM,");
            sql.append("LAST_UPDATE_USER,");
            sql.append("VERSION,");
            sql.append("ENABLE_FLAG,");
            sql.append("LINE_FROM,");
            sql.append("LINE_TO,");
            sql.append("LINE_OUTPORT,");
            sql.append("LINE_INPORT,");
            sql.append("PAGE_ID,");
            sql.append("FK_FLOW_PROCESS_ID");
            sql.append(")");
            sql.append("VALUES");
            // 放值时必须保证先后顺序
            int i = 0;
            for (ProcessPath processPath : processPaths) {
                i++;
                if (null != processPath) {
                    String id = processPath.getId();
                    String crtUser = processPath.getCrtUser();
                    Date crtDttm = processPath.getCrtDttm();
                    String lastUpdateUser = processPath.getLastUpdateUser();
                    Date lastUpdateDttm = processPath.getLastUpdateDttm();
                    Long version = processPath.getVersion();
                    Boolean enableFlag = processPath.getEnableFlag();
                    String from = processPath.getFrom();
                    String outport = processPath.getOutport();
                    String inport = processPath.getInport();
                    String to = processPath.getTo();
                    String pageId = processPath.getPageId();
                    Process process = processPath.getProcess();

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
                    sql.append("(");
                    sql.append(Utils.addSqlStrAndReplace(id) + ",");
                    sql.append(Utils.addSqlStrAndReplace(DateUtils.dateTimesToStr(crtDttm)) + ",");
                    sql.append(Utils.addSqlStrAndReplace(crtUser) + ",");
                    sql.append(Utils.addSqlStrAndReplace(DateUtils.dateTimesToStr(lastUpdateDttm)) + ",");
                    sql.append(Utils.addSqlStrAndReplace(lastUpdateUser) + ",");
                    sql.append(version + ",");
                    sql.append((enableFlag ? 1 : 0) + ",");
                    sql.append(Utils.addSqlStrAndReplace(from) + ",");
                    sql.append(Utils.addSqlStrAndReplace(to) + ",");
                    sql.append(Utils.addSqlStrAndReplace(outport) + ",");
                    sql.append(Utils.addSqlStrAndReplace(inport) + ",");
                    sql.append(Utils.addSqlStrAndReplace(pageId) + ",");
                    sql.append(Utils.addSqlStrAndReplace((process == null ? "" : process.getId())));
                    if (i != processPaths.size()) {
                        sql.append("),");
                    } else {
                        sql.append(")");
                    }
                }
            }

            sqlStr = sql.toString() + ";";
        }
        return sqlStr;
    }

    /**
     * 根据进程Id查询processPath
     *
     * @param processId
     * @return
     */
    public String getProcessPathByProcessId(String processId) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(processId)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("FLOW_PROCESS_PATH");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("FK_FLOW_PROCESS_ID = " + Utils.addSqlStr(processId));

            sqlStr = sql.toString() + ";";
        }
        return sqlStr;
    }

    /**
     * 根据pid和pageId查询
     *
     * @param processId
     * @param pageId
     * @return
     */
    public String getProcessPathByPageIdAndPid(String processId, String pageId) {
        String sqlStr = "select 0";
        if (!StringUtils.isAnyEmpty(processId, pageId)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("FLOW_PROCESS_PATH");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("FK_FLOW_PROCESS_ID = " + Utils.addSqlStr(processId));
            sql.WHERE("PAGE_ID = " + Utils.addSqlStr(pageId));

            sqlStr = sql.toString() + ";";
        }
        return sqlStr;
    }

    /**
     * 修改processPath
     *
     * @param processPath
     * @return
     */
    public String updateProcessPath(ProcessPath processPath) {
        String sqlStr = "select 0";
        if (null != processPath) {
            String id = processPath.getId();
            if (StringUtils.isNotBlank(id)) {
                String lastUpdateUser = processPath.getLastUpdateUser();
                Date lastUpdateDttm = processPath.getLastUpdateDttm();
                Long version = processPath.getVersion();
                Boolean enableFlag = processPath.getEnableFlag();
                String to = processPath.getTo();
                String from = processPath.getFrom();
                String outport = processPath.getOutport();
                String inport = processPath.getInport();
                Process process = processPath.getProcess();
                String pageId = processPath.getPageId();

                SQL sql = new SQL();
                sql.UPDATE("FLOW_PROCESS_PATH");

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
                sql.SET("LAST_UPDATE_DTTM = " + Utils.addSqlStr(lastUpdateDttmStr));
                sql.SET("LAST_UPDATE_USER = " + Utils.addSqlStr(lastUpdateUser));
                sql.SET("VERSION = " + (version + 1));

                // 处理其他字段
                if (null != enableFlag) {
                    sql.SET("ENABLE_FLAG = " + (enableFlag ? 1 : 0));
                }
                if (StringUtils.isNotBlank(to)) {
                    sql.SET("LINE_TO = " + Utils.addSqlStr(to));
                }
                if (StringUtils.isNotBlank(from)) {
                    sql.SET("LINE_FROM = " + Utils.addSqlStr(from));
                }
                if (StringUtils.isNotBlank(outport)) {
                    sql.SET("LINE_OUTPORT = " + Utils.addSqlStr(outport));
                }
                if (StringUtils.isNotBlank(inport)) {
                    sql.SET("LINE_INPORT = " + Utils.addSqlStr(inport));
                }
                if (StringUtils.isNotBlank(pageId)) {
                    sql.SET("PAGE_ID = " + Utils.addSqlStr(pageId));
                }
                if (null != process) {
                    sql.SET("FK_FLOW_PROCESS_ID = " + Utils.addSqlStr(process.getId()));
                }
                sql.WHERE("ENABLE_FLAG = 1");
                sql.WHERE("VERSION = " + version);
                sql.WHERE("ID = " + Utils.addSqlStr(id));
                sqlStr = sql.toString() + ";";
            }
        }
        return sqlStr;
    }

    public String updateEnableFlagByProcessId(String processId, String userName) {
        String sqlStr = "select 0";
        if (!StringUtils.isAnyEmpty(processId, userName)) {
            SQL sql = new SQL();
            sql.UPDATE("FLOW_PROCESS_PATH");
            sql.SET("LAST_UPDATE_DTTM = " + Utils.addSqlStr(DateUtils.dateTimesToStr(new Date())));
            sql.SET("LAST_UPDATE_USER = " + Utils.addSqlStr(userName));
            sql.SET("VERSION=(VERSION+1)");
            sql.SET("enable_flag = 0");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("FK_FLOW_PROCESS_ID = " + Utils.addSqlStr(processId));

            sqlStr = sql.toString() + ";";
        }
        return sqlStr;
    }
}
