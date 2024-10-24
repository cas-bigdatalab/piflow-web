package cn.cnic.component.stopsComponent.mapper.provider;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.base.utils.SqlUtils;
import cn.cnic.component.stopsComponent.entity.BaseImageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;

public class BaseImageInfoMapperProvider {

    private String baseImageName;
    private String baseImageVersion;
    private String baseImageDescription;
    private String harborUser;
    private String harborPassword;
    private String crtDttm;
    private String crtUser;

    private boolean preventSQLInjectionStopsHubInfo(BaseImageInfo baseImageInfo) {
        if (null == baseImageInfo) {
            return false;
        }

        this.baseImageName = SqlUtils.preventSQLInjection(baseImageInfo.getBaseImageName());
        this.baseImageVersion = SqlUtils.preventSQLInjection(baseImageInfo.getBaseImageVersion());
        this.baseImageDescription = SqlUtils.preventSQLInjection(baseImageInfo.getBaseImageDescription());
        this.harborUser = SqlUtils.preventSQLInjection(baseImageInfo.getHarborUser());
        this.harborPassword = SqlUtils.preventSQLInjection(baseImageInfo.getHarborPassword());
        this.crtDttm = SqlUtils.preventSQLInjection(DateUtils.dateTimesToStr(new Date()));
        this.crtUser = SqlUtils.preventSQLInjection(baseImageInfo.getCrtUser());

        return true;
    }

    private boolean preventSQLInjectionStopsHubInfo(String name) {
        if (null == name) {
            return false;
        }

        this.baseImageName = SqlUtils.preventSQLInjection(name);
        return true;
    }

    private void reset() {
        this.baseImageName = null;
        this.baseImageVersion = null;
        this.baseImageDescription = null;
        this.harborUser = null;
        this.harborPassword = null;
        this.crtDttm = null;
        this.crtUser = null;
    }

    public String addBaseImageInfo(BaseImageInfo baseImageInfo) {
        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionStopsHubInfo(baseImageInfo);
        if (flag) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("INSERT INTO base_image_info ");
            strBuf.append("( ");
            strBuf.append("base_image_name, ");
            strBuf.append("base_image_version, ");
            strBuf.append("base_image_description, ");
            strBuf.append("harbor_user, ");
            strBuf.append("harbor_password, ");
            strBuf.append("crt_dttm, ");
            strBuf.append("crt_user ");
            strBuf.append(") VALUES ( ");
            strBuf.append(this.baseImageName + ", ");
            strBuf.append(this.baseImageVersion + ", ");
            strBuf.append(this.baseImageDescription + ", ");
            strBuf.append(this.harborUser + ", ");
            strBuf.append(this.harborPassword + ", ");
            strBuf.append(this.crtDttm + ", ");
            strBuf.append(this.crtUser);
            strBuf.append(") ");
            sqlStr = strBuf.toString();
        }
        this.reset();
        return sqlStr;
    }

    public String updateBaseImageInfo(BaseImageInfo baseImageInfo) {
        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionStopsHubInfo(baseImageInfo);
        if (flag) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("update base_image_info set base_image_version = ");
            strBuf.append(this.baseImageVersion);
            strBuf.append(", base_image_description = ");
            strBuf.append(this.baseImageDescription);
            strBuf.append(", harbor_user = ");
            strBuf.append(this.harborUser);
            strBuf.append(", harbor_password = ");
            strBuf.append(this.harborPassword);
            strBuf.append(", crt_user = ");
            strBuf.append(this.crtUser);
            strBuf.append(", crt_dttm = ");
            strBuf.append(this.crtDttm);
            strBuf.append(" where base_image_name = ");
            strBuf.append(this.baseImageName);
            sqlStr = strBuf.toString();
        }
        this.reset();
        return sqlStr;
    }

    public String getBaseImageInfoList() {
        String sqlStr;
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("select * ");
        strBuf.append("from base_image_info ");
        sqlStr = strBuf.toString();
        return sqlStr;
    }

    public String getBaseImageInfoListByName(String name) {
        String sqlStr = "SELECT 0";
        boolean flag = this.preventSQLInjectionStopsHubInfo(name);
        if (flag) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("select * ");
            strBuf.append("from base_image_info where base_image_name = ");
            strBuf.append(this.baseImageName);
            sqlStr = strBuf.toString();
        }
        this.reset();
        return sqlStr;
    }
}

