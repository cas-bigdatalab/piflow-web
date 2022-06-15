package cn.cnic.component.mxGraph.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
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

    private Map<String, String> paramData = new HashMap<>();

}
