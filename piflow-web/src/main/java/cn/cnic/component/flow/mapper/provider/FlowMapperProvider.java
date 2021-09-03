package cn.cnic.component.flow.mapper.provider;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.flow.entity.Flow;

public class FlowMapperProvider {

    private String id;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String description;
    private String name;
    private String uuid;
    private String driverMemory;
    private String executorCores;
    private String executorMemory;
    private String executorNumber;
    private Integer isExample;
    private String pageId;
    private String flowGroupId;

    private boolean preventSQLInjectionFlow(Flow flow) {
        if (null == flow || StringUtils.isBlank(flow.getLastUpdateUser())) {
            return false;
        }

        // Mandatory Field
        Boolean enableFlag = flow.getEnableFlag();
        Long version = flow.getVersion();
        this.id = SqlUtils.preventSQLInjection(flow.getId());
        this.lastUpdateUser = SqlUtils.preventSQLInjection(flow.getLastUpdateUser());
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        String lastUpdateDttmStr = StringUtils.isBlank(flow.getLastUpdateDttmString()) ? DateUtils.dateTimesToStr(new Date()) : flow.getLastUpdateDttmString();
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

        // Selection field
        this.description = SqlUtils.preventSQLInjection(flow.getDescription());
        this.name = SqlUtils.preventSQLInjection(flow.getName());
        this.uuid = SqlUtils.preventSQLInjection(flow.getUuid());
        this.driverMemory = SqlUtils.preventSQLInjection(flow.getDriverMemory());
        this.executorCores = SqlUtils.preventSQLInjection(flow.getExecutorCores());
        this.executorMemory = SqlUtils.preventSQLInjection(flow.getExecutorMemory());
        this.executorNumber = SqlUtils.preventSQLInjection(flow.getExecutorNumber());
        this.isExample = (null == flow.getIsExample() ? 0 : (flow.getIsExample() ? 1 : 0));
        this.pageId = SqlUtils.preventSQLInjection(flow.getPageId());
        String flowGroupId_str = (null != flow.getFlowGroup() && StringUtils.isNotBlank(flow.getFlowGroup().getId()))? flow.getFlowGroup().getId() : null;
        this.flowGroupId = SqlUtils.preventSQLInjection(flowGroupId_str);
        return true;
    }

    private void reset() {
        this.id = null;
        this.lastUpdateDttmStr = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.description = null;
        this.name = null;
        this.uuid = null;
        this.driverMemory = null;
        this.executorCores = null;
        this.executorMemory = null;
        this.executorNumber = null;
        this.isExample = null;
        this.pageId = null;
        this.flowGroupId = null;
    }

