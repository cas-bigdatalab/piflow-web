package cn.cnic.third.vo.schedule;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ThirdScheduleEntryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String scheduleEntryId;
    private String scheduleEntryType;
    
}
