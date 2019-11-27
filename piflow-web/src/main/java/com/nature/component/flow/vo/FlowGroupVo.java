package com.nature.component.flow.vo;

import com.nature.base.util.DateUtils;
import com.nature.component.flow.model.FlowGroupPaths;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class FlowGroupVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private Boolean isExample = false;
    private MxGraphModelVo mxGraphModelVo;
    private FlowProjectVo flowProjectVo;
    private Date crtDttm;
    private List<FlowVo> flowVoList = new ArrayList<>();
    private List<FlowGroupPathsVo> flowGroupPathsVoList = new ArrayList<>();


    public String getCrtDttmString() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
        return crtDttm != null ? sdf.format(crtDttm) : "";
    }
}
