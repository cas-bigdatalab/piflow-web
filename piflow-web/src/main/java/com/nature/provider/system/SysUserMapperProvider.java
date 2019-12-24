package com.nature.provider.system;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SqlUtils;
import com.nature.component.system.model.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class SysUserMapperProvider {

    /**
     * addSysUser
     *
     * @param sysUser
     * @return
     */
    public String addSysUser(SysUser sysUser) {
        String sqlStr = "select 0";
        if (null != sysUser) {
            String id = sysUser.getId();
            String crtUser = sysUser.getCrtUser();
            Date crtDttm = sysUser.getCrtDttm();
            String lastUpdateUser = sysUser.getLastUpdateUser();
            Date lastUpdateDttm = sysUser.getLastUpdateDttm();
            Long version = sysUser.getVersion();
            Boolean enableFlag = sysUser.getEnableFlag();
            String username = sysUser.getUsername();
            String password = sysUser.getPassword();
            String name = sysUser.getName();
            Integer age = sysUser.getAge();
            String sex = sysUser.getSex();

            SQL sql = new SQL();
            // INSERT_INTO brackets is table name
            sql.INSERT_INTO("sys_user");
            // The first string in the value is the field name corresponding to the table in the database.
            // all types except numeric fields must be enclosed in single quotes

            //Process the required fields first
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
            sql.VALUES("id", SqlUtils.addSqlStrAndReplace(id));
            sql.VALUES("crt_dttm", SqlUtils.addSqlStrAndReplace(DateUtils.dateTimesToStr(crtDttm)));
            sql.VALUES("crt_user", SqlUtils.addSqlStrAndReplace(crtUser));
            sql.VALUES("last_update_dttm", SqlUtils.addSqlStrAndReplace(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.VALUES("last_update_user", SqlUtils.addSqlStrAndReplace(lastUpdateUser));
            sql.VALUES("version", version + "");
            sql.VALUES("enable_flag", (enableFlag ? 1 : 0) + "");

            // handle other fields
            if (null != username) {
                sql.VALUES("username", SqlUtils.addSqlStrAndReplace(username));
            }
            if (null != password) {
                sql.VALUES("password", SqlUtils.addSqlStrAndReplace(password));
            }
            if (null != name) {
                sql.VALUES("name", SqlUtils.addSqlStrAndReplace(name));
            }
            if (null != age) {
                sql.VALUES("age", age + "");
            }
            if (null != sex) {
                sql.VALUES("sex", SqlUtils.addSqlStrAndReplace(sex));
            }

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * updateSysUser
     *
     * @param sysUser
     * @return
     */
    public String updateSysUser(SysUser sysUser) {
        String sqlStr = "select 0";
        if (null != sysUser) {
            String id = sysUser.getId();
            String lastUpdateUser = sysUser.getLastUpdateUser();
            Date lastUpdateDttm = sysUser.getLastUpdateDttm();
            Long version = sysUser.getVersion();
            Boolean enableFlag = sysUser.getEnableFlag();
            String username = sysUser.getUsername();
            String password = sysUser.getPassword();
            String name = sysUser.getName();
            Integer age = sysUser.getAge();
            String sex = sysUser.getSex();

            SQL sql = new SQL();
            sql.UPDATE("sys_user");

            //Process the required fields first
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
            sql.SET("last_update_dttm = " + SqlUtils.addSqlStr(lastUpdateDttmStr));
            sql.SET("last_update_user = " + SqlUtils.addSqlStr(lastUpdateUser));
            sql.SET("version = " + (version + 1));

            // handle other fields
            if (null != enableFlag) {
                sql.SET("enable_flag=" + (enableFlag ? 1 : 0));
            }
            if (null != username) {
                sql.SET("username=" + SqlUtils.addSqlStrAndReplace(username));
            }
            if (null != password) {
                sql.SET("password=" + SqlUtils.addSqlStrAndReplace(password));
            }
            if (null != name) {
                sql.SET("name=" + SqlUtils.addSqlStrAndReplace(name));
            }
            if (null != age) {
                sql.SET("age=" + age);
            }
            if (null != sex) {
                sql.SET("sex=" + SqlUtils.addSqlStrAndReplace(sex));
            }
            sql.WHERE("version = " + version);
            sql.WHERE("id = " + SqlUtils.addSqlStr(id));
            if (StringUtils.isNotBlank(id)) {
                sqlStr = sql.toString();
            }
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
