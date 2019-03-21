package com.nature.component.template.model;

import com.nature.base.util.DateUtils;
import com.nature.common.Eunm.PortType;
import com.nature.component.flow.model.Template;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "stops_template")
public class StopTemplateModel implements Serializable {
	/**
	 * stop模板
	 */
	private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_template_ID")
	private Template template;
	
	@Id
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	@GeneratedValue(generator = "idGenerator")
	@Column(length = 40)
	private String id;
	
	@Column(name = "page_id")
	private String pageId;

	private String name;

	private String bundel;

	private String owner;

	private String description;
	
	private String inports;
	
	@Column(nullable = false)
	private Boolean enableFlag = Boolean.TRUE;
   
    @Enumerated(EnumType.STRING)
    private PortType inPortType;

    private String outports;

    @Enumerated(EnumType.STRING)
    private PortType outPortType;
    
    private Boolean isCheckpoint;
    
    private String groups;
    
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, updatable = false)
	private Date crtDttm = new Date();
	
	@Version
	@Column
	private Long version;
	
	private String crtUser;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stopsVo")
    private List<PropertyTemplateModel> properties = new ArrayList<PropertyTemplateModel>();
    
	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBundel() {
		return bundel;
	}

	public void setBundel(String bundel) {
		this.bundel = bundel;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInports() {
		return inports;
	}

	public void setInports(String inports) {
		this.inports = inports;
	}

	public PortType getInPortType() {
		return inPortType;
	}

	public void setInPortType(PortType inPortType) {
		this.inPortType = inPortType;
	}

	public String getOutports() {
		return outports;
	}

	public void setOutports(String outports) {
		this.outports = outports;
	}

	public PortType getOutPortType() {
		return outPortType;
	}

	public void setOutPortType(PortType outPortType) {
		this.outPortType = outPortType;
	}

	public Boolean getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(Boolean enableFlag) {
		this.enableFlag = enableFlag;
	}

	public List<PropertyTemplateModel> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyTemplateModel> properties) {
		this.properties = properties;
	}

	public Boolean getIsCheckpoint() {
		return isCheckpoint;
	}

	public void setIsCheckpoint(Boolean isCheckpoint) {
		this.isCheckpoint = isCheckpoint;
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

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	public String getCrtUser() {
		return crtUser;
	}

	public void setCrtUser(String crtUser) {
		this.crtUser = crtUser;
	}
	
}
