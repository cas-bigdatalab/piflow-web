package cn.cnic.component.system.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.system.entity.SysUser;
import cn.cnic.component.system.entity.UserOpinion;
import cn.cnic.component.system.vo.UserOpiniontVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;


public class UserOpinionMapperProvider {

    private String id;
    private String lastUpdateUser;
    private String lastUpdateDttmStr;
    private boolean enableFlag;
    private String name;
    private String description;
    private Integer state; //0-待解决 1-已解决
    private String bak; //备注


    private String company; // 创建人台站
    private String organization; // 创建人单位

    private boolean preventSQLInjectionSysUser(UserOpinion userOpinion) {
        // Mandatory Field
        this.id = SqlUtils.preventSQLInjection(userOpinion.getId());

        // Selection field
        this.enableFlag = (null != userOpinion.getEnableFlag() && userOpinion.getEnableFlag());
        this.lastUpdateUser = SqlUtils.preventSQLInjection(userOpinion.getLastUpdateUser());
        String lastUpdateDttm = DateUtils.dateTimesToStr(null != userOpinion.getLastUpdateDttm() ? userOpinion.getLastUpdateDttm() : new Date());
        this.lastUpdateDttmStr = SqlUtils.preventSQLInjection(lastUpdateDttm);
        this.name = SqlUtils.preventSQLInjection(userOpinion.getName());
        this.description = SqlUtils.preventSQLInjection(userOpinion.getDescription());
        this.state = (null != userOpinion.getState() ? userOpinion.getState() : 0);
        this.bak = SqlUtils.preventSQLInjection(userOpinion.getBak());
        this.company = SqlUtils.preventSQLInjection(userOpinion.getCompany());
        this.organization = SqlUtils.preventSQLInjection(userOpinion.getOrganization());

        return true;
    }

    private void resetUserOpinion() {
        this.id = null;
        this.lastUpdateDttmStr = null;
        this.enableFlag = true;
        this.name = null;
        this.description = null;
        this.state = 0;
        this.bak = null;
        this.company = null;
        this.organization = null;
    }

    public String insert(UserOpinion userOpinion) {
        boolean flag = this.preventSQLInjectionSysUser(userOpinion);
        if (!flag) {
            return "SELECT 0";
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("INSERT INTO user_opinion ");

        strBuf.append("( ");
        strBuf.append(SqlUtils.baseFieldNameNoVersion() + ", ");
        strBuf.append("name, description, state, bak, company, organization ");
        strBuf.append(") ");

        strBuf.append("values ");
        strBuf.append("(");
        strBuf.append(SqlUtils.baseFieldValuesNoVersion(userOpinion) + ", ");
        strBuf.append(name + "," + description + "," + state + "," + bak
                + "," + company + "," + organization);
        strBuf.append(")");

        this.resetUserOpinion();
        return strBuf.toString() + ";";
    }

    public String update(UserOpinion userOpinion) {
        boolean flag = this.preventSQLInjectionSysUser(userOpinion);
        if (!flag) {
            return "SELECT 0";
        }
        SQL sql = new SQL();
        // INSERT_INTO brackets is table name
        sql.UPDATE("user_opinion");
        // The first string in the SET is the name of the field corresponding to the table in the database
        sql.SET("last_update_dttm = " + lastUpdateDttmStr);
        sql.SET("last_update_user = " + lastUpdateUser);

        // handle other fields
        sql.SET("enable_flag = " + (enableFlag ? 1 : 0));
        sql.SET("name = " + this.name);
        sql.SET("description = " + this.description);
        sql.SET("state = " + this.state);
        sql.SET("bak = " + this.bak);
        sql.SET("company = " + this.company);
        sql.SET("organization = " + this.organization);

        sql.WHERE("id = " + this.id);

        this.resetUserOpinion();
        return sql.toString();
    }

    /**
     * getSysUserVoList
     *
     * @return
     */
    public String getSysUserVoList(String username, String param, String name, String email, String company) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("SELECT id, username, name, age, sex, crt_dttm, status, last_login_ip, phone_number, email, company, organization " +
                "FROM sys_user WHERE enable_flag=1 ");
        if (StringUtils.isNotBlank(param)) {
            strBuf.append("AND ( ");
            strBuf.append(" username LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(param) + ",'%')");
            strBuf.append(") ");
        }
        if (StringUtils.isNotBlank(name)) {
            strBuf.append("AND ( ");
            strBuf.append(" name LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(name) + ",'%')");
            strBuf.append(") ");
        }
        if (StringUtils.isNotBlank(email)) {
            strBuf.append("AND ( ");
            strBuf.append(" email LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(email) + ",'%')");
            strBuf.append(") ");
        }
        if (StringUtils.isNotBlank(company)) {
            strBuf.append("AND ( ");
            strBuf.append("company LIKE CONCAT('%'," + SqlUtils.preventSQLInjection(company) + ",'%')");

            strBuf.append(") ");
        }
        String sqlStr = strBuf.toString();
        return sqlStr;
    }

    public String updateState(UserOpiniontVo userOpiniontVo) {
        String ids = userOpiniontVo.getId(); // 假设 getId() 返回逗号分隔的 ID 字符串
        StringBuilder idList = new StringBuilder();
        for (String id : ids.split(",")) {
            if (idList.length() > 0) {
                idList.append(",");
            }
            idList.append("'");
            idList.append(id.trim());
            idList.append("'");
        }
        return "update user_opinion set state = " + userOpiniontVo.getState()
                + ", bak = " + SqlUtils.preventSQLInjection(userOpiniontVo.getBak()) + "where id in (" + idList.toString() + ")";
    }

}
