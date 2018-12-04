package com.nature.component.mxGraph.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.nature.component.workFlow.model.Flow;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import com.nature.base.BaseHibernateModelUUIDNoCorpAgentId;

@Entity
@Table(name = "MX_GRAPH_MODEL")
public class MxGraphModel extends BaseHibernateModelUUIDNoCorpAgentId {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_FLOW_ID")
    private Flow flow;

    @Column(name = "MX_DX")
    private String dx;

    @Column(name = "MX_DY")
    private String dy;

    @Column(name = "MX_GRID")
    private String grid;

    @Column(name = "MX_GRIDSIZE")
    private String gridSize;

    @Column(name = "MX_GUIDES")
    private String guides;

    @Column(name = "MX_TOOLTIPS")
    private String tooltips;

    @Column(name = "MX_CONNECT")
    private String connect;

    @Column(name = "MX_ARROWS")
    private String arrows;

    @Column(name = "MX_FOLD")
    private String fold;

    @Column(name = "MX_PAGE")
    private String page;

    @Column(name = "MX_PAGESCALE")
    private String pageScale;

    @Column(name = "MX_PAGEWIDTH")
    private String pageWidth;

    @Column(name = "MX_PAGEHEIGHT")
    private String pageHeight;

    @Column(name = "MX_BACKGROUND")
    private String background;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mxGraphModel")
    @Where(clause = "enable_flag=1")
    @OrderBy(clause = "lastUpdateDttm desc")
    private List<MxCell> root = new ArrayList<MxCell>();

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

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

    public List<MxCell> getRoot() {
        return root;
    }

    public void setRoot(List<MxCell> root) {
        this.root = root;
    }

}
