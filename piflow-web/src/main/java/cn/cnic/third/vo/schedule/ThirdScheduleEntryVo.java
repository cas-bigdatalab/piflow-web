package cn.cnic.third.vo.schedule;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class ThirdScheduleEntryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String scheduleEntryId;
    private String scheduleEntryType;
    
}
