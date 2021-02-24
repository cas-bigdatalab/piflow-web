package cn.cnic.component.flow.mapper.provider;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.flow.entity.Flow;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class FlowMapperProvider {

    private String id;
    private String crtUser;
    private String crtDttmStr;
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

    private void preventSQLInjectionFlow(Flow flow) {
        if (null != flow && StringUtils.isNotBlank(flow.getLastUpdateUser())) {
            // Mandatory Field
            String id = flow.getId();
            String crtUser = flow.getCrtUser();
            String lastUpdateUser = flow.getLastUpdateUser();
            Boolean enableFlag = flow.getEnableFlag();
            Long version = flow.getVersion();
            Date crtDttm = flow.getCrtDttm();
            Date lastUpdateDttm = flow.getLastUpdateDttm();
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
            this.description = SqlUtils.preventSQLInjection(flow.getDescription());
            this.name = SqlUtils.preventSQLInjection(flow.getName());
            this.uuid = SqlUtils.preventSQLInjection(flow.getUuid());
            this.driverMemory = SqlUtils.preventSQLInjection(flow.getDriverMemory());
            this.executorCores = SqlUtils.preventSQLInjection(flow.getExecutorCores());
            this.executorMemory = SqlUtils.preventSQLInjection(flow.getExecutorMemory());
            this.executorNumber = SqlUtils.preventSQLInjection(flow.getExecutorNumber());
            this.isExample = (null == flow.getIsExample() ? 0 : (flow.getIsExample() ? 1 : 0));
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
        this.description = null;
        this.name = null;
        this.uuid = null;
        this.driverMemory = null;
        this.executorCores = null;
        this.executorMemory = null;
        this.executorNumber = null;
        this.isExample = null;
    }

    /**
     * add Flow
     *
     * @param flow
     * @return
     */
    public String addFlow(Flow flow) {
        String sqlStr = "";
        this.preventSQLInjectionFlow(flow);
        if (null != flow) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.INSERT_INTO("flow");
            // The first string in the value is the field name corresponding to the table in the database.
            // all types except numeric fields must be enclosed in single quotes

            //Process the required fields firsts
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
            sql.VALUES("is_example", isExample + "");

            // handle other fields
            sql.VALUES("description", description);
            sql.VALUES("name", name);
            sql.VALUES("uuid", uuid);
            sql.VALUES("driver_memory", driverMemory);
            sql.VALUES("executor_cores", executorCores);
            sql.VALUES("executor_memory", executorMemory);
            sql.VALUES("executor_number", executorNumber);
            sqlStr = sql.toString();
        }
        this.reset();
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

}
