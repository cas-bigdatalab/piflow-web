package cn.cnic.component.flow.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.base.utils.DateUtils;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.component.dataSource.entity.DataSource;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * stop component table
 */
@Getter
@Setter
public class Stops extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private Flow flow;
    private String name;
    private String bundel;
    private String groups;
    private String owner;
    private String description;
    private String inports;
    private PortType inPortType;
    private String outports;
    private PortType outPortType;
    private String pageId;
    private String state;
    private Date startTime;
    private Date stopTime;
    private Boolean isCheckpoint;
    private Boolean isCustomized = false;
    private DataSource dataSource;
    private List<Property> properties = new ArrayList<Property>();
    private List<Property> oldProperties = new ArrayList<Property>();
    private List<CustomizedProperty> customizedPropertyList = new ArrayList<>();
    private Boolean isDataSource = false;

    public String getStartTimes() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
        return startTime != null ? sdf.format(startTime) : "";
    }

    public String getStopTimes() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
        return stopTime != null ? sdf.format(stopTime) : "";
    }
}
