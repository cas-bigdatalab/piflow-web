package cn.cnic.provider.system;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;


public class SysUserMapperProvider {

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
}
