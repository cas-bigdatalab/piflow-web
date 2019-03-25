package com.nature.base.util;

import com.nature.base.vo.UserVo;
import com.nature.common.Eunm.SysRoleType;
import com.nature.component.sysUser.model.SysRole;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;

public class SqlUtils {
    /**
     * uuid(32位的)
     *
     * @return
     */
    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * str(给字符串加单引)
     *
     * @param str
     * @return
     */
    public static String addSqlStr(String str) {
        if (StringUtils.isNotBlank(str)) {
            return "'" + str + "' ";
        } else {
            return "'' ";
        }
    }

    /**
     * str(给字符串加单引,并替换字符串中的单引为双单引)
     *
     * @param str
     * @return
     */
    public static String addSqlStrAndReplace(String str) {
        if (StringUtils.isNotBlank(str)) {
            return "'" + replaceString(str) + "' ";
        } else {
            return "'' ";
        }
    }

    /**
     * str(替换字符串中的单引为双单引)
     *
     * @param str
     * @return
     */
    public static String replaceString(String str) {
        if (null == str) {
            str = "";
        }
        str = str.replace("'", "''");
        return str;
    }

    public static String strArrayToStr(String[] strArray) {
        String str = "";
        if (null != strArray && strArray.length > 0) {
            for (int i = 0; i < strArray.length; i++) {
                if (StringUtils.isNotBlank(strArray[i])) {
                    str += strArray[i];
                    if (i < strArray.length - 1) {
                        str += ",";
                    }
                }
            }
        }
        return str;
    }

    public static String addQueryByUserRole(UserVo currentUser, boolean isAddAnd) {
        String str = "FAIL";
        if (null != currentUser) {
            str = "CRT_USER = '" + currentUser.getUsername() + "' ";
            if (isAddAnd) {
                str = ("AND " + str);
            }
            boolean isAdmin = false;
            List<SysRole> roles = currentUser.getRoles();
            if (CollectionUtils.isNotEmpty(roles)) {
                for (SysRole sysRole : roles) {
                    if (null != sysRole && SysRoleType.ADMIN == sysRole.getRole()) {
                        isAdmin = true;
                        break;
                    }
                }
            }
            if (isAdmin) {
                str = " ";
            }
        }

        return str;
    }
}
