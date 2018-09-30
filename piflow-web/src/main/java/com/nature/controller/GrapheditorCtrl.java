package com.nature.controller;

import java.util.Date;
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
import com.nature.base.util.UUIDUtils;
import com.nature.component.workFlow.model.Flow;
import com.nature.component.workFlow.model.Paths;
import com.nature.component.workFlow.model.StopGroup;
import com.nature.component.workFlow.model.Stops;
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

	private String saveXml = "";

	private String saveLoadId = "";

	@RequestMapping("/grapheditor")
	public String kitchenSink(Model model, String load) {
		// 判斷是否存在Flow的id(load),如果存在則加載，否則生成UUID返回返回頁面
		if (StringUtils.isNotBlank(load)) {
			// 左側的組和stops
			List<StopGroup> groupsList = stopGroupService.getStopGroupAll();
			model.addAttribute("groupsList", groupsList);
			if (load.equals(saveLoadId)) {
				model.addAttribute("xmlDate", saveXml);
			} else {
				Flow flowById = flowService.getFlowById(load);
				// 把查詢出來的Flow轉爲XML
				FlowXmlUtils.flowToXml(flowById);
			}
			model.addAttribute("load", load);
		} else {
			// 生成32位UUID
			load = UUIDUtils.getUUID32();
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
		String imageXML = request.getParameter("imageXML");
		String loadId = request.getParameter("load");
		if (StringUtils.isNotBlank(imageXML) && StringUtils.isNotBlank(loadId)) {
			// 把页面传來的XML转为FLOW
			Flow xmlToFlow = FlowXmlUtils.xmlToFlow(imageXML);
			Flow flowById = flowService.getFlowById(loadId);
			if (null != flowById) {
				logger.info("在'" + loadId + "'的基礎上保存");
			} else {
				saveLoadId = loadId;
				flowById = new Flow();
				flowById.setId(loadId);
				logger.info("新建");
			}
			setFlow(xmlToFlow, flowById);
			saveXml = imageXML;
			logger.info(imageXML);
		}
		return "flwoId";
	}

	@RequestMapping("/loadData")
	@ResponseBody
	public String loadData(HttpServletRequest request, Model model) {
		String parameter = saveXml;
		logger.info(parameter);
		model.addAttribute("now", "say hello spring boot !!!!!");
		return parameter;
	}

	private Flow setFlow(Flow xmlToFlow, Flow flow) {
		if (null == xmlToFlow) {
			return null;
		} else {
			if (null == flow) {
				flow = new Flow();
			}
			flow.setAppId(xmlToFlow.getAppId());
			flow.setCrtDttm(new Date());
			flow.setCrtUser(xmlToFlow.getCrtUser());
			flow.setLastUpdateDttm(new Date());
			flow.setLastUpdateUser(xmlToFlow.getLastUpdateUser());
			flow.setName(xmlToFlow.getName());
			flow.setUuid(xmlToFlow.getUuid());
			flow.setVersion(flow.getVersion() + 1);
			List<Paths> paths = xmlToFlow.getPaths();
			List<Stops> stops = xmlToFlow.getStops();
			flow.setPaths(paths);
			flow.setStops(stops);
			return flow;
		}

	}

}
