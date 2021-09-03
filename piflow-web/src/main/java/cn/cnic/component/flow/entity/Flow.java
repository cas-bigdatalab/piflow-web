package cn.cnic.component.flow.entity;

import java.util.ArrayList;
import java.util.List;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Flow extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String name;
    private String uuid;
    private String driverMemory = "1g";
    private String executorNumber = "1";
    private String executorMemory = "1g";
    private String executorCores = "1";
    private String description;
    private String pageId;
    private Boolean isExample = false;
    private FlowGroup flowGroup;
    private MxGraphModel mxGraphModel;
    private List<Stops> stopsList = new ArrayList<Stops>();
    private List<Paths> pathsList = new ArrayList<Paths>();
    private String[] globalParamsIds;

}
