package cn.cnic.component.schedule.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.schedule.entity.FileSchedule;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

@Mapper
public class FileScheduleMapperProvider {

    //    private Long id;
    private String lastUpdateUser;
    private String lastUpdateDttmStr;
    private long version;
    private int enableFlag;
    private String name;
    private String description;
    private String fileDict;
    private String filePrefix;
    private String fileSuffix;
    private String associateId;
    private Integer associateType; //0-Flow
    private String stopId;
    private String stopName;
    private String propertyId;
    private String propertyName;
    private Integer triggerMode;
    private Integer serialRule;
    private String regex;
    private Integer serialOrder;
    private String processId;
    private String filePath;
    private Integer fileSize;
    private Date fileLastModifyTime;
    private Integer state; //状态：0-初始化（INIT） 1-正在运行（RUNNING） 2-暂停（STOP）

    private boolean preventSQLInjectionSchedule(FileSchedule fileSchedule) {
        if (null == fileSchedule || StringUtils.isBlank(fileSchedule.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        String lastUpdateDttm = DateUtils.dateTimesToStr(null != fileSchedule.getLastUpdateDttm() ? fileSchedule.getLastUpdateDttm() : new Date());
//        this.id = SqlUtils.preventSQLInjection(schedule.getId());
        this.enableFlag = ((null != fileSchedule.getEnableFlag() && fileSchedule.getEnableFlag()) ? 1 : 0);
        this.version = (null != fileSchedule.getVersion() ? fileSchedule.getVersion() : 0L);
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttm);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(fileSchedule.getLastUpdateUser());

        this.name = SqlUtils.preventSQLInjection(fileSchedule.getName());
        this.description = SqlUtils.preventSQLInjection(fileSchedule.getDescription());
        this.fileDict = SqlUtils.preventSQLInjection(fileSchedule.getFileDict());
        this.filePrefix = SqlUtils.preventSQLInjection(fileSchedule.getFilePrefix());
        this.fileSuffix = SqlUtils.preventSQLInjection(fileSchedule.getFileSuffix());
        this.associateId = SqlUtils.preventSQLInjection(fileSchedule.getAssociateId());
        this.associateType = fileSchedule.getAssociateType();
        this.stopId = SqlUtils.preventSQLInjection(fileSchedule.getStopId());
        this.stopName = SqlUtils.preventSQLInjection(fileSchedule.getStopName());
        this.propertyId = SqlUtils.preventSQLInjection(fileSchedule.getPropertyId());
        this.propertyName = SqlUtils.preventSQLInjection(fileSchedule.getPropertyName());
        this.triggerMode = fileSchedule.getTriggerMode();
        this.serialRule = fileSchedule.getSerialRule();
        this.regex = SqlUtils.preventSQLInjection(fileSchedule.getRegex());
        this.serialOrder = fileSchedule.getSerialOrder();
        this.processId = SqlUtils.preventSQLInjection(fileSchedule.getProcessId());
        this.filePath = SqlUtils.preventSQLInjection(fileSchedule.getFilePath());
        this.fileSize = fileSchedule.getFileSize();
        this.fileLastModifyTime = fileSchedule.getFileLastModifyTime();
        this.state = fileSchedule.getState();


        return true;
    }

    private void resetSchedule() {
//        this.id = null;
        this.lastUpdateUser = null;
        this.lastUpdateDttmStr = null;
        this.version = 0L;
        this.enableFlag = 1;
        this.name = null;
        this.description = null;
        this.fileDict = null;
        this.filePrefix = null;
        this.fileSuffix = null;
        this.associateId = null;
        this.associateType = null;
        this.stopId = null;
        this.stopName = null;
        this.propertyId = null;
        this.propertyName = null;
        this.triggerMode = null;
        this.serialRule = null;
        this.regex = null;
        this.serialOrder = null;
        this.processId = null;
        this.filePath = null;
        this.fileSize = null;
        this.fileLastModifyTime = null;
        this.state = null;
    }

    /**
     * insert schedule
     *
     * @param fileSchedule
     * @return string sql
     */
    public String insert(FileSchedule fileSchedule) {
        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionSchedule(fileSchedule);
        if (flag) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO file_schedule ");
            strBuf.append("( ");
            strBuf.append(SqlUtils.baseFieldNameWithNoId() + ", ");
            strBuf.append("name, ");
            strBuf.append("description, ");
            strBuf.append("file_dict, ");
            strBuf.append("file_prefix, ");
            strBuf.append("file_suffix, ");
            strBuf.append("associate_id, ");
            strBuf.append("associate_type, ");
            strBuf.append("stop_id, ");
            strBuf.append("stop_name, ");
            strBuf.append("property_id, ");
            strBuf.append("property_name, ");
            strBuf.append("trigger_mode, ");
            strBuf.append("serial_rule, ");
            strBuf.append("regex, ");
            strBuf.append("serial_order, ");
            strBuf.append("process_id, ");
            strBuf.append("file_path, ");
            strBuf.append("file_size, ");
            strBuf.append("file_last_modify_time, ");
            strBuf.append("state ");
            strBuf.append(") ");

            strBuf.append("values ");
            strBuf.append("(");
            strBuf.append(SqlUtils.baseFieldValuesNoLongId(fileSchedule) + ", ");
            strBuf.append(this.name + ", ");
            strBuf.append(this.description + ", ");
            strBuf.append(this.fileDict + ", ");
            strBuf.append(this.filePrefix + ", ");
            strBuf.append(this.fileSuffix + ", ");
            strBuf.append(this.associateId + ", ");
            strBuf.append(this.associateType + ",");
            strBuf.append(this.stopId + ",");
            strBuf.append(this.stopName + ",");
            strBuf.append(this.propertyId + ",");
            strBuf.append(this.propertyName + ",");
            strBuf.append(this.triggerMode + ",");
            strBuf.append(this.serialRule + ",");
            strBuf.append(this.regex + ",");
            strBuf.append(this.serialOrder + ",");
            strBuf.append(this.processId + ",");
            strBuf.append(this.filePath + ",");
            strBuf.append(this.fileSize + ",");
            strBuf.append((null == this.fileLastModifyTime ? "null" : SqlUtils.preventSQLInjection(DateUtils.dateToStr(this.fileLastModifyTime))) + ",");
            strBuf.append(this.state);
            strBuf.append(")");
            this.resetSchedule();
            sqlStr = strBuf.toString() + ";";
        }
        return sqlStr;
    }

