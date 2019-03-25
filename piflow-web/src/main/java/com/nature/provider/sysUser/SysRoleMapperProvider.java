package com.nature.provider.sysUser;

import com.nature.base.util.DateUtils;
import com.nature.base.util.Utils;
import com.nature.component.sysUser.model.SysRole;
import com.nature.component.sysUser.model.SysUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
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
        String strSql = "SELECT 0";
        if (StringUtils.isNotBlank(sysUserId)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("SELECT * ");
            strBuf.append("FROM SYS_ROLE ");
            strBuf.append("WHERE ");
            strBuf.append("FK_SYS_USER_ID = '" + sysUserId + "'");
            strSql = strBuf.toString();
        }
        return strSql;
    }

    public String addSysRoleList(Map map) {
        String strSql = "SELECT 0";
        List<SysRole> list = (List<SysRole>) map.get("list");
        String sysUserId = (String) map.get("sysUserId");
        if (CollectionUtils.isNotEmpty(list) && StringUtils.isNotBlank(sysUserId)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO ");
            strBuf.append("SYS_ROLE ");
            strBuf.append("( ");
            strBuf.append("ROLE, ");
            strBuf.append("FK_SYS_USER_ID ");
            strBuf.append(") ");
            strBuf.append("VALUES ");
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
