package cn.cnic.component.schedule.entity;

import cn.cnic.common.Eunm.MessageProtocol;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息触发任务定义实体。
 * 对应数据库中的一张表，存储各种消息源的详细配置。
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "name", "protocol", "filterRuleJson", "targetWorkflowId",  "status", "updateTime", "properties"})
public class MessageTriggerTaskDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id; // 主键ID
    private String name; // 消息源名称
    private String type; // 调度类型
    private MessageProtocol protocol; // 消息协议类型 (如 RABBITMQ, KAFKA)

    private String properties;
    private String advancedConfig;


    // 触发规则与目标
    private String filterRuleType;
    private String filterRuleJson; // 消息过滤规则（Aviator Script表达式）JSON
    private String targetWorkflowId; // 目标工作流ID（或PiFlow流程ID）
    private String targetWorkflowName; // 目标工作流的ID或名称
    private Integer concurrencyLimit;
    private String status; // 消息源状态：ENABLED, PAUSED, DELETED
    private String creatorId;

    private Date createTime; // 创建时间
    private Date updateTime; // 更新时间
}