package cn.cnic.component.flow.mapper.provider;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.flow.entity.Stops;
import cn.cnic.third.vo.flow.ThirdFlowInfoStopVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class StopsMapperProvider {

    private String id;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String bundel;
    private String description;
    private String groups;
    private String name;
    private String inports;
    private String inPortType;
    private String outports;
    private String outPortType;
    private String owner;
    private String pageId;
    private Integer checkpoint;
    private int isCustomized;
    private String flowId;
    private String dataSourceId;

    private boolean preventSQLInjectionStops(Stops stops) {
        if (null == stops || StringUtils.isBlank(stops.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        String id = stops.getId();
        String lastUpdateUser = stops.getLastUpdateUser();
        Boolean enableFlag = stops.getEnableFlag();
        Long version = stops.getVersion();
        Date lastUpdateDttm = stops.getLastUpdateDttm();
        this.id = SqlUtils.preventSQLInjection(id);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(lastUpdateUser);
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

        // Selection field
        this.bundel = SqlUtils.preventSQLInjection(stops.getBundel());
        this.description = SqlUtils.preventSQLInjection(stops.getDescription());
        this.groups = SqlUtils.preventSQLInjection(stops.getGroups());
        this.name = SqlUtils.preventSQLInjection(stops.getName());
        this.inports = SqlUtils.preventSQLInjection(stops.getInports());
        this.inPortType = SqlUtils.preventSQLInjection(null != stops.getInPortType() ? stops.getInPortType().name() : null);
        this.outports = SqlUtils.preventSQLInjection(stops.getOutports());
        this.outPortType = SqlUtils.preventSQLInjection(null != stops.getOutPortType() ? stops.getOutPortType().name() : null);
        this.owner = SqlUtils.preventSQLInjection(stops.getOwner());
        this.pageId = SqlUtils.preventSQLInjection(stops.getPageId());
        this.checkpoint = ((null != stops.getIsCheckpoint() && stops.getIsCheckpoint()) ? 1 : 0);
        this.isCustomized = ((null != stops.getIsCustomized() && stops.getIsCustomized()) ? 1 : 0);
        String flowIdStr = (null != stops.getFlow() ? stops.getFlow().getId() : null);
        this.flowId = (null != flowIdStr ? SqlUtils.preventSQLInjection(flowIdStr) : null);
        String dataSourceIdStr = (null != stops.getDataSource() ? stops.getDataSource().getId() : null);
        this.dataSourceId = (null != dataSourceIdStr ? SqlUtils.preventSQLInjection(dataSourceIdStr) : null);
        return true;
    }

    private void reset() {
        this.id = null;
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
        this.pageId = null;
        this.checkpoint = null;
        this.isCustomized = 0;
        this.flowId = null;
        this.dataSourceId = null;
    }

    /**
     * add Stops
     *
     * @param stops
     * @return
     */
    public String addStops(Stops stops) {
        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionStops(stops);
        if (flag) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("INSERT INTO ");
            stringBuffer.append("flow_stops ");
            stringBuffer.append("(");
            stringBuffer.append(SqlUtils.baseFieldName() + ",");
            stringBuffer.append("bundel,description,groups,name,inports,in_port_type,outports,out_port_type,owner,page_id,is_checkpoint,is_customized,fk_flow_id,fk_data_source_id");
            stringBuffer.append(") ");
            stringBuffer.append("VALUES");
            stringBuffer.append("(");
            stringBuffer.append(SqlUtils.baseFieldValues(stops) + ",");
            stringBuffer.append(this.bundel + ",");
            stringBuffer.append(this.description + ",");
            stringBuffer.append(this.groups + ",");
            stringBuffer.append(this.name + ",");
            stringBuffer.append(this.inports + ",");
            stringBuffer.append(this.inPortType + ",");
            stringBuffer.append(this.outports + ",");
            stringBuffer.append(this.outPortType + ",");
            stringBuffer.append(this.owner + ",");
            stringBuffer.append(this.pageId + ",");
            stringBuffer.append(this.checkpoint + ",");
            stringBuffer.append(this.isCustomized + ",");
            stringBuffer.append(this.flowId + ",");
            stringBuffer.append(this.dataSourceId);
            stringBuffer.append(")");
            sqlStr = stringBuffer.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * Insert list<Stops> Note that the method of spelling sql must use Map to connect Param content to key value.
     *
     * @param map (Content: The key is stopsList and the value is List<Stops>)
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String addStopsList(Map map) {
        List<Stops> stopsList = (List<Stops>) map.get("stopsList");
        StringBuffer sql = new StringBuffer();
        if (null != stopsList && stopsList.size() > 0) {
            sql.append("INSERT INTO ");
            sql.append("flow_stops ");
            sql.append("(");
            sql.append(SqlUtils.baseFieldName() + ",");            
            sql.append("bundel,");
            sql.append("description,");
            sql.append("groups,");
            sql.append("name,");
            sql.append("inports,");
            sql.append("in_port_type,");
            sql.append("outports,");
            sql.append("out_port_type,");
            sql.append("owner,");
            sql.append("page_id,");
            sql.append("is_checkpoint,");
            sql.append("is_customized,");
            sql.append("fk_flow_id,");
            sql.append("fk_data_source_id");
            sql.append(") ");
            sql.append("VALUES");
            int i = 0;
            for (Stops stops : stopsList) {
                i++;
                boolean flag = this.preventSQLInjectionStops(stops);
                if (flag) {
                    sql.append("(");
                    sql.append(SqlUtils.baseFieldValues(stops) + ",");
                    // handle other fields
                    sql.append(bundel + ",");
                    sql.append(description + ",");
                    sql.append(groups + ",");
                    sql.append(name + ",");
                    sql.append(inports + ",");
                    sql.append(inPortType + ",");
                    sql.append(outports + ",");
                    sql.append(outPortType + ",");
                    sql.append(owner + ",");
                    sql.append(pageId + ",");
                    sql.append(checkpoint + ",");
                    sql.append(isCustomized + ",");
                    sql.append(flowId + ",");
                    sql.append(dataSourceId);
                    if (i != stopsList.size()) {
                        sql.append("),");
                    } else {
                        sql.append(")");
                    }
                }
                this.reset();
            }
            sql.append(";");
        }
        return sql.toString();
    }

    /**
     * update stops
     *
     * @param stops
     * @return
     */
    public String updateStops(Stops stops) {
        String sqlStr = "";
        boolean flag = this.preventSQLInjectionStops(stops);
        if (flag) {
            SQL sql = new SQL();

            sql.UPDATE("flow_stops");

            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag = " + enableFlag);
            sql.SET("bundel = " + bundel);
            sql.SET("description = " + description);
            sql.SET("groups = " + groups);
            sql.SET("name = " + name);
            sql.SET("inports = " + inports);
            sql.SET("in_port_type = " + inPortType);
            sql.SET("outports = " + outports);
            sql.SET("out_port_type = " + outPortType);
            sql.SET("owner = " + owner);
            sql.SET("is_checkpoint = " + checkpoint);
            sql.SET("fk_data_source_id = " + dataSourceId);
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
     * Query all stops data
     *
     * @return
     */
    public String getStopsList() {
        String sqlStr = "SELECT * FROM flow_stops WHERE enable_flag=1";
        return sqlStr;
    }

    /**
     * Query StopsList based on flowId
     *
     * @param flowId
     * @return
     */
    public String getStopsListByFlowId(String flowId) {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_stops");
        sql.WHERE("enable_flag = 1");
        sql.WHERE("fk_flow_id = " + SqlUtils.preventSQLInjection(flowId));
        sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * Query StopsList based on flowId
     *
     * @param map
     * @return
     */
    @SuppressWarnings("rawtypes")
    public String getStopsListByFlowIdAndPageIds(Map map) {
        String flowId = (String) map.get("flowId");
        String[] pageIds = (String[]) map.get("pageIds");
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_stops");
        sql.WHERE("enable_flag = 1");
        sql.WHERE("fk_flow_id = " + SqlUtils.preventSQLInjection(flowId));
        if (null != pageIds && pageIds.length > 0) {
            StringBuffer pageIdsStr = new StringBuffer();
            pageIdsStr.append("( ");
            for (int i = 0; i < pageIds.length; i++) {
                String pageId = pageIds[i];
                pageIdsStr.append("page_id = " + SqlUtils.preventSQLInjection(pageId));
                if (i < pageIds.length - 1) {
                    pageIdsStr.append(" OR ");
                }
            }
            pageIdsStr.append(")");
            sql.WHERE(pageIdsStr.toString());
        }
        sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * Query according to stopsId
     *
     * @param Id
     * @return
     */
    public String getStopsById(String Id) {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_stops");
        sql.WHERE("enable_flag = 1");
        sql.WHERE("id = " + SqlUtils.preventSQLInjection(Id));
        sqlStr = sql.toString();
        return sqlStr;
    }

    /**
     * Modify the stop status information according to flowId and name
     *
     * @param stopVo
     * @return
     */
    public String updateStopsByFlowIdAndName(ThirdFlowInfoStopVo stopVo) {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.UPDATE("flow_stops");
        Date endTime = DateUtils.strCstToDate(stopVo.getEndTime());
        Date startTime = DateUtils.strCstToDate(stopVo.getStartTime());
        String name = stopVo.getName();
        String state = stopVo.getState();
        String flowId = stopVo.getFlowId();
        if (null != endTime) {
            sql.SET("stop_time = " + SqlUtils.preventSQLInjection(DateUtils.dateTimeToStr(endTime)));
        }
        if (StringUtils.isNotBlank(state)) {
            sql.SET("state = " + SqlUtils.preventSQLInjection(state));
        }
        if (null != startTime) {
            sql.SET("start_time = " + SqlUtils.preventSQLInjection(DateUtils.dateTimeToStr(startTime)));
        }
        sql.WHERE("fk_flow_id = " + SqlUtils.preventSQLInjection(flowId));
        sql.WHERE("name = " + SqlUtils.preventSQLInjection(name));
        sqlStr = sql.toString();
        return sqlStr;
    }


    public String updateEnableFlagByFlowId(String username, String id) {
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }
        SQL sql = new SQL();
        sql.UPDATE("flow_stops");
        sql.SET("enable_flag=0");
        sql.SET("last_update_user=" + SqlUtils.preventSQLInjection(username));
        sql.SET("last_update_dttm=" + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
        sql.WHERE("enable_flag = 0");
        sql.WHERE("fk_flow_id=" + SqlUtils.preventSQLInjection(id));

        return sql.toString();
    }

}
