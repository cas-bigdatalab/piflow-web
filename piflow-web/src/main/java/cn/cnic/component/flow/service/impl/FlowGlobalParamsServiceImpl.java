package cn.cnic.component.flow.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.cnic.common.constant.MessageConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.cnic.base.utils.JsonUtils;
import cn.cnic.base.utils.PageHelperUtils;
import cn.cnic.base.utils.ReturnMapUtils;
import cn.cnic.component.flow.domain.FlowGlobalParamsDomain;
import cn.cnic.component.flow.entity.FlowGlobalParams;
import cn.cnic.component.flow.service.IFlowGlobalParamsService;
import cn.cnic.component.flow.utils.FlowGlobalParamsUtils;
import cn.cnic.controller.requestVo.FlowGlobalParamsVoRequest;
import cn.cnic.controller.requestVo.FlowGlobalParamsVoRequestAdd;

@Service
public class FlowGlobalParamsServiceImpl implements IFlowGlobalParamsService {

    private final FlowGlobalParamsDomain flowGlobalParamsDomain;

    @Autowired
    public FlowGlobalParamsServiceImpl(FlowGlobalParamsDomain flowGlobalParamsDomain) {
        this.flowGlobalParamsDomain = flowGlobalParamsDomain;
    }

    @Override
	public String addFlowGlobalParams(String username, FlowGlobalParamsVoRequestAdd globalParamsVo) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (null == globalParamsVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param name is empty");
        }
        if (StringUtils.isBlank(globalParamsVo.getName())) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null");
        }
        FlowGlobalParams globalParams = FlowGlobalParamsUtils.setFlowGlobalParamsBasicInformation(null, true, username);
        // copy
        BeanUtils.copyProperties(globalParamsVo, globalParams);
        // set update info
        globalParams.setLastUpdateDttm(new Date());
        globalParams.setLastUpdateUser(username);
        int affectedRows = flowGlobalParamsDomain.addFlowGlobalParams(globalParams);
        if (affectedRows <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("save failed");
        }
		return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("globalParamsId", globalParams.getId());
	}

	@Override
	public String updateFlowGlobalParams(String username, boolean isAdmin, FlowGlobalParamsVoRequest globalParamsVo) throws Exception {
        if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (null == globalParamsVo) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("param name is empty");
        }
        String id = globalParamsVo.getId();
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null");
        }
        FlowGlobalParams globalParamsById = flowGlobalParamsDomain.getFlowGlobalParamsById(username, isAdmin, id);
        if (null == globalParamsById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null");
        }
        // copy
        BeanUtils.copyProperties(globalParamsVo, globalParamsById);
		int affectedRows = flowGlobalParamsDomain.updateFlowGlobalParams(globalParamsById);
		if (affectedRows <= 0) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("update failed");
        }
		return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
	}
    
    @Override
    public String deleteFlowGlobalParamsById(String username, boolean isAdmin, String id) {
    	int affectedRows = flowGlobalParamsDomain.updateEnableFlagById(username, id, false);
    	if (affectedRows <= 0) {
    		return ReturnMapUtils.setFailedMsgRtnJsonStr("delete failed");
        }
    	return ReturnMapUtils.setSucceededMsgRtnJsonStr(MessageConfig.SUCCEEDED_MSG());
    }

    /**
     * Paging query FlowGlobalParams
     *
     * @param username
     * @param isAdmin
     * @param offset   Number of pages
     * @param limit    Number of pages per page
     * @param param    search for the keyword
     * @return
     */
    @Override
    public String getFlowGlobalParamsListPage(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
        if (null == offset || null == limit) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr(MessageConfig.ERROR_MSG());
        }
        Page<FlowGlobalParams> page = PageHelper.startPage(offset, limit,"crt_dttm desc");
        flowGlobalParamsDomain.getFlowGlobalParamsListParam(username, isAdmin, param);
        Map<String, Object> rtnMap = ReturnMapUtils.setSucceededMsg(MessageConfig.SUCCEEDED_MSG());
        return PageHelperUtils.setLayTableParamRtnStr(page, rtnMap);
    }
    
    /**
     * Paging query FlowGlobalParams
     *
     * @param username
     * @param isAdmin
     * @param param    search for the keyword
     * @return
     */
    @Override
    public String getFlowGlobalParamsList(String username, boolean isAdmin, String param) {
        List<FlowGlobalParams> flowGlobalParamsListParam = flowGlobalParamsDomain.getFlowGlobalParamsListParam(username, isAdmin, param);
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("data", flowGlobalParamsListParam);
    }

	@Override
	public String getFlowGlobalParamsById(String username, boolean isAdmin, String id) {
		if (StringUtils.isBlank(username)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
        }
        if (StringUtils.isBlank(id)) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null");
        }
        FlowGlobalParams globalParamsById = flowGlobalParamsDomain.getFlowGlobalParamsById(username, isAdmin, id);
        if (null == globalParamsById) {
            return ReturnMapUtils.setFailedMsgRtnJsonStr("data is null");
        }
        return ReturnMapUtils.setSucceededCustomParamRtnJsonStr("globalParams", globalParamsById);
	}

}
