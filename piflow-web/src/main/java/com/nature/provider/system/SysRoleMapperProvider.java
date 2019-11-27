package com.nature.provider.system;

import com.nature.component.system.model.SysRole;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class SysRoleMapperProvider {


    /**
     * getSysRoleListBySysUserId
     *
     * @param sysUserId
     * @return
     */
    public String getSysRoleListBySysUserId(String sysUserId) {
        String strSql = "select 0";
        if (StringUtils.isNotBlank(sysUserId)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from sys_role ");
            strBuf.append("where ");
            strBuf.append("fk_sys_user_id = '" + sysUserId + "'");
            strSql = strBuf.toString();
        }
        return strSql;
    }

    public String addSysRoleList(Map map) {
        String strSql = "select 0";
        List<SysRole> list = (List<SysRole>) map.get("list");
        String sysUserId = (String) map.get("sysUserId");
        if (CollectionUtils.isNotEmpty(list) && StringUtils.isNotBlank(sysUserId)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("insert into ");
            strBuf.append("sys_role ");
            strBuf.append("( ");
            strBuf.append("role, ");
            strBuf.append("fk_sys_user_id ");
            strBuf.append(") ");
            strBuf.append("values ");
            for (int i = 0; i < list.size(); i++) {
                SysRole sysRole = list.get(i);
                if (null != sysRole) {
                    strBuf.append("(");
                    strBuf.append("'" + sysRole.getRole().getValue() + "',");
                    strBuf.append("'" + sysUserId + "'");
                    if (i != list.size() - 1) {
                        strBuf.append("),");
                    } else {
                        strBuf.append(")");
                    }
                }
            }
            strSql = strBuf.toString();
        }
        return strSql;
    }
}
