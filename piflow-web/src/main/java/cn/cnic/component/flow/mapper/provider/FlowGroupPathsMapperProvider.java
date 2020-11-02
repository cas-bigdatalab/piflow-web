package cn.cnic.component.flow.mapper.provider;

import cn.cnic.base.util.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

public class FlowGroupPathsMapperProvider {

    /**
     * Query flowGroupPath by flowGroupId
     *
     * @param flowGroupId
     * @return
     */
    public String getFlowGroupPathsByFlowGroupId(String flowGroupId) {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_group_path");
        sql.WHERE("enable_flag = 1");
        sql.WHERE("fk_flow_group_id = " + SqlUtils.preventSQLInjection(flowGroupId));
        sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * Query connection information
     *
     * @param flowGroupId
     * @param pageId
     * @param from
     * @param to
     * @return
     */
    public String getFlowGroupPaths(String flowGroupId, String pageId, String from, String to) {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_group_path");
        sql.WHERE("enable_flag = 1");
        if (StringUtils.isNotBlank(flowGroupId)) {
            sql.WHERE("fk_flow_group_id = " + SqlUtils.preventSQLInjection(flowGroupId));
        }
        if (StringUtils.isNotBlank(pageId)) {
            sql.WHERE("page_id = " + SqlUtils.preventSQLInjection(pageId));
        }
        if (StringUtils.isNotBlank(from)) {
            sql.WHERE("line_from = " + SqlUtils.preventSQLInjection(from));
        }
        if (StringUtils.isNotBlank(to)) {
            sql.WHERE("line_to = " + SqlUtils.preventSQLInjection(to));
        }
        sqlStr = sql.toString();
        return sqlStr;
    }
}
