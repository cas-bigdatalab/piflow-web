package cn.cnic.component.testData.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.cnic.base.util.JsonUtils;
import cn.cnic.base.util.LoggerUtil;
import cn.cnic.base.util.PageHelperUtils;
import cn.cnic.base.util.ReturnMapUtils;
import cn.cnic.component.dataSource.vo.DataSourceVo;
import cn.cnic.component.testData.domain.TestDataDomain;
import cn.cnic.component.testData.service.ITestDataService;

@Service
@Transactional
public class TestDataServiceImpl implements ITestDataService {
	Logger logger = LoggerUtil.getLogger();

	@Autowired
	private TestDataDomain testDataDomain;

	/**
	 * add addTestData
	 *
	 * @param username
	 * @param name
	 * @param loadId
	 * @param templateType
	 * @return
	 */
	@Override
	public String addTestData(String username, String name, String loadId, String templateType) {
		if (StringUtils.isBlank(username)) {
			return ReturnMapUtils.setFailedMsgRtnJsonStr("Illegal users");
		}
		if (StringUtils.isBlank(name)) {
			return ReturnMapUtils.setFailedMsgRtnJsonStr("param name is empty");
		}
		if (StringUtils.isBlank(loadId)) {
			return ReturnMapUtils.setFailedMsgRtnJsonStr("param 'loadId' is empty");
		}
		if (StringUtils.isBlank(templateType)) {
			return ReturnMapUtils.setFailedMsgRtnJsonStr("param 'templateType' is empty");
		}
		return ReturnMapUtils.setSucceededMsgRtnJsonStr("save template success");
	}

	public String getTestDataList(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
		testDataDomain.getTestDataList(isAdmin, username, param);
		return "";
	}

	public String getTestDataSchemaList(String username, boolean isAdmin, Integer offset, Integer limit, String param) {
		return "";
	}

	/**
	 * getDataSourceVoListPage
	 * 
	 * @param username
	 * @param isAdmin
	 * @param offset
	 * @param limit
	 * @param param
	 * @return
	 */
	@Override
	public String getTestDataSchemaValuesCustomList(String username, boolean isAdmin, Integer offset, Integer limit,
			String param) {
		if (!isAdmin && StringUtils.isBlank(username)) {
			return ReturnMapUtils.setFailedMsgRtnJsonStr("illegal user");
		}
		if (null == offset || null == limit) {
			return ReturnMapUtils.setFailedMsgRtnJsonStr("param is error");
		}
		if (null == param) {
			logger.info("test");
		}
		Page<DataSourceVo> page = PageHelper.startPage(offset, limit);
		testDataDomain.getTestDataSchemaValuesCustomList(isAdmin, username, null);
		Map<String, Object> rtnMap = PageHelperUtils.setLayTableParam(page, null);
		rtnMap.put(ReturnMapUtils.KEY_CODE, ReturnMapUtils.SUCCEEDED_CODE);
		return JsonUtils.toJsonNoException(rtnMap);
	}

}
