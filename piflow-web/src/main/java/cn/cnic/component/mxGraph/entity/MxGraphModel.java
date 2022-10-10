package cn.cnic.component.mxGraph.entity;

import java.util.ArrayList;
import java.util.List;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import cn.cnic.component.flow.entity.Flow;
import cn.cnic.component.flow.entity.FlowGroup;
import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.entity.ProcessGroup;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MxGraphModel extends BaseModelUUIDNoCorpAgentId {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Flow flow;
    private FlowGroup flowGroup;
    private Process process;
    private ProcessGroup processGroup;
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
    private List<MxCell> root = new ArrayList<>();

}
