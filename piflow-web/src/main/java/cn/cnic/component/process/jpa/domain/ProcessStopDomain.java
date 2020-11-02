package cn.cnic.component.process.jpa.domain;

import cn.cnic.component.process.entity.ProcessStop;
import cn.cnic.component.process.jpa.repository.ProcessStopJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessStopDomain {

    @Autowired
    private ProcessStopJpaRepository processStopJpaRepository;

    public ProcessStop getProcessStopById(String id) {
        ProcessStop processStop = processStopJpaRepository.getOne(id);
        if (null == processStop || !processStop.getEnableFlag()) {
            processStop = null;
        }
        return processStop;
    }

    public Page<ProcessStop> getProcessStopListPage(String username, boolean isAdmin, int page, int size, String param) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "crtDttm"));
        if (isAdmin) {
            return processStopJpaRepository.getProcessStopListPage(null == param ? "" : param, pageRequest);
        } else {
            return processStopJpaRepository.getProcessStopListPage(username, null == param ? "" : param, pageRequest);
        }
    }

    public ProcessStop saveOrUpdate(ProcessStop processStop) {
        return processStopJpaRepository.save(processStop);
    }

    public List<ProcessStop> saveOrUpdate(List<ProcessStop> processStopList) {
        return processStopJpaRepository.saveAll(processStopList);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return processStopJpaRepository.updateEnableFlagById(id, enableFlag);
    }


}
