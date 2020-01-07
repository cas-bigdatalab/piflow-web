package com.nature.provider.system;

import org.apache.commons.lang3.StringUtils;


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

}
