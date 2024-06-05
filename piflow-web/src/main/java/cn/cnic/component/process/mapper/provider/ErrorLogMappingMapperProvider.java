package cn.cnic.component.process.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.process.entity.ErrorLogMapping;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class ErrorLogMappingMapperProvider {

    private Long id;
    private Date lastUpdateDttm;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;

    private String name;//名称
    private String originAbstract;//原始报错摘要
    private String explainZh;//中文解释映射
    private String regexPattern;//关键词匹配

    private boolean preventSQLInjection(ErrorLogMapping errorLogMapping) {
        if (null == errorLogMapping) {
            return false;
        }

        // Mandatory Field
        Boolean enableFlag = errorLogMapping.getEnableFlag();
        Long version = errorLogMapping.getVersion();
        this.id = errorLogMapping.getId();
        this.lastUpdateUser = SqlUtils.preventSQLInjection(errorLogMapping.getLastUpdateUser());
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        this.lastUpdateDttm = errorLogMapping.getLastUpdateDttm();

        // Selection field
        this.name = SqlUtils.preventSQLInjection(errorLogMapping.getName());
        this.originAbstract = SqlUtils.preventSQLInjection(errorLogMapping.getOriginAbstract());
        this.explainZh = SqlUtils.preventSQLInjection(errorLogMapping.getExplainZh());
        this.regexPattern = SqlUtils.preventSQLInjection(errorLogMapping.getRegexPattern());
        return true;
    }

    private void reset() {
        this.id = null;
        this.lastUpdateDttm = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.name = null;
        this.originAbstract = null;
        this.explainZh = null;
        this.regexPattern = null;
    }

    /**
     * add errorLogMapping
     *
     * @param errorLogMapping
     * @return
     */
    public String insert(ErrorLogMapping errorLogMapping) {
        String sqlStr = "";
        boolean flag = this.preventSQLInjection(errorLogMapping);
        if (flag) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("INSERT INTO error_log_mapping ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldNameWithNoId() + ", ");
            stringBuffer.append("name, ");
            stringBuffer.append("origin_abstract, ");
            stringBuffer.append("explain_zh, ");
            stringBuffer.append("regex_pattern ");
            stringBuffer.append(") ");
            stringBuffer.append("VALUES ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldValuesNoLongId(errorLogMapping)).append(", ");
            // handle other fields
            stringBuffer.append(this.name).append(", ");
            stringBuffer.append(this.originAbstract).append(", ");
            stringBuffer.append(this.explainZh).append(", ");
            stringBuffer.append(this.regexPattern);
            stringBuffer.append(" ) ");
            sqlStr = stringBuffer.toString();
            this.reset();
        }
        return sqlStr;
    }

    /**
     * update errorLogMapping
     *
     * @param errorLogMapping
     * @return
     */
    public String update(ErrorLogMapping errorLogMapping) {

        String sqlStr = "";
        this.preventSQLInjection(errorLogMapping);
        if (null != errorLogMapping) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("error_log_mapping");
            // The first string in the SET is the name of the field corresponding to the table in the database
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.SET("last_update_user = " + lastUpdateUser);
            if (errorLogMapping.getVersion() != null) {
                sql.SET("version = " + (version + 1));
            }

            // handle other fields
//            sql.SET("enable_flag = " + enableFlag);
            sql.SET("name = " + name);
            sql.SET("origin_abstract = " + originAbstract);
            sql.SET("explain_zh = " + explainZh);
            sql.SET("regex_pattern = " + regexPattern);
            sql.WHERE("id = " + id);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * delete errorLogMapping
     *
     * @param errorLogMapping
     * @return
     */
    public String delete(ErrorLogMapping errorLogMapping) {

        String sqlStr = "";
        this.preventSQLInjection(errorLogMapping);
        if (null != errorLogMapping) {
            SQL sql = new SQL();

            sql.UPDATE("error_log_mapping");
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("enable_flag = " + this.enableFlag);

            sql.WHERE("id = " + id);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }


}
