package cn.cnic.component.mxGraph.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.mxGraph.entity.MxGeometry;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class MxGeometryMapperProvider {

    private String id;
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

    private boolean preventSQLInjectionMxGeometry(MxGeometry mxGeometry) {
        if (null == mxGeometry || StringUtils.isBlank(mxGeometry.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        Boolean enableFlag = mxGeometry.getEnableFlag();
        Long version = mxGeometry.getVersion();
        Date lastUpdateDttm = mxGeometry.getLastUpdateDttm();
        this.id = SqlUtils.preventSQLInjection(mxGeometry.getId());
        this.lastUpdateUser = SqlUtils.preventSQLInjection(mxGeometry.getLastUpdateUser());
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
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
        return true;
    }

    private void reset() {
        this.id = null;
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
     * add MxGeometry
     *
     * @param mxGeometry
     * @return
     */
    public String addMxGeometry(MxGeometry mxGeometry) {
        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionMxGeometry(mxGeometry);
        if (flag) {
            StringBuffer strBuf = new StringBuffer();
            // INSERT_INTO brackets is table name
            strBuf.append("INSERT INTO mx_geometry ");
            strBuf.append("( ");
            strBuf.append(SqlUtils.baseFieldName() + ", ");
            strBuf.append("mx_as, ");
            strBuf.append("mx_relative, ");
            strBuf.append("mx_height, ");
            strBuf.append("mx_width, ");
            strBuf.append("mx_x, ");
            strBuf.append("mx_y, ");
            strBuf.append("fk_mx_cell_id ");
            strBuf.append(") VALUES ( ");
            strBuf.append(SqlUtils.baseFieldValues(mxGeometry) + ", ");
            strBuf.append(this.as + ", ");
            strBuf.append(this.relative + ", ");
            strBuf.append(this.height + ", ");
            strBuf.append(this.width + ", ");
            strBuf.append(this.x + ", ");
            strBuf.append(this.y + ", ");
            strBuf.append(this.mxCellId + " ");
            strBuf.append(") ");
            sqlStr = strBuf.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * update MxGeometry
     *
     * @param mxGeometry
     * @return
     */
    public String updateMxGeometry(MxGeometry mxGeometry) {
        String sqlStr = "SELECT 0";
        if (this.preventSQLInjectionMxGeometry(mxGeometry)) {
            SQL sql = new SQL();
            // UPDATE parentheses for the database table name
            sql.UPDATE("mx_geometry");
            // The first string in the SET is the name of the field corresponding to the table in the database
            // all types except numeric fields must be enclosed in single quotes

            //Process the required fields first
            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag = " + enableFlag);
            sql.SET("mx_as = " + as);
            sql.SET("mx_relative = " + relative);
            sql.SET("mx_height = " + height);
            sql.SET("mx_width = " + width);
            sql.SET("mx_x = " + x);
            sql.SET("mx_y = " + y);
            if (null != mxCellId) {
                sql.SET("fk_mx_cell_id = " + mxCellId);
            }
            sql.WHERE("version = " + version);
            sql.WHERE("id = " + id);
            sqlStr = sql.toString();
            if (StringUtils.isBlank(id)) {
                sqlStr = "";
            }
        }
        this.reset();
        return sqlStr;
    }

    /**
     * Query MxGeometry based on id
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
            sql.WHERE("enable_flag = 1");
            sqlStr = sql.toString();
        }

        return sqlStr;
    }

    /**
     * Query MxGeometry based on mxCellId
     *
     * @param mxCellId
     * @return
     */
    public String getMxGeometryByMxCellId(String mxCellId) {
        String sqlStr = "";
        if (StringUtils.isNotBlank(mxCellId)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("mx_geometry");
            sql.WHERE("fk_mx_cell_id = " + SqlUtils.preventSQLInjection(mxCellId));
            sql.WHERE("enable_flag = 1");
            sqlStr = sql.toString();
        }

        return sqlStr;
    }

    /**
     * delete 'MxGeometry' by 'mxCellId'
     *
     * @param username
     * @param mxCellId
     * @return
     */
    public String deleteMxGeometryByFlowId(String username, String mxCellId) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(mxCellId)) {
            return "SELECT 0";
        }
        SQL sql = new SQL();
        sql.UPDATE("mx_graph_model");
        sql.SET("enable_flag = 0");
        sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
        sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
        sql.WHERE("enable_flag = 1");
        sql.WHERE("fk_mx_cell_id = " + SqlUtils.preventSQLInjection(mxCellId));

        return sql.toString();
    }

}
