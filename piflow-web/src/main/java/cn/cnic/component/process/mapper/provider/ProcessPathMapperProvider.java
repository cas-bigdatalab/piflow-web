package cn.cnic.component.process.mapper.provider;

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

    private boolean preventSQLInjectionProcessPath(ProcessPath processPath) {
        if (null == processPath || StringUtils.isBlank(processPath.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        String lastUpdateUser = processPath.getLastUpdateUser();
        Boolean enableFlag = processPath.getEnableFlag();
        Long version = processPath.getVersion();
        Date lastUpdateDttm = processPath.getLastUpdateDttm();
        this.id = SqlUtils.preventSQLInjection(processPath.getId());
        this.lastUpdateUser = SqlUtils.preventSQLInjection(lastUpdateUser);
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

        // Selection field
        this.from = SqlUtils.preventSQLInjection(processPath.getFrom());
        this.outport = SqlUtils.preventSQLInjection(processPath.getOutport());
        this.inport = SqlUtils.preventSQLInjection(processPath.getInport());
        this.to = SqlUtils.preventSQLInjection(processPath.getTo());
        this.pageId = SqlUtils.preventSQLInjection(processPath.getPageId());
        String processIdStr = (null != processPath.getProcess() ? processPath.getProcess().getId() : null);
        this.processId = (null != processIdStr ? SqlUtils.preventSQLInjection(processIdStr) : null);
        return true;
    }

    private void reset() {
        this.id = null;
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
        String sqlStr = "SELECT 0";
        if (this.preventSQLInjectionProcessPath(processPath)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO ");
            strBuf.append("flow_process_path ");
            strBuf.append("(");
            // Mandatory Field
            strBuf.append(SqlUtils.baseFieldName() + ", ");
            strBuf.append("line_from, ");
            strBuf.append("line_outport, ");
            strBuf.append("line_to, ");
            strBuf.append("line_inport, ");
            strBuf.append("page_id, ");
            strBuf.append("fk_flow_process_id ");
            strBuf.append(") ");

            strBuf.append("VALUES ");
            strBuf.append("(");
            // Selection field
            strBuf.append(SqlUtils.baseFieldValues(processPath) + ", ");
            // handle other fields
            strBuf.append(from + ", ");
            strBuf.append(outport + ", ");
            strBuf.append(to + ", ");
            strBuf.append(inport + ", ");
            strBuf.append(pageId + ", ");
            strBuf.append(processId + " ");
            strBuf.append(")");
            this.reset();
            return strBuf.toString() + ";";
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
        if (null == processPaths || processPaths.size() <= 0) {
            return "SELECT 0";
        }
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ");
        sql.append("flow_process_path ");
        sql.append("(");
        sql.append(SqlUtils.baseFieldName() + ", ");
        sql.append("line_from,line_to,line_outport,line_inport,page_id,fk_flow_process_id");
        sql.append(")");
        sql.append("VALUES ");
        // The order must be guaranteed
        int i = 0;
        for (ProcessPath processPath : processPaths) {
            i++;
            if (this.preventSQLInjectionProcessPath(processPath)) {
                sql.append("(");
                sql.append(SqlUtils.baseFieldValues(processPath) + ",");
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
        String sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * Query processPath according to process Id
     *
     * @param processId
     * @return
     */
    public String getProcessPathByProcessId(String processId) {
        String sqlStr = "SELECT 0";
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
        String sqlStr = "SELECT 0";
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
        String sqlStr = "SELECT 0";
        if (this.preventSQLInjectionProcessPath(processPath)) {
            if (StringUtils.isBlank(id)) {
                return "SELECT 0";
            }
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
        this.reset();
        return sqlStr;
    }

    public String updateEnableFlagByProcessId(String processId, String userName) {
        String sqlStr = "SELECT 0";
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
        String sqlStr = "SELECT 0";
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
