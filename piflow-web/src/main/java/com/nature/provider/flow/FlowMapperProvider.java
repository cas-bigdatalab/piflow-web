package com.nature.provider.flow;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.Flow;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.Map;

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
     * 新增Flow
     *
     * @param flow
     * @return
     */
    public String addFlow(Flow flow) {
        String sqlStr = "";
        this.preventSQLInjectionFlow(flow);
        if (null != flow) {
            SQL sql = new SQL();

            // INSERT_INTO括号中为数据库表名
            sql.INSERT_INTO("flow");
            // value中的第一个字符串为数据库中表对应的字段名
            // 除数字类型的字段外其他类型必须加单引号

            //先处理修改必填字段s
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
            sql.VALUES("IS_EXAMPLE", isExample + "");

            // 处理其他字段
            sql.VALUES("description", description);
            sql.VALUES("NAME", name);
            sql.VALUES("UUID", uuid);
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
     * 修改Flow
     *
     * @param flow
     * @return
     */
    public String updateFlow(Flow flow) {

        String sqlStr = "";
        this.preventSQLInjectionFlow(flow);
        if (null != flow) {
            SQL sql = new SQL();

            // INSERT_INTO括号中为数据库表名
            sql.UPDATE("flow");
            // SET中的第一个字符串为数据库中表对应的字段名
            sql.SET("LAST_UPDATE_DTTM = " + lastUpdateDttmStr);
            sql.SET("LAST_UPDATE_USER = " + lastUpdateUser);
            sql.SET("VERSION = " + (version + 1));

            // 处理其他字段
            sql.SET("ENABLE_FLAG = " + enableFlag);
            sql.SET("description = " + description);
            sql.SET("NAME = " + name);
            sql.SET("UUID = " + uuid);
            sql.SET("driver_memory = " + driverMemory);
            sql.SET("executor_cores = " + executorCores);
            sql.SET("executor_memory = " + executorMemory);
            sql.SET("executor_number = " + executorNumber);
            sql.WHERE("VERSION = " + version);
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
     * 查詢所有工作流
     *
     * @return
     */
    public String getFlowList() {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow");
        sql.WHERE("ENABLE_FLAG = 1");
        sql.WHERE("IS_EXAMPLE = 0");
        sql.ORDER_BY(" CRT_DTTM DESC  ");
        sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * 查詢所有工作流分页查询
     *
     * @param param
     * @return
     */
    public String getFlowListParam(String param) {
        String sqlStr = "SELECT 0";
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("SELECT * ");
        strBuf.append("FROM FLOW ");
        strBuf.append("WHERE ");
        strBuf.append("ENABLE_FLAG = 1 ");
        strBuf.append("AND IS_EXAMPLE = 0 ");
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("AND ( ");
            strBuf.append("NAME LIKE '%" + param + "%' ");
            strBuf.append("OR DESCRIPTION LIKE '%" + param + "%' ");
            strBuf.append(") ");
        }
        strBuf.append(SqlUtils.addQueryByUserRole(currentUser, true));
        strBuf.append("ORDER BY CRT_DTTM DESC ");
        sqlStr = strBuf.toString();
        return sqlStr;
    }

    /**
     * 查詢所有样例工作流
     *
     * @return
     */
    public String getFlowExampleList() {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("ID,NAME");
        sql.FROM("FLOW");
        sql.WHERE("ENABLE_FLAG = 1");
        sql.WHERE("IS_EXAMPLE = 1");
        sql.ORDER_BY(" NAME ASC  ");
        sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * 根据工作流Id查询工作流
     *
     * @param id
     * @return
     */
    public String getFlowById(String id) {
        String sqlStr = "";
        if (StringUtils.isNotBlank(id)) {
            UserVo currentUser = SessionUserUtil.getCurrentUser();
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("SELECT * ");
            strBuf.append("FROM FLOW ");
            strBuf.append("WHERE ENABLE_FLAG = 1 ");
            strBuf.append("AND id = " + SqlUtils.preventSQLInjection(id) + " ");
            //strBuf.append(SqlUtils.addQueryByUserRole(currentUser, true));
            sqlStr = strBuf.toString();
        }
        return sqlStr;
    }

    /**
     * 根据id逻辑删除,设为无效
     *
     * @param id
     * @return
     */
    public String updateEnableFlagById(String id) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(id)) {
            SQL sql = new SQL();
            sql.UPDATE("FLOW");
            sql.SET("ENABLE_FLAG = 0");
            sql.SET("LAST_UPDATE_USER = " + SqlUtils.preventSQLInjection(username));
            sql.SET("LAST_UPDATE_DTTM = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
            sql.WHERE("ENABLE_FLAG = 1");
            sql.WHERE("IS_EXAMPLE = 0");
            sql.WHERE("ID = " + SqlUtils.preventSQLInjection(id));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

}
