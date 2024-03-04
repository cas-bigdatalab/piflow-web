package cn.cnic.component.dataProduct.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.dataProduct.entity.DataProductType;
import cn.cnic.component.dataProduct.entity.ProductTypeAssociate;
import cn.cnic.component.flow.entity.Flow;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class DataProductTypeMapperProvider {

    private Long id;
    private Date lastUpdateDttm;
    private String lastUpdateUser;
    //    private int enableFlag;
    private long version;
    private Long parentId;
    private Integer level;//级别
    private String name;
    private String description;

    private boolean preventSQLInjectionDataProductType(DataProductType dataProductType) {
        if (null == dataProductType) {
            return false;
        }

        // Mandatory Field
        Boolean enableFlag = dataProductType.getEnableFlag();
        Long version = dataProductType.getVersion();
        this.id = dataProductType.getId();
        this.lastUpdateUser = SqlUtils.preventSQLInjection(dataProductType.getLastUpdateUser());
//        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        this.lastUpdateDttm = dataProductType.getLastUpdateDttm();

        // Selection field
        this.parentId = dataProductType.getParentId();
        this.level = dataProductType.getLevel();
        this.name = SqlUtils.preventSQLInjection(dataProductType.getName());
        this.description = SqlUtils.preventSQLInjection(dataProductType.getDescription());
        return true;
    }

    private void reset() {
        this.id = null;
        this.lastUpdateDttm = null;
        this.lastUpdateUser = null;
//        this.enableFlag = 1;
        this.version = 0L;
        this.parentId = null;
        this.level = null;
        this.name = null;
        this.description = null;
    }

    /**
     * add DataProductType
     *
     * @param dataProductType
     * @return
     */
    public String insert(DataProductType dataProductType) {
        String sqlStr = "";
        boolean flag = this.preventSQLInjectionDataProductType(dataProductType);
        if (flag) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("INSERT INTO data_product_type ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldNameWithNoId() + ", ");
            stringBuffer.append("parent_id, ");
            stringBuffer.append("level, ");
            stringBuffer.append("name, ");
            stringBuffer.append("description ");
            stringBuffer.append(") ");
            stringBuffer.append("VALUES ");
            stringBuffer.append("( ");
            stringBuffer.append(SqlUtils.baseFieldValuesNoLongId(dataProductType) + ", ");
            // handle other fields
            stringBuffer.append(this.parentId + ", ");
            stringBuffer.append(this.level + ", ");
            stringBuffer.append(this.name + ", ");
            stringBuffer.append(this.description + " ");
            stringBuffer.append(") ");
            sqlStr = stringBuffer.toString();
            this.reset();
        }
        return sqlStr;
    }

    /**
     * update dataProductType
     *
     * @param dataProductType
     * @return
     */
    public String update(DataProductType dataProductType) {

        String sqlStr = "";
        this.preventSQLInjectionDataProductType(dataProductType);
        if (null != dataProductType) {
            SQL sql = new SQL();

            // INSERT_INTO brackets is table name
            sql.UPDATE("data_product_type");
            // The first string in the SET is the name of the field corresponding to the table in the database
            sql.SET("last_update_dttm = " + SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(lastUpdateDttm)));
            sql.SET("last_update_user = " + lastUpdateUser);
            sql.SET("version = " + (version + 1));

            // handle other fields
//            sql.SET("enable_flag = " + enableFlag);
            sql.SET("parent_id = " + parentId);
            sql.SET("level = " + level);
            sql.SET("name = " + name);
            sql.SET("description = " + description);
            sql.WHERE("id = " + id);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

//    /**
//     * get all
//     *
//     * @return
//     */
//    public String getAll(String userName) {
//        String sqlStr = "";
//        SQL sql = new SQL();
//        if(StringUtils.isNotBlank(userName)){
//            sql.SELECT("f.*, ");
//        }
//        sql.SELECT("*");
//        this.preventSQLInjectionDataProductType(dataProductType);
//        if (null != dataProductType) {
//            SQL sql = new SQL();
//            sql.SELECT("*");
//            sql.FROM("flow as f ");
//            sql.LEFT_OUTER_JOIN(product_type_associate )
//            sql.WHERE("enable_flag = 1");
//            sql.WHERE("is_example = 0");
//            sql.WHERE("fk_flow_group_id = null ");
//            sql.ORDER_BY(" crt_dttm desc  ");
//            sqlStr = sql.toString();
//        }
//        this.reset();
//        return sqlStr;
//    }

    public String insertAssociate(ProductTypeAssociate associate) {
        String sqlStr = "";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("INSERT INTO product_type_associate ");
        stringBuffer.append("( ");
        stringBuffer.append("id, ");
        stringBuffer.append("product_type_id, ");
        stringBuffer.append("product_type_name, ");
        stringBuffer.append("associate_id, ");
        stringBuffer.append("associate_type, ");
        stringBuffer.append("state ");
        stringBuffer.append(") ");
        stringBuffer.append("VALUES ");
        stringBuffer.append("( ");
        // handle other fields
        stringBuffer.append(associate.getId()).append(", ");
        stringBuffer.append(associate.getProductTypeId()).append(", ");
        stringBuffer.append(SqlUtils.preventSQLInjection(associate.getProductTypeName())).append(", ");
        stringBuffer.append(SqlUtils.preventSQLInjection(associate.getAssociateId())).append(", ");
        stringBuffer.append(associate.getAssociateType()).append(", ");
        stringBuffer.append(associate.getState());
        stringBuffer.append(" ) ");
        sqlStr = stringBuffer.toString();
        return sqlStr;
    }

    public String updateAssociate(ProductTypeAssociate associate) {

        String sqlStr = "";
        SQL sql = new SQL();
        // INSERT_INTO brackets is table name
        sql.UPDATE("product_type_associate");

        // handle other fields
//            sql.SET("enable_flag = " + enableFlag);
        sql.SET("product_type_id = " + associate.getProductTypeId());
        sql.SET("product_type_name = " + SqlUtils.preventSQLInjection(associate.getProductTypeName()));
        sql.SET("associate_id = " + SqlUtils.preventSQLInjection(associate.getAssociateId()));
        sql.SET("associate_type = " + associate.getAssociateType());
        sql.SET("state = " + associate.getState());
        sql.WHERE("id = " + associate.getId());
        sqlStr = sql.toString();
        return sqlStr;
    }

}
