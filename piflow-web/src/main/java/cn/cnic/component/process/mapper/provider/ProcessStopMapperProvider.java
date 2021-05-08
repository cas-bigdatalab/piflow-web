package cn.cnic.component.process.mapper.provider;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.process.entity.ProcessStop;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProcessStopMapperProvider {

    private String id;
    private String crtUser;
    private String crtDttmStr;
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

    private void preventSQLInjectionProcessStop(ProcessStop processStop) {
        if (null != processStop && StringUtils.isNotBlank(processStop.getLastUpdateUser())) {
            // Mandatory Field
            String id = processStop.getId();
            String crtUser = processStop.getCrtUser();
            String lastUpdateUser = processStop.getLastUpdateUser();
            Boolean enableFlag = processStop.getEnableFlag();
            Long version = processStop.getVersion();
            Date crtDttm = processStop.getCrtDttm();
            Date lastUpdateDttm = processStop.getLastUpdateDttm();
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
            this.name = SqlUtils.preventSQLInjection(processStop.getName());
            this.bundel = SqlUtils.preventSQLInjection(processStop.getBundel());
            this.groups = SqlUtils.preventSQLInjection(processStop.getGroups());
            this.owner = SqlUtils.preventSQLInjection(processStop.getOwner());
            this.description = SqlUtils.preventSQLInjection(processStop.getDescription());
            this.inports = SqlUtils.preventSQLInjection(processStop.getInports());
            this.inPortTypeName = SqlUtils.preventSQLInjection(null != processStop.getInPortType() ? processStop.getInPortType().name() : null);
            this.outports = SqlUtils.preventSQLInjection(processStop.getOutports());
            this.outPortTypeName = SqlUtils.preventSQLInjection(null != processStop.getOutPortType() ? processStop.getOutPortType().name() : null);
            this.stateName = SqlUtils.preventSQLInjection(null != processStop.getState() ? processStop.getState().name() : null);
            String startTime = (null != processStop.getStartTime() ? DateUtils.dateTimesToStr(processStop.getStartTime()) : null);
            String endTime = (null != processStop.getEndTime() ? DateUtils.dateTimesToStr(processStop.getEndTime()) : null);
            this.startTimeStr = SqlUtils.preventSQLInjection(startTime);
            this.endTimeStr = SqlUtils.preventSQLInjection(endTime);
            this.pageId = SqlUtils.preventSQLInjection(processStop.getPageId());
            String processIdStr = (null != processStop.getProcess() ? processStop.getProcess().getId() : null);
            this.processId = (null != processIdStr ? SqlUtils.preventSQLInjection(processIdStr) : null);
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
        String sqlStr = "SELECT 0";
        this.preventSQLInjectionProcessStop(processStop);
        if (null != processStop) {

            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.INSERT_INTO("flow_process_stop");
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
            sql.VALUES("version", version + "");
            sql.VALUES("enable_flag", enableFlag + "");

            // handle other fields
            sql.VALUES("name", name);
            sql.VALUES("bundel", bundel);
            sql.VALUES("groups", groups);
            sql.VALUES("owner", owner);
            sql.VALUES("description", description);
            sql.VALUES("inports", inports);
            sql.VALUES("in_port_type", inPortTypeName);
            sql.VALUES("outports", SqlUtils.preventSQLInjection(outports));
            sql.VALUES("out_port_type", outPortTypeName);
            sql.VALUES("state", stateName);
            sql.VALUES("start_time", startTimeStr);
            sql.VALUES("end_time", endTimeStr);
            sql.VALUES("page_id", pageId);
            sql.VALUES("fk_flow_process_id", processId);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * add processStopList
     *
     * @param processStopList
     * @return
     */
    public String addProcessStopList(Map<String, List<ProcessStop>> processStopList) {
        String sqlStr = "SELECT 0";
        List<ProcessStop> processStops = processStopList.get("processStopList");
        if (null != processStops && processStops.size() > 0) {
            StringBuffer sql = new StringBuffer();
            sql.append("insert into ");
            sql.append("flow_process_stop ");
            sql.append("(");
            sql.append("id,");
            sql.append("crt_dttm,");
            sql.append("crt_user,");
            sql.append("last_update_dttm,");
            sql.append("last_update_user,");
            sql.append("version,");
            sql.append("enable_flag,");
            sql.append("name,");
            sql.append("bundel,");
            sql.append("groups,");
            sql.append("owner,");
            sql.append("description,");
            sql.append("inports,");
            sql.append("in_port_type,");
            sql.append("outports,");
            sql.append("out_port_type,");
            sql.append("state,");
//            sql.append("start_time,");
//            sql.append("end_time,");
            sql.append("page_id,");
            sql.append("fk_flow_process_id");
            sql.append(")");
            sql.append("values");

            // The order must be guaranteed
            int i = 0;
            for (ProcessStop processStop : processStops) {
                i++;
                if (null != processStop) {
                    this.preventSQLInjectionProcessStop(processStop);
                    if (null == crtDttmStr) {
                        String crtDttm = DateUtils.dateTimesToStr(new Date());
                        crtDttmStr = SqlUtils.preventSQLInjection(crtDttm);
                    }
                    if (StringUtils.isBlank(crtUser)) {
                        crtUser = SqlUtils.preventSQLInjection("-1");
                        ;
                    }
                    sql.append("(");
                    sql.append(id + ",");
                    sql.append(crtDttmStr + ",");
                    sql.append(crtUser + ",");
                    sql.append(lastUpdateDttmStr + ",");
                    sql.append(lastUpdateUser + ",");
                    sql.append(version + ",");
                    sql.append(enableFlag + ",");
                    sql.append(name + ",");
                    sql.append(bundel + ",");
                    sql.append(groups + ",");
                    sql.append(owner + ",");
                    sql.append(description + ",");
                    sql.append(inports + ",");
                    sql.append(inPortTypeName + ",");
                    sql.append(SqlUtils.preventSQLInjection(outports) + ",");
                    sql.append(outPortTypeName + ",");
                    sql.append(stateName + ",");
//                    sql.append(startTimeStr + ",");
//                    sql.append(endTimeStr + ",");
                    sql.append(pageId + ",");
                    sql.append(processId);
                    if (i != processStops.size()) {
                        sql.append("),");
                    } else {
                        sql.append(")");
                    }
                    this.reset();
                }
            }
            sqlStr = sql.toString();
        }
        return sqlStr;
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

    public String updateProcessStop(ProcessStop processStop) {
        String sqlStr = "SELECT 0";
        this.preventSQLInjectionProcessStop(processStop);
        if (null != processStop) {
            String id = processStop.getId();
            if (StringUtils.isNotBlank(id)) {
                SQL sql = new SQL();
                sql.UPDATE("flow_process_stop");

                //Process the required fields first
                sql.SET("last_update_dttm = " + lastUpdateDttmStr);
                sql.SET("last_update_user = " + lastUpdateUser);
                sql.SET("version = " + (version + 1));

                // handle other fields
                sql.SET("enable_flag = " + enableFlag);
                sql.SET("name = " + name);
                sql.SET("bundel = " + bundel);
                sql.SET("groups = " + groups);
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
                sql.WHERE("id = " + SqlUtils.preventSQLInjection(id));

                sqlStr = sql.toString();
            }
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
