package cn.cnic.third.vo.schedule;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class ThirdScheduleVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String createTime;
    private String cronExpression;
    private String state;
    private String startDate;
    private String endDate;
    private String updateTime;
    private List<ThirdScheduleEntryVo> entryList;

}
