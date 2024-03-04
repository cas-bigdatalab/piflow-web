package cn.cnic.component.system.mapper.provider;

import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.dataProduct.entity.ProductUser;
import cn.cnic.component.system.entity.File;

import java.util.Date;

public class FileMapperProvider {

    private Long id;
    private Date lastUpdateDttm;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String fileName;
    private String fileType;
    private String filePath;
    private Integer associateType;
    private String associateId;

    private boolean preventSQLInjectionFile(File file) {
        if (null == file) {
            return false;
        }

        // Mandatory Field
        Boolean enableFlag = file.getEnableFlag();
        Long version = file.getVersion();
        this.id = file.getId();
        this.lastUpdateUser = SqlUtils.preventSQLInjection(file.getLastUpdateUser());
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        this.lastUpdateDttm = file.getLastUpdateDttm();

        // Selection field
        this.fileName = SqlUtils.preventSQLInjection(file.getFileName());
        this.fileType = SqlUtils.preventSQLInjection(file.getFileType());
        this.filePath = SqlUtils.preventSQLInjection(file.getFilePath());
        this.associateType = file.getAssociateType();
        this.associateId = SqlUtils.preventSQLInjection(file.getAssociateId());
        return true;
    }

    private void reset() {
        this.id = null;
        this.lastUpdateDttm = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.fileName = null;
        this.fileType = null;
        this.filePath = null;
        this.associateType =null;
        this.associateId = null;
    }

    /**
     * add File
     *
     * @param file
     * @return
     */
    public String insert(File file) {
        String sqlStr = "";
        boolean flag = this.preventSQLInjectionFile(file);
        if (flag) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("INSERT INTO file ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldName() + ", ");
            stringBuffer.append("file_name, ");
            stringBuffer.append("file_type, ");
            stringBuffer.append("file_path, ");
            stringBuffer.append("associate_type, ");
            stringBuffer.append("associate_id ");
            stringBuffer.append(") ");
            stringBuffer.append("VALUES ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldValuesWithLongId(file)).append(", ");
            // handle other fields
            stringBuffer.append(this.fileName).append(", ");
            stringBuffer.append(this.fileType).append(", ");
            stringBuffer.append(this.filePath).append(", ");
            stringBuffer.append(this.associateType).append(", ");
            stringBuffer.append(this.associateId);
            stringBuffer.append(" ) ");
            sqlStr = stringBuffer.toString();
            this.reset();
        }
        return sqlStr;
    }

//    /**
//     * update dataProductType
//     *
//     * @param flow
//     * @return
//     */
//    public String updateFlow(Flow flow) {
//
//        String sqlStr = "";
//        this.preventSQLInjectionFlow(flow);
//        if (null != flow) {
//            SQL sql = new SQL();
//
//            // INSERT_INTO brackets is table name
//            sql.UPDATE("flow");
//            // The first string in the SET is the name of the field corresponding to the table in the database
//            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
//            sql.SET("last_update_user = " + lastUpdateUser);
//            sql.SET("version = " + (version + 1));
//
//            // handle other fields
//            sql.SET("enable_flag = " + enableFlag);
//            sql.SET("description = " + description);
//            sql.SET("name = " + name);
//            sql.SET("uuid = " + uuid);
//            sql.SET("driver_memory = " + driverMemory);
//            sql.SET("executor_cores = " + executorCores);
//            sql.SET("executor_memory = " + executorMemory);
//            sql.SET("executor_number = " + executorNumber);
//            sql.WHERE("version = " + version);
//            sql.WHERE("id = " + id);
//            sqlStr = sql.toString();
//            if (StringUtils.isBlank(id)) {
//                sqlStr = "";
//            }
//        }
//        this.reset();
//        return sqlStr;
//    }
//
//    /**
//     * get flow list
//     *
//     * @return
//     */
//    public String getFlowList() {
//        String sqlStr = "";
//        SQL sql = new SQL();
//        sql.SELECT("*");
//        sql.FROM("flow");
//        sql.WHERE("enable_flag = 1");
//        sql.WHERE("is_example = 0");
//        sql.WHERE("fk_flow_group_id = null ");
//        sql.ORDER_BY(" crt_dttm desc  ");
//        sqlStr = sql.toString();
//        return sqlStr;
//    }

}
