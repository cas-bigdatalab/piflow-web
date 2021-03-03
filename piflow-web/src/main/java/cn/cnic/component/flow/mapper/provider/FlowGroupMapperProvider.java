package cn.cnic.component.flow.mapper.provider;

import cn.cnic.base.util.SqlUtils;
import org.apache.commons.lang3.StringUtils;

public class FlowGroupMapperProvider {

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

    public String getFlowGroupNameListById(String fId, String id){
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