    /**
     * add Flow
     *
     * @param flow
     * @return
     */
    public String addFlow(Flow flow) {
        String sqlStr = "";
        boolean flag = this.preventSQLInjectionFlow(flow);
        if (flag) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("INSERT INTO flow ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldName() + ", ");
            stringBuffer.append("description, ");
            stringBuffer.append("name, ");
            stringBuffer.append("uuid, ");
            stringBuffer.append("driver_memory, ");
            stringBuffer.append("executor_cores, ");
            stringBuffer.append("executor_memory, ");
            stringBuffer.append("executor_number, ");
            stringBuffer.append("is_example, ");
            stringBuffer.append("page_id, ");
            stringBuffer.append("fk_flow_group_id ");
            stringBuffer.append(") ");
            stringBuffer.append("VALUES ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldValues(flow) + ", ");
            // handle other fields
            stringBuffer.append(this.description + ", ");
            stringBuffer.append(this.name + ", ");
            stringBuffer.append(this.uuid + ", ");
            stringBuffer.append(this.driverMemory + ", ");
            stringBuffer.append(this.executorCores + ", ");
            stringBuffer.append(this.executorMemory + ", ");
            stringBuffer.append(this.executorNumber + ", ");
            stringBuffer.append(this.isExample + ", ");
            stringBuffer.append(this.pageId + ", ");
            stringBuffer.append(this.flowGroupId + " ");
            stringBuffer.append(") ");
            sqlStr = stringBuffer.toString();
            this.reset();
        }
        return sqlStr;
    }

    /**
     * update Flow
     *
     * @param flow
     * @return
     */
    public String updateFlow(Flow flow) {

        String sqlStr = "";
        this.preventSQLInjectionFlow(flow);
        if (null != flow) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("flow");
            // The first string in the SET is the name of the field corresponding to the table in the database
            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag = " + enableFlag);
            sql.SET("description = " + description);
            sql.SET("name = " + name);
            sql.SET("uuid = " + uuid);
            sql.SET("driver_memory = " + driverMemory);
            sql.SET("executor_cores = " + executorCores);
            sql.SET("executor_memory = " + executorMemory);
            sql.SET("executor_number = " + executorNumber);
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
     * get flow list
     *
     * @return
     */
    public String getFlowList() {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow");
        sql.WHERE("enable_flag = 1");
        sql.WHERE("is_example = 0");
        sql.WHERE("fk_flow_group_id = null ");
        sql.ORDER_BY(" crt_dttm desc  ");
        sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * Query all flow paging queries
     *
     * @param param
     * @return
     */
    public String getFlowListParam(String username, boolean isAdmin, String param) {
        String sqlStr = "SELECT 0";
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("SELECT * ");
        strBuf.append("FROM flow ");
        strBuf.append("WHERE ");
        strBuf.append("enable_flag = 1 ");
        strBuf.append("AND is_example = 0 ");
        strBuf.append("AND fk_flow_group_id IS NULL ");
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("AND ( ");
            strBuf.append("name LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            strBuf.append("OR description LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            strBuf.append(") ");
        }
        if (!isAdmin) {
            strBuf.append("AND crt_user = " + SqlUtils.preventSQLInjection(username));
        }
        strBuf.append("ORDER BY crt_dttm DESC ");
        sqlStr = strBuf.toString();
        return sqlStr;
    }

    /**
     * Query the sample flow list
     *
     * @return
     */
    public String getFlowExampleList() {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("id,name");
        sql.FROM("flow");
        sql.WHERE("enable_flag = 1");
        sql.WHERE("is_example = 1");
        sql.ORDER_BY(" name asc  ");
        sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * get flow by id
     *
     * @param id
     * @return
     */
    public String getFlowById(String id) {
        String sqlStr = "";
        if (StringUtils.isNotBlank(id)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from flow ");
            strBuf.append("where enable_flag = 1 ");
            strBuf.append("and id = " + SqlUtils.preventSQLInjection(id) + " ");
            sqlStr = strBuf.toString();
        }
        return sqlStr;
    }

    /**
     * get flow by pageId
     *
     * @param fid
     * @param pageId
     * @return
     */
    public String getFlowByPageId(String fid, String pageId) {
        String sqlStr = "";
        if (StringUtils.isBlank(fid) || StringUtils.isBlank(pageId)) {
            return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from flow ");
        strBuf.append("where enable_flag=1 ");
        strBuf.append("and fk_flow_group_id= " + SqlUtils.preventSQLInjection(fid) + " ");
        strBuf.append("and page_id= " + SqlUtils.preventSQLInjection(pageId) + " ");
        sqlStr = strBuf.toString();
        return sqlStr;
    }

    /**
     * Delete according to id logic, set to invalid
     *
     * @param id
     * @return
     */
    public String updateEnableFlagById(String username, String id) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }
        SQL sql = new SQL();
        sql.UPDATE("flow");
        sql.SET("enable_flag = 0");
        sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
        sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
        sql.WHERE("enable_flag = 1");
        sql.WHERE("is_example = 0");
        sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));

        return sql.toString();
    }

    public String getFlowListGroupId(String flowGroupId) {
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(flowGroupId)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from flow ");
            strBuf.append("where enable_flag = 1 ");
            strBuf.append("and fk_flow_group_id = " + SqlUtils.preventSQLInjection(flowGroupId) + " ");
            sqlStr = strBuf.toString();
        }
        return sqlStr;
    }
    
    public String getGlobalParamsIdsByFlowId(String flowId) {
        if (StringUtils.isBlank(flowId)) {
        	return "SELECT 0";
        }
        String sqlStr = ("SELECT global_params_id FROM `association_global_params_flow` WHERE flow_id= " + SqlUtils.preventSQLInjection(flowId));
        return sqlStr;
    }
    
    public String linkGlobalParams(String flowId, String[] globalParamsIds) {
        if (StringUtils.isBlank(flowId) || globalParamsIds.length <= 0) {
        	return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("INSERT INTO `association_global_params_flow` ");
        strBuf.append("( ");
        strBuf.append("`flow_id`, ");
        strBuf.append("`global_params_id` ");
        strBuf.append(") ");
        strBuf.append("values ");
        for (int i = 0; i < globalParamsIds.length; i++) {
        	strBuf.append("( ");
        	strBuf.append(SqlUtils.preventSQLInjection(flowId) + ", ");
        	strBuf.append(SqlUtils.preventSQLInjection(globalParamsIds[i]));
        	strBuf.append(") ");
        	if((i+1) < globalParamsIds.length) {
        		strBuf.append(", ");	
        	}
		}
        String sqlStr = strBuf.toString();
        return sqlStr;
    }
    
    public String unlinkGlobalParams(String flowId, String[] globalParamsIds) {
        if (StringUtils.isBlank(flowId) || globalParamsIds.length <= 0) {
        	return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("DELETE FROM `association_global_params_flow` ");
        strBuf.append("WHERE ");
        strBuf.append(" `flow_id`= " + SqlUtils.preventSQLInjection(flowId));
        strBuf.append(" AND ");
        strBuf.append(" `global_params_id` in ");
        strBuf.append("( ");
        for (int i = 0; i < globalParamsIds.length; i++) {
        	strBuf.append(SqlUtils.preventSQLInjection(globalParamsIds[i]));
        	if((i+1) < globalParamsIds.length) {
        		strBuf.append(", ");	
        	}
		}
    	strBuf.append(") ");
        String sqlStr = strBuf.toString();
        return sqlStr;
    }

}
