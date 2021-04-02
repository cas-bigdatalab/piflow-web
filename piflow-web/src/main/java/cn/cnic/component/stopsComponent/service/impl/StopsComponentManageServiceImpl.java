package cn.cnic.component.stopsComponent.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.component.stopsComponent.domain.StopsComponentManageDomain;
import cn.cnic.component.stopsComponent.model.StopsComponentManage;
import cn.cnic.component.stopsComponent.service.IStopsComponentManageService;
import cn.cnic.component.stopsComponent.utils.StopsComponentManageUtils;
import cn.cnic.controller.requestVo.UpdatestopsComponentIsShow;

@Service
public class StopsComponentManageServiceImpl implements IStopsComponentManageService {

	Logger logger = LoggerUtil.getLogger();

	@Resource
	private StopsComponentManageDomain stopsComponentManageDomain;

	@Override
	public String updateStopsComponentsIsShow(String username, boolean isAdmin, UpdatestopsComponentIsShow[] stopsManageList) throws Exception {
		if (!isAdmin) {
			return ReturnMapUtils.setFailedMsgRtnJsonStr("Permission error");
		}
		if (null == stopsManageList || stopsManageList.length <= 0) {
			return ReturnMapUtils.setFailedMsgRtnJsonStr("stopsManageList is null");
		}
		for (UpdatestopsComponentIsShow stopsManage : stopsManageList) {
			StopsComponentManage stopsComponentManage = stopsComponentManageDomain.getStopsComponentManageByBundleAndGroup(stopsManage.getBundle(), stopsManage.getStopsGroups());
			if (null == stopsComponentManage) {
				stopsComponentManage = StopsComponentManageUtils.stopsComponentManageNewNoId(username);
				stopsComponentManage.setBundle(stopsManage.getBundle());
				stopsComponentManage.setStopsGroups(stopsManage.getStopsGroups());
			}
			stopsComponentManage.setIsShow(stopsManage.getIsShow());
			stopsComponentManageDomain.saveOrUpdeate(stopsComponentManage);
		}
		return ReturnMapUtils.setSucceededMsgRtnJsonStr(ReturnMapUtils.SUCCEEDED_MSG);
	}
	
    /**
     * updateStopsComponentsIsShow
     * 
     * @param username
     * @param isAdmin
     * @param stopsManage
     * @return
     * @throws Exception
     */
	@Override
    public String updateStopsComponentIsShow(String username, boolean isAdmin, UpdatestopsComponentIsShow stopsManage) throws Exception {
		if (!isAdmin) {
			return ReturnMapUtils.setFailedMsgRtnJsonStr("Permission error");
		}
		if (null == stopsManage) {
			return ReturnMapUtils.setFailedMsgRtnJsonStr("stopsManageList is null");
		}
		StopsComponentManage stopsComponentManage = stopsComponentManageDomain.getStopsComponentManageByBundleAndGroup(stopsManage.getBundle(), stopsManage.getStopsGroups());
		if (null == stopsComponentManage) {
			stopsComponentManage = StopsComponentManageUtils.stopsComponentManageNewNoId(username);
			stopsComponentManage.setBundle(stopsManage.getBundle());
			stopsComponentManage.setStopsGroups(stopsManage.getStopsGroups());
		}
		stopsComponentManage.setIsShow(stopsManage.getIsShow());
		stopsComponentManageDomain.saveOrUpdeate(stopsComponentManage);
		return ReturnMapUtils.setSucceededMsgRtnJsonStr(ReturnMapUtils.SUCCEEDED_MSG);
	}

}
