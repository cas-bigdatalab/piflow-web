package com.nature.provider.template;

import com.nature.base.util.SqlUtils;
import org.apache.commons.lang3.StringUtils;

public class FlowGroupTemplateMapperProvider {


    public String getFlowGroupTemplateVoListPage(String param) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select* ");
        strBuf.append("from flow_group_template ");
        strBuf.append("where ");
        strBuf.append("enable_flag = 1 ");
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("and name like " + SqlUtils.addSqlStrLikeAndReplace(param) + " ");
        }
        strBuf.append(SqlUtils.addQueryByUserRole(true, false));
        strBuf.append("order by crt_dttm desc ");
        return strBuf.toString();
    }

    public String getFlowGroupTemplateVoById(String id) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(id)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select* ");
            strBuf.append("from flow_group_template ");
            strBuf.append("where ");
            strBuf.append("enable_flag = 1 ");
            strBuf.append("and ");
            strBuf.append("id = " + SqlUtils.addSqlStrAndReplace(id) + " ");
            strBuf.append(SqlUtils.addQueryByUserRole(true, false));
            sqlStr = strBuf.toString();
        }
        return sqlStr;
    }

}
