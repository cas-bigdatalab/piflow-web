package com.nature.component.flow.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import com.nature.component.mxGraph.model.MxGraphModel;

@Entity
@Table(name = "FLOW")
@Setter
@Getter
public class Flow extends BaseHibernateModelUUIDNoCorpAgentId {

	private static final long serialVersionUID = 1L;

	@Column(columnDefinition="varchar(255) COMMENT 'flow name'")
	private String name;

	@Column(columnDefinition="varchar(255) COMMENT 'flow uuid'")
	private String uuid;

	@Column(columnDefinition="varchar(255) COMMENT 'driverMemory'")
	private String driverMemory;

	@Column(columnDefinition="varchar(255) COMMENT 'executorNumber'")
	private String executorNumber;

	@Column(columnDefinition="varchar(255) COMMENT 'executorMemory'")
	private String executorMemory;

	@Column(columnDefinition="varchar(255) COMMENT 'executorCores'")
	private String executorCores;
	
	@Column(name = "description",columnDefinition="text(0) COMMENT 'description'")
	private String description;

	@Column(columnDefinition="bit(1) COMMENT 'isExample'")
	private Boolean isExample = false;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "flow")
	@Where(clause = "enable_flag=1")
	private FlowInfoDb appId;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "flow")
	@Where(clause = "enable_flag=1")
	private MxGraphModel mxGraphModel;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "flow")
	@Where(clause = "enable_flag=1")
	@OrderBy(clause = "lastUpdateDttm desc")
	private List<Stops> stopsList = new ArrayList<Stops>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "flow")
	@Where(clause = "enable_flag=1")
	@OrderBy(clause = "lastUpdateDttm desc")
	private List<Paths> pathsList = new ArrayList<Paths>();

}
