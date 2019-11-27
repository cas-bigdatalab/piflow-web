package com.nature.component.mxGraph.vo;

import java.io.Serializable;

public class MxCellVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MxGraphModelVo mxGraphModelVo;

	private String pageId;

	private String parent;

	private String style;

	private String edge; // Line has

	private String source; // Line has

	private String target; // Line has

	private String value;

	private String vertex;

	private MxGeometryVo mxGeometryVo;

	public MxGraphModelVo getMxGraphModelVo() {
		return mxGraphModelVo;
	}

	public void setMxGraphModelVo(MxGraphModelVo mxGraphModelVo) {
		this.mxGraphModelVo = mxGraphModelVo;
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

	public MxGeometryVo getMxGeometryVo() {
		return mxGeometryVo;
	}

	public void setMxGeometryVo(MxGeometryVo mxGeometryVo) {
		this.mxGeometryVo = mxGeometryVo;
	}

}
