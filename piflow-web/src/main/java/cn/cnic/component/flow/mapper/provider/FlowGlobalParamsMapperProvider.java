package cn.cnic.component.flow.mapper.provider;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.flow.entity.FlowGlobalParams;

public class FlowGlobalParamsMapperProvider {

    private String id;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String name;
    private String type;
    private String content;

    private boolean preventSQLInjectionFlowGlobalParams(FlowGlobalParams globalParams) {
        if (null == globalParams || StringUtils.isBlank(globalParams.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        Boolean enableFlag = globalParams.getEnableFlag();
        Long version = globalParams.getVersion();
        this.id = SqlUtils.preventSQLInjection(globalParams.getId());
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(globalParams.getLastUpdateUser());
        String lastUpdateDttmStr = StringUtils.isBlank(globalParams.getLastUpdateDttmString()) ? DateUtils.dateTimesToStr(new Date()) : globalParams.getLastUpdateDttmString();
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

        // Selection field
        this.name = SqlUtils.preventSQLInjection(globalParams.getName());
        this.type = SqlUtils.preventSQLInjection(globalParams.getType());
        this.content = SqlUtils.preventSQLInjection(globalParams.getContent());
       
        return true;
    }

    private void resetFlowGlobalParams() {
        this.id = null;
        this.lastUpdateDttmStr = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.type = null;
        this.content = null;
    }

    /**
     * add FlowGlobalParams
     *
     * @param globalParams
     * @return
     */
    public String addGlobalParams(FlowGlobalParams globalParams) {
        String sqlStr = "";
        boolean flag = this.preventSQLInjectionFlowGlobalParams(globalParams);
        if (flag) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("INSERT INTO flow_global_params ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldName() + ", ");
            stringBuffer.append("`name`, ");
            stringBuffer.append("`type`, ");
            stringBuffer.append("`content` ");
            stringBuffer.append(") ");
            stringBuffer.append("VALUES ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldValues(globalParams) + ", ");
            // handle other fields
            stringBuffer.append(this.name + ", ");
            stringBuffer.append(this.type + ", ");
            stringBuffer.append(this.content + " ");
            stringBuffer.append(") ");
            sqlStr = stringBuffer.toString();
            this.resetFlowGlobalParams();
        }
        return sqlStr;
    }

    /**
     * update FlowGlobalParams
     *
     * @param globalParams
     * @return
     */
    public String updateGlobalParams(FlowGlobalParams globalParams) {

        String sqlStr = "SELECT 0";
        this.preventSQLInjectionFlowGlobalParams(globalParams);
        if (null != globalParams) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("`flow_global_params`");
            // The first string in the SET is the name of the field corresponding to the
            // table in the database
            sql.SET("`last_update_dttm` = " + this.lastUpdateDttmStr);
            sql.SET("`last_update_user` = " + this.lastUpdateUser);
            sql.SET("`version` = " + (this.version + 1));

            // handle other fields
            sql.SET("`enable_flag` = " + this.enableFlag);
            sql.SET("`name` = " + this.name);
            sql.SET("`type` = " + this.type);
            sql.SET("`content` = " + this.content);
            sql.WHERE("`version` = " + this.version);
            sql.WHERE("`id` = " + this.id);
            if (StringUtils.isNotBlank(this.id)) {
                sqlStr = sql.toString();
            }
        }
        this.resetFlowGlobalParams();
        return sqlStr;
    }
    
    public String updateEnableFlagById(String username, String id, boolean enableFlag) {
        if (StringUtils.isBlank(id) || StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        String lastUpdateDttm = DateUtils.dateTimesToStr(new Date());
        int enableFlagInt = enableFlag ? 1 : 0;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("UPDATE `flow_global_params` c SET ");
        stringBuffer.append("c.`enable_flag`=" + enableFlagInt + ",");
        stringBuffer.append("c.`last_update_dttm`=" + SqlUtils.preventSQLInjection(lastUpdateDttm) + ",");
        stringBuffer.append("c.`last_update_user`=" + SqlUtils.preventSQLInjection(username) + " ");
        stringBuffer.append("where c.`id`=" + SqlUtils.preventSQLInjection(id));
        return stringBuffer.toString();
        
    }

    /**
     * Query all flow paging queries
     *
     * @param param
     * @return
     */
    public String getGlobalParamsListParam(String username, boolean isAdmin, String param) {
        String sqlStr = "SELECT 0";
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("SELECT * ");
        strBuf.append("FROM `flow_global_params` ");
        strBuf.append("WHERE ");
        strBuf.append("`enable_flag`=1 ");
        if (!isAdmin) {
            strBuf.append("AND `crt_user`=" + SqlUtils.preventSQLInjection(username) + " ");
        }
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("AND ( ");
            strBuf.append("`name` LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            strBuf.append("OR `type` LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            strBuf.append("OR `content` LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            strBuf.append(") ");
        }
        strBuf.append("ORDER BY `crt_dttm` DESC ");
        sqlStr = strBuf.toString();
        return sqlStr;
    }

    /**
     * Query globalParams by ID
     *
     * @param id
     * @return
     */
    public String getGlobalParamsById(String username, boolean isAdmin, String id) {
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(id)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from `flow_global_params` ");
            strBuf.append("where `enable_flag` = 1 ");
            strBuf.append("and `id` = " + SqlUtils.preventSQLInjection(id) + " ");
            if (!isAdmin) {
                strBuf.append("and `crt_user` = " + SqlUtils.preventSQLInjection(username));
            }
            sqlStr = strBuf.toString();
        }
        return sqlStr;
    }
    
    public String getFlowGlobalParamsByIds(String[] ids) {
    	if (null == ids || ids.length <= 0) {
    		return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from `flow_global_params` ");
        strBuf.append("where `enable_flag` = 1 ");
        strBuf.append("and `id` in ( " + SqlUtils.strArrayToStr(ids) + ") ");
        String sqlStr = strBuf.toString();
        return sqlStr;
    }
    
    public String getFlowGlobalParamsByFlowId(String flowId) {
    	if (null == flowId) {
    		return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from `flow_global_params` ");
        strBuf.append("where `enable_flag` = 1 ");
        strBuf.append("and `id` in (select global_params_id from association_global_params_flow where flow_id= " + SqlUtils.preventSQLInjection(flowId) + ") ");
        String sqlStr = strBuf.toString();
        return sqlStr;
    }
    
    public String getFlowGlobalParamsByProcessId(String processId) {
    	if (null == processId) {
    		return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from `flow_global_params` ");
        strBuf.append("where `enable_flag` = 1 ");
        strBuf.append("and `id` in (select global_params_id from association_global_params_flow where process_id= " + SqlUtils.preventSQLInjection(processId) + ") ");
        String sqlStr = strBuf.toString();
        return sqlStr;
    }

}
