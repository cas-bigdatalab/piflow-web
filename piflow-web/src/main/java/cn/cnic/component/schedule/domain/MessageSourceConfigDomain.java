package cn.cnic.component.schedule.domain;

import cn.cnic.component.schedule.entity.MessageTriggerTaskDefinition;
import cn.cnic.component.schedule.mapper.MessageSourceConfigMapper;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class MessageSourceConfigDomain {
    private final MessageSourceConfigMapper mapper;

    @Autowired
    public MessageSourceConfigDomain(MessageSourceConfigMapper mapper) {
        this.mapper = mapper;
    }

    public List<MessageTriggerTaskDefinition> getAll(Integer page, Integer limit, String search) {
        int pageNum = (page == null || page < 1) ? 1 : page;
        int pageSize = (limit == null || limit < 1) ? 10 : limit;
        int offset = (pageNum - 1) * pageSize;
        return mapper.selectByPageAndParam(offset, pageSize, search);
    }

    public MessageTriggerTaskDefinition getById(Long id) {
        return mapper.getById(id);
    }
    public Integer getConcurrencyLimitById(Long id) {
        return mapper.getConcurrencyLimitById(id);
    }

    public int insert(MessageTriggerTaskDefinition config) {
        return mapper.insert(config);
    }

    public int update(MessageTriggerTaskDefinition config) {
        return mapper.update(config);
    }

    public int deleteById(Long id) {
        return mapper.deleteById(id);
    }

    public String getMessageSourcePasswordById(Long id) {
        return mapper.getMessageSourcePasswordById(id);
    }

    public int updateStatusById(Long id, String status, Date updateTime) {
        return mapper.updateStatusById(id, status, updateTime) ;
    }
}