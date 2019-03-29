package com.nature.component.flow.service.impl;

import com.nature.component.flow.model.PropertyTemplate;
import com.nature.component.flow.model.StopGroup;
import com.nature.component.flow.model.StopsTemplate;
import com.nature.component.flow.service.IStopGroupService;
import com.nature.component.flow.vo.PropertyTemplateVo;
import com.nature.component.flow.vo.StopGroupVo;
import com.nature.component.flow.vo.StopsTemplateVo;
import com.nature.mapper.flow.StopGroupMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class StopGroupServiceImpl implements IStopGroupService {

    @Resource
    StopGroupMapper stopGroupMapper;

    /**
     * 查询所有组和它下的所有stops
     *
     * @return
     */
    @Override
    public List<StopGroupVo> getStopGroupAll() {
        List<StopGroupVo> stopGroupVoList = null;
        List<StopGroup> stopGroupList = stopGroupMapper.getStopGroupList();
        if (null != stopGroupList && stopGroupList.size() > 0) {
            stopGroupVoList = new ArrayList<StopGroupVo>();
            for (StopGroup stopGroup : stopGroupList) {
                if (null != stopGroup) {
                    StopGroupVo stopGroupVo = new StopGroupVo();
                    BeanUtils.copyProperties(stopGroup, stopGroupVo);
                    List<StopsTemplate> stopsTemplateList = stopGroup.getStopsTemplateList();
                    if (null != stopsTemplateList && stopsTemplateList.size() > 0) {
                        List<StopsTemplateVo> stopsTemplateVoList = new ArrayList<StopsTemplateVo>();
                        for (StopsTemplate stopsTemplate : stopsTemplateList) {
                            if (null != stopsTemplate) {
                                StopsTemplateVo stopsTemplateVo = new StopsTemplateVo();
                                BeanUtils.copyProperties(stopsTemplate, stopsTemplateVo);
                                List<PropertyTemplate> properties = stopsTemplate.getProperties();
                                if (null != properties && properties.size() > 0) {
                                    List<PropertyTemplateVo> propertiesVo = new ArrayList<PropertyTemplateVo>();
                                    for (PropertyTemplate propertyTemplate : properties) {
                                        if (null != propertiesVo) {
                                            PropertyTemplateVo propertyTemplateVo = new PropertyTemplateVo();
                                            BeanUtils.copyProperties(propertyTemplate, propertyTemplateVo);
                                            propertiesVo.add(propertyTemplateVo);
                                        }
                                    }
                                    stopsTemplateVo.setPropertiesVo(propertiesVo);
                                }
                                stopsTemplateVoList.add(stopsTemplateVo);
                            }
                        }
                        stopGroupVo.setStopsTemplateVoList(stopsTemplateVoList);
                    }
                    stopGroupVoList.add(stopGroupVo);
                }
            }
        }
        return stopGroupVoList;
    }

}
