package cn.cnic.component.system.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.system.entity.SysUser;

import java.util.Date;


public class SysUserMapperProvider {

    private String id;
    private String username;
    private String password;
    private String name;
    private Integer age;
    private String lastUpdateUser;
    private String lastUpdateDttmStr;
    private long version;
    private boolean enableFlag;
    private String sex;
    private Byte status;
    private String lastLoginIp;
    private String developerAccessKey;
    private String phoneNumber;
    private String email;

    private boolean preventSQLInjectionSysUser(SysUser sysUser) {
        if (null == sysUser || StringUtils.isBlank(sysUser.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        this.username = SqlUtils.preventSQLInjection(sysUser.getUsername());
        this.password = SqlUtils.preventSQLInjection(sysUser.getPassword());
        this.name = SqlUtils.preventSQLInjection(sysUser.getName());
        this.age = sysUser.getAge();
        this.sex = SqlUtils.preventSQLInjection(sysUser.getSex());
        this.id = SqlUtils.preventSQLInjection(sysUser.getId());

        // Selection field
        this.enableFlag = (null != sysUser.getEnableFlag() && sysUser.getEnableFlag());
        this.version = (null != sysUser.getVersion() ? sysUser.getVersion() : 0L);
        this.status = (null != sysUser.getStatus() ? sysUser.getStatus() : 0);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(sysUser.getLastUpdateUser());
        this.lastLoginIp = SqlUtils.preventSQLInjection(sysUser.getLastLoginIp());
        String lastUpdateDttm = DateUtils.dateTimesToStr(null != sysUser.getLastUpdateDttm() ? sysUser.getLastUpdateDttm() : new Date());
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttm);
        this.developerAccessKey = SqlUtils.preventSQLInjection(sysUser.getDeveloperAccessKey());
        this.phoneNumber = SqlUtils.preventSQLInjection(sysUser.getPhoneNumber());
        this.email = SqlUtils.preventSQLInjection(sysUser.getEmail());

        return true;
    }

    private void resetSysUser() {
        this.id = null;
        this.username = null;
        this.password = null;
        this.lastUpdateDttmStr = null;
        this.version = 0L;
        this.status = 0;
        this.enableFlag = true;
        this.name = null;
        this.age = null;
        this.sex = null;
        this.lastLoginIp = null;
        this.phoneNumber = null;
        this.email = null;
    }

    /**
     * insert sysUser
     *
     * @param sysUser sysUser
     * @return string sql
     */
    public String insertSysUser(SysUser sysUser) {
        boolean flag = this.preventSQLInjectionSysUser(sysUser);
        if (!flag) {
            return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("INSERT INTO sys_user ");

        strBuf.append("( ");
        strBuf.append(SqlUtils.baseFieldName() + ", ");
        strBuf.append("username, password, name, age, sex, status, developer_access_key, phone_number,email ");
        strBuf.append(") ");

        strBuf.append("values ");
        strBuf.append("(");
        strBuf.append(SqlUtils.baseFieldValues(sysUser) + ", ");
        strBuf.append(username + "," + password + "," + name + "," + age + "," + sex + "," + status + "," + developerAccessKey + "," + phoneNumber + "," + email);
        strBuf.append(")");

        this.resetSysUser();
        return strBuf.toString() + ";";
    }


    /**
     * update sysUser
     *
     * @param sysUser sysUser
     * @return string sql
     */

    public String updateSysUser(SysUser sysUser) {
        boolean flag = this.preventSQLInjectionSysUser(sysUser);
        if (!flag) {
            return "SELECT 0";
        }
        if (StringUtils.isNotBlank(this.username) && !StringUtils.equals(this.username, "'admin'")) {

        }
        SQL sql = new SQL();
        // INSERT_INTO brackets is table name
        sql.UPDATE("sys_user");
        // The first string in the SET is the name of the field corresponding to the table in the database
        sql.SET("last_update_dttm = " + lastUpdateDttmStr);
        sql.SET("last_update_user = " + lastUpdateUser);
        sql.SET("version = " + (version + 1));

        // handle other fields
        sql.SET("enable_flag = " + enableFlag);
        sql.SET("username = " + this.username);
        sql.SET("name = " + this.name);
        if (StringUtils.isNotBlank(this.password)) {
            sql.SET("password = " + this.password);
        }
        if (null != status) {
            sql.SET("status = " + this.status);
        }
        if(StringUtils.isNotBlank(lastLoginIp)){
            sql.SET("last_login_ip = " + this.lastLoginIp);
        }
        if(StringUtils.isNotBlank(developerAccessKey)){
            sql.SET("developer_access_key = " + this.developerAccessKey);
        }
        sql.SET("phone_number = " + this.phoneNumber);
        sql.SET("email = " + this.email);

        sql.WHERE("version = " + this.version);
        sql.WHERE("id = " + this.id);

        this.resetSysUser();
        return sql.toString();
    }

    public String delUserById(boolean isAdmin, String username, String id) {
        if (StringUtils.isBlank(id)) {
            return "SELECT 0";
        }
        if (StringUtils.isBlank(username)) {
            return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        String lastUpdateDttm = DateUtils.dateTimesToStr(new Date());
        strBuf.append("update sys_user set ");
        strBuf.append("enable_flag = 0 , ");
        strBuf.append("last_update_user = " + SqlUtils.preventSQLInjection(username) + " , ");
        strBuf.append("last_update_dttm = " + SqlUtils.preventSQLInjection(lastUpdateDttm) + " ");
        strBuf.append("last_login_ip = " + SqlUtils.preventSQLInjection(lastLoginIp) + " ");
        strBuf.append("where ");
        strBuf.append("enable_flag = 1 ");
        strBuf.append("and id = " + SqlUtils.preventSQLInjection(id) + " ");
        if (!isAdmin) {
            strBuf.append("and crt_user = " + SqlUtils.preventSQLInjection(username));
        }
        String sqlStr = strBuf.toString();
        return sqlStr;
    }

    public String getSysUserById(boolean isAdmin, String username, String id) {
        String sqlStr = "SELECT 0";
        if (isAdmin && StringUtils.isNotBlank(id)) {
            StringBuffer sqlStrbuf = new StringBuffer();
            sqlStrbuf.append("SELECT user.*,role.role ");
            sqlStrbuf.append("FROM sys_user user, sys_role role ");
            sqlStrbuf.append("WHERE enable_flag = 1 and user.id = role.fk_sys_user_id ");
            sqlStrbuf.append("AND ");
            sqlStrbuf.append("user.id = " + SqlUtils.addSqlStrAndReplace(id) + " ");
            sqlStr = sqlStrbuf.toString();
        }
        return sqlStr;
    }

    /**
     * findUserByNameLike
     *
     * @param name
     * @return
     */
    public String findUserByNameLike(String name) {
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(name)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("sys_user");
            sql.WHERE("name like CONCAT('%'," + SqlUtils.preventSQLInjection(name) + ",'%') ");
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * findUserByName
     *
     * @param name
     * @return
     */
    public String findUserByName(String name) {
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(name)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("sys_user");
            sql.WHERE("name = '" + name + "'");
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * findUserByUserName
     *
     * @param userName
     * @return
     */
    public String findUserByUserName(String userName) {
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(userName)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("sys_user");
            sql.WHERE("username = '" + userName + "'");
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * getSysUserVoList
     *
     * @return
     */
    public String getSysUserVoList(boolean isAdmin, String username, String param) {
        if (!isAdmin) {
            return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("SELECT id, username, name, age, sex, crt_dttm, status, last_login_ip FROM sys_user WHERE enable_flag=1");
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("AND ( ");
            strBuf.append("name LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            strBuf.append("OR username LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            strBuf.append(") ");
        }
        String sqlStr = strBuf.toString();
        return sqlStr;
    }

    /**
     * deleteUser
     *
     * @param id
     * @return
     */
    public String deleteUser(String id) {
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(id)) {
            SQL sql = new SQL();
            sql.DELETE_FROM("sys_user");
            sql.WHERE("username = '" + id + "'");
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

}
