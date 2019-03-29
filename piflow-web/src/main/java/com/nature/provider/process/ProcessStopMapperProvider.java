package com.nature.provider.process;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SqlUtils;
import com.nature.common.Eunm.PortType;
import com.nature.common.Eunm.StopState;
import com.nature.component.process.model.Process;
import com.nature.component.process.model.ProcessStop;
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
     * 添加processStop
     *
     * @param processStop
     * @return
     */
    public String addProcessStop(ProcessStop processStop) {
        String sqlStr = "select 0";
        this.preventSQLInjectionProcessStop(processStop);
        if (null != processStop) {

            SQL sql = new SQL();

            // INSERT_INTO括号中为数据库表名
            sql.INSERT_INTO("FLOW_PROCESS_STOP");
            // value中的第一个字符串为数据库中表对应的字段名
            // 除数字类型的字段外其他类型必须加单引号

            //先处理修改必填字段
            if (null == crtDttmStr) {
                String crtDttm = DateUtils.dateTimesToStr(new Date());
                crtDttmStr = SqlUtils.preventSQLInjection(crtDttm);
            }
            if (StringUtils.isBlank(crtUser)) {
                crtUser = SqlUtils.preventSQLInjection("-1");
            }
            sql.VALUES("ID", id);
            sql.VALUES("CRT_DTTM", crtDttmStr);
            sql.VALUES("CRT_USER", crtUser);
            sql.VALUES("LAST_UPDATE_DTTM", lastUpdateDttmStr);
            sql.VALUES("LAST_UPDATE_USER", lastUpdateUser);
            sql.VALUES("VERSION", version + "");
            sql.VALUES("ENABLE_FLAG", enableFlag + "");

            // 处理其他字段
            sql.VALUES("NAME", name);
            sql.VALUES("BUNDEL", bundel);
            sql.VALUES("GROUPS", groups);
            sql.VALUES("OWNER", owner);
            sql.VALUES("DESCRIPTION", description);
            sql.VALUES("INPORTS", inports);
            sql.VALUES("IN_PORT_TYPE", inPortTypeName);
            sql.VALUES("OUTPORTS", SqlUtils.preventSQLInjection(outports));
            sql.VALUES("OUT_PORT_TYPE", outPortTypeName);
            sql.VALUES("STATE", stateName);
            sql.VALUES("START_TIME", startTimeStr);
            sql.VALUES("END_TIME", endTimeStr);
            sql.VALUES("PAGE_ID", pageId);
            sql.VALUES("FK_FLOW_PROCESS_ID", processId);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * 添加processStopList
     *
     * @param processStopList
     * @return
     */
    public String addProcessStopList(Map<String, List<ProcessStop>> processStopList) {
        String sqlStr = "select 0";
        List<ProcessStop> processStops = processStopList.get("processStopList");
        if (null != processStops && processStops.size() > 0) {
            StringBuffer sql = new StringBuffer();
            sql.append("INSERT INTO ");
            sql.append("FLOW_PROCESS_STOP ");
            sql.append("(");
            sql.append("ID,");
            sql.append("CRT_DTTM,");
            sql.append("CRT_USER,");
            sql.append("LAST_UPDATE_DTTM,");
            sql.append("LAST_UPDATE_USER,");
            sql.append("VERSION,");
            sql.append("ENABLE_FLAG,");
            sql.append("NAME,");
            sql.append("BUNDEL,");
            sql.append("GROUPS,");
            sql.append("OWNER,");
            sql.append("DESCRIPTION,");
            sql.append("INPORTS,");
            sql.append("IN_PORT_TYPE,");
            sql.append("OUTPORTS,");
            sql.append("OUT_PORT_TYPE,");
            sql.append("STATE,");
//            sql.append("START_TIME,");
//            sql.append("END_TIME,");
            sql.append("PAGE_ID,");
            sql.append("FK_FLOW_PROCESS_ID");
            sql.append(")");
            sql.append("VALUES");

            // 放值时必须保证先后顺序
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
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(processId)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("FLOW_PROCESS_STOP");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("FK_FLOW_PROCESS_ID = " + SqlUtils.preventSQLInjection(processId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * 根据pid和pageId查询
     *
     * @param processId
     * @param pageId
     * @return
     */
    public String getProcessStopByPageIdAndPageId(String processId, String pageId) {
        String sqlStr = "select 0";
        if (!StringUtils.isAnyEmpty(processId, pageId)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("FLOW_PROCESS_STOP");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("FK_FLOW_PROCESS_ID = " + SqlUtils.preventSQLInjection(processId));
            sql.WHERE("PAGE_ID = " + SqlUtils.preventSQLInjection(pageId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * 根据pid和pageIds查询
     *
     * @param map
     * @return
     */
    public String getProcessStopByPageIdAndPageIds(Map map) {
        String processId = (String) map.get("processId");
        String[] pageIds = (String[]) map.get("pageIds");
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(processId) && null != pageIds && pageIds.length > 0) {
            String pageIdsStr = SqlUtils.strArrayToStr(pageIds);
            if (StringUtils.isNotBlank(pageIdsStr)) {

                pageIdsStr = pageIdsStr.replace(",", "','");
                pageIdsStr = "'" + pageIdsStr + "'";
                SQL sql = new SQL();
                sql.SELECT("*");
                sql.FROM("FLOW_PROCESS_STOP");
                sql.WHERE("enable_flag = 1");
                sql.WHERE("FK_FLOW_PROCESS_ID = " + SqlUtils.preventSQLInjection(processId));
                sql.WHERE("PAGE_ID IN ( " + pageIdsStr + ")");

                sqlStr = sql.toString();
            }
        }
        return sqlStr;
    }

    /**
     * 根据pid和name查询
     *
     * @param processId
     * @param name
     * @return
     */
    public String getProcessStopByNameAndPid(String processId, String name) {
        String sqlStr = "select 0";
        if (!StringUtils.isAnyEmpty(processId, name)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("FLOW_PROCESS_STOP");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("FK_FLOW_PROCESS_ID = " + SqlUtils.preventSQLInjection(processId));
            sql.WHERE("NAME = " + SqlUtils.preventSQLInjection(name));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    public String updateProcessStop(ProcessStop processStop) {
        String sqlStr = "select 0";
        this.preventSQLInjectionProcessStop(processStop);
        if (null != processStop) {
            String id = processStop.getId();
            if (StringUtils.isNotBlank(id)) {
                SQL sql = new SQL();
                sql.UPDATE("FLOW_PROCESS_STOP");

                //先处理修改必填字段
                sql.SET("LAST_UPDATE_DTTM = " + lastUpdateDttmStr);
                sql.SET("LAST_UPDATE_USER = " + lastUpdateUser);
                sql.SET("VERSION = " + (version + 1));

                // 处理其他字段
                sql.SET("ENABLE_FLAG = " + enableFlag);
                sql.SET("NAME = " + name);
                sql.SET("BUNDEL = " + bundel);
                sql.SET("GROUPS = " + groups);
                sql.SET("OWNER = " + owner);
                sql.SET("DESCRIPTION = " + description);
                sql.SET("INPORTS = " + inports);
                sql.SET("IN_PORT_TYPE = " + inPortTypeName);
                sql.SET("OUTPORTS = " + outports);
                sql.SET("OUT_PORT_TYPE = " + outPortTypeName);
                sql.SET("state = " + stateName);
                sql.SET("START_TIME = " + startTimeStr);
                sql.SET("end_time = " + endTimeStr);
                sql.SET("PAGE_ID = " + pageId);
                sql.WHERE("ENABLE_FLAG = 1");
                sql.WHERE("VERSION = " + version);
                sql.WHERE("ID = " + SqlUtils.preventSQLInjection(id));

                sqlStr = sql.toString();
            }
        }
        this.reset();
        return sqlStr;
    }

    public String updateEnableFlagByProcessId(String processId, String username) {
        String sqlStr = "select 0";
        if (StringUtils.isNoneEmpty(processId, username)) {
            SQL sql = new SQL();
            sql.UPDATE("FLOW_PROCESS_STOP");
            sql.SET("LAST_UPDATE_DTTM = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date())));
            sql.SET("LAST_UPDATE_USER = " + SqlUtils.preventSQLInjection(username));
            sql.SET("VERSION=(VERSION+1)");
            sql.SET("enable_flag = 0");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("FK_FLOW_PROCESS_ID = " + SqlUtils.preventSQLInjection(processId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }
}
