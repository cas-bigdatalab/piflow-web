package cn.cnic.component.process.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.ProcessParentType;
import cn.cnic.common.Eunm.ProcessState;
import cn.cnic.common.Eunm.RunModeType;
import cn.cnic.component.mxGraph.entity.MxGraphModel;
import cn.cnic.component.schedule.entity.Schedule;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Process extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String name;
    private String driverMemory;
    private String executorNumber;
    private String executorMemory;
    private String executorCores;
    private String viewXml;
    private String description;
    private String pageId;
    private String flowId;
    private String appId;
    private String parentProcessId;
    private String processId;
    private ProcessState state;
    private Date startTime;
    private Date endTime;
    private String progress;
    private RunModeType runModeType = RunModeType.RUN;
    private ProcessParentType processParentType;
    private Schedule schedule;
    private ProcessGroup processGroup;
    private MxGraphModel mxGraphModel;
    private List<ProcessStop> processStopList = new ArrayList<ProcessStop>();
    private List<ProcessPath> processPathList = new ArrayList<ProcessPath>();

	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

}
