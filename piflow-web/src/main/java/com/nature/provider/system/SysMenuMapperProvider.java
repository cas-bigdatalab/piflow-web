package com.nature.provider.system;

import com.nature.base.util.SqlUtils;
import com.nature.common.Eunm.SysRoleType;
import com.nature.component.system.model.SysMenu;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public class SysMenuMapperProvider {


    /**
     * getSysMenuList
     *
     * @param role
     * @return
     */
    public String getSysMenuList(String role) {
        String sqlStr = "select 0";
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
}
