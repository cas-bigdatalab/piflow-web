package com.nature.component.flow.model;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name =  "flow_info")
@Setter
@Getter
public class FlowInfoDb extends BaseHibernateModelUUIDNoCorpAgentId {

	private static final long serialVersionUID = 1L;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_FLOW_ID")
	private Flow flow;

	private String name;
	private String state;
	private Date startTime;
	private Date endTime;
	private String progress;

}
