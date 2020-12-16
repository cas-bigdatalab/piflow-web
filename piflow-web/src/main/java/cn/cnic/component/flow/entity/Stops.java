package cn.cnic.component.flow.entity;

import cn.cnic.base.BaseHibernateModelUUIDNoCorpAgentId;
import cn.cnic.base.util.DateUtils;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.component.dataSource.entity.DataSource;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * stop component table
 */
@Getter
@Setter
@Entity
@Table(name = "FLOW_STOPS")
public class Stops extends BaseHibernateModelUUIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_FLOW_ID")
    private Flow flow;

    private String name;

    private String bundel;

    private String groups;

    private String owner;

    @Column(columnDefinition = "text(0) COMMENT 'description'")
    private String description;

    private String inports;

    @Enumerated(EnumType.STRING)
    private PortType inPortType;

    private String outports;

    @Enumerated(EnumType.STRING)
    private PortType outPortType;

    @Column(name = "page_id")
    private String pageId;

    private String state;

    private Date startTime;

    private Date stopTime;

    private Boolean isCheckpoint;

    private Boolean isCustomized = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_DATA_SOURCE_ID")
    private DataSource dataSource;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "stops")
    @Where(clause = "enable_flag=1 and is_old_data=0")
    @OrderBy(clause = "propertySort desc")
    private List<Property> properties = new ArrayList<Property>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "stops")
    @Where(clause = "enable_flag=1 and is_old_data=1")
    @OrderBy(clause = "propertySort desc")
    private List<Property> oldProperties = new ArrayList<Property>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "stops")
    @Where(clause = "enable_flag=1")
    @OrderBy(clause = "lastUpdateDttm asc")
    private List<CustomizedProperty> customizedPropertyList = new ArrayList<>();

    public String getStartTimes() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
        return startTime != null ? sdf.format(startTime) : "";
    }

    public String getStopTimes() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
        return stopTime != null ? sdf.format(stopTime) : "";
    }
}
