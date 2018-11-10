package com.nature.base.util;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class Utils {
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
            return "'" + str + "'";
        } else {
            return "''";
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
}
