package com.nature.component.mxGraph.model;

import javax.persistence.*;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;
import org.apache.ibatis.annotations.Insert;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "MX_CELL")
public class MxCell extends BaseHibernateModelUUIDNoCorpAgentId {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_MX_GRAPH_ID")
	private MxGraphModel mxGraphModel;

	@Column(name = "MX_PAGEID")
	private String pageId;

	@Column(name = "MX_PARENT")
	private String parent;

	@Column(name = "MX_STYLE")
	private String style;

	@Column(name = "MX_EDGE")
	private String edge; // 线有

	@Column(name = "MX_SOURCE")
	private String source; // 线有

	@Column(name = "MX_TARGET")
	private String target; // 线有

	@Column(name = "MX_VALUE")
	private String value;

	@Column(name = "MX_VERTEX")
	private String vertex;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "mxCell")
	@Where(clause = "enable_flag=1")
	private MxGeometry mxGeometry;

	public MxGraphModel getMxGraphModel() {
		return mxGraphModel;
	}

	public void setMxGraphModel(MxGraphModel mxGraphModel) {
		this.mxGraphModel = mxGraphModel;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getEdge() {
		return edge;
	}

	public void setEdge(String edge) {
		this.edge = edge;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getVertex() {
		return vertex;
	}

	public void setVertex(String vertex) {
		this.vertex = vertex;
	}

	public MxGeometry getMxGeometry() {
		return mxGeometry;
	}

	public void setMxGeometry(MxGeometry mxGeometry) {
		this.mxGeometry = mxGeometry;
	}

}
