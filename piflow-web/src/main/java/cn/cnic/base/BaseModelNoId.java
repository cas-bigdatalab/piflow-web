package cn.cnic.base;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.cnic.base.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BaseModelNoId implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_LEN_SHORT = 60;
    public static final int DEFAULT_LEN_300 = 300;
    public static final int DEFAULT_LEN_LONG = 4000;

    private Date crtDttm = new Date();
    private String crtUser;
    private Date lastUpdateDttm = new Date();
    private String lastUpdateUser;
    private Boolean enableFlag = Boolean.TRUE;
    private Long version;

    private String crtDttmStr;
    private String lastUpdateDttmStr;
    private Integer enableFlagNum;

    /**
     * @return yyyy-MM-dd HH:mm:ss ,such as 2012-12-25 20:20:20
     */
    public String getCrtDttmString() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
        return crtDttm != null ? sdf.format(crtDttm) : "";
    }

    /**
     * @return yyyy-MM-dd HH:mm:ss ,such as 2012-12-25 20:20:20
     */
    public String getLastUpdateDttmString() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
        return lastUpdateDttm != null ? sdf.format(lastUpdateDttm) : "";
    }

}