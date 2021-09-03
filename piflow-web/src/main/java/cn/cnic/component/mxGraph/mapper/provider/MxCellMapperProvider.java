package cn.cnic.component.mxGraph.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.mxGraph.entity.MxCell;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class MxCellMapperProvider {

    private String id;
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

    private boolean preventSQLInjectionMxCell(MxCell mxCell) {
        if (null == mxCell || StringUtils.isBlank(mxCell.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        String id = mxCell.getId();
        String lastUpdateUser = mxCell.getLastUpdateUser();
        Boolean enableFlag = mxCell.getEnableFlag();
        Long version = mxCell.getVersion();
        Date lastUpdateDttm = mxCell.getLastUpdateDttm();
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
        this.id = SqlUtils.preventSQLInjection(id);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(lastUpdateUser);
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
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
        return true;
    }

    private void reset() {
        this.id = null;
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
        boolean flag = this.preventSQLInjectionMxCell(mxCell);
        if (flag) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("INSERT INTO mx_cell");
            stringBuffer.append("(");
            stringBuffer.append(SqlUtils.baseFieldName() + ",");
            stringBuffer.append("mx_pageid, ");
            stringBuffer.append("mx_parent, ");
            stringBuffer.append("mx_style, ");
            stringBuffer.append("mx_edge, ");
            stringBuffer.append("mx_source, ");
            stringBuffer.append("mx_target,");
            stringBuffer.append("mx_value, ");
            stringBuffer.append("mx_vertex, ");
            stringBuffer.append("fk_mx_graph_id ");
            stringBuffer.append(") ");
            stringBuffer.append("VALUES");
            stringBuffer.append("(");
            stringBuffer.append(SqlUtils.baseFieldValues(mxCell) + ",");
            stringBuffer.append(pageId + ", ");
            stringBuffer.append(parent + ", ");
            stringBuffer.append(style + ", ");
            stringBuffer.append(edge + ", ");
            stringBuffer.append(source + ", ");
            stringBuffer.append(target + ", ");
            stringBuffer.append(value + ", ");
            stringBuffer.append(vertex + ", ");
            stringBuffer.append(mxGraphModelId + " ");
            stringBuffer.append(")");
            sqlStr = stringBuffer.toString();
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
        boolean flag = this.preventSQLInjectionMxCell(mxCell);
        if (flag) {
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
