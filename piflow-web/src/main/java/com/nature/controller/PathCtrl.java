package com.nature.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nature.base.util.LoggerUtil;
import com.nature.component.workFlow.service.IPathsService;
import com.nature.component.workFlow.vo.PathsVo;

@RestController
@RequestMapping("/path/*")
public class PathCtrl {

    /**
     * @Title 引入日志，注意都是"org.slf4j"包下
     */
    Logger logger = LoggerUtil.getLogger();

    @Autowired
	IPathsService iPathsService;

    /**
     * 根据flowId 和 pageId 查询path
     * @param fid
     * @param id
     * @return
     */
	@RequestMapping("/queryPathInfo")
	public PathsVo getStopGroup(String fid,String id) {
		if (StringUtils.isNotBlank(fid) && StringUtils.isNotBlank(id)) {
			PathsVo queryInfo = iPathsService.getPathsByFlowIdAndPageId(fid, id);
			if (null != queryInfo) {
				return queryInfo;
			}
		}
		return null;
	}
     
     
}
