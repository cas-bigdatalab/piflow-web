package com.nature.component.mxGraph.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nature.base.util.LoggerUtil;
import com.nature.component.mxGraph.service.MxCellService;
import com.nature.mapper.mxGraph.MxCellMapper;

@Service
public class MxCellServiceImpl implements MxCellService {

	Logger logger = LoggerUtil.getLogger();

	@Autowired
	private MxCellMapper mxCellMapper;

	@Override
	public int deleteMxCellById(String id) {
		return mxCellMapper.deleteMxCellById(id);
	}
 
}
