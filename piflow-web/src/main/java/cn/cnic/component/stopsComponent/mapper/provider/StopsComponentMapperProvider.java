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

    private void preventSQLInjectionStops(StopsComponent stopsComponent) {
        if (null != stopsComponent && StringUtils.isNotBlank(stopsComponent.getLastUpdateUser())) {
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
        }
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
        String sqlStr = "select 0";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_stops_template");
        sql.WHERE("enable_flag = 1 ");
        sql.WHERE("id = " + SqlUtils.addSqlStr(id));
        sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * Query StopsComponent according to stopsName
     *
     * @param stopsName
     * @return
     */
    public String getStopsComponentByName(String stopsName) {
        String sqlStr = "select ''";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_stops_template");
        sql.WHERE("enable_flag = 1 ");
        sql.WHERE("name = " + SqlUtils.addSqlStr(stopsName));
        sqlStr = sql.toString();
        return sqlStr;
    }

    public String insertStopsComponent(StopsComponent stopsComponent) {
        if (null == stopsComponent) {
            return "select 0";
        }
        this.preventSQLInjectionStops(stopsComponent);

        StringBuffer strBuf = new StringBuffer();
        strBuf.append("INSERT INTO flow_stops_template ");

        strBuf.append("( ");
        strBuf.append(SqlUtils.baseFieldName() + ", ");
        strBuf.append("bundel, description, groups, name, owner, inports, in_port_type, outports, out_port_type, is_customized ");
        strBuf.append(") ");

        strBuf.append("values ");
        strBuf.append("(");
        strBuf.append(SqlUtils.baseFieldValues(stopsComponent) + ", ");
        strBuf.append(bundel + "," + description + "," + groups + "," + name + "," + owner + "," + inports + "," + inPortType + "," + outports + "," + outPortType + "," + isCustomized);
        strBuf.append(")");
        this.reset();
        return strBuf.toString() + ";";
    }

}
