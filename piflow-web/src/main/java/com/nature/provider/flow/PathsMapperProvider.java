package com.nature.provider.flow;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Paths;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class PathsMapperProvider {

    private String id;
    private String crtUser;
    private String crtDttmStr;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String from;
    private String to;
    private String outport;
    private String inport;
    private String pageId;
    private String flowId;

    private void preventSQLInjectionPaths(Paths paths) {
        if (null != paths && StringUtils.isNotBlank(paths.getLastUpdateUser())) {
            // Mandatory Field
            String id = paths.getId();
            String crtUser = paths.getCrtUser();
            String lastUpdateUser = paths.getLastUpdateUser();
            Boolean enableFlag = paths.getEnableFlag();
            Long version = paths.getVersion();
            Date crtDttm = paths.getCrtDttm();
            Date lastUpdateDttm = paths.getLastUpdateDttm();
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
            this.from = SqlUtils.preventSQLInjection(paths.getFrom());
            this.to = SqlUtils.preventSQLInjection(paths.getTo());
            this.outport = SqlUtils.preventSQLInjection(paths.getOutport());
            this.inport = SqlUtils.preventSQLInjection(paths.getInport());
            this.pageId = SqlUtils.preventSQLInjection(paths.getPageId());
            String flowIdStr = (null != paths.getFlow() ? paths.getFlow().getId() : null);
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
        this.from = null;
        this.to = null;
        this.outport = null;
        this.inport = null;
        this.pageId = null;
        this.flowId = null;
    }

    /**
     * 插入list<Paths> 注意拼sql的方法必须用map接 Param内容为键值
     *
     * @param map (内容： 键为pathsList,值为List<Paths>)
     * @return
     */
    public String addPathsList(Map map) {
        List<Paths> pathsList = (List<Paths>) map.get("pathsList");
        StringBuffer sql = new StringBuffer();
        if (null != pathsList && pathsList.size() > 0) {
            sql.append("insert into ");
            sql.append("flow_path ");
            sql.append("(");
            sql.append("id,");
            sql.append("crt_dttm,");
            sql.append("crt_user,");
            sql.append("last_update_dttm,");
            sql.append("last_update_user,");
            sql.append("version,");
            sql.append("enable_flag,");
            sql.append("line_from,");
            sql.append("line_to,");
            sql.append("line_outport,");
            sql.append("line_inport,");
            sql.append("page_id,");
            sql.append("fk_flow_id");
            sql.append(") ");
            sql.append("values");
            int i = 0;
            for (Paths paths : pathsList) {
                i++;
                if (null != paths) {
                    this.preventSQLInjectionPaths(paths);

                    if (null == crtDttmStr) {
                        String crtDttm = DateUtils.dateTimesToStr(new Date());
                        crtDttmStr = SqlUtils.preventSQLInjection(crtDttm);
                    }
                    if (StringUtils.isBlank(crtUser)) {
                        crtUser = SqlUtils.preventSQLInjection("-1");
                    }
                    sql.append("(");
                    sql.append(id + ",");
                    sql.append(crtDttmStr + ",");
                    sql.append(crtUser + ",");
                    sql.append(lastUpdateDttmStr + ",");
                    sql.append(lastUpdateUser + ",");
                    sql.append(version + ",");
                    sql.append(enableFlag + ",");
                    sql.append(from + ",");
                    sql.append(to + ",");
                    sql.append(outport + ",");
                    sql.append(inport + ",");
                    sql.append(pageId + ",");
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
     * 新增paths
     *
     * @param paths
     * @return
     */
    public String addPaths(Paths paths) {
        String sqlStr = "";
        this.preventSQLInjectionPaths(paths);
        if (null != paths) {
            SQL sql = new SQL();
            sql.INSERT_INTO("flow_path");

            //先处理修改必填字段
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
            sql.VALUES("last_update_dttm", lastUpdateDttmStr);
            sql.VALUES("last_update_user", lastUpdateUser);
            sql.VALUES("version", (version + 1) + "");
            sql.VALUES("ENABLE_FLAG", enableFlag + "");

            // 处理其他字段
            sql.VALUES("line_from", from);
            sql.VALUES("line_to", to);
            sql.VALUES("line_outport", outport);
            sql.VALUES("line_inport", inport);
            sql.VALUES("line_port", pageId);
            sql.VALUES("fk_flow_id", flowId);
            sqlStr = sql.toString();

        }
        this.reset();
        return sqlStr;
    }

    /**
     * 修改paths
     *
     * @param paths
     * @return
     */
    public String updatePaths(Paths paths) {
        String sqlStr = "";
        this.preventSQLInjectionPaths(paths);
        if (null != paths) {
            SQL sql = new SQL();
            sql.UPDATE("flow_path");

            sql.SET("LAST_UPDATE_DTTM = " + lastUpdateDttmStr);
            sql.SET("LAST_UPDATE_USER = " + lastUpdateUser);
            sql.SET("VERSION = " + (version + 1));

            // 处理其他字段
            sql.SET("ENABLE_FLAG = " + enableFlag);
            sql.SET("line_from = " + from);
            sql.SET("line_to = " + to);
            sql.SET("line_outport = " + outport);
            sql.SET("line_inport = " + inport);
            sql.WHERE("VERSION = " + version);
            sql.WHERE("id = " + id);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * 根据flowId查询
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
     * 查询连线信息
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
     * 查询连线的数量
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
     * 根据id查询paths
     *
     * @param id
     * @return
     */
    public String getPathsById(String id) {
        String sqlStr = "select 0";
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
     * 根据flowId逻辑删除,设为无效
     *
     * @param flowId
     * @return
     */
    public String updateEnableFlagByFlowId(String flowId) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(flowId)) {
            SQL sql = new SQL();
            sql.UPDATE("flow_path");
            sql.SET("ENABLE_FLAG = 0");
            sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
            sql.WHERE("ENABLE_FLAG = 1");
            sql.WHERE("fk_flow_id = " + SqlUtils.preventSQLInjection(flowId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }
}
