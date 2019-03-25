package com.nature.component.template.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nature.base.util.JsonUtils;
import com.nature.base.util.PageHelperUtils;
import com.nature.base.util.SessionUserUtil;
import com.nature.base.vo.UserVo;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nature.base.util.LoggerUtil;
import com.nature.component.template.service.ITemplateService;
import com.nature.component.flow.model.Template;
import com.nature.mapper.template.TemplateMapper;

import javax.annotation.Resource;

@Service
@Transactional
public class TemplateServiceImpl implements ITemplateService {

    Logger logger = LoggerUtil.getLogger();

    @Resource
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

    @Override
    public String getTemplateListPage(Integer offset, Integer limit, String param) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        UserVo currentUser = SessionUserUtil.getCurrentUser();
        if (null != offset && null != limit) {
            Page page = PageHelper.startPage(offset, limit);
            templateMapper.findTemPlateListPage(currentUser, param);
            rtnMap = PageHelperUtils.setDataTableParam(page, rtnMap);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

}
