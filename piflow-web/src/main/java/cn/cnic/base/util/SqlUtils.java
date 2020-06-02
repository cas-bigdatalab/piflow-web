package cn.cnic.base.util;

import cn.cnic.base.vo.UserVo;
import cn.cnic.common.Eunm.SysRoleType;
import cn.cnic.component.system.model.SysRole;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;

public class SqlUtils {
    /**
     * uuid(32-bit)
     *
     * @return
     */
    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * str(Add a single quote to a string)
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
     * str(Replace the single quote in the string as a double single quote)
     *
     * @param str
     * @return
     */
    public static String replaceString(String str) {
        if (null != str) {
            str = str.replace("'", "''");
        }
        return str;
    }

    /**
     * str(Add a single quote to the string and replace the single quote in the string with a double quote)
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
     * str(Add a single quote to the string and replace the single quote in the string with a double quote)
     *
     * @param str
     * @return
     */
    public static String addSqlStrLikeAndReplace(String str) {
        if (StringUtils.isNotBlank(str)) {
            return "'%" + replaceString(str) + "%' ";
        } else {
            return "'' ";
        }
    }

    public static String strArrayToStr(String[] strArray) {
        String str = "";
        if (null != strArray && strArray.length > 0) {
            for (int i = 0; i < strArray.length; i++) {
                if (StringUtils.isNotBlank(strArray[i])) {
                    str += addSqlStrAndReplace(strArray[i]);
                    if (i < strArray.length - 1) {
                        str += ",";
                    }
                }
            }
        }
        return str;
    }

    public static String addQueryByUserRole(boolean beforeAddAnd, boolean afterAddAnd) {
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        String str = "FAIL";
        if (null != currentUser) {
            str = "CRT_USER = '" + currentUser.getUsername() + "' ";
            if (beforeAddAnd) {
                str = ("AND " + str);
            }
            if (afterAddAnd) {
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

    public static String preventSQLInjection(String str) {
        String sqlStr = "null";
        if (null != str) {
            String replace = str.replace("'", "''");
            sqlStr = "'" + replace + "'";
        }
        return sqlStr;
    }

    public static String strListToStr(List<String> strArray) {
        String str = "";
        if (null != strArray && strArray.size() > 0) {
            for (int i = 0; i < strArray.size(); i++) {
                if (StringUtils.isNotBlank(strArray.get(i))) {
                    String replaceString = replaceString(strArray.get(i));
                    if (null != replaceString) {
                        str += addSqlStr(strArray.get(i));
                    }
                    if (i < strArray.size() - 1) {
                        str += ",";
                    }
                }
            }
        }
        return str;
    }
}
