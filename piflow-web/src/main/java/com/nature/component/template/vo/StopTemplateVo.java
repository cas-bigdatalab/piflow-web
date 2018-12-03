package com.nature.component.template.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.nature.component.workFlow.model.Template;
import com.nature.common.Eunm.PortType;
import com.nature.component.workFlow.vo.PropertyVo;

@Entity
@Table(name = "stops_template")
public class StopTemplateVo implements Serializable {
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stopsVo")
    private List<PropertyVo> properties = new ArrayList<PropertyVo>();
    
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

	public List<PropertyVo> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyVo> properties) {
		this.properties = properties;
	}
}
