package com.nature.component.template.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nature.base.util.*;
import com.nature.component.flow.model.Flow;
import com.nature.component.flow.model.Paths;
import com.nature.component.flow.model.Property;
import com.nature.component.flow.model.Stops;
import com.nature.component.mxGraph.model.MxCell;
import com.nature.component.mxGraph.model.MxGeometry;
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.template.model.Template;
import com.nature.component.template.service.ITemplateService;
import com.nature.domain.flow.FlowDomain;
import com.nature.domain.flow.PathsDomain;
import com.nature.domain.flow.PropertyDomain;
import com.nature.domain.flow.StopsDomain;
import com.nature.domain.mxGraph.MxCellDomain;
import com.nature.domain.mxGraph.MxGeometryDomain;
import com.nature.domain.mxGraph.MxGraphModelDomain;
import com.nature.mapper.template.TemplateMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (null != offset && null != limit) {
            Page page = PageHelper.startPage(offset, limit);
            templateMapper.findTemPlateListPage(param);
            rtnMap = PageHelperUtils.setDataTableParam(page, rtnMap);
        }
        return JsonUtils.toJsonNoException(rtnMap);
    }

}
