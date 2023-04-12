package cn.cnic.component.stopsComponent.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.stopsComponent.entity.StopsComponent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StopsComponentMapperProvider {

    private String bundel;
    private String description;
    private String groups;
    private String name;
    private String owner;
    private String inports;
    private String inPortType;
    private String outports;
    private String outPortType;
    private int isCustomized;
    private String visualizationType;
    private int isDataSource;
    private String imageUrl;
    private String componentType;    //PYTHON/SCALA
    private String dockerImagesName; //python component
    private String stopsHubId;

    private boolean preventSQLInjectionStops(StopsComponent stopsComponent) {
        if (null == stopsComponent || StringUtils.isBlank(stopsComponent.getLastUpdateUser())) {
            return false;
        }
        // Selection field
        this.bundel = SqlUtils.preventSQLInjection(stopsComponent.getBundel());
        this.description = SqlUtils.preventSQLInjection(stopsComponent.getDescription());
        this.groups = SqlUtils.preventSQLInjection(stopsComponent.getGroups());
        this.name = SqlUtils.preventSQLInjection(stopsComponent.getName());
        this.inports = SqlUtils.preventSQLInjection(stopsComponent.getInports());
        this.inPortType = SqlUtils.preventSQLInjection(null != stopsComponent.getInPortType() ? stopsComponent.getInPortType().name() : null);
        this.outports = SqlUtils.preventSQLInjection(stopsComponent.getOutports());
        this.outPortType = SqlUtils.preventSQLInjection(null != stopsComponent.getOutPortType() ? stopsComponent.getOutPortType().name() : null);
        this.owner = SqlUtils.preventSQLInjection(stopsComponent.getOwner());
        this.isCustomized = ((null != stopsComponent.getIsCustomized() && stopsComponent.getIsCustomized()) ? 1 : 0);
        this.visualizationType = SqlUtils.preventSQLInjection(stopsComponent.getVisualizationType());
        this.isDataSource = ((null != stopsComponent.getIsDataSource() && stopsComponent.getIsDataSource()) ? 1 : 0);
        this.imageUrl = SqlUtils.preventSQLInjection(stopsComponent.getImageUrl());
        this.componentType = SqlUtils.preventSQLInjection(null != stopsComponent.getComponentType() ? stopsComponent.getComponentType().name() : null);
        this.dockerImagesName = SqlUtils.preventSQLInjection(stopsComponent.getDockerImagesName());
        this.stopsHubId = SqlUtils.preventSQLInjection(stopsComponent.getStopsHubId());
        return true;
    }

    private void reset() {
        this.bundel = null;
        this.description = null;
        this.groups = null;
        this.name = null;
        this.inports = null;
        this.inPortType = null;
        this.outports = null;
        this.outPortType = null;
        this.owner = null;
        this.isCustomized = 0;
        this.visualizationType = null;
        this.isDataSource = 0;
        this.imageUrl = null;
        this.componentType = null;
        this.dockerImagesName = null;
        this.stopsHubId = null;
    }

    /**
     * Query all stops templates
     *
     * @return
     */
    public String getStopsComponentList() {
        String sqlStr = "select ''";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_stops_template");
        sql.WHERE("enable_flag = 1 ");
        sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * Query template based on stop template id
     *
     * @param id
     * @return
     */
    public String getStopsComponentById(String id) {
        String sqlStr = "SELECT 0";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_stops_template");
        sql.WHERE("enable_flag = 1 ");
        sql.WHERE("id = " + SqlUtils.addSqlStr(id));
        sqlStr = sql.toString();
        return sqlStr;
    }

    public String getStopsComponentListByGroupId(String groupId) {
        if (StringUtils.isBlank(groupId)) {
            return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();

        SqlUtils.preventSQLInjection(groupId);

        strBuf.append("SELECT * FROM flow_stops_template fst ");
        strBuf.append("WHERE fst.enable_flag=1 ");
        strBuf.append("AND fst.id IN ( ");
        strBuf.append("SELECT agst.stops_template_id FROM association_groups_stops_template agst WHERE agst.groups_id= ");
        strBuf.append(SqlUtils.preventSQLInjection(groupId));
        strBuf.append(") ");
        strBuf.append("AND fst.BUNDEL NOT IN ");
        strBuf.append("(");
        strBuf.append("SELECT fstm.bundle FROM flow_stops_template_manage fstm ");
        strBuf.append("WHERE fstm.enable_flag = 1 ");
        strBuf.append("AND fstm.is_show!=1 ");
        strBuf.append("AND fstm.stops_groups LIKE CONCAT('%',(SELECT fsg1.group_name FROM flow_stops_groups fsg1 WHERE fsg1.id=" + SqlUtils.preventSQLInjection(groupId) + "),'%') ");
        strBuf.append(")");
        return strBuf.toString();
    }

    public String getManageStopsComponentListByGroupId(String groupId) {
        if (StringUtils.isBlank(groupId)) {
            return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();

        SqlUtils.preventSQLInjection(groupId);

        strBuf.append("SELECT fst.*, fstm0.is_show FROM flow_stops_template fst ");
        strBuf.append("LEFT JOIN flow_stops_template_manage fstm0 ON fstm0.bundle=fst.BUNDEL ");
        strBuf.append("AND fstm0.stops_groups=(SELECT fsg0.group_name FROM flow_stops_groups fsg0 WHERE fsg0.id=" + SqlUtils.preventSQLInjection(groupId) + " ) ");
        strBuf.append("WHERE fst.enable_flag=1 ");
        strBuf.append("AND fst.id IN ( ");
        strBuf.append("SELECT agst.stops_template_id FROM association_groups_stops_template agst WHERE agst.groups_id=" + SqlUtils.preventSQLInjection(groupId) + ") ");
        return strBuf.toString();
    }

    /**
     * Query StopsComponent according to stopsName
     *
     * @param stopsName
     * @return
     */
    public String getStopsComponentByName(String stopsName) {
        String sqlStr = "SELECT 0";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_stops_template");
        sql.WHERE("enable_flag = 1 ");
        sql.WHERE("name = " + SqlUtils.addSqlStr(stopsName));
        sqlStr = sql.toString();
        return sqlStr;
    }

    public String insertStopsComponent(StopsComponent stopsComponent) {
        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionStops(stopsComponent);
        if (flag) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO flow_stops_template ");

            strBuf.append("( ");
            strBuf.append(SqlUtils.baseFieldName() + ", ");
            strBuf.append("bundel, description, `groups`, name, owner, inports, in_port_type, outports, out_port_type, is_customized, visualization_type, is_data_source,image_url,component_type,docker_images_name,stops_hub_id ");
            strBuf.append(") ");

            strBuf.append("values ");
            strBuf.append("(");
            strBuf.append(SqlUtils.baseFieldValues(stopsComponent) + ", ");
            strBuf.append(bundel + "," + description + "," + groups + "," + name + "," + owner + "," + inports + "," + inPortType + "," + outports + "," + outPortType + "," + isCustomized + "," + visualizationType + "," + isDataSource + "," + imageUrl + "," + componentType + "," + dockerImagesName + "," + stopsHubId);
            strBuf.append(")");
            sqlStr = strBuf.toString() + ";";
        }
        this.reset();
        return sqlStr;
    }

    /**
     * Query StopsComponent according to bundle
     *
     * @param bundle
     * @return
     */
    public String getStopsComponentByBundle(String bundle) {
        String sqlStr = "SELECT 0";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_stops_template");
        sql.WHERE("enable_flag = 1 ");
        sql.WHERE("bundle = " + SqlUtils.addSqlStr(bundle));
        sqlStr = sql.toString();
        return sqlStr;
    }

    public String updateStopsComponent(StopsComponent stopsComponent) {
        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionStops(stopsComponent);
        if (flag) {
            SQL sql = new SQL();
            sql.UPDATE("flow_stops_template");
            sql.SET("`groups` = " + groups);
            sql.SET("description = " + description);
            sql.SET("image_url = " + imageUrl);
            sql.SET("owner = " + owner);
            sql.SET("inports = " + inports);
            sql.SET("in_port_type = " + inPortType);
            sql.SET("outports = " + outports);
            sql.SET("out_port_type = " + outPortType);
            sql.SET("is_data_source = " + isDataSource);
            sql.SET("is_customized = " + isCustomized);
            sql.SET("visualization_type = " + visualizationType);
            String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != stopsComponent.getLastUpdateDttm() ? stopsComponent.getLastUpdateDttm() : new Date());
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(lastUpdateDttmStr));
            sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(stopsComponent.getLastUpdateUser()));
            sql.SET("image_url = " + imageUrl);
            sql.SET("version = " + (stopsComponent.getVersion() + 1));
            sql.WHERE("id = " + SqlUtils.preventSQLInjection(stopsComponent.getId()));
            sql.WHERE("version = " + stopsComponent.getVersion());
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * @Description update component type by id and type
     * @Param stopsComponents
     * @Return java.lang.String
     * @Author TY
     * @Date 12:57 2023/4/4
     **/
//    public String updateComponentTypeByIdAndType(List<StopsComponent> stopsComponents) {
//        List<String> stopsIds = new ArrayList<>();
//        StringBuilder sql = new StringBuilder("UPDATE flow_stops_template SET component_type = CASE");
//        for (StopsComponent stopsComponent : stopsComponents) {
//            stopsIds.add(stopsComponent.getId());
//            sql.append(" WHEN id = ").append(stopsComponent.getId()).append(" THEN ").append(stopsComponent.getComponentType().name());
//        }
//        sql.append(" END,version = CASE");
//        for (StopsComponent stopsComponent : stopsComponents) {
//            sql.append(" WHEN id = ").append(stopsComponent.getId()).append(" THEN ").append(stopsComponent.getVersion() + 1);
//        }
//        sql.append(" END,last_update_dttm = CASE");
//        for (StopsComponent stopsComponent : stopsComponents) {
//            sql.append(" WHEN id = ").append(stopsComponent.getId()).append(" THEN ").append(stopsComponent.getLastUpdateDttm());
//        }
//        sql.append(" END WHERE id IN (").append(SqlUtils.strListToStr(stopsIds)).append(") and enable_flag = 1");
//        return sql.toString();
//    }
    public String updateComponentTypeByIdAndType(StopsComponent stopsComponent) {

        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionStops(stopsComponent);
        if (flag) {
            SQL sql = new SQL();
            sql.UPDATE("flow_stops_template");
            sql.SET("component_type = " + componentType);
            String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != stopsComponent.getLastUpdateDttm() ? stopsComponent.getLastUpdateDttm() : new Date());
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(lastUpdateDttmStr));
            sql.SET("version = " + (stopsComponent.getVersion() + 1));
            sql.WHERE("id = " + SqlUtils.preventSQLInjection(stopsComponent.getId()));
            sql.WHERE("version = " + stopsComponent.getVersion());
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    public String getOnlyStopsComponentByBundles(String[] bundles){
        if (null == bundles || bundles.length <= 0) {
            return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from `flow_stops_template` ");
        strBuf.append("where `enable_flag` = 1 ");
        strBuf.append("and `bundel` in ( " + SqlUtils.strArrayToStr(bundles) + ") ");
        String sqlStr = strBuf.toString();
        return sqlStr;
    }
}
