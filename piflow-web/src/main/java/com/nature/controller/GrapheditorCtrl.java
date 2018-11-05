package com.nature.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.nature.base.util.JsonUtils;
import com.nature.third.service.GetGroupsAndStops;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nature.base.util.FlowXmlUtils;
import com.nature.base.util.LoggerUtil;
import com.nature.base.util.Utils;
import com.nature.base.vo.StatefulRtnBase;
import com.nature.component.mxGraph.vo.MxGraphModelVo;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.FlowInfoDb;
import com.nature.component.workFlow.model.StopGroup;
import com.nature.component.workFlow.service.IFlowService;
import com.nature.component.workFlow.service.IStopGroupService;

/**
 * 画板的ctrl
 */
@Controller
@RequestMapping("/grapheditor/*")
public class GrapheditorCtrl {

	/**
	 * @Title 引入日志，注意都是"org.slf4j"包下
	 */
	Logger logger = LoggerUtil.getLogger();

	@Autowired
	private IFlowService flowServiceImpl;

	@Autowired
	private IStopGroupService stopGroupServiceImpl;

	@Autowired
	private GetGroupsAndStops getGroupsAndStops;

	@RequestMapping("/home")
	public String kitchenSink(Model model, String load) {
		// 判斷是否存在Flow的id(load),如果存在則加載，否則生成UUID返回返回頁面
		if (StringUtils.isNotBlank(load)) {
			// 左側的組和stops
			List<StopGroup> groupsList = stopGroupServiceImpl.getStopGroupAll();
			model.addAttribute("groupsList", groupsList);
			Flow flowById = flowServiceImpl.getFlowById(load);
			if (null != flowById) {
				FlowInfoDb appVo = flowById.getAppId();
				if (null != appVo) {
					String appId = appVo.getId();
					String state = appVo.getState();
					model.addAttribute("appId", appId);
					model.addAttribute("flowState", state);
				}
			}
			// 把查詢出來的Flow轉爲XML
			String loadXml = FlowXmlUtils.mxGraphModelToXml(FlowXmlUtils.flowToMxGraphModelVo(flowById));
			model.addAttribute("xmlDate", loadXml);
			model.addAttribute("load", load);
		} else {
			// 生成32位UUID
			load = Utils.getUUID32();
			return "redirect:/grapheditor/home?load=" + load;
		}
		return "grapheditor/index";
	}

	@RequestMapping("/open")
	public String open(Model model) {
		model.addAttribute("josnStr", "");
		return "grapheditor/open";
	}

	@RequestMapping("/saveData")
	@ResponseBody
	public String saveData(HttpServletRequest request, Model model) {
		String rtnStr = "0";
		String imageXML = request.getParameter("imageXML");
		String loadId = request.getParameter("load");
		if (StringUtils.isNotBlank(imageXML) && StringUtils.isNotBlank(loadId)) {
			// 把页面传來的XML转为mxGraphModel
			MxGraphModelVo xmlToMxGraphModel = FlowXmlUtils.xmlToMxGraphModel(imageXML);
			StatefulRtnBase addFlow = flowServiceImpl.saveOrUpdateFlowAll(xmlToMxGraphModel, loadId);
			// addFlow不为空且ReqRtnStatus的值为true,则保存成功
			if (null != addFlow && addFlow.isReqRtnStatus()) {
				rtnStr = "1";
			}
		}
		return rtnStr;
	}

	@RequestMapping("/reloadStops")
	@ResponseBody
	public String reloadStops(String load){
		Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("code", "0");
		getGroupsAndStops.addGroupAndStopsList();
		rtnMap.put("code","1");
		rtnMap.put("load",load);
		return JsonUtils.toJsonNoException(rtnMap);
	}

}
