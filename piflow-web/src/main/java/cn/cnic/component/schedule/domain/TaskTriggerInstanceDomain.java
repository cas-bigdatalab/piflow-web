package cn.cnic.component.schedule.domain;

import cn.cnic.common.Eunm.MessageProtocol;
import cn.cnic.component.schedule.entity.TaskTriggerInstance;
import cn.cnic.component.schedule.mapper.TaskTriggerInstanceMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class TaskTriggerInstanceDomain {
    private final TaskTriggerInstanceMapper mapper;

    @Autowired
    public TaskTriggerInstanceDomain(TaskTriggerInstanceMapper mapper) {
        this.mapper = mapper;
    }

    public TaskTriggerInstance getById(Long id) {
        return mapper.getById(id);
    }


    public List<TaskTriggerInstance> selectByMessageSourceIdAndStatus(Long messageSourceId, String status, int limit, int offset) {
        return mapper.selectByMessageSourceIdAndStatus(messageSourceId, status, limit, offset);
    }

    public List<TaskTriggerInstance> selectByMessageSourceId(Long messageSourceId, int limit, int offset) {
        return mapper.selectByMessageSourceId(messageSourceId, limit, offset);
    }

    public List<TaskTriggerInstance> selectByStatus(Long messageSourceId, String status) {
        return mapper.selectByStatus(messageSourceId, status);
    }
    public int insert(TaskTriggerInstance instance){
       return mapper.insert(instance);
    }

    public void update(TaskTriggerInstance instance){
         mapper.update(instance);
    }

    public void deleteByMessageSourceId(Long id) {
        mapper.deleteByMessageSourceId(id);
    }

    public int countByMessageSourceIdAndStatus(Long messageSourceId, String status) {
        return mapper.countByMessageSourceIdAndStatus(messageSourceId, status);
    }

    public int countByMessageSourceId(Long messageSourceId) {
        return mapper.countByMessageSourceId(messageSourceId);
    }

    public TaskTriggerInstance findByMessageIdWithLock(String messageId) {
        return mapper.findByMessageIdWithLock(messageId);
    }

    public int checkIsDuplicateMessage(MessageProtocol protocol, String messageId) {
        return mapper.checkIsDuplicateMessage(protocol, messageId);
    }
}