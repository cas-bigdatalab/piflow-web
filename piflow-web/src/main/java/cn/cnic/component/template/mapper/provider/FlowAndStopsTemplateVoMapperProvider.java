package cn.cnic.component.template.mapper.provider;

import cn.cnic.base.util.DateUtils;
import cn.cnic.base.util.SqlUtils;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.component.template.entity.PropertyTemplateModel;
import cn.cnic.component.template.entity.StopTemplateModel;
import cn.cnic.component.template.entity.FlowTemplate;
import cn.cnic.component.template.vo.FlowTemplateModelVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class FlowAndStopsTemplateVoMapperProvider {

    /**
     * Insert list<Property> Note that the method of spelling sql must use Map to connect Param content to key value.
     *
     * @param map (Content: The key is propertyList and the value is List<Property>)
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String addPropertyList(Map map) {
        List<PropertyTemplateModel> propertyList = (List<PropertyTemplateModel>) map.get("propertyList");
        StringBuffer sqlStrBuffer = new StringBuffer();
        if (null != propertyList && propertyList.size() > 0) {
            sqlStrBuffer.append("insert into ");
            sqlStrBuffer.append("property_template ");
            sqlStrBuffer.append("(");
            sqlStrBuffer.append("id,");
            sqlStrBuffer.append("crt_dttm,");
            sqlStrBuffer.append("enable_flag,");
            sqlStrBuffer.append("name,");
            sqlStrBuffer.append("display_name,");
            sqlStrBuffer.append("description,");
            sqlStrBuffer.append("custom_value,");
            sqlStrBuffer.append("allowable_values,");
            sqlStrBuffer.append("property_required,");
            sqlStrBuffer.append("property_sensitive,");
            sqlStrBuffer.append("version,");
            sqlStrBuffer.append("fk_stops_id,");
            sqlStrBuffer.append("is_select,");
            sqlStrBuffer.append("crt_user");
            sqlStrBuffer.append(") ");
            sqlStrBuffer.append("values");
            int i = 0;
            for (PropertyTemplateModel property : propertyList) {
                i++;
                String id = property.getId();
                Date crtDttm = property.getCrtDttm();
                Boolean enableFlag = property.getEnableFlag();
                String name = property.getName();
                String displayName = property.getDisplayName();
                String description = "null".equals(property.getDescription()) ? null : property.getDisplayName();
                String customValue = property.getCustomValue();
                String allowableValues = property.getAllowableValues();
                Boolean required = property.getRequired();
                Boolean sensitive = property.getSensitive();
                Long version = property.getVersion();
                StopTemplateModel stops = property.getStopsVo();
                Boolean isSelect = property.getIsSelect();
                String crtUser = property.getCrtUser();
                // You can't make a mistake when you splice
                sqlStrBuffer.append("(");
                sqlStrBuffer.append(SqlUtils.addSqlStrAndReplace(id) + ",");
                sqlStrBuffer.append(SqlUtils.addSqlStr((crtDttm == null ? "" : DateUtils.dateTimesToStr(crtDttm))) + ",");
                sqlStrBuffer.append((enableFlag == null ? "1" : (enableFlag ? 1 : 0)) + ",");
                sqlStrBuffer.append(SqlUtils.addSqlStrAndReplace(name) + ",");
                sqlStrBuffer.append(SqlUtils.addSqlStrAndReplace(displayName) + ",");
                sqlStrBuffer.append(SqlUtils.addSqlStrAndReplace(description) + ",");
                sqlStrBuffer.append(SqlUtils.addSqlStrAndReplace(customValue) + ",");
                sqlStrBuffer.append(SqlUtils.addSqlStrAndReplace(allowableValues) + ",");
                sqlStrBuffer.append((required ? 1 : 0) + ",");
                sqlStrBuffer.append((sensitive ? 1 : 0) + ",");
                sqlStrBuffer.append((version == null ? "0" : version) + ",");
                sqlStrBuffer.append(SqlUtils.addSqlStrAndReplace((stops == null ? "" : stops.getId())) + ",");
                sqlStrBuffer.append((isSelect == null ? 0 : isSelect) + ",");
                sqlStrBuffer.append(SqlUtils.addSqlStrAndReplace(crtUser));
                if (i != propertyList.size()) {
                    sqlStrBuffer.append("),");
                } else {
                    sqlStrBuffer.append(")");
                }
            }
            sqlStrBuffer.append(";");
        }
        String sqlStr = sqlStrBuffer.toString();
        return sqlStr;
    }

    /**
     * add FlowTemplateVo
     *
     * @param flow
     * @return
     */
    public String addFlow(FlowTemplateModelVo flow) {
        String sqlStr = "";
        if (null != flow) {
            String id = flow.getId();
            String description = flow.getDescription();
            String name = flow.getName();
            SQL sql = new SQL();
            // INSERT_INTO brackets is table name
            sql.INSERT_INTO("flow_template");
            // The first string in the value is the field name corresponding to the table in the database.
            // all types except numeric fields must be enclosed in single quotes

            sql.VALUES("id", SqlUtils.addSqlStr(id));
            if (StringUtils.isNotBlank(description)) {
                sql.VALUES("description", SqlUtils.addSqlStr(description));
            }
            if (StringUtils.isNotBlank(name)) {
                sql.VALUES("name", SqlUtils.addSqlStr(name));
            }
            sqlStr = sql.toString();
        }
        return sqlStr;
    }

    /**
     * add StopTemplateVo
     *
     * @param stops
     * @return
     */
    public String addStops(StopTemplateModel stops) {
        String sqlStr = "";
        if (null != stops) {
            String id = stops.getId();
            String bundel = stops.getBundel();
            String description = stops.getDescription().equals("null") ? null : stops.getDescription();
            String name = stops.getName();
            String inports = stops.getInports();
            PortType inPortType = stops.getInPortType();
            String outports = stops.getOutports();
            PortType outPortType = stops.getOutPortType();
            String owner = stops.getOwner();
            String pageId = stops.getPageId();
            FlowTemplate flow = stops.getFlowTemplate();
            Boolean enableFlag = stops.getEnableFlag();
            Date crtDttm = stops.getCrtDttm();
            Long version = stops.getVersion();
            Boolean checkpoint = stops.getIsCheckpoint();
            String groups = stops.getGroups();
            String crtUser = stops.getCrtUser();
            SQL sql = new SQL();
            sql.INSERT_INTO("stops_template");

            //Process the required fields first
            if (null == crtDttm) {
                crtDttm = new Date();
            }
            if (null == version) {
                version = 0L;
            }
            if (null == enableFlag) {
                enableFlag = true;
            }
            sql.VALUES("id", SqlUtils.addSqlStr(id));
            String crtDttmStr = DateUtils.dateTimesToStr(crtDttm);
            sql.VALUES("crt_dttm", SqlUtils.addSqlStr(crtDttmStr));
            sql.VALUES("version", (version + 1) + "");
            int enableFlagInt = enableFlag ? 1 : 0;
            sql.VALUES("enable_flag", enableFlagInt + "");

            // handle other fields
            if (StringUtils.isNotBlank(bundel)) {
                sql.VALUES("bundel", SqlUtils.addSqlStr(bundel));
            }
            if (StringUtils.isNotBlank(description)) {
                sql.VALUES("description", SqlUtils.addSqlStr(description));
            }
            if (StringUtils.isNotBlank(name)) {
                sql.VALUES("name", SqlUtils.addSqlStr(name));
            }
            if (StringUtils.isNotBlank(inports)) {
                sql.VALUES("inports", SqlUtils.addSqlStr(inports));
            }
            if (null != inPortType) {
                sql.VALUES("in_port_type", SqlUtils.addSqlStr(inPortType.name()));
            }
            if (StringUtils.isNotBlank(outports)) {
                sql.VALUES("outports", SqlUtils.addSqlStr(outports));
            }
            if (null != outPortType) {
                sql.VALUES("out_port_type", SqlUtils.addSqlStr(outPortType.name()));
            }
            if (StringUtils.isNotBlank(owner)) {
                sql.VALUES("owner", SqlUtils.addSqlStr(owner));
            }
            if (StringUtils.isNotBlank(pageId)) {
                sql.VALUES("page_id", SqlUtils.addSqlStr(pageId));
            }
            if (StringUtils.isNotBlank(crtUser)) {
                sql.VALUES("crt_user", SqlUtils.addSqlStr(crtUser));
            }
            if (null != flow) {
                String flowId = flow.getId();
                if (StringUtils.isNotBlank(flowId)) {
                    sql.VALUES("fk_template_id", SqlUtils.addSqlStr(flowId));
                }
            }
            if (null != checkpoint) {
                sql.VALUES("is_checkpoint", (checkpoint ? 1 : 0) + "");
            }
            if (StringUtils.isNotBlank(groups)) {
                sql.VALUES("groups", SqlUtils.addSqlStr(groups));
            }

            sqlStr = sql.toString();
        }
        return sqlStr;
    }
}
