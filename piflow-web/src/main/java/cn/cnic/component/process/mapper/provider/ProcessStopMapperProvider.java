package cn.cnic.component.process.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.process.entity.ProcessStop;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProcessStopMapperProvider {

    private String id;
    private String lastUpdateDttmStr;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String name;
    private String bundel;
    private String groups;
    private String owner;
    private String description;
    private String inports;
    private String inPortTypeName;
    private String outports;
    private String outPortTypeName;
    private String stateName;
    private String startTimeStr;
    private String endTimeStr;
    private String pageId;
    private String processId;
    private int isDataSource;

    private boolean preventSQLInjectionProcessStop(ProcessStop processStop) {
        if (null == processStop || StringUtils.isBlank(processStop.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        String id = processStop.getId();
        String lastUpdateUser = processStop.getLastUpdateUser();
        Boolean enableFlag = processStop.getEnableFlag();
        Long version = processStop.getVersion();
        Date lastUpdateDttm = processStop.getLastUpdateDttm();
        this.id = SqlUtils.preventSQLInjection(id);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(lastUpdateUser);
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttmStr);

        // Selection field
        this.name = SqlUtils.preventSQLInjection(processStop.getName());
        this.bundel = SqlUtils.preventSQLInjection(processStop.getBundel());
        this.groups = SqlUtils.preventSQLInjection(processStop.getGroups());
        this.owner = SqlUtils.preventSQLInjection(processStop.getOwner());
        this.description = SqlUtils.preventSQLInjection(processStop.getDescription());
        this.inports = SqlUtils.preventSQLInjection(processStop.getInports());
        this.inPortTypeName = SqlUtils
                .preventSQLInjection(null != processStop.getInPortType() ? processStop.getInPortType().name() : null);
        this.outports = SqlUtils.preventSQLInjection(processStop.getOutports());
        this.outPortTypeName = SqlUtils
                .preventSQLInjection(null != processStop.getOutPortType() ? processStop.getOutPortType().name() : null);
        this.stateName = SqlUtils
                .preventSQLInjection(null != processStop.getState() ? processStop.getState().name() : null);
        String startTime = (null != processStop.getStartTime() ? DateUtils.dateTimesToStr(processStop.getStartTime())
                : null);
        String endTime = (null != processStop.getEndTime() ? DateUtils.dateTimesToStr(processStop.getEndTime()) : null);
        this.startTimeStr = SqlUtils.preventSQLInjection(startTime);
        this.endTimeStr = SqlUtils.preventSQLInjection(endTime);
        this.pageId = SqlUtils.preventSQLInjection(processStop.getPageId());
        String processIdStr = (null != processStop.getProcess() ? processStop.getProcess().getId() : null);
        this.processId = (null != processIdStr ? SqlUtils.preventSQLInjection(processIdStr) : null);
        this.isDataSource = ((null != processStop.getIsDataSource() && processStop.getIsDataSource()) ? 1 : 0);
        return true;
    }

    private void reset() {
        this.id = null;
        this.lastUpdateDttmStr = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.name = null;
        this.bundel = null;
        this.groups = null;
        this.owner = null;
        this.description = null;
        this.inports = null;
        this.inPortTypeName = null;
        this.outports = null;
        this.outPortTypeName = null;
        this.stateName = null;
        this.startTimeStr = null;
        this.endTimeStr = null;
        this.pageId = null;
        this.processId = null;
    }

    /**
     * add processStop
     *
     * @param processStop
     * @return
     */
    public String addProcessStop(ProcessStop processStop) {
        this.preventSQLInjectionProcessStop(processStop);
        if (this.preventSQLInjectionProcessStop(processStop)) {
            // INSERT_INTO brackets is table name
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO flow_process_stop ");
            strBuf.append("(");
            // Mandatory Field
            strBuf.append(SqlUtils.baseFieldName() + ", ");
            strBuf.append("name, ");
            strBuf.append("bundel, ");
            strBuf.append("`groups`, ");
            strBuf.append("owner, ");
            strBuf.append("description, ");
            strBuf.append("inports, ");
            strBuf.append("in_port_type, ");
            strBuf.append("outports, ");
            strBuf.append("out_port_type, ");
            strBuf.append("state, ");
            strBuf.append("start_time, ");
            strBuf.append("end_time, ");
            strBuf.append("page_id, ");
            strBuf.append("fk_flow_process_id, ");
            strBuf.append("is_data_source ");
            strBuf.append(") ");
            strBuf.append("VALUES ");
            strBuf.append("(");
            // Selection field
            strBuf.append(SqlUtils.baseFieldValues(processStop) + ", ");
            // handle other fields
            strBuf.append(this.name + ", ");
            strBuf.append(this.bundel + ", ");
            strBuf.append(this.groups + ", ");
            strBuf.append(this.owner + ", ");
            strBuf.append(this.description + ", ");
            strBuf.append(this.inports + ", ");
            strBuf.append(this.inPortTypeName + ", ");
            strBuf.append(this.outports + ", ");
            strBuf.append(this.outPortTypeName + ", ");
            strBuf.append(this.stateName + ", ");
            strBuf.append(this.startTimeStr + ", ");
            strBuf.append(this.endTimeStr + ", ");
            strBuf.append(this.pageId + ", ");
            strBuf.append(this.processId + ", ");
            strBuf.append(this.isDataSource + " ");
            strBuf.append(") ");
            this.reset();
            return strBuf.toString();
        }
        return "SELECT 0";
    }

    /**
     * add processStopList
     *
     * @param processStopList
     * @return
     */
    public String addProcessStopList(Map<String, List<ProcessStop>> processStopList) {
        List<ProcessStop> processStops = processStopList.get("processStopList");
        if (null == processStops || processStops.size() <= 0) {
            return "SELECT 0";
        }
        // INSERT_INTO brackets is table name
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("INSERT INTO flow_process_stop ");
        strBuf.append("(");
        // Mandatory Field
        strBuf.append(SqlUtils.baseFieldName() + ", ");
        strBuf.append("name, ");
        strBuf.append("bundel, ");
        strBuf.append("`groups`, ");
        strBuf.append("owner, ");
        strBuf.append("description, ");
        strBuf.append("inports, ");
        strBuf.append("in_port_type, ");
        strBuf.append("outports, ");
        strBuf.append("out_port_type, ");
        strBuf.append("state, ");
        strBuf.append("start_time, ");
        strBuf.append("end_time, ");
        strBuf.append("page_id, ");
        strBuf.append("fk_flow_process_id, ");
        strBuf.append("is_data_source ");
        strBuf.append(") ");
        strBuf.append("VALUES ");

        boolean firstFlag = true;
        for (int i = 0; i < processStops.size(); i++) {
            ProcessStop processStop = processStops.get(i);
            if (preventSQLInjectionProcessStop(processStop)) {
                if (firstFlag) {
                    strBuf.append("(");
                } else {
                    strBuf.append(",(");
                }
                // Selection field
                strBuf.append(SqlUtils.baseFieldValues(processStop) + ", ");
                // handle other fields
                strBuf.append(this.name + ", ");
                strBuf.append(this.bundel + ", ");
                strBuf.append(this.groups + ", ");
                strBuf.append(this.owner + ", ");
                strBuf.append(this.description + ", ");
                strBuf.append(this.inports + ", ");
                strBuf.append(this.inPortTypeName + ", ");
                strBuf.append(this.outports + ", ");
                strBuf.append(this.outPortTypeName + ", ");
                strBuf.append(this.stateName + ", ");
                strBuf.append(this.startTimeStr + ", ");
                strBuf.append(this.endTimeStr + ", ");
                strBuf.append(this.pageId + ", ");
                strBuf.append(this.processId + ", ");
                strBuf.append(this.isDataSource + " ");
                strBuf.append(") ");
            }
            this.reset();
        }
        return strBuf.toString();
    }

    public String getProcessStopByProcessId(String processId) {
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(processId)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("flow_process_stop");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_id = " + SqlUtils.preventSQLInjection(processId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * Query based on pid and pageId
     *
     * @param processId
     * @param pageId
     * @return
     */
    public String getProcessStopByPageIdAndPageId(String processId, String pageId) {
        String sqlStr = "SELECT 0";
        if (!StringUtils.isAnyEmpty(processId, pageId)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("flow_process_stop");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_id = " + SqlUtils.preventSQLInjection(processId));
            sql.WHERE("page_id = " + SqlUtils.preventSQLInjection(pageId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * Query based on pid and pageIds
     *
     * @param map
     * @return
     */
    @SuppressWarnings("rawtypes")
    public String getProcessStopByPageIdAndPageIds(Map map) {
        String processId = (String) map.get("processId");
        String[] pageIds = (String[]) map.get("pageIds");
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(processId) && null != pageIds && pageIds.length > 0) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("flow_process_stop");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_id = " + SqlUtils.preventSQLInjection(processId));
            sql.WHERE("page_id in ( " + SqlUtils.strArrayToStr(pageIds) + ")");

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * Query by pid and name
     *
     * @param processId
     * @param name
     * @return
     */
    public String getProcessStopByNameAndPid(String processId, String name) {
        String sqlStr = "SELECT 0";
        if (!StringUtils.isAnyEmpty(processId, name)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("flow_process_stop");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_id = " + SqlUtils.preventSQLInjection(processId));
            sql.WHERE("name = " + SqlUtils.preventSQLInjection(name));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * Query by id
     *
     * @param id
     * @return
     */
    public static String getProcessStopById(String id) {
        String sqlStr = "SELECT 0";
        if (!StringUtils.isAnyEmpty(id)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("flow_process_stop");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    public String updateProcessStop(ProcessStop processStop) {
        String sqlStr = "SELECT 0";
        if (this.preventSQLInjectionProcessStop(processStop)) {
            if (StringUtils.isBlank(this.id)) {
                return "SELECT 0";
            }
            SQL sql = new SQL();
            sql.UPDATE("flow_process_stop");

            // Process the required fields first
            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag = " + enableFlag);
            sql.SET("name = " + name);
            sql.SET("bundel = " + bundel);
            sql.SET("`groups` = " + groups);
            sql.SET("owner = " + owner);
            sql.SET("description = " + description);
            sql.SET("inports = " + inports);
            sql.SET("in_port_type = " + inPortTypeName);
            sql.SET("outports = " + outports);
            sql.SET("out_port_type = " + outPortTypeName);
            sql.SET("state = " + stateName);
            sql.SET("start_time = " + startTimeStr);
            sql.SET("end_time = " + endTimeStr);
            sql.SET("page_id = " + pageId);
            sql.WHERE("enable_flag = 1");
            sql.WHERE("version = " + version);
            sql.WHERE("id = " + id);

            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    public String updateEnableFlagByProcessId(String processId, String username) {
        String sqlStr = "SELECT 0";
        if (StringUtils.isNoneEmpty(processId, username)) {
            SQL sql = new SQL();
            sql.UPDATE("flow_process_stop");
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
            sql.SET("last_update_user = " + SqlUtils.preventSQLInjection(username));
            sql.SET("version=(version+1)");
            sql.SET("enable_flag = 0");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("fk_flow_process_id = " + SqlUtils.preventSQLInjection(processId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }
}
