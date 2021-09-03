package cn.cnic.component.template.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.cnic.component.template.entity.FlowTemplate;
import cn.cnic.component.template.mapper.FlowTemplateMapper;

@Component
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
public class FlowTemplateDomain {

    @Autowired
    private FlowTemplateMapper flowTemplateMapper;

    public int insertFlowTemplate(FlowTemplate flowTemplate) {
        return flowTemplateMapper.insertFlowTemplate(flowTemplate);
    }

    public int updateEnableFlagById(String id, boolean enableFlag) {
        return flowTemplateMapper.updateEnableFlagById(id, enableFlag);
    }

    public FlowTemplate getFlowTemplateById(String id) {
        return flowTemplateMapper.getFlowTemplateById(id);
    }

    public List<FlowTemplate> getFlowTemplateList(String username, boolean isAdmin) {
        return flowTemplateMapper.getFlowTemplateList(username, isAdmin);
    }

    public List<FlowTemplate> getFlowTemplateListByParam(String username, boolean isAdmin, String param) {
        return flowTemplateMapper.getFlowTemplateListByParam(username, isAdmin, param);
    }

}
