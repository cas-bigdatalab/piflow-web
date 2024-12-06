package cn.cnic.component.schedule.domain;

import cn.cnic.component.schedule.entity.FileSchedule;
import cn.cnic.component.schedule.entity.FileScheduleOrigin;
import cn.cnic.component.schedule.mapper.FileScheduleMapper;
import cn.cnic.component.schedule.vo.FileScheduleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class FileScheduleDomain {
    private final FileScheduleMapper fileScheduleMapper;

    @Autowired
    public FileScheduleDomain(FileScheduleMapper fileScheduleMapper) {
        this.fileScheduleMapper = fileScheduleMapper;
    }

    public FileSchedule getById(long id) {
        return fileScheduleMapper.getById(id);
    }

    public int insert(FileSchedule fileSchedule) {
        return fileScheduleMapper.insert(fileSchedule);
    }

    /**
     * update schedule
     *
     * @param fileSchedule
     * @return
     */
    public int update(FileSchedule fileSchedule) {
        return fileScheduleMapper.update(fileSchedule);
    }

    public List<FileScheduleVo> getFileScheduleListByPage(String keyword,String username) {
        return fileScheduleMapper.getFileScheduleListByPage(keyword,username);
    }

    public int deleteById(Long id) {
        return fileScheduleMapper.deleteById(id);
    }

    public List<FileSchedule> getAllRunning() {
        return fileScheduleMapper.getAllRunning();
    }

    public int updateOrigin(FileScheduleOrigin origin) {
        return fileScheduleMapper.updateOrigin(origin);
    }

    public FileScheduleOrigin getOriginByScheduleId(Long scheduleId) {
        return fileScheduleMapper.getOriginByScheduleId(scheduleId);
    }

    public int insertOrigin(FileScheduleOrigin fileScheduleOrigin) {
        return fileScheduleMapper.insertOrigin(fileScheduleOrigin);
    }
}
