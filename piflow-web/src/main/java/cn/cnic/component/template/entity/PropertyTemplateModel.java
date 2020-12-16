
package cn.cnic.component.template.entity;

import cn.cnic.base.util.DateUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Stop attribute
 */
@Entity
@Table(name = "property_template")
public class PropertyTemplateModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_STOPS_ID")
    private StopTemplateModel stopsVo;

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(length = 40)
    private String id;

    private String name;

    private String displayName;

    @Column(name = "description", columnDefinition = "varchar(1024) COMMENT 'description'")
    private String description;

    @Column(name = "CUSTOM_VALUE")
    private String customValue;

    @Column(name = "ALLOWABLE_VALUES")
    private String allowableValues;

    @Column(name = "PROPERTY_REQUIRED")
    private Boolean required;

    @Column(name = "PROPERTY_SENSITIVE")
    private Boolean sensitive;

    @Column(nullable = false)
    private Boolean enableFlag = Boolean.TRUE;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date crtDttm = new Date();

    @Version
    @Column
    private Long version;

    private Boolean isSelect;

    private String crtUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StopTemplateModel getStopsVo() {
        return stopsVo;
    }

    public void setStopsVo(StopTemplateModel stopsVo) {
        this.stopsVo = stopsVo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomValue() {
        return customValue;
    }

    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }

    public String getAllowableValues() {
        return allowableValues;
    }

    public void setAllowableValues(String allowableValues) {
        this.allowableValues = allowableValues;
    }

    public Boolean getRequired() {
        return required;
    }

    public Boolean getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Boolean enableFlag) {
        this.enableFlag = enableFlag;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getSensitive() {
        return sensitive;
    }

    public void setSensitive(Boolean sensitive) {
        this.sensitive = sensitive;
    }

    public Date getCrtDttm() {
        return crtDttm;
    }

    public void setCrtDttm(Date crtDttm) {
        this.crtDttm = crtDttm;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getCrtDttmString() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
        return crtDttm != null ? sdf.format(crtDttm) : "";
    }

    public Boolean getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Boolean isSelect) {
        this.isSelect = isSelect;
    }

    public String getCrtUser() {
        return crtUser;
    }

    public void setCrtUser(String crtUser) {
        this.crtUser = crtUser;
    }

}
