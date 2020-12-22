package cn.cnic.component.mxGraph.mapper.provider;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.mxGraph.entity.MxCell;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class MxCellMapperProvider {

    private String id;
    private String crtUser;
    private String crtDttmStr;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String pageId;
    private String parent;
    private String style;
    private String edge;
    private String source;
    private String target;
    private String value;
    private String vertex;
    private String mxGraphModelId;

    private void preventSQLInjectionMxCell(MxCell mxCell) {
        if (null != mxCell && StringUtils.isNotBlank(mxCell.getLastUpdateUser())) {
            // Mandatory Field
            String id = mxCell.getId();
            String crtUser = mxCell.getCrtUser();
            String lastUpdateUser = mxCell.getLastUpdateUser();
            Boolean enableFlag = mxCell.getEnableFlag();
            Long version = mxCell.getVersion();
            Date crtDttm = mxCell.getCrtDttm();
            Date lastUpdateDttm = mxCell.getLastUpdateDttm();
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
            this.pageId = SqlUtils.preventSQLInjection(mxCell.getPageId());
            this.parent = SqlUtils.preventSQLInjection(mxCell.getParent());
            this.style = SqlUtils.preventSQLInjection(mxCell.getStyle());
            this.edge = SqlUtils.preventSQLInjection(mxCell.getEdge());
            this.source = SqlUtils.preventSQLInjection(mxCell.getSource());
            this.value = SqlUtils.preventSQLInjection(mxCell.getValue());
            this.target = SqlUtils.preventSQLInjection(mxCell.getTarget());
            this.vertex = SqlUtils.preventSQLInjection(mxCell.getVertex());
            String mxGraphModelIdStr = (null != mxCell.getMxGraphModel() ? mxCell.getMxGraphModel().getId() : null);
            this.mxGraphModelId = (null != mxGraphModelIdStr ? SqlUtils.preventSQLInjection(mxGraphModelIdStr) : null);
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
        this.pageId = null;
        this.parent = null;
        this.style = null;
        this.edge = null;
        this.source = null;
        this.value = null;
        this.target = null;
        this.vertex = null;
    }


    /**
     * add mxCell
     *
     * @param mxCell
     * @return
     */
    public String addMxCell(MxCell mxCell) {
        String sqlStr = "";
        this.preventSQLInjectionMxCell(mxCell);
        if (null != lastUpdateUser) {
            SQL NewSQL = new SQL();

            // INSERT_INTO brackets is table name
            NewSQL.INSERT_INTO("mx_cell");
            // The first string in the value is the field name corresponding to the table in the database.

            if (null == crtUser) {
                crtUser = SqlUtils.addSqlStr("-1");
            }
            if (null == crtDttmStr) {
                String nowTimeStr = DateUtils.dateTimesToStr(new Date());
                crtDttmStr = SqlUtils.addSqlStr(nowTimeStr);
            }
            NewSQL.VALUES("id", id);
            NewSQL.VALUES("crt_dttm", crtDttmStr);
            NewSQL.VALUES("crt_user", crtUser);
            NewSQL.VALUES("last_update_dttm", lastUpdateDttmStr);
            NewSQL.VALUES("last_update_user", lastUpdateUser);
            NewSQL.VALUES("version", version + "");
            NewSQL.VALUES("enable_flag", enableFlag + "");

            // handle other fields
            NewSQL.VALUES("mx_pageid", pageId);
            NewSQL.VALUES("mx_parent", parent);
            NewSQL.VALUES("mx_style", style);
            NewSQL.VALUES("mx_edge", edge);
            NewSQL.VALUES("mx_source", source);
            NewSQL.VALUES("mx_target", target);
            NewSQL.VALUES("mx_value", value);
            NewSQL.VALUES("mx_vertex", vertex);
            NewSQL.VALUES("fk_mx_graph_id", mxGraphModelId);
            sqlStr = NewSQL.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * update mxCell
     *
     * @param mxCell
     * @return
     */
    public String updateMxCell(MxCell mxCell) {
        String sqlStr = "";
        this.preventSQLInjectionMxCell(mxCell);
        if (null != lastUpdateUser) {
            SQL newSQL = new SQL();
            // UPDATE parentheses for the database table name
            newSQL.UPDATE("mx_cell");
            // The first string in the SET is the name of the field corresponding to the table in the database

            newSQL.SET("last_update_dttm = " + lastUpdateDttmStr);
            newSQL.SET("last_update_user = " + lastUpdateUser);
            newSQL.SET("version = " + (version + 1));

            // handle other fields
            newSQL.SET("enable_flag = " + enableFlag);
            newSQL.SET("mx_pageid = " + pageId);
            newSQL.SET("mx_parent = " + parent);
            newSQL.SET("mx_style = " + style);
            newSQL.SET("mx_edge = " + edge);
            newSQL.SET("mx_source = " + source);
            newSQL.SET("mx_target = " + target);
            newSQL.SET("mx_value = " + value);
            newSQL.SET("mx_vertex = " + vertex);
            if (null != mxGraphModelId) {
                newSQL.SET("fk_mx_graph_id = " + mxGraphModelId);
            }
            newSQL.WHERE("version = " + version);
            newSQL.WHERE("id = " + id);
            sqlStr = newSQL.toString();
            if (StringUtils.isBlank(id)) {
                sqlStr = "";
            }
        }
        this.reset();
        return sqlStr;
    }

    /**
     * Query MxCell's list based on mxGraphId
     *
     * @param mxGraphId
     * @return
     */
    public String getMeCellByMxGraphId(String mxGraphId) {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("mx_cell");
        sql.WHERE("fk_mx_graph_id = " + SqlUtils.preventSQLInjection(mxGraphId));
        sql.WHERE("enable_flag = 1 ");
        sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * Query MxCell based on Id
     *
     * @param id
     * @return
     */
    public String getMeCellById(String id) {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("mx_cell");
        sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));
        sql.WHERE("enable_flag = 1");
        sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * Query MxCell based on mxGraphId and pageId
     *
     * @param mxGraphId
     * @param pageId
     * @return
     */
    public String getMxCellByMxGraphIdAndPageId(String mxGraphId, String pageId) {
        if (StringUtils.isBlank(mxGraphId)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(pageId)) {
            return "SELECT 0";
        }
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("mx_cell");
        sql.WHERE("fk_mx_graph_id = " + SqlUtils.preventSQLInjection(mxGraphId));
        sql.WHERE("mx_pageid = " + SqlUtils.preventSQLInjection(pageId));
        sql.WHERE("enable_flag = 1");
        return sql.toString();
    }


    /**
     * Delete according to id logic, set to invalid
     *
     * @param id
     * @return
     */
    public String updateEnableFlagById(String username, String id) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }
        SQL sql = new SQL();
        sql.UPDATE("mx_cell");
        sql.SET("enable_flag = 0");
        sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
        sql.SET("last_update_dttm = " + SqlUtils.addSqlStr(DateUtils.dateTimesToStr(new Date())));
        sql.WHERE("enable_flag = 1");
        sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));

        return sql.toString();
    }

}
