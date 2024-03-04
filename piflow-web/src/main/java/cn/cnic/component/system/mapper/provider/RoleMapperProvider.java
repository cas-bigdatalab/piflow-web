package cn.cnic.component.system.mapper.provider;

import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.dataProduct.entity.ProductUser;
import cn.cnic.component.system.entity.Role;

import java.util.Date;

public class RoleMapperProvider {

    private Long id;
    private Date lastUpdateDttm;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private Long parentId;
    private String code;
    private String name;
    private String description;

    private boolean preventSQLInjectionRole(Role role) {
        if (null == role) {
            return false;
        }

        // Mandatory Field
        Boolean enableFlag = role.getEnableFlag();
        Long version = role.getVersion();
        this.id = role.getId();
        this.lastUpdateUser = SqlUtils.preventSQLInjection(role.getLastUpdateUser());
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        this.lastUpdateDttm = role.getLastUpdateDttm();

        // Selection field
        this.parentId = role.getParentId();
        this.code = SqlUtils.preventSQLInjection(role.getCode());
        this.name = SqlUtils.preventSQLInjection(role.getName());
        this.description = SqlUtils.preventSQLInjection(role.getDescription());
        return true;
    }

    private void reset() {
        this.id = null;
        this.lastUpdateDttm = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.parentId = null;
        this.code = null;
        this.name = null;
        this.description =null;
    }

    /**
     * add Role
     *
     * @param role
     * @return
     */
    public String addRole(Role role) {
        String sqlStr = "";
        boolean flag = this.preventSQLInjectionRole(role);
        if (flag) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("INSERT INTO role ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldName() + ", ");
            stringBuffer.append("parent_id, ");
            stringBuffer.append("code, ");
            stringBuffer.append("name, ");
            stringBuffer.append("description ");
            stringBuffer.append(") ");
            stringBuffer.append("VALUES ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldValuesWithLongId(role)).append(", ");
            // handle other fields
            stringBuffer.append(this.parentId).append(", ");
            stringBuffer.append(this.code).append(", ");
            stringBuffer.append(this.name).append(", ");
            stringBuffer.append(this.description);
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
