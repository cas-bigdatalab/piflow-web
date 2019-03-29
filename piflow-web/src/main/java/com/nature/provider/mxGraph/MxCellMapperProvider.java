package com.nature.provider.mxGraph;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGraphModel;
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
     * 添加mxCell
     *
     * @param mxCell
     * @return
     */
    public String addMxCell(MxCell mxCell) {
        String sqlStr = "";
        this.preventSQLInjectionMxCell(mxCell);
        if (null != lastUpdateUser) {
            SQL NewSQL = new SQL();

            // INSERT_INTO括号中为数据库表名
            NewSQL.INSERT_INTO("mx_cell");
            // value中的第一个字符串为数据库中表对应的字段名

            if (null == crtUser) {
                crtUser = SqlUtils.addSqlStr("-1");
            }
            if (null == crtDttmStr) {
                String nowTimeStr = DateUtils.dateTimesToStr(new Date());
                crtDttmStr = SqlUtils.addSqlStr(nowTimeStr);
            }
            NewSQL.VALUES("ID", id);
            NewSQL.VALUES("CRT_DTTM", crtDttmStr);
            NewSQL.VALUES("CRT_USER", crtUser);
            NewSQL.VALUES("LAST_UPDATE_DTTM", lastUpdateDttmStr);
            NewSQL.VALUES("LAST_UPDATE_USER", lastUpdateUser);
            NewSQL.VALUES("VERSION", version + "");
            NewSQL.VALUES("ENABLE_FLAG", enableFlag + "");

            // 处理其他字段
            NewSQL.VALUES("MX_PAGEID", pageId);
            NewSQL.VALUES("MX_PARENT", parent);
            NewSQL.VALUES("MX_STYLE", style);
            NewSQL.VALUES("MX_EDGE", edge);
            NewSQL.VALUES("MX_SOURCE", source);
            NewSQL.VALUES("MX_TARGET", target);
            NewSQL.VALUES("MX_VALUE", value);
            NewSQL.VALUES("MX_VERTEX", vertex);
            NewSQL.VALUES("FK_MX_GRAPH_ID", mxGraphModelId);
            sqlStr = NewSQL.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * 修改mxCell
     *
     * @param mxCell
     * @return
     */
    public String updateMxCell(MxCell mxCell) {
        String sqlStr = "";
        this.preventSQLInjectionMxCell(mxCell);
        if (null != lastUpdateUser) {
            SQL newSQL = new SQL();
            // UPDATE括号中为数据库表名
            newSQL.UPDATE("mx_cell");
            // set中的第一个字符串为数据库中表对应的字段名

            newSQL.SET("LAST_UPDATE_DTTM = " + lastUpdateDttmStr);
            newSQL.SET("LAST_UPDATE_USER = " + lastUpdateUser);
            newSQL.SET("VERSION = " + (version + 1));

            // 处理其他字段
            newSQL.SET("ENABLE_FLAG = " + enableFlag);
            newSQL.SET("MX_PAGEID = " + pageId);
            newSQL.SET("MX_PARENT = " + parent);
            newSQL.SET("MX_STYLE = " + style);
            newSQL.SET("MX_EDGE = " + edge);
            newSQL.SET("MX_SOURCE = " + source);
            newSQL.SET("MX_TARGET = " + target);
            newSQL.SET("MX_VALUE = " + value);
            newSQL.SET("MX_VERTEX = " + vertex);
            if (null != mxGraphModelId) {
                newSQL.SET("FK_MX_GRAPH_ID = " + mxGraphModelId);
            }
            newSQL.WHERE("VERSION = " + version);
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
     * 根据mxGraphId查询MxCell的list
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
     * 根据Id查询MxCell
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
     * 根据id逻辑删除,设为无效
     *
     * @param id
     * @return
     */
    public String updateEnableFlagById(String id) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(id)) {
            SQL sql = new SQL();
            sql.UPDATE("mx_cell");
            sql.SET("ENABLE_FLAG = 0");
            sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
            sql.SET("last_update_dttm = " + SqlUtils.addSqlStr(DateUtils.dateTimesToStr(new Date())));
            sql.WHERE("ENABLE_FLAG = 1");
            sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

}
