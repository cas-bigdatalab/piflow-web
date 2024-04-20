package cn.cnic.component.dataProduct.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.dataProduct.entity.DataProductType;
import cn.cnic.component.dataProduct.entity.EcosystemTypeAssociate;
import cn.cnic.component.dataProduct.entity.ProductTypeAssociate;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class EcosystemTypeMapperProvider {

    private Long id;
    private Date lastUpdateDttm;
    private String lastUpdateUser;
    //    private int enableFlag;
    private long version;
    private String name;
    private String description;

    private boolean preventSQLInjectionDataProductType(DataProductType dataProductType) {
        if (null == dataProductType) {
            return false;
        }

        // Mandatory Field
        //Boolean enableFlag = dataProductType.getEnableFlag();
        Long version = dataProductType.getVersion();
        this.id = dataProductType.getId();
        this.lastUpdateUser = SqlUtils.preventSQLInjection(dataProductType.getLastUpdateUser());
//        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        this.lastUpdateDttm = dataProductType.getLastUpdateDttm();

        // Selection field
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
            stringBuffer.append("INSERT INTO ecosystem_type ");
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
            sql.SET("name = " + name);
            sql.SET("description = " + description);
            sql.WHERE("id = " + id);
            sqlStr = sql.toString();
        }
        this.reset();
        return sqlStr;
    }

    public String insertAssociate(EcosystemTypeAssociate associate) {
        String sqlStr = "";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("INSERT INTO ecosystem_type_associate ");
        stringBuffer.append("( ");
        stringBuffer.append("id, ");
        stringBuffer.append("ecosystem_type_id, ");
        stringBuffer.append("ecosystem_type_name, ");
        stringBuffer.append("associate_id, ");
        stringBuffer.append("associate_type ");
        stringBuffer.append(") ");
        stringBuffer.append("VALUES ");
        stringBuffer.append("( ");
        // handle other fields
        stringBuffer.append(associate.getId()).append(", ");
        stringBuffer.append(associate.getEcosystemTypeId()).append(", ");
        stringBuffer.append(SqlUtils.preventSQLInjection(associate.getEcosystemTypeName())).append(", ");
        stringBuffer.append(SqlUtils.preventSQLInjection(associate.getAssociateId())).append(", ");
        stringBuffer.append(associate.getAssociateType());
        stringBuffer.append(" ) ");
        sqlStr = stringBuffer.toString();
        return sqlStr;
    }

    public String updateAssociate(EcosystemTypeAssociate associate) {

        String sqlStr = "";
        SQL sql = new SQL();
        // INSERT_INTO brackets is table name
        sql.UPDATE("ecosystem_type_associate");

        // handle other fields
        sql.SET("ecosystem_type_id = " + associate.getEcosystemTypeId());
        sql.SET("ecosystem_type_name = " + SqlUtils.preventSQLInjection(associate.getEcosystemTypeName()));
        sql.SET("associate_id = " + SqlUtils.preventSQLInjection(associate.getAssociateId()));
        sql.SET("associate_type = " + associate.getAssociateType());
        sql.WHERE("id = " + associate.getId());
        sqlStr = sql.toString();
        return sqlStr;
    }

}
