package cn.cnic.component.system.mapper.provider;

import cn.cnic.base.util.SqlUtils;
import cn.cnic.common.Eunm.SysRoleType;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class SysMenuMapperProvider {


    /**
     * getSysMenuList
     *
     * @param role
     * @return
     */
    public String getSysMenuList(String role) {
        String sqlStr = "SELECT 0";
        if (StringUtils.isNotBlank(role)) {
            SQL sql = new SQL();
            sql.SELECT("*");
            sql.FROM("sys_menu");
            if (!SysRoleType.ADMIN.getValue().equals(role)) {
                sql.WHERE("menu_jurisdiction = " + SqlUtils.addSqlStrAndReplace(role));
            }
            sql.WHERE("enable_flag = 1");
            sql.ORDER_BY("menu_sort asc", "last_update_dttm desc");
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * getSampleMenuList
     *
     * @return
     */
    public String getSampleMenuList() {
        String sqlStr = "SELECT 0";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("sys_menu");
        sql.WHERE("enable_flag = 1");
        sql.WHERE("menu_parent = 'Example'");
        sqlStr = sql.toString();
        return sqlStr;
    }

    public static String deleteSampleMenuListByIds(Map<String, String[]> map) {
        String sqlStr = "SELECT 0";
        if (null == map) {
            return sqlStr;
        }
        String[] ids = map.get("ids");
        if (null == ids || ids.length <= 0) {
            return sqlStr;
        }
        SQL sql = new SQL();
        sql.DELETE_FROM("sys_menu");
        sql.WHERE("id in (" + SqlUtils.strArrayToStr(ids) + ")");
        sqlStr = sql.toString();
        return sqlStr;
    }
}
