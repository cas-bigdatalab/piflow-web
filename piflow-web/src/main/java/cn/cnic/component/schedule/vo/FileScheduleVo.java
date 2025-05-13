package cn.cnic.component.schedule.vo;

import cn.cnic.base.vo.BasePageVo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class FileScheduleVo extends BasePageVo implements Serializable {

    private String id;
    private String name;
    private String description;
    private String fileDict;
    private String filePrefix;
    private String fileSuffix;
    private String associateId;
    private String stopId;
    private String stopName;
    private String propertyId;
    private String propertyName;
    private Integer triggerMode;
    private Integer serialRule;
    private String regex;
    private Integer serialOrder;
    private String processId;
    private Integer state; //状态：0-初始化（INIT） 1-正在运行（RUNNING） 2-暂停（STOP）
    private Long version;
}
