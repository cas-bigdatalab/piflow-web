package com.nature.provider;

import com.nature.base.config.vo.UserVo;
import com.nature.base.util.DateUtils;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.Utils;
import com.nature.component.workFlow.model.Flow;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class FlowMapperProvider {

    /**
     * 新增Flow
     *
     * @param flow
     * @return
     */
    public String addFlow(Flow flow) {
        String sqlStr = "";
        if (null != flow) {
            String id = flow.getId();
            String crtUser = flow.getCrtUser();
            Date crtDttm = flow.getCrtDttm();
            String lastUpdateUser = flow.getLastUpdateUser();
            Date lastUpdateDttm = flow.getLastUpdateDttm();
            Long version = flow.getVersion();
            Boolean enableFlag = flow.getEnableFlag();
            String description = flow.getDescription();
            String name = flow.getName();
            String uuid = flow.getUuid();
            String driverMemory = flow.getDriverMemory();
            String executorCores = flow.getExecutorCores();
            String executorMemory = flow.getExecutorMemory();
            String executorNumber = flow.getExecutorNumber();
            Boolean isExample = flow.getIsExample();
            SQL sql = new SQL();

            // INSERT_INTO括号中为数据库表名
            sql.INSERT_INTO("flow");
            // value中的第一个字符串为数据库中表对应的字段名
            // 除数字类型的字段外其他类型必须加单引号

            //先处理修改必填字段s
            if (null == crtDttm) {
                crtDttm = new Date();
            }
            if (StringUtils.isBlank(crtUser)) {
                crtUser = "-1";
            }
            if (null == lastUpdateDttm) {
                lastUpdateDttm = new Date();
            }
            if (StringUtils.isBlank(lastUpdateUser)) {
                lastUpdateUser = "-1";
            }
            if (null == version) {
                version = 1L;
            }
            if (null == enableFlag) {
                enableFlag = true;
            }
            if (null == isExample) {
                isExample = false;
            }
            sql.VALUES("ID", Utils.addSqlStr(id));
            sql.VALUES("CRT_DTTM", Utils.addSqlStr(DateUtils.dateTimesToStr(crtDttm)));
            sql.VALUES("CRT_USER", Utils.addSqlStr(crtUser));
            sql.VALUES("LAST_UPDATE_DTTM", Utils.addSqlStr(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.VALUES("LAST_UPDATE_USER", Utils.addSqlStr(lastUpdateUser));
            sql.VALUES("VERSION", version + "");
            sql.VALUES("ENABLE_FLAG", (enableFlag ? 1 : 0) + "");
            sql.VALUES("IS_EXAMPLE", (isExample ? 1 : 0) + "");

            // 处理其他字段
            if (StringUtils.isNotBlank(description)) {
                sql.VALUES("description", Utils.addSqlStr(description));
            }
            if (StringUtils.isNotBlank(name)) {
                sql.VALUES("NAME", Utils.addSqlStr(name));
            }
            if (StringUtils.isNotBlank(uuid)) {
                sql.VALUES("UUID", Utils.addSqlStr(uuid));
            }
            if (StringUtils.isNotBlank(driverMemory)) {
                sql.VALUES("driver_memory", Utils.addSqlStr(driverMemory));
            }
            if (StringUtils.isNotBlank(executorCores)) {
                sql.VALUES("executor_cores", Utils.addSqlStr(executorCores));
            }
            if (StringUtils.isNotBlank(executorMemory)) {
                sql.VALUES("executor_memory", Utils.addSqlStr(executorMemory));
            }
            if (StringUtils.isNotBlank(executorNumber)) {
                sql.VALUES("executor_number", Utils.addSqlStr(executorNumber));
            }
            sqlStr = sql.toString() + ";";
        }
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
        if (null != flow) {
            String id = flow.getId();
            String lastUpdateUser = flow.getLastUpdateUser();
            Date lastUpdateDttm = flow.getLastUpdateDttm();
            Long version = flow.getVersion();
            Boolean enableFlag = flow.getEnableFlag();
            String description = flow.getDescription();
            String name = flow.getName();
            String uuid = flow.getUuid();
            String driverMemory = flow.getDriverMemory();
            String executorCores = flow.getExecutorCores();
            String executorMemory = flow.getExecutorMemory();
            String executorNumber = flow.getExecutorNumber();
            SQL sql = new SQL();

            // INSERT_INTO括号中为数据库表名
            sql.UPDATE("flow");
            // SET中的第一个字符串为数据库中表对应的字段名
            // 除数字类型的字段外其他类型必须加单引号

            //先处理修改必填字段
            if (null == lastUpdateDttm) {
                lastUpdateDttm = new Date();
            }
            if (StringUtils.isBlank(lastUpdateUser)) {
                lastUpdateUser = "-1";
            }
            if (null == version) {
                version = 1L;
            }
            String lastUpdateDttmStr = DateUtils.dateTimesToStr(lastUpdateDttm);
            sql.SET("LAST_UPDATE_DTTM = " + Utils.addSqlStr(lastUpdateDttmStr));
            sql.SET("LAST_UPDATE_USER = " + Utils.addSqlStr(lastUpdateUser));
            sql.SET("VERSION = " + (version + 1));
            
            // 处理其他字段
            if (null == enableFlag) {
                int enableFlagInt = enableFlag ? 1 : 0;
                sql.SET("ENABLE_FLAG = " + enableFlagInt);
            }
            if (StringUtils.isNotBlank(description)) {
                sql.SET("description = " + Utils.addSqlStr(description));
            }
            if (StringUtils.isNotBlank(name)) {
                sql.SET("NAME = " + Utils.addSqlStr(name));
            }
            if (StringUtils.isNotBlank(uuid)) {
                sql.SET("UUID = " + Utils.addSqlStr(uuid));
            }
            if (StringUtils.isNotBlank(driverMemory)) {
                sql.SET("driver_memory = " + Utils.addSqlStr(driverMemory));
            }
            if (StringUtils.isNotBlank(executorCores)) {
                sql.SET("executor_cores = " + Utils.addSqlStr(executorCores));
            }
            if (StringUtils.isNotBlank(executorMemory)) {
                sql.SET("executor_memory = " + Utils.addSqlStr(executorMemory));
            }
            if (StringUtils.isNotBlank(executorNumber)) {
                sql.SET("executor_number = " + Utils.addSqlStr(executorNumber));
            }
            sql.WHERE("VERSION = " + version);
            sql.WHERE("id = " + Utils.addSqlStr(id));
            sqlStr = sql.toString() + ";";
            if (StringUtils.isBlank(id)) {
                sqlStr = "";
            }
        }
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
        sqlStr = sql.toString() + ";";
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
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow");
        sql.WHERE("id = " + Utils.addSqlStr(id));
        sql.WHERE("ENABLE_FLAG = 1");
        sql.WHERE("IS_EXAMPLE = 0");
        sqlStr = sql.toString() + ";";
        return sqlStr;
    }
    
    /**
     * 根据id逻辑删除,设为无效
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
              sql.SET("LAST_UPDATE_USER = " + Utils.addSqlStr(username) );
              sql.SET("LAST_UPDATE_DTTM = " + Utils.addSqlStr(DateUtils.dateTimesToStr(new Date())) );
              sql.WHERE("ENABLE_FLAG = 1");
              sql.WHERE("IS_EXAMPLE = 0");
              sql.WHERE("ID = " + Utils.addSqlStrAndReplace(id));

              sqlStr = sql.toString() + ";";
          }
          return sqlStr;
      }

}
