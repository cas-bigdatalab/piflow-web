package cn.cnic.base.util;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

public class SqlUtils {

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
            str = str.replace("'", "''").replace("\\","\\\\");
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

    public static String baseFieldName() {
        return " id, crt_dttm, crt_user, last_update_dttm, last_update_user, enable_flag, version ";
    }

    public static String baseFieldValues(BaseHibernateModelUUIDNoCorpAgentId baseInfo) {
        if (null == baseInfo) {
            return " ";
        }
        StringBuffer valueStringBuffer = new StringBuffer();
        String id = StringUtils.isNotBlank(baseInfo.getId()) ? baseInfo.getId() : UUIDUtils.getUUID32();
        String crtUser = StringUtils.isNotBlank(baseInfo.getCrtUser()) ? baseInfo.getCrtUser() : "-1";
        String lastUpdateUser = baseInfo.getLastUpdateUser();
        Boolean enableFlag = baseInfo.getEnableFlag();
        Long version = baseInfo.getVersion();
        Date crtDttm = (null == baseInfo.getCrtDttm()) ? new Date() : baseInfo.getCrtDttm();
        String crtDttmStr = DateUtils.dateTimesToStr(crtDttm);
        Date lastUpdateDttm = (null == baseInfo.getLastUpdateDttm()) ? new Date() : baseInfo.getLastUpdateDttm();
        String lastUpdateDttmStr = DateUtils.dateTimesToStr(null != lastUpdateDttm ? lastUpdateDttm : new Date());


        valueStringBuffer.append(SqlUtils.preventSQLInjection(id) + ",");
        valueStringBuffer.append(SqlUtils.preventSQLInjection(crtDttmStr) + ",");
        valueStringBuffer.append(SqlUtils.preventSQLInjection(crtUser) + ",");
        valueStringBuffer.append(SqlUtils.preventSQLInjection(lastUpdateDttmStr) + ",");
        valueStringBuffer.append(SqlUtils.preventSQLInjection(lastUpdateUser) + ",");
        valueStringBuffer.append(((null != enableFlag && enableFlag) ? 1 : 0) + ",");
        valueStringBuffer.append((null != version ? version : 0L) + " ");
        return valueStringBuffer.toString();
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

    public static String preventSQLInjection(String str) {
        String sqlStr = "null";
        if (null != str) {
            String replace = str.replace("'", "''").replace("\\","\\\\");
            sqlStr = "'" + replace + "'";
        }
        return sqlStr;
    }
    /**
     * addSymbol
     * 
     * @param str
     * @param symbol
     * @param isbefore
     * @param after
     * @return
     */
    public static String addSymbol(String str, String symbol, boolean isbefore, boolean after) {
        if (null ==str) {
            return "null";
        }
        if (null == symbol) {
            return str;
        }
        String sqlStr = str;
        if (isbefore) {
            sqlStr = (symbol + sqlStr);
        }
        if (isbefore) {
            sqlStr = (sqlStr + symbol);
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
