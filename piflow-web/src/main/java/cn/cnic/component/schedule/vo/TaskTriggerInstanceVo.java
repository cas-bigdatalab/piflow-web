package cn.cnic.component.schedule.vo;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 任务触发实例VO
 */
@Getter
@Setter
public class TaskTriggerInstanceVo {
    private Long id;
    private Long messageSourceId;
    private String processId;
    private Date triggerTime;
    private String status;
    private Long durationMillis;
    private String errorMessage;
    private String rawMessageContent;
    private String executionList;
} 