package com.nature.provider;

import com.nature.base.util.DateUtils;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.util.Utils;
import com.nature.base.vo.UserVo;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Template;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class TemplateMapperProvider {

    /**
     * 新增Template
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

            // INSERT_INTO括号中为数据库表名
            sql.INSERT_INTO("flow_template");
            // value中的第一个字符串为数据库中表对应的字段名
            // 除数字类型的字段外其他类型必须加单引号


            //先处理修改必填字段
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
            sql.VALUES("ID", Utils.addSqlStr(id));
            sql.VALUES("CRT_DTTM", Utils.addSqlStr(DateUtils.dateTimesToStr(crtDttm)));
            sql.VALUES("CRT_USER", Utils.addSqlStr(crtUser));
            sql.VALUES("LAST_UPDATE_DTTM", Utils.addSqlStr(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.VALUES("LAST_UPDATE_USER", Utils.addSqlStr(lastUpdateUser));
            sql.VALUES("VERSION", version + "");
            sql.VALUES("ENABLE_FLAG", (enableFlag ? 1 : 0) + "");

            // 处理其他字段
            if (StringUtils.isNotBlank(description)) {
                sql.VALUES("description", Utils.addSqlStr(description));
            }
            if (StringUtils.isNotBlank(name)) {
                sql.VALUES("NAME", Utils.addSqlStr(name));
            }
            if (StringUtils.isNotBlank(value)) {
                sql.VALUES("value", Utils.addSqlStr(value));
            }
            if (StringUtils.isNotBlank(path)) {
                sql.VALUES("path", Utils.addSqlStr(path));
            }
            if (null != flow) {
                sql.VALUES("fk_flow_id", Utils.addSqlStr(flow.getId()));
            }
            sqlStr = sql.toString();
        }
        return sqlStr;
    }
    
    /**
     * 根据updateEnableFlagById逻辑删除,设为无效
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
              sql.SET("ENABLE_FLAG = 0");
              sql.SET("last_update_user = " + Utils.addSqlStr(username) );
              sql.SET("last_update_dttm = " + Utils.addSqlStr(DateUtils.dateTimesToStr(new Date())) );
              sql.WHERE("ENABLE_FLAG = 1");
              sql.WHERE("id = " + Utils.addSqlStrAndReplace(id));

              sqlStr = sql.toString();
          }
          return sqlStr;
      }

}
