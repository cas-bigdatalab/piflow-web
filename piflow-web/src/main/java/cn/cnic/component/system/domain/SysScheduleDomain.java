package cn.cnic.component.system.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.component.system.entity.SysSchedule;
import cn.cnic.component.system.mapper.SysScheduleMapper;
import cn.cnic.component.system.vo.SysScheduleVo;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class SysScheduleDomain {

    private final SysScheduleMapper sysScheduleMapper;

    @Autowired
    public SysScheduleDomain(SysScheduleMapper sysScheduleMapper) {
        this.sysScheduleMapper = sysScheduleMapper;
    }

    /**
     * getSysScheduleList
     *
     * @param param
     * @return
     */
    public List<SysScheduleVo> getSysScheduleList(boolean isAdmin, String param) {
        return sysScheduleMapper.getSysScheduleList(isAdmin, param);
    }

    /**
     * getSysScheduleById
     *
     * @param isAdmin
     * @param id
     * @return
     */
    public SysSchedule getSysScheduleById(boolean isAdmin, String id) {
        return sysScheduleMapper.getSysScheduleById(isAdmin, id);
    }

    /**
     * getSysScheduleVoById
     *
     * @param isAdmin
     * @param id
     * @return
     */
    public SysScheduleVo getSysScheduleVoById(boolean isAdmin, String id) {
        return sysScheduleMapper.getSysScheduleVoById(isAdmin, id);
    }

    public int insertSysSchedule(SysSchedule sysSchedule) {
        if (null == sysSchedule) {
            return 0;
        }
        return sysScheduleMapper.insert(sysSchedule);
    }

    public int updateSysSchedule(SysSchedule sysSchedule) {
        if (null == sysSchedule) {
            return 0;
        }
        return sysScheduleMapper.update(sysSchedule);
    }

}