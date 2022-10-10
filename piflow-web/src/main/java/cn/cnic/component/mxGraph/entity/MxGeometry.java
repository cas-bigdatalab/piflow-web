package cn.cnic.component.mxGraph.entity;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MxGeometry extends BaseModelUUIDNoCorpAgentId {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private MxCell mxCell;
    private String relative;
    private String as;
    private String x;
    private String y;
    private String width;
    private String height;

}
