package com.nature.controller;

import com.nature.base.util.JsonUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.component.workFlow.service.IPathsService;
import com.nature.component.workFlow.vo.PathsVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/path/*")
public class PathCtrl {

    /**
     * 引入日志，注意都是"org.slf4j"包下
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
	public String getStopGroup(String fid, String id) {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put("code", "0");
		if (StringUtils.isNotBlank(fid) && StringUtils.isNotBlank(id)) {
			PathsVo queryInfo = iPathsService.getPathsByFlowIdAndPageId(fid, id);
			if (null != queryInfo) {
				rtnMap.put("code", "1");
				rtnMap.put("queryInfo", queryInfo);
				rtnMap.put("errMsg", "成功");
			}else {
				rtnMap.put("errMsg", "没有查询到paths的信息");
				logger.warn("没有查询到paths的信息");
			}
		}else {
			rtnMap.put("errMsg", "参数fid或id为空");
			logger.warn("参数fid或id为空");
		}
		return JsonUtils.toJsonNoException(rtnMap);
	}
     
     
}