    /**
     * update schedule
     *
     * @param fileSchedule schedule
     * @return string sql
     */
    public String update(FileSchedule fileSchedule) {

        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionSchedule(fileSchedule);
        if (flag && null != fileSchedule.getId()) {
            SQL sql = new SQL();
            // INSERT_INTO brackets is table name
            sql.UPDATE("file_schedule");
            // The first string in the SET is the name of the field corresponding to the table in the database
            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
            sql.SET("enable_flag = " + enableFlag);
            sql.SET("name = " + this.name);
            sql.SET("description = " + this.description);
            sql.SET("file_dict = " + this.fileDict);
            sql.SET("file_prefix = " + this.filePrefix);
            sql.SET("file_suffix = " + this.fileSuffix);
            sql.SET("associate_id = " + this.associateId);
            sql.SET("associate_type = " + this.associateType);
            sql.SET("stop_id = " + this.stopId);
            sql.SET("stop_name = " + this.stopName);
            sql.SET("property_id = " + this.propertyId);
            sql.SET("property_name = " + this.propertyName);
            sql.SET("trigger_mode = " + this.triggerMode);
            sql.SET("serial_rule = " + this.serialRule);
            sql.SET("regex = " + this.regex);
            sql.SET("serial_order = " + this.serialOrder);
            sql.SET("process_id = " + this.processId);
            sql.SET("file_path = " + this.filePath);
            sql.SET("file_size = " + this.fileSize);
            sql.SET("file_last_modify_time = " + (null == this.fileLastModifyTime ? "null" : SqlUtils.preventSQLInjection(DateUtils.dateToStr(this.fileLastModifyTime))));
            sql.SET("state = " + this.state);
            sql.WHERE("id = " + fileSchedule.getId());
            sqlStr = sql.toString();
        }
            this.resetSchedule();
            return sqlStr;
        }


    }
