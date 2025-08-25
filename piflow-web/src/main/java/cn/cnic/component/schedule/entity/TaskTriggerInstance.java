package cn.cnic.component.schedule.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 任务触发实例实体。
 * 记录每一次消息触发工作流的详细信息和状态。
 */
@Getter
@Setter
@NoArgsConstructor
public class TaskTriggerInstance {
    private static final long serialVersionUID = 1L;

    private Long id; // 数据库主键ID
    private String type; // 调度类型
    private String triggerInstanceId; // 内部生成的唯一触发实例ID，用于日志追踪，UUID
    private Long messageSourceId; // 关联的消息源ID
    private String messageId; // 关联的消息的MessageId (来自IMessage)
    private String protocolType; // 消息协议类型 (来自IMessage)
    private String targetWorkflowId; // 目标工作流的ID或名称
    private String targetWorkflowName; // 目标工作流的ID或名称
    private String processId; // process，则记录process的ID
    private Date triggerTime; // 触发时间
    private String status; // 任务状态：RUNNING, SUCCESS, FAILED, RETRYING 等
    private Long durationMillis; // 触发耗时（毫秒）
    private String errorMessage; // 错误信息，如果触发失败
    private String rawMessageContent; // 原始消息内容（部分或全部），用于回溯
    private String executionList;
    private Date createTime; // 创建时间
    private Date updateTime; // 更新时间

    public TaskTriggerInstance(Date createTime) {
        this.createTime = createTime;
        this.updateTime = createTime;
    }
}