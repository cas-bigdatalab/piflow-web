package cn.cnic.component.process.jpa.domain;

import cn.cnic.component.process.entity.Process;
import cn.cnic.component.process.jpa.repository.ProcessJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessDomain {

    @Autowired
    private ProcessJpaRepository processJpaRepository;

    public Process getProcessById(String id) {
        Process process = processJpaRepository.getOne(id);
        if (null != process && !process.getEnableFlag()) {
            process = null;
        }
        return process;
    }

    public Page<Process> getProcessListPage(String username, boolean isAdmin, int page, int size, String param) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "crtDttm"));
        if (isAdmin) {
            return processJpaRepository.getProcessListPage(null == param ? "" : param, pageRequest);
        } else {
            return processJpaRepository.getProcessListPage(username, null == param ? "" : param, pageRequest);
        }
    }

    public Process saveOrUpdate(Process process) {
        return processJpaRepository.save(process);
    }

    public List<Process> saveOrUpdate(List<Process> processList) {
        return processJpaRepository.saveAll(processList);
    }

    public Process getProcessByPageId(String fid, String pageId) {
        return processJpaRepository.getProcessByPageId(fid, pageId);
    }

    public String getProcessIdByPageId(String fid, String pageId) {
        return processJpaRepository.getProcessIdByPageId(fid, pageId);
    }

    public String getProcessIdByNameAndProcessGroupId(String fid, String processName) {
        return processJpaRepository.getProcessIdByNameAndProcessGroupId(fid, processName);
    }

    public String getMaxStopPageIdByProcessGroupId(String processGroupId) {
        return processJpaRepository.getMaxStopPageIdByProcessGroupId(processGroupId);
    }

    public Process getProcessNoGroupByAppId(String appId) {
        return processJpaRepository.getProcessNoGroupByAppId(appId);
    }

    public List<String> getRunningProcessAppId() {
        return processJpaRepository.getRunningProcessAppId();
    }

}
