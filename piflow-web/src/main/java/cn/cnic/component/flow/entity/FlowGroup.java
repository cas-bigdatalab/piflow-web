package cn.cnic.component.flow.entity;

import java.util.ArrayList;
import java.util.List;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FlowGroup extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private String pageId;
    private Boolean isExample = false;
    private MxGraphModel mxGraphModel;
    private List<Flow> flowList = new ArrayList<>();
    private List<FlowGroupPaths> flowGroupPathsList = new ArrayList<>();
    private FlowGroup flowGroup;
    private List<FlowGroup> flowGroupList = new ArrayList<>();

}
