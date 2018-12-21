package com.nature.component.mxGraph.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.base.util.LoggerUtil;
import com.nature.component.mxGraph.service.IMxGraphService;
import com.nature.mapper.mxGraph.MxGeometryMapper;

@Service
public class MxGraphServiceImpl implements IMxGraphService {

	Logger logger = LoggerUtil.getLogger();

	@Autowired
	private MxGeometryMapper mxGeometryMapper;

	@Override
	public int deleteMxGraphById(String id){
		return mxGeometryMapper.updateEnableFlagById(id);
	}
 
}
