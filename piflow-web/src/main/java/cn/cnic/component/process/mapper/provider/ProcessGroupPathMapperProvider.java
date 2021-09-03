package cn.cnic.component.process.mapper.provider;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.process.entity.ProcessGroupPath;

public class ProcessGroupPathMapperProvider {

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
    private String processGroupId;

    private boolean preventSQLInjectionProcessGroupPath(ProcessGroupPath processGroupPath) {
        if (null == processGroupPath || StringUtils.isBlank(processGroupPath.getLastUpdateUser())) {
            return false;
        }

        // Mandatory Field
        String lastUpdateUser = processGroupPath.getLastUpdateUser();
        Boolean enableFlag = processGroupPath.getEnableFlag();
        Long version = processGroupPath.getVersion();
        Date lastUpdateDttm = processGroupPath.getLastUpdateDttm();
        this.id = SqlUtils.preventSQLInjection(processGroupPath.getId());
        this.lastUpdateUser = SqlUtils.preventSQLInjection(lastUpdateUser);
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

        // Selection field
        this.from = SqlUtils.preventSQLInjection(processGroupPath.getFrom());
        this.outport = SqlUtils.preventSQLInjection(processGroupPath.getOutport());
        this.inport = SqlUtils.preventSQLInjection(processGroupPath.getInport());
        this.to = SqlUtils.preventSQLInjection(processGroupPath.getTo());
        this.pageId = SqlUtils.preventSQLInjection(processGroupPath.getPageId());
        this.processGroupId = SqlUtils.preventSQLInjection(null != processGroupPath.getProcessGroup() ? processGroupPath.getProcessGroup().getId() : null);

        return true;
    }

    private void resetProcessGroupPath() {
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
        this.processGroupId = null;
    }

    /**
     * add processGroupPath
     *
     * @param processPath
     * @return
     */
    public String addProcessGroupPath(ProcessGroupPath processGroupPath) {

        String sqlStr = "SELECT 0";
        if (this.preventSQLInjectionProcessGroupPath(processGroupPath)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO flow_process_group_path ");
            strBuf.append("( ");
            strBuf.append(SqlUtils.baseFieldName() + ", ");
            strBuf.append("line_from, ");
            strBuf.append("line_outport, ");
            strBuf.append("line_inport, ");
            strBuf.append("line_to, ");
            strBuf.append("page_id, ");
            strBuf.append("fk_flow_process_group_id ");
            strBuf.append(") ");
            strBuf.append("VALUES ");
            strBuf.append("(");
            strBuf.append(SqlUtils.baseFieldValues(processGroupPath) + ", ");
            strBuf.append(this.from + ",");
            strBuf.append(this.outport + ",");
            strBuf.append(this.inport + ",");
            strBuf.append(this.to + ",");
            strBuf.append(this.pageId + ",");
            strBuf.append(this.processGroupId + " ");
            strBuf.append(")");
            strBuf.toString();
            sqlStr = strBuf.toString();
            this.resetProcessGroupPath();
        }
        return sqlStr;
    }

    /**
     * add processGroupPath List
     *
     * @param processPathList
     * @return
     */
    public String addProcessGroupPathList(Map<String, List<ProcessGroupPath>> processPathList) {
        List<ProcessGroupPath> processGroupPaths = processPathList.get("processGroupPathList");

        StringBuffer strBuf = new StringBuffer();
        strBuf.append("INSERT INTO flow_process_group_path ");
        strBuf.append("( ");
        strBuf.append(SqlUtils.baseFieldName() + ", ");
        strBuf.append("line_from, ");
        strBuf.append("line_outport, ");
        strBuf.append("line_inport, ");
        strBuf.append("line_to, ");
        strBuf.append("page_id, ");
        strBuf.append("fk_flow_process_group_id ");
        strBuf.append(") ");

        strBuf.append("values ");


        // The order must be guaranteed
        int i = 0;
        for (ProcessGroupPath processGroupPath : processGroupPaths) {
            i++;
            if (null == processGroupPath) {
                continue;
            }
            boolean flag = this.preventSQLInjectionProcessGroupPath(processGroupPath);
            if (!flag) {
                continue;
            }
            strBuf.append("(");
            strBuf.append(SqlUtils.baseFieldValues(processGroupPath) + ", ");
            strBuf.append(this.from + ",");
            strBuf.append(this.outport + ",");
            strBuf.append(this.inport + ",");
            strBuf.append(this.to + ",");
            strBuf.append(this.pageId + ",");
            strBuf.append(this.processGroupId + " ");
            if (i != processGroupPaths.size()) {
                strBuf.append("),");
            } else {
                strBuf.append(")");
            }
            this.resetProcessGroupPath();
        }
        return strBuf.toString();
    }

    /**
     * update updateProcessGroupPath
     *
     * @param processGroupPath
     * @return
     */
    public String updateProcessGroupPath(ProcessGroupPath processGroupPath) {
        String sqlStr = "SELECT 0";
        if (this.preventSQLInjectionProcessGroupPath(processGroupPath)) {
            SQL sql = new SQL();
            sql.UPDATE("flow_process_group_path");

            // Process the required fields first
            sql.SET("last_update_dttm = " + this.lastUpdateDttmStr);
            sql.SET("last_update_user = " + this.lastUpdateUser);
            sql.SET("version = " + (this.version + 1));

            // handle other fields
            sql.SET("enable_flag=" + this.enableFlag);

            sql.SET("line_from=" + this.from);
            sql.SET("line_outport=" + this.outport);
            sql.SET("line_inport=" + this.inport);
            sql.SET("line_to=" + this.to);
            sql.SET("page_id=" + this.pageId);
            sql.SET("fk_flow_process_group_id=" + this.processGroupId);

            sql.WHERE("version = " + this.version);
            sql.WHERE("id = " + this.id);
            if (StringUtils.isNotBlank(this.id)) {
                sqlStr = sql.toString();
            }
        }
        this.resetProcessGroupPath();
        return sqlStr;
    }

    /**
     * Query processGroupPath according to processGroup Id
     *
     * @param processGroupId
     * @return
     */
    public String getProcessPathByProcessGroupId(String processGroupId) {
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(processGroupId)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("flow_process_group_path");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_group_id = " + SqlUtils.preventSQLInjection(processGroupId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    public String updateEnableFlagByProcessGroupId(String processGroupId, String userName) {
        String sqlStr = "SELECT 0";
        if (!StringUtils.isAnyEmpty(processGroupId, userName)) {
            SQL sql = new SQL();
            sql.UPDATE("flow_process_group_path");
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
            sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(userName));
            sql.SET("version=(version+1)");
            sql.SET("enable_flag = 0");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_group_id = " + SqlUtils.preventSQLInjection(processGroupId));

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
            sql.FROM("flow_process_group_path");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_group_id = " + SqlUtils.preventSQLInjection(processGroupId));
            sql.WHERE("page_id = " + SqlUtils.preventSQLInjection(pageId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }
}
