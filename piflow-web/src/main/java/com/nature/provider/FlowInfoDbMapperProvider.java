package com.nature.provider;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.FlowInfoDb;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class FlowInfoDbMapperProvider {

    public String addFlowInfo(FlowInfoDb app) {
        String sqlStr = "select 0";
        if (null != app) {
            String id = app.getId();
            Date crtDttm = app.getCrtDttm();
            String crtUser = app.getCrtUser();
            Boolean enableFlag = app.getEnableFlag();
            Date lastUpdateDttm = app.getLastUpdateDttm();
            String lastUpdateUser = app.getLastUpdateUser();
            Long version = app.getVersion();
            Date startTime = app.getStartTime();
            Date endTime = app.getEndTime();
            String name = app.getName();
            String state = app.getState();
            String progress = app.getProgress();
            Flow flow = app.getFlow();

            SQL sql = new SQL();
            sql.INSERT_INTO("flow_info");
            if (null != id) {
                sql.VALUES("id", SqlUtils.addSqlStr(id));
            }
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
                version = 0L;
            }
            sql.VALUES("crt_dttm", SqlUtils.addSqlStr(DateUtils.dateTimesToStr(crtDttm)));
            sql.VALUES("crt_user", SqlUtils.addSqlStr(crtUser));
            sql.VALUES("enable_flag", (enableFlag ? 1 : 0) + "");
            sql.VALUES("last_update_dttm", SqlUtils.addSqlStr(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.VALUES("last_update_user", SqlUtils.addSqlStr(lastUpdateUser));
            sql.VALUES("version", version + "");

            if (null != startTime) {
                sql.VALUES("start_time", SqlUtils.addSqlStr(DateUtils.dateTimesToStr(startTime)));
            }
            if (null != endTime) {
                sql.VALUES("end_time", SqlUtils.addSqlStr(DateUtils.dateTimesToStr(endTime)));
            }
            if (null != name) {
                sql.VALUES("name", SqlUtils.addSqlStr(name));
            }
            if (null != state) {
                sql.VALUES("state", SqlUtils.addSqlStr(state));
            }
            if (null != progress) {
                sql.VALUES("progress", SqlUtils.addSqlStr(progress));
            }
            if (null != flow) {
                sql.VALUES("fk_flow_id", SqlUtils.addSqlStr(flow.getId()));
            }
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * 根据flowId查询appId 信息
     *
     * @param flowId
     * @return
     */
    public String getFlowInfoDbListByFlowId(String flowId) {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_info");
        sql.WHERE("enable_flag = 1");
        sql.WHERE("fk_flow_id = '" + flowId + "' ");
        sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * 修改FlowInfo
     *
     * @param flow
     * @return
     */
    public String updateFlowInfo(FlowInfoDb flow) {

        String sqlStr = "";
        if (null != flow) {
            String id = flow.getId();
            String lastUpdateUser = flow.getLastUpdateUser();
            Date lastUpdateDttm = flow.getLastUpdateDttm();
            Long version = flow.getVersion();
            Boolean enableFlag = flow.getEnableFlag();
            String name = flow.getName();
            String state = flow.getState();
            String progress = flow.getProgress();
            Date endTime = flow.getEndTime();
            Date startTime = flow.getStartTime();
            Flow flow1 = flow.getFlow();
            SQL sql = new SQL();
            // UPDATE括号中为数据库表名
            sql.UPDATE("flow_info");
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
                version = 0L;
            }
            String lastUpdateDttmStr = DateUtils.dateTimesToStr(lastUpdateDttm);
            sql.SET("LAST_UPDATE_DTTM = " + SqlUtils.addSqlStr(lastUpdateDttmStr));
            sql.SET("LAST_UPDATE_USER = " + SqlUtils.addSqlStr(lastUpdateUser));
            sql.SET("VERSION = " + (version + 1));

            // 处理其他字段
            if (null != enableFlag) {
                sql.SET("ENABLE_FLAG = " + (enableFlag ? 1 : 0));
            }
            if (StringUtils.isNotBlank(state)) {
                sql.SET("state = " + SqlUtils.addSqlStr(state));
            }
            if (StringUtils.isNotBlank(name)) {
                sql.SET("NAME = " + SqlUtils.addSqlStr(name));
            }
            if (StringUtils.isNotBlank(progress)) {
                sql.SET("progress = " + SqlUtils.addSqlStr(progress));
            }
            if (null != endTime) {
                String endTimeStr = DateUtils.dateTimesToStr(endTime);
                if (StringUtils.isNotBlank(endTimeStr)) {
                    sql.SET("end_time = " + SqlUtils.addSqlStr(endTimeStr));
                }
            }
            if (null != startTime) {
                String startTimeStr = DateUtils.dateTimesToStr(startTime);
                if (StringUtils.isNotBlank(startTimeStr)) {
                    sql.SET("start_time = " + SqlUtils.addSqlStr(startTimeStr));
                }
            }
            if (null != flow1) {
                sql.SET("fk_flow_id = " + SqlUtils.addSqlStr(flow1.getId()));
            }
            sql.WHERE("VERSION = " + version);
            sql.WHERE("ID = " + SqlUtils.addSqlStr(id));
            sqlStr = sql.toString();
            if (StringUtils.isBlank(id)) {
                sqlStr = "";
            }
        }
        return sqlStr;
    }

    public String updateEnableFlagById(String id) {
     	 UserVo user = SessionUserUtil.getCurrentUser();
          String username = (null != user) ? user.getUsername() : "-1";
          String sqlStr = "select 0";
         if (StringUtils.isNotBlank(id)) {
             SQL sql = new SQL();
             sql.UPDATE("flow_info");
             sql.SET("ENABLE_FLAG = 0");
             sql.SET("last_update_user = " + SqlUtils.addSqlStr(username) );
             sql.SET("last_update_dttm = " + SqlUtils.addSqlStr(DateUtils.dateTimesToStr(new Date())) );
             sql.WHERE("ENABLE_FLAG = 1");
             sql.WHERE("id = " + SqlUtils.addSqlStrAndReplace(id));

             sqlStr = sql.toString();
         }
         return sqlStr;
     }
    
}
