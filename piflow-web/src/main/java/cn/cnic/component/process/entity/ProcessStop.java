package cn.cnic.component.process.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.common.Eunm.StopState;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProcessStop extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private Process process;
    private String name;
    private String bundel;
    private String groups;
    private String owner;
    private String description;
    private String inports;
    private PortType inPortType;
    private String outports;
    private PortType outPortType;
    private StopState state = StopState.INIT;
    private Date startTime;
    private Date endTime;
    private String pageId;
    private List<ProcessStopProperty> processStopPropertyList = new ArrayList<ProcessStopProperty>();
    private List<ProcessStopCustomizedProperty> processStopCustomizedPropertyList = new ArrayList<>();
}
