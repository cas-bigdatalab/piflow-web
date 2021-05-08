package cn.cnic.component.process.vo;

import java.io.Serializable;

public class ProcessPathVo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private ProcessVo processVo;
    private String from;
    private String outport;
    private String inport;
    private String to;
    private String pageId;

    public ProcessVo getProcessVo() {
        return processVo;
    }

    public void setProcessVo(ProcessVo processVo) {
        this.processVo = processVo;
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

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }
}
