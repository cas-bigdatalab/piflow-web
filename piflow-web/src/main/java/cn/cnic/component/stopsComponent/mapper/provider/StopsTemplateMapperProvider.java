package cn.cnic.component.stopsComponent.mapper.provider;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.stopsComponent.model.StopsTemplate;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class StopsTemplateMapperProvider {

    private String id;
    private String crtUser;
    private String crtDttmStr;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
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

    private void preventSQLInjectionStops(StopsTemplate stopsTemplate) {
        if (null != stopsTemplate && StringUtils.isNotBlank(stopsTemplate.getLastUpdateUser())) {
            // Mandatory Field
            String id = stopsTemplate.getId();
            String crtUser = stopsTemplate.getCrtUser();
            String lastUpdateUser = stopsTemplate.getLastUpdateUser();
            Boolean enableFlag = stopsTemplate.getEnableFlag();
            Long version = stopsTemplate.getVersion();
            Date crtDttm = stopsTemplate.getCrtDttm();
            Date lastUpdateDttm = stopsTemplate.getLastUpdateDttm();
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
            this.bundel = SqlUtils.preventSQLInjection(stopsTemplate.getBundel());
            this.description = SqlUtils.preventSQLInjection(stopsTemplate.getDescription());
            this.groups = SqlUtils.preventSQLInjection(stopsTemplate.getGroups());
            this.name = SqlUtils.preventSQLInjection(stopsTemplate.getName());
            this.inports = SqlUtils.preventSQLInjection(stopsTemplate.getInports());
            this.inPortType = SqlUtils.preventSQLInjection(null != stopsTemplate.getInPortType() ? stopsTemplate.getInPortType().name() : null);
            this.outports = SqlUtils.preventSQLInjection(stopsTemplate.getOutports());
            this.outPortType = SqlUtils.preventSQLInjection(null != stopsTemplate.getOutPortType() ? stopsTemplate.getOutPortType().name() : null);
            this.owner = SqlUtils.preventSQLInjection(stopsTemplate.getOwner());
            this.isCustomized = ((null != stopsTemplate.getIsCustomized() && stopsTemplate.getIsCustomized()) ? 1 : 0);
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
    public String getStopsTemplateList() {
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
    public String getStopsTemplateById(String id) {
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
     * Query StopsTemplate according to stopsName
     *
     * @param stopsName
     * @return
     */
    public String getStopsTemplateByName(String stopsName) {
        String sqlStr = "select ''";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_stops_template");
        sql.WHERE("enable_flag = 1 ");
        sql.WHERE("name = " + SqlUtils.addSqlStr(stopsName));
        sqlStr = sql.toString();
        return sqlStr;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public String insertStopsTemplate(Map map) {
        String sqlStr = "select ''";
        List<StopsTemplate> stopsTemplateList = (List<StopsTemplate>) map.get("stopsTemplateList");
        if (null != stopsTemplateList && stopsTemplateList.size() > 0) {
            SQL sqlColumns = new SQL();
            sqlColumns.INSERT_INTO("flow_stops_template");
            sqlColumns.INTO_COLUMNS(
                    "id",
                    "crt_dttm",
                    "crt_user",
                    "enable_flag",
                    "last_update_dttm",
                    "last_update_user",
                    "version",
                    "bundel",
                    "description",
                    "groups",
                    "name",
                    "owner",
                    "inports",
                    "in_port_type",
                    "outports",
                    "out_port_type",
                    "is_customized"
            );
            StringBuffer sqlValuesStr = new StringBuffer();
            sqlValuesStr.append("\nvalues\n");
            for (int i = 0; i < stopsTemplateList.size(); i++) {
                StopsTemplate stopsTemplate = stopsTemplateList.get(i);
                if (null != stopsTemplate) {
                    this.preventSQLInjectionStops(stopsTemplate);
                    sqlValuesStr.append("(");
                    //Process the required fields first
                    if (null == crtDttmStr) {
                        String crtDttm = DateUtils.dateTimesToStr(new Date());
                        crtDttmStr = SqlUtils.preventSQLInjection(crtDttm);
                    }
                    if (StringUtils.isBlank(crtUser)) {
                        crtUser = SqlUtils.preventSQLInjection("-1");
                    }
                    sqlValuesStr.append(id + ",");
                    sqlValuesStr.append(crtDttmStr + ",");
                    sqlValuesStr.append(crtUser + ",");
                    sqlValuesStr.append(enableFlag + ",");
                    sqlValuesStr.append(lastUpdateDttmStr + ",");
                    sqlValuesStr.append(lastUpdateUser + ",");
                    sqlValuesStr.append(version + ",");
                    sqlValuesStr.append(bundel + ",");
                    sqlValuesStr.append(description + ",");
                    sqlValuesStr.append(groups + ",");
                    sqlValuesStr.append(name + ",");
                    sqlValuesStr.append(owner + ",");
                    sqlValuesStr.append(inports + ",");
                    sqlValuesStr.append(inPortType + ",");
                    sqlValuesStr.append(outports + ",");
                    sqlValuesStr.append(outPortType + ",");
                    sqlValuesStr.append(isCustomized);
                    sqlValuesStr.append(")");
                    if (i < stopsTemplateList.size() - 1) {
                        sqlValuesStr.append(",\n");
                    }
                    this.reset();
                }
            }
            sqlStr = sqlColumns.toString() + sqlValuesStr.toString() + ";";
        }
        return sqlStr;
    }

}
