package com.nature.provider.mxGraph;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.Flow;
import com.nature.component.mxGraph.model.MxGraphModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class MxGraphModelProvider {

    public String addMxGraphModel(MxGraphModel mxGraphModel) {
        String sqlStr = "select 0";
        if (null != mxGraphModel) {
            String id = mxGraphModel.getId();
            String crtUser = mxGraphModel.getCrtUser();
            Date crtDttm = mxGraphModel.getCrtDttm();
            Date lastUpdateDttm = mxGraphModel.getLastUpdateDttm();
            String lastUpdateUser = mxGraphModel.getLastUpdateUser();
            Boolean enableFlag = mxGraphModel.getEnableFlag();
            Long version = mxGraphModel.getVersion();
            String dx = mxGraphModel.getDx();
            String dy = mxGraphModel.getDy();
            String grid = mxGraphModel.getGrid();
            String gridSize = mxGraphModel.getGridSize();
            String guides = mxGraphModel.getGuides();
            String tooltips = mxGraphModel.getTooltips();
            String connect = mxGraphModel.getConnect();
            String arrows = mxGraphModel.getArrows();
            String fold = mxGraphModel.getFold();
            String page = mxGraphModel.getPage();
            String pageScale = mxGraphModel.getPageScale();
            String pageWidth = mxGraphModel.getPageWidth();
            String pageHeight = mxGraphModel.getPageHeight();
            String background = mxGraphModel.getBackground();
            Flow flow = mxGraphModel.getFlow();
            SQL sql = new SQL();

            // INSERT_INTO括号中为数据库表名
            sql.INSERT_INTO("mx_graph_model");
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
            if (null == version) {
                version = 0L;
            }
            if (null == enableFlag) {
                enableFlag = true;
            }
            sql.VALUES("ID", SqlUtils.addSqlStr(id));
            sql.VALUES("CRT_DTTM", SqlUtils.addSqlStr(DateUtils.dateTimesToStr(crtDttm)));
            sql.VALUES("CRT_USER", SqlUtils.addSqlStr(crtUser));
            sql.VALUES("LAST_UPDATE_DTTM", SqlUtils.addSqlStr(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.VALUES("LAST_UPDATE_USER", SqlUtils.addSqlStr(lastUpdateUser));
            sql.VALUES("ENABLE_FLAG", (enableFlag ? 1 : 0) + "");
            sql.VALUES("VERSION", version + "");

            // 处理其他字段
            if (StringUtils.isNotBlank(dx)) {
                sql.VALUES("MX_DX", SqlUtils.addSqlStr(dx));
            }
            if (StringUtils.isNotBlank(dy)) {
                sql.VALUES("MX_DY", SqlUtils.addSqlStr(dy));
            }
            if (StringUtils.isNotBlank(grid)) {
                sql.VALUES("MX_GRID", SqlUtils.addSqlStr(grid));
            }
            if (StringUtils.isNotBlank(gridSize)) {
                sql.VALUES("MX_GRIDSIZE", SqlUtils.addSqlStr(gridSize));
            }
            if (StringUtils.isNotBlank(guides)) {
                sql.VALUES("MX_GUIDES", SqlUtils.addSqlStr(guides));
            }
            if (StringUtils.isNotBlank(tooltips)) {
                sql.VALUES("MX_TOOLTIPS", SqlUtils.addSqlStr(tooltips));
            }
            if (StringUtils.isNotBlank(connect)) {
                sql.VALUES("MX_CONNECT", SqlUtils.addSqlStr(connect));
            }
            if (StringUtils.isNotBlank(arrows)) {
                sql.VALUES("MX_ARROWS", SqlUtils.addSqlStr(arrows));
            }
            if (StringUtils.isNotBlank(fold)) {
                sql.VALUES("MX_FOLD", SqlUtils.addSqlStr(fold));
            }
            if (StringUtils.isNotBlank(page)) {
                sql.VALUES("MX_PAGE", SqlUtils.addSqlStr(page));
            }
            if (StringUtils.isNotBlank(pageScale)) {
                sql.VALUES("MX_PAGESCALE", SqlUtils.addSqlStr(pageScale));
            }
            if (StringUtils.isNotBlank(pageWidth)) {
                sql.VALUES("MX_PAGEWIDTH", SqlUtils.addSqlStr(pageWidth));
            }
            if (StringUtils.isNotBlank(pageHeight)) {
                sql.VALUES("MX_PAGEHEIGHT", SqlUtils.addSqlStr(pageHeight));
            }
            if (StringUtils.isNotBlank(background)) {
                sql.VALUES("MX_BACKGROUND", SqlUtils.addSqlStr(background));
            }
            if (null != flow) {
                sql.VALUES("FK_FLOW_ID", SqlUtils.addSqlStr(flow.getId()));
            }

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    public String updateMxGraphModel(MxGraphModel mxGraphModel) {
        String sqlStr = "";
        if (null != mxGraphModel) {
            String id = mxGraphModel.getId();
            if (StringUtils.isNotBlank(id)) {
                Date lastUpdateDttm = mxGraphModel.getLastUpdateDttm();
                String lastUpdateUser = mxGraphModel.getLastUpdateUser();
                Boolean enableFlag = mxGraphModel.getEnableFlag();
                Long version = mxGraphModel.getVersion();
                String dx = mxGraphModel.getDx();
                String dy = mxGraphModel.getDy();
                String grid = mxGraphModel.getGrid();
                String gridSize = mxGraphModel.getGridSize();
                String guides = mxGraphModel.getGuides();
                String tooltips = mxGraphModel.getTooltips();
                String connect = mxGraphModel.getConnect();
                String arrows = mxGraphModel.getArrows();
                String fold = mxGraphModel.getFold();
                String page = mxGraphModel.getPage();
                String pageScale = mxGraphModel.getPageScale();
                String pageWidth = mxGraphModel.getPageWidth();
                String pageHeight = mxGraphModel.getPageHeight();
                String background = mxGraphModel.getBackground();
                Flow flow = mxGraphModel.getFlow();
                SQL sql = new SQL();

                // INSERT_INTO括号中为数据库表名
                sql.UPDATE("mx_graph_model");
                // set中的第一个字符串为数据库中表对应的字段名
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
                if (StringUtils.isNotBlank(dx)) {
                    sql.SET("MX_DX = " + SqlUtils.addSqlStr(dx));
                }
                if (StringUtils.isNotBlank(dy)) {
                    sql.SET("MX_DY = " + SqlUtils.addSqlStr(dy));
                }
                if (StringUtils.isNotBlank(grid)) {
                    sql.SET("MX_GRID = " + SqlUtils.addSqlStr(grid));
                }
                if (StringUtils.isNotBlank(gridSize)) {
                    sql.SET("MX_GRIDSIZE = " + SqlUtils.addSqlStr(gridSize));
                }
                if (StringUtils.isNotBlank(guides)) {
                    sql.SET("MX_GUIDES = " + SqlUtils.addSqlStr(guides));
                }
                if (StringUtils.isNotBlank(tooltips)) {
                    sql.SET("MX_TOOLTIPS = " + SqlUtils.addSqlStr(tooltips));
                }
                if (StringUtils.isNotBlank(connect)) {
                    sql.SET("MX_CONNECT = " + SqlUtils.addSqlStr(connect));
                }
                if (StringUtils.isNotBlank(arrows)) {
                    sql.SET("MX_ARROWS = " + SqlUtils.addSqlStr(arrows));
                }
                if (StringUtils.isNotBlank(fold)) {
                    sql.SET("MX_FOLD = " + SqlUtils.addSqlStr(fold));
                }
                if (StringUtils.isNotBlank(page)) {
                    sql.SET("MX_PAGE = " + SqlUtils.addSqlStr(page));
                }
                if (StringUtils.isNotBlank(pageScale)) {
                    sql.SET("MX_PAGESCALE = " + SqlUtils.addSqlStr(pageScale));
                }
                if (StringUtils.isNotBlank(pageWidth)) {
                    sql.SET("MX_PAGEWIDTH = " + SqlUtils.addSqlStr(pageWidth));
                }
                if (StringUtils.isNotBlank(pageHeight)) {
                    sql.SET("MX_PAGEHEIGHT = " + SqlUtils.addSqlStr(pageHeight));
                }
                if (StringUtils.isNotBlank(background)) {
                    sql.SET("MX_BACKGROUND = " + SqlUtils.addSqlStr(background));
                }
                if (null != flow) {
                    sql.SET("FK_FLOW_ID = " + SqlUtils.addSqlStr(flow.getId()));
                }
                sql.WHERE("VERSION = " + version);
                sql.WHERE("id = " + SqlUtils.addSqlStr(id));
                sqlStr = sql.toString();
                if (StringUtils.isBlank(id)) {
                    sqlStr = "";
                }
            }
        }
        return sqlStr;
    }

    /**
     * 根据id查询mxGraphModel
     *
     * @param id
     * @return
     */
    public String getMxGraphModelById(String id) {
        String sqlStr = "select * from mx_graph_model where id = #{id}";
        if (StringUtils.isNotBlank(id)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("mx_graph_model");
            sql.WHERE("id = " + SqlUtils.addSqlStr(id));
            sql.WHERE("enable_flag = 1");
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * 根据flowId查询mxGraphModel
     *
     * @param flowId
     * @return
     */
    public String getMxGraphModelByFlowId(String flowId) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(flowId)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("mx_graph_model");
            sql.WHERE("fk_flow_id = " + SqlUtils.addSqlStr(flowId));
            sql.WHERE("enable_flag = 1");
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    
    /**
     * 根据flowId逻辑删除,设为无效
     * @param flowId
     * @return
     */
    public String updateEnableFlagByFlowId(String flowId) {
      	 UserVo user = SessionUserUtil.getCurrentUser();
           String username = (null != user) ? user.getUsername() : "-1";
           String sqlStr = "select 0";
          if (StringUtils.isNotBlank(flowId)) {
              SQL sql = new SQL();
              sql.UPDATE("mx_graph_model");
              sql.SET("ENABLE_FLAG = 0");
              sql.SET("last_update_user = " + SqlUtils.addSqlStr(username) );
              sql.SET("last_update_dttm = " + SqlUtils.addSqlStr(DateUtils.dateTimesToStr(new Date())) );
              sql.WHERE("ENABLE_FLAG = 1");
              sql.WHERE("fk_flow_id = " + SqlUtils.addSqlStrAndReplace(flowId));

              sqlStr = sql.toString();
          }
          return sqlStr;
      }

}
