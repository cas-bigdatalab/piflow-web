package cn.cnic.component.process.entity;

import cn.cnic.base.BaseModelUUIDNoCorpAgentId;
import cn.cnic.common.Eunm.ComponentFileType;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.common.Eunm.StopState;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class ProcessStop extends BaseModelUUIDNoCorpAgentId {

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
    private String startTime;
    private String endTime;
    private String pageId;
    private List<ProcessStopProperty> processStopPropertyList = new ArrayList<ProcessStopProperty>();
    private List<ProcessStopCustomizedProperty> processStopCustomizedPropertyList = new ArrayList<>();
    private Boolean isDataSource = false;

    private String dockerImagesName;          //docker image name,not save in process_stop
    private ComponentFileType componentType;        //component type,not save in process_stop
}
