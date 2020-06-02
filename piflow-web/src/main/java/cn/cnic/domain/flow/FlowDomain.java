package cn.cnic.domain.flow;

import cn.cnic.component.flow.model.Flow;
import cn.cnic.repository.flow.FlowJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class FlowDomain {

    @Resource
    private FlowJpaRepository flowJpaRepository;

    public Flow getFlowById(String id) {
        Flow flow = flowJpaRepository.getOne(id);
        if (null != flow && !flow.getEnableFlag()) {
            flow = null;
        }
        return flow;
    }

    public Flow saveOrUpdate(Flow flow) {
        return flowJpaRepository.save(flow);
    }

    public List<Flow> saveOrUpdate(List<Flow> flowList) {
        return flowJpaRepository.saveAll(flowList);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return flowJpaRepository.updateEnableFlagById(id, enableFlag);
    }

    public Flow getFlowByPageId(String fid, String pageId) {
        return flowJpaRepository.getFlowByPageId(fid, pageId);
    }

    public String getFlowIdByPageId(String fid, String pageId) {
        return flowJpaRepository.getFlowIdByPageId(fid, pageId);
    }

    public String getFlowIdByNameAndFlowGroupId(String fid, String flowName) {
        return flowJpaRepository.getFlowIdByNameAndFlowGroupId(fid, flowName);
    }

    public Integer getMaxFlowPageIdByFlowGroupId(String flowGroupId) {
        return flowJpaRepository.getMaxFlowPageIdByFlowGroupId(flowGroupId);
    }

    public Page<Flow> getFlowListPage(int page, int size, String param) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "crtDttm"));
        return flowJpaRepository.getFlowListPage(null == param ? "" : param, pageRequest);
    }

    public Page<Flow> getFlowListPageByUser(int page, int size, String param,String username) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "crtDttm"));
        return flowJpaRepository.getFlowListPage(username, null == param ? "" : param, pageRequest);
    }

    public String[] getFlowNamesByFlowGroupId(String flowGroupId){
        return flowJpaRepository.getFlowNamesByFlowGroupId(flowGroupId);
    }

    public String[] getFlowAndGroupNamesByFlowGroupId(String flowGroupId){
        return flowJpaRepository.getFlowAndGroupNamesByFlowGroupId(flowGroupId);
    }

}
