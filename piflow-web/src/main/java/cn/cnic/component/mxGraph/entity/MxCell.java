package cn.cnic.component.mxGraph.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MxCell extends BaseHibernateModelUUIDNoCorpAgentId {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private MxGraphModel mxGraphModel;
    private String pageId;
    private String parent;
    private String style;
    private String edge; // Line has
    private String source; // Line has
    private String target; // Line has
    private String value;
    private String vertex;
    private MxGeometry mxGeometry;

}
