package cn.cnic.component.system.mapper.provider;

import cn.cnic.base.util.SqlUtils;
import cn.cnic.component.stopsComponent.model.StopsComponent;
import cn.cnic.component.system.model.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;


public class SysUserMapperProvider {

    private String username;
    private String password;
    private String name;
    private Integer age;
    private String sex;

    private void preventSQLInjectionSysUser(SysUser sysUser) {
        if (null != sysUser && StringUtils.isNotBlank(sysUser.getLastUpdateUser())) {
            // Selection field
            this.username = SqlUtils.preventSQLInjection(sysUser.getUsername());
            this.password = SqlUtils.preventSQLInjection(sysUser.getPassword());
            this.name = SqlUtils.preventSQLInjection(sysUser.getName());
            this.age = sysUser.getAge();
            this.sex = SqlUtils.preventSQLInjection(sysUser.getSex());
        }
    }

    private void resetSysUser() {
        this.username = null;
        this.password = null;
        this.name = null;
        this.age = null;
        this.sex = null;
    }


    /**
     * findUserByNameLike
     *
     * @param name
     * @return
     */
    public String findUserByNameLike(String name) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(name)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("sys_user");
            sql.WHERE("name like '%" + name + "%'");
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
        String sqlStr = "select 0";
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
        String sqlStr = "select 0";
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
     * getUserList
     *
     * @return
     */
    public String getUserList() {
        String sqlStr = "select* from sys_user";
        return sqlStr;
    }

    /**
     * deleteUser
     *
     * @param id
     * @return
     */
    public String deleteUser(String id) {
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(id)) {
            SQL sql = new SQL();
            sql.DELETE_FROM("sys_user");
            sql.WHERE("username = '" + id + "'");
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    public String insertSysUser(SysUser sysUser) {
        if (null == sysUser) {
            return "select 0";
        }
        this.preventSQLInjectionSysUser(sysUser);

        StringBuffer strBuf = new StringBuffer();
        strBuf.append("INSERT INTO sys_user ");

        strBuf.append("( ");
        strBuf.append(SqlUtils.baseFieldName() + ", ");
        strBuf.append("username, password, name, age, sex ");
        strBuf.append(") ");

        strBuf.append("values ");
        strBuf.append("(");
        strBuf.append(SqlUtils.baseFieldValues(sysUser) + ", ");
        strBuf.append(username + "," + password + "," + name + "," + age + "," + sex);
        strBuf.append(")");
        this.resetSysUser();
        return strBuf.toString() + ";";
    }
}
