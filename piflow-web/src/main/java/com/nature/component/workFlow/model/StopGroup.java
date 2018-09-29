package com.nature.component.workFlow.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;

/**
 * 组名称表
 * 
 * @author Nature
 *
 */
@Entity
@Table(name = "FLOW_SOTPS_GROUPS")
public class StopGroup extends BaseHibernateModelUUIDNoCorpAgentId {

	private static final long serialVersionUID = 1L;

	private String groupName; // 组名称

	// 组包含的stop
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "stopGroup")
	@Where(clause = "enable_flag=1")
	@OrderBy(clause = "lastUpdateDttm desc")
	private List<StopsTemplate> stopsList = new ArrayList<StopsTemplate>();

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<StopsTemplate> getStopsList() {
		return stopsList;
	}

	public void setStopsList(List<StopsTemplate> stopsList) {
		this.stopsList = stopsList;
	}

}
