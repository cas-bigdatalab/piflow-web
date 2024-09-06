package cn.cnic.component.process.mapper.provider;

import cn.cnic.base.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;

public class ProcessAndProcessGroupMapperProvider {

    public String getProcessAndProcessGroupList(String createUser, String param, String name, String state, String company) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("SELECT id, last_update_dttm AS lastUpdateDttm, crt_dttm AS crtDttm, ");
        strBuf.append("app_id AS appId, name, description, start_time AS startTime, ");
        strBuf.append("end_time AS endTime, progress, state, parent_process_id AS parentProcessId, processType, ");
        strBuf.append(" company, username ");
        strBuf.append("FROM ( ");
        String commonCondition = "enable_flag=1 AND app_id IS NOT NULL AND fk_flow_process_group_id IS NULL ";
        if (StringUtils.isNotBlank(createUser)) {
            commonCondition += " AND crt_user LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(createUser) + ",'%') ";
        }
        if (StringUtils.isNotBlank(name)) {
            commonCondition += " AND name LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(name) + ",'%') ";
        }
        if (StringUtils.isNotBlank(state)) {
            commonCondition += " AND state LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(state) + ",'%') ";
        }
        if (StringUtils.isNotBlank(company)) {
            commonCondition += " AND company LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(company) + ",'%') ";
        }

        strBuf.append("SELECT id, last_update_dttm, crt_dttm, app_id, name, description, ");
        strBuf.append("start_time, end_time, progress, state, parent_process_id, 'TASK' AS processType, company, crt_user AS username ");
        strBuf.append("FROM flow_process WHERE " + commonCondition);

        strBuf.append(" UNION ALL ");

        strBuf.append("SELECT id, last_update_dttm, crt_dttm, app_id, name, description, ");
        strBuf.append("start_time, end_time, progress, state, parent_process_id, 'GROUP' AS processType, company, crt_user AS username ");
        strBuf.append("FROM flow_process_group WHERE " + commonCondition);

        strBuf.append(") AS re ");
        return strBuf.toString();
    }

    public String getProcessAndProcessGroupListByUser(String param, String username) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("SELECT ");
        strBuf.append("id, ");
        strBuf.append("last_update_dttm AS lastUpdateDttm, ");
        strBuf.append("crt_dttm AS crtDttm, ");
        strBuf.append("app_id AS appId, ");
        strBuf.append("name, ");
        strBuf.append("description, ");
        strBuf.append("start_time AS startTime, ");
        strBuf.append("end_time AS endTime, ");
        strBuf.append("progress, ");
        strBuf.append("state, ");
        strBuf.append("parent_process_id AS parentProcessId, ");
        strBuf.append("processType ");
        strBuf.append("FROM ");
        strBuf.append("( ");
        strBuf.append("SELECT id,last_update_dttm,crt_dttm,app_id,name,description,start_time,end_time,progress,state,parent_process_id,'TASK' AS processType FROM flow_process ");
        strBuf.append("WHERE enable_flag=1 AND crt_user=" + SqlUtils.addSqlStrAndReplace(username) + " AND app_id IS NOT NULL AND fk_flow_process_group_id IS NULL AND (name LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(param) + ",'%') OR description LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(param) + ",'%') OR state LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(param) + ",'%') " + ") ");
        strBuf.append("UNION ALL ");
        strBuf.append("SELECT id,last_update_dttm,crt_dttm,app_id,name,description,start_time,end_time,progress,state,parent_process_id,'GROUP' AS processType FROM flow_process_group ");
        strBuf.append("WHERE enable_flag=1 AND crt_user=" + SqlUtils.addSqlStrAndReplace(username) + " AND app_id IS NOT NULL AND fk_flow_process_group_id IS NULL AND (name LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(param) + ",'%') OR description LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(param) + ",'%') OR state LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(param) + ",'%') " + ") ");
        strBuf.append(") ");
        strBuf.append("AS re");
        return strBuf.toString();
    }

}
