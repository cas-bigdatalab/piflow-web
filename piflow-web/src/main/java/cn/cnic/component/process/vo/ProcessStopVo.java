package cn.cnic.component.process.vo;

import cn.cnic.base.utils.DateUtils;
import cn.cnic.common.Eunm.PortType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProcessStopVo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private ProcessVo processVo;
    private String id;
    private String name;
    private String bundel;
    private String groups;
    private String owner;
    private String description;
    private String inports;
    private PortType inPortType;
    private String outports;
    private PortType outPortType;
    private String state;
    private String startTime;
    private String endTime;
    private String pageId;
    private String visualizationType;
    private List<ProcessStopPropertyVo> processStopPropertyVoList = new ArrayList<ProcessStopPropertyVo>();
    private List<ProcessStopsCustomizedPropertyVo> processStopCustomizedPropertyVoList = new ArrayList<>();
    private Boolean isDataSource;

    public String getStartTimeStr() {
        return this.startTime;
    }

    public String getEndTimeStr() {
        return this.endTime;
    }
}
