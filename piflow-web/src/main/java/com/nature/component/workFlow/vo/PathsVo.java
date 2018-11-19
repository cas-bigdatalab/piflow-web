package com.nature.component.workFlow.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.nature.base.util.DateUtils;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Stops;

public class PathsVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private Flow flow;

    private String from;

    private String outport;

    private String inport;

    private String to;

    private String port;

    private String pageId;

    private Stops stopFrom; 
    
    private Stops StopTo;
    
    private Date crtDttm;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getOutport() {
        return outport;
    }

    public void setOutport(String outport) {
        this.outport = outport;
    }

    public String getInport() {
        return inport;
    }

    public void setInport(String inport) {
        this.inport = inport;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

	public Stops getStopFrom() {
		return stopFrom;
	}

	public void setStopFrom(Stops stopFrom) {
		this.stopFrom = stopFrom;
	}

	public Stops getStopTo() {
		return StopTo;
	}

	public void setStopTo(Stops stopTo) {
		StopTo = stopTo;
	}

	public Date getCrtDttm() {
		return crtDttm;
	}

	public void setCrtDttm(Date crtDttm) {
		this.crtDttm = crtDttm;
	}
	
	public String getCrtDttmString() {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
		return crtDttm != null ? sdf.format(crtDttm) : "";
	}
}
