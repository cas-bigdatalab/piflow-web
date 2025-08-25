package cn.cnic.component.schedule.vo;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * 消息源（事件定义）VO
 */
@Getter
@Setter
public class MessageSourceConfigVo {
    private Long id;
    private String name;
    private String type; // 调度类型
    private String protocol; // 与 MessageProtocol 枚举对应

    // 通用的属性Map，存储所有协议特有的配置
    private String properties;
    private String filterRuleType;
    private String filterRuleJson;
    private String targetWorkflowId;
    private String targetWorkflowName;
    /**
     * 此任务定义的并发执行上限。
     * 可以设置为 null 或 0 表示不限制。
     */
    private Integer concurrencyLimit;
    private String status;
    private Date createTime;
    private Date updateTime;
}