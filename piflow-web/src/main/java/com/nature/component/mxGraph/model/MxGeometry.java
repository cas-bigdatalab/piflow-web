package com.nature.component.mxGraph.model;

import javax.persistence.*;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;

@Entity
@Table(name = "MX_GEOMETRY")
public class MxGeometry extends BaseHibernateModelUUIDNoCorpAgentId {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FK_MX_CELL_ID")
	private MxCell mxCell;

	@Column(name = "MX_RELATIVE")
	private String relative;

	@Column(name = "MX_AS")
	private String as;

	@Column(name = "MX_X")
	private String x;

	@Column(name = "MX_Y")
	private String y;

	@Column(name = "MX_WIDTH")
	private String width;

	@Column(name = "MX_HEIGHT")
	private String height;

	public MxCell getMxCell() {
		return mxCell;
	}

	public void setMxCell(MxCell mxCell) {
		this.mxCell = mxCell;
	}

	public String getRelative() {
		return relative;
	}

	public void setRelative(String relative) {
		this.relative = relative;
	}

	public String getAs() {
		return as;
	}

	public void setAs(String as) {
		this.as = as;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
}
