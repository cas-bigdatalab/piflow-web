package cn.cnic.component.flow.vo;

import cn.cnic.component.mxGraph.vo.MxGraphModelVo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class FlowProjectVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String uuid;
    private String description;
    private Boolean isExample = false;
    private MxGraphModelVo mxGraphModelVo;
    private List<FlowVo> flowVoList = new ArrayList<>();
    private List<FlowGroupVo> flowGroupVoList = new ArrayList<>();

}
