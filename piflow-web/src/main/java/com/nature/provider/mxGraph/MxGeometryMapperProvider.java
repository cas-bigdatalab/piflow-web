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

    /**
     * 新增MxGeometry
     *
     * @param mxGeometry
     * @return
     */
    public String addMxGeometry(MxGeometry mxGeometry) {
        String sqlStr = "";
        if (null != mxGeometry) {
            String id = mxGeometry.getId();
            String crtUser = mxGeometry.getCrtUser();
            Date crtDttm = mxGeometry.getCrtDttm();
            Boolean enableFlag = mxGeometry.getEnableFlag();
            Date lastUpdateDttm = mxGeometry.getLastUpdateDttm();
            String lastUpdateUser = mxGeometry.getLastUpdateUser();
            Long version = mxGeometry.getVersion();
            String as = mxGeometry.getAs();
            String relative = mxGeometry.getRelative();
            String height = mxGeometry.getHeight();
            String width = mxGeometry.getWidth();
            String x = mxGeometry.getX();
            String y = mxGeometry.getY();
            MxCell mxCell = mxGeometry.getMxCell();
            SQL sql = new SQL();

            // INSERT_INTO括号中为数据库表名
            sql.INSERT_INTO("mx_geometry");
            // value中的第一个字符串为数据库中表对应的字段名
            // 除数字类型的字段外其他类型必须加单引号

            //先处理修改必填字段
            if (null == crtDttm) {
                crtDttm = new Date();
            }
            if (StringUtils.isBlank(crtUser)) {
                crtUser = "-1";
            }
            if (null == lastUpdateDttm) {
                lastUpdateDttm = new Date();
            }
            if (StringUtils.isBlank(lastUpdateUser)) {
                lastUpdateUser = "-1";
            }
            if (null == enableFlag) {
                enableFlag = true;
            }
            if (null == version) {
                version = 0L;
            }
            sql.VALUES("ID", SqlUtils.addSqlStr(id));
            sql.VALUES("CRT_DTTM", SqlUtils.addSqlStr(DateUtils.dateTimesToStr(crtDttm)));
            sql.VALUES("CRT_USER", SqlUtils.addSqlStr(crtUser));
            sql.VALUES("LAST_UPDATE_DTTM", SqlUtils.addSqlStr(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.VALUES("LAST_UPDATE_USER", SqlUtils.addSqlStr(lastUpdateUser));
            sql.VALUES("ENABLE_FLAG", (enableFlag ? 1 : 0) + "");
            sql.VALUES("VERSION", version + "");

            // 处理其他字段
            if (StringUtils.isNotBlank(as)) {
                sql.VALUES("MX_AS", SqlUtils.addSqlStr(as));
            }
            if (StringUtils.isNotBlank(relative)) {
                sql.VALUES("MX_RELATIVE", SqlUtils.addSqlStr(relative));
            }
            if (StringUtils.isNotBlank(height)) {
                sql.VALUES("MX_HEIGHT", SqlUtils.addSqlStr(height));
            }
            if (StringUtils.isNotBlank(width)) {
                sql.VALUES("MX_WIDTH", SqlUtils.addSqlStr(width));
            }
            if (StringUtils.isNotBlank(x)) {
                sql.VALUES("MX_X", SqlUtils.addSqlStr(x));
            }
            if (StringUtils.isNotBlank(y)) {
                sql.VALUES("MX_Y", SqlUtils.addSqlStr(y));
            }
            if (null != mxCell) {
                sql.VALUES("FK_MX_CELL_ID", SqlUtils.addSqlStr(mxCell.getId()));
            }
            sqlStr = sql.toString();
        }
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
        if (null != mxGeometry) {
            String id = mxGeometry.getId();
            Boolean enableFlag = mxGeometry.getEnableFlag();
            Date lastUpdateDttm = mxGeometry.getLastUpdateDttm();
            String lastUpdateUser = mxGeometry.getLastUpdateUser();
            Long version = mxGeometry.getVersion();
            String as = mxGeometry.getAs();
            String relative = mxGeometry.getRelative();
            String height = mxGeometry.getHeight();
            String width = mxGeometry.getWidth();
            String x = mxGeometry.getX();
            String y = mxGeometry.getY();
            MxCell mxCell = mxGeometry.getMxCell();
            SQL sql = new SQL();
            // UPDATE括号中为数据库表名
            sql.UPDATE("mx_geometry");
            // SET中的第一个字符串为数据库中表对应的字段名
            // 除数字类型的字段外其他类型必须加单引号

            //先处理修改必填字段
            if (null == lastUpdateDttm) {
                lastUpdateDttm = new Date();
            }
            if (StringUtils.isBlank(lastUpdateUser)) {
                lastUpdateUser = "-1";
            }
            if (null == version) {
                version = 0L;
            }
            String lastUpdateDttmStr = DateUtils.dateTimesToStr(lastUpdateDttm);
            sql.SET("LAST_UPDATE_DTTM = " + SqlUtils.addSqlStr(lastUpdateDttmStr));
            sql.SET("LAST_UPDATE_USER = " + SqlUtils.addSqlStr(lastUpdateUser));
            sql.SET("VERSION = " + (version + 1));

            // 处理其他字段
            if (null != enableFlag) {
                sql.SET("ENABLE_FLAG = " + (enableFlag ? 1 : 0));
            }
            if (StringUtils.isNotBlank(as)) {
                sql.SET("MX_AS = " + SqlUtils.addSqlStr(as));
            }
            if (StringUtils.isNotBlank(relative)) {
                sql.SET("MX_RELATIVE = " + SqlUtils.addSqlStr(relative));
            }
            if (StringUtils.isNotBlank(height)) {
                sql.SET("MX_HEIGHT = " + SqlUtils.addSqlStr(height));
            }
            if (StringUtils.isNotBlank(width)) {
                sql.SET("MX_WIDTH = " + SqlUtils.addSqlStr(width));
            }
            if (StringUtils.isNotBlank(x)) {
                sql.SET("MX_X = " + SqlUtils.addSqlStr(x));
            }
            if (StringUtils.isNotBlank(y)) {
                sql.SET("MX_Y = " + SqlUtils.addSqlStr(y));
            }
            if (null != mxCell) {
                sql.SET("FK_MX_CELL_ID = " + SqlUtils.addSqlStr(mxCell.getId()));
            }
            sql.WHERE("VERSION = " + version);
            sql.WHERE("ID = " + SqlUtils.addSqlStr(id));

            sqlStr = sql.toString();

            if (StringUtils.isBlank(id)) {
                sqlStr = "";
            }
        }
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
            sql.WHERE("id = " + SqlUtils.addSqlStr(id));
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
            sql.WHERE("FK_MX_CELL_ID = " + SqlUtils.addSqlStr(flowId));
            sql.WHERE("ENABLE_FLAG = 1");
            sqlStr = sql.toString();
        }

        return sqlStr;
    }

    /**
     * 根据id逻辑删除,设为无效
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
             sql.SET("last_update_user = " + SqlUtils.addSqlStr(username) );
             sql.SET("last_update_dttm = " + SqlUtils.addSqlStr(DateUtils.dateTimesToStr(new Date())) );
             sql.WHERE("ENABLE_FLAG = 1");
             sql.WHERE("id = " + SqlUtils.addSqlStrAndReplace(id));

             sqlStr = sql.toString();
         }
         return sqlStr;
     }
    
}
