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
        String[] bundleArr = stopsManage.getBundle();
        String[] stopsGroupsArr = stopsManage.getStopsGroups();
        if (null == bundleArr || null == stopsGroupsArr || bundleArr.length <= 0 || stopsGroupsArr.length <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is error");
        }
        if (bundleArr.length != stopsGroupsArr.length) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param is error");
        }
        for (int i = 0; i < stopsGroupsArr.length; i++) {
            String stopsGroup_i = stopsGroupsArr[i];
            String bundle_i = bundleArr[i];
            StopsComponentManage stopsComponentManage = stopsComponentManageDomain.getStopsComponentManageByBundleAndGroup(bundle_i, stopsGroup_i);
            if (null == stopsComponentManage) {
                stopsComponentManage = StopsComponentManageUtils.stopsComponentManageNewNoId(username);
                stopsComponentManage.setBundle(bundle_i);
                stopsComponentManage.setStopsGroups(stopsGroup_i);
            }
            stopsComponentManage.setIsShow(stopsManage.getIsShow());
            stopsComponentManageDomain.saveOrUpdeate(stopsComponentManage);
        }
        return ReturnMapUtils.setSucceededMsgRtnJsonStr(ReturnMapUtils.SUCCEEDED_MSG);
    }

}
