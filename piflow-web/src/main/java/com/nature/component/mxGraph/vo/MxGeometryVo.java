package com.nature.component.mxGraph.vo;

import java.io.Serializable;

public class MxGeometryVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String relative;

	private String as;

	private String x;

	private String y;

	private String width;

	private String height;

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
