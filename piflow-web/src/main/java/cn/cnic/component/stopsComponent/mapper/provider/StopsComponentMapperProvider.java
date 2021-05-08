package cn.cnic.component.stopsComponent.mapper.provider;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.stopsComponent.model.StopsComponent;

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
        if(flag) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO flow_stops_template ");

            strBuf.append("( ");
            strBuf.append(SqlUtils.baseFieldName() + ", ");
            strBuf.append("bundel, description, groups, name, owner, inports, in_port_type, outports, out_port_type, is_customized, visualization_type ");
            strBuf.append(") ");

            strBuf.append("values ");
            strBuf.append("(");
            strBuf.append(SqlUtils.baseFieldValues(stopsComponent) + ", ");
            strBuf.append(bundel + "," + description + "," + groups + "," + name + "," + owner + "," + inports + "," + inPortType + "," + outports + "," + outPortType + "," + isCustomized + "," + visualizationType);
            strBuf.append(")");
            sqlStr = strBuf.toString() + ";";
        }
        this.reset();
        return sqlStr;
    }

}
