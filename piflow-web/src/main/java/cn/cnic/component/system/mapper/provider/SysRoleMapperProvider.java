package cn.cnic.component.system.mapper.provider;

import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.system.entity.SysRole;
import cn.cnic.component.system.vo.SysUserVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

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
            strBuf.append("select * ");
            strBuf.append("from sys_role ");
            strBuf.append("where ");
            strBuf.append("fk_sys_user_id = '" + sysUserId + "'");
            strSql = strBuf.toString();
        }
        return strSql;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String insertSysRoleList(Map map) {
        List<SysRole> roles = (List<SysRole>) map.get("roles");
        String userId = (String) map.get("userId");
        if (null == roles || roles.isEmpty() || StringUtils.isBlank(userId)) {
            return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("INSERT INTO sys_role ");
        strBuf.append("(id,role,fk_sys_user_id)");
        strBuf.append("values");
        SysRole sysRole;
        for (int i = 0; i < roles.size(); i++) {
            sysRole = roles.get(i);
            if (null == sysRole) {
                continue;
            }
            if (i != 0 && roles.size() - 1 > i) {
                strBuf.append(",(");
            } else {
                strBuf.append("(");
            }
            strBuf.append(sysRole.getId() + ", ");
            strBuf.append(SqlUtils.preventSQLInjection(sysRole.getRole().name()) + ", ");
            strBuf.append(SqlUtils.preventSQLInjection(userId));
            strBuf.append(")");

        }
        return strBuf.toString();

    }
    
    /**
     * getSysRoleBySysUserId
     *
     * @param sysUserId
     * @return
     */
    public String getSysRoleBySysUserId(String sysUserId) {
        String strSql = "SELECT 0";
        if (StringUtils.isNotBlank(sysUserId)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("SELECT * ");
            strBuf.append("FROM sys_role ");
            strBuf.append("WHERE ");
            strBuf.append("fk_sys_user_id = '" + sysUserId + "' ORDER BY role ASC LIMIT 1");
            strSql = strBuf.toString();
        }
        return strSql;
    }

    public String updateRole(SysUserVo sysUserVo) {
        SQL sql = new SQL();
        sql.UPDATE("sys_role");
        sql.SET("role = " + SqlUtils.preventSQLInjection(sysUserVo.getRole().getRole().name()));
        sql.WHERE("fk_sys_user_id = " + SqlUtils.preventSQLInjection(sysUserVo.getId()));
        return sql.toString();
    }

}
