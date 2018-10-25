package com.nature.third.impl;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.nature.base.util.HttpUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.common.constant.SysParamsCache;
import com.nature.third.inf.IGetAllGroups;

@Component
public class GetAllGroupsImpl implements IGetAllGroups {

	Logger logger = LoggerUtil.getLogger();

	@Override
	public void getAllGroup() {
		String sendGetData = HttpUtils.doGet(SysParamsCache.STOP_GROUPS_URL(), null);
		logger.info("返回信息：" + sendGetData);
	}

}
