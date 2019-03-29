package com.nature.provider.mxGraph;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class MxGeometryMapperProvider {

    private String id;
    private String crtUser;
    private String crtDttmStr;
    private int enableFlag;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private Long version;
    private String as;
    private String relative;
    private String height;
    private String width;
    private String x;
    private String y;
    private String mxCellId;

    private void preventSQLInjectionMxGeometry(MxGeometry mxGeometry) {
        if (null != mxGeometry && StringUtils.isNotBlank(mxGeometry.getLastUpdateUser())) {
            // Mandatory Field
            String id = mxGeometry.getId();
            String crtUser = mxGeometry.getCrtUser();
            String lastUpdateUser = mxGeometry.getLastUpdateUser();
            Boolean enableFlag = mxGeometry.getEnableFlag();
            Long version = mxGeometry.getVersion();
            Date crtDttm = mxGeometry.getCrtDttm();
            Date lastUpdateDttm = mxGeometry.getLastUpdateDttm();
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
            this.as = SqlUtils.preventSQLInjection(mxGeometry.getAs());
            this.relative = SqlUtils.preventSQLInjection(mxGeometry.getRelative());
            this.height = SqlUtils.preventSQLInjection(mxGeometry.getHeight());
            this.width = SqlUtils.preventSQLInjection(mxGeometry.getWidth());
            this.x = SqlUtils.preventSQLInjection(mxGeometry.getX());
            this.y = SqlUtils.preventSQLInjection(mxGeometry.getY());
            String mxCellIdStr = (null != mxGeometry.getMxCell() ? mxGeometry.getMxCell().getId() : null);
            this.mxCellId = (null != mxCellIdStr ? SqlUtils.preventSQLInjection(mxCellIdStr) : null);
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
        this.as = null;
        this.relative = null;
        this.height = null;
        this.width = null;
        this.x = null;
        this.y = null;
        this.mxCellId = null;
    }

    /**
     * 新增MxGeometry
     *
     * @param mxGeometry
     * @return
     */
    public String addMxGeometry(MxGeometry mxGeometry) {
        String sqlStr = "";
        this.preventSQLInjectionMxGeometry(mxGeometry);
        if (null != mxGeometry) {
            SQL sql = new SQL();

            // INSERT_INTO括号中为数据库表名
            sql.INSERT_INTO("mx_geometry");
            // value中的第一个字符串为数据库中表对应的字段名
            // 除数字类型的字段外其他类型必须加单引号

            //先处理修改必填字段
            if (null == crtDttmStr) {
                String crtDttm = DateUtils.dateTimesToStr(new Date());
                crtDttmStr = SqlUtils.preventSQLInjection(crtDttm);
            }
            if (StringUtils.isBlank(crtUser)) {
                crtUser = SqlUtils.preventSQLInjection("-1");
            }

            sql.VALUES("ID", id);
            sql.VALUES("CRT_DTTM", crtDttmStr);
            sql.VALUES("CRT_USER", crtUser);
            sql.VALUES("LAST_UPDATE_DTTM", lastUpdateDttmStr);
            sql.VALUES("LAST_UPDATE_USER", lastUpdateUser);
            sql.VALUES("ENABLE_FLAG", enableFlag + "");
            sql.VALUES("VERSION", version + "");

            // 处理其他字段
            sql.VALUES("MX_AS", as);
            sql.VALUES("MX_RELATIVE", relative);
            sql.VALUES("MX_HEIGHT", height);
            sql.VALUES("MX_WIDTH", width);
            sql.VALUES("MX_X", x);
            sql.VALUES("MX_Y", y);
            sql.VALUES("FK_MX_CELL_ID", mxCellId);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * 修改MxGeometry
     *
     * @param mxGeometry
     * @return
     */
    public String updateMxGeometry(MxGeometry mxGeometry) {
        String sqlStr = "";
        this.preventSQLInjectionMxGeometry(mxGeometry);
        if (null != mxGeometry) {
            SQL sql = new SQL();
            // UPDATE括号中为数据库表名
            sql.UPDATE("mx_geometry");
            // SET中的第一个字符串为数据库中表对应的字段名
            // 除数字类型的字段外其他类型必须加单引号

            //先处理修改必填字段
            sql.SET("LAST_UPDATE_DTTM = " + lastUpdateDttmStr);
            sql.SET("LAST_UPDATE_USER = " + lastUpdateUser);
            sql.SET("VERSION = " + (version + 1));

            // 处理其他字段
            sql.SET("ENABLE_FLAG = " + enableFlag);
            sql.SET("MX_AS = " + as);
            sql.SET("MX_RELATIVE = " + relative);
            sql.SET("MX_HEIGHT = " + height);
            sql.SET("MX_WIDTH = " + width);
            sql.SET("MX_X = " + x);
            sql.SET("MX_Y = " + y);
            if (null != mxCellId) {
                sql.SET("FK_MX_CELL_ID = " + mxCellId);
            }
            sql.WHERE("VERSION = " + version);
            sql.WHERE("ID = " + id);
            sqlStr = sql.toString();
            if (StringUtils.isBlank(id)) {
                sqlStr = "";
            }
        }
        this.reset();
        return sqlStr;
    }

    /**
     * 根据id查询MxGeometry
     *
     * @param id
     * @return
     */
    public String getMxGeometryById(String id) {
        String sqlStr = "";
        if (StringUtils.isNotBlank(id)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("mx_geometry");
            sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));
            sql.WHERE("ENABLE_FLAG = 1");
            sqlStr = sql.toString();
        }

        return sqlStr;
    }

    /**
     * 根据flowId查询MxGeometry
     *
     * @param flowId
     * @return
     */
    public String getMxGeometryByFlowId(String flowId) {
        String sqlStr = "";
        if (StringUtils.isNotBlank(flowId)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("mx_geometry");
            sql.WHERE("FK_MX_CELL_ID = " + SqlUtils.preventSQLInjection(flowId));
            sql.WHERE("ENABLE_FLAG = 1");
            sqlStr = sql.toString();
        }

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
            sql.UPDATE("mx_geometry");
            sql.SET("ENABLE_FLAG = 0");
            sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
            sql.WHERE("ENABLE_FLAG = 1");
            sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

}
