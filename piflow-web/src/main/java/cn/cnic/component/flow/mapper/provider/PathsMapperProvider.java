package cn.cnic.component.flow.mapper.provider;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.flow.entity.Paths;

public class PathsMapperProvider {

    private String id;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String from;
    private String to;
    private String outport;
    private String inport;
    private String pageId;
    private String filterCondition;
    private String flowId;

    private boolean preventSQLInjectionPaths(Paths paths) {
        if (null == paths || StringUtils.isBlank(paths.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        String id = paths.getId();
        String lastUpdateUser = paths.getLastUpdateUser();
        Boolean enableFlag = paths.getEnableFlag();
        Long version = paths.getVersion();
        this.id = SqlUtils.preventSQLInjection(id);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(lastUpdateUser);
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(new Date());
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

        // Selection field
        this.from = SqlUtils.preventSQLInjection(paths.getFrom());
        this.to = SqlUtils.preventSQLInjection(paths.getTo());
        this.outport = SqlUtils.preventSQLInjection(paths.getOutport());
        this.inport = SqlUtils.preventSQLInjection(paths.getInport());
        this.pageId = SqlUtils.preventSQLInjection(paths.getPageId());
        this.filterCondition = SqlUtils.preventSQLInjection(paths.getFilterCondition());
        String flowIdStr = (null != paths.getFlow() ? paths.getFlow().getId() : null);
        this.flowId = (null != flowIdStr ? SqlUtils.preventSQLInjection(flowIdStr) : null);
        return true;
    }

    private void reset() {
        this.id = null;
        this.lastUpdateDttmStr = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.from = null;
        this.to = null;
        this.outport = null;
        this.inport = null;
        this.pageId = null;
        this.filterCondition = null;
        this.flowId = null;
    }

    /**
     * Insert paths
     *
     * @param paths
     * @return
     */
    public String addPaths(Paths paths) {
        String sqlString = "SELECT 0";
        boolean flag = this.preventSQLInjectionPaths(paths);
        if (flag) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("INSERT INTO ");
            stringBuffer.append("flow_path ");
            stringBuffer.append("(");
            stringBuffer.append(SqlUtils.baseFieldName() + ",");
            stringBuffer.append("line_from,line_to,line_outport,line_inport,page_id,filter_condition,fk_flow_id");
            stringBuffer.append(") ");
            stringBuffer.append("VALUES");
            stringBuffer.append("(");
            stringBuffer.append(SqlUtils.baseFieldValues(paths) + ",");
            stringBuffer.append(this.from + ",");
            stringBuffer.append(this.to + ",");
            stringBuffer.append(this.outport + ",");
            stringBuffer.append(this.inport + ",");
            stringBuffer.append(this.pageId + ",");
            stringBuffer.append(this.filterCondition + ",");
            stringBuffer.append(this.flowId);
            stringBuffer.append(")");
            sqlString = stringBuffer.toString();
            this.reset();
        }
        return sqlString;
    }

    /**
     * Insert "list<Paths>" Note that the method of spelling SQL must use "map" to connect the "Param" content to the key value.
     *
     * @param map (Content: The key is pathsList, the value is List<Paths>)
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String addPathsList(Map map) {
        List<Paths> pathsList = (List<Paths>) map.get("pathsList");
        StringBuffer sql = new StringBuffer();
        if (null != pathsList && pathsList.size() > 0) {
            sql.append("INSERT INTO ");
            sql.append("flow_path ");
            sql.append("(");
            sql.append(SqlUtils.baseFieldName() + ",");
            sql.append("line_from,");
            sql.append("line_to,");
            sql.append("line_outport,");
            sql.append("line_inport,");
            sql.append("page_id,");
            sql.append("filter_condition,");
            sql.append("fk_flow_id");
            sql.append(") ");
            sql.append("VALUES");
            int i = 0;
            for (Paths paths : pathsList) {
                i++;
                boolean flag = this.preventSQLInjectionPaths(paths);
                if (flag) {
                    sql.append("(");
                    sql.append(SqlUtils.baseFieldValues(paths) + ",");
                    sql.append(from + ",");
                    sql.append(to + ",");
                    sql.append(outport + ",");
                    sql.append(inport + ",");
                    sql.append(pageId + ",");
                    sql.append(filterCondition + ",");
                    sql.append(flowId);
                    if (i != pathsList.size()) {
                        sql.append("),");
                    } else {
                        sql.append(")");
                    }
                    this.reset();
                }
            }
            sql.append(";");
        }
        return sql.toString();
    }

    /**
     * update paths
     *
     * @param paths
     * @return
     */
    public String updatePaths(Paths paths) {
        String sqlStr = "SELECT 0";
        if (this.preventSQLInjectionPaths(paths)) {
            SQL sql = new SQL();
            sql.UPDATE("flow_path");

            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // Handling other fields
            sql.SET("enable_flag = " + enableFlag);
            sql.SET("line_from = " + from);
            sql.SET("line_to = " + to);
            sql.SET("line_outport = " + outport);
            sql.SET("line_inport = " + inport);
            sql.SET("filter_condition = " + filterCondition);
            sql.WHERE("version = " + version);
            sql.WHERE("id = " + id);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * Query according to "flowId"
     *
     * @param flowId
     * @return
     */
    public String getPathsListByFlowId(String flowId) {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_path");
        sql.WHERE("enable_flag = 1");
        sql.WHERE("fk_flow_id = " + SqlUtils.preventSQLInjection(flowId));
        sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * Query connection information
     *
     * @param flowId
     * @param pageId
     * @param from
     * @param to
     * @return
     */
    public String getPaths(String flowId, String pageId, String from, String to) {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_path");
        sql.WHERE("enable_flag = 1");
        if (StringUtils.isNotBlank(flowId)) {
            sql.WHERE("fk_flow_id = " + SqlUtils.preventSQLInjection(flowId));
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

    /**
     * Query the number of connections
     *
     * @param flowId
     * @param pageId
     * @param from
     * @param to
     * @return
     */
    public String getPathsCounts(String flowId, String pageId, String from, String to) {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("count(id)");
        sql.FROM("flow_path");
        sql.WHERE("enable_flag = 1");
        if (StringUtils.isNotBlank(flowId)) {
            sql.WHERE("fk_flow_id = " + SqlUtils.preventSQLInjection(flowId));
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

    /**
     * Query paths by id
     *
     * @param id
     * @return
     */
    public String getPathsById(String id) {
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(id)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("flow_path");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("id=" + SqlUtils.preventSQLInjection(id));
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * Logically delete flowInfo according to flowId
     *
     * @param flowId
     * @return
     */
    public String updateEnableFlagByFlowId(String username, String flowId) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(flowId)) {
            return "SELECT 0";
        }
        SQL sql = new SQL();
        sql.UPDATE("flow_path");
        sql.SET("enable_flag = 0");
        sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
        sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
        sql.WHERE("enable_flag = 1");
        sql.WHERE("fk_flow_id = " + SqlUtils.preventSQLInjection(flowId));

        return sql.toString();
    }
}
