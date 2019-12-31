package com.nature.provider.flow;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.FlowInfoDb;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class FlowInfoDbMapperProvider {

    private String id;
    private String crtUser;
    private String crtDttmStr;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String startTimeStr;
    private String endTimeStr;
    private String name;
    private String state;
    private String progress;
    private String flowId;


    private void preventSQLInjectionFlowInfoDb(FlowInfoDb flowInfoDb) {
        if (null != flowInfoDb && StringUtils.isNotBlank(flowInfoDb.getLastUpdateUser())) {
            // Mandatory Field
            String id = flowInfoDb.getId();
            String crtUser = flowInfoDb.getCrtUser();
            String lastUpdateUser = flowInfoDb.getLastUpdateUser();
            Boolean enableFlag = flowInfoDb.getEnableFlag();
            Long version = flowInfoDb.getVersion();
            Date crtDttm = flowInfoDb.getCrtDttm();
            Date lastUpdateDttm = flowInfoDb.getLastUpdateDttm();
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
            //String startTime = (null != flowInfoDb.getStartTime() ? DateUtils.dateTimesToStr(flowInfoDb.getStartTime()) : null);
            //String endTime = (null != flowInfoDb.getEndTime() ? DateUtils.dateTimesToStr(flowInfoDb.getEndTime()) : null);
            this.name = SqlUtils.preventSQLInjection(flowInfoDb.getName());
            this.state = SqlUtils.preventSQLInjection(flowInfoDb.getState());
            this.progress = SqlUtils.preventSQLInjection(flowInfoDb.getProgress());
            String flowIdStr = (null != flowInfoDb.getFlow() ? flowInfoDb.getFlow().getId() : null);
            this.flowId = (null != flowIdStr ? SqlUtils.preventSQLInjection(flowIdStr) : null);
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
        this.startTimeStr = null;
        this.endTimeStr = null;
        this.name = null;
        this.state = null;
        this.progress = null;
        this.flowId = null;
    }

    public String addFlowInfo(FlowInfoDb app) {
        String sqlStr = "select 0";
        this.preventSQLInjectionFlowInfoDb(app);
        if (null != app) {

            SQL sql = new SQL();
            sql.INSERT_INTO("flow_info");

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
            sql.VALUES("enable_flag", enableFlag + "");
            sql.VALUES("last_update_dttm", lastUpdateDttmStr);
            sql.VALUES("last_update_user", SqlUtils.preventSQLInjection(lastUpdateUser));
            sql.VALUES("version", version + "");

            sql.VALUES("start_time", startTimeStr);
            sql.VALUES("end_time", endTimeStr);
            sql.VALUES("name", name);
            sql.VALUES("state", state);
            sql.VALUES("progress", progress);
            sql.VALUES("fk_flow_id", flowId);
            sqlStr = sql.toString();
        }
        this.reset();
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
        sql.WHERE("fk_flow_id = " + SqlUtils.preventSQLInjection(flowId) + " ");
        sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * update FlowInfo
     *
     * @param flow
     * @return
     */
    public String updateFlowInfo(FlowInfoDb flow) {
        String sqlStr = "";
        this.preventSQLInjectionFlowInfoDb(flow);
        if (null != flow) {

            SQL sql = new SQL();
            // UPDATE parentheses for the database table name
            sql.UPDATE("flow_info");
            // The first string in the SET is the name of the field corresponding to the table in the database
            // all types except numeric fields must be enclosed in single quotes

            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag = " + enableFlag);
            sql.SET("state = " + state);
            sql.SET("name = " + name);
            sql.SET("progress = " + progress);
            sql.SET("end_time = " + endTimeStr);
            sql.SET("start_time = " + startTimeStr);
            /*if (null != flowId) {
                sql.SET("fk_flow_id = " + flowId);
            }*/
            sql.WHERE("version = " + version);
            sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));
            sqlStr = sql.toString();
            if (StringUtils.isBlank(id)) {
                sqlStr = "";
            }
        }
        this.reset();
        return sqlStr;
    }

    public String updateEnableFlagById(String id) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(id)) {
            SQL sql = new SQL();
            sql.UPDATE("flow_info");
            sql.SET("enable_flag = 0");
            sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
            sql.WHERE("enable_flag = 1");
            sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

}
