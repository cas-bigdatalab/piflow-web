package com.nature.provider.template;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.SqlUtils;
import com.nature.base.vo.UserVo;
import com.nature.component.flow.model.Flow;
import com.nature.component.template.model.Template;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class TemplateMapperProvider {


    public String findTemPlateListPage(String param) {
        String sqlStr = "select 0";
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select* ");
        strBuf.append("from flow_template ");
        strBuf.append("where ");
        strBuf.append("enable_flag = 1 ");
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("and name like '%" + param + "%' ");
        }
        strBuf.append(SqlUtils.addQueryByUserRole(true, false));
        strBuf.append("order by crt_dttm desc ");
        sqlStr = strBuf.toString();
        return sqlStr;
    }

    /**
     * add Template
     *
     * @param template
     * @return
     */
    public String addTemplate(Template template) {
        String sqlStr = "";
        if (null != template) {
            String id = template.getId();
            String crtUser = template.getCrtUser();
            Date crtDttm = template.getCrtDttm();
            String lastUpdateUser = template.getLastUpdateUser();
            Date lastUpdateDttm = template.getLastUpdateDttm();
            Long version = template.getVersion();
            Boolean enableFlag = template.getEnableFlag();
            String name = template.getName();
            Flow flow = template.getFlow();
            String path = template.getPath();
            String value = template.getValue();
            String description = template.getDescription();

            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.INSERT_INTO("flow_template");
            // The first string in the value is the field name corresponding to the table in the database.
            // all types except numeric fields must be enclosed in single quotes


            //Process the required fields first
            if (null == crtDttm) {
                crtDttm = new Date();
            }
            if (StringUtils.isBlank(crtUser)) {
                crtUser = "-1";
            }
            if (null == lastUpdateDttm) {
                lastUpdateDttm = new Date();
            }
            if (StringUtils.isBlank(lastUpdateUser)) {
                lastUpdateUser = "-1";
            }
            if (null == version) {
                version = 0L;
            }
            if (null == enableFlag) {
                enableFlag = true;
            }
            sql.VALUES("id", SqlUtils.addSqlStr(id));
            sql.VALUES("crt_dttm", SqlUtils.addSqlStr(DateUtils.dateTimesToStr(crtDttm)));
            sql.VALUES("crt_user", SqlUtils.addSqlStr(crtUser));
            sql.VALUES("last_update_dttm", SqlUtils.addSqlStr(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.VALUES("last_update_user", SqlUtils.addSqlStr(lastUpdateUser));
            sql.VALUES("version", version + "");
            sql.VALUES("enable_flag", (enableFlag ? 1 : 0) + "");

            // handle other fields
            if (StringUtils.isNotBlank(description)) {
                sql.VALUES("description", SqlUtils.addSqlStr(description));
            }
            if (StringUtils.isNotBlank(name)) {
                sql.VALUES("name", SqlUtils.addSqlStr(name));
            }
            if (StringUtils.isNotBlank(value)) {
                sql.VALUES("value", SqlUtils.addSqlStr(value));
            }
            if (StringUtils.isNotBlank(path)) {
                sql.VALUES("path", SqlUtils.addSqlStr(path));
            }
            if (null != flow) {
                sql.VALUES("fk_flow_id", SqlUtils.addSqlStr(flow.getId()));
            }
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * Logical delete according to updateEnableFlagById
     *
     * @param id
     * @return
     */
    public String updateEnableFlagById(String id) {
        UserVo user = SessionUserUtil.getCurrentUser();
        String username = (null != user) ? user.getUsername() : "-1";
        String sqlStr = "select 0";
        if (StringUtils.isNotBlank(id)) {
            SQL sql = new SQL();
            sql.UPDATE("flow_template");
            sql.SET("enable_flag = 0");
            sql.SET("last_update_user = " + SqlUtils.addSqlStr(username));
            sql.SET("last_update_dttm = " + SqlUtils.addSqlStr(DateUtils.dateTimesToStr(new Date())));
            sql.WHERE("enable_flag = 1");
            sql.WHERE("id = " + SqlUtils.addSqlStrAndReplace(id));

            sqlStr = sql.toString();
        }
        return sqlStr;
    }

}
