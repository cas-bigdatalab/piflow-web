package cn.cnic.component.template.mapper.provider;

import cn.cnic.base.util.SqlUtils;
import org.apache.commons.lang3.StringUtils;

public class FlowGroupTemplateMapperProvider {


    public String getFlowGroupTemplateVoListPage(String username, boolean isAdmin, String param) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select* ");
        strBuf.append("from flow_group_template ");
        strBuf.append("where ");
        strBuf.append("enable_flag = 1 ");
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("and name like CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
        }
        if (!isAdmin) {
            strBuf.append("and crt_user = " + SqlUtils.preventSQLInjection(username));
        }
        strBuf.append("order by crt_dttm desc ");
        return strBuf.toString();
    }

    public String getFlowGroupTemplateVoById(String username, boolean isAdmin, String id) {
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(id)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select* ");
            strBuf.append("from flow_group_template ");
            strBuf.append("where ");
            strBuf.append("enable_flag = 1 ");
            strBuf.append("and ");
            strBuf.append("id = " + SqlUtils.addSqlStrAndReplace(id) + " ");
            if (!isAdmin) {
                strBuf.append("and crt_user = " + SqlUtils.preventSQLInjection(username));
            }
            sqlStr = strBuf.toString();
        }
        return sqlStr;
    }

}
