package cn.cnic.component.flow.mapper.provider;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.flow.entity.FlowGroupPaths;

public class FlowGroupPathsMapperProvider {

    private String id;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String from;
    private String inport;
    private String to;
    private String outport;
    private String pageId;
    private String filterCondition;
    private String flowGroupId;

    private boolean preventSQLInjectionFlowGroupPaths(FlowGroupPaths flowGroupPaths) {
        if (null == flowGroupPaths || StringUtils.isBlank(flowGroupPaths.getLastUpdateUser())) {
            return false;
        }

        // Mandatory Field
        this.id = SqlUtils.preventSQLInjection(flowGroupPaths.getId());
        Boolean enableFlag = flowGroupPaths.getEnableFlag();
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        Long version = flowGroupPaths.getVersion();
        this.version = (null != version ? version : 0L);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(flowGroupPaths.getLastUpdateUser());
        String lastUpdateDttmStr = StringUtils.isBlank(flowGroupPaths.getLastUpdateDttmString()) ? DateUtils.dateTimesToStr(new Date()) : flowGroupPaths.getLastUpdateDttmString();
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);
        

        // Selection field
        this.from = SqlUtils.preventSQLInjection(flowGroupPaths.getFrom());
        this.inport = SqlUtils.preventSQLInjection(flowGroupPaths.getInport());
        this.to = SqlUtils.preventSQLInjection(flowGroupPaths.getTo());
        this.outport = SqlUtils.preventSQLInjection(flowGroupPaths.getOutport());
        this.pageId = SqlUtils.preventSQLInjection(flowGroupPaths.getPageId());
        this.filterCondition = SqlUtils.preventSQLInjection(flowGroupPaths.getFilterCondition());
        String flowGroupId = null != flowGroupPaths.getFlowGroup() ? flowGroupPaths.getFlowGroup().getId() : null;
        this.flowGroupId = SqlUtils.preventSQLInjection(flowGroupId);
        return true;
    }

    private void resetFlowGroupPaths() {
        this.id = null;
        this.lastUpdateDttmStr = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.from = null;
        this.inport = null;
        this.to = null;
        this.outport = null;
        this.pageId = null;
        this.filterCondition = null;
        this.flowGroupId = null;
    }
    
    /**
     * add flowGroupPaths
     *
     * @param flowGroupPaths
     * @return
     */
    public String addFlowGroupPaths(FlowGroupPaths flowGroupPaths) {
        String sqlStr = "";
        boolean flag = this.preventSQLInjectionFlowGroupPaths(flowGroupPaths);
        if (flag) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("INSERT INTO flow_group_path ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldName() + ", ");
            stringBuffer.append("line_from, ");
            stringBuffer.append("line_inport, ");
            stringBuffer.append("line_to, ");
            stringBuffer.append("line_outport, ");
            stringBuffer.append("page_id, ");
            stringBuffer.append("filter_condition, ");
            stringBuffer.append("fk_flow_group_id ");
            stringBuffer.append(") ");
            stringBuffer.append("VALUES ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldValues(flowGroupPaths) + ", ");

            // handle other fields
            stringBuffer.append(this.from + ", ");
            stringBuffer.append(this.inport + ", ");
            stringBuffer.append(this.to + ", ");
            stringBuffer.append(this.outport + ", ");
            stringBuffer.append(this.pageId + ", ");
            stringBuffer.append(this.filterCondition + ", ");
            stringBuffer.append(this.flowGroupId + " ");
            stringBuffer.append(") ");
            sqlStr = stringBuffer.toString();
            this.resetFlowGroupPaths();
        }
        return sqlStr;
    }

    /**
     * update FlowGroupPaths
     *
     * @param flowGroupPaths
     * @return
     */
    public String updateFlowGroupPaths(FlowGroupPaths flowGroupPaths) {

        String sqlStr = "SELECT 0";
        this.preventSQLInjectionFlowGroupPaths(flowGroupPaths);
        if (null != flowGroupPaths) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("flow_group_path");
            // The first string in the SET is the name of the field corresponding to the
            // table in the database
            sql.SET("last_update_dttm = " + this.lastUpdateDttmStr);
            sql.SET("last_update_user = " + this.lastUpdateUser);
            sql.SET("version = " + (this.version + 1));

            // handle other fields
            sql.SET("enable_flag = " + this.enableFlag);
            sql.SET("line_from = " + this.from);
            sql.SET("line_inport = " + this.inport);
            sql.SET("line_to = " + this.to);
            sql.SET("line_outport = " + this.outport);
            sql.SET("page_id = " + this.pageId);
            sql.SET("filter_condition = " + this.filterCondition);
            sql.SET("fk_flow_group_id = " + this.flowGroupId);
            sql.WHERE("version = " + this.version);
            sql.WHERE("id = " + this.id);
            if (StringUtils.isNotBlank(this.id)) {
                sqlStr = sql.toString();
            }
        }
        this.resetFlowGroupPaths();
        return sqlStr;
    }
    
    /**
     * Query flowGroupPath by flowGroupId
     *
     * @param flowGroupId
     * @return
     */
    public String getFlowGroupPathsByFlowGroupId(String flowGroupId) {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_group_path");
        sql.WHERE("enable_flag = 1");
        sql.WHERE("fk_flow_group_id = " + SqlUtils.preventSQLInjection(flowGroupId));
        sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * Query connection information
     *
     * @param flowGroupId
     * @param pageId
     * @param from
     * @param to
     * @return
     */
    public String getFlowGroupPaths(String flowGroupId, String pageId, String from, String to) {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_group_path");
        sql.WHERE("enable_flag = 1");
        if (StringUtils.isNotBlank(flowGroupId)) {
            sql.WHERE("fk_flow_group_id = " + SqlUtils.preventSQLInjection(flowGroupId));
        }
        if (StringUtils.isNotBlank(pageId)) {
            sql.WHERE("page_id = " + SqlUtils.preventSQLInjection(pageId));
        }
        if (StringUtils.isNotBlank(from)) {
            sql.WHERE("line_from = " + SqlUtils.preventSQLInjection(from));
        }
        if (StringUtils.isNotBlank(to)) {
            sql.WHERE("line_to = " + SqlUtils.preventSQLInjection(to));
        }
        sqlStr = sql.toString();
        return sqlStr;
    }
}
