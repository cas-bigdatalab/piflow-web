package cn.cnic.component.user.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.system.entity.SysRole;
import cn.cnic.component.system.entity.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;


public class AdminUserMapperProvider {

    private String id;
    private String username;
    private String password;
    private String name;
    private int age;
    private String lastUpdateUser;
    private String lastUpdateDttmStr;
    private long version;
    private boolean enableFlag;
    private String sex;
    private Byte status;
    private String lastLoginIp;
    private List<SysRole> roles;

    private boolean preventSQLInjectionUser(SysUser user) {
        if (null == user || StringUtils.isBlank(user.getLastUpdateUser())) {
            return false;
        }
        // Mandatory Field
        String lastUpdateDttm = DateUtils.dateTimesToStr(null != user.getLastUpdateDttm() ? user.getLastUpdateDttm() : new Date());
        this.username = SqlUtils.preventSQLInjection(user.getUsername());
        this.password = SqlUtils.preventSQLInjection(user.getPassword());
        this.name = SqlUtils.preventSQLInjection(user.getName());
        this.sex = SqlUtils.preventSQLInjection(user.getSex());
        this.id = SqlUtils.preventSQLInjection(user.getId());

        this.enableFlag = (null != user.getEnableFlag() && user.getEnableFlag());
        this.version = (null != user.getVersion() ? user.getVersion() : 0L);
        this.status = (null != user.getStatus() ? user.getStatus() : 0);
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttm);
        this.lastUpdateUser = SqlUtils.preventSQLInjection(user.getLastUpdateUser());
        this.lastLoginIp=SqlUtils.preventSQLInjection(user.getLastLoginIp());

        return true;
    }

    private void resetUser() {
        this.id = null;
        this.username = null;
        this.lastUpdateDttmStr = null;
        this.version = 0L;
        this.status = 0;
        this.enableFlag = true;
        this.name = null;
        this.age = 0;
        this.sex = null;
        this.lastLoginIp = null;
    }

    /**
     * insert user
     *
     * @param user user
     * @return string sql
     */
    public String insert(SysUser user) {
        if (null == user) {
            return "SELECT 0";
        }
        this.preventSQLInjectionUser(user);

        StringBuffer strBuf = new StringBuffer();
        strBuf.append("INSERT INTO sys_user ");

        strBuf.append("( ");
        strBuf.append(SqlUtils.baseFieldName() + ", ");
        strBuf.append("username, password, name, age, sex ");
        strBuf.append(") ");

        strBuf.append("values ");
        strBuf.append("(");
        strBuf.append(SqlUtils.baseFieldValues(user) + ", ");
        strBuf.append(username + "," + password + "," + name + "," + age + "," + sex);
        strBuf.append(")");
        this.resetUser();
        return strBuf.toString() + ";";
    }


    public String getUserById(boolean isAdmin, String username, String id) {
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



    public String getUserList(boolean isAdmin, String username, String param) {
        if (!isAdmin) {
            return "SELECT 0 from sys_user ";
        }
        StringBuffer sqlStrbuf = new StringBuffer();
        sqlStrbuf.append("SELECT user.*,role.role ");
        sqlStrbuf.append("FROM sys_user user,sys_role role ");
        sqlStrbuf.append("WHERE enable_flag = 1 AND  user.id = role.fk_sys_user_id ");
        if (StringUtils.isNotBlank(param)) {
            sqlStrbuf.append("AND ");
            sqlStrbuf.append("( ");
            sqlStrbuf.append("name like CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') OR ");
            sqlStrbuf.append("username like CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') OR ");
            sqlStrbuf.append("crt_dttm like CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
//                sqlStrbuf.append(" like CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%') ");
            sqlStrbuf.append(") ");
        }
        sqlStrbuf.append("ORDER BY crt_dttm asc,last_update_dttm DESC");
        String sqlStr = sqlStrbuf.toString();

        return sqlStr;
    }

    /**
     * update user
     *
     * @param user user
     * @return string sql
     */

    public String update(SysUser user) {

        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionUser(user);
        if (flag && StringUtils.isNotBlank(this.username) && !StringUtils.equals(this.username, "'admin'")) {
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
            sql.SET("password = " + this.password);
            sql.SET("status = " + this.status);
            sql.SET("last_login_ip = " + this.lastLoginIp);

            sql.WHERE("version = " + this.version);
            sql.WHERE("id = " + this.id);
            sqlStr = sql.toString();
        }
        this.resetUser();
        return sqlStr;
    }

    public String delUserById(boolean isAdmin, String username, String id){
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

}
