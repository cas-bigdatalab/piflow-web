package cn.cnic.component.flow.vo;

import cn.cnic.base.util.DateUtils;
import cn.cnic.common.Eunm.PortType;
import cn.cnic.component.dataSource.vo.DataSourceVo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Stop component table
 */
@Setter
@Getter
public class StopsVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private FlowVo flowVo;

    private String name;

    private String bundel;

    private String groups;

    private String owner;

    private String description;

    private String pageId;

    private String inports;

    private PortType inPortType;

    private String outports;

    private PortType outPortType;

    private Boolean isCheckpoint;

    private Long version;

    private Boolean isCustomized = false;

    private Date crtDttm = new Date();

    private String language;

    private DataSourceVo dataSourceVo;

    private List<StopsPropertyVo> propertiesVo = new ArrayList<>();

    private List<StopsPropertyVo> oldPropertiesVo = new ArrayList<>();

    private List<StopsCustomizedPropertyVo> stopsCustomizedPropertyVoList = new ArrayList<>();

    public String getCrtDttmString() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
        return crtDttm != null ? sdf.format(crtDttm) : "";
    }
}
