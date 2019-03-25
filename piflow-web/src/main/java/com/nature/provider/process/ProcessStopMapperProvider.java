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

    /**
     * 添加processStop
     *
     * @param processStop
     * @return
     */
    public String addProcessStop(ProcessStop processStop) {
        String sqlStr = "select 0";
        if (null != processStop) {
            String id = processStop.getId();
            String crtUser = processStop.getCrtUser();
            Date crtDttm = processStop.getCrtDttm();
            String lastUpdateUser = processStop.getLastUpdateUser();
            Date lastUpdateDttm = processStop.getLastUpdateDttm();
            Long version = processStop.getVersion();
            Boolean enableFlag = processStop.getEnableFlag();
            String name = processStop.getName();
            String bundel = processStop.getBundel();
            String groups = processStop.getGroups();
            String owner = processStop.getOwner();
            String description = processStop.getDescription();
            String inports = processStop.getInports();
            PortType inPortType = processStop.getInPortType();
            String outports = processStop.getOutports();
            PortType outPortType = processStop.getOutPortType();
            StopState state = processStop.getState();
            Date startTime = processStop.getStartTime();
            Date endTime = processStop.getEndTime();
            String pageId = processStop.getPageId();
            Process process = processStop.getProcess();

            SQL sql = new SQL();

            // INSERT_INTO括号中为数据库表名
            sql.INSERT_INTO("FLOW_PROCESS_STOP");
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
            sql.VALUES("ID", SqlUtils.addSqlStrAndReplace(id));
            sql.VALUES("CRT_DTTM", SqlUtils.addSqlStrAndReplace(DateUtils.dateTimesToStr(crtDttm)));
            sql.VALUES("CRT_USER", SqlUtils.addSqlStrAndReplace(crtUser));
            sql.VALUES("LAST_UPDATE_DTTM", SqlUtils.addSqlStrAndReplace(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.VALUES("LAST_UPDATE_USER", SqlUtils.addSqlStrAndReplace(lastUpdateUser));
            sql.VALUES("VERSION", version + "");
            sql.VALUES("ENABLE_FLAG", (enableFlag ? 1 : 0) + "");

            // 处理其他字段
            if (null != name) {
                sql.VALUES("NAME", SqlUtils.addSqlStrAndReplace(name));
            }
            if (null != bundel) {
                sql.VALUES("BUNDEL", SqlUtils.addSqlStrAndReplace(bundel));
            }
            if (null != groups) {
                sql.VALUES("GROUPS", SqlUtils.addSqlStrAndReplace(groups));
            }
            if (null != owner) {
                sql.VALUES("OWNER", SqlUtils.addSqlStrAndReplace(owner));
            }
            if (null != description) {
                sql.VALUES("DESCRIPTION", SqlUtils.addSqlStrAndReplace(description));
            }
            if (null != inports) {
                sql.VALUES("INPORTS", SqlUtils.addSqlStrAndReplace(inports));
            }
            if (null != inPortType) {
                sql.VALUES("IN_PORT_TYPE", SqlUtils.addSqlStrAndReplace(inPortType.name()));
            }
            if (null != outports) {
                sql.VALUES("OUTPORTS", SqlUtils.addSqlStrAndReplace(outports));
            }
            if (null != outPortType) {
                sql.VALUES("OUT_PORT_TYPE", SqlUtils.addSqlStrAndReplace(outPortType.name()));
            }
            if (null != state) {
                sql.VALUES("STATE", SqlUtils.addSqlStrAndReplace(state.name()));
            }
            if (null != startTime) {
                sql.VALUES("START_TIME", SqlUtils.addSqlStrAndReplace(DateUtils.dateTimesToStr(startTime)));
            }
            if (null != endTime) {
                sql.VALUES("END_TIME", SqlUtils.addSqlStrAndReplace(DateUtils.dateTimesToStr(endTime)));
            }
            if (null != pageId) {
                sql.VALUES("PAGE_ID", SqlUtils.addSqlStrAndReplace(pageId));
            }
            if (null != process) {
                sql.VALUES("FK_FLOW_PROCESS_ID", SqlUtils.addSqlStrAndReplace(process.getId()));
            }
            sqlStr = sql.toString();
        }
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
                    String id = processStop.getId();
                    String crtUser = processStop.getCrtUser();
                    Date crtDttm = processStop.getCrtDttm();
                    String lastUpdateUser = processStop.getLastUpdateUser();
                    Date lastUpdateDttm = processStop.getLastUpdateDttm();
                    Long version = processStop.getVersion();
                    Boolean enableFlag = processStop.getEnableFlag();
                    String name = processStop.getName();
                    String bundel = processStop.getBundel();
                    String groups = processStop.getGroups();
                    String owner = processStop.getOwner();
                    String description = processStop.getDescription();
                    String inports = processStop.getInports();
                    PortType inPortType = processStop.getInPortType();
                    String outports = processStop.getOutports();
                    PortType outPortType = processStop.getOutPortType();
                    StopState state = processStop.getState();
                    Date startTime = processStop.getStartTime();
                    Date endTime = processStop.getEndTime();
                    String pageId = processStop.getPageId();
                    Process process = processStop.getProcess();

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

                    sql.append("(");
                    sql.append(SqlUtils.addSqlStrAndReplace(id) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(DateUtils.dateTimesToStr(crtDttm)) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(crtUser) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(DateUtils.dateTimesToStr(lastUpdateDttm)) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(lastUpdateUser) + ",");
                    sql.append(version + ",");
                    sql.append((enableFlag ? 1 : 0) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(name) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(bundel) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(groups) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(owner) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(description) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(inports) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace((null != inPortType ? inPortType.name() : "")) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(outports) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace((null != outPortType ? outPortType.name() : "")) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace((null != state ? state.name() : "")) + ",");
//                    sql.append(SqlUtils.addSqlStrAndReplace(DateUtils.dateTimesToStr(startTime)) + ",");
//                    sql.append(SqlUtils.addSqlStrAndReplace(DateUtils.dateTimesToStr(endTime)) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(pageId) + ",");
                    sql.append(SqlUtils.addSqlStrAndReplace(process.getId()));
                    if (i != processStops.size()) {
                        sql.append("),");
                    } else {
                        sql.append(")");
                    }
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
            sql.WHERE("FK_FLOW_PROCESS_ID = " + SqlUtils.addSqlStr(processId));

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
            sql.WHERE("FK_FLOW_PROCESS_ID = " + SqlUtils.addSqlStr(processId));
            sql.WHERE("PAGE_ID = " + SqlUtils.addSqlStr(pageId));

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
                sql.WHERE("FK_FLOW_PROCESS_ID = " + SqlUtils.addSqlStr(processId));
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
            sql.WHERE("FK_FLOW_PROCESS_ID = " + SqlUtils.addSqlStr(processId));
            sql.WHERE("NAME = " + SqlUtils.addSqlStr(name));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    public String updateProcessStop(ProcessStop processStop) {
        String sqlStr = "select 0";
        if (null != processStop) {
            String id = processStop.getId();
            if (StringUtils.isNotBlank(id)) {
                String lastUpdateUser = processStop.getLastUpdateUser();
                Date lastUpdateDttm = processStop.getLastUpdateDttm();
                Long version = processStop.getVersion();
                Boolean enableFlag = processStop.getEnableFlag();
                String name = processStop.getName();
                String bundel = processStop.getBundel();
                String groups = processStop.getGroups();
                String owner = processStop.getOwner();
                String description = processStop.getDescription();
                String inports = processStop.getInports();
                PortType inPortType = processStop.getInPortType();
                String outports = processStop.getOutports();
                PortType outPortType = processStop.getOutPortType();
                StopState state = processStop.getState();
                Date startTime = processStop.getStartTime();
                Date endTime = processStop.getEndTime();
                String pageId = processStop.getPageId();
                Process process = processStop.getProcess();

                SQL sql = new SQL();
                sql.UPDATE("FLOW_PROCESS_STOP");

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
                if (StringUtils.isNotBlank(name)) {
                    sql.SET("NAME = " + SqlUtils.addSqlStr(name));
                }
                if (StringUtils.isNotBlank(bundel)) {
                    sql.SET("BUNDEL = " + SqlUtils.addSqlStr(bundel));
                }
                if (StringUtils.isNotBlank(groups)) {
                    sql.SET("GROUPS = " + SqlUtils.addSqlStr(groups));
                }
                if (StringUtils.isNotBlank(owner)) {
                    sql.SET("OWNER = " + SqlUtils.addSqlStr(owner));
                }
                if (StringUtils.isNotBlank(description)) {
                    sql.SET("DESCRIPTION = " + SqlUtils.addSqlStr(description));
                }
                if (StringUtils.isNotBlank(inports)) {
                    sql.SET("INPORTS = " + SqlUtils.addSqlStr(inports));
                }
                if (null != inPortType) {
                    sql.SET("IN_PORT_TYPE = " + SqlUtils.addSqlStr(inPortType.name()));
                }
                if (StringUtils.isNotBlank(outports)) {
                    sql.SET("OUTPORTS = " + SqlUtils.addSqlStr(outports));
                }
                if (null != outPortType) {
                    sql.SET("OUT_PORT_TYPE = " + SqlUtils.addSqlStr(outPortType.name()));
                }
                if (null != state) {
                    sql.SET("state = " + SqlUtils.addSqlStr(state.name()));
                }
                if (null != startTime) {
                    String startTimeStr = DateUtils.dateTimesToStr(startTime);
                    if (StringUtils.isNotBlank(startTimeStr)) {
                        sql.SET("START_TIME = " + SqlUtils.addSqlStr(startTimeStr));
                    }
                }
                if (null != endTime) {
                    String endTimeStr = DateUtils.dateTimesToStr(endTime);
                    if (StringUtils.isNotBlank(endTimeStr)) {
                        sql.SET("end_time = " + SqlUtils.addSqlStr(endTimeStr));
                    }
                }
                if (StringUtils.isNotBlank(pageId)) {
                    sql.SET("PAGE_ID = " + SqlUtils.addSqlStr(pageId));
                }

                sql.WHERE("ENABLE_FLAG = 1");
                sql.WHERE("VERSION = " + version);
                sql.WHERE("ID = " + SqlUtils.addSqlStr(id));

                sqlStr = sql.toString();
            }
        }
        return sqlStr;
    }

    public String updateEnableFlagByProcessId(String processId, String username) {
        String sqlStr = "select 0";
        if (StringUtils.isAnyEmpty(processId, username)) {
            SQL sql = new SQL();
            sql.UPDATE("FLOW_PROCESS_STOP");
            sql.SET("LAST_UPDATE_DTTM = " + SqlUtils.addSqlStr(DateUtils.dateTimesToStr(new Date())));
            sql.SET("LAST_UPDATE_USER = " + SqlUtils.addSqlStr(username));
            sql.SET("VERSION=(VERSION+1)");
            sql.SET("enable_flag = 0");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("FK_FLOW_PROCESS_ID = " + SqlUtils.addSqlStr(processId));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }
}
