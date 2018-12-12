package com.nature.component.workFlow.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import com.nature.component.template.model.StopTemplateModel;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name =  "flow_template")
public class Template extends BaseHibernateModelUUIDNoCorpAgentId {

	private static final long serialVersionUID = 1L;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FK_FLOW_ID")
	private Flow flow; 

	private String name;
	
	@Column(name = "description",columnDefinition="varchar(1000) COMMENT '描述'")
	private String description;
	
	@Lob @Basic(fetch = FetchType.LAZY) @Type(type="text") @Column(name="value", nullable=true)
	private String value;
	
	private String path;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "template")
	private List<StopTemplateModel> stopsList = new ArrayList<StopTemplateModel>();

 	public Flow getFlow() {
		return flow;
	}

	public void setFlow(Flow flow) {
		this.flow = flow;
	} 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<StopTemplateModel> getStopsList() {
		return stopsList;
	}

	public void setStopsList(List<StopTemplateModel> stopsList) {
		this.stopsList = stopsList;
	}
}
