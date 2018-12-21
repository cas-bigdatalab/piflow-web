package com.nature.component.template.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nature.base.util.LoggerUtil;
import com.nature.component.template.service.ITemplateService;
import com.nature.component.workFlow.model.Template;
import com.nature.mapper.template.TemplateMapper;

@Service
@Transactional
public class TemplateServiceImpl implements ITemplateService {

    Logger logger = LoggerUtil.getLogger();

    @Autowired
    private TemplateMapper templateMapper;

	@Override
	public int addTemplate(Template template) {
		return templateMapper.addFlow(template);
	}

	@Override
	public List<Template> findTemPlateList() {
		return templateMapper.findTemPlateList();
	}

	@Override
	public int deleteTemplate(String id) {
		return templateMapper.updateEnableFlagById(id);
	}

	@Override
	public Template queryTemplate(String id) {
		return templateMapper.queryTemplate(id);
	}

}
