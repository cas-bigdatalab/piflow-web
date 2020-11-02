package cn.cnic.base;

import cn.cnic.base.util.DateUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@MappedSuperclass
public class BaseHibernateModelNoId implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_LEN_SHORT = 60;
    public static final int DEFAULT_LEN_300 = 300;
    public static final int DEFAULT_LEN_LONG = 4000;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date crtDttm = new Date();

    @Column(nullable = false, updatable = false)
    private String crtUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdateDttm = new Date();

    @Column(nullable = false)
    private String lastUpdateUser;

    @Column(nullable = false)
    private Boolean enableFlag = Boolean.TRUE;

    @Version
    @Column
    private Long version;

    public Boolean getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Boolean enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Date getCrtDttm() {
        return crtDttm;
    }

    /**
     * @return yyyy-MM-dd HH:mm:ss ,such as 2012-12-25 20:20:20
     */
    public String getCrtDttmString() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
        return crtDttm != null ? sdf.format(crtDttm) : "";
    }

    public void setCrtDttm(Date crtDttm) {
        this.crtDttm = crtDttm;
    }

    public String getCrtUser() {
        return crtUser;
    }

    public void setCrtUser(String crtUserId) {
        this.crtUser = crtUserId;
    }

    public Date getLastUpdateDttm() {
        return lastUpdateDttm;
    }

    /**
     * @return yyyy-MM-dd HH:mm:ss ,such as 2012-12-25 20:20:20
     */
    public String getLastUpdateDttmString() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
        return lastUpdateDttm != null ? sdf.format(lastUpdateDttm) : "";
    }

    public void setLastUpdateDttm(Date lastUpdateDttm) {
        this.lastUpdateDttm = lastUpdateDttm;
    }

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUserId) {
        this.lastUpdateUser = lastUpdateUserId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}