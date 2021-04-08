package cn.cnic.component.mxGraph.mapper.provider;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.mxGraph.entity.MxGeometry;
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
     * add MxGeometry
     *
     * @param mxGeometry
     * @return
     */
    public String addMxGeometry(MxGeometry mxGeometry) {
        String sqlStr = "";
        this.preventSQLInjectionMxGeometry(mxGeometry);
        if (null != mxGeometry) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.INSERT_INTO("mx_geometry");
            // The first string in the value is the field name corresponding to the table in the database.
            // all types except numeric fields must be enclosed in single quotes

            //Process the required fields first
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
            sql.VALUES("enable_flag", enableFlag + "");
            sql.VALUES("version", version + "");

            // handle other fields
            sql.VALUES("mx_as", as);
            sql.VALUES("mx_relative", relative);
            sql.VALUES("mx_height", height);
            sql.VALUES("mx_width", width);
            sql.VALUES("mx_x", x);
            sql.VALUES("mx_y", y);
            sql.VALUES("fk_mx_cell_id", mxCellId);
            sqlStr = sql.toString();
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
        String sqlStr = "";
        this.preventSQLInjectionMxGeometry(mxGeometry);
        if (null != mxGeometry) {
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
     * Query MxGeometry based on flowId
     *
     * @param flowId
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
        sql.UPDATE("mx_geometry");
        sql.SET("enable_flag = 0");
        sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
        sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
        sql.WHERE("enable_flag = 1");
        sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));

        return sql.toString();
    }

}
