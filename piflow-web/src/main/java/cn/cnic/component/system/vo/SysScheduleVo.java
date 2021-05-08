package cn.cnic.component.system.vo;

import cn.cnic.base.util.DateUtils;
import cn.cnic.common.Eunm.ScheduleRunResultType;
import cn.cnic.common.Eunm.ScheduleState;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class SysScheduleVo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String id;
    private Date crtDttm;
    private Date lastUpdateDttm;
    private String jobName;
    private String jobClass;
    private String cronExpression;
    private ScheduleState status;
    private ScheduleRunResultType lastRunResult;
    private String description;

    /**
     * @return yyyy-MM-dd HH:mm:ss ,such as 2012-12-25 20:20:20
     */
    public String getCrtDttmString() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
        return crtDttm != null ? sdf.format(crtDttm) : "";
    }

    /**
     * @return yyyy-MM-dd HH:mm:ss ,such as 2012-12-25 20:20:20
     */
    public String getLastUpdateDttmString() {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN_yyyy_MM_dd_HH_MM_ss);
        return lastUpdateDttm != null ? sdf.format(lastUpdateDttm) : "";
    }
}
