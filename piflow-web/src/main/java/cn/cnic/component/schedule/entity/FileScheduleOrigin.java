package cn.cnic.component.schedule.entity;

import cn.cnic.base.BaseModelIDNoCorpAgentId;
import lombok.Getter;
import lombok.Setter;

/**
 * @author tianyao
 * @description 文件触发
 * @date 2024/5/14 17:44
 */
@Setter
@Getter
public class FileScheduleOrigin extends BaseModelIDNoCorpAgentId {

    private static final long serialVersionUID = 1L;

    private Long scheduleId;
    private String originFileRecord;
    private String pendingFileRecord;
}
