package cn.cnic.component.template.mapper.provider;

import cn.cnic.base.utils.SqlUtils;
import cn.cnic.common.Eunm.TemplateType;
import cn.cnic.component.template.entity.FlowTemplate;
import org.apache.commons.lang3.StringUtils;

public class FlowTemplateMapperProvider {

    private String sourceFlowName;
    private String templateTypeStr;
    private String name;
    private String description;
    private String path;
    private String url;

    private boolean preventSQLInjectionDataSource(FlowTemplate flowTemplate) {
        if (null == flowTemplate || StringUtils.isBlank(flowTemplate.getLastUpdateUser())) {
            return false;
        }

        // Selection field
        TemplateType templateType = flowTemplate.getTemplateType();
        this.templateTypeStr = SqlUtils.preventSQLInjection(null != templateType ? templateType.name() : "");
        this.sourceFlowName = SqlUtils.preventSQLInjection(flowTemplate.getSourceFlowName());
        this.name = SqlUtils.preventSQLInjection(flowTemplate.getName());
        this.description = SqlUtils.preventSQLInjection(flowTemplate.getDescription());
        this.path = SqlUtils.preventSQLInjection(flowTemplate.getPath());
        this.url = SqlUtils.preventSQLInjection(flowTemplate.getUrl());
        return true;
    }

    private void reset() {
        this.sourceFlowName = null;
        this.templateTypeStr = null;
        this.name = null;
        this.description = null;
        this.path = null;
        this.url = null;
    }

    public String insertFlowTemplate(FlowTemplate flowTemplate) {
        String sqlStr = "select 0";
        if (preventSQLInjectionDataSource(flowTemplate)) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO flow_template ");

            strBuf.append("( ");
            strBuf.append(SqlUtils.baseFieldName() + ", ");
            strBuf.append("source_flow_name, template_type, name, description, path, url ");
            strBuf.append(") ");

            strBuf.append("values ");
            strBuf.append("(");
            strBuf.append(SqlUtils.baseFieldValues(flowTemplate) + ", ");
            strBuf.append(this.sourceFlowName + ", ");
            strBuf.append(this.templateTypeStr + ", ");
            strBuf.append(this.name + ", ");
            strBuf.append(this.description + ", ");
            strBuf.append(this.path + ", ");
            strBuf.append(this.url + " ");
            strBuf.append(")");
            sqlStr = strBuf.toString();
        }
        this.reset();
        return sqlStr;
    }

    public String updateEnableFlagById(String id, boolean enableFlag) {
        if (StringUtils.isBlank(id)) {
            return "select 0";
        }
        int enableFlagInt = enableFlag ? 1 : 0;
        return "update flow_template ft set ft.enable_flag = " + enableFlagInt + " where ft.id = " + SqlUtils.preventSQLInjection(id);
    }

    public String getFlowTemplateList(String username, boolean isAdmin, String type) {
        String sqlStr = "select ft.* from flow_template ft where ft.enable_flag=1 ";
        if (!isAdmin) {
            sqlStr += "and crt_user=" + SqlUtils.preventSQLInjection(username) + " ";
        }
        if (StringUtils.isNotBlank(type)) {
        	String[] split = type.split(",");
            sqlStr += "and template_type in (" + SqlUtils.strArrayToStr(split) + ") ";
        }
        sqlStr += "order by crt_dttm desc ";
        return sqlStr;
    }

    public String getFlowTemplateListByParam(String username, boolean isAdmin, String param) {
        String sqlStr = "";
        param = StringUtils.isBlank(param) ? "" : param;
        String param_value = SqlUtils.preventSQLInjection(param);
        if (isAdmin) {
            sqlStr = "select ft.* from flow_template ft where ft.enable_flag=1 and (ft.name like CONCAT('%'," + param_value + ",'%'))";
        } else {
            String user_value = SqlUtils.preventSQLInjection(username);
            sqlStr = "select ft.* from flow_template ft where ft.enable_flag=1 and ft.crt_user=" + user_value + " and (ft.name like CONCAT('%'," + param_value + ",'%'))";
        }
        return sqlStr;
    }

}
