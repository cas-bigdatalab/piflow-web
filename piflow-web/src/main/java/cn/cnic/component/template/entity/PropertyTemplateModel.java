
package cn.cnic.component.template.entity;

import cn.cnic.base.utils.DateUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Stop attribute
 */
@Getter
@Setter
public class PropertyTemplateModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private StopTemplateModel stopsVo;
    private String id;
    private String name;
    private String displayName;
    private String description;
    private String customValue;
    private String allowableValues;
    private Boolean required;
    private Boolean sensitive;
    private Boolean enableFlag = Boolean.TRUE;
    private Date crtDttm = new Date();
    private Long version;
    private Boolean isSelect;
    private String crtUser;

    public String getCrtDttmString() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
        return crtDttm != null ? sdf.format(crtDttm) : "";
    }

}
