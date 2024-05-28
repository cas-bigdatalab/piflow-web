package cn.cnic.component.process.domain;

import cn.cnic.component.process.entity.ErrorLogMapping;
import cn.cnic.component.process.mapper.ErrorLogMappingMapper;
import cn.cnic.component.process.vo.ErrorLogMappingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class ErrorLogMappingDomain {

    @Autowired
    private ErrorLogMappingMapper errorLogMappingMapper;

    public int insert(ErrorLogMapping errorLogMapping) {
        return errorLogMappingMapper.insert(errorLogMapping);
    }

    public int update(ErrorLogMapping errorLogMapping) {
        return errorLogMappingMapper.update(errorLogMapping);
    }

    public int delete(ErrorLogMapping errorLogMapping) {
        return errorLogMappingMapper.delete(errorLogMapping);
    }

    public ErrorLogMappingVo getById(Long id) {
        return errorLogMappingMapper.getById(id);
    }

    public List<ErrorLogMappingVo> getByPage(ErrorLogMappingVo errorLogMappingVo, String username) {
        return errorLogMappingMapper.getByPage(errorLogMappingVo.getKeyword(), username);
    }

    public List<ErrorLogMapping> getAllAvailable() {
        return errorLogMappingMapper.getAllAvailable();
    }
}
