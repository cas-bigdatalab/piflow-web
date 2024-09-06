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
    private Integer isShare;
    private String doiId;
    private String cstrId;
    private String subjectTypeId;
    private String timeRange;
    private String spacialRange;
    private String datasetSize;
    private Integer datasetType;
    private String associateId;
    private String company;

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
        this.isShare = (null == dataProduct.getIsShare()) ? 0 : dataProduct.getIsShare();
        this.doiId = SqlUtils.preventSQLInjection(dataProduct.getDoiId());
        this.cstrId = SqlUtils.preventSQLInjection(dataProduct.getCstrId());
        this.subjectTypeId = SqlUtils.preventSQLInjection(dataProduct.getSubjectTypeId());
        this.timeRange = SqlUtils.preventSQLInjection(dataProduct.getTimeRange());
        this.spacialRange = SqlUtils.preventSQLInjection(dataProduct.getSpacialRange());
        this.datasetSize = SqlUtils.preventSQLInjection(dataProduct.getDatasetSize());
        this.datasetType = (null == dataProduct.getDatasetType()) ? 0 : dataProduct.getDatasetType();
        this.associateId = SqlUtils.preventSQLInjection(dataProduct.getAssociateId());
        this.company = SqlUtils.preventSQLInjection(dataProduct.getCompany());
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
        this.isShare = null;
        this.doiId = null;
        this.cstrId = null;
        this.subjectTypeId = null;
        this.timeRange = null;
        this.spacialRange = null;
        this.datasetSize = null;
        this.datasetType = null;
        this.associateId = null;
    }

    /**
     * add DataProduct
     *
     * @param dataProduct
     * @return
     */
    public String insert(DataProduct dataProduct) {
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
            stringBuffer.append("down_reason, ");
            stringBuffer.append("is_share, ");
            stringBuffer.append("company, ");
            if (!"null".equals(this.doiId)) {
                stringBuffer.append("doi_id, ");
            }
            if (!"null".equals(this.cstrId)) {
                stringBuffer.append("cstr_id, ");
            }
            if (!"null".equals(this.subjectTypeId)) {
                stringBuffer.append("subject_type_id, ");
            }
            if (!"null".equals(this.timeRange)) {
                stringBuffer.append("time_range, ");
            }
            if (!"null".equals(this.spacialRange)) {
                stringBuffer.append("spacial_range, ");
            }
            if (!"null".equals(this.datasetSize)) {
                stringBuffer.append("dataset_size, ");
            }
            stringBuffer.append("dataset_type, ");
            stringBuffer.append("associate_id ");
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
            stringBuffer.append(this.downReason).append(", ");
            stringBuffer.append(this.isShare).append(", ");
            stringBuffer.append(this.company).append(", ");
            if (!"null".equals(this.doiId)) {
                stringBuffer.append(this.doiId).append(", ");
            }
            if (!"null".equals(this.cstrId)) {
                stringBuffer.append(this.cstrId).append(", ");
            }
            if (!"null".equals(this.subjectTypeId)) {
                stringBuffer.append(this.subjectTypeId).append(", ");
            }
            if (!"null".equals(this.timeRange)) {
                stringBuffer.append(this.timeRange).append(", ");
            }
            if (!"null".equals(this.spacialRange)) {
                stringBuffer.append(this.spacialRange).append(", ");
            }
            if (!"null".equals(this.datasetSize)) {
                stringBuffer.append(this.datasetSize).append(", ");
            }
            stringBuffer.append(this.datasetType).append(", ");
            stringBuffer.append(this.associateId);
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
            if (null != dataProduct.getIsShare()) {
                sql.SET("is_share = " + isShare);
            }
            if (StringUtils.isNotBlank(dataProduct.getDoiId())) {
                sql.SET("doi_id = " + doiId);
            }
            if (StringUtils.isNotBlank(dataProduct.getCstrId())) {
                sql.SET("cstr_id = " + cstrId);
            }
            if (StringUtils.isNotBlank(dataProduct.getSubjectTypeId())) {
                sql.SET("subject_type_id = " + subjectTypeId);
            }
            if (StringUtils.isNotBlank(dataProduct.getTimeRange())) {
                sql.SET("time_range = " + timeRange);
            }
            if (StringUtils.isNotBlank(dataProduct.getSpacialRange())) {
                sql.SET("spacial_range = " + spacialRange);
            }
            if (StringUtils.isNotBlank(dataProduct.getDatasetSize())) {
                sql.SET("dataset_size = " + datasetSize);
            }
            if (null != dataProduct.getDatasetType()) {
                sql.SET("dataset_type = " + datasetType);
            }
            if (null != dataProduct.getAssociateId()) {
                sql.SET("associate_id = " + associateId);
            }
            if (null != dataProduct.getCompany()) {
                sql.SET("company = " + company);
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
            sql.SET("enable_flag = " + this.enableFlag);

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
