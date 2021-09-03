package cn.cnic.component.flow.mapper.provider;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.flow.entity.FlowGroup;

public class FlowGroupMapperProvider {

    private String id;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String name;
    private String description;
    private String pageId;
    private Integer isExample;
    private String flowGroupId;

    private boolean preventSQLInjectionFlowGroup(FlowGroup flowGroup) {
        if (null == flowGroup || StringUtils.isBlank(flowGroup.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        Boolean enableFlag = flowGroup.getEnableFlag();
        Long version = flowGroup.getVersion();
        this.id = SqlUtils.preventSQLInjection(flowGroup.getId());
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(flowGroup.getLastUpdateUser());
        String lastUpdateDttmStr = StringUtils.isBlank(flowGroup.getLastUpdateDttmString()) ? DateUtils.dateTimesToStr(new Date()) : flowGroup.getLastUpdateDttmString();
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

        // Selection field
        this.name = SqlUtils.preventSQLInjection(flowGroup.getName());
        this.description = SqlUtils.preventSQLInjection(flowGroup.getDescription());
        this.pageId = SqlUtils.preventSQLInjection(flowGroup.getPageId());
        this.isExample = (null == flowGroup.getIsExample() ? 0 : (flowGroup.getIsExample() ? 1 : 0));
        String flowGroupId = null != flowGroup.getFlowGroup() ? flowGroup.getFlowGroup().getId() : null;
        this.flowGroupId = SqlUtils.preventSQLInjection(flowGroupId);
        return true;
    }

    private void resetFlowGroup() {
        this.id = null;
        this.lastUpdateDttmStr = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.name = null;
        this.description = null;
        this.pageId = null;
        this.isExample = null;
        this.flowGroupId = null;
    }

    /**
     * add FlowGroup
     *
     * @param flowGroup
     * @return
     */
    public String addFlowGroup(FlowGroup flowGroup) {
        String sqlStr = "";
        boolean flag = this.preventSQLInjectionFlowGroup(flowGroup);
        if (flag) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("INSERT INTO flow_group ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldName() + ", ");
            stringBuffer.append("name, ");
            stringBuffer.append("description, ");
            stringBuffer.append("page_id, ");
            stringBuffer.append("is_example, ");
            stringBuffer.append("fk_flow_group_id ");
            stringBuffer.append(") ");
            stringBuffer.append("VALUES ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldValues(flowGroup) + ", ");
            // handle other fields
            stringBuffer.append(this.name + ", ");
            stringBuffer.append(this.description + ", ");
            stringBuffer.append(this.pageId + ", ");
            stringBuffer.append(this.isExample + ", ");
            stringBuffer.append(this.flowGroupId + " ");
            stringBuffer.append(") ");
            sqlStr = stringBuffer.toString();
            this.resetFlowGroup();
        }
        return sqlStr;
    }

    /**
     * update FlowGroup
     *
     * @param flowGroup
     * @return
     */
    public String updateFlowGroup(FlowGroup flowGroup) {

        String sqlStr = "SELECT 0";
        this.preventSQLInjectionFlowGroup(flowGroup);
        if (null != flowGroup) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("flow_group");
            // The first string in the SET is the name of the field corresponding to the
            // table in the database
            sql.SET("last_update_dttm = " + this.lastUpdateDttmStr);
            sql.SET("last_update_user = " + this.lastUpdateUser);
            sql.SET("version = " + (this.version + 1));

            // handle other fields
            sql.SET("enable_flag = " + this.enableFlag);
            sql.SET("name = " + this.name);
            sql.SET("description = " + this.description);
            sql.SET("page_id = " + this.pageId);
            sql.SET("is_example = " + this.isExample);
            //sql.SET("fk_flow_group_id = " + this.flowGroupId);
            sql.WHERE("version = " + this.version);
            sql.WHERE("id = " + this.id);
            if (StringUtils.isNotBlank(this.id)) {
                sqlStr = sql.toString();
            }
        }
        this.resetFlowGroup();
        return sqlStr;
    }
    
    public String updateEnableFlagById(String username, String id, boolean enableFlag) {
        if (StringUtils.isBlank(id) || StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        String lastUpdateDttm = DateUtils.dateTimesToStr(new Date());
        int enableFlagInt = enableFlag ? 1 : 0;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("UPDATE flow_group c SET ");
        stringBuffer.append("c.enable_flag=" + enableFlagInt + ",");
        stringBuffer.append("c.last_update_dttm=" + SqlUtils.preventSQLInjection(lastUpdateDttm) + ",");
        stringBuffer.append("c.last_update_user=" + SqlUtils.preventSQLInjection(username) + " ");
        stringBuffer.append("where c.id=" + SqlUtils.preventSQLInjection(id));
        return stringBuffer.toString();
        
    }

    /**
     * Query all flow paging queries
     *
     * @param param
     * @return
     */
    public String getFlowGroupListParam(String username, boolean isAdmin, String param) {
        String sqlStr = "SELECT 0";
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("SELECT * ");
        strBuf.append("FROM flow_group ");
        strBuf.append("WHERE ");
        strBuf.append("enable_flag=1 ");
        strBuf.append("AND is_example<>1 ");
        strBuf.append("AND fk_flow_group_id IS NULL ");
        if (!isAdmin) {
            strBuf.append("AND crt_user=" + SqlUtils.preventSQLInjection(username) + " ");
        }
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("AND ( ");
            strBuf.append("name LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            strBuf.append("OR description LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            strBuf.append(") ");
        }
        strBuf.append("ORDER BY crt_dttm DESC ");
        sqlStr = strBuf.toString();
        return sqlStr;
    }

    /**
     * Query flowGroup by ID
     *
     * @param id
     * @return
     */
    public String getFlowGroupById(String id) {
        String sqlStr = "";
        if (StringUtils.isNotBlank(id)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from flow_group ");
            strBuf.append("where enable_flag = 1 ");
            strBuf.append("and id = " + SqlUtils.preventSQLInjection(id) + " ");
            sqlStr = strBuf.toString();
        }
        return sqlStr;
    }

    /**
     * Query flowGroup by fkFlowGroupId
     *
     * @param fkFlowGroupId
     * @return
     */
    public String getFlowGroupListByFkGroupId(String fkFlowGroupId) {
        String sqlStr = "";
        if (StringUtils.isNotBlank(fkFlowGroupId)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from flow_group ");
            strBuf.append("where enable_flag = 1 ");
            strBuf.append("and fk_flow_group_id = " + SqlUtils.preventSQLInjection(fkFlowGroupId) + " ");
            sqlStr = strBuf.toString();
        }
        return sqlStr;
    }

    public String getFlowGroupNameListById(String fId, String id) {
        String sqlStr = "";
        if (StringUtils.isNotBlank(id)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select Name ");
            strBuf.append("from flow_group ");
            strBuf.append("where enable_flag = 1 ");
            strBuf.append("and fk_flow_group_id = " + SqlUtils.preventSQLInjection(fId) + " and id != " + SqlUtils.preventSQLInjection(id) + " ");
            sqlStr = strBuf.toString();
        }
        return sqlStr;
    }

}
