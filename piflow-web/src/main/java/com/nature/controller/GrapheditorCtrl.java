package com.nature.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.nature.component.mxGraph.model.MxGraphModel;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.StopGroup;
import com.nature.component.workFlow.service.FlowService;
import com.nature.component.workFlow.service.StopGroupService;

@Controller
@RequestMapping("/flow/*")
public class GrapheditorCtrl {

	/**
	 * 引入日志，注意都是"org.slf4j"包下
	 */
	Logger logger = LoggerUtil.getLogger();

	@Autowired
	private FlowService flowService;

	@Autowired
	private StopGroupService stopGroupService;

	private MxGraphModel mxGraphModel = null;

	private String saveLoadId = "";

	@RequestMapping("/grapheditor")
	public String kitchenSink(Model model, String load) {
		// 判斷是否存在Flow的id(load),如果存在則加載，否則生成UUID返回返回頁面
		if (StringUtils.isNotBlank(load)) {
			// 左側的組和stops
			List<StopGroup> groupsList = stopGroupService.getStopGroupAll();
			model.addAttribute("groupsList", groupsList);
			Flow flowById = flowService.getFlowById(load);
			// 把查詢出來的Flow轉爲XML
			String saveXml = FlowXmlUtils.mxGraphModelToXml(flowToMxGraphModel(flowById));
			model.addAttribute("xmlDate", saveXml);
			model.addAttribute("load", load);
		} else {
			// 生成32位UUID
			load = Utils.getUUID32();
			return "redirect:grapheditor?load=" + load;
		}
		return "grapheditor/index";
	}

	@RequestMapping("/open")
	public String open(Model model) {
		model.addAttribute("josnStr", "");
		return "/grapheditor/open";
	}

	@RequestMapping("/table")
	public String runningList(Model model) {
		model.addAttribute("say", "say hello spring boot !!!!!");
		model.addAttribute("sss", "ss后台返回数据ss");
		return "/charisma/table";
	}

	@RequestMapping("/index")
	public String index(Model model) {
		model.addAttribute("now", "say hello spring boot !!!!!");
		return "/charisma/index";
	}

	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("now", "say hello spring boot !!!!!");
		return "/charisma/login";
	}

	@RequestMapping("/tour")
	public String tour(Model model) {
		model.addAttribute("now", "say hello spring boot !!!!!");
		return "/charisma/tour";
	}

	@RequestMapping("/ui")
	public String ui(Model model) {
		model.addAttribute("now", "say hello spring boot !!!!!");
		return "/charisma/ui";
	}

	@RequestMapping("/errorPage")
	public String error(Model model) {
		model.addAttribute("now", "say hello spring boot !!!!!");
		return "/charisma/errorPage";
	}

	@RequestMapping("/saveData")
	@ResponseBody
	public String saveData(HttpServletRequest request, Model model) {
		String rtnStr = "0";
		String imageXML = request.getParameter("imageXML");
		String loadId = request.getParameter("load");
		if (StringUtils.isNotBlank(imageXML) && StringUtils.isNotBlank(loadId)) {
			// 把页面传來的XML转为FLOW
			mxGraphModel = FlowXmlUtils.xmlToMxGraphModel(imageXML);
			// 根据Flow的id查询
			Flow flowById = flowService.getFlowById(loadId);
			if (null != flowById) {
				logger.info("在'" + loadId + "'的基礎上保存");
			} else {
				logger.info("新建");
				int addFlow = flowService.addFlow(flowById);
				// addFlow不为空且ReqRtnStatus的值为true,则保存成功
				rtnStr = addFlow;
			}
		}
		return rtnStr;
	}

	private MxGraphModel flowToMxGraphModel(Flow flow) {
		return mxGraphModel;
	}

}
