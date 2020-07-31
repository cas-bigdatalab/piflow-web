package cn.cnic.component.process.mapper;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.process.entity.ProcessPath;
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
            this.crtUser = (null != crtUser ? SqlUtils.preventSQLInjection(crtUser) : null);
            this.lastUpdateUser = SqlUtils.preventSQLInjection(lastUpdateUser);
            this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
            this.version = (null != version ? version : 0L);
            String crtDttmStr = DateUtils.dateTimesToStr(crtDttm);
            String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
            this.crtDttmStr = (null != crtDttm ? SqlUtils.preventSQLInjection(crtDttmStr) : null);
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
     * add processPath
     *
     * @param processPath
     * @return
     */
    public String addProcessPath(ProcessPath processPath) {
        String sqlStr = "select 0";
        this.preventSQLInjectionProcessPath(processPath);
        if (null != processPath) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.INSERT_INTO("flow_process_path");
            // The first string in the value is the field name corresponding to the table in the database.
            // all types except numeric fields must be enclosed in single quotes

            //Process the required fields first
            if (null == crtDttmStr) {
                String crtDttm = DateUtils.dateTimesToStr(new Date());
                crtDttmStr = SqlUtils.preventSQLInjection(crtDttm);
            }
            if (StringUtils.isBlank(crtUser)) {
                crtUser = SqlUtils.preventSQLInjection("-1");
            }
            sql.VALUES("id", id);
            sql.VALUES("crt_dttm", crtDttmStr);
            sql.VALUES("crt_user", crtUser);
            sql.VALUES("last_update_dttm", lastUpdateDttmStr);
            sql.VALUES("last_update_user", lastUpdateUser);
            sql.VALUES("version", version + "");
            sql.VALUES("enable_flag", enableFlag + "");

            // handle other fields
            sql.VALUES("line_from", from);
            sql.VALUES("line_outport", outport);
            sql.VALUES("line_inport", inport);
            sql.VALUES("line_to", to);
            sql.VALUES("page_id", pageId);
            sql.VALUES("fk_flow_process_id", processId);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * add processPath
     *
     * @param processPathList
     * @return
     */
    public String addProcessPathList(Map<String, List<ProcessPath>> processPathList) {
        List<ProcessPath> processPaths = processPathList.get("processPathList");
        String sqlStr = "select 0";
        if (null != processPaths && processPaths.size() > 0) {
            StringBuffer sql = new StringBuffer();
            sql.append("insert into ");
            sql.append("flow_process_path ");
            sql.append("(");
            sql.append("id,");
            sql.append("crt_dttm,");
            sql.append("crt_user,");
            sql.append("last_update_dttm,");
            sql.append("last_update_user,");
            sql.append("version,");
            sql.append("enable_flag,");
            sql.append("line_from,");
            sql.append("line_to,");
            sql.append("line_outport,");
            sql.append("line_inport,");
            sql.append("page_id,");
            sql.append("fk_flow_process_id");
            sql.append(")");
            sql.append("values");
            // The order must be guaranteed
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
     * Query processPath according to process Id
     *
     * @param processId
     * @return
     */
    public String getProcessPathByProcessId(String processId) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(processId)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("flow_process_path");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_id = " + SqlUtils.preventSQLInjection(processId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * Query based on pid and pageId
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
            sql.FROM("flow_process_path");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_id = " + SqlUtils.preventSQLInjection(processId));
            sql.WHERE("page_id = " + SqlUtils.preventSQLInjection(pageId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * update processPath
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
                sql.UPDATE("flow_process_path");

                //Process the required fields first
                sql.SET("last_update_dttm = " + lastUpdateDttmStr);
                sql.SET("last_update_user = " + lastUpdateUser);
                sql.SET("version = " + (version + 1));

                // handle other fields
                sql.SET("enable_flag = " + enableFlag);
                sql.SET("line_to = " + to);
                sql.SET("line_from = " + from);
                sql.SET("line_outport = " + outport);
                sql.SET("line_inport = " + inport);
                sql.SET("page_id = " + pageId);
                if (null != processId) {
                    sql.SET("fk_flow_process_id = " + processId);
                }
                sql.WHERE("enable_flag = 1");
                sql.WHERE("version = " + version);
                sql.WHERE("id = " + id);
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
            sql.UPDATE("flow_process_path");
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
            sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(userName));
            sql.SET("version=(version+1)");
            sql.SET("enable_flag = 0");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_id = " + SqlUtils.preventSQLInjection(processId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * Query based on processGroupId and pageId
     *
     * @param processGroupId
     * @param pageId
     * @return
     */
    public String getProcessPathByPageIdAndProcessGroupId(String processGroupId, String pageId) {
        String sqlStr = "select 0";
        if (!StringUtils.isAnyEmpty(processGroupId, pageId)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("flow_process_path");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_group_id = " + SqlUtils.preventSQLInjection(processGroupId));
            sql.WHERE("page_id = " + SqlUtils.preventSQLInjection(pageId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }
}
