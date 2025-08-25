package cn.cnic.component.schedule.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TaskExecutionInfo {
    /** 每次触发工作流时生成的唯一 ID */
    private String processId;

    /** 本次执行的开始时间 */
    private Date createTime;

    /** 本次执行所消耗的毫秒数 */
    private Long durationMillis;

    /** 本次执行的最终状态，例如 SUCCESS, FAILED 等 */
    private String status;


    public TaskExecutionInfo() {}

    public TaskExecutionInfo(String processId, Date createTime, Long durationMillis, String status, String errorMessage) {
        this.processId = processId;
        this.createTime = createTime;
        this.durationMillis = durationMillis;
        this.status = status;
    }

    @Override
    public String toString() {
        return "TaskExecutionInfo{" +
                "processId='" + processId + '\'' +
                ", createTime=" + createTime +
                ", durationMillis=" + durationMillis +
                ", status='" + status + '\'' +
                '}';
    }
}