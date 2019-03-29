package com.nature.provider.process;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SqlUtils;
import com.nature.component.process.model.Process;
import com.nature.component.process.model.ProcessPath;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProcessPathMapperProvider {

    private String id;
    private String crtUser;
    private String crtDttmStr;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String from;
    private String outport;
    private String inport;
    private String to;
    private String pageId;
    private String processId;

    private void preventSQLInjectionProcessPath(ProcessPath processPath) {
        if (null != processPath && StringUtils.isNotBlank(processPath.getLastUpdateUser())) {
            // Mandatory Field
            String id = processPath.getId();
            String crtUser = processPath.getCrtUser();
            String lastUpdateUser = processPath.getLastUpdateUser();
            Boolean enableFlag = processPath.getEnableFlag();
            Long version = processPath.getVersion();
            Date crtDttm = processPath.getCrtDttm();
            Date lastUpdateDttm = processPath.getLastUpdateDttm();
            this.id = SqlUtils.preventSQLInjection(id);
            this.crtUser = (null != crtUser ? SqlUtils.preventSQLInjection(crtUser):null);
            this.lastUpdateUser = SqlUtils.preventSQLInjection(lastUpdateUser);
            this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
            this.version = (null != version ? version : 0L);
            String crtDttmStr = DateUtils.dateTimesToStr(crtDttm);
            String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
            this.crtDttmStr = (null != crtDttm ? SqlUtils.preventSQLInjection(crtDttmStr):null);
            this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

            // Selection field
            this.from = SqlUtils.preventSQLInjection(processPath.getFrom());
            this.outport = SqlUtils.preventSQLInjection(processPath.getOutport());
            this.inport = SqlUtils.preventSQLInjection(processPath.getInport());
            this.to = SqlUtils.preventSQLInjection(processPath.getTo());
            this.pageId = SqlUtils.preventSQLInjection(processPath.getPageId());
            String processIdStr = (null != processPath.getProcess() ? processPath.getProcess().getId() : null);
            this.processId = (null != processIdStr ? SqlUtils.preventSQLInjection(processIdStr) : null);
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
        this.from = null;
        this.outport = null;
        this.inport = null;
        this.to = null;
        this.pageId = null;
        this.processId = null;
    }

    /**
     * 添加 processPath
     *
     * @param processPath
     * @return
     */
    public String addProcessPath(ProcessPath processPath) {
        String sqlStr = "select 0";
        this.preventSQLInjectionProcessPath(processPath);
        if (null != processPath) {
            SQL sql = new SQL();

            // INSERT_INTO括号中为数据库表名
            sql.INSERT_INTO("FLOW_PROCESS_PATH");
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
            sql.VALUES("LINE_FROM", from);
            sql.VALUES("LINE_OUTPORT", outport);
            sql.VALUES("LINE_INPORT", inport);
            sql.VALUES("LINE_TO", to);
            sql.VALUES("PAGE_ID", pageId);
            sql.VALUES("FK_FLOW_PROCESS_ID", processId);
            sqlStr = sql.toString();
        }
        this.reset();
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
                    this.preventSQLInjectionProcessPath(processPath);
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
                    sql.append(from + ",");
                    sql.append(to + ",");
                    sql.append(outport + ",");
                    sql.append(inport + ",");
                    sql.append(pageId + ",");
                    sql.append(processId);
                    if (i != processPaths.size()) {
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
            sql.WHERE("FK_FLOW_PROCESS_ID = " + SqlUtils.preventSQLInjection(processId));

            sqlStr = sql.toString();
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
            sql.WHERE("FK_FLOW_PROCESS_ID = " + SqlUtils.preventSQLInjection(processId));
            sql.WHERE("PAGE_ID = " + SqlUtils.preventSQLInjection(pageId));

            sqlStr = sql.toString();
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
        this.preventSQLInjectionProcessPath(processPath);
        if (null != processPath) {
            if (StringUtils.isNotBlank(id)) {

                SQL sql = new SQL();
                sql.UPDATE("FLOW_PROCESS_PATH");

                //先处理修改必填字段
                sql.SET("LAST_UPDATE_DTTM = " + lastUpdateDttmStr);
                sql.SET("LAST_UPDATE_USER = " + lastUpdateUser);
                sql.SET("VERSION = " + (version + 1));

                // 处理其他字段
                sql.SET("ENABLE_FLAG = " + enableFlag);
                sql.SET("LINE_TO = " + to);
                sql.SET("LINE_FROM = " + from);
                sql.SET("LINE_OUTPORT = " + outport);
                sql.SET("LINE_INPORT = " + inport);
                sql.SET("PAGE_ID = " + pageId);
                if (null != processId) {
                    sql.SET("FK_FLOW_PROCESS_ID = " + processId);
                }
                sql.WHERE("ENABLE_FLAG = 1");
                sql.WHERE("VERSION = " + version);
                sql.WHERE("ID = " + id);
                sqlStr = sql.toString();
            }
        }
        this.reset();
        return sqlStr;
    }

    public String updateEnableFlagByProcessId(String processId, String userName) {
        String sqlStr = "select 0";
        if (!StringUtils.isAnyEmpty(processId, userName)) {
            SQL sql = new SQL();
            sql.UPDATE("FLOW_PROCESS_PATH");
            sql.SET("LAST_UPDATE_DTTM = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
            sql.SET("LAST_UPDATE_USER = " + SqlUtils.preventSQLInjection(userName));
            sql.SET("VERSION=(VERSION+1)");
            sql.SET("enable_flag = 0");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("FK_FLOW_PROCESS_ID = " + SqlUtils.preventSQLInjection(processId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }
}
