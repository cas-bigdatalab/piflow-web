package cn.cnic.component.dataProduct.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.dataProduct.entity.DataProduct;
import cn.cnic.component.dataProduct.entity.DataProductType;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class DataProductMapperProvider {

    private Long id;
    private Date lastUpdateDttm;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private String processId;
    private Long propertyId;
    private String propertyName;
    private String datasetUrl;
    private String name;
    private String description;
    private Integer permission;
    private String keyword;
    private String sdPublisher;
    private String email;
    private Integer state;
    private String opinion;
    private String downReason;

    private boolean preventSQLInjectionDataProduct(DataProduct dataProduct) {
        if (null == dataProduct) {
            return false;
        }

        // Mandatory Field
        Boolean enableFlag = dataProduct.getEnableFlag();
        Long version = dataProduct.getVersion();
        this.id = dataProduct.getId();
        this.lastUpdateUser = SqlUtils.preventSQLInjection(dataProduct.getLastUpdateUser());
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        this.lastUpdateDttm = dataProduct.getLastUpdateDttm();

        // Selection field
        this.processId = SqlUtils.preventSQLInjection(dataProduct.getProcessId());
        this.propertyId = dataProduct.getPropertyId();
        this.propertyName = SqlUtils.preventSQLInjection(dataProduct.getPropertyName());
        this.datasetUrl = SqlUtils.preventSQLInjection(dataProduct.getDatasetUrl());
        this.name = SqlUtils.preventSQLInjection(dataProduct.getName());
        this.description = SqlUtils.preventSQLInjection(dataProduct.getDescription());
        this.permission = dataProduct.getPermission();
        this.keyword = SqlUtils.preventSQLInjection(dataProduct.getKeyword());
        this.sdPublisher = SqlUtils.preventSQLInjection(dataProduct.getSdPublisher());
        this.email = SqlUtils.preventSQLInjection(dataProduct.getEmail());
        this.state = dataProduct.getState();
        this.opinion = SqlUtils.preventSQLInjection(dataProduct.getOpinion());
        this.downReason = SqlUtils.preventSQLInjection(dataProduct.getDownReason());
        return true;
    }

    private void reset() {
        this.id = null;
        this.lastUpdateDttm = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.processId = null;
        this.propertyId = null;
        this.propertyName = null;
        this.datasetUrl = null;
        this.name = null;
        this.description = null;
        this.permission = 0;
        this.keyword = null;
        this.sdPublisher = null;
        this.email = null;
        this.state = null;
        this.opinion = null;
        this.downReason = null;
    }

    /**
     * add DataProduct
     *
     * @param dataProduct
     * @return
     */
    public String addDataProduct(DataProduct dataProduct) {
        String sqlStr = "";
        boolean flag = this.preventSQLInjectionDataProduct(dataProduct);
        if (flag) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("INSERT INTO data_product ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldName() + ", ");
            stringBuffer.append("process_id, ");
            stringBuffer.append("property_id, ");
            stringBuffer.append("property_name, ");
            stringBuffer.append("dataset_url, ");
            stringBuffer.append("name, ");
            stringBuffer.append("description, ");
            stringBuffer.append("permission, ");
            stringBuffer.append("keyword, ");
            stringBuffer.append("sdPublisher, ");
            stringBuffer.append("email, ");
            stringBuffer.append("state, ");
            stringBuffer.append("opinion, ");
            stringBuffer.append("downReason ");
            stringBuffer.append(") ");
            stringBuffer.append("VALUES ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldValuesWithLongId(dataProduct)).append(", ");
            // handle other fields
            stringBuffer.append(this.processId).append(", ");
            stringBuffer.append(this.propertyId).append(", ");
            stringBuffer.append(this.propertyName).append(", ");
            stringBuffer.append(this.datasetUrl).append(", ");
            stringBuffer.append(this.name).append(", ");
            stringBuffer.append(this.description).append(", ");
            stringBuffer.append(this.permission).append(", ");
            stringBuffer.append(this.keyword).append(", ");
            stringBuffer.append(this.sdPublisher).append(", ");
            stringBuffer.append(this.email).append(", ");
            stringBuffer.append(this.state).append(", ");
            stringBuffer.append(this.opinion).append(", ");
            stringBuffer.append(this.downReason);
            stringBuffer.append(" ) ");
            sqlStr = stringBuffer.toString();
            this.reset();
        }
        return sqlStr;
    }

    /**
     * update dataProduct
     *
     * @param dataProduct
     * @return
     */
    public String update(DataProduct dataProduct) {

        String sqlStr = "";
        this.preventSQLInjectionDataProduct(dataProduct);
        if (null != dataProduct) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("data_product");
            // The first string in the SET is the name of the field corresponding to the table in the database
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.SET("last_update_user = " + lastUpdateUser);
            if (dataProduct.getVersion() != null) {
                sql.SET("version = " + (version + 1));
            }

            // handle other fields
//            sql.SET("enable_flag = " + enableFlag);
            sql.SET("name = " + name);
            sql.SET("description = " + description);
            sql.SET("permission = " + permission);
            sql.SET("keyword = " + keyword);
            sql.SET("sdPublisher = " + sdPublisher);
            sql.SET("email = " + email);
            sql.SET("state = " + state);
            if (StringUtils.isNotBlank(dataProduct.getOpinion())) {
                sql.SET("opinion = " + opinion);
            }
            if (StringUtils.isNotBlank(dataProduct.getDownReason())) {
                sql.SET("down_reason = " + downReason);
            }
            sql.WHERE("id = " + id);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * update permission
     *
     * @param dataProduct
     * @return
     */
    public String updatePermission(DataProduct dataProduct) {

        String sqlStr = "";
        this.preventSQLInjectionDataProduct(dataProduct);
        if (null != dataProduct) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("data_product");
            // The first string in the SET is the name of the field corresponding to the table in the database
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.SET("last_update_user = " + lastUpdateUser);

            // handle other fields
//            sql.SET("enable_flag = " + enableFlag);
            sql.SET("state = " + state);
            sql.SET("opinion = " + opinion);
            sql.WHERE("id = " + id);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * delete permission
     *
     * @param dataProduct
     * @return
     */
    public String delete(DataProduct dataProduct) {

        String sqlStr = "";
        this.preventSQLInjectionDataProduct(dataProduct);
        if (null != dataProduct) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("data_product");
            // The first string in the SET is the name of the field corresponding to the table in the database
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("enable_flag = " + 0);

            // handle other fields
//            sql.SET("enable_flag = " + enableFlag);
            sql.SET("state = " + state);
            sql.WHERE("id = " + id);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    /**
     * down permission
     *
     * @param dataProduct
     * @return
     */
    public String down(DataProduct dataProduct) {

        String sqlStr = "";
        this.preventSQLInjectionDataProduct(dataProduct);
        if (null != dataProduct) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("data_product");
            // The first string in the SET is the name of the field corresponding to the table in the database
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("enable_flag = " + 0);

            // handle other fields
//            sql.SET("enable_flag = " + enableFlag);
            sql.SET("state = " + state);
            sql.SET("down_reason = " + downReason);
            sql.WHERE("id = " + id);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }


}
