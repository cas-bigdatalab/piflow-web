package cn.cnic.component.system.domain;

import cn.cnic.component.system.entity.UserOpinion;
import cn.cnic.component.system.mapper.SystemInfoMapper;
import cn.cnic.component.system.mapper.UserOpinionMapper;
import cn.cnic.component.system.vo.SystemInfoVo;
import cn.cnic.component.system.vo.UserOpiniontVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class UserOpinionDomain {

    @Autowired
    private UserOpinionMapper userOpinionMapper;

    public int save(UserOpinion userOpinion) {
        return userOpinionMapper.insert(userOpinion);
    }

    public int update(UserOpinion userOpinion) {
        return userOpinionMapper.update(userOpinion);
    }

    public UserOpinion getById(String id) {
        return userOpinionMapper.getById(id);
    }

    public int delete(String id) {
        return userOpinionMapper.delete(id);
    }

    public List<UserOpinion> getByPageWithAdmin(UserOpiniontVo userOpiniontVo) {
        return userOpinionMapper.getByPageWithAdmin(userOpiniontVo);
    }

    public List<UserOpinion> getByPage(UserOpiniontVo userOpiniontVo) {
        return userOpinionMapper.getByPage(userOpiniontVo);
    }

    public int updateState(UserOpiniontVo userOpinionVo) {
        return userOpinionMapper.updateState(userOpinionVo);
    }
}
