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

}
