package cn.cnic.component.dataProduct.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.dataProduct.entity.DataProduct;
import cn.cnic.component.dataProduct.entity.ProductUser;

import java.util.Date;

public class ProductUserMapperProvider {

    private Long id;
    private Date lastUpdateDttm;
    private String lastUpdateUser;
    private int enableFlag;
    private long version;
    private Long productId;
    private String productName;
    private String userId;
    private String userName;
    private String userEmail;
    private String reason;
    private String opinion;
    private Integer state;
    private String bak1;
    private String bak2;
    private String bak3;

    private boolean preventSQLInjectionProductUser(ProductUser productUser) {
        if (null == productUser) {
            return false;
        }

        // Mandatory Field
        Boolean enableFlag = productUser.getEnableFlag();
        Long version = productUser.getVersion();
        this.id = productUser.getId();
        this.lastUpdateUser = SqlUtils.preventSQLInjection(productUser.getLastUpdateUser());
        this.enableFlag = ((null != enableFlag && enableFlag) ? 1 : 0);
        this.version = (null != version ? version : 0L);
        this.lastUpdateDttm = productUser.getLastUpdateDttm();

        // Selection field
        this.productId = productUser.getProductId();
        this.productName = SqlUtils.preventSQLInjection(productUser.getProductName());
        this.userId = SqlUtils.preventSQLInjection(productUser.getUserId());
        this.userName = SqlUtils.preventSQLInjection(productUser.getUserName());
        this.userEmail = SqlUtils.preventSQLInjection(productUser.getUserEmail());
        this.reason = SqlUtils.preventSQLInjection(productUser.getReason());
        this.opinion = SqlUtils.preventSQLInjection(productUser.getOpinion());
        this.state = productUser.getState();
        this.bak1 = SqlUtils.preventSQLInjection(productUser.getBak1());
        this.bak2 = SqlUtils.preventSQLInjection(productUser.getBak2());
        this.bak3 = SqlUtils.preventSQLInjection(productUser.getBak3());
        return true;
    }

    private void reset() {
        this.id = null;
        this.lastUpdateDttm = null;
        this.lastUpdateUser = null;
        this.enableFlag = 1;
        this.version = 0L;
        this.productId = null;
        this.productName = null;
        this.userId = null;
        this.userName =null;
        this.userEmail = null;
        this.reason = null;
        this.opinion = null;
        this.state = 1;
    }

    /**
     * add ProductUser
     *
     * @param productUser
     * @return
     */
    public String insert(ProductUser productUser) {
        String sqlStr = "";
        boolean flag = this.preventSQLInjectionProductUser(productUser);
        if (flag) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("INSERT INTO product_user ");
            stringBuffer.append("( ");
            stringBuffer.append("id, ");
            stringBuffer.append("product_id, ");
            stringBuffer.append("product_name, ");
            stringBuffer.append("user_id, ");
            stringBuffer.append("user_name, ");
            stringBuffer.append("user_email, ");
            stringBuffer.append("reason, ");
            stringBuffer.append("opinion, ");
            stringBuffer.append("state, ");
            stringBuffer.append("last_update_dttm, ");
            stringBuffer.append("last_update_user, ");
            stringBuffer.append("bak1, ");
            stringBuffer.append("bak2, ");
            stringBuffer.append("bak3 ");
            stringBuffer.append(") ");
            stringBuffer.append("VALUES ");
            stringBuffer.append("( ");
            stringBuffer.append(this.id).append(", ");
            // handle other fields
            stringBuffer.append(this.productId).append(", ");
            stringBuffer.append(this.productName).append(", ");
            stringBuffer.append(this.userId).append(", ");
            stringBuffer.append(this.userName).append(", ");
            stringBuffer.append(this.userEmail).append(", ");
            stringBuffer.append(this.reason).append(", ");
            stringBuffer.append(this.opinion).append(", ");
            stringBuffer.append(this.state).append(", ");
            stringBuffer.append(SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(this.lastUpdateDttm))).append(", ");
            stringBuffer.append(this.lastUpdateUser).append(", ");
            stringBuffer.append(this.bak1).append(", ");
            stringBuffer.append(this.bak2).append(", ");
            stringBuffer.append(this.bak3);
            stringBuffer.append(" ) ");
            sqlStr = stringBuffer.toString();
            this.reset();
        }
        return sqlStr;
    }

//    /**
//     * update dataProductType
//     *
//     * @param flow
//     * @return
//     */
//    public String updateFlow(Flow flow) {
//
//        String sqlStr = "";
//        this.preventSQLInjectionFlow(flow);
//        if (null != flow) {
//            SQL sql = new SQL();
//
//            // INSERT_INTO brackets is table name
//            sql.UPDATE("flow");
//            // The first string in the SET is the name of the field corresponding to the table in the database
//            sql.SET("last_update_dttm = " + lastUpdateDttmStr);
//            sql.SET("last_update_user = " + lastUpdateUser);
//            sql.SET("version = " + (version + 1));
//
//            // handle other fields
//            sql.SET("enable_flag = " + enableFlag);
//            sql.SET("description = " + description);
//            sql.SET("name = " + name);
//            sql.SET("uuid = " + uuid);
//            sql.SET("driver_memory = " + driverMemory);
//            sql.SET("executor_cores = " + executorCores);
//            sql.SET("executor_memory = " + executorMemory);
//            sql.SET("executor_number = " + executorNumber);
//            sql.WHERE("version = " + version);
//            sql.WHERE("id = " + id);
//            sqlStr = sql.toString();
//            if (StringUtils.isBlank(id)) {
//                sqlStr = "";
//            }
//        }
//        this.reset();
//        return sqlStr;
//    }
//
//    /**
//     * get flow list
//     *
//     * @return
//     */
//    public String getFlowList() {
//        String sqlStr = "";
//        SQL sql = new SQL();
//        sql.SELECT("*");
//        sql.FROM("flow");
//        sql.WHERE("enable_flag = 1");
//        sql.WHERE("is_example = 0");
//        sql.WHERE("fk_flow_group_id = null ");
//        sql.ORDER_BY(" crt_dttm desc  ");
//        sqlStr = sql.toString();
//        return sqlStr;
//    }

}
