package cn.cnic.component.process.mapper.provider;

import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.process.vo.ProcessPageVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class ProcessAndProcessGroupMapperProvider {

    public String getProcessAndProcessGroupList(ProcessPageVo pageVo) {
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
        strBuf.append("processType, ");
        strBuf.append("trigger_mode, ");
        strBuf.append("schedule_id, ");
        strBuf.append("schedule_name, ");
        strBuf.append("trigger_file ");
        strBuf.append("FROM ");
        strBuf.append("( ");
        strBuf.append("SELECT id,last_update_dttm,crt_dttm,app_id,name,description,start_time,end_time,progress,state,parent_process_id,'TASK' AS processType,trigger_mode,schedule_id,schedule_name,trigger_file FROM flow_process ");
        strBuf.append("WHERE enable_flag=1 AND app_id IS NOT NULL AND fk_flow_process_group_id IS NULL ");
        if (StringUtils.isNotBlank(pageVo.getName())) {
            strBuf.append("AND name LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(pageVo.getName()) + ",'%') ");
        }
        if (ObjectUtils.isNotEmpty(pageVo.getTriggerMode())) {
            strBuf.append("AND trigger_mode = " + pageVo.getTriggerMode() +" ");
        }
        if (StringUtils.isNotBlank(pageVo.getState())) {
            strBuf.append("AND state = " + SqlUtils.addSqlStrAndReplace(pageVo.getState()) + " ");
        }
        if (StringUtils.isNotBlank(pageVo.getScheduleName())) {
            strBuf.append("AND schedule_name LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(pageVo.getScheduleName()) + ",'%') ");
        }
        if (StringUtils.isNotBlank(pageVo.getCrtDttm())) {
            strBuf.append("AND crt_dttm LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(pageVo.getCrtDttm()) + ",'%') ");
        }

        strBuf.append("UNION ALL ");
        strBuf.append("SELECT id,last_update_dttm,crt_dttm,app_id,name,description,start_time,end_time,progress,state,parent_process_id,'GROUP' AS processType,NULL,NULL,NULL,NULL FROM flow_process_group ");
        strBuf.append("WHERE enable_flag=1 AND app_id IS NOT NULL AND fk_flow_process_group_id IS NULL ");
        if (StringUtils.isNotBlank(pageVo.getName())) {
            strBuf.append("AND name LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(pageVo.getName()) + ",'%') ");
        }
        if (StringUtils.isNotBlank(pageVo.getState())) {
            strBuf.append("AND state = " + SqlUtils.addSqlStrAndReplace(pageVo.getState()) + " ");
        }
        if (StringUtils.isNotBlank(pageVo.getCrtDttm())) {
            strBuf.append("AND crt_dttm LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(pageVo.getCrtDttm()) + ",'%') ");
        }

        strBuf.append(") ");
        strBuf.append("AS re ");
        return strBuf.toString();
    }

    public String getProcessAndProcessGroupListByUser(String username, ProcessPageVo pageVo) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("SELECT DISTINCT ");
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
        strBuf.append("processType, ");
        strBuf.append("trigger_mode, ");
        strBuf.append("schedule_id, ");
        strBuf.append("schedule_name, ");
        strBuf.append("trigger_file ");
        strBuf.append("FROM ");
        strBuf.append("( ");
        strBuf.append("SELECT id,last_update_dttm,crt_dttm,app_id,name,description,start_time,end_time,progress,state,parent_process_id,'TASK' AS processType,trigger_mode,schedule_id,schedule_name,trigger_file FROM flow_process ");
        strBuf.append("WHERE enable_flag=1 AND crt_user=" + SqlUtils.addSqlStrAndReplace(username) + " AND app_id IS NOT NULL AND fk_flow_process_group_id IS NULL ");
        if (StringUtils.isNotBlank(pageVo.getName())) {
            strBuf.append("AND name LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(pageVo.getName()) + ",'%') ");
        }
        if (ObjectUtils.isNotEmpty(pageVo.getTriggerMode())) {
            strBuf.append("AND trigger_mode = " + pageVo.getTriggerMode() + " ");
        }
        if (StringUtils.isNotBlank(pageVo.getState())) {
            strBuf.append("AND state = " + SqlUtils.addSqlStrAndReplace(pageVo.getState()) + " ");
        }
        if (StringUtils.isNotBlank(pageVo.getScheduleName())) {
            strBuf.append("AND schedule_name LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(pageVo.getScheduleName()) + ",'%') ");
        }
        if (StringUtils.isNotBlank(pageVo.getCrtDttm())) {
            strBuf.append("AND crt_dttm LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(pageVo.getCrtDttm()) + ",'%') ");
        }
        strBuf.append("UNION ALL ");
        strBuf.append("SELECT id,last_update_dttm,crt_dttm,app_id,name,description,start_time,end_time,progress,state,parent_process_id,'GROUP' AS processType,NULL,NULL,NULL,NULL FROM flow_process_group ");
        strBuf.append("WHERE enable_flag=1 AND crt_user=" + SqlUtils.addSqlStrAndReplace(username) + " AND app_id IS NOT NULL AND fk_flow_process_group_id IS NULL ");
        if (StringUtils.isNotBlank(pageVo.getName())) {
            strBuf.append("AND name LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(pageVo.getName()) + ",'%') ");
        }
        if (StringUtils.isNotBlank(pageVo.getState())) {
            strBuf.append("AND state = " + SqlUtils.addSqlStrAndReplace(pageVo.getState()) + " ");
        }
        if (StringUtils.isNotBlank(pageVo.getCrtDttm())) {
            strBuf.append("AND crt_dttm LIKE CONCAT('%'," + SqlUtils.addSqlStrAndReplace(pageVo.getCrtDttm()) + ",'%') ");
        }
        strBuf.append(") ");
        strBuf.append("AS re");
        return strBuf.toString();
    }

}
