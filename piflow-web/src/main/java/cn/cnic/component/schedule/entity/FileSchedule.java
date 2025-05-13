package cn.cnic.component.schedule.entity;

import cn.cnic.base.BaseModelIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author tianyao
 * @description 文件触发
 * @date 2024/5/14 17:44
 */
@Setter
@Getter
public class FileSchedule extends BaseModelIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private String fileDict;
    private String filePrefix;
    private String fileSuffix;
    private String associateId;
    private Integer associateType; //0-Flow
    private String stopId;
    private String stopName;
    private String propertyId;
    private String propertyName;
    private Integer triggerMode;
    private Integer serialRule;
    private String regex;
    private Integer serialOrder;
    private String processId;
    private String filePath;
    private Integer fileSize;
    private Date fileLastModifyTime;
    private Integer state; //状态：0-初始化（INIT） 1-正在运行（RUNNING） 2-暂停（STOP）
}
