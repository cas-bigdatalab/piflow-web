package com.nature.provider.process;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
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
            sql.VALUES("ID", Utils.addSqlStrAndReplace(id));
            sql.VALUES("CRT_DTTM", Utils.addSqlStrAndReplace(DateUtils.dateTimesToStr(crtDttm)));
            sql.VALUES("CRT_USER", Utils.addSqlStrAndReplace(crtUser));
            sql.VALUES("LAST_UPDATE_DTTM", Utils.addSqlStrAndReplace(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.VALUES("LAST_UPDATE_USER", Utils.addSqlStrAndReplace(lastUpdateUser));
            sql.VALUES("VERSION", version + "");
            sql.VALUES("ENABLE_FLAG", (enableFlag ? 1 : 0) + "");

            // 处理其他字段
            if (null != name) {
                sql.VALUES("NAME", Utils.addSqlStrAndReplace(name));
            }
            if (null != bundel) {
                sql.VALUES("BUNDEL", Utils.addSqlStrAndReplace(bundel));
            }
            if (null != groups) {
                sql.VALUES("GROUPS", Utils.addSqlStrAndReplace(groups));
            }
            if (null != owner) {
                sql.VALUES("OWNER", Utils.addSqlStrAndReplace(owner));
            }
            if (null != description) {
                sql.VALUES("DESCRIPTION", Utils.addSqlStrAndReplace(description));
            }
            if (null != inports) {
                sql.VALUES("INPORTS", Utils.addSqlStrAndReplace(inports));
            }
            if (null != inPortType) {
                sql.VALUES("IN_PORT_TYPE", Utils.addSqlStrAndReplace(inPortType.name()));
            }
            if (null != outports) {
                sql.VALUES("OUTPORTS", Utils.addSqlStrAndReplace(outports));
            }
            if (null != outPortType) {
                sql.VALUES("OUT_PORT_TYPE", Utils.addSqlStrAndReplace(outPortType.name()));
            }
            if (null != state) {
                sql.VALUES("STATE", Utils.addSqlStrAndReplace(state.name()));
            }
            if (null != startTime) {
                sql.VALUES("START_TIME", Utils.addSqlStrAndReplace(DateUtils.dateTimesToStr(startTime)));
            }
            if (null != endTime) {
                sql.VALUES("END_TIME", Utils.addSqlStrAndReplace(DateUtils.dateTimesToStr(endTime)));
            }
            if (null != pageId) {
                sql.VALUES("PAGE_ID", Utils.addSqlStrAndReplace(pageId));
            }
            if (null != process) {
                sql.VALUES("FK_FLOW_PROCESS_ID", Utils.addSqlStrAndReplace(process.getId()));
            }
            sqlStr = sql.toString() + ";";
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
                    sql.append(Utils.addSqlStrAndReplace(id) + ",");
                    sql.append(Utils.addSqlStrAndReplace(DateUtils.dateTimesToStr(crtDttm)) + ",");
                    sql.append(Utils.addSqlStrAndReplace(crtUser) + ",");
                    sql.append(Utils.addSqlStrAndReplace(DateUtils.dateTimesToStr(lastUpdateDttm)) + ",");
                    sql.append(Utils.addSqlStrAndReplace(lastUpdateUser) + ",");
                    sql.append(version + ",");
                    sql.append((enableFlag ? 1 : 0) + ",");
                    sql.append(Utils.addSqlStrAndReplace(name) + ",");
                    sql.append(Utils.addSqlStrAndReplace(bundel) + ",");
                    sql.append(Utils.addSqlStrAndReplace(groups) + ",");
                    sql.append(Utils.addSqlStrAndReplace(owner) + ",");
                    sql.append(Utils.addSqlStrAndReplace(description) + ",");
                    sql.append(Utils.addSqlStrAndReplace(inports) + ",");
                    sql.append(Utils.addSqlStrAndReplace((null != inPortType ? inPortType.name() : "")) + ",");
                    sql.append(Utils.addSqlStrAndReplace(outports) + ",");
                    sql.append(Utils.addSqlStrAndReplace((null != outPortType ? outPortType.name() : "")) + ",");
                    sql.append(Utils.addSqlStrAndReplace((null != state ? state.name() : "")) + ",");
//                    sql.append(Utils.addSqlStrAndReplace(DateUtils.dateTimesToStr(startTime)) + ",");
//                    sql.append(Utils.addSqlStrAndReplace(DateUtils.dateTimesToStr(endTime)) + ",");
                    sql.append(Utils.addSqlStrAndReplace(pageId) + ",");
                    sql.append(Utils.addSqlStrAndReplace(process.getId()));
                    if (i != processStops.size()) {
                        sql.append("),");
                    } else {
                        sql.append(")");
                    }
                }
            }
            sqlStr = sql.toString() + ";";
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
            sql.WHERE("FK_FLOW_PROCESS_ID = " + Utils.addSqlStr(processId));

            sqlStr = sql.toString() + ";";
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
            sql.WHERE("FK_FLOW_PROCESS_ID = " + Utils.addSqlStr(processId));
            sql.WHERE("PAGE_ID = " + Utils.addSqlStr(pageId));

            sqlStr = sql.toString() + ";";
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
            String pageIdsStr = Utils.strArrayToStr(pageIds);
            if (StringUtils.isNotBlank(pageIdsStr)) {

                pageIdsStr = pageIdsStr.replace(",", "','");
                pageIdsStr = "'" + pageIdsStr + "'";
                SQL sql = new SQL();
                sql.SELECT("*");
                sql.FROM("FLOW_PROCESS_STOP");
                sql.WHERE("enable_flag = 1");
                sql.WHERE("FK_FLOW_PROCESS_ID = " + Utils.addSqlStr(processId));
                sql.WHERE("PAGE_ID IN ( " + pageIdsStr + ")");

                sqlStr = sql.toString() + ";";
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
            sql.WHERE("FK_FLOW_PROCESS_ID = " + Utils.addSqlStr(processId));
            sql.WHERE("NAME = " + Utils.addSqlStr(name));

            sqlStr = sql.toString() + ";";
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
                sql.SET("LAST_UPDATE_DTTM = " + Utils.addSqlStr(lastUpdateDttmStr));
                sql.SET("LAST_UPDATE_USER = " + Utils.addSqlStr(lastUpdateUser));
                sql.SET("VERSION = " + (version + 1));

                // 处理其他字段
                if (null != enableFlag) {
                    sql.SET("ENABLE_FLAG = " + (enableFlag ? 1 : 0));
                }
                if (StringUtils.isNotBlank(name)) {
                    sql.SET("NAME = " + Utils.addSqlStr(name));
                }
                if (StringUtils.isNotBlank(bundel)) {
                    sql.SET("BUNDEL = " + Utils.addSqlStr(bundel));
                }
                if (StringUtils.isNotBlank(groups)) {
                    sql.SET("GROUPS = " + Utils.addSqlStr(groups));
                }
                if (StringUtils.isNotBlank(owner)) {
                    sql.SET("OWNER = " + Utils.addSqlStr(owner));
                }
                if (StringUtils.isNotBlank(description)) {
                    sql.SET("DESCRIPTION = " + Utils.addSqlStr(description));
                }
                if (StringUtils.isNotBlank(inports)) {
                    sql.SET("INPORTS = " + Utils.addSqlStr(inports));
                }
                if (null != inPortType) {
                    sql.SET("IN_PORT_TYPE = " + Utils.addSqlStr(inPortType.name()));
                }
                if (StringUtils.isNotBlank(outports)) {
                    sql.SET("OUTPORTS = " + Utils.addSqlStr(outports));
                }
                if (null != outPortType) {
                    sql.SET("OUT_PORT_TYPE = " + Utils.addSqlStr(outPortType.name()));
                }
                if (null != state) {
                    sql.SET("state = " + Utils.addSqlStr(state.name()));
                }
                if (null != startTime) {
                    String startTimeStr = DateUtils.dateTimesToStr(startTime);
                    if (StringUtils.isNotBlank(startTimeStr)) {
                        sql.SET("START_TIME = " + Utils.addSqlStr(startTimeStr));
                    }
                }
                if (null != endTime) {
                    String endTimeStr = DateUtils.dateTimesToStr(endTime);
                    if (StringUtils.isNotBlank(endTimeStr)) {
                        sql.SET("end_time = " + Utils.addSqlStr(endTimeStr));
                    }
                }
                if (StringUtils.isNotBlank(pageId)) {
                    sql.SET("PAGE_ID = " + Utils.addSqlStr(pageId));
                }

                sql.WHERE("ENABLE_FLAG = 1");
                sql.WHERE("VERSION = " + version);
                sql.WHERE("ID = " + Utils.addSqlStr(id));

                sqlStr = sql.toString() + ";";
            }
        }
        return sqlStr;
    }

    public String updateEnableFlagByProcessId(String processId) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(processId)) {
            SQL sql = new SQL();
            sql.UPDATE("FLOW_PROCESS_STOP");
            sql.SET("enable_flag = 0");
            sql.WHERE("enable_flag = 1");
            sql.WHERE("FK_FLOW_PROCESS_ID = " + Utils.addSqlStr(processId));

            sqlStr = sql.toString() + ";";
        }
        return sqlStr;
    }
}
