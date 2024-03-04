package cn.cnic.component.flow.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.dataProduct.entity.DataProductType;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.FlowPublishing;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class FlowPublishingMapperProvider {

    private Long id;
    private Date lastUpdateDttm;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String name;
    private String flowId;
    private Long productTypeId;
    private String productTypeName;
    private String productTypeDescription;
    private String description;

    private boolean preventSQLInjectionFlowPublishing(FlowPublishing flowPublishing) {
        if (null == flowPublishing) {
            return false;
        }

        // Mandatory Field
        Boolean enableFlag = flowPublishing.getEnableFlag();
        Long version = flowPublishing.getVersion();
        this.id = flowPublishing.getId();
        this.lastUpdateUser = SqlUtils.preventSQLInjection(flowPublishing.getLastUpdateUser());
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        this.lastUpdateDttm = flowPublishing.getLastUpdateDttm();

        // Selection field
        this.name = SqlUtils.preventSQLInjection(flowPublishing.getName());
        this.flowId = SqlUtils.preventSQLInjection(flowPublishing.getFlowId());
        this.productTypeId = flowPublishing.getProductTypeId();
        this.productTypeName = SqlUtils.preventSQLInjection(flowPublishing.getProductTypeName());
        this.productTypeDescription = SqlUtils.preventSQLInjection(flowPublishing.getProductTypeDescription());
        this.description = SqlUtils.preventSQLInjection(flowPublishing.getDescription());
        return true;
    }

    private void reset() {
        this.id = null;
        this.lastUpdateDttm = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.name = null;
        this.flowId = null;
        this.productTypeId = null;
        this.productTypeName = null;
        this.productTypeDescription = null;
        this.description = null;
    }

    /**
     * add FlowPublishing
     *
     * @param flowPublishing
     * @return
     */
    public String insert(FlowPublishing flowPublishing) {
        String sqlStr = "";
        boolean flag = this.preventSQLInjectionFlowPublishing(flowPublishing);
        if (flag) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("INSERT INTO flow_publishing ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldName() + ", ");
            stringBuffer.append("name, ");
            stringBuffer.append("flow_id, ");
            stringBuffer.append("product_type_id, ");
            stringBuffer.append("product_type_name, ");
            stringBuffer.append("product_type_description, ");
            stringBuffer.append("description ");
            stringBuffer.append(") ");
            stringBuffer.append("VALUES ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldValuesWithLongId(flowPublishing)).append(", ");
            // handle other fields
            stringBuffer.append(this.name).append(", ");
            stringBuffer.append(this.flowId).append(", ");
            stringBuffer.append(this.productTypeId).append(", ");
            stringBuffer.append(this.productTypeName).append(", ");
            stringBuffer.append(this.productTypeDescription).append(", ");
            stringBuffer.append(this.description);
            stringBuffer.append(" ) ");
            sqlStr = stringBuffer.toString();
            this.reset();
        }
        return sqlStr;
    }

    /**
     * update FlowPublishing
     *
     * @param flowPublishing
     * @return
     */
    public String update(FlowPublishing flowPublishing) {

        String sqlStr = "";
        this.preventSQLInjectionFlowPublishing(flowPublishing);
        if (null != flowPublishing) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("flow_publishing");
            // The first string in the SET is the name of the field corresponding to the table in the database
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
//            sql.SET("enable_flag = " + enableFlag);
            sql.SET("name = " + name);
            sql.SET("flow_id = " + flowId);
            sql.SET("product_type_id = " + productTypeId);
            sql.SET("product_type_name = " + productTypeName);
            sql.SET("product_type_description = " + productTypeDescription);
            sql.SET("description = " + description);
            sql.WHERE("id = " + id);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * getFullInfoById
     *
     * @return
     */
    public String getFullInfoById(String id) {
        String sqlStr = "";
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("flow_publishing");
        sql.WHERE("id = "+id);
        sql.WHERE("enable_flag = 1");
        sql.ORDER_BY(" last_update_dttm desc  ");
        sqlStr = sql.toString();
        return sqlStr;
    }

}
