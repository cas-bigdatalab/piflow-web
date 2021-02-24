package cn.cnic.component.process.mapper.provider;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessGroup;
import cn.cnic.component.process.entity.ProcessGroupPath;
import cn.cnic.component.process.entity.ProcessPath;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProcessGroupPathMapperProvider {

    private String id;
    private Integer enableFlag;
    private Long version;
    private String lastUpdateDataTimeStr;
    private String lastUpdateUser;

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
        String lastUpdateDttm = DateUtils.dateTimesToStr(null != processGroupPath.getLastUpdateDttm() ? processGroupPath.getLastUpdateDttm() : new Date());
        this.id = SqlUtils.preventSQLInjection(processGroupPath.getId());
        this.version = (null != processGroupPath.getVersion() ? processGroupPath.getVersion() : 0L);
        this.enableFlag = ((null != processGroupPath.getEnableFlag() && processGroupPath.getEnableFlag()) ? 1 : 0);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(processGroupPath.getLastUpdateUser());
        this.lastUpdateDataTimeStr = SqlUtils.preventSQLInjection(lastUpdateDttm);

        // Selection field
        this.to = SqlUtils.preventSQLInjection(processGroupPath.getTo());
        this.from = SqlUtils.preventSQLInjection(processGroupPath.getFrom());
        this.inport = SqlUtils.preventSQLInjection(processGroupPath.getInport());
        this.outport = SqlUtils.preventSQLInjection(processGroupPath.getOutport());
        this.pageId = SqlUtils.preventSQLInjection(processGroupPath.getPageId());
        this.processGroupId = SqlUtils.preventSQLInjection(null != processGroupPath.getProcessGroup() ? processGroupPath.getProcessGroup().getId() : null);

        return true;
    }

    private void resetProcessGroupPath() {
        this.id = null;
        this.lastUpdateUser = null;
        this.lastUpdateDataTimeStr = null;
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
     * @param processPathList
     * @return
     */
    public String addProcessGroupPathList(Map<String, List<ProcessGroupPath>> processPathList) {
        List<ProcessGroupPath> processGroupPaths = processPathList.get("processGroupPathList");

        StringBuffer strBuf = new StringBuffer();
        strBuf.append("INSERT INTO group_schedule ");
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
        return "select 0";
    }

    /**
     * Query processGroupPath according to processGroup Id
     *
     * @param processGroupId
     * @return
     */
    public String getProcessPathByProcessGroupId(String processGroupId) {
        String sqlStr = "select 0";
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
        String sqlStr = "select 0";
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
        String sqlStr = "select 0";
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
