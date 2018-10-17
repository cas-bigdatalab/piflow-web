package com.nature.component.mxGraph.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MxGraphModelVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String dx;

	private String dy;

	private String grid;

	private String gridSize;

	private String guides;

	private String tooltips;

	private String connect;

	private String arrows;

	private String fold;

	private String page;

	private String pageScale;

	private String pageWidth;

	private String pageHeight;

	private String background;

	private List<MxCellVo> rootVo = new ArrayList<MxCellVo>();

	public String getDx() {
		return dx;
	}

	public void setDx(String dx) {
		this.dx = dx;
	}

	public String getDy() {
		return dy;
	}

	public void setDy(String dy) {
		this.dy = dy;
	}

	public String getGrid() {
		return grid;
	}

	public void setGrid(String grid) {
		this.grid = grid;
	}

	public String getGridSize() {
		return gridSize;
	}

	public void setGridSize(String gridSize) {
		this.gridSize = gridSize;
	}

	public String getGuides() {
		return guides;
	}

	public void setGuides(String guides) {
		this.guides = guides;
	}

	public String getTooltips() {
		return tooltips;
	}

	public void setTooltips(String tooltips) {
		this.tooltips = tooltips;
	}

	public String getConnect() {
		return connect;
	}

	public void setConnect(String connect) {
		this.connect = connect;
	}

	public String getArrows() {
		return arrows;
	}

	public void setArrows(String arrows) {
		this.arrows = arrows;
	}

	public String getFold() {
		return fold;
	}

	public void setFold(String fold) {
		this.fold = fold;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPageScale() {
		return pageScale;
	}

	public void setPageScale(String pageScale) {
		this.pageScale = pageScale;
	}

	public String getPageWidth() {
		return pageWidth;
	}

	public void setPageWidth(String pageWidth) {
		this.pageWidth = pageWidth;
	}

	public String getPageHeight() {
		return pageHeight;
	}

	public void setPageHeight(String pageHeight) {
		this.pageHeight = pageHeight;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public List<MxCellVo> getRootVo() {
		return rootVo;
	}

	public void setRootVo(List<MxCellVo> rootVo) {
		this.rootVo = rootVo;
	}

}
