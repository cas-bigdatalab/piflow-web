package com.nature.provider.sysUser;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
import com.nature.common.Eunm.ProcessState;
import com.nature.component.process.model.Process;
import com.nature.component.sysUser.model.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.Map;

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
            String role = sysUser.getRole();

            SQL sql = new SQL();
            // INSERT_INTO括号中为数据库表名
            sql.INSERT_INTO("SYS_USER");
            // value中的第一个字符串为数据库中表对应的字段名
            // 除数字类型的字段外其他类型必须加单引号

            //先处理修改必填字段
            if (null == crtDttm) {
                crtDttm = new Date();
            }
            if (null != crtUser) {
                crtUser = "-1";
            }
            if (null == lastUpdateDttm) {
                lastUpdateDttm = new Date();
            }
            if (null != lastUpdateUser) {
                lastUpdateUser = "-1";
            }
            if (null == version) {
                version = 0L;
            }
            if (null != enableFlag) {
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
            if (null != username) {
                sql.VALUES("USERNAME", Utils.addSqlStrAndReplace(username));
            }
            if (null != password) {
                sql.VALUES("PASSWORD", Utils.addSqlStrAndReplace(password));
            }
            if (null != name) {
                sql.VALUES("NAME", Utils.addSqlStrAndReplace(name));
            }
            if (null != age) {
                sql.VALUES("AGE", age + "");
            }
            if (null != sex) {
                sql.VALUES("SEX", Utils.addSqlStrAndReplace(sex));
            }
            if (null != role) {
                sql.VALUES("ROLE", Utils.addSqlStrAndReplace(role));
            }

            sqlStr = sql.toString() + ";";
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
        String sqlStr = "SELECT 0";
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
            String role = sysUser.getRole();

            SQL sql = new SQL();
            sql.UPDATE("SYS_USER");

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
                sql.SET("ENABLE_FLAG=" + (enableFlag ? 1 : 0));
            }
            if (null != username) {
                sql.SET("USERNAME=" + Utils.addSqlStrAndReplace(username));
            }
            if (null != password) {
                sql.SET("PASSWORD=" + Utils.addSqlStrAndReplace(password));
            }
            if (null != name) {
                sql.SET("NAME=" + Utils.addSqlStrAndReplace(name));
            }
            if (null != age) {
                sql.SET("AGE=" + age);
            }
            if (null != sex) {
                sql.SET("SEX=" + Utils.addSqlStrAndReplace(sex));
            }
            if (null != role) {
                sql.SET("ROLE=" + Utils.addSqlStrAndReplace(role));
            }
            sql.WHERE("VERSION = " + version);
            sql.WHERE("id = " + Utils.addSqlStr(id));
            if (StringUtils.isNotBlank(id)) {
                sqlStr = sql.toString() + ";";
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
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(name)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("SYS_USER");
            sql.WHERE("NAME LIKE '%" + name + "%'");
            sqlStr = sql.toString() + ";";
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
            sql.FROM("SYS_USER");
            sql.WHERE("NAME = '" + name + "'");
            sqlStr = sql.toString() + ";";
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
            sql.FROM("SYS_USER");
            sql.WHERE("USERNAME = '" + userName + "'");
            sqlStr = sql.toString() + ";";
        }
        return sqlStr;
    }

    /**
     * getUserList
     *
     * @return
     */
    public String getUserList() {
        String sqlStr = "SELECT * FROM SYS_USER";
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
            sql.DELETE_FROM("SYS_USER");
            sql.WHERE("USERNAME = '" + id + "'");
            sqlStr = sql.toString() + ";";
        }
        return sqlStr;
    }
}
